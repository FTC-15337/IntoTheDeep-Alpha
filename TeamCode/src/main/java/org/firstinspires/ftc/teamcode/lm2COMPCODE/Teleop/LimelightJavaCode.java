package org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop;

import com.parshwa.drive.tele.DriveModes;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.parshwa.drive.tele.Drive;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

@TeleOp (name = "LimelightJavaCode")

public class LimelightJavaCode extends LinearOpMode
{
    private RevHubOrientationOnRobot orientation;
    private IMU imu;

    private Limelight3A limelight;

    // Constants for your robot setup (adjust these values to match your system)
    private static final double CAMERA_HEIGHT = 0.2; // Camera height in meters (adjust as needed)
    private static final double TARGET_HEIGHT = 0.13; // Target height in meters (adjust as needed)
    private static final double CAMERA_PITCH = 0; // Camera pitch angle in degrees (adjust as needed)
    LLResult result ;
    double tx;  // Horizontal angle to the AprilTag
    double ty;// Vertical angle to the AprilTag
    double ta; // Tag ID
    double txRadians;
    double distance;

    @Override
    public void runOpMode() throws InterruptedException
    {

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        limelight.start();

        Drive driver = new Drive();
        orientation = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,RevHubOrientationOnRobot.UsbFacingDirection.UP);
        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(orientation));
        driver.change(imu);
        driver.change("RFM","RBM","LFM","LBM");
        driver.change(DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.REVERSE);
        driver.init(hardwareMap,telemetry, DriveModes.MecanumFeildOriented);
        waitForStart();

        telemetry.addLine("Limelight AprilTag detection has started");
        telemetry.update();

        while (opModeIsActive()) {
            limelight.setPollRateHz(100); // Poll rate for Limelight

            result = limelight.getLatestResult();

            if (result != null && result.isValid() || true)
            {

                //Get the horizontal angle (tx) from the Limelight
                tx = result.getTx();
                // Calculate horizontal distance to AprilTag
                double horizontalDistance = calculateHorizontalDistance(tx);

                // Display the result
                telemetry.addData("tx (angle): " , tx);
                telemetry.addData("ty (angle): " , ty);
                telemetry.addData("Horizontal Distance To AprilTag: ", horizontalDistance);
                telemetry.update(); // Updates Telemetry

                if(distance > 0.3)
                {
                    //driver.move(tx , ty , ta); //moves bot by the angles

                }
                else
                {
                    driver.move(0,0,0,0); //stops bot
                }
                if (horizontalDistance > 0.5)
                {
                    telemetry.addLine("Greater than 0.5 meters away from AprilTag");
                }
            }

        }
    }

    // Method that calculates the horizontal distance using tx
    private double calculateHorizontalDistance(double tx)
    {
        // Convert tx from degrees to radians
        double txRadians = Math.toRadians(tx);

        // Calculate horizontal distance from the april tag or whatever its detecting
        // Formula: distance = (target height - camera height) / tan(tx + camera pitch)
        distance = (TARGET_HEIGHT - CAMERA_HEIGHT) / Math.tan(tx);
        // Returns final distance value

        telemetry.addLine().addData("Distance: ", distance);
        //Returns and prints the distance
        return distance;

    }

}