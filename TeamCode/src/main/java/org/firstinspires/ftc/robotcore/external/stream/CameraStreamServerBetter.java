//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.firstinspires.ftc.robotcore.external.stream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import androidx.annotation.Nullable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.robocol.Command;
import java.io.ByteArrayOutputStream;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class CameraStreamServerBetter implements OpModeManagerNotifier.Notifications {
    public static final int CHUNK_SIZE = 4096;
    private static final int DEFAULT_JPEG_QUALITY = 75;
    private static int frameNum;
    private static final CameraStreamServerBetter INSTANCE = new CameraStreamServerBetter();
    private OpModeManagerImpl opModeManager;
    @Nullable
    private CameraStreamSource source;
    private int jpegQuality = 75;

    public static CameraStreamServerBetter getInstance() {
        return INSTANCE;
    }

    private CameraStreamServerBetter() {
    }

    public synchronized void setSource(@Nullable CameraStreamSource source) {
        this.source = source;
        RobotCoreCommandList.CmdStreamChange cmd = new RobotCoreCommandList.CmdStreamChange();
        cmd.available = source != null;
        NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_STREAM_CHANGE", cmd.serialize()));
        if (source != null) {
            this.opModeManager = OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity());
            if (this.opModeManager != null) {
                this.opModeManager.registerListener(this);
            }
        }

    }

    public int getJpegQuality() {
        return this.jpegQuality;
    }

    public void setJpegQuality(int quality) {
        this.jpegQuality = quality;
    }

    public CallbackResult handleRequestFrame() {
        if (this.source != null) {
            synchronized(this) {
                this.source.getFrameBitmap(Continuation.createTrivial(new Consumer<Bitmap>() {
                    public void accept(Bitmap bitmap) {
                        CameraStreamServerBetter.this.sendFrame(bitmap);
                    }
                }));
            }
        }

        return CallbackResult.HANDLED;
    }
    public void send(Bitmap btm){
        sendFrame(btm);
    }
    private void sendFrame(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, this.jpegQuality, outputStream);
        byte[] data = outputStream.toByteArray();
        NetworkConnectionHandler handler = NetworkConnectionHandler.getInstance();
        RobotCoreCommandList.CmdReceiveFrameBegin receiveFrameBegin = new RobotCoreCommandList.CmdReceiveFrameBegin(frameNum, data.length);
        handler.sendCommand(new Command("CMD_RECEIVE_FRAME_BEGIN", receiveFrameBegin.serialize()));

        for(int chunkNum = 0; (double)chunkNum < Math.ceil((double)data.length / 4096.0); ++chunkNum) {
            int offset = chunkNum * 4096;
            int length = Math.min(4096, data.length - offset);
            RobotCoreCommandList.CmdReceiveFrameChunk receiveFrameChunk = new RobotCoreCommandList.CmdReceiveFrameChunk(frameNum, chunkNum, data, offset, length);
            handler.sendCommand(new Command("CMD_RECEIVE_FRAME_CHUNK", receiveFrameChunk.serialize()));
        }

        ++frameNum;
    }

    public void onOpModePreInit(OpMode opMode) {
    }

    public void onOpModePreStart(OpMode opMode) {
    }

    public void onOpModePostStop(OpMode opMode) {
        this.setSource((CameraStreamSource)null);
        if (this.opModeManager != null) {
            this.opModeManager.unregisterListener(this);
            this.opModeManager = null;
        }

    }
}
