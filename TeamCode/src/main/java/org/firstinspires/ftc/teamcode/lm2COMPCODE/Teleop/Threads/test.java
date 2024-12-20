package org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.Threads;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Interleague.CONSTANTS;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.packages.SliderManger;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.packages.servoManger;
import org.firstinspires.ftc.teamcode.Interleague.Teleop;

public class test extends Thread{
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

    public void init(Teleop main){
        this.mainFile = main;
        this.gamepad2 = main.gamepad2;
        this.SM = main.SM;
        this.sc = main.sc;
        this.sr = main.sr;
        this.clawServo = main.clawServo;
        this.clawRotateServo = main.clawRotateServo;
        this.clawRotateServo2 = main.clawRotateServo2;
    }

    public void run(){
        try{
            while(running && !mainFile.isStopRequested()){
                bypassEnabled = gamepad2.back;
                resetEnabled = gamepad2.start;
                if(sc.getCurrentPosition() < -20 && sr.getCurrentPosition() > 600) {
                    SM.move(-gamepad2.left_stick_y - 0.01 > 1.0 ? -gamepad2.left_stick_y : -gamepad2.left_stick_y - 0.01);
                }else{
                    if(sc.getCurrentPosition() > -1100) {
                        SM.move(-gamepad2.left_stick_y);
                    }else{
                        if(-gamepad2.left_stick_y > 0.0) {
                            SM.move(1.0);
                        }else{
                            SM.move(0.0);
                        }
                    }
                }


                // The code below is used for raising everything
                while(gamepad2.left_trigger >= 0.3 && !(gamepad2.back))
                {
                    SM.setPos(CONSTANTS.SLIDEROTATEMAX,1);

                    mainFile.safeWaitMilliseconds(50);

                    SM.setPos2(CONSTANTS.SLIDEEXPANSTIONMAX,1);

                    mainFile.safeWaitMilliseconds(50);

                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEHIGH);

                    mainFile.safeWaitMilliseconds(50);

                    clawServo.setServoPosition(CONSTANTS.SERVOOPEN);
                }

                // The code below is used for bringing everything to it's original position:
                while(gamepad2.right_trigger >= 0.3 && !(gamepad2.back))
                {
                    mainFile.safeWaitMilliseconds(50);

                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATELOWEST);

                    mainFile.safeWaitMilliseconds(50);

                    SM.setPos2(CONSTANTS.SLIDEEXPANTIONLOW,1);

                    mainFile.safeWaitMilliseconds(50);

                    SM.setPos(CONSTANTS.SLIDEROTATEMIN,1);

                }


/*
                if(-gamepad2.right_stick_y <= -0.3 && !gamepad2.back){
                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEMIDDLE);
                    SM.setPos(CONSTANTS.SLIDEROTATEMIN, 0.5);
                }
                if(-gamepad2.right_stick_y >= 0.3 && !gamepad2.back){
                    SM.setPos(CONSTANTS.SLIDEROTATEMAX, 0.5);
                }
                if(sr.getCurrentPosition() > CONSTANTS.SLIDEROTATEMAX - 10 && !(-gamepad2.right_stick_y <= -0.3)){
                    sr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    sr.setPower(0.1);
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
                if(gamepad2.right_bumper){
                    clawServo.setServoPosition(CONSTANTS.SERVOCLOSE);
                }
                if(gamepad2.left_bumper){
                    clawServo.setServoPosition(CONSTANTS.SERVOOPEN);
                }
                /*(if(gamepad2.dpad_left){
                    clawRotateServo2.setServoPosition(0.8);
                }else if(gamepad2.dpad_right){
                    clawRotateServo2.setServoPosition(0.3);
                }else if(gamepad2.dpad_up){
                    clawRotateServo2.setServoPosition(0.1);
                }else if(gamepad2.dpad_down){
                    clawRotateServo2.setServoPosition(0.9);
                }else{

                }*/


                //clawRotateServo2.setServoPosition(CONSTANTS.SERVOROTATE2MID);
            }
        }catch(Exception e){
            mainFile.telemetry.addLine("ERROR");
            mainFile.telemetry.addData("error in test", String.valueOf(e));
            //mainFile.telemetry.addLine(String.valueOf(e));
        }
    }


}
