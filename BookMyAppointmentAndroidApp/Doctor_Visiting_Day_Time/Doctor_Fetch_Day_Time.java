package Doctor_Visiting_Day_Time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Doctor_Fetch_Day_Time
{
	
	public ArrayList<ArrayList<String>> Fetch_Day_Time(String email)
	{
		final ArrayList<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		
		ParseQuery query = new ParseQuery("Doctor_Visiting_Day_Time");
		query.whereEqualTo("ID", email);
		try
		{
			List<ParseObject> object=query.find();
		    if (object!= null) 
		    {
		    	for(ParseObject po: object)
		    	{
		    		ArrayList<String> value=new ArrayList<String>();
		    		value.add(po.getString("ID"));
		    		value.add(po.getString("Day"));
		    		value.add(po.getString("Morning"));
		    		value.add(po.getString("Afternoon"));
		    		value.add(po.getString("Evening"));
		    		data.add(value);
		    	}
		    	
		    	Collections.sort(data, new Comparator<ArrayList<String>>()
			  	{

			    					@Override
			    					public int compare(ArrayList<String> lhs, ArrayList<String> rhs) 
			    					{
			    						return Integer.signum(dayint(lhs.get(1))-dayint(rhs.get(1)));
			    					}
			    					
			    					public int dayint(String day)
			    					{
			    						int setday=1;
			    						
			    						if(day.equals("Sunday"))
			    							setday=1;
			    						if(day.equals("Monday"))
			    							setday=2;
			    						if(day.equals("Tuesday"))
			    							setday=3;
			    						if(day.equals("Wednesday"))
			    							setday=4;
			    						if(day.equals("Thursday"))
			    							setday=5;
			    						if(day.equals("Friday"))
			    							setday=6;
			    						if(day.equals("Saturday"))
			    							setday=7;
			    						
			    						return setday;
			    					}
			  });
		    }
		}
		catch(ParseException e)
		{
		  	System.out.println(e.getCode());
		}
		
		return data;
	}

}
