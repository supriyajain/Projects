package Patient_Book_Appointment;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Doctor_Fetch_All_Patient_Email_ID 
{
	public ArrayList<String> FetchAllPatientEmail(String docemail)
	{
		final ArrayList<String> data=new ArrayList<String>();
		
		ParseQuery query = new ParseQuery("Book_Appointment");
		query.whereEqualTo("Doc_ID", docemail);
		try
		{
			List<ParseObject> objects=query.find();
			if (objects!= null)
			{
		    	for(ParseObject object: objects)
		    	{
		    		String patemail=object.getString("Pat_ID");
		    		if(!data.contains(patemail))
						data.add(patemail);
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
