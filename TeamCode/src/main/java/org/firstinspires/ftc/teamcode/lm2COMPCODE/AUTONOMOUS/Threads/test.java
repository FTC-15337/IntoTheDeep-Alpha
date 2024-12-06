package org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.Threads;

import android.graphics.Bitmap;
import android.graphics.Picture;
import android.os.Build;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamClient;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamServerBetter;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.teamcode.lm2COMPCODE.AUTONOMOUS.AutoTestTeleop;

public class test extends Thread{
    public AutoTestTeleop mainFile;
    public CameraStreamClient client;
    public CameraStreamServerBetter server;
    public CameraStreamSource limelightSource;
    public void init(AutoTestTeleop main){
        mainFile = main;
        server = CameraStreamServerBetter.getInstance();
        client = CameraStreamClient.getInstance();
        class limelightSource implements CameraStreamSource {
            public CameraStreamServerBetter server;
            public void init(CameraStreamServerBetter server){
                this.server = server;
            }
            @Override
            public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> continuation) {
                server = CameraStreamServerBetter.getInstance();
                Picture pic = new Picture();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    server.send(Bitmap.createBitmap(pic));
                }
            }
        }
        limelightSource = new limelightSource();
        server.setSource(limelightSource);
    }
}
