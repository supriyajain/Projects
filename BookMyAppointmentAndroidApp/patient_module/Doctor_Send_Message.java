package com.example.patient_module;

import java.util.ArrayList;
import java.util.Calendar;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import Block_User.Block_User;
import Messages.Messages;
import Patient_Book_Appointment.Doctor_Fetch_All_Patient_Email_ID;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Doctor_Send_Message extends Activity 
{

	String docemail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_doctor__send__message);
		
			Intent i=getIntent();
			docemail=i.getStringExtra("docemail");
		
			Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
		
			final EditText to=(EditText)findViewById(R.id.docsendmsgnumb);
			final EditText msgText=(EditText)findViewById(R.id.docsendmsgText);
			final ArrayList<String> selectedpatemail=new ArrayList<String>();
			
			Button selectpatlist=(Button)findViewById(R.id.docsendmsgSelectUserBtn);
			selectpatlist.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Doctor_Fetch_All_Patient_Email_ID dfapn=new Doctor_Fetch_All_Patient_Email_ID();
					final ArrayList<String> patemaillist=dfapn.FetchAllPatientEmail(docemail);
					
					if(patemaillist.size()>0)
					{
						final ArrayList<String> pl=new ArrayList<String>();
						for(String s: patemaillist)
						{
							Block_User bu=new Block_User();
							int flag=bu.Verify_block_user(s,docemail);
							if(flag==0)
							{
								ParseQuery q=new ParseQuery("Patient");
								q.whereEqualTo("Email", s);
								try
								{
									ParseObject o=q.getFirst();
									if(o!=null)
									{
										String detail=o.getString("First_name")+" "+o.getString("Last_name")+" <"+s+">";
										if(!selectedpatemail.contains(detail))
											pl.add(detail);
									}
								}
								catch(ParseException e)
								{
									System.out.println(e.getCode());
								}
							}
						}
						
						final String patlist[]=new String[pl.size()];
						int i=0;
						for(String pls: pl)
						{
							patlist[i]=pls;
							i++;
						}
											
						AlertDialog.Builder ab1=new AlertDialog.Builder(Doctor_Send_Message.this);
						ab1.setTitle("Select Patient(s)");
						if(patlist.length>0)
						{
							ab1.setMultiChoiceItems(patlist, null, new DialogInterface.OnMultiChoiceClickListener() 
							{
								
								@Override
								public void onClick(DialogInterface dialog, int which, boolean isChecked) 
								{
									if(isChecked)
									{
										if(!selectedpatemail.contains(patlist[which]))
											selectedpatemail.add(patlist[which]);
									}
									else
									{
										if(selectedpatemail.contains(patlist[which]))
											selectedpatemail.remove(patlist[which]);
									}
								}
							});
						
							ab1.setPositiveButton("Ok", new DialogInterface.OnClickListener()
							{
								
								@Override
								public void onClick(DialogInterface dialog, int which) 
								{
									if(selectedpatemail.size()>0)
									{
										String data="";
										for(String str: selectedpatemail)
											data=data+str+", ";
										if(data.endsWith(", "))
											data=data.substring(0, data.lastIndexOf(","));
										
										to.setText(data);
									}
									else
									{
										to.setText("");
									}
								}
							});
							ab1.setNeutralButton("Select All", new DialogInterface.OnClickListener() 
							{
								
								@Override
								public void onClick(DialogInterface dialog, int which) 
								{
									for(String sl: patlist)
									{
										if(!selectedpatemail.contains(sl))
											selectedpatemail.add(sl);
									}
									
									String data="";
									for(String str: selectedpatemail)
										data=data+str+", ";
									if(data.endsWith(", "))
										data=data.substring(0, data.lastIndexOf(","));
									
									to.setText(data);
								}
							});
							ab1.setNegativeButton("Cancel", null);
						}
						else
						{
							ab1.setMessage("No more users !");
							ab1.setPositiveButton("OK", null);
						}
						
						ab1.show();
					}
					else
					{
						AlertDialog.Builder ab2=new AlertDialog.Builder(Doctor_Send_Message.this);
						ab2.setTitle("No Record");
						ab2.setMessage("You have no patient who has booked appointment for you !");
						ab2.setPositiveButton("OK", null);
						ab2.show();
					}
				}
			});
			
			Button remove=(Button)findViewById(R.id.docsendmsgRemoveBtn);
			remove.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					if(selectedpatemail.size()>1)
					{
						final String slist[]=new String[selectedpatemail.size()];
						boolean[] blist=new boolean[selectedpatemail.size()];
						int j=0;
						for(String s: selectedpatemail)
						{
							slist[j]=s;
							blist[j]=Boolean.TRUE;
							j++;
						}
						
						AlertDialog.Builder ab4=new AlertDialog.Builder(Doctor_Send_Message.this);
						ab4.setTitle("Selected User(s)");
						ab4.setMultiChoiceItems(slist, blist, new DialogInterface.OnMultiChoiceClickListener() 
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which, boolean isChecked) 
							{
								if(isChecked)
								{
									if(!selectedpatemail.contains(slist[which]))
										selectedpatemail.add(slist[which]);
								}
								else
								{
									if(selectedpatemail.contains(slist[which]))
									{
										selectedpatemail.remove(slist[which]);
									}
								}
							}
						});
						ab4.setPositiveButton("Ok", new DialogInterface.OnClickListener()
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								if(selectedpatemail.size()>0)
								{
									String data="";
									for(String str: selectedpatemail)
										data=data+str+", ";
									if(data.endsWith(", "))
										data=data.substring(0, data.lastIndexOf(","));
									
									to.setText(data);
								}
								else
								{
									to.setText("");
								}
							}
						});
						ab4.setNegativeButton("Cancel", null);
						ab4.show();
					}
					else
					{
						to.setText("");
						selectedpatemail.remove(0);
					}
				}
			});
			
			Button send=(Button)findViewById(R.id.docsendmsgSendbtn);
			send.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					String number=to.getText().toString();
					String msg=msgText.getText().toString();
					
					if(number.equals("") || msg.equals(""))
					{
						AlertDialog.Builder ab3=new AlertDialog.Builder(Doctor_Send_Message.this);
						ab3.setTitle("Incomplete Information");
						ab3.setMessage("Please enter both receiver's name and message body before sending message!");
						ab3.setPositiveButton("OK", null);
						ab3.show();
					}
					else
					{
						Calendar c=Calendar.getInstance();
						int date=c.get(Calendar.DATE);
						int month=c.get(Calendar.MONTH)+1;
						int year=c.get(Calendar.YEAR);
						String idate=date+"/"+month+"/"+year;
						
						int hour=c.get(Calendar.HOUR);
						int min=c.get(Calendar.MINUTE);
						int shift=c.get(Calendar.AM_PM);
						String sh="";
						if(shift==1)
							sh="PM";
						else
							sh="AM";
						String smin=min+"";
						if(min<10)
							smin="0"+min;
						String time=hour+":"+smin+" "+sh;
						if(hour<10)
							time="0"+time;
						
						for(String snumb: selectedpatemail)
						{
							String mobreceiver=snumb.substring(snumb.indexOf("<")+1, snumb.indexOf(">"));
							
							Messages im=new Messages();
							im.save_Message(docemail, mobreceiver, idate, time, msg);
							
							ParseQuery pushQuery = ParseInstallation.getQuery();
							pushQuery.whereEqualTo("ID", mobreceiver);
							 
							ParsePush push = new ParsePush();
							push.setQuery(pushQuery);
							push.setMessage("You have new message");
							push.sendInBackground();
						}
						
						Toast.makeText(Doctor_Send_Message.this, "Message(s) sent successfully !", 0).show();
						
						Intent ihome1=new Intent(Doctor_Send_Message.this,Doctor_Home.class);
						startActivity(ihome1);
					}
				}
			});
			
			Button reset=(Button)findViewById(R.id.docsendmsgResetbtn);
			reset.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					to.setText("");
					msgText.setText("");
				}
			});
			
			Button home=(Button)findViewById(R.id.docsendmsgHomebtn);
			home.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent ihome=new Intent(Doctor_Send_Message.this,Doctor_Home.class);
					startActivity(ihome);
				}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_doctor__send__message, menu);
		return true;
	}

	@Override
	public void onBackPressed() 
	{
	    return;
	}
}
