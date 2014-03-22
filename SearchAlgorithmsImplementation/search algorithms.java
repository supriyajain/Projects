import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;


public class search
{
	public static void main(String argv[])
	{
		String InputPath="", TieBrk="", OutputPath="", OutputLog="";
		if(argv[0].equals("-t"))
		{
			if(argv[2].equals("-s") && argv[4].equals("-g") && argv[6].equals("-i") && argv[8].equals("-t") && argv[10].equals("-op") && argv[12].equals("-ol"))
			{
				InputPath=argv[7];
				TieBrk=argv[9];
				OutputPath=argv[11];
				OutputLog=argv[13];
			}
			else if(argv[2].equals("-i") && argv[4].equals("-t") && argv[6].equals("-op") && argv[8].equals("-ol"))
			{
				InputPath=argv[3];
				TieBrk=argv[5];
				OutputPath=argv[7];
				OutputLog=argv[9];
			}
			else
			{
				System.out.println("Invalid arguments !");
				System.exit(1);
			}
			
			HashMap<String, Integer> list=new HashMap<String, Integer>();
	    	HashMap<Integer, String> revlist=new HashMap<Integer, String>();
	    	HashMap<Integer, Integer> priorlist=new HashMap<Integer, Integer>();
	    	
			InputStream fin=null,fin2=null,fin3=null;
			BufferedReader dis=null,dis2=null,dis3=null;
			try
			{
				fin=new FileInputStream(InputPath);
		        dis=new BufferedReader(new InputStreamReader(fin));
		        int count=0,index=0,index2=0;
		        String str=dis.readLine();
		       
		        while(str!=null)
		        {
		        	index=str.indexOf(",");
			    	String substr=str.substring(0, index);
			    	if(!list.containsKey(substr))
			    	{
			    		list.put(substr, count);
			    		revlist.put(count, substr);
			    		count++;
			    	}
			    	
			    	index2=str.indexOf(',', index+1);
    				String name=str.substring(index+1, index2);
    				if(!list.containsKey(name))
			    	{
			    		list.put(name, count);
			    		revlist.put(count, name);
			    		count++;
			    	}
    				str=dis.readLine();
		        }
		        
		        float costarr[][]=new float[list.size()][list.size()];
		        int n=list.size();
		        for(int i=0;i<n;i++)
		        {
		        	for(int j=0;j<n;j++)
		        	{
		        		costarr[i][j]=(float)0;
		        	}
		        }
		        
		        fin2=new FileInputStream(InputPath);
		        dis2=new BufferedReader(new InputStreamReader(fin2));
		        int index3=0,index4=0;
		        String str1=dis2.readLine();
		        
		        while(str1!=null)
		        {
		        	index3=str1.indexOf(",");
			    	String node1=str1.substring(0, index3);
			    	index4=str1.indexOf(',', index3+1);
    				String node2=str1.substring(index3+1, index4);
    				String nodecost=str1.substring(index4+1);
    				
    				if(list.containsKey(node1) && list.containsKey(node2))
			    	{
			    		costarr[list.get(node1)][list.get(node2)]=Float.parseFloat(nodecost);
			    		costarr[list.get(node2)][list.get(node1)]=Float.parseFloat(nodecost);
			    	}
    				str1=dis2.readLine();
		        }
		        
		        fin3=new FileInputStream(TieBrk);
		        dis3=new BufferedReader(new InputStreamReader(fin3));
		        int pr=0;
		        String line=dis3.readLine();
		        
		        while(line!=null)
		        {
		        	if(list.containsKey(line))
		        	{
		        		priorlist.put(list.get(line), pr);
		        		pr++;
		        	}
		        	line=dis3.readLine();
		        }
		      
		        int t=Integer.parseInt(argv[1]);
				switch(t)
				{
					case 1:	
						BFS(argv[3],argv[5],OutputPath,OutputLog,costarr,list,revlist,priorlist);
						break;
					case 2:
						DFS(argv[3],argv[5],OutputPath,OutputLog,costarr,list,revlist,priorlist);
						break;
					case 3:
						UCS(argv[3],argv[5],OutputPath,OutputLog,costarr,list,revlist,priorlist);
						break;
					case 4:
						Communities(TieBrk,OutputPath,OutputLog,costarr,list,revlist,priorlist);
						break;
				}
		    }
			catch(IOException e)
			{
				System.out.println(e.toString());
			}
		}
		else
		{
			System.out.println("Invalid arguments !");
			System.exit(1);
		}
	}
	
