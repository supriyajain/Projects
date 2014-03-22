package Patient_Prescription;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Prescriptions 
{
	
	int flag=0;
	
	public void save_pres(String docemail, String patemail, String date, String time, String prob, String presc)
	{
		ParseObject val=new ParseObject("Prescriptions");
		val.put("Doc_ID", docemail);
		val.put("Pat_ID", patemail);
		val.put("Date", date);
		val.put("Time", time);
		val.put("Problem", prob);
		val.put("Prescription", presc);
		val.saveEventually();
	}
	
	
	public ArrayList<ArrayList<String>> Patient_Fetch_History(String patemail)
	{
		final ArrayList<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		
		ParseQuery query = new ParseQuery("Prescriptions");
		query.whereEqualTo("Pat_ID", patemail);
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
		    		value.add(object.getString("Time"));
		    		value.add(object.getString("Problem"));
		    		value.add(object.getString("Prescription"));
					data.add(value);
		    	}
		    
			    Collections.sort(data, new Comparator<ArrayList<String>>()
			    {
					@Override
					public int compare(ArrayList<String> lhs, ArrayList<String> rhs) 
					{
						Date ld=new Date(lhs.get(2));
						Date rd=new Date(rhs.get(2));
						int i=Integer.signum(-(ld.compareTo(rd)));
										
						if(i==0)
						{
							i=mycomp(lhs.get(3),rhs.get(3));
						}
										
						return i;
					}
									
					public int mycomp(String date1, String date2)
					{
						int j=0;
						int date1hour=Integer.parseInt(date1.substring(0, date1.indexOf(":")));
						int date2hour=Integer.parseInt(date2.substring(0, date2.indexOf(":")));
						int date1mins=Integer.parseInt(date1.substring(date1.indexOf(":")+1,date1.indexOf("-")-1));
						int date2mins=Integer.parseInt(date2.substring(date2.indexOf(":")+1,date2.indexOf("-")-1));
										
						if(date1hour>date2hour)
							j=-1;
						else if(date1hour<date2hour)
							j=1;
						else
						{
							if(date1mins>date2mins)
								j=-1;
							else if(date1mins<date2mins)
								j=1;
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
	
	public ArrayList<ArrayList<String>> Doctor_Fetch_History(String docemail, String patemail)
	{
		final ArrayList<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		
		ParseQuery query = new ParseQuery("Prescriptions");
		query.whereEqualTo("Doc_ID", docemail);
		query.whereEqualTo("Pat_ID", patemail);
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
		    		value.add(object.getString("Time"));
		    		value.add(object.getString("Problem"));
		    		value.add(object.getString("Prescription"));
					data.add(value);
		    	}
		    	Collections.sort(data, new Comparator<ArrayList<String>>()
				{
					@Override
					public int compare(ArrayList<String> lhs, ArrayList<String> rhs) 
					{
						Date ld=new Date(lhs.get(2));
						Date rd=new Date(rhs.get(2));
						int i=Integer.signum(-(ld.compareTo(rd)));
										
						if(i==0)
						{
							i=mycomp(lhs.get(3),rhs.get(3));
						}
										
						return i;
					}
									
					public int mycomp(String date1, String date2)
					{
						int j=0;
						int date1hour=Integer.parseInt(date1.substring(0, date1.indexOf(":")));
						int date2hour=Integer.parseInt(date2.substring(0, date2.indexOf(":")));
						int date1mins=Integer.parseInt(date1.substring(date1.indexOf(":")+1,date1.indexOf("-")-1));
						int date2mins=Integer.parseInt(date2.substring(date2.indexOf(":")+1,date2.indexOf("-")-1));
										
						if(date1hour>date2hour)
							j=-1;
						else if(date1hour<date2hour)
							j=1;
						else
						{
							if(date1mins>date2mins)
								j=-1;
							else if(date1mins<date2mins)
								j=1;
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
	
	
	public ArrayList<String> Fetch_History_By_Disease(String docemail, String disease)
	{
		final ArrayList<String> data=new ArrayList<String>();
		
		ParseQuery query = new ParseQuery("Prescriptions");
		query.whereEqualTo("Doc_ID", docemail);
		query.whereEqualTo("Problem", disease);
		try
		{
			List<ParseObject> objects=query.find();
			if (objects!= null) 
		    {
		    	for(ParseObject object: objects)
		    	{
		    		String patemail=object.getString("Pat_ID");
		    		if(!data.contains(patemail))
					{
						data.add(patemail);
					}
		    	}
		    }
		}
		catch(ParseException e)
		{
		 	System.out.println(e.getCode());
		}
		  
		return data;
	}
	
	
	public int Verify_Patient(String docemail, String patemail)
	{
		ParseQuery query = new ParseQuery("Prescriptions");
		query.whereEqualTo("Doc_ID", docemail);
		query.whereEqualTo("Pat_ID", patemail);
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
