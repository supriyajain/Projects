package Client;



import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

public class Receiver extends Thread
{
        BufferedReader buf;
        JTextArea ta;
        public Receiver(BufferedReader buf,JTextArea ta)
        {
                this.buf=buf;
                this.ta=ta;
        }
    @Override
        public void run()
        {
                while(true)
                {
            try {
                String text = buf.readLine();
                if (text != null) {
                    ta.append("server says: "+text + "\n");
                }
            } catch (IOException ex) {
                Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            }
                }
        }
}
