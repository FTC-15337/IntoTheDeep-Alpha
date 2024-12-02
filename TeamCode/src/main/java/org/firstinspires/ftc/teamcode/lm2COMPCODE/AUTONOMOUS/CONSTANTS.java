package org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

@Config(value = "autoDONTCHANGE")
public class CONSTANTS {
    public static double SERVOROTATELOWEST = 0.05;
    public static double SERVOROTATELOW = 0.15;
    public static double SERVOROTATEMIDDLE = 0.6;
    public static double SERVOROTATEHIGH = 0.8;
    public static double SERVOOPEN = 0.4;
    public static double SERVOCLOSE = 0.7;
    public static double SERVOROTATE2LEFT90 = 0.5;
    public static double SERVOROTATE2LEFT45 = 0.5;
    public static double SERVOROTATE2MID = 0.5;
    public static double SERVOROTATE2RIGHT45 = 0.5;
    public static double SERVOROTATE2RIGHT90 = 0.5;

    public static int SLIDEEXPANSTIONMAX = 2100;
    public static int SLIDEEXPANTIONLOW = 1;

    public static int SLIDEROTATEMAX = 675;
    public static int SLIDEROTATEMIN = 0;

    public static RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
    public static RevHubOrientationOnRobot.UsbFacingDirection usDirection = RevHubOrientationOnRobot.UsbFacingDirection.UP;
}
