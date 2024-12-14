package org.firstinspires.ftc.teamcode.Interleauge;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.AutoTestTeleop;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.packages.SliderManger;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.packages.servoManger;

public class Gamepad2 extends Thread{
    public boolean running = true;
    public boolean resetEnabled = false;
    public boolean bypassEnabled = false;

    public SliderManger SM = new SliderManger();
    public DcMotor sc, sr;
    public servoManger clawServo = new servoManger();
    public servoManger clawRotateServo = new servoManger();
    public servoManger clawRotateServo2 = new servoManger();

    public Gamepad gamepad2;
    private AutoTestTeleop mainFile;

    public void init(AutoTestTeleop main){
        this.mainFile = main;
        this.gamepad2 = main.gamepad2;
        this.SM = main.SM;
        this.sc = main.sc;
        this.sr = main.sr;
        this.clawServo = main.clawServo;
        this.clawRotateServo = main.clawRotateServo;
        this.clawRotateServo2 = main.clawRotateServo2;
        sr.setDirection(DcMotorSimple.Direction.FORWARD);
        sc.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void run(){
        try{
            while(running && !mainFile.isStopRequested()){
                bypassEnabled = gamepad2.back;
                resetEnabled = gamepad2.start;
                if(sc.getCurrentPosition() < -20 && sr.getCurrentPosition() > 600) {
                    // when slider is at 90 degree and expanded then it gets small bit of power to stay up
                    SM.move(-gamepad2.left_stick_y + 0.05 > 1.0 ? -gamepad2.left_stick_y : -gamepad2.left_stick_y + 0.05);
                }else{
                    SM.move(-gamepad2.left_stick_y);
                }
                /*if(gamepad2.left_trigger > 0.1){
                    SM.setPos2(180 , 1);
                }
                if(gamepad2.right_trigger > 0.1){
                    SM.setPos2(0 , -1);
                }*/
                //SM.move(-gamepad2.left_stick_y);
                mainFile.telemetry.addLine(String.valueOf(sc.getCurrentPosition()));
                // The code below is used for raising everything
                // THIS IS CAUSING battery to decrease way to much needs to be revisited DO NOT ENABLE THIS CODE
                while(gamepad2.left_trigger >= 0.3 && !(gamepad2.back))
                {
                    mainFile.dropSampleToHighBasket();
                }
                // THIS IS CAUSING battery to decrease way to much needs to be revisited DO NOT ENABLE THIS CODE
                // The code below is used for bringing everything to it's original position:
                /*while(gamepad2.right_trigger >= 0.3 && !(gamepad2.back))

                {

                    clawServo.setServoPosition(CONSTANTS.SERVOOPEN);

                    mainFile.safeWaitSeconds(50);

                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATELOWEST);

                    mainFile.safeWaitSeconds(50);

                    SM.setPos2(CONSTANTS.SLIDEEXPANSTIONLOW);

                   // while (sc.getCurrentPosition() > CONSTANTS.SLIDEEXPANTIONLOW+10){}

                    mainFile.safeWaitSeconds(50);

                   // clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEMIDDLE);

                    //mainFile.safeWaitSeconds(50);

                    SM.setPos(CONSTANTS.SLIDEROTATEMIN,1);

                   // while (sr.getCurrentPosition() > CONSTANTS.SLIDEROTATEMIN+10){}

                    sc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }*/ // THIS IS CAUSING battery to decrease way to much needs to be revisited DO NOT ENABLE THIS CODE


                while(gamepad2.right_trigger >= 0.3 && !(gamepad2.back))
                {
                    clawServo.setServoPosition(CONSTANTS.SERVOOPEN);

                    mainFile.safeWaitSeconds(50);

                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATELOWEST);

                    mainFile.safeWaitSeconds(50);

                    SM.setPos2(CONSTANTS.SLIDEEXPANSTIONLOW);

                    mainFile.safeWaitSeconds(50);

                    SM.setPos(CONSTANTS.SLIDEROTATEMIN);
                }

                if(-gamepad2.right_stick_y <= -0.3 && !gamepad2.back){
                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEMIDDLE);
                    SM.setPos(CONSTANTS.SLIDEROTATEMIN, 0.5);
                }
                if(-gamepad2.right_stick_y >= 0.3 && !gamepad2.back){
                    SM.setPos(CONSTANTS.SLIDEROTATEMAX, 0.5);
                }
                if(sr.getCurrentPosition() > CONSTANTS.SLIDEROTATEMAX - 10 && !(-gamepad2.right_stick_y <= -0.3)){
                    sr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    sr.setPower(0.01);
                }
                if(gamepad2.b) {
                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATELOW);
                }
                // if(gamepad2.left_stick_y >= 0.3) {
                // SM.move();
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
            if(gamepad2.dpad_left){
                clawRotateServo2.setServoPosition(0.8);
            }else if(gamepad2.dpad_right){
                clawRotateServo2.setServoPosition(0.3);
            }else if(gamepad2.dpad_up){
                clawRotateServo2.setServoPosition(0.1);
            }else if(gamepad2.dpad_down){
                clawRotateServo2.setServoPosition(0.9);
            }else{
                clawRotateServo2.setServoPosition(CONSTANTS.SERVOROTATE2MID);
            }

        }
    catch(Exception e){
        mainFile.telemetry.addLine("ERROR");
        mainFile.telemetry.addLine(String.valueOf(e));
    }
}


}
