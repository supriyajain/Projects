package com.example.patient_module;

import java.util.ArrayList;
import java.util.Calendar;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import Patient_Book_Appointment.Doctor_Fetch_All_Booked_Appointments;
import Patient_Book_Appointment.Doctor_Fetch_DateWise_Booked_Appointments;
import Patient_Book_Appointment.Patient_Delete_Booked_Appointment;
import Patient_Prescription.Prescriptions;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Doctor_View_Appointments extends Activity 
{
	
	String docemail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_doctor__view__appointments);
			
			Intent i=getIntent();
			docemail=i.getStringExtra("docemail");
			
			Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
		
			Calendar c=Calendar.getInstance();
			int date=c.get(Calendar.DATE);
			int month=c.get(Calendar.MONTH)+1;
			int year=c.get(Calendar.YEAR);
			final String idate=date+"/"+month+"/"+year;
			
			Doctor_Fetch_All_Booked_Appointments dftba=new Doctor_Fetch_All_Booked_Appointments();
			final ArrayList<ArrayList<String>> appdetailslist=dftba.FetchAllBookedAppointments(docemail);
			
			final ArrayList<String> app=new ArrayList<String>();
			app.add("---> Go to Home");
			app.add("---> View appointments for some other date");
			
			for(ArrayList<String> a: appdetailslist)
			{
				String adate=a.get(2);
				if(idate.equals(adate))
				{
					ParseQuery q=new ParseQuery("Patient");
					q.whereEqualTo("Email", a.get(1));
					try
					{
						ParseObject o=q.getFirst();
						if(o!=null)
						{
							String patname=o.getString("First_name")+" "+o.getString("Last_name");
					
							String appdetails=a.get(4)+"  "+a.get(5)+"  "+patname;
							app.add(appdetails);
						}
					}
					catch(ParseException e)
					{
						System.out.println(e.getCode());
					}
				}
			}
			
			if(app.size()==2)
			{
				AlertDialog.Builder a1=new AlertDialog.Builder(Doctor_View_Appointments.this);
				a1.setTitle("No Appointments");
				a1.setMessage("There are no appointments for today !");
				a1.setNegativeButton("OK", null);
				a1.setPositiveButton("Home", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						Intent ihome=new Intent(Doctor_View_Appointments.this,Doctor_Home.class);
						startActivity(ihome);
					}
				});
				a1.show();
			}
			
			final ListView l=(ListView)findViewById(R.id.DocViewAppList);
			ArrayAdapter<String> ad=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,app);
			l.setAdapter(ad);
			l.setOnItemClickListener(new OnItemClickListener()
			{
	
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) 
				{
					final int selected=arg2;
					
					if(selected==0)
					{
						Intent ihome=new Intent(Doctor_View_Appointments.this,Doctor_Home.class);
						startActivity(ihome);
					}
					else if(selected==1)
					{
						ArrayList<String> dates=new ArrayList<String>();
						for(ArrayList<String> s: appdetailslist)
						{
							String date=s.get(2);
							if(!(date.equals(idate)) && !(dates.contains(date)))
							{
								dates.add(date);
							}
						}
						
						final String datelist[]=new String[dates.size()];
						for(int i=0;i<dates.size();i++)
						{
							datelist[i]=dates.get(i);
						}
						
						if(dates.size()<1)
						{
							AlertDialog.Builder ad=new AlertDialog.Builder(Doctor_View_Appointments.this);
							ad.setTitle("No Appointments");
							ad.setMessage("There is no appointment for any other date !");
							ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
							{
								
								@Override
								public void onClick(DialogInterface dialog, int which) 
								{
									Intent ihome=new Intent(Doctor_View_Appointments.this,Doctor_Home.class);
									startActivity(ihome);
								}
							});
							ad.show();
						}
						else
						{
							AlertDialog.Builder adb=new AlertDialog.Builder(Doctor_View_Appointments.this);
							adb.setTitle("Select a Date");
							adb.setItems(datelist, new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which) 
								{
									final int select=which;
									String selecteddate=datelist[select];
								
									Doctor_Fetch_DateWise_Booked_Appointments dfdba=new Doctor_Fetch_DateWise_Booked_Appointments();
									final ArrayList<ArrayList<String>> patlist=dfdba.FetchBookedAppointments(docemail, selecteddate);
								
									ArrayList<String> plist=new ArrayList<String>();
								
									for(ArrayList<String> l: patlist)
									{
										ParseQuery q=new ParseQuery("Patient");
										q.whereEqualTo("Email", l.get(1));
										try
										{
											ParseObject o=q.getFirst();
											if(o!=null)
											{
												String patname=o.getString("First_name")+" "+o.getString("Last_name");
										
												String appdetails=l.get(4)+"  "+l.get(5)+"  "+patname;
												plist.add(appdetails);
											}
										}
										catch(ParseException e)
										{
											System.out.println(e.getCode());
										}
									}
								
									final String palist[]=new String[plist.size()];
									for(int i=0;i<plist.size();i++)
									{
										palist[i]=plist.get(i);
									}
								
									AlertDialog.Builder adb1=new AlertDialog.Builder(Doctor_View_Appointments.this);
									adb1.setTitle("Total "+palist.length+" Appointment(s)");
									adb1.setItems(palist, new DialogInterface.OnClickListener()
									{
										@Override
										public void onClick(DialogInterface dialog, int which) 
										{
											Prescriptions pre=new Prescriptions();
											final ArrayList<ArrayList<String>> applist=pre.Doctor_Fetch_History(docemail, patlist.get(select).get(1));
										
											ArrayList<String> app=new ArrayList<String>();
										
											for(ArrayList<String> a: applist)
											{
												String appdetails=a.get(2)+"   "+a.get(3)+"  Disease: "+a.get(4)+"\nPrescription: "+a.get(5);
												app.add(appdetails);
											}
										
											String arr[]=new String[app.size()];
											for(int i=0;i<app.size();i++)
											{
												arr[i]=app.get(i);
											}
										
											if(app.size()>1)
											{
												AlertDialog.Builder adb2=new AlertDialog.Builder(Doctor_View_Appointments.this);
												adb2.setTitle("Patient History");
												adb2.setItems(arr, null);
												adb2.setPositiveButton("OK", null);
												adb2.show();
											}
											else
											{
												AlertDialog.Builder adb3=new AlertDialog.Builder(Doctor_View_Appointments.this);
												adb3.setTitle("No History Found");
												adb3.setMessage("There is no previous record for this patient !");
												adb3.setPositiveButton("OK", null);
												adb3.show();
											}
										}
									});
									adb1.setPositiveButton("Ok", new DialogInterface.OnClickListener()
									{
									
										@Override
										public void onClick(DialogInterface dialog, int which) 
										{
											Intent ihome=new Intent(Doctor_View_Appointments.this,Doctor_Home.class);
											startActivity(ihome);
										}
									});
									adb1.show();
								}
							});
							adb.setNegativeButton("Cancel", null);
							adb.show();
						}
					}
					else
					{
						String options[]={"View History","Add Prescription","Remove this patient from list"};
						
						AlertDialog.Builder ad=new AlertDialog.Builder(Doctor_View_Appointments.this);
						ad.setTitle("Options");
						ad.setItems(options, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								if(which==0)
								{
									Intent ihistory=new Intent(Doctor_View_Appointments.this,Doctor_View_History.class);
									ihistory.putExtra("Doc_ID", docemail);
									ihistory.putExtra("Pat_ID", appdetailslist.get(selected-2).get(1));
									ihistory.putExtra("Time", appdetailslist.get(selected-2).get(5));
									startActivity(ihistory);
								}
								else if(which==1)
								{
									appdetailslist.get(selected-2).add("Doctor_View_Appointments");
									Intent ip=new Intent(Doctor_View_Appointments.this,Doctor_Add_Prescription.class);
									ip.putStringArrayListExtra("Values", appdetailslist.get(selected-2));
									startActivity(ip);
								}
								else
								{
									AlertDialog.Builder adb=new AlertDialog.Builder(Doctor_View_Appointments.this);
									adb.setTitle("Confirm Remove");
									adb.setMessage("Remove this patient from today's appointment list ?");
									adb.setPositiveButton("Yes", new DialogInterface.OnClickListener()
									{
										
										@Override
										public void onClick(DialogInterface dialog, int which) 
										{
											Patient_Delete_Booked_Appointment pdba=new Patient_Delete_Booked_Appointment();
											pdba.Delete_Booked_Appointment(docemail, appdetailslist.get(selected-2).get(1), appdetailslist.get(selected-2).get(2), appdetailslist.get(selected-2).get(5));
											
											app.remove(selected);
											appdetailslist.remove(selected-2);
											ArrayAdapter<String> ads=new ArrayAdapter<String>(Doctor_View_Appointments.this, android.R.layout.simple_list_item_1,app);
											l.setAdapter(ads);
										}
									});
									adb.setNegativeButton("No", null);
									adb.show();
								}
							}
						});
						ad.setNegativeButton("Cancel", null);
						ad.show();
					}
				}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_doctor__view__appointments,
				menu);
		return true;
	}
	
	@Override
	public void onBackPressed() 
	{
	    return;
	}
}
