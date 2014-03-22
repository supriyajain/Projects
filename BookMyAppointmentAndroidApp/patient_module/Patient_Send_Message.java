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
import Patient_Book_Appointment.Patient_Fetch_All_Booked_Doctor_Email_ID;
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

public class Patient_Send_Message extends Activity
{

	String patemail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_patient__send__message);
			
			Intent i=getIntent();
			patemail=i.getStringExtra("patemail");
			
			Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
		
			final EditText to=(EditText)findViewById(R.id.patsendmsgnumb);
			final EditText msgText=(EditText)findViewById(R.id.patsendmsgText);
			
			Button selectdoclist=(Button)findViewById(R.id.patsendmsgSelectUserBtn);
			selectdoclist.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Patient_Fetch_All_Booked_Doctor_Email_ID pfabdn=new Patient_Fetch_All_Booked_Doctor_Email_ID();
					final ArrayList<String> docmoblist=pfabdn.FetchAllDoctorEmail(patemail);
					
					if(docmoblist.size()>0)
					{
						final ArrayList<String> dl=new ArrayList<String>();
						for(String s: docmoblist)
						{
							Block_User vbu=new Block_User();
							int flag=vbu.Verify_block_user(s,patemail);
							if(flag==0)
							{
								ParseQuery q=new ParseQuery("Doctor");
								q.whereEqualTo("Email", s);
								try
								{
									ParseObject o=q.getFirst();
									if(o!=null)
									{
										String detail="Dr. "+o.getString("First_name")+" "+o.getString("Last_name")+" <"+s+">";
										dl.add(detail);
									}
								}
								catch(ParseException e)
								{
									System.out.println(e.getCode());
								}
							}
						}
						
						final String doclist[]=new String[dl.size()];
						int i=0;
						for(String dls:dl)
						{
							doclist[i]=dls;
							i++;
						}
											
						AlertDialog.Builder ab1=new AlertDialog.Builder(Patient_Send_Message.this);
						ab1.setTitle("Select Doctor");
						if(doclist.length>0)
						{
							ab1.setItems(doclist, new DialogInterface.OnClickListener() 
							{
								
								@Override
								public void onClick(DialogInterface dialog, int which) 
								{
									to.setText(doclist[which]);
								}
							});
							ab1.setNegativeButton("Cancel", null);
						}
						else
						{
							ab1.setMessage("No user to display !");
							ab1.setPositiveButton("OK", null);
						}
						ab1.show();
					}
					else
					{
						AlertDialog.Builder ab2=new AlertDialog.Builder(Patient_Send_Message.this);
						ab2.setTitle("No Record");
						ab2.setMessage("Please book a doctor's appointment first to send a message to a doctor !");
						ab2.setPositiveButton("OK", null);
						ab2.show();
					}
				}
			});
			
			Button send=(Button)findViewById(R.id.patsendmsgSendbtn);
			send.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					String number=to.getText().toString();
					String msg=msgText.getText().toString();
					
					if(number.equals("") || msg.equals(""))
					{
						AlertDialog.Builder ab3=new AlertDialog.Builder(Patient_Send_Message.this);
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
						
						String mobreceiver=number.substring(number.indexOf("<")+1, number.indexOf(">"));
							
						Messages im=new Messages();
						im.save_Message(patemail, mobreceiver, idate, time, msg);
						
						ParseQuery pushQuery = ParseInstallation.getQuery();
						pushQuery.whereEqualTo("ID", mobreceiver);
						 
						ParsePush push = new ParsePush();
						push.setQuery(pushQuery);
						push.setMessage("You have new message");
						push.sendInBackground();
						
						Toast.makeText(Patient_Send_Message.this, "Message sent successfully !", 0).show();
						
						Intent ihome1=new Intent(Patient_Send_Message.this,Patient_Home.class);
						startActivity(ihome1);
					}
				}
			});
			
			Button reset=(Button)findViewById(R.id.patsendmsgResetbtn);
			reset.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					to.setText("");
					msgText.setText("");
				}
			});
			
			Button home=(Button)findViewById(R.id.patsendmsgHomebtn);
			home.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent ihome1=new Intent(Patient_Send_Message.this,Patient_Home.class);
					startActivity(ihome1);
				}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_patient__send__message, menu);
		return true;
	}

	@Override
	public void onBackPressed() 
	{
	    return;
	}
}
