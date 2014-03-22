package com.example.patient_module;

import java.util.ArrayList;
import com.example.patient_module.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import Doctor_Visiting_Day_Time.Doctor_Fetch_Day_Time;
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
import android.widget.EditText;
import android.widget.TextView;

public class Doctor_Profile extends Activity 
{

	String email,time="";
	ParseException pe;
	int checkflag=0;
	String[] details=new String[7];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_doctor__profile);
			
			Intent i=getIntent();
			email=i.getStringExtra("docemail");
			
			Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
			
			new progress().execute();
		
			Button home=(Button)findViewById(R.id.dochomebtn);
			home.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent i=new Intent(Doctor_Profile.this, Doctor_Home.class);
					startActivity(i);
				}
			});
			
			Button edit=(Button)findViewById(R.id.doceditbtn);
			edit.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent i2=new Intent(Doctor_Profile.this, Edit_Doctor_Profile.class);
					i2.putExtra("docemail", email);
					i2.putExtra("details", details);
					startActivity(i2);
				}
			});
			
			Button changepass=(Button)findViewById(R.id.docChangePasswordbtn);
			changepass.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					AlertDialog.Builder b1=new AlertDialog.Builder(Doctor_Profile.this);
					b1.setTitle("Enter Your Email ID");
					final EditText ed=new EditText(Doctor_Profile.this);
					ed.setHint("Enter Email ID / Username");
					ed.setGravity(0);
					ed.setLines(1);
					b1.setView(ed);
					b1.setPositiveButton("OK",new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							String email=ed.getText().toString();
							
							ParseUser.requestPasswordResetInBackground(email,new RequestPasswordResetCallback()
							{
								public void done(ParseException e) 
								{
									if (e == null) 
									{
										AlertDialog.Builder b3=new AlertDialog.Builder(Doctor_Profile.this);
										b3.setTitle("Link Sent");
										b3.setMessage("Password Reset link has been sent to your Email ID !");
										b3.setPositiveButton("OK", null);	
										b3.show();
									}
									else 
									{
										System.out.println(e.getCode());
										if(e.getCode()==ParseException.INVALID_EMAIL_ADDRESS || e.getCode()==ParseException.OBJECT_NOT_FOUND)
										 {
											 AlertDialog.Builder b=new AlertDialog.Builder(Doctor_Profile.this);
											 b.setTitle("Incorrect Email ID");
											 b.setMessage("Please enter correct Email ID !");
											 b.setPositiveButton("OK",null);
											 b.show();
										 }
										 else if(e.getCode()==ParseException.EMAIL_NOT_FOUND)
										 {
											 AlertDialog.Builder b=new AlertDialog.Builder(Doctor_Profile.this);
											 b.setTitle("Email ID not registered yet");
											 b.setMessage("This Email ID is not Registered yet !\nPlease Register it first !");
											 b.setPositiveButton("OK",null);
											 b.show();
										 }
										 else if(e.getCode()==ParseException.CONNECTION_FAILED)
										 {
											 AlertDialog.Builder b=new AlertDialog.Builder(Doctor_Profile.this);
											 b.setTitle("No Connection");
											 b.setMessage("Please check your internet connection !");
											 b.setPositiveButton("OK",null);
											 b.show();
										 }
									}
								}
							});
							
						}
					});
					b1.setNegativeButton("Cancel", null);
					b1.show();
				}
			});
	}
	
	class progress extends AsyncTask<Void, Void, Integer>
	{
		ProgressDialog pDialog = new ProgressDialog(Doctor_Profile.this);
		
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
			ParseQuery q = new ParseQuery("Doctor");
			q.whereEqualTo("Email", email);
			try
			{
				ParseObject po=q.getFirst(); 
			    if (po!= null) 
			    {
			    	details[0]=po.getString("First_name");
			    	details[1]=po.getString("Last_name");
			    	details[2]=po.getString("Gender");
			    	details[3]=po.getInt("Age")+"";
			    	details[4]=po.getString("Address");
			    	details[5]=po.getString("Mobile");
			    	details[6]=po.getString("Specialization");
			    }
			}
			catch(ParseException e)
			{
			   	System.out.println(e.getCode());
			   	checkflag=1;
				pe=e;
			}
			
			Doctor_Fetch_Day_Time dfdt=new Doctor_Fetch_Day_Time();
			ArrayList<ArrayList<String>> daytimelist=dfdt.Fetch_Day_Time(email);
			for(ArrayList<String> s: daytimelist)
			{
				time=time+s.get(1)+":\n";
				if(!(s.get(2).equals("None")))
					time=time+s.get(2)+", ";
				if(!(s.get(3).equals("None")))
					time=time+s.get(3)+", ";
				if(!(s.get(4).equals("None")))
					time=time+s.get(4);
				
				if(time.endsWith(", "))
					time=time.substring(0, time.lastIndexOf(","));
				
				time=time+"\n";
			}
			    
			return null;
		}
		
		@Override
        protected void onPostExecute(Integer f) 
		{
			pDialog.dismiss();
			
			if(checkflag==1)
			{
				if(pe.getCode()==ParseException.CONNECTION_FAILED || pe.getCode()==ParseException.OBJECT_NOT_FOUND)
				{
					AlertDialog.Builder b=new AlertDialog.Builder(Doctor_Profile.this);
					b.setTitle("No Connection");
					b.setMessage("Please check your internet connection !");
					b.setPositiveButton("OK",null);
					b.show();
					
					Intent ihome=new Intent(Doctor_Profile.this,Doctor_Home.class);
					startActivity(ihome);
				}
			}
			
			TextView nm=(TextView)findViewById(R.id.getdocname);
	    	nm.setText(details[0]+" "+details[1]);
	    	TextView gen=(TextView)findViewById(R.id.getdocgen);
	    	gen.setText(details[2]);
	    	TextView age=(TextView)findViewById(R.id.getdocage);
	    	age.setText(details[3]+" years");
	    	TextView add=(TextView)findViewById(R.id.getdocaddress);
	    	add.setText(details[4]);
	    	TextView mob=(TextView)findViewById(R.id.getdocmob);
	    	mob.setText(details[5]);
	    	TextView spec=(TextView)findViewById(R.id.getdocspec);
	    	spec.setText(details[6]);
	    	TextView em=(TextView)findViewById(R.id.getdocemail);
	    	em.setText(email);
	    	TextView timing=(TextView)findViewById(R.id.getdocvisittime);
			timing.setText(time);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_doctor__profile, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() 
	{
	    return;
	}
}
