package org.firstinspires.ftc.teamcode.Interleauge;

import com.parshwa.drive.tele.Drive;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.parshwa.drive.tele.DriveModes;

@TeleOp(name = "ALIGNWITHAPRILTAG")
public class AlignWithAprilTagMecanum extends LinearOpMode {

    private LimelightOptimized limelightOptimized;
    private Drive driver;

    // Offset for Limelight (meters)
    private static final double LIMELIGHT_X_OFFSET = 0.1; // Horizontal offset of Limelight from robot center
    private static final double APPROACH_DISTANCE = 0.5; // Target distance to stop near the tag (meters)

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize LimelightOptimized
        limelightOptimized = new LimelightOptimized();
        limelightOptimized.init(hardwareMap, telemetry);

        // Initialize mecanum drive
        driver = new Drive();
        driver.change("RFM", "RBM", "LFM", "LBM");
        driver.change(
                DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD
        );
        driver.init(hardwareMap, telemetry, DriveModes.MecanumFeildOriented);

        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelightOptimized.getLatestResult();

            if (limelightOptimized.isResultValid(result)) {
                double tx = result.getTx(); // Horizontal alignment angle
                double ty = result.getTy(); // Vertical alignment angle
                double distance = limelightOptimized.calculateHorizontalDistance(ty); // Distance to tag

                // Calculate mecanum drive speeds
                double strafeSpeed = calculateStrafeSpeed(tx);
                double forwardSpeed = calculateForwardSpeed(distance);

                // Drive the robot
                driver.move(strafeSpeed, forwardSpeed, 0); // 0 for no rotation

                // Log telemetry
                limelightOptimized.logData(result);
                telemetry.addData("Strafe Speed", strafeSpeed);
                telemetry.addData("Forward Speed", forwardSpeed);
            } else {
                telemetry.addLine("No valid AprilTag detected.");
                driver.move(0, 0, 0); // Stop the robot
            }

            telemetry.update();
        }
    }

    // Calculate strafe speed based on horizontal angle (tx)
    private double calculateStrafeSpeed(double tx) {
        double kP = 0.02; // Proportional constant for strafe adjustment
        return -kP * (tx + LIMELIGHT_X_OFFSET); // Adjust for Limelight offset
    }

    // Calculate forward speed based on distance
    private double calculateForwardSpeed(double distance) {
        double kP = 0.1; // Proportional constant for forward adjustment
        double error = distance - APPROACH_DISTANCE; // Error in distance to target
        return -kP * error; // Negative to approach, positive to move away
    }
}
