package com.AI;

import java.util.Comparator;

public class AxingComparator implements Comparator<int[][]>{

	Game game;
	
	public AxingComparator(Game game)
	{
		this.game=game;
	}
	
	public int compare(int[][] o1,int[][] o2)
	{
		int[] t1=o1[1];
		int[] t2=o2[1];
		
		int[] target=game.target;
		//�X�a�dù�Z��
		int a=game.visited[o2[0][1]][o2[0][0]]+Math.abs(t1[0]-target[0])+Math.abs(t1[1]-target[1]);
		int b=game.visited[o2[0][1]][o2[0][0]]+Math.abs(t2[0]-target[0])+Math.abs(t2[1]-target[1]);	
		return a-b;
	}
	
	public boolean equals(Object obj)
	{
		return false;
	}
}
