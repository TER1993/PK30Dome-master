package speedata.com.blelib.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

import speedata.com.blelib.base.BaseBleApplication;
import speedata.com.blelib.service.BluetoothLeService;

public class PK30DataUtils {

    //头
    static String head = "AA";
    //命令
    static String cmd1 = "01";
    static String cmd2 = "02";
    static String cmd3 = "03";
    static String cmd4 = "04";
    static String cmd5 = "05";
    static String cmd6 = "06";
    static String cmd7 = "07";
    static String cmd8 = "08";
    static String cmd9 = "09";
    //气体类型
    static String all = "AA";
    static String ch4 = "A1";
    static String co = "A2";
    static String co2 = "A3";
    static String o2 = "A4";
    //基础数据00000
    static String basedata = "3030303030";

    static String zheng = "2B";
    static String fu = "2D";


    /**
     * 矿灯检验数据是否符合规定
     *
     * @param bytes 数据
     * @return boolean
     */
    public static boolean checkData2(byte[] bytes) {
        if ((byte) 0xAA != bytes[0]) {
            return false;
        }
        int length = bytes.length;

        byte[] bytes1 = new byte[length - 2];
        byte[] bytes2 = new byte[2];

        System.arraycopy(bytes, 0, bytes1, 0, length - 2);
        Log.d("ZM", "bytes1: " + DataManageUtils.bytesToHexString(bytes1));
        System.arraycopy(bytes, length - 2, bytes2, 0, 2);
        Log.d("ZM", "bytes2: " + DataManageUtils.bytesToHexString(bytes2));
        String result = DataManageUtils.getCRC(bytes1);
        String back1 = result.substring(0, 2);
        String back2 = result.substring(2);
        String reback = DataManageUtils.exChange(back2 + back1);
        Log.d("ZM", "reback: " + reback);
        if (Objects.equals(ByteUtils.toHexString(bytes2), reback)) {
            Log.d("ZM", "trueresult=" + reback + "\nByteUtils.toHexString(bytes2)=" + ByteUtils.toHexString(bytes2));
            return true;
        } else {
            Log.d("ZM", "falseresult=" + reback + "\nByteUtils.toHexString(bytes2)=" + ByteUtils.toHexString(bytes2));
            return false;
        }


    }


    //==================================实现接收数据的解析============================================

