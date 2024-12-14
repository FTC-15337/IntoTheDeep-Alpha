package org.firstinspires.ftc.teamcode.Interleauge;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class Hang {
        private Servo leftServo;
        private double ticksPerSecond;

    public void runOpMode(HardwareMap hwMap) {
        DcMotor servo = hwMap.get(DcMotor.class, "Servo");
    }
}
