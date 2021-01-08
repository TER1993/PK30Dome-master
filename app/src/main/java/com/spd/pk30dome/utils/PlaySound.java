package com.spd.pk30dome.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.spd.pk30dome.R;


/**
 * 播放扫描提示音
 *
 * @author lenovo-pc
 * @date 2017/8/10
 */

public class PlaySound {

    private static SparseIntArray mapSRC;
    private static SoundPool sp;
    public static final int channel_1 = 1;
    public static final int channel_2 = 2;
    public static final int channel_3 = 3;
    public static final int channel_4 = 4;
    public static final int channel_5 = 5;
    public static final int channel_6 = 6;
    public static final int channel_7 = 7;
    public static final int channel_8 = 8;
    public static final int channel_9 = 9;
    public static final int channel_10 = 10;
    public static final int channel_11 = 11;
    public static final int channel_12 = 12;
    public static final int channel_13 = 13;
    public static final int channel_14 = 14;
    public static final int channel_15 = 15;
    public static final int channel_16 = 16;

    public static int NO_CYCLE = 0;

    //初始化声音池
    public static void initSoundPool(Context context) {
        sp = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 0);
        //mapSRC = new HashMap<>();
        mapSRC = new SparseIntArray();
        //请刷新二维码
        mapSRC.put(channel_1, sp.load(context, R.raw.woring_batter, 0));


    }


    /**
     * 播放声音池的声音
     */
    public static void play(int sound, int number) {
        //播放的声音资源
        sp.play(mapSRC.get(sound),
                //左声道，范围为0--1.0
                1.0f,
                //右声道，范围为0--1.0
                1.0f,
                //优先级，0为最低优先级
                0,
                //循环次数,0为不循环
                number,
                //播放速率，1为正常速率
                1);
    }

}
