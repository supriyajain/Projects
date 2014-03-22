package com.example.patient_module;

import com.example.patient_module.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Doctor_Registration extends Activity 
{

	String first_name="",last_name="",address="",email="",mob_numb="",password="",specialization="",gen="";
	int docage=0,Time=0,checkflag=0;
	ParseException pe=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor__registration);
		
		Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
		
		final EditText fnm=(EditText)findViewById(R.id.docfnm);
		final EditText lnm=(EditText)findViewById(R.id.doclnm);
		final EditText age=(EditText)findViewById(R.id.docage);
		final EditText add=(EditText)findViewById(R.id.docaddress);
		final EditText em=(EditText)findViewById(R.id.docemail);
		final EditText mob=(EditText)findViewById(R.id.docmob);
		final EditText setpass=(EditText)findViewById(R.id.docsetpass);
		final EditText spec=(EditText)findViewById(R.id.docspecialization);
		final RadioGroup rg=(RadioGroup)findViewById(R.id.docrg);
		
		final String TimeList[]={"05","10","15","20","25","30","35","40","45","50","55","60"};
		
		Button submit=(Button)findViewById(R.id.docsubmitbtn);
		submit.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				first_name=fnm.getText().toString();
				last_name=lnm.getText().toString();
				address=add.getText().toString();
				email=em.getText().toString();
				mob_numb=mob.getText().toString();
				password=setpass.getText().toString();
				specialization=spec.getText().toString();
				
				int id=rg.getCheckedRadioButtonId();
				if(id!=-1)
				{
					RadioButton rbtn=(RadioButton)findViewById(id);
					gen=rbtn.getText().toString();
				}
				
				int ageflag=0;
				try
				{
					docage=Integer.parseInt(age.getText().toString());
				}
				catch(Exception e)
				{
					ageflag=1;
				}
				
				
				if(!first_name.equals("") && !last_name.equals("") && !address.equals("") && !email.equals("") && !mob_numb.equals("") && !password.equals("") && !gen.equals("") && !specialization.equals("") && !age.getText().toString().equals(""))
				{
					if(docage>120 || docage<=0 || ageflag==1)
					{
						AlertDialog.Builder b=new AlertDialog.Builder(Doctor_Registration.this);
						b.setTitle("Invalid Age");
						b.setMessage("Please Enter Valid Age");
						b.setPositiveButton("OK", null);
						b.show();
					}
					
					else if(mob_numb.length()!=10)
					{
						AlertDialog.Builder b1=new AlertDialog.Builder(Doctor_Registration.this);
						b1.setTitle("Invalid Mobile Number");
						b1.setMessage("Please Enter Valid 10 digit mobile number");
						b1.setPositiveButton("OK", null);
						b1.show();
					}
					else if(password.length()<5)
					{
						AlertDialog.Builder b2=new AlertDialog.Builder(Doctor_Registration.this);
						b2.setTitle("Inappropriate password");
						b2.setMessage("Length of the password must be greater than 4 characters !\nPlease enter appropriate password !");
						b2.setPositiveButton("OK", null);
						b2.show();
					}
					else
					{
						AlertDialog.Builder builder=new AlertDialog.Builder(Doctor_Registration.this);
						builder.setTitle("Select Appointment Time Interval (in Minutes)");
						builder.setItems(TimeList, new DialogInterface.OnClickListener()
						{
								
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								Time=Integer.parseInt(TimeList[which]);
								new progress().execute();
							}
						});
						builder.show();
					}
				}
				else
				{
					AlertDialog.Builder b4=new AlertDialog.Builder(Doctor_Registration.this);
					b4.setTitle("Incomplete Information");
					b4.setMessage("Please fill information in all the fields before submitting !");
					b4.setPositiveButton("OK", null);
					b4.show();
				}
			}
		});
		
		Button reset=(Button)findViewById(R.id.docresetbtn);
		reset.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				fnm.setText("");
				lnm.setText("");
				age.setText("");
				add.setText("");
				em.setText("");
				mob.setText("");
				setpass.setText("");
				spec.setText("");
				rg.clearCheck();
			}
		});
		
	}

	class progress extends AsyncTask<Void, Void, Integer>
	{
	
		ProgressDialog pDialog = new ProgressDialog(Doctor_Registration.this);
		
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
			ParseUser user = new ParseUser();
			user.setUsername(email);
			user.setEmail(email);
			user.setPassword(password);
			user.put("LoginAs", "Doctor");
			try
			{
					user.signUp();
					
					ParseObject u=new ParseObject("Doctor");
			    	u.put("First_name", first_name);
					u.put("Last_name", last_name);
					u.put("Gender", gen);
					u.put("Age", docage);
					u.put("Address", address);
					u.put("Mobile", mob_numb);
					u.put("Email", email);
					u.put("Specialization", specialization);
					u.put("Time_interval", Time);
					try
					{
						u.save();
						checkflag=1;
						
						try
						 {
							 ParseInstallation installation = ParseInstallation.getCurrentInstallation();
							 installation.put("ID",email);
							 installation.save();
								
							 PushService.setDefaultPushCallback(Doctor_Registration.this, Doctor_View_Messages.class);
						 }
						 catch(ParseException e1)
						 {
							 System.out.println(e1.getCode());
							 pe=e1;
						 }
					}
					catch(ParseException e1)
					{
						System.out.println(e1.getCode());
						checkflag=2;
						pe=e1;
					}
			  }
			  catch(ParseException e)
			  {
			    	System.out.println(e.getCode());
			    	checkflag=3;
			    	pe=e;
			  }
			 
			return checkflag;
		}
		
		@Override
        protected void onPostExecute(Integer f) 
		{
			pDialog.dismiss();
			
			if(checkflag==1)
			{
				Toast.makeText(Doctor_Registration.this, "Information Saved Successfully", 1).show();
				
				if(pe!=null)
				{
					Toast.makeText(Doctor_Registration.this, "Device Registration error !", 1).show();
				}
				
		    	Intent i2=new Intent(Doctor_Registration.this,Doctor_Select_Day_Time.class);
		    	i2.putExtra("Doc_ID", email);
		    	startActivity(i2);
			}
			else if(checkflag==2)
			{
				AlertDialog.Builder b=new AlertDialog.Builder(Doctor_Registration.this);
				b.setTitle("No Connection");
				b.setMessage("Please check your internet connection !");
				b.setPositiveButton("OK",null);
				b.show();
			}
			else if(checkflag==3)
			{
				if(pe.getCode()==ParseException.USERNAME_TAKEN)
		    	{
		    	  AlertDialog.Builder b2=new AlertDialog.Builder(Doctor_Registration.this);
		    	  b2.setTitle("Aleready Registered");
		    	  b2.setMessage("This Email ID is already Registered !!");
		    	  b2.setPositiveButton("OK", null);
		    	  b2.show();
		    	}
		    	else if(pe.getCode()==ParseException.INVALID_EMAIL_ADDRESS)
		    	{
		    	  AlertDialog.Builder b2=new AlertDialog.Builder(Doctor_Registration.this);
		    	  b2.setTitle("Invalid Email ID");
		    	  b2.setMessage("Please enter a valid email ID !!");
		    	  b2.setPositiveButton("OK", null);
		    	  b2.show();
		    	}
		    	else if(pe.getCode()==ParseException.CONNECTION_FAILED)
		    	{
		    		AlertDialog.Builder b=new AlertDialog.Builder(Doctor_Registration.this);
					b.setTitle("No Connection");
					b.setMessage("Please check your internet connection !");
					b.setPositiveButton("OK",null);
					b.show();
		    	}
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_doctor__registration, menu);
		return true;
	}

	@Override
	public void onBackPressed() 
	{
	    Intent icancel=new Intent(Doctor_Registration.this,MainActivity.class);
	    startActivity(icancel);
		return;
	}
}
