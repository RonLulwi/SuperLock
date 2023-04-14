package com.ronlu.superlock;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class LockManager {

    private SensorManager sensorManager;
    private Sensor proximitySensor, compassSensor;

    private CallBack_updateLock callBack_updateLock;

    public void setCallBack_updateLock(CallBack_updateLock callBack_updateLock){
        this.callBack_updateLock = callBack_updateLock;
    }


    private  SensorEventListener compassSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            //Todo detect when device is pointing north
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    private SensorEventListener proximitySensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // method to check accuracy changed in sensor.
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0)
                    callBack_updateLock.updateLock(0);
            }
        }
    };

    public LockManager(Sensor proximitySensor, SensorManager sensorManager) {
        this.proximitySensor = proximitySensor;
        this.sensorManager = sensorManager;
        sensorManager.registerListener(proximitySensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public LockManager(Context context) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        this.compassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(proximitySensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(compassSensorEventListener, compassSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public interface CallBack_updateLock{
        void updateLock(int index);
    }
}


