package Client;

import java.util.Collection;
import java.util.LinkedList;

public class Node {
 char content;
 boolean marker; 
 Collection<Node> child;
 
 public Node(char c){
  child = new LinkedList<Node>();
  marker = false;
  content = c;
 }
 public Node(){
  child = new LinkedList<Node>();
  marker = false;
  //content = null;
 }
 public Node subNode(char c){
  if(child!=null){
   for(Node eachChild:child){
    if(eachChild.content == c){
     return eachChild;
    }
   }
  }
  return null;
 }

public Node subChild()
{
    if(child!=null)
    {
        for (Node eachchild:child)
        {
            return eachchild; 
        }
    }
  return null;
}
}