	private static void BFS(String source, String goal, String OutputPath, String OutputLog, float[][] costarr, HashMap<String, Integer> list, HashMap<Integer, String> revlist, HashMap<Integer, Integer> priorlist) 
	{
		if(list.containsKey(source) && list.containsKey(goal))
		{
			int srcindex=list.get(source);
			ArrayList<Integer> path=new ArrayList<Integer>();
			path.add(srcindex);
			Node srcnode=new Node(srcindex, path, 0);
			
			Queue<Node> que=new LinkedList<Node>();
			que.add(srcnode);
			int n=list.size();
			
			BufferedWriter logwriter= null,pathwriter=null;
			try 
			{
			    logwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OutputLog), "utf-8"));
			    pathwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OutputPath), "utf-8"));
			    
			    ArrayList<Integer> Visited=new ArrayList<Integer>();
			    Visited.add(srcindex);
			    
			    logwriter.write("name,depth,cost");
			    while(!que.isEmpty())
			    {
			    	Node root=que.remove();
			    	int rindex=root.index;
			    	
			    	if(revlist.get(rindex).equalsIgnoreCase(goal))
			    	{
			    		logwriter.newLine();
				    	String w=revlist.get(rindex)+","+(root.path.size()-1)+","+root.pathcost;
			    		logwriter.write(w);
				    	for(int z=0;z<root.path.size();z++)
				    	{
				    		pathwriter.write(revlist.get(root.path.get(z)));
				    		pathwriter.newLine();
				    	}
				    	break;
			    	}
			    	
			    	ArrayList<NodePrior> ChildList=new ArrayList<NodePrior>();
			    	for(int i=0;i<n;i++)
			    	{
			    		if(costarr[rindex][i]!=0)
			    		{
			    			NodePrior np=new NodePrior(i, priorlist.get(i));
			    			ChildList.add(np);
			    		}
			    	}
				
			    	Collections.sort(ChildList);
			    	while(!ChildList.isEmpty())
			    	{
			    		int i=ChildList.remove(0).in;
			    		if(!Visited.contains(i))
			    		{
			    			ArrayList<Integer> cpath=new ArrayList<Integer>();
			    			cpath.addAll(root.path);
			    			cpath.add(i);
			    			Node cnode=new Node(i, cpath, root.pathcost+costarr[rindex][i]);
			    			que.add(cnode);
			    			Visited.add(i);
			    		}
			    	}
			    	logwriter.newLine();
			    	logwriter.write(revlist.get(rindex)+","+(root.path.size()-1)+","+root.pathcost);
			    }
			}
			catch (IOException ex) 
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
			   catch (IOException ex) 
			   {
				   System.out.println(ex.toString());
			   }
			}
		}
		else
		{
			System.out.println("Invalid Source/Goal Name");
		}
	}
	
	private static void DFS(String source, String goal, String OutputPath, String OutputLog, float[][] costarr, HashMap<String, Integer> list, HashMap<Integer, String> revlist, HashMap<Integer, Integer> priorlist) 
	{
		if(list.containsKey(source) && list.containsKey(goal))
		{
			int srcindex=list.get(source);
			ArrayList<Integer> path=new ArrayList<Integer>();
			path.add(srcindex);
			Node srcnode=new Node(srcindex, path, 0);
			
			Stack<Node> stack=new Stack<Node>();
			stack.push(srcnode);
			int n=list.size();
			
			BufferedWriter logwriter= null,pathwriter=null;
			try 
			{
			    logwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OutputLog), "utf-8"));
			    pathwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OutputPath), "utf-8"));
			    
			    ArrayList<Integer> Visited=new ArrayList<Integer>();
			    Visited.add(srcindex);
			    
			    logwriter.write("name,depth,cost");
			    while(!stack.isEmpty())
			    {
			    	Node root=stack.pop();
			    	int rindex=root.index;
			    	
			    	if(revlist.get(rindex).equalsIgnoreCase(goal))
			    	{
			    		logwriter.newLine();
				    	String w=revlist.get(rindex)+","+(root.path.size()-1)+","+root.pathcost;
			    		logwriter.write(w);
				    	for(int z=0;z<root.path.size();z++)
				    	{
				    		pathwriter.write(revlist.get(root.path.get(z)));
				    		pathwriter.newLine();
				    	}
				    	break;
			    	}
			    	
			    	ArrayList<NodePrior> ChildList=new ArrayList<NodePrior>();
			    	for(int i=0;i<n;i++)
			    	{
			    		if(costarr[rindex][i]!=0)
			    		{
			    			NodePrior np=new NodePrior(i, priorlist.get(i));
			    			ChildList.add(np);
			    		}
			    	}
				
			    	Collections.sort(ChildList);
			    	Collections.reverse(ChildList);
			    	while(!ChildList.isEmpty())
			    	{
			    		int i=ChildList.remove(0).in;
			    		if(!Visited.contains(i))
			    		{
			    			ArrayList<Integer> cpath=new ArrayList<Integer>();
			    			cpath.addAll(root.path);
			    			cpath.add(i);
			    			Node cnode=new Node(i, cpath, root.pathcost+costarr[rindex][i]);
			    			stack.push(cnode);
			    			Visited.add(i);
			    		}
			    	}
			    	logwriter.newLine();
			    	logwriter.write(revlist.get(rindex)+","+(root.path.size()-1)+","+root.pathcost);
			    }
			}
			catch (IOException ex) 
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
			   catch (IOException ex) 
			   {
				   System.out.println(ex.toString());
			   }
			}
		}
		else
		{
			System.out.println("Invalid Source/Goal Name");
		}
	}
	
	private static void UCS(String source, String goal, String OutputPath, String OutputLog, float[][] costarr, HashMap<String, Integer> list, HashMap<Integer, String> revlist, HashMap<Integer, Integer> priorlist) 
	{
		if(list.containsKey(source) && list.containsKey(goal))
		{
			int srcindex=list.get(source);
			ArrayList<Integer> path=new ArrayList<Integer>();
			path.add(srcindex);
			Node srcnode=new Node(srcindex, path, 0, priorlist.get(srcindex));
			
			PriorityQueue<Node> PQ=new PriorityQueue<Node>(list.size(), new MyComparator());
			PQ.add(srcnode);
			HashMap<Integer, Node> map=new HashMap<Integer, Node>();
			map.put(srcindex, srcnode);
			
			int n=list.size();
			
			BufferedWriter logwriter= null,pathwriter=null;
			try 
			{
			    logwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OutputLog), "utf-8"));
			    pathwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OutputPath), "utf-8"));
			    
			    ArrayList<Integer> Visited=new ArrayList<Integer>();
			    Visited.add(srcindex);
			    
			    logwriter.write("name,depth,cost");
			    while(!PQ.isEmpty())
			    {
			    	Node root=PQ.remove();
			    	int rindex=root.index;
			    	Visited.add(rindex);
			    	
			    	if(revlist.get(rindex).equalsIgnoreCase(goal))
			    	{
			    		logwriter.newLine();
				    	String w=revlist.get(rindex)+","+(root.path.size()-1)+","+root.pathcost;
			    		logwriter.write(w);
				    	for(int z=0;z<root.path.size();z++)
				    	{
				    		pathwriter.write(revlist.get(root.path.get(z)));
				    		pathwriter.newLine();
				    	}
				    	break;
			    	}
			    	
			    	ArrayList<Integer> ChildList=new ArrayList<Integer>();
			    	for(int i=0;i<n;i++)
			    	{
			    		if(costarr[rindex][i]!=0)
			    		{
			    			ChildList.add(i);
			    		}
			    	}
				
			    	while(!ChildList.isEmpty())
			    	{
			    		int i=ChildList.remove(0);
			    		if(!Visited.contains(i))
			    		{
			    			if(map.containsKey(i))
			    			{
			    				if(map.get(i).pathcost>root.pathcost+costarr[rindex][i])
			    				{
			    					Node update=map.remove(i);
			    					if(PQ.contains(update))
			    						PQ.remove(update);
			    					update.pathcost=root.pathcost+costarr[rindex][i];
			    					ArrayList<Integer> cpath=new ArrayList<Integer>();
				    				cpath.addAll(root.path);
				    				cpath.add(i);
			    					update.path=cpath;
			    					PQ.add(update);
			    					map.put(i,update);
			    				}
			    				else if(map.get(i).pathcost==root.pathcost+costarr[rindex][i])
			    				{
			    					if(priorlist.get(rindex)<priorlist.get(i))
			    					{
			    						Node update=map.remove(i);
				    					if(PQ.contains(update))
				    						PQ.remove(update);
				    					ArrayList<Integer> cpath=new ArrayList<Integer>();
					    				cpath.addAll(root.path);
					    				cpath.add(i);
				    					update.path=cpath;
				    					PQ.add(update);
				    					map.put(i,update);
				    				}
			    				}
			    			}
			    			else
			    			{
			    				ArrayList<Integer> cpath=new ArrayList<Integer>();
			    				cpath.addAll(root.path);
			    				cpath.add(i);
			    				float pathcost=root.pathcost+costarr[rindex][i];
			    				Node cnode=new Node(i, cpath, pathcost, priorlist.get(i));
			    				PQ.add(cnode);
			    				map.put(i, cnode);
			    			}
			    		}
			    	}
			    	logwriter.newLine();
			    	logwriter.write(revlist.get(rindex)+","+(root.path.size()-1)+","+root.pathcost);
			    }
			}
			catch (IOException ex) 
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
			   catch (IOException ex) 
			   {
				   System.out.println(ex.toString());
			   }
			}
		}
		else
		{
			System.out.println("Invalid Source/Goal Name");
		}
	}
	
	private static void Communities(String TieBrk, String OutputPath, String OutputLog, float[][] costarr, HashMap<String, Integer> list, HashMap<Integer, String> revlist, HashMap<Integer, Integer> priorlist)
	{
		ArrayList<String> allnodes=new ArrayList<String>();
		InputStream fin=null;
		BufferedReader dis=null;
		try
		{
			fin=new FileInputStream(TieBrk);
			dis=new BufferedReader(new InputStreamReader(fin));
			String line=dis.readLine();
			while(line!=null)
			{
				if(!allnodes.contains(line))
				{
					allnodes.add(line);
				}
				line=dis.readLine();
			}
		}
		catch (IOException ex) 
		{
			System.out.println(ex.toString());
		}
		finally 
		{
		   try 
		   {
			   fin.close();
			   dis.close();
		   }
		   catch (IOException ex) 
		   {
			   System.out.println(ex.toString());
		   }
		}
		
		BufferedWriter logwriter= null,pathwriter=null;
		try 
		{
			logwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OutputLog), "utf-8"));
			pathwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OutputPath), "utf-8"));
			logwriter.write("name,depth,group");
			
			ArrayList<Integer> Visited=new ArrayList<Integer>();
			int group=1;
			
			while(Visited.size()!=allnodes.size())
			{
				int srcindex=0;
				for(int m=0;m<allnodes.size();m++)
				{
					int t=list.get(allnodes.get(m));
					if(!Visited.contains(t))
					{
						srcindex=t;
						break;
					}
				}
				
				ArrayList<Integer> path=new ArrayList<Integer>();
				path.add(srcindex);
				Node srcnode=new Node(srcindex, path, 0);
					
				Queue<Node> que=new LinkedList<Node>();
				que.add(srcnode);
				ArrayList<String> nodepathlist=new ArrayList<String>();
				nodepathlist.add(revlist.get(srcindex));
				Visited.add(srcindex);
				
				while(!que.isEmpty())
			    {
			    	Node root=que.remove();
			    	int rindex=root.index;
			    	
			    	ArrayList<NodePrior> ChildList=new ArrayList<NodePrior>();
			    	for(int i=0;i<list.size();i++)
			    	{
			    		if(costarr[rindex][i]!=0)
			    		{
			    			NodePrior np=new NodePrior(i, priorlist.get(i));
			    			ChildList.add(np);
			    		}
			    	}
				
			    	Collections.sort(ChildList);
			    	while(!ChildList.isEmpty())
			    	{
			    		int i=ChildList.remove(0).in;
			    		if(!Visited.contains(i))
			    		{
			    			ArrayList<Integer> cpath=new ArrayList<Integer>();
			    			cpath.addAll(root.path);
			    			cpath.add(i);
			    			Node cnode=new Node(i, cpath, 0);
			    			que.add(cnode);
			    			Visited.add(i);
			    			nodepathlist.add(revlist.get(i));
			    		}
			    	}
			    	logwriter.newLine();
			    	logwriter.write(revlist.get(rindex)+","+(root.path.size()-1)+","+group);
			    }
				group++;
				logwriter.newLine();
				logwriter.write("---------------------");
				Collections.sort(nodepathlist);
				for(int z=0;z<nodepathlist.size();z++)
		    	{
		    		if(z==nodepathlist.size()-1)
		    			pathwriter.write(nodepathlist.get(z));
		    		else
		    			pathwriter.write(nodepathlist.get(z)+",");
		    	}
				pathwriter.newLine();
			}
		}
		catch (IOException ex) 
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
			catch (IOException ex) 
			{
			   System.out.println(ex.toString());
			}
		}
		
	}
}

