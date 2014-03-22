package Doctor_Available_Appointment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Messages.Messages;
import Patient_Book_Appointment.Doctor_Fetch_All_Booked_Appointments;
import Patient_Book_Appointment.Patient_Delete_Booked_Appointment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

public class Doctor_Delete_Available_Date_Time 
{
	
	String docname="";
	
	public void Delete_Available_Day_Time(final String email, final String day, String shift)
	{
		ParseQuery query = new ParseQuery("Doctor_Available_Date_Time");
		query.whereEqualTo("ID", email);
		query.whereEqualTo("Day", day);
		query.whereEqualTo("Shift", shift);
		query.findInBackground(new FindCallback() 
		{
		  public void done(List<ParseObject> objects, ParseException e)
		  {
		    if (e== null) 
		    {
		    	for(ParseObject object: objects)
		    	{
		    		object.deleteInBackground();
		    	}
		    	
		    	Calendar cal=Calendar.getInstance();
				int date=cal.get(Calendar.DATE);
				int month=cal.get(Calendar.MONTH)+1;
				int year=cal.get(Calendar.YEAR);
				String idate=date+"/"+month+"/"+year;
				
				int hour=cal.get(Calendar.HOUR);
				int min=cal.get(Calendar.MINUTE);
				int shift=cal.get(Calendar.AM_PM);
				String sh="";
				if(shift==1)
					sh="PM";
				else
					sh="AM";
				String smin=min+"";
				if(min<10)
					smin="0"+min;
				String time=hour+":"+smin+" "+sh;
				if(hour<10)
					time="0"+time;
				
				
				Doctor_Fetch_All_Booked_Appointments pfaba=new Doctor_Fetch_All_Booked_Appointments();
				ArrayList<ArrayList<String>> bookeddata=pfaba.FetchAllBookedAppointments(email);
				
				ParseQuery q = new ParseQuery("Doctor");
				q.whereEqualTo("ID", email);
				try
				{
					ParseObject po=q.getFirst();
					if (po!= null) 
					{
					   	docname="Dr. "+po.getString("First_name")+" "+po.getString("Last_name");
					}
				
					for(ArrayList<String> s: bookeddata)
					{
						if(s.get(3).equals(day))
						{
							String temp=s.get(2);
							int daydate=Integer.parseInt(temp.substring(0, temp.indexOf("/")));
							int daymonth=Integer.parseInt(temp.substring(temp.indexOf("/")+1,temp.lastIndexOf("/")));
							int dayyear=Integer.parseInt(temp.substring(temp.lastIndexOf("/")+1));
							
							if(daydate>=date && daymonth>=month && dayyear>=year)
							{
								Patient_Delete_Booked_Appointment pdba=new Patient_Delete_Booked_Appointment();
								pdba.Delete_Booked_Appointment(email, s.get(1), s.get(2), s.get(5));
								
								String msg="Your appointment with "+docname+" on "+s.get(2)+" ("+s.get(3)+") at "+s.get(5)+" has been cancelled because of doctor's unavailability. Please book appointment for next available date. Thank You !";
								
								Messages m=new Messages();
								m.save_Message(email, s.get(1), idate, time, msg);
								
								ParseQuery pushQuery = ParseInstallation.getQuery();
								pushQuery.whereEqualTo("ID", s.get(1));
								 
								ParsePush push = new ParsePush();
								push.setQuery(pushQuery);
								push.setMessage("You have new message");
								push.sendInBackground();
							}
						}
					}
				}
				catch(ParseException e1)
				{
					System.out.println(e1.getCode());
				}
			}
			else
				System.out.println(e.getCode());
			 }
		});
	}
}
