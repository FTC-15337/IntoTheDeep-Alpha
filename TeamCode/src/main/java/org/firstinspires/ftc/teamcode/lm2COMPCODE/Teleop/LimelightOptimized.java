package org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop;

import com.parshwa.drive.tele.DriveModes;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.parshwa.drive.tele.Drive;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

@Autonomous(name = "LimelightJavaCodeOPTIMIZED")
public class LimelightOptimized extends LinearOpMode {

    private static final double CAMERA_HEIGHT = 0.17; // Camera height in meters
    private static final double TARGET_HEIGHT = 0.13; // Target height in meters
    private static final double CAMERA_PITCH_RADIANS = Math.toRadians(0); // Precomputed pitch angle
    private RevHubOrientationOnRobot orientation;
    private IMU imu;
    private Limelight3A limelight;
    private Drive driver;

    @Override
    public void runOpMode() throws InterruptedException {

        // Initialize hardware
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        imu = hardwareMap.get(IMU.class, "imu");
        orientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        );
        imu.initialize(new IMU.Parameters(orientation));

        driver = new Drive();
        driver.change(imu);
        driver.change("RFM", "RBM", "LFM", "LBM");
        driver.change(
                DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD
        );
        driver.init(hardwareMap, telemetry, DriveModes.MecanumFeildOriented);

        limelight.pipelineSwitch(0);
        limelight.start();

        telemetry.setMsTransmissionInterval(11);
        telemetry.addLine("Initialization complete. Waiting for start...");
        telemetry.update();

        waitForStart();

        telemetry.addLine("Limelight AprilTag detection has started");

        while (opModeIsActive()) {
            limelight.setPollRateHz(100);  // Poll rate for Limelight

            LLResult result = limelight.getLatestResult();
            if (result != null && result.isValid()) {

                double tx = result.getTx();  // Horizontal angle
                double ty = result.getTy();  // Vertical angle
                double ta = result.getTa();  // Area of the tag (for reference)
                double distance = calculateHorizontalDistance(ty);

                telemetry.addData("tx (angle): ", tx);
                telemetry.addData("ty (angle): ", ty);
                telemetry.addData("Horizontal Distance (meters): ", distance);

                if (distance > 0.3) {
                    driver.move(tx, ty, ta); // Moves the bot based on detected angles
                } else {
                    driver.move(0, 0, 0, 0); // Stops the bot
                }

                if (distance > 0.5) {
                    telemetry.addLine("Greater than 0.5 meters away from the target");
                }


            } else {
                telemetry.addLine("No valid result from Limelight.");
                telemetry.update();
            }
        }
    }

    // Helper method for calculating horizontal distance
    private double calculateHorizontalDistance(double ty) {
        double tyRadians = Math.toRadians(ty);
        return (TARGET_HEIGHT - CAMERA_HEIGHT) / Math.tan(tyRadians + CAMERA_PITCH_RADIANS);
    }
}
