package com.example.patient_module;

import com.example.patient_module.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Doctor_Home extends Activity 
{

	String docemail,detail="";
	int checkflag=0;
	ParseException pe=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_doctor__home);
		
			Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
			
			new progress().execute();
			
			Button close=(Button)findViewById(R.id.docHomeCloseBtn);
			close.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent homeIntent1= new Intent(Intent.ACTION_MAIN);
					homeIntent1.addCategory(Intent.CATEGORY_HOME);
					homeIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(homeIntent1);
				}
			});
			
			Button VProfile=(Button)findViewById(R.id.viewdocprofilebtn);
			VProfile.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent i2=new Intent(Doctor_Home.this, Doctor_Profile.class);
					i2.putExtra("docemail", docemail);
					startActivity(i2);
				}
			});
			
			Button changeDayTime=(Button)findViewById(R.id.changedocappbtn);
			changeDayTime.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent ieditdaytime=new Intent(Doctor_Home.this,Doctor_Edit_Day_Time.class);
					ieditdaytime.putExtra("docemail", docemail);
					startActivity(ieditdaytime);
				}
			});
			
			Button ViewApp=(Button)findViewById(R.id.Viewdocappbtn);
			ViewApp.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent iviewapp=new Intent(Doctor_Home.this,Doctor_View_Appointments.class);
					iviewapp.putExtra("docemail", docemail);
					startActivity(iviewapp);
				}
			});
			
			Button searchPatient=(Button)findViewById(R.id.SearchPatient);
			searchPatient.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent isearchpat=new Intent(Doctor_Home.this,Doctor_Search_Patient.class);
					isearchpat.putExtra("docemail", docemail);
					startActivity(isearchpat);
				}
			});
			
			Button AddMed=(Button)findViewById(R.id.AddMedicines);
			AddMed.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent iaddmed=new Intent(Doctor_Home.this,Doctor_Add_Medicine.class);
					iaddmed.putExtra("docemail", docemail);
					startActivity(iaddmed);
				}
			});
			
			Button sendmsg=(Button)findViewById(R.id.Sendpatmsgbtn);
			sendmsg.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent isendmsg=new Intent(Doctor_Home.this,Doctor_Send_Message.class);
					isendmsg.putExtra("docemail", docemail);
					startActivity(isendmsg);
				}
			});
			
			Button viewmsg=(Button)findViewById(R.id.Viewdocmsgbtn);
			viewmsg.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent iviewmsg=new Intent(Doctor_Home.this,Doctor_View_Messages.class);
					iviewmsg.putExtra("docemail", docemail);
					startActivity(iviewmsg);
				}
			});
			
			Button viewblockedusers=(Button)findViewById(R.id.docviewblockusersbtn);
			viewblockedusers.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent iviewusers=new Intent(Doctor_Home.this,Blocked_Users.class);
					iviewusers.putExtra("docemail", docemail);
					iviewusers.putExtra("Val", 1);
					startActivity(iviewusers);
				}
			});
			
			Button logout=(Button)findViewById(R.id.logoutdochomebtn);
			logout.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					ParseUser.logOut();
					Intent ilogout=new Intent(Doctor_Home.this,MainActivity.class);
					startActivity(ilogout);
				}
			});
	}
	
	class progress extends AsyncTask<Void, Void, Integer>
	{
	
		ProgressDialog pDialog = new ProgressDialog(Doctor_Home.this);
		
		@Override
		protected void onPreExecute()
		{
		    super.onPreExecute();
		    
		    pDialog.setMessage("Please Wait...");
		    pDialog.setIndeterminate(true);
		    pDialog.setCancelable(false);
		    pDialog.show();
		}
	
		
		@Override
		protected Integer doInBackground(Void... params)
		{
			final ParseUser cu = ParseUser.getCurrentUser();
			if (cu != null && cu.getBoolean("emailVerified")) 
			{
				docemail=cu.getUsername();
			
				ParseQuery q=new ParseQuery("Doctor");
				q.whereEqualTo("Email", docemail);
				try
				{
					ParseObject o=q.getFirst();
					if(o!=null)
					{
						detail="Dr. "+o.getString("First_name");
					}
				}
				catch(ParseException e)
				{
					System.out.println(e.getCode());
					checkflag=1;
					pe=e;
				}
			}
			else
			{
				Intent i2=new Intent(Doctor_Home.this, MainActivity.class);
				startActivity(i2);
			}
			
			return checkflag;
		}
		
		@Override
        protected void onPostExecute(Integer f) 
		{
			pDialog.dismiss();
			
			if(checkflag==1)
			{
				if(pe.getCode()==ParseException.CONNECTION_FAILED)
				{
					AlertDialog.Builder b=new AlertDialog.Builder(Doctor_Home.this);
					b.setTitle("No Connection");
					b.setMessage("Please check your internet connection !");
					b.setPositiveButton("OK",new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							Intent homeIntent= new Intent(Intent.ACTION_MAIN);
							homeIntent.addCategory(Intent.CATEGORY_HOME);
							homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(homeIntent);
						}
					});
					b.show();
				}
			}
			
			TextView welcometext=(TextView)findViewById(R.id.DocWelcomeText);
			welcometext.setText("Welcome "+detail+" !");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_doctor__home, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() 
	{
		Intent homeIntent= new Intent(Intent.ACTION_MAIN);
		homeIntent.addCategory(Intent.CATEGORY_HOME);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(homeIntent);
		return;
	}
}
