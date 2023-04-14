package com.ronlu.superlock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText main_EDT_password;
    private AppCompatButton main_BTN_password;
    private int[] LockPins;
    private LockManager lockManager;

    LockManager.CallBack_updateLock callBack_updateLock = new LockManager.CallBack_updateLock() {
        @Override
        public void updateLock(int index) {
            LockPins[index] = 1;
            switch (index){
                case 0:
                    Log.d("pttt", "Lock[0] = 1");
                    break;
                case 1:
                    Log.d("pttt", "Lock[1] = 1");
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
        LockPins = new int[4];
        lockManager = new LockManager(this);
        lockManager.setCallBack_updateLock(this.callBack_updateLock);

//        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
//        if (proximitySensor == null){
//            Toast.makeText(this, "No proximity sensor found in device.", Toast.LENGTH_SHORT).show();
//            finish();
//        }else {
//            lockManager = new LockManager(proximitySensor, sensorManager);
//            lockManager.setCallBack_updateLock(this.callBack_updateLock);
//        }
    }
}