package com.example.patient_module;

import java.util.ArrayList;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import Block_User.Block_User;
import Messages.Messages;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Patient_View_Messages extends Activity
{

	String patemail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient__view__messages);
		
		final ParseUser cu = ParseUser.getCurrentUser();
		if (cu != null && cu.getBoolean("emailVerified")) 
		{
			patemail=cu.getUsername();
			
			Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
		
			final ListView l=(ListView)findViewById(R.id.patviewmsglist);
			
			final ArrayList<String> msglist=new ArrayList<String>();
			msglist.add("---> Go to Home");
			msglist.add("---> View Old Messages");
			
			Messages fm=new Messages();
			final ArrayList<ArrayList<String>> unreadmsglist=fm.fetch_Message(patemail, 0);
			
			if(unreadmsglist.size()>0)
			{
				for(ArrayList<String> a: unreadmsglist)
				{
					ParseQuery q=new ParseQuery("Doctor");
					q.whereEqualTo("Email", a.get(0));
					try
					{
						ParseObject o=q.getFirst();
						if(o!=null)
						{
							String detail="Dr. "+o.getString("First_name")+" "+o.getString("Last_name");
							String name=detail+" "+a.get(2)+" "+a.get(3);
							msglist.add(name);
						}
					}
					catch(ParseException e)
					{
						System.out.println(e.getCode());
						if(e.getCode()==ParseException.CONNECTION_FAILED)
						 {
							 AlertDialog.Builder b=new AlertDialog.Builder(Patient_View_Messages.this);
							 b.setTitle("No Connection");
							 b.setMessage("Please check your internet connection !");
							 b.setPositiveButton("OK",null);
							 b.show();
						 }
					}
				}
			}
			else
			{
				Toast.makeText(Patient_View_Messages.this, "No New Message !", 0).show();
			}
				
			ArrayAdapter<String> ad=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,msglist);
			l.setAdapter(ad);
				
			l.setOnItemClickListener(new OnItemClickListener()
			{
	
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int arg2, long arg3) 
				{
					if(arg2==0)
					{
						Intent ihome=new Intent(Patient_View_Messages.this,Patient_Home.class);
						startActivity(ihome);
					}
					else if(arg2==1)
					{
						final ArrayList<String> msglist1=new ArrayList<String>();
						msglist1.add("---> Go to Home");
							
						Messages fm1=new Messages();
						final ArrayList<ArrayList<String>> readmsglist=fm1.fetch_Message(patemail, 1);
							
						if(readmsglist.size()>0)
						{
							for(ArrayList<String> a1: readmsglist)
							{
								ParseQuery q=new ParseQuery("Doctor");
								q.whereEqualTo("Email", a1.get(0));
								try
								{
									ParseObject o=q.getFirst();
									if(o!=null)
									{
										String detail="Dr. "+o.getString("First_name")+" "+o.getString("Last_name");
										String name=detail+" "+a1.get(2)+" "+a1.get(3);
										msglist1.add(name);
									}
								}
								catch(ParseException e)
								{
									System.out.println(e.getCode());
									if(e.getCode()==ParseException.CONNECTION_FAILED)
									 {
										 AlertDialog.Builder b=new AlertDialog.Builder(Patient_View_Messages.this);
										 b.setTitle("No Connection");
										 b.setMessage("Please check your internet connection !");
										 b.setPositiveButton("OK",null);
										 b.show();
									 }
								}
							}
						}
						else
						{
							Toast.makeText(Patient_View_Messages.this, "No message to display !!", 0).show();
						}
						
						final ListView l1=(ListView)findViewById(R.id.patviewmsglist); 
						ArrayAdapter<String> ad2=new ArrayAdapter<String>(Patient_View_Messages.this, android.R.layout.simple_list_item_1,msglist1);
						l1.setAdapter(ad2);
						
						l1.setOnItemClickListener(new OnItemClickListener()
						{
	
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1,
									final int arg2, long arg3) 
							{
								if(arg2==0)
								{
									Intent ihome1=new Intent(Patient_View_Messages.this,Patient_Home.class);
									startActivity(ihome1);
								}
								else
								{
									final int selected1=arg2-1;
									AlertDialog.Builder ab2=new AlertDialog.Builder(Patient_View_Messages.this);
									ab2.setTitle(msglist1.get(arg2));
									ab2.setMessage(readmsglist.get(selected1).get(4));
									ab2.setPositiveButton("OK", null);
									ab2.setNeutralButton("Delete", new DialogInterface.OnClickListener() 
									{
										
										@Override
										public void onClick(DialogInterface dialog, int which) 
										{
											AlertDialog.Builder ab3=new AlertDialog.Builder(Patient_View_Messages.this);
											ab3.setTitle("Confirm Delete");
											ab3.setMessage("Delete this message ?");
											ab3.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
											{
												
												@Override
												public void onClick(DialogInterface dialog, int which) 
												{
													Messages ums=new Messages();
													ums.Msg_Delete(readmsglist.get(selected1).get(6));
													readmsglist.remove(selected1);
													msglist1.remove(arg2);
													ArrayAdapter<String> ad3=new ArrayAdapter<String>(Patient_View_Messages.this, android.R.layout.simple_list_item_1,msglist1);
													l1.setAdapter(ad3);
												}
											});
											ab3.setNegativeButton("No", null);
											ab3.show();
										}
									});
									ab2.setNegativeButton("Block User", new DialogInterface.OnClickListener()
									{
											
										@Override
										public void onClick(DialogInterface dialog, int which) 
										{
											Block_User ibu1=new Block_User();
											ibu1.save_block_user(patemail, readmsglist.get(selected1).get(0));
											Toast.makeText(Patient_View_Messages.this, "User has been blocked !!\nYou will not receive any message from this user in future !!", 0).show();
										}
									});
									ab2.show();
								}
							}
						});
					}
					else
					{
						final int selected=arg2-2;
						AlertDialog.Builder ab1=new AlertDialog.Builder(Patient_View_Messages.this);
						ab1.setTitle(msglist.get(arg2));
						ab1.setMessage(unreadmsglist.get(selected).get(4));
						ab1.setPositiveButton("OK", new DialogInterface.OnClickListener()
						{
								
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								Messages ums=new Messages();
								ums.Flag_Update(unreadmsglist.get(selected).get(0), patemail, unreadmsglist.get(selected).get(2), unreadmsglist.get(selected).get(3));
								msglist.remove(arg2);
								unreadmsglist.remove(selected);
								ArrayAdapter<String> ad1=new ArrayAdapter<String>(Patient_View_Messages.this, android.R.layout.simple_list_item_1,msglist);
								l.setAdapter(ad1);
							}
						});
						ab1.setNeutralButton("Delete", new DialogInterface.OnClickListener() 
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								AlertDialog.Builder ab4=new AlertDialog.Builder(Patient_View_Messages.this);
								ab4.setTitle("Confirm Delete");
								ab4.setMessage("Delete this message ?");
								ab4.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
								{
									
									@Override
									public void onClick(DialogInterface dialog, int which) 
									{
										Messages ums=new Messages();
										ums.Msg_Delete(unreadmsglist.get(selected).get(6));
										unreadmsglist.remove(selected);
										msglist.remove(arg2);
										ArrayAdapter<String> ad3=new ArrayAdapter<String>(Patient_View_Messages.this, android.R.layout.simple_list_item_1,msglist);
										l.setAdapter(ad3);
									}
								});
								ab4.setNegativeButton("No", null);
								ab4.show();
							}
						});
						ab1.setNegativeButton("Block User", new DialogInterface.OnClickListener()
						{
								
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								Block_User ibu=new Block_User();
								ibu.save_block_user(patemail, unreadmsglist.get(selected).get(0));
								
								Messages ums=new Messages();
								ums.Flag_Update(unreadmsglist.get(selected).get(0), patemail, unreadmsglist.get(selected).get(2), unreadmsglist.get(selected).get(3));
								msglist.remove(arg2);
								unreadmsglist.remove(selected);
								ArrayAdapter<String> ad1=new ArrayAdapter<String>(Patient_View_Messages.this, android.R.layout.simple_list_item_1,msglist);
								l.setAdapter(ad1);
								
								Toast.makeText(Patient_View_Messages.this, "User has been blocked !!\nYou will not receive any message from this user in future !!", 0).show();
							}
						});
						ab1.show();
					}
				}
			});
		}
		else
		{
			Intent i2=new Intent(Patient_View_Messages.this, MainActivity.class);
			startActivity(i2);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater()
				.inflate(R.menu.activity_patient__view__messages, menu);
		return true;
	}

	@Override
	public void onBackPressed() 
	{
	    return;
	}
}
