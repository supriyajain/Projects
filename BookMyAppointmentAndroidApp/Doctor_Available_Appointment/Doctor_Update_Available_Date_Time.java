package Doctor_Available_Appointment;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Doctor_Update_Available_Date_Time
{
	public void Update_Day_Time(String email, String day, final String morn, final String noon, final String even)
	{
		
		final ArrayList<String> shift=new ArrayList<String>();
		final ArrayList<String> time=new ArrayList<String>();
		
		if(!morn.equals("None"))
		{
			ParseQuery query = new ParseQuery("Doctor_Available_Date_Time");
			query.whereEqualTo("ID", email);
			query.whereEqualTo("Day", day);
			query.whereEqualTo("Shift", shift);
			try
			{
				List<ParseObject> objects=query.find();
				if (objects!= null) 
			    {
			    	for(ParseObject object: objects)
			    	{	
			    		object.put("Time", morn);
			    		object.saveInBackground();
			    	}
			    }
			    else
			    {
			    	shift.add("Morning");
			    	time.add(morn);
			    }
			}
			catch(ParseException e)
			{
				System.out.println(e.getCode());
			}
		}
		else
		{
			Doctor_Delete_Available_Date_Time ddadt=new Doctor_Delete_Available_Date_Time();
			ddadt.Delete_Available_Day_Time(email, day, "Morning");
			
		}
		
		if(!noon.equals("None"))
		{
			ParseQuery query = new ParseQuery("Doctor_Available_Date_Time");
			query.whereEqualTo("ID", email);
			query.whereEqualTo("Day", day);
			query.whereEqualTo("Shift", shift);
			try
			{
				List<ParseObject> objects=query.find();
				if (objects!= null) 
			    {
			    	for(ParseObject object: objects)
			    	{	
			    		object.put("Time", noon);
			    		object.saveInBackground();
			    	}
			    }
			    else
			    {
			    	shift.add("Afetrnoon");
			    	time.add(noon);
			    }
			}
			catch(ParseException e)
			{
				System.out.println(e.getCode());
			}
		}
		else
		{
			Doctor_Delete_Available_Date_Time ddadt=new Doctor_Delete_Available_Date_Time();
			ddadt.Delete_Available_Day_Time(email, day, "Afternoon");
		}
		
		if(!even.equals("None"))
		{
			ParseQuery query = new ParseQuery("Doctor_Available_Date_Time");
			query.whereEqualTo("ID", email);
			query.whereEqualTo("Day", day);
			query.whereEqualTo("Shift", shift);
			try
			{
				List<ParseObject> objects=query.find();
				if (objects!= null) 
			    {
			    	for(ParseObject object: objects)
			    	{	
			    		object.put("Time", even);
			    		object.saveInBackground();
			    	}
			    }
			    else
			    {
			    	shift.add("Evening");
			    	time.add(even);
			    }
			}
			catch(ParseException e)
			{
				System.out.println(e.getCode());
			}
		}
		else
		{
			Doctor_Delete_Available_Date_Time ddadt=new Doctor_Delete_Available_Date_Time();
			ddadt.Delete_Available_Day_Time(email, day, "Evening");
		}
		
		
		if(shift.size()>0)
		{
			Doctor_Available_Appoinment_Date_Time daadt=new Doctor_Available_Appoinment_Date_Time();
			daadt.save_appoinment_date_time(email, day, shift, time);
		}
	}
}
