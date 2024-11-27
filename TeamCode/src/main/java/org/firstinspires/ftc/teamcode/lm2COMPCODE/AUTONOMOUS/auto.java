package org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS;

import static com.qualcomm.robotcore.util.ElapsedTime.Resolution.MILLISECONDS;

import com.parshwa.drive.auto.AutoDriverBetaV1;
import com.parshwa.drive.tele.Drive;
import com.parshwa.drive.tele.DriveModes;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.lm2COMPCODE.CONSTANTS;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.Threads.lights;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.packages.SliderManger;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.packages.servoManger;

@Autonomous(name="LM2 LEFT AUTO")
public class auto extends LinearOpMode {
    public lights lighting = new lights();
    public Servo bottomLed;

    private AutoDriverBetaV1 autoDriver = new AutoDriverBetaV1();
    private Drive driver = new Drive();
    private IMU imu;

    private SliderManger SM = new SliderManger();
    private DcMotor sc, sr;
    private servoManger clawServo = new servoManger();
    private servoManger clawRotateServo = new servoManger();
    private servoManger clawRotateServo2 = new servoManger();

    @Override
    public void runOpMode() throws InterruptedException {
        bottomLed = hardwareMap.get(Servo.class, "RGBLED");
        lighting.init2(this);
        lighting.start();

        clawServo.init(hardwareMap, "cs");
        clawRotateServo.init(hardwareMap, "crs");
        clawRotateServo2.init(hardwareMap, "crs2");
        sc = hardwareMap.dcMotor.get("sc");
        sr = hardwareMap.dcMotor.get("sr");
        sc.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        SM.init(sc,sr);

        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,RevHubOrientationOnRobot.UsbFacingDirection.UP);
        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(orientation));
        driver.change(imu);
        driver.change("RFM","RBM","LFM","LBM");
        driver.change(DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.REVERSE);
        driver.init(hardwareMap,telemetry, DriveModes.MecanumRobotOriented);
        autoDriver.init(hardwareMap,driver);
        autoDriver.enableTurn(this);

        //POSITIONS
        int DropPos  = autoDriver.lineTo(-300,400,1.0);
        int pickup1  = autoDriver.lineTo(-100,100,1.0);
        int DropPos2 = autoDriver.lineTo(-100,100,1.0);
        int pickup2  = autoDriver.lineTo(-100,100,1.0);
        int DropPos3 = autoDriver.lineTo(-100,100,1.0);
        int pickup3  = autoDriver.lineTo(-100,100,1.0);
        int DropPos4 = autoDriver.lineTo(-100,100,1.0);

        waitForStart();

        clawServo.setServoPosition(CONSTANTS.SERVOCLOSE);
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEMIDDLE);
        clawRotateServo2.setServoPosition(CONSTANTS.SERVOROTATE2MID);

        boolean completed = false;
        while(!completed && !isStopRequested()){
            completed = autoDriver.move(DropPos);
        }
        autoDriver.turnAngle(-45);
        completed = false;
        while(!completed && !isStopRequested()){
            SM.setPos(CONSTANTS.SLIDEROTATEMAX, 1);
            completed = sr.getCurrentPosition() < CONSTANTS.SLIDEROTATEMAX + 10 && sr.getCurrentPosition() > CONSTANTS.SLIDEROTATEMAX - 10;
        }
        sr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sr.setPower(0.1);
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEMIDDLE);
        safeWaitSeconds(100);
        completed = false;
        while(!completed && !isStopRequested()){
            SM.setPos2(CONSTANTS.SLIDEEXPANSTIONMAX, 1);
            completed = sc.getCurrentPosition() > CONSTANTS.SLIDEEXPANSTIONMAX - 10 && sc.getCurrentPosition() < CONSTANTS.SLIDEEXPANSTIONMAX + 10;
        }
        sc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sc.setPower(0.2);
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEHIGH);
        clawServo.setServoPosition(CONSTANTS.SERVOOPEN);
        safeWaitSeconds(500);
        while(!isStopRequested()){}
    }
    public void safeWaitSeconds(double time) {
        ElapsedTime timer = new ElapsedTime(MILLISECONDS);
        timer.reset();
        while (!isStopRequested() && timer.time() < time) {
        }
    }
}
