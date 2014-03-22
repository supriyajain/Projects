package Doctor_Available_Appointment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Doctor_Fetch_Available_Appoinment_Date_Time 
{
	public ArrayList<String> Fetch_Date_Time(String email, String date, String shift)
	{
		final ArrayList<String> alist=new ArrayList<String>();
		
		ParseQuery query = new ParseQuery("Doctor_Available_Date_Time");
		query.whereEqualTo("ID", email);
		query.whereEqualTo("Date", date);
		query.whereEqualTo("Shift", shift);
		try
		{
			List<ParseObject> objects=query.find();
		    if (objects!= null) 
		    {
		    	for(ParseObject object: objects)
		    	{
		    		alist.add(object.getString("Time"));
		    	}
		    }
		    
			Collections.sort(alist, new Comparator<String>()
			{
				@Override
				public int compare(String lhs, String rhs) 
				{
					int j=0;
					int hour1=Integer.parseInt(lhs.substring(0, lhs.indexOf(":")));
					int hour2=Integer.parseInt(rhs.substring(0, rhs.indexOf(":")));
					int mins1=Integer.parseInt(lhs.substring(lhs.indexOf(":")+1,lhs.indexOf("-")-1));
					int mins2=Integer.parseInt(rhs.substring(rhs.indexOf(":")+1,rhs.indexOf("-")-1));
									
					if(hour1>hour2)
						j=1;
					else if(hour1<hour2)
						j=-1;
					else
					{
						if(mins1>mins2)
							j=1;
						else if(mins1<mins2)
							j=-1;
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
		
		return alist;
	}
}
