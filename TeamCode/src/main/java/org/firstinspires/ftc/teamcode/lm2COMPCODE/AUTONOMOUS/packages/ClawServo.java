package org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.packages;

public class ClawServo {
    private static double servoPosition;

    public static void setServoPosition(double servoPosition) {
        ClawServo.servoPosition = servoPosition;
    }

    public static double getServoPosition() {
        return servoPosition;
    }
}