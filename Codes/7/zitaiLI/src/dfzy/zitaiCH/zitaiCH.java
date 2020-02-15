package dfzy.zitaiCH;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

import dfzy.zitaiCH.R;

import android.app.Activity;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
public class zitaiCH extends Activity {
    TextView myTextView1;//Yaw
    TextView myTextView2;//Pitch
    TextView myTextView3;//Roll
    //SensorManager mySensorManager;//SensorManager����Ѧ�
    SensorManagerSimulator mySensorManager;//�ŧiSensorManagerSimulator����,�����ɥ�
    @Override 
    public void onCreate(Bundle savedInstanceState) {//���s�w�qonCreate��k
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);//�]�w�ثe���ϥΪ̬ɭ�
        myTextView1 = (TextView) findViewById(R.id.myTextView1);//�o��myTextView1���Ѧ�
        myTextView2 = (TextView) findViewById(R.id.myTextView2);//�o��myTextView2���Ѧ�
        myTextView3 = (TextView) findViewById(R.id.myTextView3);//�o��myTextView3���Ѧ�
        //mySensorManager = 
        //	(SensorManager)getSystemService(SENSOR_SERVICE);//��oSensorManager
        //�����ɥ�
        mySensorManager = SensorManagerSimulator.getSystemService(this, SENSOR_SERVICE);
        mySensorManager.connectSimulator();				//�PSimulator���A���s�u
    }
    private SensorListener mySensorListener = new SensorListener(){
    	@Override
    	public void onAccuracyChanged(int sensor, int accuracy) {}	//���s�w�qonAccuracyChanged��k
    	@Override
    	public void onSensorChanged(int sensor, float[] values) {		//���s�w�qonSensorChanged��k
    		if(sensor == SensorManager.SENSOR_ORIENTATION){//�u�ˬd���A���ܤ�
    			myTextView1.setText("Yaw���G"+values[0]);	//�N�����ܨ�TextView
    			myTextView2.setText("Pitch���G"+values[1]);	//�N�����ܨ�TextView
    			myTextView3.setText("Roll���G"+values[2]);	//�N�����ܨ�TextView
    		}
    	}
    };
    @Override
    protected void onResume() {//���s�w�q��onResume��k
    	mySensorManager.registerListener(//�n����ť
    			mySensorListener, //��ť��SensorListener����
    			SensorManager.SENSOR_ORIENTATION,//�P���������A�����A
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