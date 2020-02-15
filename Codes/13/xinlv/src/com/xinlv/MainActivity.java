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
	 * �w�ɤ��R��v����ƪ��w�ɾ�
	 */
	private Timer timer = new Timer(); 
	private TimerTask task; 
	public static TextView textView;
  @SuppressLint("HandlerLeak")
private Handler handler=new Handler(){
	  public void handleMessage(Message msg) {
		  //�I�s�۾��^�ձ��f�ѩ�MainActivity�w�g��{�F�^�ձ��f�A�ҥHMainActivity.this�Y�i
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
		//�z�LIDfindview���
		oprenCamer=(Button) findViewById(R.id.open);
		closeCamer=(Button) findViewById(R.id.close);
		textView=(TextView) findViewById(R.id.heart_text);
		oprenCamer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//��v���}�l�����w���ظ��
				 camera.startPreview();
				 //���o�۾��ثe�]�w�Ѽ�
				 Camera.Parameters parameter = camera.getParameters();  
				 //�Ұʰ{���O�C
				 parameter.setFlashMode(Parameters.FLASH_MODE_TORCH); 
				camera.setParameters(parameter);
				//�}�ҩw�ɾ�
				startTimer();
			}
		});
		closeCamer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//���o��v���Ѽ�
				Camera.Parameters parameter = camera.getParameters();  
				//�]�w��v���Ѽ� �����{���O
				 parameter.setFlashMode(Parameters.FLASH_MODE_OFF); 
				 
				camera.setParameters(parameter);
				pauseTimer();
			}
		});
		init();
	}
	 public void onPause() {
	        super.onPause();
	        //�Ȱ��w�ɾ�
	        pauseTimer();
	        //�N�۾����^�ձ��f�]��NULL�Y���A�����^�ո��
	        camera.setPreviewCallback(null);
	        camera.stopPreview();
	        //���_�۾������񪫥�
	        camera.release();
	        camera = null;
	    }
	 /***
	  * �}�l���դ�k
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
	  * �������դ�k
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
	//�}����v��
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
	 * ��v����Ʀ^�ձ��f
	 */
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		Log.i("onPreviewFrame", "onPreviewFrame��Ʊ��f�^��");
        if (data == null) throw new NullPointerException();
        //���o��v����ƪ��ؤo
        Camera.Size size = camera.getParameters().getPreviewSize();
        //�Y�G���o������v����Ƥؤo�ߥX�ҥ~
        if (size == null) throw new NullPointerException();
        //���o��v����ƪ��׻P����
        int width = size.width;
        int height = size.height;
        //���f�^�ըè��o��۾���ƫ�I�sYUV�ഫRGB��k�N��ƶi���ഫ�C
        int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);
        Log.i("imgAvg", "imgAvg========"+imgAvg);
        //���w�߲v�Ƚd��A�u���Ȧb�X�z���]�N�O40��150�������Ȥ~�|��ܥX��
        if(imgAvg>40&&imgAvg<151){
        	//�NImagvg�g�J�ϧΤ�k�����X���W�h�i�u
        	setHeratbeat(imgAvg);
        	textView.setText(""+imgAvg+"��/m");
        }else{
        	setHeratbeat(0);
        	textView.setText("�бN����л\��v��");
        }
    }
	  private void init() {  
		  //�_�l�Ʀ��u��
	        LinearLayout layout=(LinearLayout) findViewById(R.id.root);  
	        view = new DrawChart(this);  
	        view.invalidate();  
	        layout.addView(view);
	    }  
}
