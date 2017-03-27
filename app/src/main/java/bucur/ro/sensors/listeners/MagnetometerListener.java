package bucur.ro.sensors.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by bucur on 3/25/2017.
 */

public class MagnetometerListener implements SensorEventListener {

    public static float magnetic[];
    private SensorManager sensorManager;
    private Sensor magnetometer;

    public MagnetometerListener(SensorManager sensorManager) {
        magnetic = new float[4];
        this.sensorManager = sensorManager;
        this.magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        magnetic[0] = event.values[0];
        magnetic[1] = event.values[1];
        magnetic[2] = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void startMagnetometer(){
        System.out.println("Magnetometru initializat");
        sensorManager.registerListener(this,magnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stopMagnetometer(){
        sensorManager.unregisterListener(this, magnetometer);
    }

    public float[] getMagnetometer(){
        return magnetic;
    }

}
