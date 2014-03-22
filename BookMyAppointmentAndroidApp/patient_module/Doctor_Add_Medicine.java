package com.example.patient_module;

import java.util.ArrayList;

import com.parse.Parse;
import com.parse.ParseException;
import Doctor_Medicine.Doctor_Medicine;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Doctor_Add_Medicine extends Activity 
{
	
	String email;
	int errorflag=0;
	ParseException pe=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_doctor_add__medicine);
			
			Intent i=getIntent();
			email=i.getStringExtra("docemail");
			
			Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
		
			final EditText med=(EditText)findViewById(R.id.AddMedName);
			final EditText comp=(EditText)findViewById(R.id.AddCompanyName);
			
			Button add=(Button)findViewById(R.id.AddMedCompBtn);
			add.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					String medname=med.getText().toString();
					String compname=comp.getText().toString();
					
					if(medname.equals("") || compname.equals(""))
					{
						AlertDialog.Builder a=new AlertDialog.Builder(Doctor_Add_Medicine.this);
						a.setTitle("Incomplete Information");
						a.setMessage("Please enter both medicine name and company name before submitting !");
						a.setPositiveButton("OK", null);
						a.show();
					}
					else
					{
						Doctor_Medicine dim=new Doctor_Medicine();
						dim.save_med(email, medname, compname);
						
						med.setText("");
						comp.setText("");
						Toast.makeText(Doctor_Add_Medicine.this, "Medicine: "+medname+"\nCompany: "+compname+" Successfully Saved !", 0).show();
					}
				}
			});
			
			Button reset=(Button)findViewById(R.id.ResetMedCompBtn);
			reset.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					med.setText("");
					comp.setText("");
				}
			});
			
			Button edit=(Button)findViewById(R.id.EditMedCompBtn);
			edit.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					
					Doctor_Medicine dfm=new Doctor_Medicine();
					ArrayList<String[]> medlist=dfm.fetch_med(email);
					
					if(medlist.size()<1)
					{
						AlertDialog.Builder a0=new AlertDialog.Builder(Doctor_Add_Medicine.this);
						a0.setTitle("No Records");
						a0.setMessage("There is no record to display !\nPlease add some record first !");
						a0.setPositiveButton("OK", null);
						a0.show();
					}
					else
					{
						final String mednames[]=new String[medlist.size()];
						final String compnames[]=new String[medlist.size()];
						
						for(int i=0;i<medlist.size();i++)
						{
							mednames[i]=medlist.get(i)[0];
							compnames[i]=medlist.get(i)[1];
							System.out.println("Medicine= "+mednames[i]+" Compnay= "+compnames[i]);
						}
						
						AlertDialog.Builder a1=new AlertDialog.Builder(Doctor_Add_Medicine.this);
						a1.setTitle("Select Medicine");
						a1.setItems(mednames, new DialogInterface.OnClickListener()
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								
								final String oldmed=mednames[which];
								final String oldcomp=compnames[which];
								
								AlertDialog.Builder a2=new AlertDialog.Builder(Doctor_Add_Medicine.this);
								LayoutInflater inflater=Doctor_Add_Medicine.this.getLayoutInflater();
								View v=inflater.inflate( R.layout.edit_medicine,null);
								a2.setView(v);
								a2.setTitle("Edit Details");
								
								final EditText editmed=(EditText)v.findViewById(R.id.EditMedName);
								editmed.setText(oldmed);
								
								final EditText editcomp=(EditText)v.findViewById(R.id.EditCompanyName);
								editcomp.setText(oldcomp);
								
								a2.setPositiveButton("Update", new DialogInterface.OnClickListener() 
								{
									
									@Override
									public void onClick(DialogInterface dialog, int which) 
									{
										String umed=editmed.getText().toString();
										String ucomp=editcomp.getText().toString();
										
										if(umed.equals("") || ucomp.equals(""))
										{
											AlertDialog.Builder a3=new AlertDialog.Builder(Doctor_Add_Medicine.this);
											a3.setTitle("Incomplete Information");
											a3.setMessage("Please enter both medicine name and company name before Updating !");
											a3.setPositiveButton("OK", null);
											a3.show();
										}
										else
										{
											Doctor_Medicine dem=new Doctor_Medicine();
											dem.edit_med(email, oldmed, oldcomp, umed, ucomp);
											Toast.makeText(Doctor_Add_Medicine.this, "Update Successful !", 0).show();
										}
									}
								});
								
								a2.setNegativeButton("Cancel", null);
								a2.show();
							}
						});
						a1.setNegativeButton("Cancel", null);
						a1.show();
					}
				}
			});
			
			Button delete=(Button)findViewById(R.id.DeleteMedCompBtn);
			delete.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					
					Doctor_Medicine dfm=new Doctor_Medicine();
					ArrayList<String[]> medlist=dfm.fetch_med(email);
					
					if(medlist.size()<1)
					{
						AlertDialog.Builder a0=new AlertDialog.Builder(Doctor_Add_Medicine.this);
						a0.setTitle("No Records");
						a0.setMessage("There is no record to delete !");
						a0.setPositiveButton("OK", null);
						a0.show();
					}
					else
					{
						final String mednames[]=new String[medlist.size()];
						
						for(int i=0;i<medlist.size();i++)
						{
							mednames[i]=medlist.get(i)[0];
							System.out.println("Medicine= "+mednames[i]);
						}
						
						AlertDialog.Builder a3=new AlertDialog.Builder(Doctor_Add_Medicine.this);
						a3.setTitle("Select Medicine");
						a3.setItems(mednames, new DialogInterface.OnClickListener()
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								final String dmed=mednames[which];
								
								AlertDialog.Builder a4=new AlertDialog.Builder(Doctor_Add_Medicine.this);
								a4.setTitle("Edit Details");
								a4.setMessage("Are you sure you want to delete medicine ("+dmed+") ?");
								a4.setPositiveButton("Yes", new DialogInterface.OnClickListener()
								{
									
									@Override
									public void onClick(DialogInterface dialog, int which) 
									{
										Doctor_Medicine ddm=new Doctor_Medicine();
										ddm.del_med(email, dmed);
										Toast.makeText(Doctor_Add_Medicine.this, "Medicine: "+dmed+" Successfully Deleted !", 0).show();
									}
								});
								a4.setNegativeButton("No", null);
								a4.show();
							}
						});
						a3.setNegativeButton("Cancel", null);
						a3.show();
					}
				}
			});
			
			Button home=(Button)findViewById(R.id.AddMedCompHomeBtn);
			home.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent ihome=new Intent(Doctor_Add_Medicine.this, Doctor_Home.class);
					startActivity(ihome);
				}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add__medicine, menu);
		return true;
	}

	@Override
	public void onBackPressed() 
	{
	    return;
	}
}
