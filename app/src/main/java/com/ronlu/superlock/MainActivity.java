package com.ronlu.superlock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText main_EDT_password;
    private AppCompatButton main_BTN_password;
    private int[] locksPins;
    private LockManager lockManager;
    private Thread soundStateThread, chargingStateThread;
    private int batteryLevel;

    LockManager.CallBack_updateLock callBack_updateLock = new LockManager.CallBack_updateLock() {
        @Override
        public void updateLock(int index) {
            locksPins[index] = 1;
            switch (index){
                case 0:
                    Log.d("pttt", "Lock[0] = 1");
                    break;
                case 1:
                    Log.d("pttt", "Lock[1] = 1");
                    break;
                case 2:
                    Log.d("pttt", "Lock[2] = 1");
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();


    }

    private void findViews() {
        main_EDT_password = findViewById(R.id.main_EDT_password);
        main_BTN_password = findViewById(R.id.main_BTN_password);
    }

    private void initViews() {
        locksPins = new int[4];
        lockManager = new LockManager(this);
        lockManager.setCallBack_updateLock(this.callBack_updateLock);
        initSoundThread();
        initChargingThread();
    }

    private void initSoundThread(){
        Thread soundStateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()){
                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    if( audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
                        locksPins[2] = 1;
                        Log.d("pttt",  "RINGER_MODE_NORMAL");
                    }else
                        Log.d("pttt", " not --> RINGER_MODE_NORMAL");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        soundStateThread.start();
    }

    private void initChargingThread(){
        chargingStateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()){
                    IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                    Intent batteryStatus = registerReceiver(null, filter);
                    int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
                    Log.d("pttt", isCharging+"");



                    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    batteryLevel = level * 100 / scale;

                    Log.d("pttt", "level: " + batteryLevel);


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        chargingStateThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundStateThread.interrupt();
        chargingStateThread.interrupt();
    }
}