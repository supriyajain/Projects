package Doctor_Visiting_Day_Time;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import Doctor_Available_Appointment.Doctor_Delete_Available_Date_Time;

public class Doctor_Delete_Day_Time 
{
	public void Delete_Day_Time(String email, String day)
	{
		ParseQuery query = new ParseQuery("Doctor_Visiting_Day_Time");
		query.whereEqualTo("ID", email);
		query.whereEqualTo("Day", day);
		try
		{
			ParseObject object=query.getFirst();
			if (object != null) 
		    {
		    	object.delete();
			    Doctor_Delete_Available_Date_Time ddadt=new Doctor_Delete_Available_Date_Time();
				ddadt.Delete_Available_Day_Time(email, day, "Morning");
				ddadt.Delete_Available_Day_Time(email, day, "Afternoon");
				ddadt.Delete_Available_Day_Time(email, day, "Evening");
		    }
		}
		catch(ParseException e)
		{
		System.out.println(e.getCode());
		}
	}
}
