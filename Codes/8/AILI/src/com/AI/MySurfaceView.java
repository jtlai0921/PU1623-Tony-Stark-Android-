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
implements SurfaceHolder.Callback { //��{�ͩR�P���^�ձ��f
	MyActivity mActivity;//activity�Ѧ�
	Paint paint;//�e���Ѧ�
	Game game=new Game(this,getHolder());//�إߪ���
	final float span=15.7f;//�x�Τj�p
	final int LJCD_COUNT=6;//���|����
	int LjcdCount;//���|����
	public MySurfaceView(MyActivity mActivity) {
		super(mActivity);
		// TODO Auto-generated constructor stub
		this.mActivity=mActivity;
		this.getHolder().addCallback(this);//�]�w�ͩR�P���^�ձ��f����{��
		paint = new Paint();//�إߵe��
		paint.setAntiAlias(true);//�}�ҧܿ���
	}
	public void onDraw(Canvas canvas){
		int map[][]=game.map;//���o�a��
		int row=map.length;//�a�Ϧ��
		int col=map[0].length;//�a�ϦC��
		canvas.drawARGB(255, 128, 128, 128);//�]�w�I���m��
		int width=(int)span*map.length;//�e���e��
		int hight=(int)span*map[0].length;// �e������
		canvas.setViewport(width, hight);//�]�w�e���j�p
		for(int i=0;i<row;i++)//ø��a��
		{
			for(int j=0;j<col;j++)
			{
				if(map[i][j]==1)
				{
					paint.setColor(Color.BLACK);	//�]�w�e���m�⬰�¦�	
				}
				else if(map[i][j]==0)
				{
					paint.setColor(Color.WHITE);//�]�w�e���m�⬰�զ�
				}
				canvas.drawRect(2+j*(span+1),2+i*(span+1),2+j*(span+1)+span,2+i*(span+1)+span, paint);//ø��x��
			}
		} 
		//ø��M��L�{
		ArrayList<int[][]> searchProcess=game.searchProcess;
		for(int k=0;k<searchProcess.size();k++)
		{
			int[][] edge=searchProcess.get(k);  
			paint.setColor(Color.BLACK);//�]�w�e���m��
			canvas.drawLine
			(
					edge[0][0]*(span+1)+span/2+2, edge[0][1]*(span+1)+span/2+2, 
					edge[1][0]*(span+1)+span/2+2, edge[1][1]*(span+1)+span/2+2, paint
			);
			
		}
		
		//ø��G���|
		if(
				mActivity.mySurfaceView.game.algorithmId==0||
				mActivity.mySurfaceView.game.algorithmId==1||
				mActivity.mySurfaceView.game.algorithmId==2
		)
		{//�`���u���A�s���u���A�s���u��A*
			if(game.pathFlag)
			{
				HashMap<String,int[][]> hm=game.hm;		
				int[] temp=game.target;
				int count=0;//���|���׭p�ƾ�	
				while(true)
				{
					int[][] tempA=hm.get(temp[0]+":"+temp[1]);//���o���G���|�O��
					paint.setColor(Color.BLACK);//�]�w�e���¦�
					paint.setStrokeWidth(3);//�]�w�e���e��		
					canvas.drawLine//ø��u�q
				    (
				    	tempA[0][0]*(span+1)+span/2+2,tempA[0][1]*(span+1)+span/2+2,
						tempA[1][0]*(span+1)+span/2+2,tempA[1][1]*(span+1)+span/2+2,paint
				    );

					
					count++;
					//�P�_�O�_��X�o�I
					if(tempA[1][0]==game.source[0]&&tempA[1][1]==game.source[1])
					{
						break;
					}
					
					temp=tempA[1];			
				}
				LjcdCount=count;//�O�����|����
				mActivity.hd.sendEmptyMessage(LJCD_COUNT);//�ܧ���|����
			}			
		}
		else if(
				mActivity.mySurfaceView.game.algorithmId==3||
				mActivity.mySurfaceView.game.algorithmId==4
		)
		{//Dijkstra���|ø��
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
				LjcdCount=alPath.size();//�O�����|����
				mActivity.hd.sendEmptyMessage(LJCD_COUNT);//�ܧ���|����
		    }
		}
		
		
		//ø��X�o�I
		Bitmap bitmapTmpS=BitmapFactory.decodeResource(mActivity.getResources(),R.drawable.source);
		canvas.drawBitmap(bitmapTmpS, game.source[0]*(span+1),game.source[1]*(span+1) , paint);
		//ø��ت��I
		Bitmap bitmapTmpT=BitmapFactory.decodeResource(mActivity.getResources(),R.drawable.target);
		canvas.drawBitmap(bitmapTmpT, game.target[0]*(span+1),game.target[1]*(span+1), paint);
	}
	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}

	public void surfaceCreated(SurfaceHolder holder) {//�إ߮ɳQ�I�s
		Canvas canvas = holder.lockCanvas();//���o�e��
		try{
			synchronized(holder){
				onDraw(canvas);//ø��
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
		Canvas canvas = holder.lockCanvas();//���o�e��
		try{
			synchronized(holder){
				onDraw(canvas);//ø��
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

	public void surfaceDestroyed(SurfaceHolder arg0) {//�P���ɳQ�I�s

	}
	
	
}
