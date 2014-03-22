package Patient_Book_Appointment;

import com.parse.ParseObject;

public class Patient_Book_Appointment 
{
	public void Insert_Date_Time(String docemail, String patemail, String date, String day, String shift, String time)
	{
		ParseObject val=new ParseObject("Book_Appointment");
		val.put("Doc_ID", docemail);
		val.put("Pat_ID", patemail);
		val.put("Date", date);
		val.put("Day", day);
		val.put("Shift", shift);
		val.put("Time", time);
		val.saveInBackground();
	}
}
