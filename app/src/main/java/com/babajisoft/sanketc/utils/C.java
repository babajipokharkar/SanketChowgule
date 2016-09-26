package com.babajisoft.sanketc.utils;

/**
 * Created by babaji on 18/9/16.
 */

public class C {
    public static SerialPortTools printSerialPortTools;
    public static String printPort_58mm = "/dev/ttyS6"; //
    public static int printBaudrate_58mm = 115200; //
    public static String printPort_80mm = "/dev/ttyS6"; //
    public static int printBaudrate_80mm = 9600; //


    public static SerialPortTools ledSerialPortTools;
    public static SerialPortTools kitchenSerialPortTools;
    public static String ledPort = "/dev/ttyS6";
    public static int ledBaudrate = 9600;
    public static String kitchenPort = "/dev/ttyS6"; // 打印串口
    public static int kitchenBaudrate = 115200; // 打印波特率
}

