package org.firstinspires.ftc.teamcode.Interleague.FLL_Learning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class LearningFLL extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException{
        while(opModeIsActive()) {
            telemetry.addLine("Hello World");
            telemetry.update();
        }
    }
}
