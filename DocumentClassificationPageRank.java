import java.io.*;
import java.util.*;
import java.util.logging.*;

public class main
{
    public static void main(String[] args)
    {
        FileInputStream fis=null,fis1 = null;
        BufferedWriter out=null,out1=null;
        DataInputStream dis=null,dis1=null;
        BufferedReader br=null,br1=null;
        try
        {

                //First create global dictionary and then create frequency file for each doc....calculate frequency of each word in each doc..
                Map<String, Integer> m = new TreeMap<String, Integer>();
                Map<String, Integer> m1 = new TreeMap<String, Integer>();
                int noOfDocs=100;
                String catNames[]={"Atheism","Auto","Baseball","Christian","Crypt"};
                int noOfcat=catNames.length,noOfLabelledDocs=5,noOfDocsInEachCat=20;
                File directory = new File("Data");
                File[] files = directory.listFiles();
                for (File f : files)
                {
                    fis = new FileInputStream(f);
                    dis = new DataInputStream(fis);
                    br = new BufferedReader(new InputStreamReader(dis));
                    String str,strput;
                    while ((str = br.readLine()) != null)
                    {
                        StringTokenizer st = new StringTokenizer(str,",&&--&& &&\"&&(&&)&&[&&]&&{&&}&&<&&>&&!&&:&&.&&_&&-&&*&&#&&^&&?&&;&&|&&`&&~&&+&&=&&/&&$&&%",false);
                        while (st.hasMoreTokens())
                        {
                            strput=st.nextToken();
                            m.put(strput,1);

                        }
                    }
                }
                File f2=new File("Dictionary.txt");
                out = new BufferedWriter(new FileWriter(f2));
                String mystr;
                for (String key:m.keySet())
                {
                    mystr=key+"\n";
                    //System.out.println(mystr);
                    out.write(mystr); /**/
                }
                out.close();
                System.out.println("Global dictionay size: "+m.size());

                String str,str1;
                int freq;
                for(int z=0;z<files.length;z++)
                {
                    fis = new FileInputStream(files[z]);
                    dis = new DataInputStream(fis);
                    br = new BufferedReader(new InputStreamReader(dis));
                    while ((str = br.readLine()) != null)
                    {
                        StringTokenizer st = new StringTokenizer(str,",&&--&& &&\"&&(&&)&&[&&]&&{&&}&&<&&>&&!&&:&&.&&_&&-&&*&&#&&^&&?&&;&&|&&`&&~&&+&&=&&/&&$&&%",false);
                        while (st.hasMoreTokens())
                        {
                            str1=st.nextToken();
                            if(m1.get(str1)==null)
                                freq=0;
                            else
                                freq=m1.get(str1);
                            m1.put(str1,(freq==0) ? 1 : freq + 1); /**/
                        }
                    }
                    for(String dickey:m.keySet())
                    {
                        if(m1.get(dickey)==null)
                            m1.put(dickey,0);
                        else
                            continue;
                    }
                    File f3=new File("FreqData/text_"+(z+1)+".txt");
                    out1 = new BufferedWriter(new FileWriter(f3));
                    String mystr1;
                    for (String key:m1.keySet())
                    {
                       mystr1=m1.get(key)+"\n";
                       //System.out.println(key+"-->"+mystr1);
                       out1.write(mystr1); /**/
                    }
                    out1.close();
                    //System.out.println("Document "+z+" size: "+m1.size());
                    m1.clear();
                }

                //calculating cosine similarity between all docs..
                int docsim[][]=new int[noOfDocs][noOfDocs];
                int value1,value2,numerator=0,deno1=0,deno2=0;//,c=0;
                double denominator,sim;
                File directory1 = new File("FreqData");
                File[] files1 = directory1.listFiles();
                for(int u=0;u<files1.length;u++)
                {
                    for(int v=0;v<files1.length;v++)
                    {
                        if(u==v)
                            docsim[u][v] = 1;
                        else
                        {
                            fis = new FileInputStream(files1[u]);
                            dis = new DataInputStream(fis);
                            br = new BufferedReader(new InputStreamReader(dis));
                            fis1 = new FileInputStream(files1[v]);
                            dis1 = new DataInputStream(fis1);
                            br1 = new BufferedReader(new InputStreamReader(dis1));
                            while ((str = br.readLine()) != null)
                            {
                                //System.out.println(c);
                            	value1=Integer.parseInt(br.readLine()); /**/
                                value2=Integer.parseInt(br1.readLine()); /**/
                                numerator=numerator+(value1*value2);
                                deno1=deno1+value1*value1;
                                deno2=deno2+value2*value2;
                                //c++;
                            }
                            denominator=Math.sqrt(deno1)*Math.sqrt(deno2);
                            sim=numerator/denominator; /**/
                            //System.out.println(sim);
                            if(sim>= .01)
                                docsim[u][v]=1;
                            else
                                docsim[u][v]=0;
                        }
                        numerator=0;deno1=0;deno2=0;
                    }
                }
                /*for(int t=0;t<noOfDocs;t++)
                {
                	for (int r=0;r<noOfDocs;r++)
                		System.out.print(docsim[t][r]);
                	System.out.println("\n");
                }*/
                //calculate page rank for each category for doc...
                int p=0;
                double docPR[][]=new double[noOfDocs][noOfcat];
                double d=0.5,pr=0.0;
                for(int i=0;i<noOfcat;i++)
                {
                    for(int j=0;j<noOfDocs;j++)
                    {
                        docPR[j][i]=0.0;
                    }
                    for(int k=0;k<noOfLabelledDocs;k++)
                    {
                        System.out.println(p+","+k);
                    	docPR[p][i]=1.0/noOfLabelledDocs; // you need to write 1.0 else it will become zero.. 1/anything(int) =0
                    	// so you have labeled docs as 0 to noOfLabelledDocs and then  noOfDocsInEachCat to noOfDocsInEachCat+noOfLabelledDocs and so on.. right?
                        p++;
                    }
                    for(int a=0;a<200;a++)
                    {
                        for(int g=0;g<noOfDocs;g++)
                        {
                            for(int h=0;h<noOfDocs;h++)
                            {
                                if(g!=h && docsim[g][h]==1)
                                {
                                    int count=0;
                                    for(int b=0;b<noOfDocs;b++)
                                    {
                                        if(docsim[h][b]==1) 
                                            count++;
                                    }
                                    pr=pr+(docPR[h][i]/count); /**/
                                }
                            }
                            docPR[g][i]=((1-d)/noOfDocs)+(d*pr);
                            pr=0.0;
                        }
                    }
                    p=p+(noOfDocsInEachCat-noOfLabelledDocs); // why this?
                }

                for(int t=0;t<noOfDocs;t++)
                {
                	for (int r=0;r<noOfcat;r++)
                		System.out.print(docPR[t][r]+" , ");
                	System.out.println("\n");
                }
                //Identify category on the basis of highest page rank of a doc for a category...
                String result="";
                for(int y=0;y<noOfDocs;y++)
                {
                    int max=0;
                    for(int x=1;x<noOfcat;x++)
                    {
                        if(docPR[y][x]>docPR[y][max])
                            max=x; /**/
                    }
                    result=result+"Document number "+(y+1)+" belongsto "+catNames[max]+" Category\n";
                }

                System.out.println(result);

        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
                fis1.close();
                out.close();
                out1.close();
                } catch (IOException ex) {

Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }

    }

}
