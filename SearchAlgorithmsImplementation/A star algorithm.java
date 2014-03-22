import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;


public class tsp 
{
	public static void main(String[] args) 
	{
		String InputPath="", OutputPath="", OutputLog="";
		int flag=0,row=1,column=1;
		if(args[0].equals("-t") && args[2].equals("-i") && args[4].equals("-op") && args[6].equals("-ol"))
		{
			InputPath=args[3];
			OutputPath=args[5];
			OutputLog=args[7];
			
			HashMap<String, Integer> list=new HashMap<String, Integer>();
	    	HashMap<Integer, String> revlist=new HashMap<Integer, String>();
			HashMap<String, Integer[]> stops=new HashMap<String, Integer[]>();
			ArrayList<String> nodelist=new ArrayList<String>();
			ArrayList<String> sortednodelist=new ArrayList<String>();
			
			InputStream fin=null,fin2=null;
			BufferedReader dis=null,dis2=null;
			try
			{
				fin=new FileInputStream(InputPath);
		        dis=new BufferedReader(new InputStreamReader(fin));
		        String str=dis.readLine();
		        while(str!=null)
		        {
		        	row++;
		        	column=str.length();
		        	str=dis.readLine();
		        }
		    }
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
			
			if(row>1 && column>1)
			{
				int data[][]=new int[row][column];
				try
				{
					fin2=new FileInputStream(InputPath);
			        dis2=new BufferedReader(new InputStreamReader(fin2));
			        String str=dis2.readLine();
			        int j=0,index=0;
			        while(str!=null)
			        {
			        	for(int i=0;i<str.length();i++)
			        	{
			        		if(str.charAt(i)=='*')
			        		{
			        			data[j][i]=-1;
			        		}
			        		else if(str.charAt(i)!='*' && str.charAt(i)!=' ')
			        		{
			        			data[j][i]=0;
			        			Integer[] temp=new Integer[2];
			        			temp[0]=j;
			        			temp[1]=i;
			        			String s=String.valueOf(str.charAt(i));
			        			list.put(s,index);
			        			revlist.put(index,s);
			        			stops.put(s,temp);
			        			nodelist.add(s);
			        			index++;
			        		}
			        		else
			        		{
			        			data[j][i]=0;
			        		}
			        	}
			        	j++;
			        	str=dis2.readLine();
			        }
			    }
				catch(Exception e)
				{
					System.out.println(e.toString());
				}
				Collections.sort(nodelist);
				sortednodelist.addAll(nodelist);
				int n=nodelist.size();
				float pathcost[][]=new float[n][n];
				
				if(args[1].equalsIgnoreCase("1"))
				{
					flag=1;
					BufferedWriter logwriter= null,pathwriter=null;
					try 
					{
					    logwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OutputLog), "utf-8"));
					    pathwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OutputPath), "utf-8"));
					    
					    while(!nodelist.isEmpty())
					    {
					    	String st=nodelist.remove(0);
					    	Iterator<String> itr=nodelist.iterator();
					    	while(itr.hasNext())
						    {
						    	String t=itr.next();
						    	logwriter.write("from '"+st+"' to '"+t+"'");
						    	logwriter.newLine();
								logwriter.write("--------------------------");
								logwriter.newLine();
								logwriter.write("x,y,g,h,f");
						    	float cost=A_Star(data,row,column,stops.get(st),stops.get(t),logwriter);
						    	logwriter.newLine();
								logwriter.write("---------------------------");
								logwriter.newLine();
						    	pathcost[list.get(st)][list.get(t)]=cost;
						    	pathcost[list.get(t)][list.get(st)]=cost;
						    	pathwriter.write(st+","+t+","+cost);
						    	pathwriter.newLine();
						    }
					    }
					}
					catch (Exception ex) 
					{
							System.out.println(ex.toString());
					}
					finally 
					{
					   try 
					   {
						   logwriter.close();
						   pathwriter.close();
					   }
					   catch (Exception ex) 
					   {
						   System.out.println(ex.toString());
					   }
					}
				}
				else if(args[1].equalsIgnoreCase("2"))
				{
					if(flag==0)
					{
						while(!nodelist.isEmpty())
					    {
					    	String st=nodelist.remove(0);
					    	Iterator<String> itr=nodelist.iterator();
					    	while(itr.hasNext())
						    {
						    	String t=itr.next();
						    	float cost=A_Star(data,row,column,stops.get(st),stops.get(t),null);
						    	pathcost[list.get(st)][list.get(t)]=cost;
						    	pathcost[list.get(t)][list.get(st)]=cost;
						    }
					    }
					}
					MST(list,revlist,sortednodelist,pathcost,stops,OutputLog,OutputPath);
				}
			}
			else
			{
				System.out.println("Invalid File !");
				System.exit(1);
			}
		}
		else
		{
			System.out.println("Invalid arguments !");
			System.exit(1);
		}
	}

	private static void MST(HashMap<String, Integer> list,HashMap<Integer, String> revlist,ArrayList<String> nodelist, float[][] pathcost, HashMap<String, Integer[]> stops, String OutputLog, String OutputPath) 
	{
		String goal="A",current="A";
		ArrayList<String> visited=new ArrayList<String>();
		float h=calculate_H(list,revlist,nodelist,pathcost,visited,current,goal);
		state init_state=new state(current, visited, stops.get(current),0, h);
		
		PriorityQueue<state> PQ=new PriorityQueue<state>(1, new MyComparator2());
		PQ.add(init_state);
		ArrayList<String> allnodes=new ArrayList<String>();
		float total=0;
		
		BufferedWriter logwriter= null,pathwriter=null;
		try 
		{
		    logwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OutputLog), "utf-8"));
		    pathwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OutputPath), "utf-8"));
		
		    while(!allnodes.containsAll(nodelist))
		    {
		    	state s=PQ.remove();
		    	allnodes.clear();
		    	allnodes.addAll(s.visted_chkpnts);
		    	Iterator<String> itr=nodelist.iterator();
		    	while(itr.hasNext())
		    	{
		    		String node=itr.next();
		    		if(!node.equalsIgnoreCase(s.cur_chkpnt) && !s.visted_chkpnts.contains(node))
		    		{
		    			ArrayList<String> new_visited=new ArrayList<String>();
		    			new_visited.addAll(s.visted_chkpnts);
		    			new_visited.add(s.cur_chkpnt);
		    			float g_val=s.g+pathcost[list.get(s.cur_chkpnt)][list.get(node)];
		    			float h_val=calculate_H(list, revlist, nodelist, pathcost, new_visited, node, goal);
		    			state st=new state(node, new_visited, stops.get(node),g_val, h_val);
		    			PQ.add(st);
		    		}
		    	}
		    	allnodes.add(s.cur_chkpnt);
		    	Iterator<String> allitr=allnodes.iterator();
		    	String str="";
		    	while(allitr.hasNext())
		    	{
		    		str=str+allitr.next();
		    	}
		    	logwriter.write(str+","+s.g+","+s.h+","+s.getF());
		    	logwriter.newLine();
		    	
		    	if(allnodes.containsAll(nodelist))
		    	{
		    		logwriter.write(str+"A,"+(s.g+s.h)+",0.0,"+s.getF());
			    	logwriter.newLine();
		    		total=s.getF();
		    	}
		    }
		    Iterator<String> allitr=allnodes.iterator();
	    	while(allitr.hasNext())
	    	{
	    		pathwriter.write(allitr.next());
	    		pathwriter.newLine();
	    	}
	    	pathwriter.write(goal);
    		pathwriter.newLine();
	    	pathwriter.write("Total Tour Cost: "+total);
		}
		catch (Exception ex) 
		{
				System.out.println(ex.toString());
				ex.printStackTrace();
		}
		finally 
		{
		   try 
		   {
			   logwriter.close();
			   pathwriter.close();
		   }
		   catch (Exception ex) 
		   {
			   System.out.println(ex.toString());
		   }
		}
	}

	private static float calculate_H(HashMap<String, Integer> list,HashMap<Integer, String> revlist, ArrayList<String> nodelist,
			float[][] pathcost, ArrayList<String> visited, String current,String goal) 
	{
		ArrayList<String> unvisited=new ArrayList<String>();
		find_unvisited(current, goal, visited, unvisited, nodelist);
		ArrayList<String> checked=new ArrayList<String>();
		checked.add(current);
			
		float total=0;
		float min=100000000;
		int minnode=0;
		while(!checked.containsAll(unvisited))
		{
			Iterator<String> itr=checked.iterator();
			while(itr.hasNext())
			{
				String s=itr.next();
				int i=list.get(s);
				for(int j=0;j<pathcost[i].length;j++)
				{
					String str=revlist.get(j);
					
					if(!(visited.contains(s) && visited.contains(str)))
					{	
						if(!checked.contains(str) && unvisited.contains(str))
						{
							if(pathcost[i][j]<min)
							{
								min=pathcost[i][j];
								minnode=j;
							}
						}
					}
				}
			}
			checked.add(revlist.get(minnode));
			total=total+min;
			min=100000000;
		}
		return total;
	}

	private static void find_unvisited(String current,String goal,ArrayList<String> visited, ArrayList<String> unvisited, ArrayList<String> nodelist) 
	{
		if(!current.equalsIgnoreCase(goal))
		{
			unvisited.add(goal);
		}
		Iterator<String> itr=nodelist.iterator();
		while(itr.hasNext())
		{
			String s=itr.next();
			if(!s.equalsIgnoreCase(current) && !visited.contains(s))
			{
				unvisited.add(s);
			}
		}
	}

	private static float A_Star(int[][] data, int row, int column, Integer[] start, Integer[] goal, BufferedWriter logwriter) 
	{
		grid map[][]=new grid[row][column];
		ArrayList<grid> visited=new ArrayList<grid>();
		PriorityQueue<grid> PQ=new PriorityQueue<grid>(row+column, new MyComparator());
		
		grid start_square=new grid(0, getH(start[0],start[1],goal[0],goal[1]), start[0], start[1]);
		PQ.add(start_square);
		map[start[0]][start[1]]=start_square;
		grid current=null;
		int cur_x=0,cur_y=0;
		float finalcost=0;
		
		while(!PQ.isEmpty())
		{
			current=PQ.remove();
			if(current!=null)
			{
				visited.add(current);
				cur_x=current.x;
				cur_y=current.y;
				
				try
				{
					if(logwriter!=null)
					{
						logwriter.newLine();
						logwriter.write(current.y+","+current.x+","+current.g+","+current.h+","+current.getF());
					}
				}
				catch(Exception e)
				{
					System.out.println(e.toString());
				}
				
				if(cur_x==goal[0] && cur_y==goal[1])
				{
					finalcost=map[cur_x][cur_y].g;
					break;
				}
				
				checknode(data,visited,PQ,map,goal,cur_x,cur_y,row,column,cur_x-1,cur_y);
				checknode(data,visited,PQ,map,goal,cur_x,cur_y,row,column,cur_x,cur_y-1);
				checknode(data,visited,PQ,map,goal,cur_x,cur_y,row,column,cur_x,cur_y+1);
				checknode(data,visited,PQ,map,goal,cur_x,cur_y,row,column,cur_x+1,cur_y);
			}
		}
		
		return finalcost;
	}

	private static void checknode(int[][] data, ArrayList<grid> visited, PriorityQueue<grid> PQ, grid[][] map, Integer[] goal, int cur_x,
			int cur_y, int row, int column, int nx, int ny) 
	{
		if(nx>0 && nx<row && ny>0 && ny<column)
		{
			if(data[nx][ny]!=-1 && !visited.contains(map[nx][ny]))
			{
				if(map[nx][ny]==null)
				{
					grid temp=new grid(map[cur_x][cur_y].g+1, getH(nx,ny,goal[0],goal[1]), nx, ny);
					map[nx][ny]=temp;
					PQ.add(temp);
				}
				else if(map[nx][ny]!=null)
				{
					if(PQ.contains(map[nx][ny]))
					{
						if(map[cur_x][cur_y].g+1 < map[nx][ny].g)
						{
							PQ.remove(map[nx][ny]);
							map[nx][ny].g=map[cur_x][cur_y].g+1;
							PQ.add(map[nx][ny]);
						}
					}
				}
			}
		}
	}

	private static float getH(Integer x, Integer y, Integer goalx, Integer goaly) 
	{
		return Math.abs(goalx-x)+Math.abs(goaly-y);
	}
}

