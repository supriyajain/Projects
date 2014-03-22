package Patient_Book_Appointment;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Patient_Delete_Booked_Appointment 
{
	public void Delete_Booked_Appointment(String docemail, String patemail, String date, String time)
	{
		ParseQuery query = new ParseQuery("Book_Appointment");
		query.whereEqualTo("Doc_ID", docemail);
		query.whereEqualTo("Pat_ID", patemail);
		query.whereEqualTo("Date", date);
		query.whereEqualTo("Time", time);
		try
		{
			ParseObject val=query.getFirst();
			if (val != null) 
		    {
		    	val.deleteInBackground();
		    }
		}
		 catch(ParseException e)
		 {
		   	System.out.println(e.getCode());
		 }
	}
}
