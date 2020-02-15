package dfzy.wenduCH;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

import dfzy.wenduCH.R;

import android.app.Activity;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
public class wenduCH extends Activity {
    TextView myTextView1;//�ثe�ū�
    //SensorManager mySensorManager;//SensorManager����Ѧ�
    SensorManagerSimulator mySensorManager;//�ŧiSensorManagerSimulator����,�����ɥ�
    @Override 
    public void onCreate(Bundle savedInstanceState) {//���s�w�qonCreate��k
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);//�]�w�ثe���ϥΪ̬ɭ�
        myTextView1 = (TextView) findViewById(R.id.myTextView1);//�o��myTextView1���Ѧ�
        //mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);//��oSensorManager
        //�����ɥ�
        mySensorManager = SensorManagerSimulator.getSystemService(this, SENSOR_SERVICE);
        mySensorManager.connectSimulator();				//�PSimulator�s�u
    }
    private SensorListener mySensorListener = new SensorListener(){
    	@Override
    	public void onAccuracyChanged(int sensor, int accuracy) {}	//���s�w�qonAccuracyChanged��k
    	@Override
    	public void onSensorChanged(int sensor, float[] values) {		//���s�w�qonSensorChanged��k
    		if(sensor == SensorManager.SENSOR_TEMPERATURE){//�u�ˬd�ūת��ܤ�
    			myTextView1.setText("�ثe���ū׬��G"+values[0]);	//�N�ثe�ū���ܨ�TextView
    		}
    	}
    };
    @Override
    protected void onResume() {//���s�w�q��onResume��k
    	mySensorManager.registerListener(//�n����ť
    			mySensorListener, //��ť��SensorListener����
    			SensorManager.SENSOR_TEMPERATURE,//�P���������A���ū�
    			SensorManager.SENSOR_DELAY_UI//�P�����ƥ�ǻ����W��
    			);
    	super.onResume();
    }	
    @Override
    protected void onPause() {//���s�w�qonPause��k
    	mySensorManager.unregisterListener(mySensorListener);//�����n����ť��
    	super.onPause();
    }
}