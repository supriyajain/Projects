package com.example.patient_module;

import java.util.ArrayList;
import java.util.Calendar;
import com.example.patient_module.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import Doctor_Available_Appointment.Doctor_Insert_Updated_Available_Date_Time;
import Patient_Book_Appointment.Patient_Delete_Booked_Appointment;
import Patient_Book_Appointment.Patient_Fetch_All_Booked_Appointments;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class View_Appointments extends Activity 
{
	
	String patemail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_view__appointments);
			
			Intent i=getIntent();
			patemail=i.getStringExtra("patemail");
			
			Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
		
			Patient_Fetch_All_Booked_Appointments pfaba=new Patient_Fetch_All_Booked_Appointments();
			final ArrayList<ArrayList<String>> appdetailslist=pfaba.FetchAllBookedAppointments(patemail);
			
			if(appdetailslist.size()<1)
			{
				AlertDialog.Builder adb0=new AlertDialog.Builder(View_Appointments.this);
				adb0.setTitle("No records");
				adb0.setMessage("You have not booked any appointment yet !");
				adb0.setPositiveButton("OK", new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						Intent ihome=new Intent(View_Appointments.this,Patient_Home.class);
						startActivity(ihome);
					}
				});
				adb0.show();
			}
			else
			{
				Calendar c=Calendar.getInstance();
				int todaydate=c.get(Calendar.DATE);
				int todaymonth=c.get(Calendar.MONTH)+1;
				int todayyear=c.get(Calendar.YEAR);
				
				ArrayList<String> app=new ArrayList<String>();
				
				for(ArrayList<String> a: appdetailslist)
				{
					String adate=a.get(2);
					int appdate=Integer.parseInt(adate.substring(0, adate.indexOf("/")));
					int appmonth=Integer.parseInt(adate.substring(adate.indexOf("/")+1,adate.lastIndexOf("/")));
					int appyear=Integer.parseInt(adate.substring(adate.lastIndexOf("/")+1));
					
					if(appdate>=todaydate && appmonth>=todaymonth && appyear>=todayyear)
					{
						ParseQuery q=new ParseQuery("Doctor");
						q.whereEqualTo("Email", a.get(0));
						try
						{
							ParseObject o=q.getFirst();
							if(o!=null)
							{
								String docname="Dr. "+o.getString("First_name")+" "+o.getString("Last_name");
								
								String appdetails=docname+"\nDate: "+a.get(2)+" ("+a.get(3)+")\nTime: "+a.get(4)+" "+a.get(5);
								app.add(appdetails);
							}
						}
						catch(ParseException e)
						{
							System.out.println(e.getCode());
						}
					}
					else
					{
						Patient_Delete_Booked_Appointment pdba=new Patient_Delete_Booked_Appointment();
						pdba.Delete_Booked_Appointment(a.get(0), a.get(1), a.get(2), a.get(5));
					}
				}
				
				ListView l=(ListView)findViewById(R.id.PatientViewAppList);
				ArrayAdapter<String> ad=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,app);
				l.setAdapter(ad);
				
				l.setOnItemClickListener(new OnItemClickListener()
				{
		
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) 
					{
						final int selected=arg2;
						AlertDialog.Builder ad=new AlertDialog.Builder(View_Appointments.this);
						ad.setTitle("Cancel Appointment");
						ad.setMessage("Do you want to 'Cancel' this appointment ?!");
						ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								ArrayList<String> d=appdetailslist.get(selected);
								
								Patient_Delete_Booked_Appointment pdba=new Patient_Delete_Booked_Appointment();
								pdba.Delete_Booked_Appointment(d.get(0), d.get(1), d.get(2), d.get(5));
								
								Doctor_Insert_Updated_Available_Date_Time diuadt=new Doctor_Insert_Updated_Available_Date_Time();
								diuadt.Insert_Date_Time(d.get(0), d.get(2), d.get(3), d.get(4), d.get(5));
								
								Toast.makeText(View_Appointments.this, "Appointment Cancelled !", 0).show();
								
								Intent ihome=new Intent(View_Appointments.this,Patient_Home.class);
								startActivity(ihome);
							}
						});
						ad.setNegativeButton("No", new DialogInterface.OnClickListener()
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								Intent ihome2=new Intent(View_Appointments.this,Patient_Home.class);
								startActivity(ihome2);
							}
						});
						ad.show();
					}
				});
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_view__appointments, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() 
	{
		Intent ihome2=new Intent(View_Appointments.this,Patient_Home.class);
		startActivity(ihome2);
		return;
	}
}
