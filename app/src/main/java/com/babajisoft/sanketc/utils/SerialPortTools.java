package com.babajisoft.sanketc.utils;

/**
 * Created by babaji on 18/9/16.
 */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidParameterException;

public class SerialPortTools {

    protected SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;

    public SerialPort getSerialPort(String port,int baudrate) throws SecurityException, IOException,
            InvalidParameterException {
        if (mSerialPort == null) {
            if ((port.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }
            mSerialPort = new SerialPort(new File(port), baudrate, 0);
        }
        return mSerialPort;
    }

    /**
     * @param port 端口
     * @param baudrate 波特率
     * */
    public SerialPortTools(String port,int baudrate)
    {
        try {
            mSerialPort = this.getSerialPort(port,baudrate);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
    }

    void initp()
    {
        if (mOutputStream != null) {
            try {
                mOutputStream.write(new byte[]{0x1B,'@'});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReadThread extends Thread {
        public void run() {
            super.run();
            while(!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        System.out.println("接收到数据 大小: " + size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    /** 关闭串口 */
    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    protected void destroy() {
        if (mReadThread != null)
            mReadThread.interrupt();
        this.closeSerialPort();
        mSerialPort = null;
    }

    // [s] 输出
    public void write(String msg)
    {
        try {
            if(allowToWrite())
            {
                if(msg == null)
                    msg = "";
                mOutputStream.write(msg.getBytes("unicode"));
                mOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // [s] 输出
    public void write_unicode(String msg)
    {
        try {
            if(allowToWrite())
            {
                if(msg == null)
                    msg = "";
                mOutputStream.write(msg.getBytes("unicode"));
                mOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // [s] 输出
    public void write_Unicode(String msg,String str)
    {
        try {
            if(allowToWrite())
            {
                if(msg == null)
                    msg = "";

                if(str.equals("中文（繁/简 体）")){
                    //对要打印的字符串逐个判断是否为中文、英文、符号、数字,逐个打印；
                    for(int i=0;i<msg.length();i++){
                        String s = msg.substring(i, i+1);
                        //若不为中文
                        if(!JBInterface.isChinese(s)){
                            byte[] bytes = s.getBytes();
                            byte[] writebytes = {0x00,bytes[0]};
                            mOutputStream.write(writebytes);
                            Thread.sleep(20);
                        }else{
                            //若为中文
                            mOutputStream.write(JBInterface.getStringToHexBytes(s));
                            Thread.sleep(20);
                        }
                    }
//					Thread.sleep(50);
                }else if(str.equals("English")){
                    mOutputStream.write(JBInterface.printerENByte(msg));
                    mOutputStream.flush();
                    Thread.sleep(50);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void write_Chinese(String msg,String str)
    {
        try {
            if(allowToWrite())
            {
                if(msg == null)
                    msg = "";

                if(str.equals("中文（繁/简 体）")){

                    mOutputStream.write(JBInterface.getStringToHexBytes(msg));
                    Thread.sleep(50);

                }
//					Thread.sleep(50);
            }else if(str.equals("English")){
                mOutputStream.write(JBInterface.printerENByte(msg));
                mOutputStream.flush();
                Thread.sleep(50);
            }

        } catch (IOException e1) {
            e1.printStackTrace();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //判断是否为英文；
    private static boolean checkPwdChars(final String str){
        //先检查最后一位(提高效率)
        char tmp;
        int i=str.length()-1;
        for(;i>=0;i--){
            tmp=str.charAt(i);
            if(!(('0'<=tmp&&tmp<='9')
                    ||('a'<=tmp&&tmp<='z')
                    ||('A'<=tmp&&tmp<='Z'))){
                return false;
            }
        };
        return true;
    }



    public static byte[] getByteArray(String hexString) {

        return new BigInteger(hexString,16).toByteArray();

    }

    public static String bytesToHexString123(byte[] src){

        StringBuilder stringBuilder = new StringBuilder("");

        if (src == null || src.length <= 0) {
            return null;
        }

        for (int i = 0; i < src.length; i++) {

            int v = src[i] & 0xFF;

            String hv = Integer.toHexString(v);

            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);

        }
        return stringBuilder.toString();
    }
    /**
     * 输出
     * */
    public void write(byte[] b)
    {
        try {
            if(allowToWrite())
            {
                if(b == null)
                    return;
                mOutputStream.write(b);
                mOutputStream.flush(); // 1
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出
     * */
    public void write(int oneByte)
    {
        try {
            if(allowToWrite())
            {
                mOutputStream.write(oneByte);
                mOutputStream.flush(); // 1
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否允许打印
     * */
    public boolean allowToWrite()
    {
        if (mOutputStream == null) {
            System.out.println("输出流为空! 不能打印! ");
            return false;
        }
        return true;
    }

    // [e]

}

