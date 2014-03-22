package Client;

import java.io.*;
import java.net.*;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class NewJFrame extends javax.swing.JFrame {

    String s ="",laststr="'";
    Boolean sugdis=false;
    Boolean corrdis=true;
    Highlighter h;
    HashMap incorrwrd;
    Boolean enable=false;
    Socket soc;
    ServerSocket ssoc;
    PrintWriter pw;
    BufferedReader buf;
    
    public NewJFrame()
    {
        initComponents();
        jTextArea1.setSelectedTextColor(Color.red);
        jTextArea1.setSelectionColor(Color.yellow);
        h=jTextArea1.getHighlighter();
        this.setVisible(true);
        this.setExtendedState(MAXIMIZED_BOTH);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Client");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setFocusable(false);
        jTextArea1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextArea1CaretUpdate(evt);
            }
        });
        jTextArea1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jTextArea1CaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        jComboBox1.setFocusable(false);
        jComboBox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox1KeyPressed(evt);
            }
        });

        jButton1.setText("Deactivate Suggestion");
        jButton1.setToolTipText("");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Deactivate Correction");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Add Word");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Activate Suggestion");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Correct Text");
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Active Correction");
        jButton6.setEnabled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Start");
        jButton7.setEnabled(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Send");
        jButton8.setEnabled(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Connect");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setFocusable(false);
        jScrollPane2.setViewportView(jTextArea2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                                .addGap(51, 51, 51)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(129, 129, 129)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
                                .addGap(65, 65, 65)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(55, 55, 55)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jButton3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(jButton6)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextArea1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyPressed
        char c = evt.getKeyChar();
        //Suggestion---->
        if(sugdis==false && corrdis==true)
        {
            BufferedReader br = null;
            Collection<String> suffix=new LinkedList<String>();
            try{
                   br = new BufferedReader(new FileReader(new File("C:/TS&C/suffix.txt")));
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
        if(evt.getKeyText(evt.getKeyCode()).equals("Page Down"))
        {
            System.out.println("in page down="+s);
            jComboBox1.showPopup();
            jComboBox1.requestFocus();
        }
        else if (evt.getKeyText(evt.getKeyCode()).equals("Backspace"))
        {
                jComboBox1.removeAllItems();
                int index=jTextArea1.getText().lastIndexOf(" ")+1;
                int len=s.length()-1;
                if(index>=1 && len>0)
                {
                    try {
                            s = jTextArea1.getText(index, len);
                          } catch (BadLocationException ex) {
                                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                                }

                }
                else
                {
                        len=jTextArea1.getText().length()-1;
                        if(index==0 && len>0)
                        {
                                jComboBox1.removeAllItems();
                                try {
                                        s = jTextArea1.getText(0, len);
                                      } catch (BadLocationException ex) {
                                            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                        }
                        else
                        {
                                 jComboBox1.removeAllItems();
                                laststr=s;
                                s="";
                        }
                }
          }
          else if(c >= 97 && c <= 122 )
           {
                s =s+c;
                jComboBox1.removeAllItems();
            }
            else
            {
                    laststr=s;
                    s="";
                    jComboBox1.removeAllItems();
            }

            if(evt.getKeyText(evt.getKeyCode()).equals("Space")||s.equals(""))
            {
            }
            else
            {
               
            if(TrieLoader.trieDSA.search(s)==null)
            {
            }
            else if(TrieLoader.trieDSA.search(s).equals(s))
            {
                jComboBox1.removeAllItems();
                for(String suff:suffix)
                {
                        jComboBox1.addItem(s.concat(suff));
                }
            }
            else if(TrieLoader.trieDSA.search(s).equals("Suggestionlist"))
            {
                jComboBox1.removeAllItems();
                for(String wr:TrieLoader.trieDSA.suggestionlist())
                {
                        jComboBox1.addItem(wr);
                }
            }
          }
   }


        //Correction---->
        else if(sugdis==true && corrdis==false)
        {

            if(evt.getKeyText(evt.getKeyCode()).equals("Space"))
            {
                    try {
                            int index = jTextArea1.getText().lastIndexOf(" ") + 1;
                            int len = jTextArea1.getText().length() - index;
                            if (index > 0)
                            {
                                s = jTextArea1.getText(index, len);
                            }
                            else
                            {
                                s = jTextArea1.getText();
                            }
                            if (s ==null || s.equals(""))
                            {
                            }
                            else
                            {
                                System.out.println("s=" + s );
                                if (TrieLoader.trieDSA.search(s) == null || TrieLoader.trieDSA.search(s).equals("Suggestionlist"))
                                {
                                    int corrlen = jTextArea1.getText().length() - s.length();
                                    try {
                                         Object o=h.addHighlight(corrlen, corrlen + s.length(), DefaultHighlighter.DefaultPainter);
                                        
                                    } catch (BadLocationException ex) {
                                                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                    jComboBox1.removeAllItems();
                                    for (String st : TrieLoader.trieDSA.correction(s))
                                    {
                                            jComboBox1.addItem(st.trim());
                                    }
                                }
                                else if(h.getHighlights()!=null)
                                h.removeAllHighlights();
                                laststr = s;
                            }
                        } catch (BadLocationException ex) {
                            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
            }
            else if(evt.getKeyText(evt.getKeyCode()).equals("Page Down"))
            {
                    jComboBox1.showPopup();
                    jComboBox1.requestFocus();
            }
            else
            {
                laststr=s;
                s="";
                jComboBox1.removeAllItems();
            }
        }
    }//GEN-LAST:event_jTextArea1KeyPressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        h.removeAllHighlights();
        System.out.println("jtlen="+jTextArea1.getText().trim().length());
        System.out.println("slen="+s.length());
        s=s.trim();
        int index=jTextArea1.getText().trim().length()-s.length();
        System.out.println("index="+index);
        if(index>0 && s.length()>0)
        {
            System.out.println("inside if ");
            jTextArea1.setSelectionStart(index);
            jTextArea1.setSelectionEnd(jTextArea1.getText().trim().length());
            System.out.println("end="+jTextArea1.getText().trim().length());
        }
        else if(index==0 && s.length()>0)
        {
            System.out.println("inside else");
            jTextArea1.setSelectionStart(0);
            jTextArea1.setSelectionEnd(s.length());
        }

        if(jTextArea1.getSelectedText()!=null)
       {
            String str=jTextArea1.getSelectedText();
           System.out.println("selected text="+str);
           if(str==null || str.equals(" "))
           {
                    JOptionPane.showMessageDialog(this, "The selected text is empty....can't be added in dictionary!!","Error",JOptionPane.ERROR_MESSAGE);
           }
            else{
                        if(TrieLoader.trieDSA.search(str)==null ||TrieLoader.trieDSA.search(str).equals("Suggestionlist") )
                         {
                            int confrm=JOptionPane.showConfirmDialog(this, "Do you want to add '"+str+"' word in dictionary????","Confirm",JOptionPane.YES_NO_OPTION);
                            if(confrm==0)
                            {
                            BufferedWriter bw = null;
                            try {
                                        bw = new BufferedWriter(new FileWriter("C:/Dictionary/word.txt",true));
                                        bw.write("\n");
                                        bw.write(str);
                                        bw.flush();
                                        bw.close();
                                } catch (IOException ex) { }
                            TrieLoader.trieDSA.insert(str);
                            JOptionPane.showMessageDialog(this, "The word '"+str+"'added in dictionary!!","Done",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        else
                            JOptionPane.showMessageDialog(this, "The word already exist in dictionary!!","Error",JOptionPane.ERROR_MESSAGE);
                 
                   }

       }
            else
                    JOptionPane.showMessageDialog(this, "First select the word to be added !!","Error",JOptionPane.ERROR_MESSAGE);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox1KeyPressed
        if(corrdis==false )
            s=laststr+" ";
        if(evt.getKeyText(evt.getKeyChar()).equals("Enter"))
        {
            System.out.println("in combo s="+s);
            int st =jTextArea1.getText().length()-s.length();
            int end =st+s.length();
            s=jComboBox1.getSelectedItem().toString();
            jTextArea1.replaceRange(jComboBox1.getSelectedItem().toString(), st, end);
            jTextArea1.requestFocus();
       }
       else if(evt.getKeyText(evt.getKeyCode()).equals("Shift"))
        {
            jComboBox1.hidePopup();
            jTextArea1.requestFocus();
        }
    }//GEN-LAST:event_jComboBox1KeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
            sugdis=true;
            corrdis=false;
            jButton1.setEnabled(false);
            jButton4.setEnabled(true);
            jButton2.setEnabled(true);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
            sugdis=true;
            corrdis=true;
            jComboBox1.removeAllItems();
            jButton1.setEnabled(false);
            jButton4.setEnabled(true);
            jButton5.setEnabled(true);
            jButton6.setEnabled(true);
            jButton2.setEnabled(false);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
            sugdis=false;
            corrdis=true;
            enable=false;
            jButton1.setEnabled(true);
            jButton4.setEnabled(false);
            jButton5.setEnabled(false);
            jButton2.setEnabled(false);
            jButton6.setEnabled(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
      Collection<Collection> coll = new LinkedList<Collection>();
      int index=0;
      incorrwrd=new HashMap<String,Collection>();
     if(jTextArea1.getText().length()==0||jTextArea1.getText().equals(" "))
     {
     }
     else
     {
     String splittext[]=jTextArea1.getText().split(" ");
     for(String st:splittext)
     {
       

         if(TrieLoader.trieDSA.search(st)==null)
         {
              Collection<String> innercoll=new LinkedList<String>();
             System.out.println("incorrect text="+st);
             for(String item:TrieLoader.trieDSA.correction(st))
             {
                    try {
                        index = jTextArea1.getText().indexOf(st);
                         
                        innercoll.add(item);
                        h.addHighlight(index, index + st.length(), DefaultHighlighter.DefaultPainter);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
             }
             incorrwrd.put(st,innercoll);
           }
       else if(TrieLoader.trieDSA.search(st).equals("Suggestionlist"))
       {
             Collection<String> innercoll=new LinkedList<String>();
           System.out.println("suggested text="+st);
           for(String wr:TrieLoader.trieDSA.suggestionlist())
           {
              
               try {
                        index = jTextArea1.getText().indexOf(st);
                        innercoll.add(wr);
                        h.addHighlight(index, index + st.length(), DefaultHighlighter.DefaultPainter);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
           }
           incorrwrd.put(st,innercoll);
        }
     }
     System.out.println(jTextArea1.getCaretPosition());
     for(Collection out:coll)
     {
      for(Object item:out)
      {
         System.out.println("item="+item);
      }
         System.out.println("\n");
     }

    enable=true;
     }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextArea1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextArea1CaretUpdate

        if(enable)
        {

            int pos =jTextArea1.getCaretPosition();
            int index =jTextArea1.getText().indexOf(" ", pos);
            String wrd=null;
            if(pos==jTextArea1.getText().length())
            {}
            else if(index == -1)
            {
                try {
                    wrd = jTextArea1.getText(pos, jTextArea1.getText().length()-pos);
                } catch (BadLocationException ex) {
                    Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
          else if(index==-1&&pos==0)
              wrd=jTextArea1.getText();
            else if(index>0){
              wrd=jTextArea1.getText().substring(pos, jTextArea1.getText().indexOf(" ", pos));
            System.out.println("wrd="+wrd);
            s=wrd;
                System.out.println("in corr s="+s);
            if(incorrwrd.containsKey(wrd))
            {
                Collection<String> c=(Collection<String>)incorrwrd.get(wrd);
                jComboBox1.removeAllItems();
                for(String stt: c)
                {
                    jComboBox1.addItem(stt);
                }
            }
           }
        }
    }//GEN-LAST:event_jTextArea1CaretUpdate

    private void jTextArea1CaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextArea1CaretPositionChanged

    }//GEN-LAST:event_jTextArea1CaretPositionChanged

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
            sugdis=true;
            corrdis=false;
            enable=false;
            jButton1.setEnabled(false);
            jButton4.setEnabled(false);
            jButton5.setEnabled(false);
            jButton2.setEnabled(true);
            jButton6.setEnabled(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
            String host=JOptionPane.showInputDialog("Enter host name");
        try {
            soc = new Socket(host, 8080);
        } catch (UnknownHostException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        jButton9.setVisible(false);
        jButton7.setEnabled(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try{
                pw = new PrintWriter(soc.getOutputStream());
                buf = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                Receiver th = new Receiver(buf, jTextArea2);
                th.start();
            } catch (IOException ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
        jButton7.setVisible(false);
        jButton1.setEnabled(true);
        jButton3.setEnabled(true);
        jButton8.setEnabled(true);
        jTextArea1.setFocusable(true);
        jComboBox1.setFocusable(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        String text=jTextArea1.getText();
        pw.println(text);
        pw.flush();
        jTextArea2.append("Me: "+jTextArea1.getText() + "\n");
        jTextArea1.setText("");
    }//GEN-LAST:event_jButton8ActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables

}
