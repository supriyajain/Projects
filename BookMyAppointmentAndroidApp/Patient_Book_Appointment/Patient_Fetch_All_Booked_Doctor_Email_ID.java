package Patient_Book_Appointment;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Patient_Fetch_All_Booked_Doctor_Email_ID 
{
	public ArrayList<String> FetchAllDoctorEmail(String patemail)
	{
		final ArrayList<String> data=new ArrayList<String>();
		
		ParseQuery query = new ParseQuery("Book_Appointment");
		query.whereEqualTo("Pat_ID", patemail);
		try
		{
			List<ParseObject> objects=query.find();
			if (objects!= null) 
		    {
		    	for(ParseObject object: objects)
		    	{
		    		String docemail=object.getString("Doc_ID");
		    		if(!data.contains(docemail))
						data.add(docemail);
		    	}
		    }
		}
		catch(ParseException e)
		{
		   	System.out.println(e.getCode());
		}
		
		return data;
	}
}
