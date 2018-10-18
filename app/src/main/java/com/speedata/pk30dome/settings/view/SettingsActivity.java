package com.speedata.pk30dome.settings.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.speedata.pk30dome.MyApp;
import com.speedata.pk30dome.R;
import com.speedata.pk30dome.base.BaseActivity;
import com.speedata.pk30dome.utils.ToastUtils;

/**
 * @author xuyan  右上角设置页面
 */
public class SettingsActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener {

    private SeekBar searchBar;
    private AudioManager audioManager;
    private int currentBell;
    private int curSound;
    private MediaPlayer mMediaPlayer;
    private VolumeReceiver receiver;

    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initToolBar() {
        mTitle.setText("设置页");
        mEnd.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

        searchBar = findViewById(R.id.seekBar);

        mMediaPlayer = MediaPlayer.create(this, R.raw.here);
        mMediaPlayer
                .setOnCompletionListener(mediaPlayer -> {
                    setVolumeControlStream(AudioManager.STREAM_SYSTEM);
                });
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // mMediaPlayer.start();

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentBell = audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        searchBar.setProgress(currentBell);
        int max = audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        searchBar.setMax(max);
        searchBar.setOnSeekBarChangeListener(this);
        curSound = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        receiver = new VolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        MyApp.getInstance().registerReceiver(receiver, filter);

        //radio部分
        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);

        radioGroup.setOnCheckedChangeListener(this);
        //sp保存状态
        radioButton1.setChecked(true);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        currentBell = audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        curSound = searchBar.getProgress();
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                curSound, 0);
        int result = currentBell - progress;
        System.out.println("---progress_music_result:" + result);
        if (result > 0) {
            for (int i = 0; i < result; i++) {
                audioManager.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_PLAY_SOUND);
                int current = audioManager
                        .getStreamVolume(AudioManager.STREAM_MUSIC);
                setVolumeControlStream(AudioManager.STREAM_MUSIC);
                curSound = searchBar.getProgress();
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        curSound, 0);
                System.out.println("---progress_music:" + i
                        + " ---change_after=" + current);
            }
        } else {
            for (int i = 0; i > result; i--) {
                audioManager.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_PLAY_SOUND);
                setVolumeControlStream(AudioManager.STREAM_MUSIC);
                curSound = searchBar.getProgress();
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        curSound, 0);
                int current = audioManager
                        .getStreamVolume(AudioManager.STREAM_MUSIC);
                System.out.println("---progress_music:" + progress
                        + " ---change_after=" + current);
            }
        }


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioButton1:
                //sp保存结果
                ToastUtils.showShortToastSafe("radioButton1");
                break;
            case R.id.radioButton2:
                //sp保存结果
                ToastUtils.showShortToastSafe("radioButton2");
                break;
            case R.id.radioButton3:
                //sp保存结果
                ToastUtils.showShortToastSafe("radioButton3");
                break;
            case R.id.radioButton4:
                //sp保存结果
                ToastUtils.showShortToastSafe("radioButton4");
                break;
            default:
                break;
        }

    }

    private class VolumeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("android.media.VOLUME_CHANGED_ACTION".equals(intent.getAction())) {
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                searchBar.setProgress(currentVolume);
            }
        }
    }

    @Override
    protected void onDestroy() {
        MyApp.getInstance().unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

}
