package org.firstinspires.ftc.teamcode.learning;

import static com.qualcomm.robotcore.hardware.DcMotor.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="rotate")
public class rotate extends LinearOpMode {
    private DcMotor rotate;
    private int LastPos;
    @Override
    public void runOpMode() throws InterruptedException {
        rotate = hardwareMap.dcMotor.get("RFM");
        rotate.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        waitForStart();
        while(!isStopRequested()){
            if(gamepad2.right_stick_y <= -0.2 || gamepad2.right_stick_y >= 0.2){
                LastPos = rotate.getCurrentPosition();
                rotate.setPower(-gamepad2.right_stick_y / 2.0);
            }else{
                if(LastPos <= rotate.getCurrentPosition()-10 || LastPos >=rotate.getCurrentPosition()+10){
                    if(LastPos <= rotate.getCurrentPosition()-10){
                        rotate.setPower(0.25);
                    }
                    if(LastPos >= rotate.getCurrentPosition()+10){
                        rotate.setPower(-0.25);
                    }
                }
            }
        }

    }
}
