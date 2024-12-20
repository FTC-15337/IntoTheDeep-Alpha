package org.firstinspires.ftc.teamcode.Interleague;

import static com.qualcomm.robotcore.util.ElapsedTime.Resolution.MILLISECONDS;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
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

import org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.Threads.Lights;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.packages.SliderManger;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.packages.servoManger;

@Autonomous(name="left auto")
public class LeftAuto extends LinearOpMode {
    public Lights lighting = new Lights();
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
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
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
        SM.init(sc, sr);

        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.RIGHT, RevHubOrientationOnRobot.UsbFacingDirection.UP);
        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(orientation));
        driver.change(imu);
        driver.change("RFM", "RBM", "LFM", "LBM");
        driver.change(DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.REVERSE);
        driver.init(hardwareMap, telemetry, DriveModes.MecanumRobotOriented);
        autoDriver.init(hardwareMap, driver);
        autoDriver.enableTurn(this);

        //POSITIONS
        /*int DropPos  = autoDriver.lineTo(-300,400,1.0);
        int pickup1  = autoDriver.lineTo(-675,240,1.0);
        int DropPos2 = autoDriver.lineTo(-300,400,1.0);
        int pickup2  = autoDriver.lineTo(-100,100,1.0);
        int DropPos3 = autoDriver.lineTo(-100,100,1.0);
        int pickup3  = autoDriver.lineTo(-100,100,1.0);
        int DropPos4 = autoDriver.lineTo(-100,100,1.0);
         */

        int DropPos = autoDriver.lineTo(-300, 400, 1.0);
        int pickup1 = autoDriver.lineTo(-675, 240, 1.0);
        int DropPos2 = autoDriver.lineTo(-300, 400, 1.0);
        int pickup2 = autoDriver.lineTo(-100, 100, 1.0);
        int DropPos3 = autoDriver.lineTo(-100, 100, 1.0);
        int pickup3 = autoDriver.lineTo(-100, 100, 1.0);
        int DropPos4 = autoDriver.lineTo(-100, 100, 1.0);
        int strafeTo2 = autoDriver.lineTo(-100, 100, 1.0);

        waitForStart();

        //Preload Gahan:
        SM.setPos(CONSTANTS.SLIDEROTATEMAX); //Slider pivots up
        SM.setPos2(CONSTANTS.SLIDEEXPANSTIONMAX); //slider extends
        sleep(25); //wait for 1/4 of a second
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEHIGH); //Claw goes up
        clawRotateServo2.setServoPosition(CONSTANTS.SERVOROTATETOHIGHBASKET); //servo2 rotates in a pos to high basket
        sleep(50); //waits half a sec
        clawServo.setServoPosition(CONSTANTS.SERVOOPEN); // claw opens
        sleep(50); // waits half a sec
        clawServo.setServoPosition(CONSTANTS.SERVOCLOSE); //claw closes
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATELOW); //servo goes low
        clawRotateServo2.setServoPosition(CONSTANTS.SERVOROTATE2LEFT90); // servo2 turns, SERVOROTATE2LEFT90 is a dummy value
        sleep(25); //waits 1/4 of a second
        SM.setPos2(CONSTANTS.SLIDEEXPANSTIONLOW); //slider retracts
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEHIGH); //servo goes low
        clawServo.setServoPosition(CONSTANTS.SERVOOPEN); //claw opens
        sleep(25); //waits 1/4 of a second
        SM.setPos(CONSTANTS.SLIDEROTATEMIN); //slider pivots down


        //Module 1:

        SM.setPos2(CONSTANTS.SLIDEEXPANSTIONMAX); // expand slider
        sleep(50); // wait for 0.5 secs
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATELOWEST); // rotate claw down
        sleep(50);
        clawServo.setServoPosition(CONSTANTS.SERVOCLOSE); // close claw
        sleep(50);
        SM.setPos2(CONSTANTS.SLIDEEXPANSTIONLOW); // retract slider
        sleep(50);
        SM.setPos(CONSTANTS.SLIDEROTATEMAX); // rotate slider
        sleep(50);
        SM.setPos2(CONSTANTS.SLIDEEXPANSTIONMAX); // expand slider
        sleep(50);
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEHIGH); // drop claw back
        sleep(50);
        clawRotateServo2.setServoPosition(CONSTANTS.SERVOROTATETOHIGHBASKET); // rotate claw to basket
        sleep(50);
        clawServo.setServoPosition(CONSTANTS.SERVOOPEN); // open claw
        sleep(50);
        clawServo.setServoPosition(CONSTANTS.SERVOCLOSE); // close claw
        sleep(50);
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATELOWEST); // drop claw in front
        sleep(50);
        clawRotateServo2.setServoPosition(CONSTANTS.CLAWSTARTPOS); // rotate claw to start pos
        sleep(50);
        SM.setPos(CONSTANTS.SLIDEROTATEMIN); // rotate slider down
        sleep(50);
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEHIGH); // drop claw back
        sleep(50);
        SM.setPos2(CONSTANTS.SLIDEEXPANSTIONLOW); // retract slider
        sleep(50);
        clawServo.setServoPosition(CONSTANTS.SERVOOPEN); // open claw


        //Mod 2 Gahan
        autoDriver.move(strafeTo2); //strafe diagonal to get 2nd sample
        sleep(25); //wait 1/4 second
        SM.setPos2(CONSTANTS.SLIDEEXPANSTIONMAX); //slider extends
        sleep(25); //waits 1/4 seconds
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATELOW); //claw goes low
        sleep(10); //waits 1/10 of a second
        clawServo.setServoPosition(CONSTANTS.SERVOCLOSE); //claw closes
        sleep(25); //waits 1/4 seconds
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEHIGH); //claw goes high
        SM.setPos2(CONSTANTS.SLIDEEXPANSTIONLOW); //slider retracts
        sleep(25); //waits 1/4 seconds
        SM.setPos(CONSTANTS.SLIDEROTATEMAX); //slider pivots up
        SM.setPos2(CONSTANTS.SLIDEEXPANSTIONMAX); //slider extends
        sleep(10); //waits 1/10
        clawRotateServo2.setServoPosition(CONSTANTS.SERVOROTATETOHIGHBASKET); //servo turns to the basket
        sleep(50); //waits half a second
        clawServo.setServoPosition(CONSTANTS.SERVOOPEN); //claw opens
        sleep(10); //waits 1/10 of a second
        clawRotateServo2.setServoPosition(CONSTANTS.SERVOROTATE2LEFT45); //claw turns away from basket, SERVOROTATE2LEFT45 is a dummy value
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATELOW); //claw drops
        SM.setPos2(CONSTANTS.SLIDEEXPANSTIONLOW); // slider retracts
        sleep(10); //waits 1/10 seconds
        SM.setPos(CONSTANTS.SLIDEROTATEMIN); //slider pivots down
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEHIGH); //claw goes high
        clawServo.setServoPosition(CONSTANTS.SERVOOPEN); //claw opens







    }

    public void safeWaitSeconds ( double time)
    {
        ElapsedTime timer = new ElapsedTime(MILLISECONDS);
        timer.reset();
        while (!isStopRequested() && timer.time() < time) {
        }
    }
}



        /*clawServo.setServoPosition(CONSTANTS.SERVOCLOSE);
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEMIDDLE);
        clawRotateServo2.setServoPosition(CONSTANTS.SERVOROTATE2MID);

        boolean completed = false;
        while(!completed && !isStopRequested()){
            completed = autoDriver.move(DropPos);
        }
        autoDriver.turnAngle(-45);
        dropSampleToHighBasket();
        clawServo.setServoPosition(CONSTANTS.SERVOOPEN);
        // wait to finish
        safeWaitSeconds(500);
        autoDriver.turnAngle(0);
        clawServo.setServoPosition(CONSTANTS.SERVOOPEN);
        safeWaitSeconds(500);
        completed = false;
        while(!completed && !isStopRequested()){
            completed = autoDriver.move(pickup1);
        }
        clawServo.setServoPosition(CONSTANTS.SERVOCLOSE);
        safeWaitSeconds(500);
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEHIGH);
        safeWaitSeconds(500);
        completed = false;
        while(!completed && !isStopRequested()){
            completed = autoDriver.move(DropPos2);
        }
        autoDriver.turnAngle(-45);
        dropSampleToHighBasket();
        autoDriver.turnAngle(0);


        while(!isStopRequested()){
            telemetry.addLine("DONE");
            telemetry.update();
        }
    }

    public void dropSampleToHighBasket() {
        rotateSliderTo90DegreeAngle();
        // set claw rotation to be parallel to slider
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEMIDDLE);
        // wait to finish
        safeWaitSeconds(100);
        // expand slider to maximum
        expandSliderToTopBasket();
        // drop the sample
        dropSampleActionsForClaw();
        // retract the slider back
        retractSlider();
        clawServo.setServoPosition(CONSTANTS.SERVOOPEN);
        safeWaitSeconds(100);
        // rotate slider to down position
        rotateSliderToDownPosition();
    }
    private void dropSampleActionsForClaw() {
        // Rotate claw to drop angle
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEHIGH);
        // wait until claw is positioned on the top basket drop position
        safeWaitSeconds(500);
        // Open the claw
        clawServo.setServoPosition(CONSTANTS.SERVOOPEN);
        // wait until claw drops sample
        safeWaitSeconds(500);
        // Rotate claw back to parallel to slider
        clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATELOWEST);
        // wait until claw rotation is completed
        safeWaitSeconds(500);
        // set claw to closed position
        clawServo.setServoPosition(CONSTANTS.SERVOCLOSE);
    }

    private void rotateSliderToDownPosition() {
        boolean completed = false;
        sr.setPower(0.0);
        double currentTargetPosOrigin = sr.getTargetPosition();
        double currentPosOrigin = sr.getCurrentPosition();
        while(!completed && !isStopRequested()){
            SM.setPos(CONSTANTS.SLIDEROTATEMIN, -0.5);
            completed = sr.getCurrentPosition() < CONSTANTS.SLIDEROTATEMIN + 10;
            telemetry.addLine("currentTargetPosOrigin " + currentTargetPosOrigin);
            telemetry.addLine("currentPosOrigin " + currentPosOrigin);
            telemetry.addLine("sc.getCurrentPosition(): " + String.valueOf(sr.getCurrentPosition()));
            telemetry.addLine("CONSTANTS.SLIDEROTATEMIN : " + CONSTANTS.SLIDEROTATEMIN  );
            telemetry.addLine("completed: "+  completed);
            telemetry.update();
        }
        sr.setPower(0.0);
        telemetry.addLine("currentTargetPosOrigin " + currentTargetPosOrigin);
        telemetry.addLine("currentPosOrigin " + currentPosOrigin);
        telemetry.addLine("sc.getCurrentPosition(): " + String.valueOf(sr.getCurrentPosition()));
        telemetry.addLine("CONSTANTS.SLIDEROTATEMIN : " + CONSTANTS.SLIDEROTATEMIN  );
        telemetry.addLine("completed: "+  completed);
        telemetry.update();
    }

    private void rotateSliderTo90DegreeAngle() {
        boolean completed = false;
        //Rotate slide to 90
        while(!completed && !isStopRequested()){
            SM.setPos(CONSTANTS.SLIDEROTATEMAX, 1);
            completed = sr.getCurrentPosition() < CONSTANTS.SLIDEROTATEMAX + 10 && sr.getCurrentPosition() > CONSTANTS.SLIDEROTATEMAX - 10;
        }
        //ensure slider stays at 90
        sr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sr.setPower(0.1); // TODO: increase if it does not hold at 90
    }

    private void expandSliderToTopBasket() {
        boolean completed = false;
        while(!completed && !isStopRequested()){
            SM.setPos2(CONSTANTS.SLIDEEXPANSTIONMAX, 1);
            telemetry.addLine(String.valueOf(sc.getCurrentPosition()));
            telemetry.update();
            // using negative of SLIDEEXPANSTIONMAX because "sc.getCurrentPosition()" returns negative when expanded
            completed = sc.getCurrentPosition() < CONSTANTS.SLIDEEXPANSTIONMAX + 10;
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
            telemetry.addLine("CONSTANTS.SLIDEEXPANTIONLOW : " + CONSTANTS.SLIDEEXPANSTIONLOW);
            telemetry.addLine("completed: "+  completed);
            telemetry.update();
            completed = sc.getCurrentPosition() > CONSTANTS.SLIDEEXPANSTIONLOW - 10;
        }
        telemetry.addLine("currentTargetPos " + currentTargetPos);
        telemetry.addLine("currentPos " + currentPos);
        telemetry.addLine("sc.getCurrentPosition(): " + String.valueOf(sc.getCurrentPosition()));
        telemetry.addLine("CONSTANTS.SLIDEEXPANTIONLOW : " + CONSTANTS.SLIDEEXPANSTIONLOW);
        telemetry.addLine("completed: "+  completed);
        telemetry.update();
        sc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sc.setPower(0.0); // reset slider power to zero

    }


    }
}
} */