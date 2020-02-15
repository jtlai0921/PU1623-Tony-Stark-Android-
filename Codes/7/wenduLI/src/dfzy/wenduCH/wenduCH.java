package dfzy.wenduCH;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

import dfzy.wenduCH.R;

import android.app.Activity;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
public class wenduCH extends Activity {
    TextView myTextView1;//目前溫度
    //SensorManager mySensorManager;//SensorManager物件參考
    SensorManagerSimulator mySensorManager;//宣告SensorManagerSimulator物件,除錯時用
    @Override 
    public void onCreate(Bundle savedInstanceState) {//重新定義onCreate方法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);//設定目前的使用者界面
        myTextView1 = (TextView) findViewById(R.id.myTextView1);//得到myTextView1的參考
        //mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);//獲得SensorManager
        //除錯時用
        mySensorManager = SensorManagerSimulator.getSystemService(this, SENSOR_SERVICE);
        mySensorManager.connectSimulator();				//與Simulator連線
    }
    private SensorListener mySensorListener = new SensorListener(){
    	@Override
    	public void onAccuracyChanged(int sensor, int accuracy) {}	//重新定義onAccuracyChanged方法
    	@Override
    	public void onSensorChanged(int sensor, float[] values) {		//重新定義onSensorChanged方法
    		if(sensor == SensorManager.SENSOR_TEMPERATURE){//只檢查溫度的變化
    			myTextView1.setText("目前的溫度為："+values[0]);	//將目前溫度顯示到TextView
    		}
    	}
    };
    @Override
    protected void onResume() {//重新定義的onResume方法
    	mySensorManager.registerListener(//登錄監聽
    			mySensorListener, //監聽器SensorListener物件
    			SensorManager.SENSOR_TEMPERATURE,//感知器的型態為溫度
    			SensorManager.SENSOR_DELAY_UI//感知器事件傳遞的頻度
    			);
    	super.onResume();
    }	
    @Override
    protected void onPause() {//重新定義onPause方法
    	mySensorManager.unregisterListener(mySensorListener);//取消登錄監聽器
    	super.onPause();
    }
}