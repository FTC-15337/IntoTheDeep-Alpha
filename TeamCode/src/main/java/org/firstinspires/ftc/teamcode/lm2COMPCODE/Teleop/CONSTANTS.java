package org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

@Config
public class CONSTANTS {
    public static double SERVOROTATELOWEST = 0.05;
     public static double SERVOROTATELOW = 0.2;
    public static double SERVOROTATEMIDDLE = 0.57;
    public static double SERVOROTATEHIGH = 0.75;
    public static double SERVOOPEN = 0.4;
    public static double SERVOCLOSE = 0.7;
    public static double SERVOROTATE2LEFT90 = 0.5;
    public static double SERVOROTATE2LEFT45 = 0.5;
    public static double SERVOROTATE2MID = 0.46;
    public static double SERVOROTATE2RIGHT45 = 0.5;
    public static double SERVOROTATE2RIGHT90 = 0.5;

    public static int SLIDEEXPANSTIONMAX = 2159;
    public static int SLIDEEXPANTIONLOW = 0;
    public static int SLIDERDOWNMAXEXTENTION = 900;
    public static int SLIDEVERTICALMAX = 0;
    public static int SLIDEVERTICALMIN = 0;

    public static int SLIDEROTATEMAX = 675;
    public static int SLIDEROTATEMIN = 0;

    public static RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
    public static RevHubOrientationOnRobot.UsbFacingDirection usDirection = RevHubOrientationOnRobot.UsbFacingDirection.UP;

}
