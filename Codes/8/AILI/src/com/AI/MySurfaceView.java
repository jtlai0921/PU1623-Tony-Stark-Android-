package com.AI;
import java.util.ArrayList;
import java.util.HashMap;

import com.AI.R;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
public class MySurfaceView extends SurfaceView
implements SurfaceHolder.Callback { //實現生命周期回調接口
	MyActivity mActivity;//activity參考
	Paint paint;//畫筆參考
	Game game=new Game(this,getHolder());//建立物件
	final float span=15.7f;//矩形大小
	final int LJCD_COUNT=6;//路徑長度
	int LjcdCount;//路徑長度
	public MySurfaceView(MyActivity mActivity) {
		super(mActivity);
		// TODO Auto-generated constructor stub
		this.mActivity=mActivity;
		this.getHolder().addCallback(this);//設定生命周期回調接口的實現者
		paint = new Paint();//建立畫筆
		paint.setAntiAlias(true);//開啟抗鋸齒
	}
	public void onDraw(Canvas canvas){
		int map[][]=game.map;//取得地圖
		int row=map.length;//地圖行數
		int col=map[0].length;//地圖列數
		canvas.drawARGB(255, 128, 128, 128);//設定背景彩色
		int width=(int)span*map.length;//畫布寬度
		int hight=(int)span*map[0].length;// 畫布長度
		canvas.setViewport(width, hight);//設定畫布大小
		for(int i=0;i<row;i++)//繪制地圖
		{
			for(int j=0;j<col;j++)
			{
				if(map[i][j]==1)
				{
					paint.setColor(Color.BLACK);	//設定畫筆彩色為黑色	
				}
				else if(map[i][j]==0)
				{
					paint.setColor(Color.WHITE);//設定畫筆彩色為白色
				}
				canvas.drawRect(2+j*(span+1),2+i*(span+1),2+j*(span+1)+span,2+i*(span+1)+span, paint);//繪制矩形
			}
		} 
		//繪制尋找過程
		ArrayList<int[][]> searchProcess=game.searchProcess;
		for(int k=0;k<searchProcess.size();k++)
		{
			int[][] edge=searchProcess.get(k);  
			paint.setColor(Color.BLACK);//設定畫筆彩色
			canvas.drawLine
			(
					edge[0][0]*(span+1)+span/2+2, edge[0][1]*(span+1)+span/2+2, 
					edge[1][0]*(span+1)+span/2+2, edge[1][1]*(span+1)+span/2+2, paint
			);
			
		}
		
		//繪制結果路徑
		if(
				mActivity.mySurfaceView.game.algorithmId==0||
				mActivity.mySurfaceView.game.algorithmId==1||
				mActivity.mySurfaceView.game.algorithmId==2
		)
		{//深度優先，廣度優先，廣度優先A*
			if(game.pathFlag)
			{
				HashMap<String,int[][]> hm=game.hm;		
				int[] temp=game.target;
				int count=0;//路徑長度計數器	
				while(true)
				{
					int[][] tempA=hm.get(temp[0]+":"+temp[1]);//取得結果路徑記錄
					paint.setColor(Color.BLACK);//設定畫筆黑色
					paint.setStrokeWidth(3);//設定畫筆寬度		
					canvas.drawLine//繪制線段
				    (
				    	tempA[0][0]*(span+1)+span/2+2,tempA[0][1]*(span+1)+span/2+2,
						tempA[1][0]*(span+1)+span/2+2,tempA[1][1]*(span+1)+span/2+2,paint
				    );

					
					count++;
					//判斷是否到出發點
					if(tempA[1][0]==game.source[0]&&tempA[1][1]==game.source[1])
					{
						break;
					}
					
					temp=tempA[1];			
				}
				LjcdCount=count;//記錄路徑長度
				mActivity.hd.sendEmptyMessage(LJCD_COUNT);//變更路徑長度
			}			
		}
		else if(
				mActivity.mySurfaceView.game.algorithmId==3||
				mActivity.mySurfaceView.game.algorithmId==4
		)
		{//Dijkstra路徑繪制
		    if(game.pathFlag)
		    {
		    	Log.d(game.pathFlag+"*****************", "dijkst");
		    	HashMap<String,ArrayList<int[][]>> hmPath=game.hmPath;
				ArrayList<int[][]> alPath=hmPath.get(game.target[0]+":"+game.target[1]);
				for(int[][] tempA:alPath)
				{
					paint.setColor(Color.BLACK);	
					paint.setStrokeWidth(3);
					canvas.drawLine
				    (
				    	tempA[0][0]*(span+1)+span/2+2,tempA[0][1]*(span+1)+span/2+2,
						tempA[1][0]*(span+1)+span/2+2,tempA[1][1]*(span+1)+span/2+2,paint
				    );			
				}
				LjcdCount=alPath.size();//記錄路徑長度
				mActivity.hd.sendEmptyMessage(LJCD_COUNT);//變更路徑長度
		    }
		}
		
		
		//繪制出發點
		Bitmap bitmapTmpS=BitmapFactory.decodeResource(mActivity.getResources(),R.drawable.source);
		canvas.drawBitmap(bitmapTmpS, game.source[0]*(span+1),game.source[1]*(span+1) , paint);
		//繪制目的點
		Bitmap bitmapTmpT=BitmapFactory.decodeResource(mActivity.getResources(),R.drawable.target);
		canvas.drawBitmap(bitmapTmpT, game.target[0]*(span+1),game.target[1]*(span+1), paint);
	}
	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}

	public void surfaceCreated(SurfaceHolder holder) {//建立時被呼叫
		Canvas canvas = holder.lockCanvas();//取得畫布
		try{
			synchronized(holder){
				onDraw(canvas);//繪制
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(canvas != null){
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	public void repaint(SurfaceHolder holder)
	{
		Canvas canvas = holder.lockCanvas();//取得畫布
		try{
			synchronized(holder){
				onDraw(canvas);//繪制
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(canvas != null){
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {//銷毀時被呼叫

	}
	
	
}
