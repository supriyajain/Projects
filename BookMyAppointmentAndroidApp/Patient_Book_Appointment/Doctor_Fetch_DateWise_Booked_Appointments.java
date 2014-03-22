package Patient_Book_Appointment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Doctor_Fetch_DateWise_Booked_Appointments 
{
	public ArrayList<ArrayList<String>> FetchBookedAppointments(String docemail, String date)
	{
		final ArrayList<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		
		ParseQuery query = new ParseQuery("Book_Appointment");
		query.whereEqualTo("Doc_ID", docemail);
		query.whereEqualTo("Date", date);
		try
		{
			List<ParseObject> objects=query.find();
			if (objects!= null) 
		    {
		    	for(ParseObject object: objects)
		    	{
		    		ArrayList<String> value=new ArrayList<String>();
		    		value.add(object.getString("Doc_ID"));
		    		value.add(object.getString("Pat_ID"));
		    		value.add(object.getString("Date"));
		    		value.add(object.getString("Day"));
		    		value.add(object.getString("Shift"));
		    		value.add(object.getString("Time"));
		    		data.add(value);
		    	}
		    	Collections.sort(data, new Comparator<ArrayList<String>>()
		    	{
					@Override
					public int compare(ArrayList<String> lhs, ArrayList<String> rhs) 
					{
						return mycomp(lhs.get(5),rhs.get(5));
					}
					
					public int mycomp(String date1, String date2)
					{
						int j=0;
						int date1hour=Integer.parseInt(date1.substring(0, date1.indexOf(":")));
						int date2hour=Integer.parseInt(date2.substring(0, date2.indexOf(":")));
						int date1mins=Integer.parseInt(date1.substring(date1.indexOf(":")+1,date1.indexOf("-")-1));
						int date2mins=Integer.parseInt(date2.substring(date2.indexOf(":")+1,date2.indexOf("-")-1));
						
						if(date1hour>date2hour)
							j=1;
						else if(date1hour<date2hour)
							j=-1;
						else
						{
							if(date1mins>date2mins)
								j=1;
							else if(date1mins<date2mins)
								j=-1;
							else
								j=0;
						}
						
						return j;
					}
							
		    	});
		    }
		}
		catch(ParseException e)
		{
			System.out.println(e.getCode());
		}
		
		return data;
	}
}
