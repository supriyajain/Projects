package com.example.patient_module;

import java.util.ArrayList;

import com.example.patient_module.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import Patient_Prescription.Prescriptions;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Patient_Home extends Activity
{
	
	String email,detail="";
	int checkflag=0;
	ParseException pe=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_patient__home);
			
			Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");

			new progress().execute();
			
			Button close=(Button)findViewById(R.id.patHomeCloseBtn);
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
			
			Button VProfile=(Button)findViewById(R.id.viewprofilebtn);
			VProfile.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent i2=new Intent(Patient_Home.this, User_Profile.class);
					i2.putExtra("patemail", email);
					startActivity(i2);
				}
			});
			
			Button book_app=(Button)findViewById(R.id.bookappbtn);
			book_app.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent i=new Intent(Patient_Home.this, Book_Appointment.class);
					i.putExtra("patemail", email);
					startActivity(i);
				}
			});
			
			Button VApp=(Button)findViewById(R.id.Viewappbtn);
			VApp.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent i=new Intent(Patient_Home.this, View_Appointments.class);
					i.putExtra("patemail", email);
					startActivity(i);
				}
			});
			
			Button VPresc=(Button)findViewById(R.id.ViewPrescbtn);
			VPresc.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Prescriptions pfph=new Prescriptions();
					final ArrayList<ArrayList<String>> historylist=pfph.Patient_Fetch_History(email);
					
					final ArrayList<String> docnamelist=new ArrayList<String>();
					final ArrayList<String> docemaillist=new ArrayList<String>();
					
					for(ArrayList<String> a: historylist)
					{
						String docemail=a.get(0);
						
						ParseQuery q=new ParseQuery("Doctor");
						q.whereEqualTo("Email", docemail);
						try
						{
							ParseObject o=q.getFirst();
							if(o!=null)
							{
								String docname="Dr. "+o.getString("First_name")+" "+o.getString("Last_name");
								if(!docnamelist.contains(docname))
								{
									docnamelist.add(docname);
									docemaillist.add(docemail);
								}
							}
						}
						catch(ParseException e)
						{
							if(e.getCode()==ParseException.CONNECTION_FAILED)
							{
								AlertDialog.Builder b=new AlertDialog.Builder(Patient_Home.this);
								b.setTitle("No Connection");
								b.setMessage("Please check your internet connection !");
								b.setPositiveButton("OK",null);
								b.show();
							}
						}
					}
					
					String docnames[]=new String[docnamelist.size()];
					for(int i=0;i<docnamelist.size();i++)
					{
						docnames[i]=docnamelist.get(i);
					}
					
					if(docnamelist.size()<1)
					{
						AlertDialog.Builder ab=new AlertDialog.Builder(Patient_Home.this);
						ab.setTitle("No Prescription");
						ab.setMessage("You have no prescription from any doctor !");
						ab.setPositiveButton("Ok", null);
						ab.show();
					}
					else
					{
						AlertDialog.Builder ab1=new AlertDialog.Builder(Patient_Home.this);
						ab1.setTitle("Select Doctor");
						ab1.setItems(docnames, new DialogInterface.OnClickListener()
						{
						
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								final ArrayList<String> datelist=new ArrayList<String>();
								
								final String selecteddocname=docnamelist.get(which);
								final String selectedemail=docemaillist.get(which);
								
								for(ArrayList<String> s: historylist)
								{
									if(s.get(0).equals(selectedemail))
									{
										String date=s.get(2)+" ("+s.get(3)+")";
										datelist.add(date);
									}
								}
							
								String dates[]=new String[datelist.size()];
								for(int i=0;i<datelist.size();i++)
								{
									dates[i]=datelist.get(i);
								}
							
								AlertDialog.Builder ab2=new AlertDialog.Builder(Patient_Home.this);
								ab2.setTitle("Select Date");
								ab2.setItems(dates, new DialogInterface.OnClickListener()
								{
								
									@Override
									public void onClick(DialogInterface dialog, int which) 
									{
										String selected=datelist.get(which);
										String selecteddate=selected.substring(0, selected.indexOf("(")-1);
										String selectedtime=selected.substring(selected.indexOf("(")+1,selected.indexOf(")"));
									
										String data="";
									
										for(ArrayList<String> s: historylist)
										{
											if(s.get(0).equals(selectedemail) && s.get(2).equals(selecteddate) && s.get(3).equals(selectedtime))
											{
												data="Doctor: "+selecteddocname+"\n"+selecteddate+" ("+selectedtime+")"+"\n\nProblem: "+s.get(4)+"\n\nPrescription:\n"+s.get(5);
												break;
											}
										}
									
										LayoutInflater inflater=Patient_Home.this.getLayoutInflater();
										View v=inflater.inflate( R.layout.patient_view_prescription,null);
									
										TextView t=(TextView)v.findViewById(R.id.PatViewPrescText);
										t.setText(data);
										t.setTextColor(Color.WHITE);
										
										AlertDialog.Builder ab3=new AlertDialog.Builder(Patient_Home.this);
										ab3.setTitle("Prescription");
										ab3.setView(v);
										ab3.setPositiveButton("Ok", null);
										ab3.show();
									}
								});
								ab2.setNegativeButton("Cancel", null);
								ab2.show();
							}
						});
						ab1.setNegativeButton("Cancel", null);
						ab1.show();
					}
				}
			});
			
			Button sendmsg=(Button)findViewById(R.id.patsenddocmsgbtn);
			sendmsg.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent isendmsg=new Intent(Patient_Home.this,Patient_Send_Message.class);
					isendmsg.putExtra("patemail", email);
					startActivity(isendmsg);
				}
			});
			
			Button viewmsg=(Button)findViewById(R.id.patviewmsgbtn);
			viewmsg.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent iviewmsg=new Intent(Patient_Home.this,Patient_View_Messages.class);
					iviewmsg.putExtra("patemail", email);
					startActivity(iviewmsg);
				}
			});
			
			Button viewblockedusers=(Button)findViewById(R.id.patviewblockedusersbtn);
			viewblockedusers.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent iviewusers=new Intent(Patient_Home.this,Blocked_Users.class);
					iviewusers.putExtra("patemail", email);
					iviewusers.putExtra("Val", 0);
					startActivity(iviewusers);
				}
			});
			
			Button logout=(Button)findViewById(R.id.logouthomebtn);
			logout.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					ParseUser.logOut();
					Intent ilogout=new Intent(Patient_Home.this,MainActivity.class);
					startActivity(ilogout);
				}
			});
	}
	
	class progress extends AsyncTask<Void, Void, Integer>
	{
	
		ProgressDialog pDialog = new ProgressDialog(Patient_Home.this);
		
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
			final ParseUser cu = ParseUser.getCurrentUser();
			if (cu != null && cu.getBoolean("emailVerified")) 
			{
				email=cu.getUsername();
			
				ParseQuery q=new ParseQuery("Patient");
				q.whereEqualTo("Email", email);
				try
				{
					ParseObject o=q.getFirst();
					if(o!=null)
					{
						detail=o.getString("First_name");
					}
				}
				catch(ParseException e)
				{
					System.out.println(e.getCode());
					checkflag=1;
					pe=e;
				}
			}
			else
			{
				Intent i2=new Intent(Patient_Home.this, MainActivity.class);
				startActivity(i2);
			}
			
			return checkflag;
		}
		
		@Override
        protected void onPostExecute(Integer f) 
		{
			pDialog.dismiss();
			
			if(checkflag==1)
			{
				if(pe.getCode()==ParseException.CONNECTION_FAILED)
				{
					AlertDialog.Builder b=new AlertDialog.Builder(Patient_Home.this);
					b.setTitle("No Connection");
					b.setMessage("Please check your internet connection !");
					b.setPositiveButton("OK",new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							Intent homeIntent= new Intent(Intent.ACTION_MAIN);
							homeIntent.addCategory(Intent.CATEGORY_HOME);
							homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(homeIntent);
						}
					});
					b.show();
				}
			}
			
			TextView welcometext=(TextView)findViewById(R.id.WelcomeText);
			welcometext.setText("Welcome "+detail+" !");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_patient__home, menu);
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