class Node
{
	int index;
	ArrayList<Integer> path=new ArrayList<Integer>();
	float pathcost;
	int priority;
	
	Node(int index, ArrayList<Integer> path, float pathcost)
	{
		this.index=index;
		this.path=path;
		this.pathcost=pathcost;
	}
	
	Node(int index, ArrayList<Integer> path, float pathcost, int priority)
	{
		this.index=index;
		this.path=path;
		this.pathcost=pathcost;
		this.priority=priority;
	}
	
}

class NodePrior implements Comparable<NodePrior>
{
	int in=0;
	int pr=0;
	public NodePrior(int in, int pr) 
	{
		this.in=in;
		this.pr=pr;
	}
	
	@Override
	public int compareTo(NodePrior o)
	{
		return this.pr-o.pr;
	}
}

class MyComparator implements Comparator<Node>
{

	@Override
	public int compare(Node o1, Node o2)
	{
		int i=0;
		DecimalFormat df = new DecimalFormat("###.#");
		float f=Float.parseFloat(df.format(o1.pathcost-o2.pathcost));
		if(f<0)
			i=-1;
		else if(f>0)
			i=1;
		else if(f==0)
		{
			if(o1.priority<o2.priority)
				i=-1;
			else
				i=1;
		}
		return i;
	}
}
