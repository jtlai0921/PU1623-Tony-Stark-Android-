package dfzy.jiaSCH;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

import dfzy.jiaSCH.R;

import android.app.Activity;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
public class jiaSCH extends Activity {
    TextView myTextView1;//x��V���[�t��
    TextView myTextView2;//y��V���[�t��
    TextView myTextView3;//z��V���[�t��
    //SensorManager mySensorManager;//SensorManager����Ѧ�
    SensorManagerSimulator mySensorManager;//�ŧiSensorManagerSimulator����,�����ɥ�
    @Override 
    public void onCreate(Bundle savedInstanceState) {//���s�w�qonCreate��k
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);//�]�w�ثe���ϥΪ̬ɭ�
        myTextView1 = (TextView) findViewById(R.id.myTextView1);//�o��myTextView1���Ѧ�
        myTextView2 = (TextView) findViewById(R.id.myTextView2);//�o��myTextView2���Ѧ�
        myTextView3 = (TextView) findViewById(R.id.myTextView3);//�o��myTextView3���Ѧ�
        //mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);//��oSensorManager
        //�����ɥ�
        mySensorManager = SensorManagerSimulator.getSystemService(this, SENSOR_SERVICE);
        mySensorManager.connectSimulator();				//�PSimulator���A���s�u
    }
    private SensorListener mySensorListener = new SensorListener(){
    	@Override
    	public void onAccuracyChanged(int sensor, int accuracy) {}	//���s�w�qonAccuracyChanged��k
    	@Override
    	public void onSensorChanged(int sensor, float[] values) {		//���s�w�qonSensorChanged��k
    		if(sensor == SensorManager.SENSOR_ACCELEROMETER){//�u�ˬd�[�t�ת��ܤ�
    			myTextView1.setText("x��V�W���[�t�׬��G"+values[0]);	//�N���R��x�����ܨ�TextView
    			myTextView2.setText("y��V�W���[�t�׬��G"+values[1]);	//�N���R��y�����ܨ�TextView
    			myTextView3.setText("z��V�W���[�t�׬��G"+values[2]);	//�N���R��x�����ܨ�TextView
    		}
    	}
    };
    @Override
    protected void onResume() {//���s�w�q��onResume��k
    	mySensorManager.registerListener(//�n����ť
    			mySensorListener, //��ť��SensorListener����
    			SensorManager.SENSOR_ACCELEROMETER,//�P���������A���[�t��
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