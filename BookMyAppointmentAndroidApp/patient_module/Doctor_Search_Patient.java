package com.example.patient_module;

import java.util.ArrayList;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import Patient_Prescription.Prescriptions;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Doctor_Search_Patient extends Activity
{

	String docemail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_doctor__search__patient);
			
			Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
			
			Intent i=getIntent();
			docemail=i.getStringExtra("docemail");
		
			final RadioGroup rg=(RadioGroup)findViewById(R.id.searchpatrg);
			final RadioButton sbyname=(RadioButton)findViewById(R.id.searchpatbynamebtn);
			final RadioButton sbyemail=(RadioButton)findViewById(R.id.searchpatbyemailbtn);
			final RadioButton sbydisease=(RadioButton)findViewById(R.id.searchpatbydiseasebtn);
			final EditText firstname=(EditText)findViewById(R.id.searchbyname_first);
			final EditText lastname=(EditText)findViewById(R.id.searchbyname_last);
			final EditText patemail=(EditText)findViewById(R.id.searchbyemail);
			final EditText disease=(EditText)findViewById(R.id.searchbydisease);
			final LinearLayout namelayout=(LinearLayout)findViewById(R.id.namelayout);
			
			sbyname.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
				{
					if(isChecked)
					{
						namelayout.setVisibility(1);
						patemail.setVisibility(patemail.INVISIBLE);
						disease.setVisibility(disease.INVISIBLE);
						firstname.setText("");
						lastname.setText("");
						disease.setText("");
						patemail.setText("");
					}
				}
			});
			
			
			sbyemail.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
				{
					if(isChecked)
					{
						patemail.setVisibility(1);
						namelayout.setVisibility(namelayout.INVISIBLE);
						disease.setVisibility(disease.INVISIBLE);
						firstname.setText("");
						lastname.setText("");
						disease.setText("");
						patemail.setText("");
					}
				}
			});
			
			sbydisease.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
				{
					if(isChecked)
					{
						patemail.setVisibility(patemail.INVISIBLE);
						namelayout.setVisibility(namelayout.INVISIBLE);
						disease.setVisibility(1);
						firstname.setText("");
						lastname.setText("");
						disease.setText("");
						patemail.setText("");
					}
				}
			});
			
			Button submit=(Button)findViewById(R.id.SearchPatSubmitBtn);
			submit.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					int id=rg.getCheckedRadioButtonId();
					RadioButton rb=(RadioButton)findViewById(R.id.searchpatbynamebtn);
					
					if(id!=-1)
						rb=(RadioButton)findViewById(id);
					
					if(rb.equals(sbyname))
					{
						String fname=firstname.getText().toString();
						String lname=lastname.getText().toString();
						
						if(fname.equals("") || lname.equals(""))
						{
							AlertDialog.Builder ab=new AlertDialog.Builder(Doctor_Search_Patient.this);
							ab.setTitle("Enter full name");
							ab.setMessage("Please enter both first name and last name !");
							ab.setPositiveButton("Ok", null);
							ab.show();
						}
						else
						{
							final ArrayList<String> emaillist=new ArrayList<String>();
							
							ParseQuery pq=new ParseQuery("Patient");
							pq.whereEqualTo("First_name", fname);
							pq.whereEqualTo("Last_name", lname);
							try
							{
								List<ParseObject> objects=pq.find();
								if(pq!=null)
								{
									for(ParseObject o:objects)
									{
										emaillist.add(o.getString("Email"));
									}
									
									final ArrayList<String> patemaillist=new ArrayList<String>();
								
									for(String s:emaillist)
									{
										Prescriptions ps=new Prescriptions();
										int flag=ps.Verify_Patient(docemail, s);
									
										if(flag==1)
										{
											patemaillist.add(s);
											System.out.println("patemail= "+s);
										}
									}
								
									if(patemaillist.size()<1)
									{
										AlertDialog.Builder ab1=new AlertDialog.Builder(Doctor_Search_Patient.this);
										ab1.setTitle("No Record Found");
										ab1.setMessage("There is no matching record for this name !\nPlease check the spelling !");
										ab1.setPositiveButton("Ok", null);
										ab1.show();
									}
									else
									{
										String patdetails[]=new String[patemaillist.size()];
										int i=0;
									
										for(String patemail: patemaillist)
										{
											ParseQuery q=new ParseQuery("Patient");
											q.whereEqualTo("Email", patemail);
											try
											{
												ParseObject o=q.getFirst();
												if(o!=null)
												{
													String details=o.getString("First_name")+" "+o.getString("Last_name")+"\n"+o.getString("Gender")+", "+o.getNumber("Age")+"\n\nAddress: "+o.getString("Address")+"\nContact: +91-"+o.getString("Mobile")+" / "+o.getString("Email");
													patdetails[i]=details;
													i++;
												}
											}
											catch(ParseException e)
											{
												System.out.println(e.getCode());
											}
										}
									
									
										AlertDialog.Builder ab2=new AlertDialog.Builder(Doctor_Search_Patient.this);
										ab2.setTitle("Select to view history of the patient");
										ab2.setItems(patdetails, new DialogInterface.OnClickListener()
										{
											
											@Override
											public void onClick(DialogInterface dialog, int which) 
											{
												String em=patemaillist.get(which);
											
												Prescriptions pre=new Prescriptions();
												ArrayList<ArrayList<String>> applist=pre.Doctor_Fetch_History(docemail, em);
												
												if(applist.size()<1)
												{
													AlertDialog.Builder ab3=new AlertDialog.Builder(Doctor_Search_Patient.this);
													ab3.setTitle("No Records");
													ab3.setMessage("There is no previous record for this patient !");
													ab3.setPositiveButton("Ok", null);
													ab3.show();
												}
												else
												{
													String history[]=new String[applist.size()];
													int j=0;
												
													for(ArrayList<String> a: applist)
													{
														String appdetails=a.get(2)+"   "+a.get(3)+"\n\nDisease: "+a.get(4)+"\nPrescription:\n"+a.get(5);
														history[j]=appdetails;
														j++;
													}
												
													AlertDialog.Builder ab4=new AlertDialog.Builder(Doctor_Search_Patient.this);
													ab4.setTitle("History");
													ab4.setItems(history, null);
													ab4.setPositiveButton("Ok", null);
													ab4.show();
												}
											}
										});
										ab2.setPositiveButton("Ok", null);
										ab2.show();
									}
								}
							}
							catch(ParseException e)
							{
								System.out.println(e.getCode());
							}
						}
					}
					else if(rb.equals(sbyemail))
					{
						final String pemail=patemail.getText().toString();
						
						if(pemail.equals(""))
						{
							AlertDialog.Builder ab=new AlertDialog.Builder(Doctor_Search_Patient.this);
							ab.setTitle("Blank Field");
							ab.setMessage("Please enter valid email ID !");
							ab.setPositiveButton("Ok", null);
							ab.show();
						}
						else
						{
							Prescriptions ps=new Prescriptions();
							int flag=ps.Verify_Patient(docemail, pemail);
							
							if(flag==0)
							{
								AlertDialog.Builder ab1=new AlertDialog.Builder(Doctor_Search_Patient.this);
								ab1.setTitle("No Record Found");
								ab1.setMessage("There is no matching record for this email ID !!");
								ab1.setPositiveButton("Ok", null);
								ab1.show();
							}
							else
							{
								String details="";
								ParseQuery q=new ParseQuery("Patient");
								q.whereEqualTo("Email", pemail);
								try
								{
									ParseObject o=q.getFirst();
									if(o!=null)
									{
										details=o.getString("First_name")+" "+o.getString("Last_name")+"\n"+o.getString("Gender")+", "+o.getNumber("Age")+"\n\nAddress: "+o.getString("Address")+"\nContact: +91-"+o.getString("Mobile")+" / "+o.getString("Email");
									}
								}
								catch(ParseException e)
								{
									System.out.println(e.getCode());
								}
								
								AlertDialog.Builder ab2=new AlertDialog.Builder(Doctor_Search_Patient.this);
								ab2.setTitle("Patient's Details");
								ab2.setMessage(details);
								ab2.setPositiveButton("View History", new DialogInterface.OnClickListener() 
								{
									
									@Override
									public void onClick(DialogInterface dialog, int which) 
									{
										Prescriptions pre1=new Prescriptions();
										ArrayList<ArrayList<String>> applist=pre1.Doctor_Fetch_History(docemail, pemail);
										
										if(applist.size()<1)
										{
											AlertDialog.Builder ab3=new AlertDialog.Builder(Doctor_Search_Patient.this);
											ab3.setTitle("No Records");
											ab3.setMessage("There is no previous record for this patient !");
											ab3.setPositiveButton("Ok", null);
											ab3.show();
										}
										else
										{
											String history[]=new String[applist.size()];
											int j=0;
											
											for(ArrayList<String> a: applist)
											{
												String appdetails=a.get(2)+"   "+a.get(3)+"\n\nDisease: "+a.get(4)+"\nPrescription:\n"+a.get(5);
												history[j]=appdetails;
												j++;
											}
											
											AlertDialog.Builder ab4=new AlertDialog.Builder(Doctor_Search_Patient.this);
											ab4.setTitle("History");
											ab4.setItems(history, null);
											ab4.setPositiveButton("Ok", null);
											ab4.show();
										}
									}
								});
								ab2.setNegativeButton("Cancel", null);
								ab2.show();
							}
						}
					}
					else
					{
						String prob=disease.getText().toString();
						
						if(prob.equals(""))
						{
							AlertDialog.Builder ab=new AlertDialog.Builder(Doctor_Search_Patient.this);
							ab.setTitle("Enter disease name");
							ab.setMessage("Please enter valid disease name !");
							ab.setPositiveButton("Ok", null);
							ab.show();
						}
						else
						{
							Prescriptions pre2=new Prescriptions();
							final ArrayList<String> pemaillist=pre2.Fetch_History_By_Disease(docemail, prob);
							
							if(pemaillist.size()<1)
							{
								AlertDialog.Builder ab1=new AlertDialog.Builder(Doctor_Search_Patient.this);
								ab1.setTitle("No Record Found");
								ab1.setMessage("There are no matching records for this Disease/Problem !!\nPlease check the spelling !");
								ab1.setPositiveButton("Ok", null);
								ab1.show();
							}
							else
							{
								String patdetails[]=new String[pemaillist.size()];
								int i=0;
								
								for(String pem: pemaillist)
								{
									ParseQuery q=new ParseQuery("Patient");
									q.whereEqualTo("Email", pem);
									try
									{
										ParseObject o=q.getFirst();
										if(o!=null)
										{
											String details=o.getString("First_name")+" "+o.getString("Last_name")+"\n"+o.getString("Gender")+", "+o.getNumber("Age")+"\n\nAddress: "+o.getString("Address")+"\nContact: +91-"+o.getString("Mobile")+" / "+o.getString("Email");
											patdetails[i]=details;
											i++;
										}
									}
									catch(ParseException e)
									{
										System.out.println(e.getCode());
									}
								}
								
								AlertDialog.Builder ab2=new AlertDialog.Builder(Doctor_Search_Patient.this);
								ab2.setTitle("Select to view history of the patient");
								ab2.setItems(patdetails, new DialogInterface.OnClickListener()
								{
									
									@Override
									public void onClick(DialogInterface dialog, int which) 
									{
										String pm=pemaillist.get(which);
										
										Prescriptions pre3=new Prescriptions();
										ArrayList<ArrayList<String>> applist=pre3.Doctor_Fetch_History(docemail, pm);
										
										if(applist.size()<1)
										{
											AlertDialog.Builder ab3=new AlertDialog.Builder(Doctor_Search_Patient.this);
											ab3.setTitle("No Records");
											ab3.setMessage("There is no previous record for this patient !");
											ab3.setPositiveButton("Ok", null);
											ab3.show();
										}
										else
										{
											String history[]=new String[applist.size()];
											int j=0;
											
											for(ArrayList<String> a: applist)
											{
												String appdetails=a.get(2)+"   "+a.get(3)+"\n\nDisease: "+a.get(4)+"\nPrescription:\n"+a.get(5);
												history[j]=appdetails;
												j++;
											}
											
											AlertDialog.Builder ab4=new AlertDialog.Builder(Doctor_Search_Patient.this);
											ab4.setTitle("History");
											ab4.setItems(history, null);
											ab4.setPositiveButton("Ok", null);
											ab4.show();
										}
									}
								});
								ab2.setPositiveButton("Ok", null);
								ab2.show();
							}
					}
					}
				}
			});
			
			Button reset=(Button)findViewById(R.id.SearchPatResetBtn);
			reset.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					firstname.setText("");
					lastname.setText("");
					patemail.setText("");
					disease.setText("");
					rg.clearCheck();
					patemail.setVisibility(patemail.INVISIBLE);
					namelayout.setVisibility(namelayout.INVISIBLE);
					disease.setVisibility(disease.INVISIBLE);
				}
			});
			
			Button home=(Button)findViewById(R.id.SearchPatHomeBtn);
			home.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent ihome=new Intent(Doctor_Search_Patient.this,Doctor_Home.class);
					startActivity(ihome);
				}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater()
				.inflate(R.menu.activity_doctor__search__patient, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() 
	{
	    return;
	}
}
