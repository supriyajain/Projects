package Doctor_Visiting_Day_Time;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import Doctor_Available_Appointment.Doctor_Update_Available_Date_Time;

public class Doctor_Update_Day_Time 
{
	public void Update_Day_Time(String email, String day, final String morn, final String noon, final String even)
	{
		ParseQuery query = new ParseQuery("Doctor_Visiting_Day_Time");
		query.whereEqualTo("ID", email);
		query.whereEqualTo("Day", day);
		try
		{
			ParseObject o=query.getFirst();
			if (o != null) 
		    {
		    	o.put("Morning", morn);
		    	o.put("Afternoon", noon);
		    	o.put("Evening", even);
		    	o.saveInBackground();
		    }
		    
			Doctor_Update_Available_Date_Time duadt=new Doctor_Update_Available_Date_Time();
		    duadt.Update_Day_Time(email, day, morn, noon, even);
		}
		catch(ParseException e)
		{
			System.out.println(e.getCode());
		}
	}
}
