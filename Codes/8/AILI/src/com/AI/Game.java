package com.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.Stack;

import android.util.Log;
import android.view.SurfaceHolder;

public class Game{
	MySurfaceView mSurfaceView;//建立繪制類別參考
	int[][] map=MapList.map[0];//需要搜尋的地圖
	int[] source=MapList.source;//出發點座標
	int[] target=MapList.targetA[0];//目的點col,row
	int algorithmId=0;//算法代號，0--深度測試
	
	ArrayList<int[][]> searchProcess=new ArrayList<int[][]>();//搜尋過程
	Stack<int[][]> stack=new Stack<int[][]>();//深度優先所用堆疊
	LinkedList<int[][]> queue=new LinkedList<int[][]>();//廣度優先所用佇列
	PriorityQueue<int[][]> astarQueue=new PriorityQueue<int[][]>(100,new AxingComparator(this));//A*優先級佇列
	HashMap<String,int[][]> hm=new HashMap<String,int[][]>();//結果路徑記錄
	int[][] visited=new int[19][19];//0 未去過 1 已去過
	int[][] length=new int[19][19];//記錄路徑長度 for Dijkstra
	// 記錄到每個點的最短路徑for Dijkstra
	HashMap<String,ArrayList<int[][]>> hmPath=new HashMap<String,ArrayList<int[][]>>();
	
	boolean pathFlag=false;//true 找到路徑
	int timeSpan=10;//時間間隔
	SurfaceHolder holder;
	int[][] sequence=
	{
		{0,1},{0,-1},
		{-1,0},{1,0},
		{-1,1},{-1,-1},
		{1,-1},{1,1}
	};
	
	int tempCount;//記錄個搜尋方法所用步數
	final int DFS_COUNT=1;//深度優先使用步數標志
	final int BFS_COUNT=2;//廣度優先使用步數標志
	final int BFSASTAR_COUNT=3;//廣度優先使用步數標志
	final int DIJKSTRA_COUNT=4;//Dijkstra使用步數標志
	final int DIJKSTRASTAR_COUNT=5;//DijkstraA*使用步數標志
	public Game(MySurfaceView mSurfaceView,SurfaceHolder holder)
	{
		this.mSurfaceView=mSurfaceView;
		this.holder=holder;
	}
	public void clearState()//起始化個參考
	{
		searchProcess.clear();//清理搜尋過程清單
		stack.clear();//清理深度優先所用堆疊
		queue.clear();//清理廣度優先所用佇列
		astarQueue.clear();//清理A*優先級佇列
		hm.clear();//清理結果路徑記錄
		visited=new int[19][19];//起始化陣列
		pathFlag=false;	//尋找路經標志位
		hmPath.clear();//清理Dijkstra中記錄到每個點的最短路徑
		mSurfaceView.paint.setStrokeWidth(0);//起始化畫筆
		for(int i=0;i<length.length;i++)
		{
			for(int j=0;j<length[0].length;j++)
			{
				length[i][j]=9999;//設定起始路徑的長度（不可能這麼大）
			}
		}	
	}
	public void runAlgorithm()
	{
		clearState();//呼叫起始化方法
		switch(algorithmId)
		{
			case 0://深度優先算法
			  DFS();
			break;
			case 1://廣度優先算法			
			  BFS();
			break;
			case 2://廣度優先A*算法
			  BFSAStar();
			break;
			case 3://Dijkstra算法
			  Dijkstra();
			  Log.d("Dijkstra", "algorithmId="+algorithmId);
			break;
			case 4://DijkstraA*算法
			  DijkstraAStar();
			break;
		}		
	}
	
