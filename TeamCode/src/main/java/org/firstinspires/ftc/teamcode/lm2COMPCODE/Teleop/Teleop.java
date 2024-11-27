package org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop;

import static com.qualcomm.robotcore.util.ElapsedTime.Resolution.MILLISECONDS;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.parshwa.drive.tele.Drive;
import com.parshwa.drive.tele.DriveModes;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.lm2COMPCODE.CONSTANTS;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.Threads.lights;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.packages.SliderManger;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.packages.servoManger;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.Threads.gamepad1Controls;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.Threads.gamepad2Controls;
@TeleOp(name="teleop")
public class Teleop extends LinearOpMode {
    //driver1 vars
    public Drive driver = new Drive();
    private double SPED = 0;
    public IMU imu;
    private RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(CONSTANTS.logoDirection,CONSTANTS.usDirection);

    //driver2 vars
    public SliderManger SM = new SliderManger();
    public DcMotor sc, sr;
    public servoManger clawServo = new servoManger();
    public servoManger clawRotateServo = new servoManger();
    public servoManger clawRotateServo2 = new servoManger();

    //Thread vars
    public gamepad1Controls gampad1Thread = new gamepad1Controls();
    public gamepad2Controls gamepad2Thread = new gamepad2Controls();
    public lights lighting = new lights();

    //LEDS
    public Servo bottomLed;
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        //driver1 inits
        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(orientation));
        driver.change(imu);
        driver.change("RFM","RBM","LFM","LBM");
        driver.change(DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.REVERSE);
        driver.init(hardwareMap,telemetry, DriveModes.MecanumFeildOriented);

        //driver2 inits
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

        //LEDS
        bottomLed = hardwareMap.get(Servo.class, "RGBLED");
        lighting.init(this);

        //Thread inits
        gampad1Thread.init(this);
        gamepad2Thread.init(this);
        gampad1Thread.add2Controls();
        gampad1Thread.start();
        gamepad2Thread.start();
        waitForStart();
        lighting.start();
        while(!isStopRequested()){
        }
    }
    public void reset(){
        sr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sc.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        imu.resetYaw();
    }
    public void safeWaitSeconds(double time) {
        ElapsedTime timer = new ElapsedTime(MILLISECONDS);
        timer.reset();
        while (!isStopRequested() && timer.time() < time) {
        }
    }
}
