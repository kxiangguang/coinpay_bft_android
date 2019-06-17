package com.spark.modulebase.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/8/30.
 */

public class MathUtils {
    /**
     * 小数精确到具体位数
     *
     * @param number
     * @param n
     * @param pattern
     * @return
     */
    public static String getRundNumber(double number, int n, String pattern) {
        if (StringUtils.isEmpty(pattern)) pattern = "########0.";
        String str = "";
        for (int j = 0; j < n; j++) str += "0";
        pattern += str;
        int m = (int) Math.pow(10, n);
        number = (Math.floor(number * m)) / (m * 1.0);
        return new DecimalFormat(pattern).format(number);
    }

    /**
     * String→double
     *
     * @param strNumber
     * @return
     */
    public static double getDoubleTransferString(String strNumber) {
        double transferNumber = 0;
        if (StringUtils.isNotEmpty(strNumber)) {
            BigDecimal bigDecimal = new BigDecimal(strNumber);
            transferNumber = bigDecimal.doubleValue();
        }
        return transferNumber;
    }

    /**
     * 多于8位以上的数字正常显示，不使用科学计数法
     *
     * @param doubleNum
     * @return
     */
    public static String formatENumber(double doubleNum) {
        return new DecimalFormat("#").format(doubleNum);
    }

    /**
     * 过滤多余的0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (StringUtils.isNotEmpty(s) && s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
            return s;
        }
        return "";
    }


    /**
     * 加（精确度）
     *
     * @param number1
     * @param number2
     * @param n
     * @return
     */
    public static String getBigDecimalAddWithScale(String number1, String number2, int n) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal aBD = new BigDecimal(number1.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal bBD = new BigDecimal(number2.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal resultBD = aBD.add(bBD).setScale(n, BigDecimal.ROUND_HALF_UP);
            return resultBD.toPlainString();
        }
        return "0";
    }

    /**
     * 加
     *
     * @param number1
     * @param number2
     * @return
     */
    public static String getBigDecimalAdd(String number1, String number2) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal b1 = new BigDecimal(number1);
            BigDecimal b2 = new BigDecimal(number2);
            return b1.add(b2).toPlainString();
        }
        return "0";
    }


    /**
     * 减（精确度）
     *
     * @param number1
     * @param number2
     * @param n
     * @return
     */
    public static String getBigDecimalSubtractWithScale(String number1, String number2, int n) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal aBD = new BigDecimal(number1.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal bBD = new BigDecimal(number2.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal resultBD = aBD.subtract(bBD).setScale(n, BigDecimal.ROUND_HALF_UP);
            return resultBD.toPlainString();
        }
        return "0";
    }

    /**
     * 减
     *
     * @param number1
     * @param number2
     * @return
     */
    public static String getBigDecimalSubtract(String number1, String number2) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal b1 = new BigDecimal(number1);
            BigDecimal b2 = new BigDecimal(number2);
            return b1.subtract(b2).toPlainString();
        }
        return "0";
    }

    /**
     * 乘（精确度）
     *
     * @param number1
     * @param number2
     * @param n
     * @return
     */
    public static String getBigDecimalMultiplyWithScale(String number1, String number2, int n) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal aBD = new BigDecimal(number1.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal bBD = new BigDecimal(number2.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal resultBD = aBD.multiply(bBD).setScale(n, BigDecimal.ROUND_HALF_UP);
            return resultBD.toPlainString();
        }
        return "0";

    }

    /**
     * 乘
     *
     * @param number1
     * @param number2
     * @param n
     * @return
     */
    public static String getBigDecimalMultiply(String number1, String number2) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal b1 = new BigDecimal(number1);
            BigDecimal b2 = new BigDecimal(number2);
            return b1.multiply(b2).toPlainString();
        }
        return "0";
    }


    /**
     * 除（精确度）
     *
     * @param number1
     * @param number2
     * @param n
     * @return
     */
    public static String getBigDecimalDivideWithScale(String number1, String number2, int n) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal aBD = new BigDecimal(number1.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal bBD = new BigDecimal(number2.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal resultBD = aBD.divide(bBD, n, BigDecimal.ROUND_HALF_UP);
            return resultBD.toPlainString();
        }
        return "0";
    }

    /**
     * 除
     *
     * @param number1
     * @param number2
     * @return
     */
    public static String getBigDecimalDivide(String number1, String number2) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal b1 = new BigDecimal(number1);
            BigDecimal b2 = new BigDecimal(number2);
            return b1.divide(b2).toPlainString();
        }
        return "0";
    }


    /**
     * 比较大小
     *
     * @param number1
     * @param number2
     * @param n
     * @return
     */
    public static int getBigDecimalCompareTo(String number1, String number2, int n) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal aBD = new BigDecimal(number1.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            BigDecimal bBD = new BigDecimal(number2.trim()).setScale(n, BigDecimal.ROUND_HALF_UP);
            return aBD.compareTo(bBD);
        } else {
            return -2;
        }
    }

    //判断是否为数字，包括整数和小数,不包含正负号
    public static boolean isNumber(String str) {
        if (StringUtils.isNotEmpty(str)) {
            //带小数的
            Pattern pattern = Pattern.compile("[0-9]+.*[0-9]*");
            if (pattern.matcher(str).matches()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
