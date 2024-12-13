package org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.Threads;

import org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.AutoTestTeleop;
import org.firstinspires.ftc.teamcode.Interleauge.LeftAuto;
import org.firstinspires.ftc.teamcode.Interleauge.RightAuto;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.TestAuto;

public class Lights extends Thread{
    private AutoTestTeleop mainFile;
    private LeftAuto mainFile2;
    private RightAuto mainFile3;
    private TestAuto mainFile4;
    private boolean which;
    private boolean which2;
    private boolean which4;
    public void init(AutoTestTeleop main){
        mainFile = main;
        which = true;
        which2 = false;
    }
    public void init2(LeftAuto main){
        mainFile2 = main;
        which = false;
        which2 = false;
    }

    public void init4(TestAuto main){
        mainFile4 = main;
        which = false;
        which2 = false;
        which4= true;
    }
    public void init3(RightAuto main){
        mainFile3 = main;
        which = false;
        which2 = true;
    }
    public void run(){
        if(which && !which2) {
            double ledPos = 0.0;
            double maxLedPos = 0.8;
            double minLedPos = 0.27;
            double direction = 0.0001;
            while (!mainFile.isStopRequested()) {
                ledPos += direction;
                if (ledPos > maxLedPos) {
                    direction = -direction;
                    ledPos = maxLedPos;
                }
                if (ledPos < minLedPos) {
                    direction = -direction;
                    ledPos = minLedPos;
                }
                mainFile.bottomLed.setPosition(ledPos);
            }
        }else if(!which && !which2){
            double ledPos = 0.0;
            double maxLedPos = 0.8;
            double minLedPos = 0.27;
            double direction = 0.0001;
            while (!mainFile2.isStopRequested()) {
                ledPos += direction;
                if (ledPos > maxLedPos) {
                    direction = -direction;
                    ledPos = maxLedPos;
                }
                if (ledPos < minLedPos) {
                    direction = -direction;
                    ledPos = minLedPos;
                }
                mainFile2.bottomLed.setPosition(ledPos);
            }
        }else{
            double ledPos = 0.0;
            double maxLedPos = 0.8;
            double minLedPos = 0.27;
            double direction = 0.0001;
            while (!mainFile2.isStopRequested()) {
                ledPos += direction;
                if (ledPos > maxLedPos) {
                    direction = -direction;
                    ledPos = maxLedPos;
                }
                if (ledPos < minLedPos) {
                    direction = -direction;
                    ledPos = minLedPos;
                }
                mainFile3.bottomLed.setPosition(ledPos);
            }
        }
    }
}