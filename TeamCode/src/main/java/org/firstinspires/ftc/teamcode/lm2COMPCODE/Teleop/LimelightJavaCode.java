package org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp (name = "LimelightJavaCode")

public class LimelightJavaCode extends LinearOpMode
{

    private Limelight3A limelight;

    // Constants for your robot setup (adjust these values to match your system)
    private static final double CAMERA_HEIGHT = 0.2; // Camera height in meters (adjust as needed)
    private static final double TARGET_HEIGHT = 0.13; // Target height in meters (adjust as needed)
    private static final double CAMERA_PITCH = 0; // Camera pitch angle in degrees (adjust as needed)

    @Override
    public void runOpMode() throws InterruptedException
    {

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        limelight.start();
        limelight.start();

        waitForStart();

        while (opModeIsActive()) {
            limelight.setPollRateHz(100);  // Poll rate for Limelight
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid())
            {

                // Get the horizontal angle (tx) from the Limelight
                double tx = result.getTx();  // Horizontal angle to the AprilTag
                double ty = result.getTy(); // Vertical angle to the AprilTag

                // Calculate horizontal distance to AprilTag
                double horizontalDistance = calculateHorizontalDistance(tx);

                // Display the result
                telemetry.addData("tx (angle)", tx);
                telemetry.addData("ty (angle)" , ty);
                telemetry.addData("Horizontal Distance To AprilTag", horizontalDistance);
                telemetry.update();
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
        double distance = (TARGET_HEIGHT - CAMERA_HEIGHT) / Math.tan(txRadians + Math.toRadians(CAMERA_PITCH));
        //Returns final distance value

        telemetry.addLine().addData("distance", distance);

        return distance;
    }
}