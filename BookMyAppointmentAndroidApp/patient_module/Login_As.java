package com.example.patient_module;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

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


public class Login_As extends Activity 
{

	String email,pass,loginas;
	int errorflag=0;
	ParseException pe=null;
		
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login__as);
		
		Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
		
		Intent in=getIntent();
		loginas=in.getStringExtra("LoginAs");
		
		final EditText ed1=(EditText)findViewById(R.id.LoginEmailunm);
		final EditText ed2=(EditText)findViewById(R.id.Loginpass);
		
		TextView reg=(TextView)findViewById(R.id.Login_Register_Here);
        reg.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				if(loginas.equals("Patient"))
				{
					Intent i=new Intent(Login_As.this, Patient_Registation.class);
					startActivity(i);
				}
				else if(loginas.equals("Doctor"))
				{
					Intent i=new Intent(Login_As.this, Doctor_Registration.class);
					startActivity(i);
				}
			}
		});
        
        Button btn1=(Button)findViewById(R.id.loginbtn);
        btn1.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v)
			{
				email=ed1.getText().toString();
				pass=ed2.getText().toString();
				
				new progress().execute();
			}
		});
        
        TextView forgetpass=(TextView)findViewById(R.id.Login_Forget_Password);
        forgetpass.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				AlertDialog.Builder b1=new AlertDialog.Builder(Login_As.this);
				b1.setTitle("Enter Your Email ID");
				final EditText ed=new EditText(Login_As.this);
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
									AlertDialog.Builder b3=new AlertDialog.Builder(Login_As.this);
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
										 AlertDialog.Builder b=new AlertDialog.Builder(Login_As.this);
										 b.setTitle("Incorrect Email ID");
										 b.setMessage("Please enter correct Email ID !");
										 b.setPositiveButton("OK",null);
										 b.show();
									 }
									 else if(e.getCode()==ParseException.EMAIL_NOT_FOUND)
									 {
										 AlertDialog.Builder b=new AlertDialog.Builder(Login_As.this);
										 b.setTitle("Email ID not registered yet");
										 b.setMessage("This Email ID is not Registered yet !\nPlease Register it first !");
										 b.setPositiveButton("OK",null);
										 b.show();
									 }
									 else if(e.getCode()==ParseException.CONNECTION_FAILED)
									 {
										 AlertDialog.Builder b=new AlertDialog.Builder(Login_As.this);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login__as, menu);
		return true;
	}

	@Override
	public void onBackPressed() 
	{
	    Intent icancel=new Intent(Login_As.this,MainActivity.class);
	    startActivity(icancel);
		return;
	}


	class progress extends AsyncTask<Void, Void, Integer>
	{
	
		ProgressDialog pDialog = new ProgressDialog(Login_As.this);
		
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
				try
				{
					 ParseUser user=ParseUser.logIn(email, pass); 
					 if (user != null) 
					 {
						 if(user.getBoolean("emailVerified"))
						 {	 
						 	if(loginas.equals("Patient"))
							{
								 if(user.get("LoginAs").equals(loginas))
								 {
									 Intent i=new Intent(Login_As.this, Patient_Home.class);
									 startActivity(i);
								 }
								 else
								 {
									 errorflag=1;
								 }
							}
							else
							{
								 if(user.get("LoginAs").equals(loginas))
								 {
									 Intent i=new Intent(Login_As.this, Doctor_Home.class);
									 startActivity(i);
								 }
								 else
								 {
									 errorflag=2;
								 }
							}
					 	}
						else
						{
							errorflag=3;
						}
					 }
				}
				catch(ParseException e) 
				{
					System.out.println(e.getCode());
					errorflag=4;
					pe=e;
				}
				return errorflag;
		}
		
		@Override
        protected void onPostExecute(Integer f) 
		{
			pDialog.dismiss();
			
			if(f==1)
			{
				AlertDialog.Builder ab=new AlertDialog.Builder(Login_As.this);
				ab.setTitle("Not Registered");
				ab.setMessage("You are not registered as 'Patient' with this email ID !!");
				ab.setPositiveButton("OK",null);
				ab.show();
			}
			else if(f==2)
			{
				AlertDialog.Builder ab=new AlertDialog.Builder(Login_As.this);
				ab.setTitle("Not Registered");
				ab.setMessage("You are not registered as 'Doctor' with this email ID !!");
				ab.setPositiveButton("OK",null);
				ab.show();
			}
			else if(f==3)
			{
				AlertDialog.Builder b=new AlertDialog.Builder(Login_As.this);
				b.setTitle("Unverified User");
				b.setMessage("You are not a verified user !\nPlease verify your email ID by clicking on the verification link (enclosed in an email) you might have received after registration !");
				b.setPositiveButton("OK",null);
				b.show();
			}
			else if(f==4 && pe!=null)
			{
				if(pe.getCode()==ParseException.EMAIL_NOT_FOUND || pe.getCode()==ParseException.INVALID_EMAIL_ADDRESS || pe.getCode()==ParseException.OBJECT_NOT_FOUND)
				 {
					 AlertDialog.Builder b=new AlertDialog.Builder(Login_As.this);
					 b.setTitle("Incorrect Username/Password");
					 b.setMessage("Please enter correct combination of Username and Password !");
					 b.setPositiveButton("OK",null);
					 b.show();
				 }
				 else if(pe.getCode()==ParseException.EMAIL_MISSING)
				 {
					 AlertDialog.Builder b=new AlertDialog.Builder(Login_As.this);
					 b.setTitle("Blank Field");
					 b.setMessage("Please enter your Email ID !");
					 b.setPositiveButton("OK",null);
					 b.show();
				 }
				 else if(pe.getCode()==ParseException.PASSWORD_MISSING)
				 {
					 AlertDialog.Builder b=new AlertDialog.Builder(Login_As.this);
					 b.setTitle("Blank Field");
					 b.setMessage("Please enter your Password !");
					 b.setPositiveButton("OK",null);
					 b.show();
				 }
				 else if(pe.getCode()==ParseException.CONNECTION_FAILED)
				 {
					 AlertDialog.Builder b=new AlertDialog.Builder(Login_As.this);
					 b.setTitle("No Connection");
					 b.setMessage("Please check your internet connection !");
					 b.setPositiveButton("OK",null);
					 b.show();
				 }
			}
		}
	}

}