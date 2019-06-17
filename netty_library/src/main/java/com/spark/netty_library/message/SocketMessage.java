package com.spark.netty_library.message;


/**
 * 发送的实体类
 */

public class SocketMessage {
    private int channel; // 对应不同地址的socket
    private short cmd; // 传的指令
    private byte[] body; // 参数

    public SocketMessage(int channel, short cmd, byte[] body) {
        this.channel = channel;
        this.cmd = cmd;
        this.body = body;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(short cmd) {
        this.cmd = cmd;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
