package com.AI;
import com.AI.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
public class MyActivity extends Activity {
	int[] searchMsgId=//各種算法
			{
			R.string.depthFirstSearch,R.string.breadthFirstSearch,
			R.string.breadthFirstSearchA,R.string.Dijkstra,
			R.string.DijkstraA
			};
	int[] targetId=//目的點
			{
					R.string.tA,R.string.tB,R.string.tC,R.string.tD,R.string.tE
			};
	MySurfaceView mySurfaceView;//宣告參考
	Button button;//開始按鈕
	TextView textViewSybz;//使用步數
	TextView textViewLjcd;//路徑長度
	Handler hd;//訊息處理器
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mySurfaceView = new MySurfaceView(this);
        //this.setContentView(mySurfaceView);
        LinearLayout ly=(LinearLayout)findViewById(R.id.LinearLayout02);
        ly.addView(mySurfaceView);
        
        button=(Button)findViewById(R.id.Button01);//透過ID獲得Button
        textViewSybz=(TextView)findViewById(R.id.TextView01);//透過ID獲得使用步驟TextView
        textViewLjcd=(TextView)findViewById(R.id.TextView02);//透過ID獲得使用步驟TextView
        Spinner spinnerSearch=(Spinner)findViewById(R.id.Spinner01);//獲得搜尋方法下拉清單
        
        //為spinnerSearch準備內容介面卡
        BaseAdapter ba=new BaseAdapter()
        {
			@Override
			public int getCount() {
				return 5;//總共五個選項
			}

			@Override
			public Object getItem(int arg0) { return null; }

			@Override
			public long getItemId(int arg0) { return 0; }

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				//起始化LinearLayout
				LinearLayout ll=new LinearLayout(MyActivity.this);
				ll.setOrientation(LinearLayout.HORIZONTAL);		//設定朝向	

				
				//起始化TextView
				TextView tv=new TextView(MyActivity.this);
				tv.setText(" "+getResources().getText(searchMsgId[arg0]));//設定內容
				tv.setTextSize(20);//設定字型大小
				tv.setTextColor(Color.BLACK);//設定字型黑色
				ll.addView(tv);//新增到LinearLayout中
				
				return ll;
			}
		};
		spinnerSearch.setAdapter(ba);//為Spinner設定內容介面卡
		
		//設定選項勾選的監聽器
		spinnerSearch.setOnItemSelectedListener(
           new OnItemSelectedListener()
           {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {//重新定義選項被勾選事件的處理方法
				mySurfaceView.game.algorithmId=arg2;
				Log.d(mySurfaceView.game.algorithmId+"", "mySurfaceView.game.algorithmId");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }        	   
           }
        );
        
        Spinner spinnerTarget=(Spinner)findViewById(R.id.Spinner02);//獲得搜尋方法下拉清單
        //為spinnerTarget準備內容介面卡
        BaseAdapter baTarget=new BaseAdapter()
        {
			@Override
			public int getCount() {
				return 5;//總共五個選項
			}

			@Override
			public Object getItem(int arg0) { return null; }

			@Override
			public long getItemId(int arg0) { return 0; }

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				//起始化LinearLayout
				LinearLayout ll=new LinearLayout(MyActivity.this);
				ll.setOrientation(LinearLayout.HORIZONTAL);		//設定朝向	

				
				//起始化TextView
				TextView tv=new TextView(MyActivity.this);
				tv.setText(" "+getResources().getText(targetId[arg0]));//設定內容
				tv.setTextSize(20);//設定字型大小
				tv.setTextColor(Color.BLACK);//設定字型黑色
				ll.addView(tv);//新增到LinearLayout中
				
				return ll;
			}
		};
		spinnerTarget.setAdapter(baTarget);//為Spinner設定內容介面卡
		//設定選項勾選的監聽器
		spinnerTarget.setOnItemSelectedListener(
           new OnItemSelectedListener()
           {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {//重新定義選項被勾選事件的處理方法
				mySurfaceView.game.target=MapList.targetA[arg2];
				mySurfaceView.game.clearState();//清理狀態
				mySurfaceView.repaint(mySurfaceView.getHolder());//重繪 
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }        	   
           }
        );
		button.setOnClickListener
		(
				new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						mySurfaceView.game.runAlgorithm();//呼叫方法
						button.setClickable(false);//設定為不可點擊
					}
				
					
				}
		);
		hd=new Handler()
		{
			 @Override
	        	public void handleMessage(Message msg)
				 {
					 super.handleMessage(msg);
					 switch(msg.what)
					 {
					 	case 1://變更使用步數
					 	case 2:
					 	case 3:
					 	case 4:
					 	case 5:textViewSybz.setText("使用步數："+mySurfaceView.game.tempCount);break;
					 	case 6:textViewLjcd.setText("路徑長度："+mySurfaceView.LjcdCount);break;//變更路徑長度
					 }
				 }
		 };
		 
		
    }
}