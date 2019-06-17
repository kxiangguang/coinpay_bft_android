package com.spark.netty_library.netty;

import android.util.Log;

import com.spark.netty_library.CMD;
import com.spark.netty_library.DefineUtil;
import com.spark.netty_library.listener.SendMsgListener;
import com.spark.netty_library.message.SocketMessage;
import com.spark.netty_library.message.SocketResponse;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 格式化数据
 * Created by Administrator on 2019/1/21 0021.
 */

public class NettyInitDataUtils {
    /**
     * 格式化发送数据
     *
     * @param cmd
     * @param body
     * @return
     */
    public static byte[] buildRequest(int cmd, byte[] body) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            int length = body == null ? 26 : (26 + body.length);
            dos.writeInt(length);
            dos.writeLong(DefineUtil.SEQUESCEID);
            dos.writeShort(cmd);
            dos.writeInt(DefineUtil.VERSION);
            byte[] terminalBytes = DefineUtil.TERMINIAL.getBytes();
            dos.write(terminalBytes);
            dos.writeInt(DefineUtil.REQUESTID);
            if (body != null) dos.write(body);
            return bos.toByteArray();
        } catch (IOException ex) {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 格式化解析数据
     *
     * @param sendMsgListener
     * @param dis
     */
    public static void startRecTask(SendMsgListener sendMsgListener, DataInputStream dis) {
        if (dis == null) {
            return;
        }
        try {
            int length = dis.readInt();
            long sequenceId = dis.readLong();
            short cmd = dis.readShort();
            final int responseCode = dis.readInt();
            int requestId = dis.readInt();
            byte[] buffer = new byte[length - 22];
            int nIdx = 0;
            int nReadLen = 0;
            while (nIdx < buffer.length) {
                nReadLen = dis.read(buffer, nIdx, buffer.length - nIdx);
                if (nReadLen > 0) {
                    nIdx += nReadLen;
                } else {
                    break;
                }
            }
            String str = new String(buffer);
            if (sendMsgListener != null && cmd == CMD.ORDER_TICK) {
                Log.e("开始接收返回数据", "返回心跳指令==" + cmd + ",,,,心跳数据==" + str);
            }
            if (sendMsgListener != null && cmd != CMD.ORDER_TICK) {
                Log.e("开始接收返回数据", "返回数据指令==" + cmd + ",,,,返回数据==" + str);
                SocketResponse socketResponse = new SocketResponse(cmd, str, 0);
                sendMsgListener.onMessageResponse(socketResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
