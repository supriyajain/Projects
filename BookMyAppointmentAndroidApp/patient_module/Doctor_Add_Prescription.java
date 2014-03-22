package com.example.patient_module;

import java.util.ArrayList;

import Doctor_Medicine.Doctor_Medicine;
import Patient_Prescription.Prescriptions;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Doctor_Add_Prescription extends Activity 
{

	Intent i;
	ArrayList<String> details;
	String docemail,patemail,date,time;
	String presc="", get="", prob="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor__add__prescription);
		
		i=getIntent();
		details=i.getStringArrayListExtra("Values");
		
		if(details.get(details.size()-1).equals("Doctor_View_Appointments"))
		{
			docemail=details.get(0);
			patemail=details.get(1);
			date=details.get(2);
			time=details.get(5);
		}
		else
		{
			docemail=details.get(0);
			patemail=details.get(1);
			date=details.get(2);
			time=details.get(3);
			prob=details.get(4);
			presc=details.get(5)+"\n";
		}
	
		final EditText med=(EditText)findViewById(R.id.MedName);
		final EditText NoOfDays=(EditText)findViewById(R.id.MedNoOfDays);
		final CheckBox morn=(CheckBox)findViewById(R.id.MedMornCheck);
		final CheckBox noon=(CheckBox)findViewById(R.id.MedNoonCheck);
		final CheckBox even=(CheckBox)findViewById(R.id.MedEvenCheck);
		final CheckBox night=(CheckBox)findViewById(R.id.MedNightCheck);
		final EditText medDisplay=(EditText)findViewById(R.id.MedDisplay);
		final EditText problem=(EditText)findViewById(R.id.MedProblem);
		
		problem.setText(prob);
		medDisplay.setText(presc);
		
		Button selectfromlist=(Button)findViewById(R.id.SelectMedBtn);
		selectfromlist.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				Doctor_Medicine dm=new Doctor_Medicine();
				ArrayList<String[]> medlist=dm.fetch_med(docemail);
				
				final String mednames[]=new String[medlist.size()];
				
				for(int i=0;i<medlist.size();i++)
				{
					mednames[i]=medlist.get(i)[0];
					System.out.println("Medicine= "+mednames[i]);
				}
				
				if(medlist.size()<1)
				{
					AlertDialog.Builder abd1=new AlertDialog.Builder(Doctor_Add_Prescription.this);
					abd1.setTitle("No Records");
					abd1.setMessage("You have not entered any medicines in your record yet !\nPlease add medicines into your record from your home page");
					abd1.setPositiveButton("Ok", null);
					abd1.show();
				}
				else
				{
					AlertDialog.Builder a1=new AlertDialog.Builder(Doctor_Add_Prescription.this);
					a1.setTitle("Select Medicine");
					a1.setItems(mednames, new DialogInterface.OnClickListener()
					{
					
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							med.setText(mednames[which]);
						}
					});
					a1.setNegativeButton("Cancel", null);
					a1.show();
				}
			}
		});
		
		Button add=(Button)findViewById(R.id.MedAddBtn);
		add.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				String details="";
				
				String mednm=med.getText().toString();
				if(!mednm.equals(""))
				{	
					details=mednm;
					
					String days=NoOfDays.getText().toString();
					if(!days.equals(""))
						details=details+" / "+days+" day(s)";
					else
						details=details+" / 1 day";
					
					String dose="";
					if(morn.isChecked())
						dose=dose+"M-";
					if(noon.isChecked())
						dose=dose+"A-";
					if(even.isChecked())
						dose=dose+"E-";
					if(night.isChecked())
						dose=dose+"N-";
				
					if(dose.endsWith("-"))
						dose=dose.substring(0, dose.lastIndexOf("-"));
				
					if(!dose.equals(""))
						details=details+" / "+dose;
					
					presc=presc+details+"\n";
					
					medDisplay.setText(presc);
				}
				
				med.setText("");
				NoOfDays.setText("");
				morn.setChecked(false);
				noon.setChecked(false);
				even.setChecked(false);
				night.setChecked(false);
			}
		});
		
		Button edit=(Button)findViewById(R.id.MedEditBtn);
		edit.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				final EditText ed=new EditText(Doctor_Add_Prescription.this);
				get=medDisplay.getText().toString();
				ed.setText(get);
				
				AlertDialog.Builder ab=new AlertDialog.Builder(Doctor_Add_Prescription.this);
				ab.setTitle("Edit Details");
				ab.setView(ed);
				ab.setPositiveButton("Done", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						get=ed.getText().toString();
						if(get.endsWith("\n"))
							get=get.substring(0, get.length()-1);
						
						presc=get;
						medDisplay.setText(presc);
					}
				});
				ab.setNegativeButton("Cancel", null);
				ab.show();
				
			}
		});
		
		Button reset=(Button)findViewById(R.id.MedResetBtn);
		reset.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				med.setText("");
				NoOfDays.setText("");
				morn.setChecked(false);
				noon.setChecked(false);
				even.setChecked(false);
				night.setChecked(false);
				medDisplay.setText("");
				problem.setText("");
			}
		});
		
		Button submit=(Button)findViewById(R.id.MedSubmitBtn);
		submit.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				prob=problem.getText().toString();
				
				if(!presc.equals("") && !prob.equals(""))
				{
					Prescriptions pap=new Prescriptions();
					pap.save_pres(docemail, patemail, date, time, prob, presc);
					Toast.makeText(Doctor_Add_Prescription.this, "Prescription Successfully Saved !!", 0).show();
				
					Intent iapp=new Intent(Doctor_Add_Prescription.this,Doctor_View_Appointments.class);
					iapp.putExtra("docemail", docemail);
					startActivity(iapp);
				}
				else
				{
					AlertDialog.Builder ab=new AlertDialog.Builder(Doctor_Add_Prescription.this);
					ab.setTitle("Enter Details");
					ab.setMessage("Please enter Disease and Medicine(s) before sumbmitting!");
					ab.setPositiveButton("OK", null);
					ab.show();
				}
			}
		});
		
		Button cancel=(Button)findViewById(R.id.MedCancelBtn);
		cancel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				Intent iapp=new Intent(Doctor_Add_Prescription.this,Doctor_View_Appointments.class);
				iapp.putExtra("docemail", docemail);
				startActivity(iapp);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_doctor__add__prescription,
				menu);
		return true;
	}

	@Override
	public void onBackPressed() 
	{
	    return;
	}
}
