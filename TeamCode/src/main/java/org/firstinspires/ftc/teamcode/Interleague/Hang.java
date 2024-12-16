package org.firstinspires.ftc.teamcode.Interleague;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

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