    /**
     * 解析one的回复，向上传递one的各个数据。
     *
     * @param context
     * @param intent
     * @param bytes
     */
    public static void readOne(Context context, Intent intent, byte[] bytes) {
        int length = 5;
        byte[] result1 = new byte[length];
        byte[] result2 = new byte[length];
        byte[] result3 = new byte[length];
        try {
            System.arraycopy(bytes, 10, result1, 0, length);
            System.arraycopy(bytes, 15, result2, 0, length);
            System.arraycopy(bytes, 20, result3, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String ch4 = DataManageUtils.toAsciiString(result1);
        String o2 = DataManageUtils.toAsciiString(result2);
        String co = DataManageUtils.toAsciiString(result3);
        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_ONE1, ch4);
        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_ONE2, o2);
        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_ONE3, co);
        context.sendBroadcast(intent);

    }

    /**
     * 解析two的回复，向上传递two的结果。
     *
     * @param context
     * @param intent
     * @param bytes
     */
    public static void readTwo(Context context, Intent intent, byte[] bytes) {
        int length = 1;
        byte[] result1 = new byte[length];

        try {
            System.arraycopy(bytes, 10, result1, 0, length);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String ch4 = DataManageUtils.exChange(DataManageUtils.bytesToHexString(result1));

        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_TWO, ch4);

        context.sendBroadcast(intent);

    }

    /**
     * 解析two的回复，向上传递two的结果。
     *
     * @param context
     * @param intent
     * @param bytes
     */
    public static void readThree(Context context, Intent intent, byte[] bytes) {
        int length = 1;
        byte[] result1 = new byte[length];

        try {
            System.arraycopy(bytes, 10, result1, 0, length);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String ch4 = DataManageUtils.exChange(DataManageUtils.bytesToHexString(result1));

        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_THREE, ch4);

        context.sendBroadcast(intent);

    }

    /**
     * 解析two的回复，向上传递two的结果。
     *
     * @param context
     * @param intent
     * @param bytes
     */
    public static void readFour(Context context, Intent intent, byte[] bytes) {
        int length = 1;
        byte[] result1 = new byte[length];

        try {
            System.arraycopy(bytes, 10, result1, 0, length);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String ch4 = DataManageUtils.exChange(DataManageUtils.bytesToHexString(result1));

        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_FOUR, ch4);

        context.sendBroadcast(intent);

    }

    /**
     * 解析two的回复，向上传递two的结果。
     *
     * @param context
     * @param intent
     * @param bytes
     */
    public static void readFive(Context context, Intent intent, byte[] bytes) {
        int length = 1;
        byte[] result1 = new byte[length];

        try {
            System.arraycopy(bytes, 10, result1, 0, length);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String ch4 = DataManageUtils.exChange(DataManageUtils.bytesToHexString(result1));

        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_FIVE, ch4);

        context.sendBroadcast(intent);

    }

    /**
     * 解析two的回复，向上传递two的结果。
     *
     * @param context
     * @param intent
     * @param bytes
     */
    public static void readSix(Context context, Intent intent, byte[] bytes) {
        int length = 1;
        byte[] result1 = new byte[length];

        try {
            System.arraycopy(bytes, 10, result1, 0, length);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String ch4 = DataManageUtils.exChange(DataManageUtils.bytesToHexString(result1));

        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_SIX, ch4);

        context.sendBroadcast(intent);

    }

    /**
     * 解析two的回复，向上传递two的结果。
     *
     * @param context
     * @param intent
     * @param bytes
     */
    public static void readSeven(Context context, Intent intent, byte[] bytes) {
        int length = 1;
        byte[] result1 = new byte[length];

        try {
            System.arraycopy(bytes, 10, result1, 0, length);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String ch4 = DataManageUtils.exChange(DataManageUtils.bytesToHexString(result1));

        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_SEVEN, ch4);

        context.sendBroadcast(intent);

    }

    /**
     * 解析two的回复，向上传递two的结果。
     *
     * @param context
     * @param intent
     * @param bytes
     */
    public static void readEight(Context context, Intent intent, byte[] bytes) {
        int length = 8;
        byte[] result1 = new byte[length];
        byte[] result2 = new byte[1];

        try {
            System.arraycopy(bytes, 1, result1, 0, length);
            System.arraycopy(bytes, 10, result2, 0, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String ch4 = DataManageUtils.exChange(DataManageUtils.toAsciiString(result1));
        String o2 = DataManageUtils.exChange(DataManageUtils.bytesToHexString(result2));

        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_EIGHT1, ch4);
        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_EIGHT2, o2);

        context.sendBroadcast(intent);

    }


    //发指令1
    public static void setOne(String name) {

        String rename = DataManageUtils.exChange(DataManageUtils.stringToAscii(name));
        String front = head + rename + cmd1 + all + basedata;
        //
        String back = DataManageUtils.getCRC(DataManageUtils.HexString2Bytes(front));
        String back1 = back.substring(0, 2);
        String back2 = back.substring(2);
        String reback = DataManageUtils.exChange(back2 + back1);
        //完整待发送指令的String型
        String result = front + reback;

        Log.d("ZM", "result01:" + result);

        BaseBleApplication.writeCharacteristic3(DataManageUtils.HexString2Bytes(result));

    }

    //发指令2
    public static void setTwo(String name, String mType, String data) {
        String rename = DataManageUtils.exChange(DataManageUtils.stringToAscii(name));
        String redata = DataManageUtils.exChange(DataManageUtils.stringToAscii(data));
        String front = head + rename + cmd2 + mType + zheng + redata;
        //
        String back = DataManageUtils.getCRC(DataManageUtils.HexString2Bytes(front));
        String back1 = back.substring(0, 2);
        String back2 = back.substring(2);
        String reback = DataManageUtils.exChange(back2 + back1);
        //完整待发送指令的String型
        String result = front + reback;

        Log.d("ZM", "result02:" + result);

        BaseBleApplication.writeCharacteristic3(DataManageUtils.HexString2Bytes(result));

    }

    //发指令3
    public static void setThree(String name, String a4, String data3) {
        String rename = DataManageUtils.exChange(DataManageUtils.stringToAscii(name));
        String redata = DataManageUtils.exChange(DataManageUtils.stringToAscii(data3));
        String front = head + rename + cmd3 + a4 + zheng + redata;
        //
        String back = DataManageUtils.getCRC(DataManageUtils.HexString2Bytes(front));
        String back1 = back.substring(0, 2);
        String back2 = back.substring(2);
        String reback = DataManageUtils.exChange(back2 + back1);
        //完整待发送指令的String型
        String result = front + reback;

        Log.d("ZM", "result03:" + result);

        BaseBleApplication.writeCharacteristic3(DataManageUtils.HexString2Bytes(result));

    }

    //发指令4
    public static void setFour(String name, String mType) {
        String rename = DataManageUtils.exChange(DataManageUtils.stringToAscii(name));

        String front = head + rename + cmd4 + mType + basedata;
        //
        String back = DataManageUtils.getCRC(DataManageUtils.HexString2Bytes(front));
        String back1 = back.substring(0, 2);
        String back2 = back.substring(2);
        String reback = DataManageUtils.exChange(back2 + back1);
        //完整待发送指令的String型
        String result = front + reback;

        Log.d("ZM", "result04:" + result);

        BaseBleApplication.writeCharacteristic3(DataManageUtils.HexString2Bytes(result));

    }

    //发指令5
    public static void setFive(String name, String mType) {
        String rename = DataManageUtils.exChange(DataManageUtils.stringToAscii(name));

        String front = head + rename + cmd5 + mType + basedata;
        //
        String back = DataManageUtils.getCRC(DataManageUtils.HexString2Bytes(front));
        String back1 = back.substring(0, 2);
        String back2 = back.substring(2);
        String reback = DataManageUtils.exChange(back2 + back1);
        //完整待发送指令的String型
        String result = front + reback;

        Log.d("ZM", "result05:" + result);

        BaseBleApplication.writeCharacteristic3(DataManageUtils.HexString2Bytes(result));

    }

    //发指令6
    public static void setSix(String name, String mType, String data6) {
        String rename = DataManageUtils.exChange(DataManageUtils.stringToAscii(name));
        String redata = DataManageUtils.exChange(DataManageUtils.stringToAscii(data6));
        String front = head + rename + cmd6 + mType + zheng + redata;
        //
        String back = DataManageUtils.getCRC(DataManageUtils.HexString2Bytes(front));
        String back1 = back.substring(0, 2);
        String back2 = back.substring(2);
        String reback = DataManageUtils.exChange(back2 + back1);
        //完整待发送指令的String型
        String result = front + reback;

        Log.d("ZM", "result06:" + result);

        BaseBleApplication.writeCharacteristic3(DataManageUtils.HexString2Bytes(result));

    }

    //发指令7
    public static void setSeven(String name) {

        String rename = DataManageUtils.exChange(DataManageUtils.stringToAscii(name));
        String front = head + rename + cmd7 + all + basedata;
        //
        String back = DataManageUtils.getCRC(DataManageUtils.HexString2Bytes(front));
        String back1 = back.substring(0, 2);
        String back2 = back.substring(2);
        String reback = DataManageUtils.exChange(back2 + back1);
        //完整待发送指令的String型
        String result = front + reback;

        Log.d("ZM", "result07:" + result);

        BaseBleApplication.writeCharacteristic3(DataManageUtils.HexString2Bytes(result));

    }

    //发指令8
    public static void setEight(String data8) {
        String rename = DataManageUtils.exChange(DataManageUtils.stringToAscii(data8));
        String front = head + rename + cmd8 + all + basedata;
        //
        String back = DataManageUtils.getCRC(DataManageUtils.HexString2Bytes(front));
        String back1 = back.substring(0, 2);
        String back2 = back.substring(2);
        String reback = DataManageUtils.exChange(back2 + back1);
        //完整待发送指令的String型
        String result = front + reback;

        Log.d("ZM", "result08:" + result);

        BaseBleApplication.writeCharacteristic3(DataManageUtils.HexString2Bytes(result));

    }


}
