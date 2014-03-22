package Patient_Book_Appointment;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Doctor_Verify_Full_Booked_Appointment
{
	int flag=0;
	
	public int FullBookedVerify(String docemail, String date, String shift)
	{
		ParseQuery query = new ParseQuery("Book_Appointment");
		query.whereEqualTo("Doc_ID", docemail);
		query.whereEqualTo("Date", date);
		query.whereEqualTo("Shift", shift);
		try
		{
			ParseObject object=query.getFirst();
			if (object != null) 
		    {
		    	flag=1;		    
		    }
		}
		catch(ParseException e)
		{
			System.out.println(e.getCode());
		}
		
		return flag;
	}
}
