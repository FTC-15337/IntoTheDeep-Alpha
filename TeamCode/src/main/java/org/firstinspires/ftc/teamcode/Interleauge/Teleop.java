package org.firstinspires.ftc.teamcode.Interleauge;

import static com.qualcomm.robotcore.util.ElapsedTime.Resolution.MILLISECONDS;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
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

import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.Threads.gamepad1Controls;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.Threads.gamepad2Controls;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.Threads.lights;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.packages.SliderManger;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.packages.servoManger;

@TeleOp(name="teleop")
public class Teleop extends LinearOpMode {
    //driver1 vars
    public Drive driver = new Drive();
    private double SPED = 0;
    public IMU imu;
    private RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.logoDirection, org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.usDirection);

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
        sc.setDirection(DcMotorSimple.Direction.FORWARD);
        sr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sr.setDirection(DcMotorSimple.Direction.FORWARD);


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
    public void safeWaitMilliseconds(double time) {
        ElapsedTime timer = new ElapsedTime(MILLISECONDS);
        timer.reset();
        while (!isStopRequested() && timer.time() < time) {
        }
    }
    /*private void dropSampleActionsForClaw() {
        // Rotate claw to drop angle
        clawRotateServo.setServoPosition(org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SERVOROTATEHIGH);
        // wait until claw is positioned on the top basket drop position
        safeWaitMilliseconds(500);
        // Open the claw
        clawServo.setServoPosition(org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SERVOOPEN);
        // wait until claw drops sample
        safeWaitMilliseconds(500);
        // Rotate claw back to parallel to slider
        clawRotateServo.setServoPosition(org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SERVOROTATEMIDDLE);
        // wait until claw rotation is completed
        safeWaitMilliseconds(500);
        // set claw to closed position
        clawServo.setServoPosition(org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SERVOCLOSE);
    }*/
    /*private void rotateSliderToDownPosition() {
        boolean completed = false;
        sr.setPower(0.0);
        double currentTargetPosOrigin = sr.getTargetPosition();
        double currentPosOrigin = sr.getCurrentPosition();
        while(!completed && !isStopRequested()){
            SM.setPos(org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SLIDEROTATEMIN, -0.5);
            completed = sr.getCurrentPosition() < org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SLIDEROTATEMIN + 10;
            telemetry.addLine("currentTargetPosOrigin " + currentTargetPosOrigin);
            telemetry.addLine("currentPosOrigin " + currentPosOrigin);
            telemetry.addLine("sc.getCurrentPosition(): " + String.valueOf(sr.getCurrentPosition()));
            telemetry.addLine("CONSTANTS.SLIDEROTATEMIN : " + org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SLIDEROTATEMIN  );
            telemetry.addLine("completed: "+  completed);
            telemetry.update();
        }
        sr.setPower(0.0);
        telemetry.addLine("currentTargetPosOrigin " + currentTargetPosOrigin);
        telemetry.addLine("currentPosOrigin " + currentPosOrigin);
        telemetry.addLine("sc.getCurrentPosition(): " + String.valueOf(sr.getCurrentPosition()));
        telemetry.addLine("CONSTANTS.SLIDEROTATEMIN : " + org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SLIDEROTATEMIN  );
        telemetry.addLine("completed: "+  completed);
        telemetry.update();
    }
    */
    /*private void rotateSliderTo90DegreeAngle() {
        boolean completed = false;
        //Rotate slide to 90
        while(!completed && !isStopRequested()){
            SM.setPos(org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SLIDEROTATEMAX, 1);
            completed = sr.getCurrentPosition() < org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SLIDEROTATEMAX + 10 && sr.getCurrentPosition() > org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SLIDEROTATEMAX - 10;
        }
        //ensure slider stays at 90
        sr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sr.setPower(0.1); // TODO: increase if it does not hold at 90
    }
    */
    /*public void dropSampleToHighBasket() {
        rotateSliderTo90DegreeAngle();
        // set claw rotation to be parallel to slider
        clawRotateServo.setServoPosition(org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SERVOROTATEMIDDLE);
        // wait to finish
        safeWaitMilliseconds(100);
        // expand slider to maximum
        expandSliderToTopBasket();
        // drop the sample
        dropSampleActionsForClaw();
        // retract the slider back
        retractSlider();
        // rotate slider to down position
        rotateSliderToDownPosition();
    }
    */
   /* private void expandSliderToTopBasket() {
        boolean completed = false;
        while(!completed && !isStopRequested()){
            SM.setPos2(org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SLIDEEXPANSTIONMAX, 1);
            telemetry.addLine(String.valueOf(sc.getCurrentPosition()));
            telemetry.update();
            // using negative of SLIDEEXPANSTIONMAX because "sc.getCurrentPosition()" returns negative when expanded
            completed = sc.getCurrentPosition() < org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SLIDEEXPANSTIONMAX + 10;
        }
        sc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sc.setPower(-0.1);// TODO: increase if it does not hold slider at max expansion.
    }
    */
    /*private void retractSlider() {
        boolean completed = false;
        sc.setPower(0.0); // reset slider power to zero
        double currentTargetPos = sc.getTargetPosition();
        double currentPos = sc.getCurrentPosition();
        sc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sc.setPower(1);
        while(!completed && !isStopRequested()){
            //SM.setPos2(-CONSTANTS.SLIDEEXPANTIONLOW, -1);
            //sc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            telemetry.addLine("currentTargetPos " + currentTargetPos);
            telemetry.addLine("currentPos " + currentPos);
            telemetry.addLine("sc.getCurrentPosition(): " + String.valueOf(sc.getCurrentPosition()));
            telemetry.addLine("CONSTANTS.SLIDEEXPANTIONLOW : " + org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SLIDEEXPANSTIONLOW);
            telemetry.addLine("completed: "+  completed);
            telemetry.update();
            completed = sc.getCurrentPosition() > org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SLIDEEXPANSTIONLOW - 10;
        }
        telemetry.addLine("currentTargetPos " + currentTargetPos);
        telemetry.addLine("currentPos " + currentPos);
        telemetry.addLine("sc.getCurrentPosition(): " + String.valueOf(sc.getCurrentPosition()));
        telemetry.addLine("CONSTANTS.SLIDEEXPANTIONLOW : " + org.firstinspires.ftc.teamcode.Interleauge.CONSTANTS.SLIDEEXPANSTIONLOW);
        telemetry.addLine("completed: "+  completed);
        telemetry.update();
        sc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sc.setPower(0.0); // reset slider power to zero

    }
     */
    public void dropSampleToHighBasket() {
        rotateSliderTo90DegreeAngle();
        // set claw rotation to be parallel to slider
        clawRotateServo.setServoPosition(org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SERVOROTATEMIDDLE);
        // wait to finish
        safeWaitMilliseconds(100);
        // expand slider to maximum
        expandSliderToTopBasket();
        // drop the sample
        dropSampleActionsForClaw();
        // retract the slider back
        retractSlider();
        // rotate slider to down position
        rotateSliderToDownPosition();
    }
    private void dropSampleActionsForClaw() {
        // Rotate claw to drop angle
        clawRotateServo.setServoPosition(org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SERVOROTATEHIGH);
        // wait until claw is positioned on the top basket drop position
        safeWaitMilliseconds(1000);
        // Open the claw
        clawServo.setServoPosition(org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SERVOOPEN);
        // wait until claw drops sample
        safeWaitMilliseconds(1000);
        // Rotate claw back to parallel to slider
        clawRotateServo.setServoPosition(org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SERVOROTATELOWEST);
        // wait until claw rotation is completed
        safeWaitMilliseconds(1000);
        // set claw to closed position
        clawServo.setServoPosition(org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SERVOCLOSE);
    }

    private void rotateSliderToDownPosition() {
        boolean completed = false;
        sr.setPower(0.0);
        double currentTargetPosOrigin = sr.getTargetPosition();
        double currentPosOrigin = sr.getCurrentPosition();
        while(!completed && !isStopRequested()){
            SM.setPos(org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SLIDEROTATEMIN, -0.5);
            completed = sr.getCurrentPosition() < org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SLIDEROTATEMIN + 10;
            telemetry.addLine("currentTargetPosOrigin " + currentTargetPosOrigin);
            telemetry.addLine("currentPosOrigin " + currentPosOrigin);
            telemetry.addLine("sc.getCurrentPosition(): " + String.valueOf(sr.getCurrentPosition()));
            telemetry.addLine("CONSTANTS.SLIDEROTATEMIN : " + org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SLIDEROTATEMIN  );
            telemetry.addLine("completed: "+  completed);
            telemetry.update();
        }
        sr.setPower(0.0);
        telemetry.addLine("currentTargetPosOrigin " + currentTargetPosOrigin);
        telemetry.addLine("currentPosOrigin " + currentPosOrigin);
        telemetry.addLine("sc.getCurrentPosition(): " + String.valueOf(sr.getCurrentPosition()));
        telemetry.addLine("CONSTANTS.SLIDEROTATEMIN : " + org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SLIDEROTATEMIN  );
        telemetry.addLine("completed: "+  completed);
        telemetry.update();
    }

    private void rotateSliderTo90DegreeAngle() {
        boolean completed = false;
        //Rotate slide to 90
        while(!completed && !isStopRequested()){
            SM.setPos(org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SLIDEROTATEMAX, 1);
            completed = sr.getCurrentPosition() < org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SLIDEROTATEMAX + 10 && sr.getCurrentPosition() > org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SLIDEROTATEMAX - 10;
        }
        //ensure slider stays at 90
        sr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sr.setPower(0.1); // TODO: increase if it does not hold at 90
    }

    private void expandSliderToTopBasket() {
        boolean completed = false;
        while(!completed && !isStopRequested()){
            SM.setPos2(org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SLIDEEXPANSTIONMAX, 1);
            telemetry.addLine(String.valueOf(sc.getCurrentPosition()));
            telemetry.update();
            // using negative of SLIDEEXPANSTIONMAX because "sc.getCurrentPosition()" returns negative when expanded
            completed = sc.getCurrentPosition() < org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SLIDEEXPANSTIONMAX + 10;
        }
        sc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sc.setPower(-0.1);// TODO: increase if it does not hold slider at max expansion.
    }

    private void retractSlider() {
        boolean completed = false;
        sc.setPower(0.0); // reset slider power to zero
        double currentTargetPos = sc.getTargetPosition();
        double currentPos = sc.getCurrentPosition();
        sc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sc.setPower(1);
        while(!completed && !isStopRequested()){
            //SM.setPos2(-CONSTANTS.SLIDEEXPANTIONLOW, -1);
            //sc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            telemetry.addLine("currentTargetPos " + currentTargetPos);
            telemetry.addLine("currentPos " + currentPos);
            telemetry.addLine("sc.getCurrentPosition(): " + String.valueOf(sc.getCurrentPosition()));
            telemetry.addLine("CONSTANTS.SLIDEEXPANTIONLOW : " + org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SLIDEEXPANTIONLOW);
            telemetry.addLine("completed: "+  completed);
            telemetry.update();
            completed = sc.getCurrentPosition() > org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.CONSTANTS.SLIDEEXPANTIONLOW - 10;
        }
        telemetry.addLine("currentTargetPos " + currentTargetPos);
        telemetry.addLine("currentPos " + currentPos);
        telemetry.addLine("sc.getCurrentPosition(): " + String.valueOf(sc.getCurrentPosition()));
        telemetry.addLine("CONSTANTS.SLIDEEXPANTIONLOW : " + CONSTANTS.SLIDEEXPANTIONLOW);
        telemetry.addLine("completed: "+  completed);
        telemetry.update();
        sc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sc.setPower(0.0); // reset slider power to zeroDe
    }
}


// expansion, retraction,