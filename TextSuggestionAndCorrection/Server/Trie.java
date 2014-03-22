package Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

 public class Trie{
 public static Node root;
 int i =0,k=0;

 Collection<String> word=new LinkedList<String>();
 public Trie(){
  root = new Node(' ');
 }
 
 public void insert(String s){
  Node current = root; 
  if(s.length()==0)
   current.marker=true;
  for(int l=0;l<s.length();l++){
   Node child = current.subNode(s.charAt(l));
   if(child!=null){ 
    current = child;
   }
   else{
    current.child.add(new Node(s.charAt(l)));
    current = current.subNode(s.charAt(l));
   }
   
   if(l==s.length()-1)
    current.marker = true;
  } 
 }
 
 public String search(String s){

  Node current = root;
  String str1 =new String();
  String str2 =new String();
  while(current != null){
   for(int jp=0;jp<s.length();jp++){
    if(current.subNode(s.charAt(jp)) == null)
     return null;
    else
    {
         
        current = current.subNode(s.charAt(jp));
     }
    str1=str1+current.content;
   }
   
   if (current.marker == true)
   {
         return s ;
   }
   else if(current.marker!=true)
   {
        str2=str1.substring(0, str1.length()-1);
        word.removeAll(word);
        retrive(current,str2);
        return "Suggestionlist";
   }
   }
  return null;
 }

 public void retrive (Node parent,String prstr)
 {
    prstr =prstr+parent.content;
      
     if(parent.marker==true)
     {
         word.add(prstr);
     }
    for (Node eachchild :parent.child)
    {
        retrive(eachchild,prstr);
        i++;
         }
     }
public Collection<String> suggestionlist()
{
                return word;
 }

public Collection<String> correction(String s1)
{
    word.removeAll(word);
    retrive(root,"");
    char[] c1=new char[s1.length()];
    Collection<String> corrlist=new LinkedList<String>();
    Collection<String> corrsufflist=new LinkedList<String>();
    for(int p=0;p<s1.length();p++)
    {
        c1[p]=s1.charAt(p);
    }
    int min=5;
    for(String s2:word)
    {
        
        char [] c2=new char[s2.length()-1];
        for(int f=0;f<s2.length()-1;f++)
        {
            c2[f]=s2.charAt(f+1);
        }
       int dist=EditDistance(c1,c2);
       if(min>dist)
       {
            min=dist;
            corrlist.removeAll(corrlist);
            corrlist.add(s2);

        }
        else if(min==dist)
           corrlist.add(s2);
    }
    BufferedReader br = null;
    Collection<String> suffix=new LinkedList<String>();
        try{
                   br = new BufferedReader(new FileReader(new File("C:/Dictionary/suffix.txt")));
                    String eachLine = null;
                    while((eachLine=br.readLine())!=null)
                    {
                            suffix.add(eachLine);
                    }
            }catch (Exception e) {
                } finally{
            if(br!=null)
            {
                try {
                        br.close();
                      } catch (IOException e) {
                        e.printStackTrace();
                    }
              }
                            }
    String addsuff;
    for(String str:corrlist)
    {
        System.out.println("added word="+str);
        for(String suff:suffix)
        {
            addsuff = str.trim().concat(suff);
            System.out.println("added word after suffix="+addsuff);
            char ch2[]=new char[addsuff.length()];
            for(int p2=0;p2<addsuff.length();p2++)
            {
                ch2[p2]=addsuff.charAt(p2);
            }
            int dist=EditDistance(c1,ch2);
            if(min>dist)
            {
                min=dist;
                corrsufflist.removeAll(corrsufflist);
                corrsufflist.add(addsuff);
            }
            else if(min==dist)
                corrsufflist.add(addsuff);
        }
    }
    corrlist.addAll(corrsufflist);
    return corrlist;
  }
 public int EditDistance(char c1[], char c2[])
 {
     int m=c1.length+1;
     int n=c2.length+1;
     int[][] d=new int[m][n];

   for(int l=0;l<m;l++)
     d[l][0] = l;
   for(int j=0;j<n;j++)
     d[0][j]=j;

   for(int j=1;j<n;j++)
   {
     for(int l=1;l<m;l++)
     {
       if (c1[l-1]==c2[j-1])
       {
         d[l][j]=d[l-1][j-1];
       }
       else
       {
         d[l][j]= Math.min(Math.min(d[l-1][j],d[l][j-1]),d[l-1][j-1])+1;
        }
        
     }
  }
  return d[m-1][n-1];
 }
    


}