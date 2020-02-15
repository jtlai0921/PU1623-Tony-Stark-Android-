package com.xinlv;

import java.util.Timer;
import java.util.TimerTask;

import com.xinlv.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SurfaceHolder.Callback,PreviewCallback{
	
	private DrawChart view;
	/**
	 * 定時分析攝影機資料的定時器
	 */
	private Timer timer = new Timer(); 
	private TimerTask task; 
	public static TextView textView;
  @SuppressLint("HandlerLeak")
private Handler handler=new Handler(){
	  public void handleMessage(Message msg) {
		  //呼叫相機回調接口由於MainActivity已經實現了回調接口，所以MainActivity.this即可
	   camera.setOneShotPreviewCallback(MainActivity.this);
	   view.invalidate();
	  };
  };
	private static Camera camera = null;
	private Button  oprenCamer,closeCamer;
    private static int heartbeat=0;
    public static void setHeratbeat(int heart){
    	heartbeat=heart;
    }
    public static int getHeartbeat(){
    	return heartbeat;
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//透過IDfindview控制項
		oprenCamer=(Button) findViewById(R.id.open);
		closeCamer=(Button) findViewById(R.id.close);
		textView=(TextView) findViewById(R.id.heart_text);
		oprenCamer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//攝影機開始捕捉預覽框資料
				 camera.startPreview();
				 //取得相機目前設定參數
				 Camera.Parameters parameter = camera.getParameters();  
				 //啟動閃光燈。
				 parameter.setFlashMode(Parameters.FLASH_MODE_TORCH); 
				camera.setParameters(parameter);
				//開啟定時器
				startTimer();
			}
		});
		closeCamer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//取得攝影機參數
				Camera.Parameters parameter = camera.getParameters();  
				//設定攝影機參數 關閉閃光燈
				 parameter.setFlashMode(Parameters.FLASH_MODE_OFF); 
				 
				camera.setParameters(parameter);
				pauseTimer();
			}
		});
		init();
	}
	 public void onPause() {
	        super.onPause();
	        //暫停定時器
	        pauseTimer();
	        //將相機的回調接口設為NULL即不再接受回調資料
	        camera.setPreviewCallback(null);
	        camera.stopPreview();
	        //中斷相機並釋放物件
	        camera.release();
	        camera = null;
	    }
	 /***
	  * 開始測試方法
	  */
	public void startTimer(){
		if(timer==null){
			timer=new Timer();
		}
		if(task==null){
			task = new TimerTask() { 
			    @Override
			    public void run() { 
			        // TODO Auto-generated method stub 
			        Message message = new Message(); 
			        message.what = 1; 
			        handler.sendMessage(message); 
			    } 
			};
		}
		if(timer!=null&&task!=null){
			timer.schedule(task, 500, 500);
		}
	}
	 /***
	  * 結束測試方法
	  */
	public void pauseTimer(){
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
		if(task!=null){
			task.cancel();
			task=null;
		}
	}
 @Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	//開啟攝影機
	 camera = Camera.open();
	 
}
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
	}
	/***
	 * 攝影機資料回調接口
	 */
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		Log.i("onPreviewFrame", "onPreviewFrame資料接口回調");
        if (data == null) throw new NullPointerException();
        //取得攝影機資料的尺寸
        Camera.Size size = camera.getParameters().getPreviewSize();
        //若果取得不到攝影機資料尺寸拋出例外
        if (size == null) throw new NullPointerException();
        //取得攝影機資料長度與高度
        int width = size.width;
        int height = size.height;
        //接口回調並取得到相機資料後呼叫YUV轉換RGB方法將資料進行轉換。
        int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);
        Log.i("imgAvg", "imgAvg========"+imgAvg);
        //限定心率值範圍，只有值在合理的也就是40到150之間的值才會顯示出來
        if(imgAvg>40&&imgAvg<151){
        	//將Imagvg寫入圖形方法中做出不規則波線
        	setHeratbeat(imgAvg);
        	textView.setText(""+imgAvg+"次/m");
        }else{
        	setHeratbeat(0);
        	textView.setText("請將手指覆蓋攝影機");
        }
    }
	  private void init() {  
		  //起始化曲線圖
	        LinearLayout layout=(LinearLayout) findViewById(R.id.root);  
	        view = new DrawChart(this);  
	        view.invalidate();  
	        layout.addView(view);
	    }  
}
