package com.parshwa.drive.auto;

import static com.parshwa.drive.auto.Directions.HeadingDirection;
import static com.parshwa.drive.auto.Directions.XDirection;
import static com.parshwa.drive.auto.Directions.YDirection;
import static com.parshwa.drive.auto.Types.*;

import android.text.style.TtsSpan;

import com.parshwa.drive.tele.Drive;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.opencv.core.Mat;

import java.sql.Array;
import java.util.ArrayList;
import java.util.function.Function;

public class AutoDriverBetaV1 implements AutoDriverInterface {
    private GoBildaPinpointDriver odoComp;
    private Drive movementControler;
    private ArrayList movementIds = new ArrayList();
    private ArrayList typeIds = new ArrayList();
    private double TotalXDiff;
    private double TotalYDiff;
    private double TotalXYDiff;
    @Override
    public void init(HardwareMap hwMP, Drive movementController) {
        this.odoComp = hwMP.get(GoBildaPinpointDriver.class,"odo");
        odoComp.setOffsets(-48.0, 168.0);
        odoComp.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odoComp.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        odoComp.resetPosAndIMU();
        this.movementControler = movementController;
    }

    @Override
    public Pose2D getPosition() {
        Pose2D pos = odoComp.getPosition();
        return new Pose2D(DistanceUnit.MM,pos.getX(DistanceUnit.MM), pos.getY(DistanceUnit.MM), AngleUnit.DEGREES, pos.getHeading(AngleUnit.DEGREES));
    }

    @Override
    public double getVelocity(Directions direction) {
        Pose2D vel = odoComp.getVelocity();
        switch (direction){
            case XDirection:
                return vel.getX(DistanceUnit.MM);
            case YDirection:
                return vel.getY(DistanceUnit.MM);
            case HeadingDirection:
                return vel.getHeading(AngleUnit.DEGREES);
            default:
                return 0.0;
        }
    }

    @Override
    public int lineTo(double XEnd, double YEnd, double MaxVelocity) {
        Pose2D robotPos = getPosition();
        Pose2D finalPos = new Pose2D(DistanceUnit.MM, XEnd, YEnd, AngleUnit.DEGREES, robotPos.getHeading(AngleUnit.DEGREES));
        int id = getId();
        movementIds.add(id,finalPos);
        typeIds.add(id, LineTo);
        TotalXDiff = XEnd - robotPos.getX(DistanceUnit.MM);
        TotalYDiff = YEnd - robotPos.getY(DistanceUnit.MM);
        TotalXYDiff= Math.hypot(TotalYDiff,TotalXDiff);
        return id;
    }
    private int getId(){
        int id = movementIds.size();
        return id;
    }
    public boolean move(int id){
        odoComp.update();
        boolean completed = false;
        Pose2D finalPos = (Pose2D) movementIds.get(id);
        if(typeIds.get(id) == LineTo){
            Pose2D robotPos = odoComp.getPosition();
            double Finalx = finalPos.getX(DistanceUnit.MM);
            double Finaly = finalPos.getY(DistanceUnit.MM);
            double x = robotPos.getX(DistanceUnit.MM);
            double y = robotPos.getY(DistanceUnit.MM);
            double XDiff = Finalx - x;
            double YDiff = Finaly - y;
            double maximum = Math.max(1.0,Math.abs(XDiff));
            maximum = Math.max(maximum, Math.abs(YDiff));
            double newXSpeed = XDiff / maximum;
            double newYSpeed = YDiff / maximum;
            if(Math.abs(XDiff) <= 10.0){
                newXSpeed = 0.0;
            }
            if(Math.abs(YDiff) <= 10.0){
                newYSpeed = 0.0;
            }

            if(newXSpeed == 0.0 && newYSpeed == 0.0){
                completed = true;
            }
            double speed = 1.0;
            double XYDiff = Math.hypot(XDiff,YDiff);
            if(Math.abs(XYDiff) / Math.abs(TotalXYDiff) <= 1.0/4.0){
                speed = 0.5;
            } else if(Math.abs(XYDiff) / Math.abs(TotalXYDiff) <= 1.0/3.0){
                speed = 2.0/3.0;
            } else if(Math.abs(XYDiff) / Math.abs(TotalXYDiff) <= 1.0/2.0){
                speed = 3.0/4.0;
            }
            movementControler.move(newXSpeed, newYSpeed, 0.0,speed);
        } else if(typeIds.get(id) == Rotate){
            Pose2D robotPos = odoComp.getPosition();
            double currentHed = robotPos.getHeading(AngleUnit.DEGREES);
            double targetHed = finalPos.getHeading(AngleUnit.DEGREES);
            currentHed = AngleUnit.normalizeDegrees(currentHed);
            targetHed = AngleUnit.normalizeDegrees(targetHed);
            double headingDiff = targetHed - currentHed;
            double headingSpeed = headingDiff >= 1.0 ? headingDiff / headingDiff : headingDiff;
            movementControler.move(0.0,0.0,headingSpeed,1.0);
        } else if(typeIds.get(id) == CurveTo){
            completed = true;
        } else {
            completed = true;
        }
        return completed;
    }

    @Override
    public int curveTo(double CircleCenterX, double CircleCenterY, double TotalAngle, double MaxAngleVelocity, Directions curveDirection) {
        return 0;
    }
    @Override
    public int rotateRobot(double degrees, Directions rotationDirection) {
        Pose2D robotPos = getPosition();
        Pose2D finalPos;
        if(Directions.LeftRotateDirection == rotationDirection){
            finalPos = new Pose2D(DistanceUnit.MM, robotPos.getX(DistanceUnit.MM), robotPos.getY(DistanceUnit.MM), AngleUnit.DEGREES, robotPos.getHeading(AngleUnit.DEGREES)+degrees);
        } else if(Directions.RightRotateDirection == rotationDirection){
            finalPos = new Pose2D(DistanceUnit.MM, robotPos.getX(DistanceUnit.MM), robotPos.getY(DistanceUnit.MM), AngleUnit.DEGREES, robotPos.getHeading(AngleUnit.DEGREES)-degrees);
        } else {
            finalPos = new Pose2D(DistanceUnit.MM, robotPos.getX(DistanceUnit.MM), robotPos.getY(DistanceUnit.MM), AngleUnit.DEGREES, robotPos.getHeading(AngleUnit.DEGREES));
        }
        int id = getId();
        movementIds.add(id,finalPos);
        typeIds.add(id, Rotate);
        return id;
    }
}
