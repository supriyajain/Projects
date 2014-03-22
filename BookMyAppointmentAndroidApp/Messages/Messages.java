package Messages;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Messages 
{
	public void save_Message(String sender, String receiver, String date, String time, String msg)
	{
		ParseObject val=new ParseObject("Messages");
		val.put("Sender", sender);
		val.put("Receiver", receiver);
		val.put("Date", date);
		val.put("Time", time);
		val.put("Msg", msg);
		val.put("Flag", 0);
		val.saveEventually();
	}
	
	
	public ArrayList<ArrayList<String>> fetch_Message(String email, int flag)
	{
		final ArrayList<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		
		ParseQuery query = new ParseQuery("Messages");
		query.whereEqualTo("Receiver", email);
		query.whereEqualTo("Flag", flag);
		try
		{
			List<ParseObject> objects=query.find();
			if (objects!= null) 
		    {
		    	for(ParseObject object: objects)
		    	{
		    		ArrayList<String> value=new ArrayList<String>();
		    		value.add(object.getString("Sender"));
		    		value.add(object.getString("Receiver"));
		    		value.add(object.getString("Date"));
		    		value.add(object.getString("Time"));
		    		value.add(object.getString("Msg"));
		    		value.add(object.getString("Flag"));
		    		value.add(object.getObjectId());
					data.add(value);
		    	}
		    }
		    Collections.sort(data, new Comparator<ArrayList<String>>()
		    {
		    	@Override
		    	public int compare(ArrayList<String> lhs, ArrayList<String> rhs) 
		    	{
					Date ld=new Date(lhs.get(2));
					Date rd=new Date(rhs.get(2));
					int i=Integer.signum(-(ld.compareTo(rd)));
							
					if(i==0)
					{
						i=mycomp(lhs.get(3),rhs.get(3));
					}
							
					return i;
				}
						
				public int mycomp(String date1, String date2)
				{
					int j=0;
					int date1hour=Integer.parseInt(date1.substring(0, date1.indexOf(":")));
					int date2hour=Integer.parseInt(date2.substring(0, date2.indexOf(":")));
					int date1mins=Integer.parseInt(date1.substring(date1.indexOf(":")+1,date1.indexOf(" ")));
					int date2mins=Integer.parseInt(date2.substring(date2.indexOf(":")+1,date2.indexOf(" ")));
							
					if(date1hour>date2hour)
						j=-1;
					else if(date1hour<date2hour)
						j=1;
					else
					{
						if(date1mins>date2mins)
							j=-1;
						else if(date1mins<date2mins)
							j=1;
						else
							j=0;
					}
							
					return j;
				}
								
			});
		}
		catch(ParseException e)
		{
			System.out.println(e.getCode());
		}
		
		return data;
	}
	
	
	public void Flag_Update(String sender, String receiver, String date, String time)
	{
		ParseQuery query = new ParseQuery("Messages");
		query.whereEqualTo("Sender", sender);
		query.whereEqualTo("Receiver",receiver);
		query.whereEqualTo("Date",date);
		query.whereEqualTo("Time",time);
		try
		{
			ParseObject val=query.getFirst();
		    if (val != null) 
		    {
		    	val.put("Flag", 1);
				val.saveInBackground();
		    }
		}
		 catch(ParseException e)
		 {
		   	System.out.println(e.getCode());
		 }
	}
	
	public void Msg_Delete(String code)
	{
		ParseQuery query = new ParseQuery("Messages");
		try
		{
			ParseObject po=query.get(code);
			if(po!=null)
		    	po.delete();
		}
		 catch(ParseException e)
		 {
		   	System.out.println(e.getCode());
		 }
	}
}
