package Doctor_Available_Appointment;

import java.util.ArrayList;
import java.util.Calendar;

import com.parse.ParseObject;

public class Doctor_Available_Appoinment_Date_Time 
{
	ParseObject po=new ParseObject("Doctor_Available_Date_Time");
	
	public void save_appoinment_date_time(String email, String day, ArrayList<String> shift, ArrayList<String> time)
	{
		Calendar c=Calendar.getInstance();
		int todaydate=c.get(Calendar.DATE);
		int todaymonth=c.get(Calendar.MONTH)+1;
		int todayyear=c.get(Calendar.YEAR);
		
		int currentday=c.get(Calendar.DAY_OF_WEEK);
		int setday=1;
		
		if(day.equals("Sunday"))
			setday=1;
		if(day.equals("Monday"))
			setday=2;
		if(day.equals("Tuesday"))
			setday=3;
		if(day.equals("Wednesday"))
			setday=4;
		if(day.equals("Thursday"))
			setday=5;
		if(day.equals("Friday"))
			setday=6;
		if(day.equals("Saturday"))
			setday=7;
		
		int numb=0,nextnumb=7;
		if(currentday<setday)
		{
			numb=setday-currentday;
			nextnumb=numb+7;
		}
		else if(currentday>setday)
		{
			numb=7-(currentday-setday);
			nextnumb=0;
		}
		
		int set_date=00,set_month=00,set_year=00;
		
		if(numb>0)
		{
			if(todaymonth==01 || todaymonth==03 || todaymonth==05 || todaymonth==07 || todaymonth==Integer.parseInt("08") || todaymonth==10 || todaymonth==12)
			{
				if((todaydate+numb)<=31)
				{
					set_date=todaydate+numb;
					set_month=todaymonth;
					set_year=todayyear;
				}
				else
				{
					set_date=(todaydate+numb)-31;
					if(todaymonth<=10)
					{
						set_month=todaymonth+1;
						set_year=todayyear;
					}
					else
					{
						set_month=01;
						set_year=todayyear+1;
					}
				}
			}
			else if(todaymonth==04 || todaymonth==06 || todaymonth==Integer.parseInt("09") || todaymonth==11)
			{
				if((todaydate+numb)<=30)
				{
					set_date=todaydate+numb;
					set_month=todaymonth;
					set_year=todayyear;
				}
				else
				{
					set_date=(todaydate+numb)-30;
					set_month=todaymonth+1;
					set_year=todayyear;
					
				}
			}
			else if(todaymonth==02)
			{
				if(todayyear%4==0)
				{
					if((todaydate+numb)<=29)
					{
						set_date=todaydate+numb;
						set_month=todaymonth;
						set_year=todayyear;
					}
					else
					{
						set_date=(todaydate+numb)-29;
						set_month=todaymonth+1;
						set_year=todayyear;
						
					}
				}
				else
				{
					if((todaydate+numb)<=28)
					{
						set_date=todaydate+numb;
						set_month=todaymonth;
						set_year=todayyear;
					}
					else
					{
						set_date=(todaydate+numb)-28;
						set_month=todaymonth+1;
						set_year=todayyear;
						
					}
				}
			}
			
			String set_app_date=set_date+"/"+set_month+"/"+set_year;
			for(int i=0;i<shift.size();i++)
			{
				po.put("ID", email);
				po.put("Date", set_app_date);
				po.put("Day", day);
				po.put("Shift", shift.get(i));
				po.put("Time", time.get(i));
				po.saveInBackground();
			}
		}
		
		if(nextnumb>0)
		{
			if(todaymonth==01 || todaymonth==03 || todaymonth==05 || todaymonth==07 || todaymonth==Integer.parseInt("08") || todaymonth==10 || todaymonth==12)
			{
				if((todaydate+nextnumb)<=31)
				{
					set_date=todaydate+nextnumb;
					set_month=todaymonth;
					set_year=todayyear;
				}
				else
				{
					set_date=(todaydate+nextnumb)-31;
					if(todaymonth<=10)
					{
						set_month=todaymonth+1;
						set_year=todayyear;
					}
					else
					{
						set_month=01;
						set_year=todayyear+1;
					}
				}
			}
			else if(todaymonth==04 || todaymonth==06 || todaymonth==Integer.parseInt("09") || todaymonth==11)
			{
				if((todaydate+nextnumb)<=30)
				{
					set_date=todaydate+nextnumb;
					set_month=todaymonth;
					set_year=todayyear;
				}
				else
				{
					set_date=(todaydate+nextnumb)-30;
					set_month=todaymonth+1;
					set_year=todayyear;
					
				}
			}
			else if(todaymonth==02)
			{
				if(todayyear%4==0)
				{
					if((todaydate+nextnumb)<=29)
					{
						set_date=todaydate+nextnumb;
						set_month=todaymonth;
						set_year=todayyear;
					}
					else
					{
						set_date=(todaydate+nextnumb)-29;
						set_month=todaymonth+1;
						set_year=todayyear;
						
					}
				}
				else
				{
					if((todaydate+nextnumb)<=28)
					{
						set_date=todaydate+nextnumb;
						set_month=todaymonth;
						set_year=todayyear;
					}
					else
					{
						set_date=(todaydate+nextnumb)-28;
						set_month=todaymonth+1;
						set_year=todayyear;
						
					}
				}
			}
			
			String set_app_date=set_date+"/"+set_month+"/"+set_year;
			for(int i=0;i<shift.size();i++)
			{
				po.put("ID", email);
				po.put("Date", set_app_date);
				po.put("Day", day);
				po.put("Shift", shift.get(i));
				po.put("Time", time.get(i));
				po.saveInBackground();
			}
		}
	}
	
}
