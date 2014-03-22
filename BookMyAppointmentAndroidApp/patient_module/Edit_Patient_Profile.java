package com.example.patient_module;

import com.example.patient_module.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Edit_Patient_Profile extends Activity
{

	String first_name="",last_name="",address="",email="",mobile="",gen="";
	int patage=0,checkflag=0;
	ParseException pe=null;
	String[] details=new String[6];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_edit__patient__profile);
			
			Intent i=getIntent();
			email=i.getStringExtra("patemail");
			details=i.getStringArrayExtra("details");
			
			Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
				
			final EditText fnm=(EditText)findViewById(R.id.editfnm);
			final EditText lnm=(EditText)findViewById(R.id.editlnm);
			final EditText age=(EditText)findViewById(R.id.editage);
			final EditText add=(EditText)findViewById(R.id.editaddress);
			final EditText mob=(EditText)findViewById(R.id.editmob);
			final RadioGroup rg2=(RadioGroup)findViewById(R.id.editrg2);
			
			fnm.setText(details[0]);
			lnm.setText(details[1]);
			age.setText(details[3]);
			add.setText(details[4]);
			mob.setText(details[5]);
			String gender=details[2];
			if(gender.equals("Male"))
			{
				RadioButton male=(RadioButton)findViewById(R.id.editmale);
				male.setChecked(true);
			}
			else
			{
				RadioButton female=(RadioButton)findViewById(R.id.editfemale);
				female.setChecked(true);
			}
			   
			
			Button submit=(Button)findViewById(R.id.editSubmitbtn);
			submit.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					first_name=fnm.getText().toString();
					last_name=lnm.getText().toString();
					address=add.getText().toString();
					mobile=mob.getText().toString();
					
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
					
					
					if(!first_name.equals("") && !last_name.equals("") && !address.equals("") && !mobile.equals("") && !gen.equals("") && !age.getText().toString().equals(""))
					{
						if(patage>120 || patage<=0 || ageflag==1)
						{
							AlertDialog.Builder b=new AlertDialog.Builder(Edit_Patient_Profile.this);
							b.setTitle("Invalid Age");
							b.setMessage("Please Enter Valid Age");
							b.setPositiveButton("OK", null);
							b.show();
						}
						else if(mobile.length()!=10)
						{
							AlertDialog.Builder b0=new AlertDialog.Builder(Edit_Patient_Profile.this);
							b0.setTitle("Invalid Mobile Number");
							b0.setMessage("Please Enter Valid 10 digit mobile number");
							b0.setPositiveButton("OK", null);
							b0.show();
						}
						else
						{
							new progress().execute();
						}
					}
					else
					{
						AlertDialog.Builder b1=new AlertDialog.Builder(Edit_Patient_Profile.this);
						b1.setTitle("Incomplete Information");
						b1.setMessage("Please fill information in all the fields before submitting !");
						b1.setPositiveButton("OK", null);
						b1.show();
					}
				}
			});
			
			Button reset=(Button)findViewById(R.id.editResetbtn);
			reset.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					fnm.setText("");
					lnm.setText("");
					add.setText("");
					age.setText("");
					mob.setText("");
					rg2.clearCheck();
				}
			});
			
			Button cancel=(Button)findViewById(R.id.editCancelbtn);
			cancel.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent i2=new Intent(Edit_Patient_Profile.this, User_Profile.class);
					i2.putExtra("patemail", email);
					startActivity(i2);
				}
			});
	}
	
	class progress extends AsyncTask<Void, Void, Integer>
	{
	
		ProgressDialog pDialog = new ProgressDialog(Edit_Patient_Profile.this);
		
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
				if(po!=null)
				{
					po.put("First_name", first_name);
					po.put("Last_name", last_name);
					po.put("Gender", gen);
					po.put("Age", patage);
					po.put("Address", address);
					po.put("Mobile", mobile);
					po.save();
						
					checkflag=1;
				}
			}
			catch(ParseException e)
			{
				System.out.println(e.getCode());
				checkflag=2;
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
				Toast.makeText(Edit_Patient_Profile.this, "Information Saved Successfully", 0).show();
			  	
				Intent i2=new Intent(Edit_Patient_Profile.this,User_Profile.class);
				i2.putExtra("patemail", email);
				startActivity(i2);
			}
			else if(checkflag==2)
			{
				 AlertDialog.Builder b=new AlertDialog.Builder(Edit_Patient_Profile.this);
				 b.setTitle("No Connection");
				 b.setMessage("Please check your internet connection !");
				 b.setPositiveButton("OK",null);
				 b.show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_edit__patient__profile, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() 
	{
	    return;
	}
}
