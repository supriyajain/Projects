package Patient_Book_Appointment;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Patient_Verify_Booked_Appointment
{
	int flag=0;
	
	public int BookedVerify(String docemail, String patemail, String date, String shift)
	{
		
		ParseQuery query = new ParseQuery("Book_Appointment");
		query.whereEqualTo("Doc_ID", docemail);
		query.whereEqualTo("Pat_ID", patemail);
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
