package bucur.ro.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import bucur.ro.sensors.listeners.AccelerometerListener;
import bucur.ro.sensors.listeners.MagnetometerListener;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor acc;
    private Sensor mag;
    private AccelerometerListener accelerometerListener;
    private MagnetometerListener magnetometerListener;
    private int stepCount = 0;
    private double accelerometerResult[];
    private float magneometerResult[];
    private double mRotationInDegress;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        initSensors();
    }

    public void initSensors(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        magnetometerListener = new MagnetometerListener(sensorManager);
        accelerometerListener = new AccelerometerListener(sensorManager);

        sensorManager.registerListener(accelerometerListener,acc, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(magnetometerListener, mag, SensorManager.SENSOR_DELAY_GAME);
        new Thread() {
            public void run() {
                while (true) {
                    accelerometerResult = accelerometerListener.getmAccelResult();
                    magneometerResult = magnetometerListener.getMagnetometer();
                    if(accelerometerListener.isStepDetected()){
                        stepCount++;
                        accelerometerListener.setStepDetected(false);
                        float []gravity = new float[3];
                        for (int i = 0 ; i < accelerometerResult.length; i++)
                        {
                            gravity[i] = (float) accelerometerResult[i];
                        }
                        if(accelerometerResult != null && magneometerResult != null){
                            float rotationMatrix[] = new float[9];
                            boolean success = SensorManager.getRotationMatrix(rotationMatrix, null, gravity, magneometerResult);
                            if(success){
                                float orientationMatrix[] = new float[3];
                                SensorManager.getOrientation(rotationMatrix, orientationMatrix);
                                float rotationInRadians = orientationMatrix[0];
                                mRotationInDegress = Math.toDegrees(rotationInRadians);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setText("Steps: " + stepCount + " Rotation: " + mRotationInDegress);
                                    }
                                });

                                magneometerResult = null;
                                accelerometerResult = null;
                            }
                        }
                    }

                }
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        accelerometerListener.startAccelerometer();
        magnetometerListener.startMagnetometer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        accelerometerListener.stopAccelerometer();
        magnetometerListener.stopMagnetometer();
    }

}
