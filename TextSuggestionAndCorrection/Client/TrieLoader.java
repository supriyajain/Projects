package Client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TrieLoader {
  
 public static Trie trieDSA;
 
 public static void main(String[] args) {
  TrieLoader trieLoader = new TrieLoader();
  trieLoader.load();
  new NewJFrame();
 }
  
 public void load(){
  trieDSA = new Trie();
  BufferedReader br = null;
  try {
   br = new BufferedReader(new FileReader(new File("C:/TS&C/word.txt")));
   String eachLine = null;
   while((eachLine=br.readLine())!=null){
    trieDSA.insert(eachLine);
    
   }
  } catch (Exception e) {
   e.printStackTrace();
  } finally{
   if(br!=null){
    try {
     br.close();
    } catch (IOException e) {
     e.printStackTrace();
    }
   }
  }
 }
}