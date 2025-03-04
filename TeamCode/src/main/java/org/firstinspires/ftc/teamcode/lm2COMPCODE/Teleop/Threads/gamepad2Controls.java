package org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.Threads;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static org.firstinspires.ftc.teamcode.Interleague.CONSTANTS.SLIDEROTATEMAX;
import static org.firstinspires.ftc.teamcode.Interleague.CONSTANTS.SLIDERTOHIGHCHAMBER;
import static org.firstinspires.ftc.teamcode.Interleague.CONSTANTS.SLIDERDOWNMAXEXTENTION;
import static org.firstinspires.ftc.teamcode.Interleague.CONSTANTS.SLIDEROTATEMIN;
import static org.firstinspires.ftc.teamcode.Interleague.CONSTANTS.SLIDERTOHIGHCHAMBER;
import static org.firstinspires.ftc.teamcode.Interleague.CONSTANTS.SLIDEVERTICALMAX;
import static org.firstinspires.ftc.teamcode.Interleague.CONSTANTS.SLIDEVERTICALMIN;

import com.parshwa.drive.auto.AutoDriverBetaV1;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Interleague.CONSTANTS;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.packages.SliderManger;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.packages.servoManger;
import org.firstinspires.ftc.teamcode.Interleague.Teleop;



//import javax.swing.JSlider;

public class  gamepad2Controls extends Thread{
    public boolean running = true;
    public boolean resetEnabled = false;
    public boolean bypassEnabled = false;

    public SliderManger SM = new SliderManger();
    public DcMotor sc, sr;
    public servoManger clawServo = new servoManger();
    public servoManger clawRotateServo = new servoManger();
    public servoManger clawRotateServo2 = new servoManger();

    public Gamepad gamepad2;
    private Teleop mainFile;
    private AutoDriverBetaV1 autoDriver = new AutoDriverBetaV1();
    private double times;

    public void init(Teleop main){
        this.mainFile = main;
        this.gamepad2 = main.gamepad2;
        this.SM = main.SM;
        this.sc = main.sc;
        this.sr = main.sr;
        this.clawServo = main.clawServo;
        this.clawRotateServo = main.clawRotateServo;
        this.clawRotateServo2 = main.clawRotateServo2;
        this.autoDriver.init(main.hardwareMap,main.driver);
        times = 0;
        //mainFile.telemetry.addLine("Init of GP2C");
    }

    public void run(){
        try{
            int A = 0;
            int B = 0;
            while(running && !mainFile.isStopRequested()){
                bypassEnabled = gamepad2.back;
                resetEnabled = gamepad2.start;
                if(sc.getCurrentPosition() < -20 && sr.getCurrentPosition() > 600) {
                    // when slider is at 90 degree and expanded then it gets small bit of power to stay up
                        if(sc.getCurrentPosition() < SLIDEVERTICALMAX) //This is to maintain a vertical limit
                        {
                            SM.move(0.0);
                        }
                        else
                        {
                            if (gamepad2.left_stick_y - 0.005 > 1.0)
                            {
                                SM.move(gamepad2.left_stick_y);
                            }
                            else
                            {
                                SM.move(gamepad2.left_stick_y - 0.005); // Org value is -0.05
                            }
                            //SM.move(gamepad2.left_stick_y - 0.05 > 1.0 ? gamepad2.left_stick_y : gamepad2.left_stick_y - 0.05);
                        }
                }else
                {
                    if (sc.getCurrentPosition() < -SLIDERDOWNMAXEXTENTION && (sr.getCurrentPosition() > SLIDEROTATEMIN -10 && sr.getCurrentPosition() < SLIDEROTATEMIN +10))
                    {
                        if(gamepad2.left_stick_y > 0.2){
                            SM.move(1);
                        }else{
                           SM.move(0.1);
                        }
                    }else{
                        if(sc.getCurrentPosition() < -SLIDERDOWNMAXEXTENTION * 1 / 2){
                            SM.move(gamepad2.left_stick_y * 2 / 5);
                        }else{
                            SM.move(gamepad2.left_stick_y);
                        }
                    }
                }

                if(gamepad2.left_trigger >= 0.3)
                {
                    boolean completed = false;
                    clawRotateServo.setServoPosition(0.4);
                    SM.setPos(CONSTANTS.SLIDEROTATEMAX,0.5);
                    mainFile.safeWaitMilliseconds(1000);
                    while(!completed){
                        SM.setPos2(SLIDERTOHIGHCHAMBER, 1);
                        completed = sc.getCurrentPosition() < SLIDERTOHIGHCHAMBER + 10;
                    }
                    sc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    sc.setPower(0.1);

                }
                if(gamepad2.right_trigger >= 0.3){
                    //clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEHIGH);
                    mainFile.safeWaitMilliseconds(500);
                    times += 1;
                    int finalpos = autoDriver.lineTo(times,0,0);
                    int humanpos = autoDriver.lineTo(1200,0,0);
                    boolean completed = false;
                    while(!completed && !mainFile.isStopRequested() && bypassEnabled){
                        completed = autoDriver.move(humanpos);
                    }


                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEMIDDLE);
                    clawServo.setServoPosition(CONSTANTS.SERVOCLOSE);
                    mainFile.safeWaitMilliseconds(500);
                    completed = false;
                    while(!completed && !mainFile.isStopRequested() && bypassEnabled){
                        completed = autoDriver.move(humanpos);
                    }
                }


                if(-gamepad2.right_stick_y <= -0.3 && !gamepad2.back){
                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEMIDDLE);
                    SM.setPos(CONSTANTS.SLIDEROTATEMIN, 0.2);
                }
                if(-gamepad2.right_stick_y >= 0.3 && !gamepad2.back){
                    SM.setPos(SLIDEROTATEMAX, 0.3);
                }
                if(gamepad2.b) {
                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATELOW);
                }
                if(gamepad2.a) {
                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATELOWEST);
                }
                if(gamepad2.y) {
                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEMIDDLE);
                }
                if(gamepad2.x) {
                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEHIGH);
                }
                if(gamepad2.right_bumper){ // close
                    clawServo.setServoPosition(CONSTANTS.SERVOCLOSE);
                    mainFile.telemetry.addLine("Close");
                }
                if(gamepad2.left_bumper) { //open
                    clawServo.setServoPosition(CONSTANTS.SERVOOPEN);
                    mainFile.telemetry.addLine("Open");
                }
                if(gamepad2.dpad_left){
                    clawRotateServo2.setServoPosition(0.7);//This sets the claw to the initial straight position. We have done 0.7 as the claw has an initial movement and this compensates for it.
                }else if(gamepad2.dpad_right){
                    clawRotateServo2.setServoPosition(0.2);
                }else{
                    clawRotateServo2.setServoPosition(CONSTANTS.SERVOROTATE2MID);
                }

            }
        }catch(Exception e){
            mainFile.telemetry.addLine("ERROR");
            //mainFile.telemetry.addLine(String.valueOf(e));
            mainFile.telemetry.addData("error in Gamepad 2 controls", String.valueOf(e));

        }
    }

    public SliderManger getSM() {
        return SM;
    }
}
