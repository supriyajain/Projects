package Doctor_Available_Appointment;

import com.parse.ParseException;
import com.parse.ParseObject;

public class Doctor_Insert_Updated_Available_Date_Time
{
	ParseObject val=new ParseObject("Doctor_Available_Date_Time");
	
	public void Insert_Date_Time(String email, String date, String day, String shift, String time)
	{
		val.put("ID", email);
		val.put("Date",date);
		val.put("Day", day);
		val.put("Shift", shift);
		val.put("Time", time);
		try 
		{
			val.save();
		}
		catch (ParseException e) 
		{
			System.out.println(e.getCode());
		}
	}
	
}
