package org.firstinspires.ftc.teamcode.Interleauge;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.ftccommon.internal.manualcontrol.commands.ServoCommands;

@Disabled
public class Hang extends LinearOpMode {
    private Servo leftServo;


    @Override
    public void runOpMode() throws InterruptedException {
        leftServo = hardwareMap.get(Servo.class, "leftServo");

        waitForStart();

        leftServo.setPosition(1.0);

        sleep(3000);


    }
}