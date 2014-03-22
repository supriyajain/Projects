package com.example.patient_module;

import com.example.patient_module.R;
import com.parse.Parse;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity 
{
	String email,loginas;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");

		final ParseUser cu = ParseUser.getCurrentUser();
		if (cu != null && cu.getBoolean("emailVerified")) 
		{
			email=cu.getUsername();
			loginas=cu.getString("LoginAs");
			
			if(loginas.equals("Patient"))
			{
				Intent ihome=new Intent(MainActivity.this,Patient_Home.class);
				startActivity(ihome);
			}
			else
			{
				Intent ihome=new Intent(MainActivity.this,Doctor_Home.class);
				startActivity(ihome);
			}
		}
		else
		{
			Button close=(Button)findViewById(R.id.mainCloseBtn);
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
			
			Button pbtn=(Button)findViewById(R.id.patientbtn);
	        pbtn.setOnClickListener(new OnClickListener()
	        {
				
				@Override
				public void onClick(View v) 
				{
					Intent i=new Intent(MainActivity.this,Login_As.class);
					i.putExtra("LoginAs", "Patient");
					startActivity(i);
				}
			});
	        
	        Button dbtn=(Button)findViewById(R.id.doctorbtn);
	        dbtn.setOnClickListener(new OnClickListener()
	        {
				
				@Override
				public void onClick(View v) 
				{
					Intent i=new Intent(MainActivity.this,Login_As.class);
					i.putExtra("LoginAs", "Doctor");
					startActivity(i);
				}
			});
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
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
