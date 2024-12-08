package org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.Threads;

import com.parshwa.drive.auto.AutoDriverBetaV1;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.CONSTANTS;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.packages.SliderManger;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.packages.servoManger;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.Teleop;

public class gamepad2Controls extends Thread{
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
    }

    public void run(){
        try{
            while(running && !mainFile.isStopRequested()){
                bypassEnabled = gamepad2.back;
                resetEnabled = gamepad2.start;
                if(sc.getCurrentPosition() < -20 && sr.getCurrentPosition() > 600) {
                    // when slider is at 90 degree and expanded then it gets small bit of power to stay up
                    SM.move(gamepad2.left_stick_y - 0.05 > 1.0 ? gamepad2.left_stick_y : gamepad2.left_stick_y - 0.05);
                }else{
                    SM.move(gamepad2.left_stick_y);
                    SM.move(-gamepad2.left_stick_y + 0.05 > 1.0 ? -gamepad2.left_stick_y : -gamepad2.left_stick_y + 0.05);
                }



                /*if(gamepad2.left_trigger > 0.1){
                    SM.setPos2(180 , 1);
                }
                if(gamepad2.right_trigger > 0.1){
                    SM.setPos2(0 , -1);
                }*/
                //SM.move(-gamepad2.left_stick_y);
                if(gamepad2.right_trigger >= 0.3){
                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEHIGH);
                    clawServo.setServoPosition(CONSTANTS.SERVOOPEN);
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
                mainFile.telemetry.addLine(String.valueOf(sc.getCurrentPosition()));

                if(gamepad2.left_trigger >= 0.3){
                    mainFile.dropSampleToHighBasket();
                }
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
                    //mainFile.safeWaitSeconds(50);

                    clawServo.setServoPosition(CONSTANTS.SERVOOPEN);

                    mainFile.safeWaitSeconds(50);

                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATELOWEST);

                    mainFile.safeWaitSeconds(50);

                    SM.setPos2(CONSTANTS.SLIDEEXPANTIONLOW,1);

                    while (sc.getCurrentPosition() > CONSTANTS.SLIDEEXPANTIONLOW+10){}

                    mainFile.safeWaitSeconds(50);

                    clawRotateServo.setServoPosition(CONSTANTS.SERVOROTATEMIDDLE);

                    mainFile.safeWaitSeconds(50);

                    SM.setPos(CONSTANTS.SLIDEROTATEMIN,1);

                    while (sr.getCurrentPosition() > CONSTANTS.SLIDEROTATEMIN+10){}

                    sc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }*/ // THIS IS CAUSING battery to decrease way to much needs to be revisited DO NOT ENABLE THIS CODE



                if(-gamepad2.right_stick_y <= -0.3 && !gamepad2.back){
                    clawRotateServo.setServoPosition(org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.CONSTANTS.SERVOROTATEMIDDLE);
                    SM.setPos(org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.CONSTANTS.SLIDEROTATEMIN, 0.5);
                }
                if(-gamepad2.right_stick_y >= 0.3 && !gamepad2.back){
                    SM.setPos(org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.CONSTANTS.SLIDEROTATEMAX, 0.5);
                }
                if(sr.getCurrentPosition() > org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.CONSTANTS.SLIDEROTATEMAX - 10 && !(-gamepad2.right_stick_y <= -0.3)){
                    sr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    sr.setPower(0.01);
                }
                if(gamepad2.b) {
                    clawRotateServo.setServoPosition(org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.CONSTANTS.SERVOROTATELOW);
                }
                if(gamepad2.a) {
                    clawRotateServo.setServoPosition(org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.CONSTANTS.SERVOROTATELOWEST);
                }
                if(gamepad2.y) {
                    clawRotateServo.setServoPosition(org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.CONSTANTS.SERVOROTATEMIDDLE);
                }
                if(gamepad2.x) {
                    clawRotateServo.setServoPosition(org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.CONSTANTS.SERVOROTATEHIGH);
                }
                if(gamepad2.right_bumper){
                    clawServo.setServoPosition(org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.CONSTANTS.SERVOCLOSE);
                }
                if(gamepad2.left_bumper) {
                    clawServo.setServoPosition(org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.CONSTANTS.SERVOOPEN);
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
        }catch(Exception e){
            mainFile.telemetry.addLine("ERROR");
            mainFile.telemetry.addLine(String.valueOf(e));
        }
    }

    public SliderManger getSM() {
        return SM;
    }
}
