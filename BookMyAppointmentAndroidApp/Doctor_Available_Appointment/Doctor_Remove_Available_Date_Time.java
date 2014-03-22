package Doctor_Available_Appointment;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Doctor_Remove_Available_Date_Time 
{
	public void Remove_Available_Day_Time(String email, String date, String shift, String time)
	{
		ParseQuery query = new ParseQuery("Doctor_Available_Date_Time");
		query.whereEqualTo("ID", email);
		query.whereEqualTo("Date", date);
		query.whereEqualTo("Shift", shift);
		query.whereEqualTo("Time", time);
		try
		{
			ParseObject object=query.getFirst();
			if (object != null) 
		    {
		    	object.delete();
		    }
		}
		catch(ParseException e)
		{
		  	System.out.println(e.getCode());
		}
	}
}
