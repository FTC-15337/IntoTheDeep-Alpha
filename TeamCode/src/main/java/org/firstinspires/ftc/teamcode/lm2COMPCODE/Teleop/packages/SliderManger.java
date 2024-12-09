package org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.packages;

import static org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SLIDERDOWNMAXEXTENTION;
import static org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SLIDEROTATEMIN;

import com.qualcomm.robotcore.hardware.DcMotor;

public class SliderManger {
    private DcMotor rotator;
    private DcMotor controller;

    public void reset(){
        rotator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rotator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void init(DcMotor SlideController, DcMotor SlideRotator)
    {
        // Define Slides
        controller = SlideController;
        rotator = SlideRotator;
        controller.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rotator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rotator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void move(double controllerPower){
        if (controller.getCurrentPosition() > SLIDERDOWNMAXEXTENTION && (rotator.getCurrentPosition() > SLIDEROTATEMIN -10 && rotator.getCurrentPosition() < SLIDEROTATEMIN +10) )
        {
            controllerPower = 0;
        }
        controller.setPower(controllerPower);
    }
    public void setPos(int pos, double power){
        rotator.setPower(power);
        rotator.setTargetPosition(pos);
        rotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void setPos2(int pos, double power){
        controller.setPower(power);
        if (pos > SLIDERDOWNMAXEXTENTION && (rotator.getCurrentPosition() > SLIDEROTATEMIN -10 && rotator.getCurrentPosition() < SLIDEROTATEMIN +10) )
        {
            pos = SLIDERDOWNMAXEXTENTION;
        }
        controller.setTargetPosition(pos);
        controller.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}