	public void DFS()//深度優先
	{
		new Thread()
		{
			public void run()
			{
				boolean flag=true;//執行緒標志位
				int[][] start=//起始化出發點座標
				{
					{source[0],source[1]},
					{source[0],source[1]}
				};
				stack.push(start);//入堆疊
				int count=0;//使用步數技術器
				while(flag)
				{
					
					int[][] currentEdge=stack.pop();//從堆疊中取出邊
					int[] tempTarget=currentEdge[1];//取出此邊的目的點
					
					//判斷目的點是否去過，若去過，則直接進入下次循環
					if(visited[tempTarget[1]][tempTarget[0]]==1)
					{
						continue;
					}
					count++;//計數器自加
					//表示目的點被存取過
					visited[tempTarget[1]][tempTarget[0]]=1;
					
					//將臨時目的點加入搜尋過程中
					searchProcess.add(currentEdge);
					//記錄此臨時節點的父節點
					hm.put(tempTarget[0]+":"+tempTarget[1],new int[][]{currentEdge[1],currentEdge[0]});
					//重繪畫布
					mSurfaceView.repaint(holder);
					//執行緒睡眠一定時間
					try{Thread.sleep(timeSpan);}catch(Exception e){e.printStackTrace();}
					
					//判斷是否到達目的點
					if(tempTarget[0]==target[0]&&tempTarget[1]==target[1])
					{
						break;
					}
					
					//將所有可能的邊入堆疊
					int currCol=tempTarget[0];//取邊節點
					int currRow=tempTarget[1];
					
					for(int[] rc:sequence)//掃描該點附近所有可能的邊
					{
						int i=rc[1];
						int j=rc[0];
						if(i==0&&j==0){continue;}//若為0，0結束該次循環
						if(currRow+i>=0&&currRow+i<19&&currCol+j>=0&&currCol+j<19&&
						map[currRow+i][currCol+j]!=1)//若在地圖內
						{
							int[][] tempEdge=
							{
								{tempTarget[0],tempTarget[1]},
								{currCol+j,currRow+i}
							};
							stack.push(tempEdge);//入堆疊
						}
					}
				}
				pathFlag=true;	//標志位設為true
				mSurfaceView.repaint(holder);//重繪畫布
				tempCount=count;//深度優先使用步數
				mSurfaceView.mActivity.hd.sendEmptyMessage(DFS_COUNT);//傳送訊息變更使用步數量
				mSurfaceView.mActivity.button.setClickable(true);	//設定button可以點擊
			}
		}.start();		
	}
	
	
	public void BFS()//廣度優先
	{
		new Thread()
		{
			public void run()
			{
				int count=0;//計數器
				boolean flag=true;//循環標志位
				int[][] start=//開始狀態
				{
					{source[0],source[1]},
					{source[0],source[1]}
				};
				queue.offer(start);//將開始點加入該佇列的尾端
				
				while(flag)
				{					
					int[][] currentEdge=queue.poll();//取得並移除標頭
					int[] tempTarget=currentEdge[1];//取出此邊的目的點
					
					//判斷是否去過，若去過則直接進入下次循環
					if(visited[tempTarget[1]][tempTarget[0]]==1)
					{
						continue;
					}
					count++;//計數器自加
					//將去過的點置為1
					visited[tempTarget[1]][tempTarget[0]]=1;
					
					//降臨時目的點加入搜尋過程
					searchProcess.add(currentEdge);
					//記錄此臨時節點的父節點
					hm.put(tempTarget[0]+":"+tempTarget[1],new int[][]{currentEdge[1],currentEdge[0]});
					//重繪畫布
					mSurfaceView.repaint(holder);
					//執行緒睡眠一定時間
					try{Thread.sleep(timeSpan);}catch(Exception e){e.printStackTrace();}
					
					//判斷是否為目的點
					if(tempTarget[0]==target[0]&&tempTarget[1]==target[1])
					{
						break;
					}
					
					//將所有可能的邊加入佇列列
					int currCol=tempTarget[0];
					int currRow=tempTarget[1];
					
					for(int[] rc:sequence)
					{
						int i=rc[1];
						int j=rc[0];
						
						if(i==0&&j==0){continue;}//若在地圖外面，進入下一次循環
						if(currRow+i>=0&&currRow+i<19&&currCol+j>=0&&currCol+j<19&&
						map[currRow+i][currCol+j]!=1)//若為地圖內的點
						{
							int[][] tempEdge=
							{
								{tempTarget[0],tempTarget[1]},
								{currCol+j,currRow+i}
							};
							queue.offer(tempEdge);//將改點加加入佇列列尾端
						}
					}
				}
				pathFlag=true;	//標志位設為true
				mSurfaceView.repaint(holder);//重繪畫布
				tempCount=count;//廣度優先使用步數
				mSurfaceView.mActivity.hd.sendEmptyMessage(BFS_COUNT);	//傳送訊息變更使用步數量
				mSurfaceView.mActivity.button.setClickable(true);//設定button鍵可以點擊
			}
		}.start();			
	}
	
	
	public void BFSAStar()//廣度優先A*
	{
		new Thread()
		{
			public void run()
			{
				boolean flag=true;
				int[][] start=//開始狀態
				{
					{source[0],source[1]},
					{source[0],source[1]}
				};
				astarQueue.offer(start);//將開始點加加入佇列列尾端
				int count=0;//計數器
				while(flag)
				{					
					int[][] currentEdge=astarQueue.poll();//取得標頭，並將標頭移除
					int[] tempTarget=currentEdge[1];//取此邊的目的點
					
					//判斷是否去過，若去過則直接進入下次循環
					if(visited[tempTarget[1]][tempTarget[0]]!=0)
					{
						continue;
					}
					count++;
					//表示目的點為存取過
					visited[tempTarget[1]][tempTarget[0]]=visited[currentEdge[0][1]][currentEdge[0][0]]+1;				
					//將臨時目的點加入搜尋過程中
					searchProcess.add(currentEdge);
					//記錄此臨時節點的父節點
					hm.put(tempTarget[0]+":"+tempTarget[1],new int[][]{currentEdge[1],currentEdge[0]});
					//重繪畫布
					mSurfaceView.repaint(holder);
					
					try{Thread.sleep(timeSpan);}catch(Exception e){e.printStackTrace();}
					
					//判斷是否為目的點
					if(tempTarget[0]==target[0]&&tempTarget[1]==target[1])
					{
						break;
					}
					
					//將所有可能的邊加加入佇列列
					int currCol=tempTarget[0];
					int currRow=tempTarget[1];
					
					for(int[] rc:sequence)
					{
						int i=rc[1];
						int j=rc[0];
						if(i==0&&j==0){continue;}
						if(currRow+i>=0&&currRow+i<19&&currCol+j>=0&&currCol+j<19&&
						map[currRow+i][currCol+j]!=1)
						{
							int[][] tempEdge=
							{
								{tempTarget[0],tempTarget[1]},
								{currCol+j,currRow+i}
							};
							astarQueue.offer(tempEdge);//加加入佇列列尾端
						}						
					}
				}
				pathFlag=true;	
				mSurfaceView.repaint(holder);
				tempCount=count;//廣度優先A*使用步數
				mSurfaceView.mActivity.hd.sendEmptyMessage(BFSASTAR_COUNT);//傳送訊息變更使用步數量
				mSurfaceView.mActivity.button.setClickable(true);	//設定button為可點				
			}
		}.start();				
	}
	
	
	public void Dijkstra()
	{
		new Thread()
		{
			public void run()
			{
				int count=0;//步驟計數器
				boolean flag=true;//搜尋循環設定
				//開始點
				int[] start={source[0],source[1]};//col,row	
				visited[source[1]][source[0]]=1;
				//計算此點所有可以到達點的路徑及長度
				for(int[] rowcol:sequence)
				{					
					int trow=start[1]+rowcol[1];
					int tcol=start[0]+rowcol[0];
					if(trow<0||trow>18||tcol<0||tcol>18)continue;
					if(map[trow][tcol]!=0)continue;
					
					//記錄路徑長度
					length[trow][tcol]=1;
					
					//計算路徑					
					String key=tcol+":"+trow;
					ArrayList<int[][]> al=new ArrayList<int[][]>();
					al.add(new int[][]{{start[0],start[1]},{tcol,trow}});
					hmPath.put(key,al);	
					
					//將去過的點記錄		
					searchProcess.add(new int[][]{{start[0],start[1]},{tcol,trow}});
					count++;			
				}	
				mSurfaceView.repaint(holder);//重繪
				outer:while(flag)
				{					
					//找到目前延伸點K 要求延伸點K為從開始點到此點目前路徑最短，且此點未檢查過
					int[] k=new int[2];
					int minLen=9999;
					for(int i=0;i<visited.length;i++)
					{
						for(int j=0;j<visited[0].length;j++)
						{
							if(visited[i][j]==0)
							{
								if(minLen>length[i][j])
								{
									minLen=length[i][j];
									k[0]=j;//col
									k[1]=i;//row
								}
							}
						}
					}
					
					//設定去過的點
					visited[k[1]][k[0]]=1;					
					
					//	重繪				
					mSurfaceView.repaint(holder);
					
					//取出開始點到K的路徑長度
					int dk=length[k[1]][k[0]];
					//取出開始點到K的路徑
					ArrayList<int[][]> al=hmPath.get(k[0]+":"+k[1]);
					
					//循環計算所有K點能直接到的點到開始點的路徑長度
					for(int[] rowcol:sequence)
					{
						//計算出新的要計算的點的座標
						int trow=k[1]+rowcol[1];
						int tcol=k[0]+rowcol[0];
						
						//若要計算的點超出地圖邊界或地圖上此位置為障礙物則捨棄檢查此點
						if(trow<0||trow>18||tcol<0||tcol>18)continue;
						if(map[trow][tcol]!=0)continue;
						
						//取出開始點到此點的路徑長度
						int dj=length[trow][tcol];
						//計算經K點到此點的路徑長度				
						int dkPluskj=dk+1;
						
						//若經K點到此點的路徑長度比原來的小則修改到此點的路徑
						if(dj>dkPluskj)
						{
							String key=tcol+":"+trow;
							//複製開始點到K的路徑
							ArrayList<int[][]> tempal=(ArrayList<int[][]>)al.clone();
							//將路徑中加上一步從K到此點
							tempal.add(new int[][]{{k[0],k[1]},{tcol,trow}});
							//將此路徑設定為從開始點到此點的路徑
							hmPath.put(key,tempal);
							//修改到從開始點到此點的路徑長度						
							length[trow][tcol]=dkPluskj;
							
							//若此點從未計算過路徑長度則將此點加入檢查過程記錄
							if(dj==9999)
							{
								//將去過的點記錄		
								searchProcess.add(new int[][]{{k[0],k[1]},{tcol,trow}});
								count++;
							}
						}
						
						//看是否找到目的點
						if(tcol==target[0]&&trow==target[1])
						{
							pathFlag=true;
							tempCount=count;//Dijkstra使用步數
							mSurfaceView.mActivity.hd.sendEmptyMessage(DIJKSTRA_COUNT);	//傳送訊息變更使用步數
							mSurfaceView.mActivity.button.setClickable(true);
							mSurfaceView.repaint(holder);
							break outer;
						}
					}										
					try{Thread.sleep(timeSpan);}catch(Exception e){e.printStackTrace();}				
				}								
			}
		}.start();	
		
	}
	public void DijkstraAStar()//Dijkstra A*算法
	{
		new Thread()
		{
			public void run()
			{
				int count=0;//步數計數器
				boolean flag=true;//搜尋循環控制
				//開始點
				int[] start={source[0],source[1]};//col,row	
				visited[source[1]][source[0]]=1;
				//計算此點所有可以到達點的路徑
				for(int[] rowcol:sequence)
				{					
					int trow=start[1]+rowcol[1];
					int tcol=start[0]+rowcol[0];
					if(trow<0||trow>18||tcol<0||tcol>18)continue;
					if(map[trow][tcol]!=0)continue;
					
					//記錄路徑長度
					length[trow][tcol]=1;
					
					//計算路徑				
					String key=tcol+":"+trow;
					ArrayList<int[][]> al=new ArrayList<int[][]>();
					al.add(new int[][]{{start[0],start[1]},{tcol,trow}});
					hmPath.put(key,al);	
					
					//將去過的點記錄			
					searchProcess.add(new int[][]{{start[0],start[1]},{tcol,trow}});					
					count++;			
				}				
				mSurfaceView.repaint(holder);
				outer:while(flag)
				{					
					int[] k=new int[2];
					int minLen=9999;
					boolean iniFlag=true;
					for(int i=0;i<visited.length;i++)
					{
						for(int j=0;j<visited[0].length;j++)
						{
							if(visited[i][j]==0)
							{
								//與普通Dijkstra算法的區別部分
								if(length[i][j]!=9999)
								{
									if(iniFlag)
									{//第一個找到的可能點
										minLen=length[i][j]+
										(int)Math.sqrt((j-target[0])*(j-target[0])+(i-target[1])*(i-target[1]));
										k[0]=j;//col
										k[1]=i;//row
										iniFlag=!iniFlag;
									}
									else
									{
										int tempLen=length[i][j]+
										(int)Math.sqrt((j-target[0])*(j-target[0])+(i-target[1])*(i-target[1]));
										if(minLen>tempLen)
										{
											minLen=tempLen;
											k[0]=j;//col
											k[1]=i;//row
										}
									}
								}
								//與普通Dijkstra算法區別部分
							}
						}
					}
					//設定去過的點
					visited[k[1]][k[0]]=1;					
					
					//重繪					
					mSurfaceView.repaint(holder);
					
					int dk=length[k[1]][k[0]];
					ArrayList<int[][]> al=hmPath.get(k[0]+":"+k[1]);
					//循環計算所有K點能直接到的點到開始點的路徑長度
					for(int[] rowcol:sequence)
					{
						//計算出新的要計算的點的座標
						int trow=k[1]+rowcol[1];
						int tcol=k[0]+rowcol[0];
						//若要計算的點超出地圖邊界或地圖上此位置為障礙物則捨棄檢查此點
						if(trow<0||trow>18||tcol<0||tcol>18)continue;
						if(map[trow][tcol]!=0)continue;
						//取出開始點到此點的路徑長度
						int dj=length[trow][tcol];	
						//計算經K點到此點的路徑長度
						int dkPluskj=dk+1;
						//若經K點到此點的路徑長度比原來的小則修改到此點的路徑
						if(dj>dkPluskj)
						{
							String key=tcol+":"+trow;
							ArrayList<int[][]> tempal=(ArrayList<int[][]>)al.clone();
							tempal.add(new int[][]{{k[0],k[1]},{tcol,trow}});
							hmPath.put(key,tempal);							
							length[trow][tcol]=dkPluskj;
							
							if(dj==9999)
							{
								//將去過的點記錄		
								searchProcess.add(new int[][]{{k[0],k[1]},{tcol,trow}});								
								count++;
							}
						}
						
						//看是否找到目的點
						if(tcol==target[0]&&trow==target[1])
						{
							Log.d("target[0]="+target[0], "target[1]="+target[1]);
							pathFlag=true;	
							tempCount=count;//DijkstraA*使用步數
							mSurfaceView.mActivity.hd.sendEmptyMessage(DIJKSTRASTAR_COUNT);	//變更使用步數量
							mSurfaceView.mActivity.button.setClickable(true);	
							mSurfaceView.repaint(holder);
							break outer;
						}
					}										
					try{Thread.sleep(timeSpan);}catch(Exception e){e.printStackTrace();}				
				}								
			}
		}.start();					
	}
	
}
