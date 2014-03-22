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

public class Patient_Registation extends Activity 
{

	String first_name="",last_name="",address="",email="",mob_numb="",password="",gen="";
	int patage=0,checkflag=0;
	ParseException pe=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_registation);
		
		Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
		
		final EditText fnm=(EditText)findViewById(R.id.fnm);
		final EditText lnm=(EditText)findViewById(R.id.lnm);
		final EditText age=(EditText)findViewById(R.id.age);
		final EditText add=(EditText)findViewById(R.id.address);
		final EditText em=(EditText)findViewById(R.id.email);
		final EditText mob=(EditText)findViewById(R.id.Mob);
		final EditText setpass=(EditText)findViewById(R.id.setpass);
		final RadioGroup rg2=(RadioGroup)findViewById(R.id.rg2);
		
		Button submit=(Button)findViewById(R.id.Submitbtn);
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
				int id=rg2.getCheckedRadioButtonId();
				if(id!=-1)
				{
					RadioButton rbtn=(RadioButton)findViewById(id);
					gen=rbtn.getText().toString();
				}
				int ageflag=0;
				try
				{
					patage=Integer.parseInt(age.getText().toString());
				}
				catch(Exception e)
				{
					ageflag=1;
				}
				
				
				if(!first_name.equals("") && !last_name.equals("") && !address.equals("") && !email.equals("") && !mob_numb.equals("") && !password.equals("") && !gen.equals("") && !age.getText().toString().equals(""))
				{
					if(patage>120 || patage<=0 || ageflag==1)
					{
						AlertDialog.Builder b=new AlertDialog.Builder(Patient_Registation.this);
						b.setTitle("Invalid Age");
						b.setMessage("Please Enter Valid Age");
						b.setPositiveButton("OK", null);
						b.show();
					}
					else if(mob_numb.length()!=10)
					{
						AlertDialog.Builder b0=new AlertDialog.Builder(Patient_Registation.this);
						b0.setTitle("Invalid Mobile Number");
						b0.setMessage("Please Enter Valid 10 digit mobile number");
						b0.setPositiveButton("OK", null);
						b0.show();
					}
					else if(password.length()<5)
					{
						AlertDialog.Builder b1=new AlertDialog.Builder(Patient_Registation.this);
						b1.setTitle("Inappropriate password");
						b1.setMessage("Length of the password must be greater than 4 characters !\nPlease enter appropriate password !");
						b1.setPositiveButton("OK", null);
						b1.show();
					}
					else
					{
						new progress().execute();
					}
				}
				else
				{
					AlertDialog.Builder b3=new AlertDialog.Builder(Patient_Registation.this);
					b3.setTitle("Incomplete Information");
					b3.setMessage("Please fill information in all the fields before submitting !");
					b3.setPositiveButton("OK", null);
					b3.show();
				}
			}
		});
		
		Button reset=(Button)findViewById(R.id.Resetbtn);
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
				rg2.clearCheck();
			}
		});
	}

	class progress extends AsyncTask<Void, Void, Integer>
	{
	
		ProgressDialog pDialog = new ProgressDialog(Patient_Registation.this);
		
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
			user.put("LoginAs", "Patient");
			try
			{
					user.signUp();
					
					ParseObject u=new ParseObject("Patient");
			    	u.put("First_name", first_name);
					u.put("Last_name", last_name);
					u.put("Gender", gen);
					u.put("Age", patage);
					u.put("Address", address);
					u.put("Mobile", mob_numb);
					u.put("Email", email);
					try
					{
						u.save();
						checkflag=1;
						
						try
						 {	 	
							 ParseInstallation installation=ParseInstallation.getCurrentInstallation();
							 installation.put("ID",email);
							 installation.save();
								
							 PushService.setDefaultPushCallback(Patient_Registation.this, Patient_View_Messages.class);
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
				AlertDialog.Builder ab=new AlertDialog.Builder(Patient_Registation.this);
				ab.setTitle("Verify Your Email");
				ab.setMessage("A verification mail has been sent to the email ID you provided during registration.\nPlease click on that link to start using this software");
				ab.setPositiveButton("OK", new DialogInterface.OnClickListener() 
				{
						
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						Toast.makeText(Patient_Registation.this, "Information Saved Successfully", 0).show();
							
						if(pe!=null)
						{
							Toast.makeText(Patient_Registation.this, "Device Registration error !", 1).show();
						}
						
						Intent i2=new Intent(Patient_Registation.this,Login_As.class);
						i2.putExtra("LoginAs", "Patient");
						startActivity(i2);
					}
				});
				ab.show();
			}
			else if(checkflag==2)
			{
				AlertDialog.Builder b=new AlertDialog.Builder(Patient_Registation.this);
				b.setTitle("No Connection");
				b.setMessage("Please check your internet connection !");
				b.setPositiveButton("OK",null);
				b.show();
			}
			else if(checkflag==3)
			{
				if(pe.getCode()==ParseException.USERNAME_TAKEN)
		    	{
		    	  AlertDialog.Builder b2=new AlertDialog.Builder(Patient_Registation.this);
		    	  b2.setTitle("Aleready Registered");
		    	  b2.setMessage("This Email ID is already Registered !!");
		    	  b2.setPositiveButton("OK", null);
		    	  b2.show();
		    	}
		    	else if(pe.getCode()==ParseException.INVALID_EMAIL_ADDRESS)
		    	{
		    	  AlertDialog.Builder b2=new AlertDialog.Builder(Patient_Registation.this);
		    	  b2.setTitle("Invalid Email ID");
		    	  b2.setMessage("Please enter a valid email ID !!");
		    	  b2.setPositiveButton("OK", null);
		    	  b2.show();
		    	}
		    	else if(pe.getCode()==ParseException.CONNECTION_FAILED)
		    	{
		    		AlertDialog.Builder b=new AlertDialog.Builder(Patient_Registation.this);
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
		getMenuInflater().inflate(R.menu.activity_registation, menu);
		return true;
	}

	@Override
	public void onBackPressed() 
	{
	    Intent icancel=new Intent(Patient_Registation.this,MainActivity.class);
	    startActivity(icancel);
		return;
	}
}
