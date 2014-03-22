package com.example.patient_module;

import java.util.ArrayList;
import java.util.Calendar;
import Patient_Prescription.Prescriptions;
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

public class Doctor_View_History extends Activity 
{

	Intent i;
	String docemail,patemail,time;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor__view__history);
		
		i=getIntent();
		docemail=i.getStringExtra("Doc_ID");
		patemail=i.getStringExtra("Pat_ID");
		time=i.getStringExtra("Time");
		
		final ListView l=(ListView)findViewById(R.id.ViewHistoryList);
		
		Prescriptions pre=new Prescriptions();
		final ArrayList<ArrayList<String>> applist=pre.Doctor_Fetch_History(docemail, patemail);
		
		if(applist.size()<1)
		{
			AlertDialog.Builder abd=new AlertDialog.Builder(Doctor_View_History.this);
			abd.setTitle("No History");
			abd.setMessage("No record found for this patient !");
			abd.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					Intent ivapp=new Intent(Doctor_View_History.this,Doctor_View_Appointments.class);
					ivapp.putExtra("docemail", docemail);
					startActivity(ivapp);
				}
			});
			abd.show();
			
		}
		
		ArrayList<String> app=new ArrayList<String>();
		
		for(ArrayList<String> a: applist)
		{
			String appdetails=a.get(2)+"   "+a.get(3)+"\n\nDisease: "+a.get(4)+"\nPrescription:\n"+a.get(5);
			app.add(appdetails);
		}
		
		ArrayAdapter<String> ad=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,app);
		l.setAdapter(ad);
		
		l.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				final int selected=arg2;
				
				AlertDialog.Builder ab=new AlertDialog.Builder(Doctor_View_History.this);
				ab.setTitle("Edit");
				ab.setMessage("Press 'Edit' to edit this prescription and save it as new prescription !\n(*note: This prescription will not be deleted)");
				ab.setPositiveButton("Edit", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						Calendar c=Calendar.getInstance();
						int date=c.get(Calendar.DATE);
						int month=c.get(Calendar.MONTH)+1;
						int year=c.get(Calendar.YEAR);
						String idate=date+"/"+month+"/"+year;
						
						ArrayList<String> val=new ArrayList<String>();
						val.add(docemail);
						val.add(patemail);
						val.add(idate);
						val.add(time);
						val.add(applist.get(selected).get(4));
						val.add(applist.get(selected).get(5));
						val.add("Doctor_View_History");
						
						Intent ipresc=new Intent(Doctor_View_History.this,Doctor_Add_Prescription.class);
						ipresc.putStringArrayListExtra("Values", val);
						startActivity(ipresc);
					}
				});
				ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						Intent ivapp=new Intent(Doctor_View_History.this,Doctor_View_Appointments.class);
						ivapp.putExtra("docemail", docemail);
						startActivity(ivapp);
					}
				});
				ab.show();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_doctor__view__history, menu);
		return true;
	}
	
}
