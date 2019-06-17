package com.spark.netty_library.message;


/**
 * 接收数据的实体类
 */

public class SocketResponse {
    private short cmd; // 传的指令
    private String response; // 返回的参数
    private int channel;

    public SocketResponse(short cmd, String response) {
        this.cmd = cmd;
        this.response = response;
    }

    public SocketResponse(short cmd, String response, int channel) {
        this.cmd = cmd;
        this.response = response;
        this.channel = channel;
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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
