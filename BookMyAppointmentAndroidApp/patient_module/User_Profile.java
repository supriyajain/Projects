package com.example.patient_module;

import com.example.patient_module.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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

public class User_Profile extends Activity 
{
	
	String email;
	ParseException pe;
	int checkflag=0;
	String[] details=new String[6];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_user__profile);
			
			Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
			
			Intent i=getIntent();
			email=i.getStringExtra("patemail");
			
			new progress().execute();
			
			Button home=(Button)findViewById(R.id.Homebtn);
			home.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent i=new Intent(User_Profile.this, Patient_Home.class);
					startActivity(i);
				}
			});
			
			Button edit=(Button)findViewById(R.id.Editbtn);
			edit.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent i3=new Intent(User_Profile.this, Edit_Patient_Profile.class);
					i3.putExtra("patemail", email);
					i3.putExtra("details", details);
					startActivity(i3);
				}
			});
			
			Button changepass=(Button)findViewById(R.id.ChangePasswordbtn);
			changepass.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					AlertDialog.Builder b1=new AlertDialog.Builder(User_Profile.this);
					b1.setTitle("Enter Your Email ID");
					final EditText ed=new EditText(User_Profile.this);
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
										AlertDialog.Builder b3=new AlertDialog.Builder(User_Profile.this);
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
											 AlertDialog.Builder b=new AlertDialog.Builder(User_Profile.this);
											 b.setTitle("Incorrect Email ID");
											 b.setMessage("Please enter correct Email ID !");
											 b.setPositiveButton("OK",null);
											 b.show();
										 }
										 else if(e.getCode()==ParseException.EMAIL_NOT_FOUND)
										 {
											 AlertDialog.Builder b=new AlertDialog.Builder(User_Profile.this);
											 b.setTitle("Email ID not registered yet");
											 b.setMessage("This Email ID is not Registered yet !\nPlease Register it first !");
											 b.setPositiveButton("OK",null);
											 b.show();
										 }
										 else if(e.getCode()==ParseException.CONNECTION_FAILED)
										 {
											 AlertDialog.Builder b=new AlertDialog.Builder(User_Profile.this);
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
					b1.show();				}
			});
	}

	class progress extends AsyncTask<Void, Void, Integer>
	{
		ProgressDialog pDialog = new ProgressDialog(User_Profile.this);
		
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
			ParseQuery q = new ParseQuery("Patient");
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
			    }
			}
			catch(ParseException e)
			{
			   	System.out.println(e.getCode());
			   	checkflag=1;
				pe=e;
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
					AlertDialog.Builder b=new AlertDialog.Builder(User_Profile.this);
					b.setTitle("No Connection");
					b.setMessage("Please check your internet connection !");
					b.setPositiveButton("OK",new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							Intent ihome=new Intent(User_Profile.this,Patient_Home.class);
							startActivity(ihome);
						}
					});
					b.show();
				}
			}
			
			TextView nm=(TextView)findViewById(R.id.getname);
	    	nm.setText(details[0]+" "+details[1]);
	    	TextView gen=(TextView)findViewById(R.id.getgen);
	    	gen.setText(details[2]);
	    	TextView age=(TextView)findViewById(R.id.getage);
	    	age.setText(details[3]+" years");
	    	TextView add=(TextView)findViewById(R.id.getaddress);
	    	add.setText(details[4]);
	    	TextView mob=(TextView)findViewById(R.id.getmob);
	    	mob.setText(details[5]);
	    	TextView em=(TextView)findViewById(R.id.getemail);
	    	em.setText(email);
	    }
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_user__profile, menu);
		return true;
	}

	@Override
	public void onBackPressed() 
	{
	    return;
	}
}