class grid
{
	float g,h;
	int x,y;
	grid(float g,float h,int x,int y)
	{
		this.g=g;
		this.h=h;
		this.x=x;
		this.y=y;
	}
	
	float getF()
	{
		return this.g+this.h;
	}
}

class MyComparator implements Comparator<grid>
{

	@Override
	public int compare(grid o1, grid o2)
	{
		int i=0;
		DecimalFormat df = new DecimalFormat("###.#");
		float f=Float.parseFloat(df.format(o1.getF()-o2.getF()));
		if(f<0)
			i=-1;
		else if(f>0)
			i=1;
		else if(f==0)
		{
			if(o1.x<o2.x)
				i=-1;
			else if(o1.x>o2.x)
				i=1;
			else if(o1.x==o2.x)
			{
				if(o1.y<o2.y)
					i=-1;
				else if(o1.y>o2.y)
					i=1;
			}
		}
		return i;
	}
}

class state
{
	String cur_chkpnt;
	ArrayList<String> visted_chkpnts;
	Integer cord[]=new Integer[2];
	float g; 
	float h;
	
	state(String cur_chkpnt,ArrayList<String> visted_chkpnts,Integer cord[],float g,float h)
	{
		this.cur_chkpnt=cur_chkpnt;
		this.visted_chkpnts=visted_chkpnts;
		this.cord=cord;
		this.g=g;
		this.h=h;
	}
	
	float getF()
	{
		return this.g+this.h;
	}
}

class MyComparator2 implements Comparator<state>
{

	@Override
	public int compare(state o1, state o2)
	{
		int i=0;
		DecimalFormat df = new DecimalFormat("###.#");
		float f=Float.parseFloat(df.format(o1.getF()-o2.getF()));
		if(f<0)
			i=-1;
		else if(f>0)
			i=1;
		else if(f==0)
		{
			if(o1.visted_chkpnts.size()>o2.visted_chkpnts.size())
				i=-1;
			else if(o1.visted_chkpnts.size()<o2.visted_chkpnts.size())
				i=1;
			else if(o1.visted_chkpnts.size()==o2.visted_chkpnts.size())
			{
				if(o1.cord[0]<o2.cord[0])
					i=-1;
				else if(o1.cord[0]>o2.cord[0])
					i=1;
				else if(o1.cord[0]==o2.cord[0])
				{
					if(o1.cord[1]<o2.cord[1])
						i=-1;
					else if(o1.cord[1]>o2.cord[1])
						i=1;
				}
			}
		}
		return i;
	}
}
