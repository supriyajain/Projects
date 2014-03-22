package Doctor_Visiting_Day_Time;

import java.util.ArrayList;

import com.parse.ParseObject;

import Doctor_Available_Appointment.Doctor_Available_Appoinment_Date_Time;

public class Doctor_Save_Day_time
{
	ParseObject po=new ParseObject("Doctor_Visiting_Day_Time");
	
	public void save_day_time(String email, String day, String morn, String noon, String even)
	{
		po.put("ID", email);
		po.put("Day", day);
		po.put("Morning", morn);
		po.put("Afternoon", noon);
		po.put("Evening", even);
		po.saveInBackground();
		
		Doctor_Available_Appoinment_Date_Time daadt=new Doctor_Available_Appoinment_Date_Time();
		ArrayList<String> shift=new ArrayList<String>();
		ArrayList<String> time=new ArrayList<String>();
		
		if(!morn.equals("None"))
		{
			shift.add("Morning");
			time.add(morn);
		}
		if(!noon.equals("None"))
		{
			shift.add("Afternoon");
			time.add(noon);
		}
		if(!even.equals("None"))
		{
			shift.add("Evening");
			time.add(even);
		}
		if(shift.size()>0)
			daadt.save_appoinment_date_time(email, day, shift, time);
	}
}
