package com.babajisoft.sanketc.utils;

/**
 * Created by babaji on 18/9/16.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class JBInterface {

    public static void initPrinter() {
        JBInterface.openPrinter();
        JBInterface.convertPrinterControl();
    }

    public static boolean openPrinter() {
        int result = GpioControl.activate(GpioControl.printer_o, true);
        if (result == 0)
            return true;
        else
            return false;
    }

    public static boolean closePrinter() {
        int result = GpioControl.activate(GpioControl.printer_o, false);
        C.printSerialPortTools.closeSerialPort();
        if (result == 0)
            return true;
        else
            return false;
    }

    public static boolean convertPrinterControl() {
        int result = GpioControl.convertPrinter();
        C.printSerialPortTools = new SerialPortTools(C.printPort_58mm,
                C.printBaudrate_58mm);
        if (result == 0)
            return true;
        else
            return false;
    }

    /** 自检 */
    public static void testPrinter() {
        PrintTools_58mm.printTest();
    }

    /** 打印文字GB2312 */
    public static void printText(String text, String str) {
        Log.i("info", "text == " + text);
        PrintTools_58mm.printText(text, str);
    }

    /** 打印文字GB2312 */
    public static void printTextLine(String text, String str) {
        Log.i("info", "text == " + text);
        PrintTools_58mm.printTextLine(text, str);
    }

    /** 打印文字Unicode */
    public static void printText_Unicode(String text) {
        PrintTools_58mm.printText_Unicode(text);
    }

    public static void printQRCodeWithPath(String qrcodeImagePath) {
        PrintTools_58mm.printPhotoWithPath(qrcodeImagePath);
    }

    public static void printImageWithPath(String iamgePath) {
        PrintTools_58mm.printPhotoWithPath(iamgePath);
    }

    public static void printQRCode(Bitmap bitmap) {
        byte[] command = PrintTools_58mm.decodeBitmap(bitmap);
        PrintTools_58mm.printPhoto(command);
    }

    public static void printImage(Bitmap bitmap) {
        byte[] command = PrintTools_58mm.decodeBitmap(bitmap);
        PrintTools_58mm.printPhoto(command);
    }

    public static void printQRCodeImageInAssets(Context context, String fileName) {
        PrintTools_58mm.printPhotoInAssets(context, fileName);
    }

    public static void printImageInAssets(Context context, String fileName) {
        PrintTools_58mm.printPhotoInAssets(context, fileName);
    }

    // 完整的判断中文汉字和符号
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    public static String stringToUnicode(String s) {

        String str = "";

        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            if (ch > 255)
                str += Integer.toHexString(ch);
            else
                str += Integer.toHexString(ch);
        }

        return str;

    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    /**
     * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" –> byte[]{0x2B, 0×44, 0xEF,
     * 0xD9}
     *
     * @param src
     *            String
     * @return byte[]
     */
    public static byte[] HexString2Bytes(String src) {
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < tmp.length / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static byte[] getStringToHexBytes(String str) {
        return HexString2Bytes(stringToUnicode(str));
    }

    // 打印英文重做byte[]
    public static byte[] printerENByte(String msg) {
        byte[] b = msg.getBytes();
        byte[] writebytes = new byte[b.length * 2];
        for (int i = 0; i < b.length; i++) {
            writebytes[i * 2] = 0x00;
            writebytes[i * 2 + 1] = msg.getBytes()[i];
        }
        return writebytes;
    }
}

