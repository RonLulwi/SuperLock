package com.ronlu.superlock;

import android.content.ContentResolver;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import java.util.EventListener;

public class LockManager {

    private SensorManager sensorManager;
    private Sensor proximitySensor, magnetSensor, accelerometersSensor;

    private CallBack_updateLock callBack_updateLock;

    public void setCallBack_updateLock(CallBack_updateLock callBack_updateLock){
        this.callBack_updateLock = callBack_updateLock;
    }


    private  SensorEventListener compassSensorEventListener = new SensorEventListener() {
        private float[] mGravity = null;
        private float[] mGeomagnetic = null;

        @Override
        public void onSensorChanged(SensorEvent event) {
            //Todo detect when device is pointing north
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                mGravity = new float[3];
                System.arraycopy(event.values, 0, mGravity, 0, 3);

            }else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                mGeomagnetic = new float[3];
                System.arraycopy(event.values, 0, mGeomagnetic, 0, 3);
            }
            if(mGravity != null && mGeomagnetic != null){
                float[] rotationMatrix = new float[9];
                boolean success = SensorManager.getRotationMatrix(rotationMatrix, null, mGravity, mGeomagnetic);
                if (success){
                    float[] orientationMatrix = new float[3];
                    SensorManager.getOrientation(rotationMatrix, orientationMatrix);
                    float rotationInRadians = orientationMatrix[0];
                    double mRotationInDegress = Math.toDegrees(rotationInRadians);
                    if(mRotationInDegress < 0)
                        mRotationInDegress += 360;
                    if(mRotationInDegress >= 337.5 || mRotationInDegress < 22.5)
                        callBack_updateLock.updateLock(1);
                    mGravity = mGeomagnetic = null;
                }
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
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

    public LockManager(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometersSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(proximitySensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(compassSensorEventListener, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(compassSensorEventListener, accelerometersSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public interface CallBack_updateLock{
        void updateLock(int index);
    }
}


