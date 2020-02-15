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
	int[] searchMsgId=//�U�غ�k
			{
			R.string.depthFirstSearch,R.string.breadthFirstSearch,
			R.string.breadthFirstSearchA,R.string.Dijkstra,
			R.string.DijkstraA
			};
	int[] targetId=//�ت��I
			{
					R.string.tA,R.string.tB,R.string.tC,R.string.tD,R.string.tE
			};
	MySurfaceView mySurfaceView;//�ŧi�Ѧ�
	Button button;//�}�l���s
	TextView textViewSybz;//�ϥΨB��
	TextView textViewLjcd;//���|����
	Handler hd;//�T���B�z��
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mySurfaceView = new MySurfaceView(this);
        //this.setContentView(mySurfaceView);
        LinearLayout ly=(LinearLayout)findViewById(R.id.LinearLayout02);
        ly.addView(mySurfaceView);
        
        button=(Button)findViewById(R.id.Button01);//�z�LID��oButton
        textViewSybz=(TextView)findViewById(R.id.TextView01);//�z�LID��o�ϥΨB�JTextView
        textViewLjcd=(TextView)findViewById(R.id.TextView02);//�z�LID��o�ϥΨB�JTextView
        Spinner spinnerSearch=(Spinner)findViewById(R.id.Spinner01);//��o�j�M��k�U�ԲM��
        
        //��spinnerSearch�ǳƤ��e�����d
        BaseAdapter ba=new BaseAdapter()
        {
			@Override
			public int getCount() {
				return 5;//�`�@���ӿﶵ
			}

			@Override
			public Object getItem(int arg0) { return null; }

			@Override
			public long getItemId(int arg0) { return 0; }

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				//�_�l��LinearLayout
				LinearLayout ll=new LinearLayout(MyActivity.this);
				ll.setOrientation(LinearLayout.HORIZONTAL);		//�]�w�¦V	

				
				//�_�l��TextView
				TextView tv=new TextView(MyActivity.this);
				tv.setText(" "+getResources().getText(searchMsgId[arg0]));//�]�w���e
				tv.setTextSize(20);//�]�w�r���j�p
				tv.setTextColor(Color.BLACK);//�]�w�r���¦�
				ll.addView(tv);//�s�W��LinearLayout��
				
				return ll;
			}
		};
		spinnerSearch.setAdapter(ba);//��Spinner�]�w���e�����d
		
		//�]�w�ﶵ�Ŀ諸��ť��
		spinnerSearch.setOnItemSelectedListener(
           new OnItemSelectedListener()
           {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {//���s�w�q�ﶵ�Q�Ŀ�ƥ󪺳B�z��k
				mySurfaceView.game.algorithmId=arg2;
				Log.d(mySurfaceView.game.algorithmId+"", "mySurfaceView.game.algorithmId");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }        	   
           }
        );
        
        Spinner spinnerTarget=(Spinner)findViewById(R.id.Spinner02);//��o�j�M��k�U�ԲM��
        //��spinnerTarget�ǳƤ��e�����d
        BaseAdapter baTarget=new BaseAdapter()
        {
			@Override
			public int getCount() {
				return 5;//�`�@���ӿﶵ
			}

			@Override
			public Object getItem(int arg0) { return null; }

			@Override
			public long getItemId(int arg0) { return 0; }

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				//�_�l��LinearLayout
				LinearLayout ll=new LinearLayout(MyActivity.this);
				ll.setOrientation(LinearLayout.HORIZONTAL);		//�]�w�¦V	

				
				//�_�l��TextView
				TextView tv=new TextView(MyActivity.this);
				tv.setText(" "+getResources().getText(targetId[arg0]));//�]�w���e
				tv.setTextSize(20);//�]�w�r���j�p
				tv.setTextColor(Color.BLACK);//�]�w�r���¦�
				ll.addView(tv);//�s�W��LinearLayout��
				
				return ll;
			}
		};
		spinnerTarget.setAdapter(baTarget);//��Spinner�]�w���e�����d
		//�]�w�ﶵ�Ŀ諸��ť��
		spinnerTarget.setOnItemSelectedListener(
           new OnItemSelectedListener()
           {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {//���s�w�q�ﶵ�Q�Ŀ�ƥ󪺳B�z��k
				mySurfaceView.game.target=MapList.targetA[arg2];
				mySurfaceView.game.clearState();//�M�z���A
				mySurfaceView.repaint(mySurfaceView.getHolder());//��ø 
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
						
						mySurfaceView.game.runAlgorithm();//�I�s��k
						button.setClickable(false);//�]�w�����i�I��
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
					 	case 1://�ܧ�ϥΨB��
					 	case 2:
					 	case 3:
					 	case 4:
					 	case 5:textViewSybz.setText("�ϥΨB�ơG"+mySurfaceView.game.tempCount);break;
					 	case 6:textViewLjcd.setText("���|���סG"+mySurfaceView.LjcdCount);break;//�ܧ���|����
					 }
				 }
		 };
		 
		
    }
}