import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.XML;

public class jserv extends HttpServlet 
{
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException 
	{
		PrintWriter out=response.getWriter();
		try
		{
			String param1=request.getParameter("location");
			String param2=request.getParameter("type");
			String param3=request.getParameter("tempUnit");
			String text=param1.replaceAll("\\s","%20");

		
			String urlString="http://default-environment-2j3wffp8pf.elasticbeanstalk.com/?location="+text+"&type="+param2+"&tempUnit="+param3;
			URL url=new URL(urlString);	
			URLConnection urlConnection=url.openConnection();	
			urlConnection.setAllowUserInteraction(false);	
			InputStream urlStream=url.openStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(urlStream)); 
			StringBuffer st=new StringBuffer();
			while(br.readLine()!=null)
			{
				st.append(br.readLine());
			}
			
			JSONObject j=null;
		
			j=XML.toJSONObject(st.toString());
			out.println(j);
		}
		catch(Exception e)
		{
			out.println(e.toString());
		}
	}
}