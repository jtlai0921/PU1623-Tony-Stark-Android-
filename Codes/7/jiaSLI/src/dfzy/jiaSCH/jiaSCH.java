package dfzy.jiaSCH;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

import dfzy.jiaSCH.R;

import android.app.Activity;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
public class jiaSCH extends Activity {
    TextView myTextView1;//x方向的加速度
    TextView myTextView2;//y方向的加速度
    TextView myTextView3;//z方向的加速度
    //SensorManager mySensorManager;//SensorManager物件參考
    SensorManagerSimulator mySensorManager;//宣告SensorManagerSimulator物件,除錯時用
    @Override 
    public void onCreate(Bundle savedInstanceState) {//重新定義onCreate方法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);//設定目前的使用者界面
        myTextView1 = (TextView) findViewById(R.id.myTextView1);//得到myTextView1的參考
        myTextView2 = (TextView) findViewById(R.id.myTextView2);//得到myTextView2的參考
        myTextView3 = (TextView) findViewById(R.id.myTextView3);//得到myTextView3的參考
        //mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);//獲得SensorManager
        //除錯時用
        mySensorManager = SensorManagerSimulator.getSystemService(this, SENSOR_SERVICE);
        mySensorManager.connectSimulator();				//與Simulator伺服器連線
    }
    private SensorListener mySensorListener = new SensorListener(){
    	@Override
    	public void onAccuracyChanged(int sensor, int accuracy) {}	//重新定義onAccuracyChanged方法
    	@Override
    	public void onSensorChanged(int sensor, float[] values) {		//重新定義onSensorChanged方法
    		if(sensor == SensorManager.SENSOR_ACCELEROMETER){//只檢查加速度的變化
    			myTextView1.setText("x方向上的加速度為："+values[0]);	//將分析的x資料顯示到TextView
    			myTextView2.setText("y方向上的加速度為："+values[1]);	//將分析的y資料顯示到TextView
    			myTextView3.setText("z方向上的加速度為："+values[2]);	//將分析的x資料顯示到TextView
    		}
    	}
    };
    @Override
    protected void onResume() {//重新定義的onResume方法
    	mySensorManager.registerListener(//登錄監聽
    			mySensorListener, //監聽器SensorListener物件
    			SensorManager.SENSOR_ACCELEROMETER,//感知器的型態為加速度
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