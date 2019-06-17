package com.spark.modulebase.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * 关闭流的相关操作
 */

public class CloseUtils {

    /**
     * 关闭FileChannel
     *
     * @param fc
     */
    public static void closeIO(FileChannel fc) {
        try {
            fc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 关流InputStream,OutputStream
     *
     * @param fc
     */
    public static void closeIO(InputStream is, OutputStream os) {
        try {
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭BufferedOutputStream
     *
     * @param fc
     */
    public static void closeIO(BufferedOutputStream bos) {
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭BufferedWriter
     *
     * @param fc
     */
    public static void closeIO(BufferedWriter bw) {
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭BufferedReader
     *
     * @param fc
     */
    public static void closeIO(BufferedReader bf) {
        try {
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭InputStream
     *
     * @param fc
     */
    public static void closeIO(InputStream is) {
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
