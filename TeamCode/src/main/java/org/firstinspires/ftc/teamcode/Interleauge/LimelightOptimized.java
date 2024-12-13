package org.firstinspires.ftc.teamcode.Interleauge;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LimelightOptimized {

    private Limelight3A limelight;
    private Telemetry telemetry;

    private static final double CAMERA_HEIGHT = 0.17; // Camera height in meters
    private static final double TARGET_HEIGHT = 0.13; // Target height in meters
    private static final double CAMERA_PITCH_RADIANS = Math.toRadians(0); // Precomputed pitch angle

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    public LLResult getLatestResult() {
        return limelight.getLatestResult();
    }

    public boolean isResultValid(LLResult result) {
        return result != null && result.isValid();
    }

    public double calculateHorizontalDistance(double ty) {
        double tyRadians = Math.toRadians(ty);
        return (TARGET_HEIGHT - CAMERA_HEIGHT) / Math.tan(tyRadians + CAMERA_PITCH_RADIANS);
    }

    public void logData(LLResult result) {
        if (result != null) {
            double tx = result.getTx();
            double ty = result.getTy();
            double distance = calculateHorizontalDistance(ty);

            telemetry.addData("tx (angle): ", tx);
            telemetry.addData("ty (angle): ", ty);
            telemetry.addData("Horizontal Distance (meters): ", distance);
        } else {
            telemetry.addLine("No valid result from Limelight.");
        }
        telemetry.update();
    }
}
