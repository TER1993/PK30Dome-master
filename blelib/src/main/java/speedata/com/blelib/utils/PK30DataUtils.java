package speedata.com.blelib.utils;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.math.BigDecimal;
import java.math.BigInteger;
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
        String reback = back2 + back1;
        Log.d("ZM", "reback: " + reback);
        if (Objects.equals(ByteUtils.toHexString(bytes2), reback)) {
            Log.d("ZM", "trueresult=" + reback + "\nByteUtils.toHexString(bytes2)=" + ByteUtils.toHexString(bytes2));
            return true;
        } else {
            Log.d("ZM", "falseresult=" + reback + "\nByteUtils.toHexString(bytes2)=" + ByteUtils.toHexString(bytes2));
            return false;
        }


    }


    /**
     * 解析数据并使用广播发送出去
     *
     * @param bytes 数据
     */
    public static void analysisData(final Context context, final Intent intent, final byte[] bytes) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (bytes[1]) {
                    case 0x0A:
                        BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xAA, (byte) 0x8A, 0x01, 0x35, 0x00});
                        SystemClock.sleep(300);
                        sendBroadcast(2, context, intent, bytes, BluetoothLeService.NOTIFICATION_DATA_L, "ffff");
                        break;
                    case 0x0B:
                        BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xAA, (byte) 0x8B, 0x01, 0x36, 0x00});
                        SystemClock.sleep(300);
                        sendBroadcast(2, context, intent, bytes, BluetoothLeService.NOTIFICATION_DATA_W, "ffff");
                        break;
                    case 0x0C:
                        BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xAA, (byte) 0x8C, 0x01, 0x37, 0x00});
                        SystemClock.sleep(300);
                        sendBroadcast(2, context, intent, bytes, BluetoothLeService.NOTIFICATION_DATA_H, "ffff");
                        break;
                    case 0x0D:
                        BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xAA, (byte) 0x8D, 0x01, 0x38, 0x00});
                        SystemClock.sleep(300);
                        sendWeightBroadcast(4, context, intent, bytes, BluetoothLeService.NOTIFICATION_DATA_G);
                        break;
                    default:
                        break;
                }
            }
        }).start();

    }

    public static void replyError(byte[] bytes) {
        switch (bytes[1]) {
            case 0x0A:
//                BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xFF, (byte) 0x8A, 0x01, (byte) 0xE5, 0x00});
                PK30DataUtils.setModel(0);
                break;
            case 0x0B:
//                BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xFF, (byte) 0x8B, 0x01, (byte) 0xE6, 0x00});
                PK30DataUtils.setModel(1);
                break;
            case 0x0C:
//                BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xFF, (byte) 0x8C, 0x01, (byte) 0xE7, 0x00});
                PK30DataUtils.setModel(2);
                break;
            case 0x0D:
//                BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xFF, (byte) 0x8D, 0x01, (byte) 0xE8, 0x00});
                PK30DataUtils.setModel(3);
                break;
            default:
                break;
        }
    }

    /**
     * 获取软件版本
     */
    public static void getSoftware() {
        BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xAA, 0x52, 0x01, (byte) 0xFD, 0x00});
    }

    /**
     * 解析软件版本数据
     *
     * @param context
     * @param intent
     * @param bytes
     * @return
     */
    public static void analysisSoftware(Context context, Intent intent, byte[] bytes) {
        int length = bytes.length - 5;
        byte[] result = new byte[length];
        try {
            System.arraycopy(bytes, 3, result, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String string = DataManageUtils.toAsciiString(result);
        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_SOFT, string);
        context.sendBroadcast(intent);
    }

    /**
     * 获取硬件版本
     */
    public static void getHardware() {
        BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xAA, 0x53, 0x01, (byte) 0xFE, 0x00});
    }

    /**
     * 解析硬件版本数据
     *
     * @param context
     * @param intent
     * @param bytes
     * @return
     */
    public static void analysisHardware(Context context, Intent intent, byte[] bytes) {
        int length = bytes.length - 5;
        byte[] result = new byte[length];
        try {
            System.arraycopy(bytes, 3, result, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String string = DataManageUtils.toAsciiString(result);
        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_HARD, string);
        context.sendBroadcast(intent);
    }

    /**
     * 获取MAC
     */
    public static void getMac() {
        BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xAA, 0x51, 0x01, (byte) 0xFC, 0x00});
    }

    /**
     * 解析mac数据
     *
     * @param context
     * @param intent
     * @param bytes
     * @return
     */
    public static void analysisMac(Context context, Intent intent, byte[] bytes) {
        int length = bytes.length - 5;
        byte[] result = new byte[length];
        try {
            System.arraycopy(bytes, 3, result, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String mac = DataManageUtils.toAsciiString(result);
        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_MAC, mac);
        context.sendBroadcast(intent);
    }


    /**
     * 设置测量模式
     * <p>
     * 改成分别的功能输出
     */
    public static void setModel(int model) {
        switch (model) {
            case 0:
                BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xAA, 0x54, 0x01, (byte) 0xFF, 0x00});
                break;
            case 1:
                BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xAA, 0x55, 0x01, (byte) 0x00, 0x00});
                break;
            case 2:
                BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xAA, 0x56, 0x01, (byte) 0x01, 0x00});
                break;
            case 3:
                BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xAA, 0x57, 0x01, (byte) 0x02, 0x00});
                break;
            default:
                break;
        }
    }

    /**
     * 解析设置模式数据
     *
     * @param context
     * @param intent
     * @param bytes
     * @return
     */
    public static void analysisMdoel(Context context, Intent intent, byte[] bytes) {
        int length = bytes.length - 5;
        byte[] result = new byte[length];
        try {
            System.arraycopy(bytes, 3, result, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String string = DataManageUtils.bytesToHexString(result);
        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_MODEL, string);
        context.sendBroadcast(intent);
    }

    /**
     * 关机
     */
    public static void shutdown() {
        BaseBleApplication.writeCharacteristic3(new byte[]{(byte) 0xAA, 0x58, 0x01, (byte) 0x03, 0x00});
    }

    /**
     * 解析关机数据
     *
     * @param context
     * @param intent
     * @param bytes
     * @return
     */
    public static void analysisShutdown(Context context, Intent intent, byte[] bytes) {
        int length = bytes.length - 5;
        byte[] result = new byte[length];
        try {
            System.arraycopy(bytes, 3, result, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String string = DataManageUtils.bytesToHexString(result);
        intent.putExtra(BluetoothLeService.NOTIFICATION_SHUTDOWN, string);
        context.sendBroadcast(intent);
    }

    /**
     * 蜂鸣器
     */
    public static void fengMing(int time) {
        String timeHex = DataManageUtils.IntToHex(time);
        byte[] timeBytes = StringUtils.hexStringToByteArray(timeHex);
        String jiaoYan = DataManageUtils.getJiaoYan("08", timeHex);
        byte[] jiaoyanBytes = StringUtils.hexStringToByteArray(jiaoYan);
        byte[] concatAll = ByteUtils.concatAll(new byte[]{(byte) 0xAA, 0x59, 0x04}, timeBytes, new byte[]{0x01, 0x00}, jiaoyanBytes, new byte[]{0x00});
        BaseBleApplication.writeCharacteristic3(concatAll);
    }

    /**
     * 解析蜂鸣器数据
     *
     * @param context
     * @param intent
     * @param bytes
     * @return
     */
    public static void analysisFengMing(Context context, Intent intent, byte[] bytes) {
        int length = bytes.length - 7;
        byte[] result = new byte[length];
        try {
            System.arraycopy(bytes, 3, result, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String string = DataManageUtils.bytesToHexString(result);
        intent.putExtra(BluetoothLeService.NOTIFICATION_FENGMING, string);
        context.sendBroadcast(intent);
    }

    /**
     * 数据发出
     *
     * @param length     数据截取长度
     * @param context    context
     * @param intent     intent
     * @param bytes      数据
     * @param intentName intentName
     */
    private static void sendBroadcast(int length, Context context, Intent intent, byte[] bytes, String intentName, String errStr) {
        String data = "";
        byte[] result = new byte[length];
        try {
            System.arraycopy(bytes, 3, result, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String byteArrayToString = DataManageUtils.byteArrayToString(result);
        int l = 0;
        if (!errStr.equals(byteArrayToString)) {
            l = Integer.parseInt(Objects.requireNonNull(byteArrayToString), 16);
            double resultDouble = (double) l / 10;
            data = resultDouble + "";
        }
        intent.putExtra(intentName, data);
        context.sendBroadcast(intent);
    }


    /**
     * 重量数据发出
     *
     * @param length     数据截取长度
     * @param context    context
     * @param intent     intent
     * @param bytes      数据
     * @param intentName intentName
     */
    private static void sendWeightBroadcast(int length, Context context, Intent intent, byte[] bytes, String intentName) {
        String data = "";
        byte[] result = new byte[length];
        try {
            System.arraycopy(bytes, 3, result, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String byteArrayToString = DataManageUtils.byteArrayToString(result);
        String fuHao = Objects.requireNonNull(byteArrayToString).substring(0, 2);
        String weight = byteArrayToString.substring(2, 6);
//        String xiShu = byteArrayToString.substring(6);
        BigInteger weightB = new BigInteger(weight, 16);
//        BigInteger xiShuB = new BigInteger(xiShu, 16);
        int weightInt = weightB.intValue();
//        int xiShuInt = xiShuB.intValue();
        double f1 = (double) weightInt / 100;
        BigDecimal b = new BigDecimal(f1);
        double resultDouble = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if ("01".equals(fuHao)) {
            data = "-" + resultDouble;
        } else {
            data = resultDouble + "";
        }

        intent.putExtra(intentName, data);
        context.sendBroadcast(intent);
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

        String ch4 = DataManageUtils.toAsciiString(result2);
        String o2 = DataManageUtils.toAsciiString(result2);
        String co = DataManageUtils.toAsciiString(result3);
        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_L, ch4);
        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_W, o2);
        intent.putExtra(BluetoothLeService.NOTIFICATION_DATA_H, co);
        context.sendBroadcast(intent);

    }


    /**
     * 校验位检验
     *
     * @param result 报头一直加到校验位前
     * @param proof  校验位
     * @return boolean
     */
    private static boolean getProof(int result, byte proof) {
        String toHexString = Integer.toHexString(result).toUpperCase();
        if (toHexString.length() == 1) {
            toHexString = "0" + toHexString;
        }
        if (toHexString.length() > 2) {
            toHexString = toHexString.substring(toHexString.length() - 2, toHexString.length());
        }
        byte[] hexString2Bytes = DataManageUtils.HexString2Bytes(toHexString);
        return hexString2Bytes[hexString2Bytes.length - 1] == proof;
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

        Log.d("ZM", "result:" + result);

        BaseBleApplication.writeCharacteristic3(DataManageUtils.HexString2Bytes(result));

    }
}
