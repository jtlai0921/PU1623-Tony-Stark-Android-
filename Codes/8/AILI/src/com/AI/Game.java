package com.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.Stack;

import android.util.Log;
import android.view.SurfaceHolder;

public class Game{
	MySurfaceView mSurfaceView;//�إ�ø�����O�Ѧ�
	int[][] map=MapList.map[0];//�ݭn�j�M���a��
	int[] source=MapList.source;//�X�o�I�y��
	int[] target=MapList.targetA[0];//�ت��Icol,row
	int algorithmId=0;//��k�N���A0--�`�״���
	
	ArrayList<int[][]> searchProcess=new ArrayList<int[][]>();//�j�M�L�{
	Stack<int[][]> stack=new Stack<int[][]>();//�`���u���ҥΰ��|
	LinkedList<int[][]> queue=new LinkedList<int[][]>();//�s���u���ҥΦ�C
	PriorityQueue<int[][]> astarQueue=new PriorityQueue<int[][]>(100,new AxingComparator(this));//A*�u���Ŧ�C
	HashMap<String,int[][]> hm=new HashMap<String,int[][]>();//���G���|�O��
	int[][] visited=new int[19][19];//0 ���h�L 1 �w�h�L
	int[][] length=new int[19][19];//�O�����|���� for Dijkstra
	// �O����C���I���̵u���|for Dijkstra
	HashMap<String,ArrayList<int[][]>> hmPath=new HashMap<String,ArrayList<int[][]>>();
	
	boolean pathFlag=false;//true �����|
	int timeSpan=10;//�ɶ����j
	SurfaceHolder holder;
	int[][] sequence=
	{
		{0,1},{0,-1},
		{-1,0},{1,0},
		{-1,1},{-1,-1},
		{1,-1},{1,1}
	};
	
	int tempCount;//�O���ӷj�M��k�ҥΨB��
	final int DFS_COUNT=1;//�`���u���ϥΨB�ƼЧ�
	final int BFS_COUNT=2;//�s���u���ϥΨB�ƼЧ�
	final int BFSASTAR_COUNT=3;//�s���u���ϥΨB�ƼЧ�
	final int DIJKSTRA_COUNT=4;//Dijkstra�ϥΨB�ƼЧ�
	final int DIJKSTRASTAR_COUNT=5;//DijkstraA*�ϥΨB�ƼЧ�
	public Game(MySurfaceView mSurfaceView,SurfaceHolder holder)
	{
		this.mSurfaceView=mSurfaceView;
		this.holder=holder;
	}
	public void clearState()//�_�l�ƭӰѦ�
	{
		searchProcess.clear();//�M�z�j�M�L�{�M��
		stack.clear();//�M�z�`���u���ҥΰ��|
		queue.clear();//�M�z�s���u���ҥΦ�C
		astarQueue.clear();//�M�zA*�u���Ŧ�C
		hm.clear();//�M�z���G���|�O��
		visited=new int[19][19];//�_�l�ư}�C
		pathFlag=false;	//�M����g�ЧӦ�
		hmPath.clear();//�M�zDijkstra���O����C���I���̵u���|
		mSurfaceView.paint.setStrokeWidth(0);//�_�l�Ƶe��
		for(int i=0;i<length.length;i++)
		{
			for(int j=0;j<length[0].length;j++)
			{
				length[i][j]=9999;//�]�w�_�l���|�����ס]���i��o��j�^
			}
		}	
	}
	public void runAlgorithm()
	{
		clearState();//�I�s�_�l�Ƥ�k
		switch(algorithmId)
		{
			case 0://�`���u����k
			  DFS();
			break;
			case 1://�s���u����k			
			  BFS();
			break;
			case 2://�s���u��A*��k
			  BFSAStar();
			break;
			case 3://Dijkstra��k
			  Dijkstra();
			  Log.d("Dijkstra", "algorithmId="+algorithmId);
			break;
			case 4://DijkstraA*��k
			  DijkstraAStar();
			break;
		}		
	}
	
