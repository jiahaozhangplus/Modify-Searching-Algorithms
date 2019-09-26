package hw1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

class Point {
    int x;
    int y;
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class CostPoint {
    Point p;
    int c;
    CostPoint(Point p, int c) {
        this.p = p;
        this.c = c;
    }
}
class CostPointComparator implements Comparator<CostPoint> {
	public int compare(CostPoint o1, CostPoint o2) {
		return o1.c - o2.c;
	}
}

public class homework {
    private static int[][] choose = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
	
	public static void main(String[] args) {
		File file = new File("input.txt");
		List<String> str = new ArrayList<String>();
		BufferedReader reader =	null;
		String s = "";
		try {
            reader = new BufferedReader(new FileReader(file));
	        while((s = reader.readLine())!= null) {
	        	str.add(s);
	        }
			
		}catch(Exception e) {
	        e.printStackTrace();
	    }
		String firstl = str.get(0);
		String[] secondl = str.get(1).split(" ");
		int W = Integer.parseInt(secondl[0]);
		int H = Integer.parseInt(secondl[1]);
		String[] thirdl = str.get(2).split(" ");
		int landx = Integer.parseInt(thirdl[0]);
		int landy = Integer.parseInt(thirdl[1]);
		int elevdiff = Integer.parseInt(str.get(3));
	    int N = Integer.parseInt(str.get(4));
	    int[] targetx = new int[N];
	    int[] targety = new int[N];
	    for(int i = 0; i < N; i++) {
	    	String[] Nl = str.get(5 + i).split(" ");
	    	targetx[i] = Integer.parseInt(Nl[0]);
	    	targety[i] = Integer.parseInt(Nl[1]);
	    }
	    int[][] map = new int[H][W];
	    for(int i = 0; i < H; i++) {
	    	String[] hl = str.get(5 + N + i).split(" ");
	    	for(int j = 0; j < W; j++) {    		
	    		map[i][j] = Integer.parseInt(hl[j]);
	    	}
	    }
	    if(firstl.equals("BFS")) {
	    	BFS(map, landx, landy, elevdiff, N, targetx, targety);
	    }
	    else if(firstl.equals("UCS")) {
	    	UCS(map, landx, landy, elevdiff, N, targetx, targety);
	    }else if(firstl.equals("A*")) {
	    	AStar(map, landx, landy, elevdiff, N, targetx, targety);
	    }
	}
	private static int admissible(int[][] map, int x, int y, int i, int[] targetx, int[] targety) {
		return Math.abs(map[y][x] - map[targety[i]][targetx[i]]) + (int) Math.sqrt((targetx[i] - x)*(targetx[i] - x)*10+ (targety[i] - y)*(targety[i] - y)*10);
	}
	private static void AStar(int[][] map, int landx, int landy, int elevdiff, int n, int[] targetx, int[] targety) {
		// TODO Auto-generated method stub
		Point[][] used = new Point[map.length][map[0].length];
		int[][] cost = new int[map.length][map[0].length];
		PriorityQueue<CostPoint> path = new PriorityQueue<CostPoint>(new CostPointComparator());
		Point land = new Point(landx, landy);
		for(int i = 0; i < n; i++) {
			int hn = admissible(map, landx, landy, i, targetx, targety);
			path.offer(new CostPoint(land, hn));
			used[landy][landx] = new Point(-1, -1);
			cost[landy][landx] = hn;
			while(path.isEmpty() == false) {
				CostPoint start = path.poll();
//		    	System.out.println(start.p.x + "," + start.p.y);
				for(int j = 0; j < choose.length; j++) {
					int tempx = start.p.x + choose[j][0];
					int tempy = start.p.y + choose[j][1];
					if(tempx >= 0 && tempx <= map[0].length - 1 && tempy >= 0 && tempy <= map.length - 1) {
						int fn;
						if(Math.abs(choose[j][0])+ Math.abs(choose[j][0]) == 1) {
							fn = admissible(map, tempx, tempy, i, targetx, targety) + 10 + start.c - admissible(map, start.p.x, start.p.y, i, targetx, targety) + Math.abs(map[start.p.y][start.p.x] - map[tempy][tempx]); 
						}else {
							fn = admissible(map, tempx, tempy, i, targetx, targety) + 14 + start.c - admissible(map, start.p.x, start.p.y, i, targetx, targety) + Math.abs(map[start.p.y][start.p.x] - map[tempy][tempx]); 
						}
						if((used[tempy][tempx] == null || fn < cost[tempy][tempx])  && (Math.abs(map[start.p.y][start.p.x] - map[tempy][tempx]) <= elevdiff)){
							path.offer(new CostPoint(new Point(tempx, tempy), fn));			
							used[tempy][tempx] = start.p;
							cost[tempy][tempx] = fn;
						}	
					}
				}
			}
		}
		try {
			FileWriter w = new FileWriter("output.txt");
			BufferedWriter bw = new BufferedWriter(w);
			for(int i = 0; i < n; i++) {
				if(targetx[i] == landx && targety[i] == landy) {
					bw.write(targetx[i] + "," + targety[i]);
					bw.newLine();
					break;
				}
				if(used[targety[i]][targetx[i]] == null) {
					bw.write("FAIL");
					bw.newLine();
				}else {
					String output = targetx[i] + "," + targety[i];
					Point parent = used[targety[i]][targetx[i]];
					while(parent != land) {
						output = parent.x + "," + parent.y + " " + output;
						parent = used[parent.y][parent.x];
					}
					output = parent.x + "," + parent.y + " " + output;
					bw.write(output);
					bw.newLine();
				}
			}
            bw.close();
            w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static void UCS(int[][] map, int landx, int landy, int elevdiff, int n, int[] targetx, int[] targety) {
		// TODO Auto-generated method stub
		Point[][] used = new Point[map.length][map[0].length];
		PriorityQueue<CostPoint> path = new PriorityQueue<CostPoint>(new CostPointComparator());
		Point land = new Point(landx, landy);
		path.offer(new CostPoint(land, 0));
		used[landy][landx] = new Point(-1, -1);
		while(path.isEmpty() == false) {
			CostPoint start = path.poll();
//	    	System.out.println(start.x + "," + start.y);
			for(int i = 0; i < choose.length; i++) {
				int tempx = start.p.x + choose[i][0];
				int tempy = start.p.y + choose[i][1];
				if(tempx >= 0 && tempx <= map[0].length - 1 && tempy >= 0 && tempy <= map.length - 1 && used[tempy][tempx] == null && (Math.abs(map[start.p.y][start.p.x] - map[tempy][tempx]) <= elevdiff)){
					if(Math.abs(choose[i][0])+ Math.abs(choose[i][0]) == 1) {
						path.offer(new CostPoint(new Point(tempx, tempy), 10 + start.c));
					}else {
						path.offer(new CostPoint(new Point(tempx, tempy), 14 + start.c));
					}				
					used[tempy][tempx] = start.p;	
				}				
			}
		}
		try {
			FileWriter w = new FileWriter("output.txt");
			BufferedWriter bw = new BufferedWriter(w);
			for(int i = 0; i < n; i++) {
				if(targetx[i] == landx && targety[i] == landy) {
					bw.write(targetx[i] + "," + targety[i]);
					bw.newLine();
					break;
				}
				if(used[targety[i]][targetx[i]] == null) {
					bw.write("FAIL");
					bw.newLine();
				}else {
					String output = targetx[i] + "," + targety[i];
					Point parent = used[targety[i]][targetx[i]];
					while(parent != land) {
						output = parent.x + "," + parent.y + " " + output;
						parent = used[parent.y][parent.x];
					}
					output = parent.x + "," + parent.y + " " + output;
					bw.write(output);
					bw.newLine();
				}
			}
            bw.close();
            w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void BFS(int[][] map, int landx, int landy, int elevdiff, int n, int[] targetx, int[] targety) {
		// TODO Auto-generated method stub
		Point[][] used = new Point[map.length][map[0].length];
		Queue<Point> path = new LinkedList<>();
//		Set<Point> target = new HashSet<>();
//		for(int i = 0; i < n; i++) {
//			target.add(new Point(targetx[i], targety[i]));
//		}
		Point land = new Point(landx, landy);
		path.offer(land);
		used[landy][landx] = new Point(-1, -1);
		while(path.isEmpty() == false) {
			Point start = path.poll();
//	    	System.out.println(start.x + "," + start.y);
			for(int i = 0; i < choose.length; i++) {
				int tempx = start.x + choose[i][0];
				int tempy = start.y + choose[i][1];
				if(tempx >= 0 && tempx <= map[0].length - 1 && tempy >= 0 && tempy <= map.length - 1 && used[tempy][tempx] == null && (Math.abs(map[start.y][start.x] - map[tempy][tempx]) <= elevdiff)){
					path.offer(new Point(tempx, tempy));
					used[tempy][tempx] = start;	
				}				
			}
		}
		try {
			FileWriter w = new FileWriter("output.txt");
			BufferedWriter bw = new BufferedWriter(w);
			for(int i = 0; i < n; i++) {
				if(targetx[i] == landx && targety[i] == landy) {
					bw.write(targetx[i] + "," + targety[i]);
					bw.newLine();
					break;
				}
				if(used[targety[i]][targetx[i]] == null) {
					bw.write("FAIL");
					bw.newLine();
				}else {
					String output = targetx[i] + "," + targety[i];
					Point parent = used[targety[i]][targetx[i]];
					while(parent != land) {
						output = parent.x + "," + parent.y + " " + output;
						parent = used[parent.y][parent.x];
					}
					output = parent.x + "," + parent.y + " " + output;
					bw.write(output);
					bw.newLine();
				}
			}
            bw.close();
            w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
