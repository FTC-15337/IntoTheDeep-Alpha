package org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.Threads;

import org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.auto;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.Teleop.Teleop;

public class lights extends Thread{
    private Teleop mainFile;
    private auto mainFile2;
    private boolean which;
    public void init(Teleop main){
        mainFile = main;
        which = true;
    }
    public void init2(auto main){
        mainFile2 = main;
        which = false;
    }
    public void run(){
        if(which) {
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
                mainFile2.bottomLed.setPosition(ledPos);
            }
        }
    }
}