package Doctor_Medicine;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Doctor_Medicine 
{
	
	public void save_med(String email, String med, String comp)
	{
		ParseObject val=new ParseObject("Medicines");
		val.put("ID", email);
		val.put("Medicine", med);
		val.put("Company", comp);
		val.saveEventually();
	}
	
	
	public ArrayList<String[]> fetch_med(String email)
	{
		final ArrayList<String[]> data=new ArrayList<String[]>();
		ParseQuery query = new ParseQuery("Medicines");
		query.whereEqualTo("ID", email);
		try
		{
			List<ParseObject> objects=query.find();
			if (objects!= null) 
		    {
		    	for(ParseObject object: objects)
		    	{
		    		String med[]=new String[2];
					med[0]=object.getString("Medicine");
					med[1]=object.getString("Company");
					data.add(med);
		    	}
		    }
		    
			Collections.sort(data, new Comparator<String[]>()
			{
	
				@Override
				public int compare(String[] lhs, String[] rhs) 
				{
					return lhs[0].compareTo(rhs[0]);
				}
				
			});
		 }
		 catch(ParseException e)
		 {
			 System.out.println(e.getCode());
		 }
		
		 return data;
	}
	
	
	public void del_med(String email, String med)
	{
		ParseQuery query = new ParseQuery("Medicines");
		query.whereEqualTo("ID", email);
		query.whereEqualTo("Medicine",med);
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
	 
	public void edit_med(String email, String oldmed, String oldcomp, final String newmed, final String newcomp)
	{
		ParseQuery query = new ParseQuery("Medicines");
		query.whereEqualTo("ID", email);
		query.whereEqualTo("Medicine",oldmed);
		query.whereEqualTo("Company",oldcomp);
		try
		{
			ParseObject val=query.getFirst();
			if (val != null) 
		    {
		    	val.put("Medicine", newmed);
				val.put("Company", newcomp);
				val.saveInBackground();
		    }
		}
		catch(ParseException e)
		{
		  	System.out.println(e.getCode());
		}
	}
}