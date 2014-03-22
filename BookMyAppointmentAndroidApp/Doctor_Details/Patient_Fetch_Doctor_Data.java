package Doctor_Details;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Patient_Fetch_Doctor_Data 
{
	public ArrayList<ArrayList<String>> Fetch_Doctor_Data()
	{
		final ArrayList<ArrayList<String>> values=new ArrayList<ArrayList<String>>();
		ParseQuery pq=new ParseQuery("Doctor");
		try 
		{
			List<ParseObject> ob= pq.find();
			for(ParseObject o:ob)
			{
				ArrayList<String> val=new ArrayList<String>();
				val.add(o.getString("First_name"));//0
				val.add(o.getString("Last_name"));//1
				val.add(o.getString("Gender"));//2
				val.add(o.getNumber("Age")+"");//3
				val.add(o.getString("Address"));//4
				val.add(o.getString("Mobile"));//5
				val.add(o.getString("Email"));//6
				val.add(o.getString("Specialization"));//7
				val.add(o.getNumber("Time_interval")+"");//8
				values.add(val);
			}
		}
		catch (ParseException e) 
		{
			System.out.println(e.getCode());
		}
		
		return values;
	}
}