	public void DFS()//�`���u��
	{
		new Thread()
		{
			public void run()
			{
				boolean flag=true;//������ЧӦ�
				int[][] start=//�_�l�ƥX�o�I�y��
				{
					{source[0],source[1]},
					{source[0],source[1]}
				};
				stack.push(start);//�J���|
				int count=0;//�ϥΨB�Ƨ޳N��
				while(flag)
				{
					
					int[][] currentEdge=stack.pop();//�q���|�����X��
					int[] tempTarget=currentEdge[1];//���X���䪺�ت��I
					
					//�P�_�ت��I�O�_�h�L�A�Y�h�L�A�h�����i�J�U���`��
					if(visited[tempTarget[1]][tempTarget[0]]==1)
					{
						continue;
					}
					count++;//�p�ƾ��ۥ[
					//��ܥت��I�Q�s���L
					visited[tempTarget[1]][tempTarget[0]]=1;
					
					//�N�{�ɥت��I�[�J�j�M�L�{��
					searchProcess.add(currentEdge);
					//�O�����{�ɸ`�I�����`�I
					hm.put(tempTarget[0]+":"+tempTarget[1],new int[][]{currentEdge[1],currentEdge[0]});
					//��ø�e��
					mSurfaceView.repaint(holder);
					//������ίv�@�w�ɶ�
					try{Thread.sleep(timeSpan);}catch(Exception e){e.printStackTrace();}
					
					//�P�_�O�_��F�ت��I
					if(tempTarget[0]==target[0]&&tempTarget[1]==target[1])
					{
						break;
					}
					
					//�N�Ҧ��i�઺��J���|
					int currCol=tempTarget[0];//����`�I
					int currRow=tempTarget[1];
					
					for(int[] rc:sequence)//���y���I����Ҧ��i�઺��
					{
						int i=rc[1];
						int j=rc[0];
						if(i==0&&j==0){continue;}//�Y��0�A0�����Ӧ��`��
						if(currRow+i>=0&&currRow+i<19&&currCol+j>=0&&currCol+j<19&&
						map[currRow+i][currCol+j]!=1)//�Y�b�a�Ϥ�
						{
							int[][] tempEdge=
							{
								{tempTarget[0],tempTarget[1]},
								{currCol+j,currRow+i}
							};
							stack.push(tempEdge);//�J���|
						}
					}
				}
				pathFlag=true;	//�ЧӦ�]��true
				mSurfaceView.repaint(holder);//��ø�e��
				tempCount=count;//�`���u���ϥΨB��
				mSurfaceView.mActivity.hd.sendEmptyMessage(DFS_COUNT);//�ǰe�T���ܧ�ϥΨB�ƶq
				mSurfaceView.mActivity.button.setClickable(true);	//�]�wbutton�i�H�I��
			}
		}.start();		
	}
	
	
	public void BFS()//�s���u��
	{
		new Thread()
		{
			public void run()
			{
				int count=0;//�p�ƾ�
				boolean flag=true;//�`���ЧӦ�
				int[][] start=//�}�l���A
				{
					{source[0],source[1]},
					{source[0],source[1]}
				};
				queue.offer(start);//�N�}�l�I�[�J�Ӧ�C������
				
				while(flag)
				{					
					int[][] currentEdge=queue.poll();//���o�ò������Y
					int[] tempTarget=currentEdge[1];//���X���䪺�ت��I
					
					//�P�_�O�_�h�L�A�Y�h�L�h�����i�J�U���`��
					if(visited[tempTarget[1]][tempTarget[0]]==1)
					{
						continue;
					}
					count++;//�p�ƾ��ۥ[
					//�N�h�L���I�m��1
					visited[tempTarget[1]][tempTarget[0]]=1;
					
					//���{�ɥت��I�[�J�j�M�L�{
					searchProcess.add(currentEdge);
					//�O�����{�ɸ`�I�����`�I
					hm.put(tempTarget[0]+":"+tempTarget[1],new int[][]{currentEdge[1],currentEdge[0]});
					//��ø�e��
					mSurfaceView.repaint(holder);
					//������ίv�@�w�ɶ�
					try{Thread.sleep(timeSpan);}catch(Exception e){e.printStackTrace();}
					
					//�P�_�O�_���ت��I
					if(tempTarget[0]==target[0]&&tempTarget[1]==target[1])
					{
						break;
					}
					
					//�N�Ҧ��i�઺��[�J��C�C
					int currCol=tempTarget[0];
					int currRow=tempTarget[1];
					
					for(int[] rc:sequence)
					{
						int i=rc[1];
						int j=rc[0];
						
						if(i==0&&j==0){continue;}//�Y�b�a�ϥ~���A�i�J�U�@���`��
						if(currRow+i>=0&&currRow+i<19&&currCol+j>=0&&currCol+j<19&&
						map[currRow+i][currCol+j]!=1)//�Y���a�Ϥ����I
						{
							int[][] tempEdge=
							{
								{tempTarget[0],tempTarget[1]},
								{currCol+j,currRow+i}
							};
							queue.offer(tempEdge);//�N���I�[�[�J��C�C����
						}
					}
				}
				pathFlag=true;	//�ЧӦ�]��true
				mSurfaceView.repaint(holder);//��ø�e��
				tempCount=count;//�s���u���ϥΨB��
				mSurfaceView.mActivity.hd.sendEmptyMessage(BFS_COUNT);	//�ǰe�T���ܧ�ϥΨB�ƶq
				mSurfaceView.mActivity.button.setClickable(true);//�]�wbutton��i�H�I��
			}
		}.start();			
	}
	
	
	public void BFSAStar()//�s���u��A*
	{
		new Thread()
		{
			public void run()
			{
				boolean flag=true;
				int[][] start=//�}�l���A
				{
					{source[0],source[1]},
					{source[0],source[1]}
				};
				astarQueue.offer(start);//�N�}�l�I�[�[�J��C�C����
				int count=0;//�p�ƾ�
				while(flag)
				{					
					int[][] currentEdge=astarQueue.poll();//���o���Y�A�ñN���Y����
					int[] tempTarget=currentEdge[1];//�����䪺�ت��I
					
					//�P�_�O�_�h�L�A�Y�h�L�h�����i�J�U���`��
					if(visited[tempTarget[1]][tempTarget[0]]!=0)
					{
						continue;
					}
					count++;
					//��ܥت��I���s���L
					visited[tempTarget[1]][tempTarget[0]]=visited[currentEdge[0][1]][currentEdge[0][0]]+1;				
					//�N�{�ɥت��I�[�J�j�M�L�{��
					searchProcess.add(currentEdge);
					//�O�����{�ɸ`�I�����`�I
					hm.put(tempTarget[0]+":"+tempTarget[1],new int[][]{currentEdge[1],currentEdge[0]});
					//��ø�e��
					mSurfaceView.repaint(holder);
					
					try{Thread.sleep(timeSpan);}catch(Exception e){e.printStackTrace();}
					
					//�P�_�O�_���ت��I
					if(tempTarget[0]==target[0]&&tempTarget[1]==target[1])
					{
						break;
					}
					
					//�N�Ҧ��i�઺��[�[�J��C�C
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
							astarQueue.offer(tempEdge);//�[�[�J��C�C����
						}						
					}
				}
				pathFlag=true;	
				mSurfaceView.repaint(holder);
				tempCount=count;//�s���u��A*�ϥΨB��
				mSurfaceView.mActivity.hd.sendEmptyMessage(BFSASTAR_COUNT);//�ǰe�T���ܧ�ϥΨB�ƶq
				mSurfaceView.mActivity.button.setClickable(true);	//�]�wbutton���i�I				
			}
		}.start();				
	}
	
	
	public void Dijkstra()
	{
		new Thread()
		{
			public void run()
			{
				int count=0;//�B�J�p�ƾ�
				boolean flag=true;//�j�M�`���]�w
				//�}�l�I
				int[] start={source[0],source[1]};//col,row	
				visited[source[1]][source[0]]=1;
				//�p�⦹�I�Ҧ��i�H��F�I�����|�Ϊ���
				for(int[] rowcol:sequence)
				{					
					int trow=start[1]+rowcol[1];
					int tcol=start[0]+rowcol[0];
					if(trow<0||trow>18||tcol<0||tcol>18)continue;
					if(map[trow][tcol]!=0)continue;
					
					//�O�����|����
					length[trow][tcol]=1;
					
					//�p����|					
					String key=tcol+":"+trow;
					ArrayList<int[][]> al=new ArrayList<int[][]>();
					al.add(new int[][]{{start[0],start[1]},{tcol,trow}});
					hmPath.put(key,al);	
					
					//�N�h�L���I�O��		
					searchProcess.add(new int[][]{{start[0],start[1]},{tcol,trow}});
					count++;			
				}	
				mSurfaceView.repaint(holder);//��ø
				outer:while(flag)
				{					
					//���ثe�����IK �n�D�����IK���q�}�l�I�즹�I�ثe���|�̵u�A�B���I���ˬd�L
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
					
					//�]�w�h�L���I
					visited[k[1]][k[0]]=1;					
					
					//	��ø				
					mSurfaceView.repaint(holder);
					
					//���X�}�l�I��K�����|����
					int dk=length[k[1]][k[0]];
					//���X�}�l�I��K�����|
					ArrayList<int[][]> al=hmPath.get(k[0]+":"+k[1]);
					
					//�`���p��Ҧ�K�I�ઽ���쪺�I��}�l�I�����|����
					for(int[] rowcol:sequence)
					{
						//�p��X�s���n�p�⪺�I���y��
						int trow=k[1]+rowcol[1];
						int tcol=k[0]+rowcol[0];
						
						//�Y�n�p�⪺�I�W�X�a����ɩΦa�ϤW����m����ê���h�˱��ˬd���I
						if(trow<0||trow>18||tcol<0||tcol>18)continue;
						if(map[trow][tcol]!=0)continue;
						
						//���X�}�l�I�즹�I�����|����
						int dj=length[trow][tcol];
						//�p��gK�I�즹�I�����|����				
						int dkPluskj=dk+1;
						
						//�Y�gK�I�즹�I�����|���פ��Ӫ��p�h�ק�즹�I�����|
						if(dj>dkPluskj)
						{
							String key=tcol+":"+trow;
							//�ƻs�}�l�I��K�����|
							ArrayList<int[][]> tempal=(ArrayList<int[][]>)al.clone();
							//�N���|���[�W�@�B�qK�즹�I
							tempal.add(new int[][]{{k[0],k[1]},{tcol,trow}});
							//�N�����|�]�w���q�}�l�I�즹�I�����|
							hmPath.put(key,tempal);
							//�ק��q�}�l�I�즹�I�����|����						
							length[trow][tcol]=dkPluskj;
							
							//�Y���I�q���p��L���|���׫h�N���I�[�J�ˬd�L�{�O��
							if(dj==9999)
							{
								//�N�h�L���I�O��		
								searchProcess.add(new int[][]{{k[0],k[1]},{tcol,trow}});
								count++;
							}
						}
						
						//�ݬO�_���ت��I
						if(tcol==target[0]&&trow==target[1])
						{
							pathFlag=true;
							tempCount=count;//Dijkstra�ϥΨB��
							mSurfaceView.mActivity.hd.sendEmptyMessage(DIJKSTRA_COUNT);	//�ǰe�T���ܧ�ϥΨB��
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
	public void DijkstraAStar()//Dijkstra A*��k
	{
		new Thread()
		{
			public void run()
			{
				int count=0;//�B�ƭp�ƾ�
				boolean flag=true;//�j�M�`������
				//�}�l�I
				int[] start={source[0],source[1]};//col,row	
				visited[source[1]][source[0]]=1;
				//�p�⦹�I�Ҧ��i�H��F�I�����|
				for(int[] rowcol:sequence)
				{					
					int trow=start[1]+rowcol[1];
					int tcol=start[0]+rowcol[0];
					if(trow<0||trow>18||tcol<0||tcol>18)continue;
					if(map[trow][tcol]!=0)continue;
					
					//�O�����|����
					length[trow][tcol]=1;
					
					//�p����|				
					String key=tcol+":"+trow;
					ArrayList<int[][]> al=new ArrayList<int[][]>();
					al.add(new int[][]{{start[0],start[1]},{tcol,trow}});
					hmPath.put(key,al);	
					
					//�N�h�L���I�O��			
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
								//�P���qDijkstra��k���ϧO����
								if(length[i][j]!=9999)
								{
									if(iniFlag)
									{//�Ĥ@�ӧ�쪺�i���I
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
								//�P���qDijkstra��k�ϧO����
							}
						}
					}
					//�]�w�h�L���I
					visited[k[1]][k[0]]=1;					
					
					//��ø					
					mSurfaceView.repaint(holder);
					
					int dk=length[k[1]][k[0]];
					ArrayList<int[][]> al=hmPath.get(k[0]+":"+k[1]);
					//�`���p��Ҧ�K�I�ઽ���쪺�I��}�l�I�����|����
					for(int[] rowcol:sequence)
					{
						//�p��X�s���n�p�⪺�I���y��
						int trow=k[1]+rowcol[1];
						int tcol=k[0]+rowcol[0];
						//�Y�n�p�⪺�I�W�X�a����ɩΦa�ϤW����m����ê���h�˱��ˬd���I
						if(trow<0||trow>18||tcol<0||tcol>18)continue;
						if(map[trow][tcol]!=0)continue;
						//���X�}�l�I�즹�I�����|����
						int dj=length[trow][tcol];	
						//�p��gK�I�즹�I�����|����
						int dkPluskj=dk+1;
						//�Y�gK�I�즹�I�����|���פ��Ӫ��p�h�ק�즹�I�����|
						if(dj>dkPluskj)
						{
							String key=tcol+":"+trow;
							ArrayList<int[][]> tempal=(ArrayList<int[][]>)al.clone();
							tempal.add(new int[][]{{k[0],k[1]},{tcol,trow}});
							hmPath.put(key,tempal);							
							length[trow][tcol]=dkPluskj;
							
							if(dj==9999)
							{
								//�N�h�L���I�O��		
								searchProcess.add(new int[][]{{k[0],k[1]},{tcol,trow}});								
								count++;
							}
						}
						
						//�ݬO�_���ت��I
						if(tcol==target[0]&&trow==target[1])
						{
							Log.d("target[0]="+target[0], "target[1]="+target[1]);
							pathFlag=true;	
							tempCount=count;//DijkstraA*�ϥΨB��
							mSurfaceView.mActivity.hd.sendEmptyMessage(DIJKSTRASTAR_COUNT);	//�ܧ�ϥΨB�ƶq
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
