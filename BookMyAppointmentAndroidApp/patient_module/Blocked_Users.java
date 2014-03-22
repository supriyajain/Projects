package com.example.patient_module;

import java.util.ArrayList;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import Block_User.Block_User;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Blocked_Users extends Activity 
{

	Intent i;
	String emailuser,detail="";
	int val;
	int errorflag=0;
	ParseException pe=null;
	ArrayList<String> emaillist;
	ListView l;
	ArrayList<String> list=new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blocked__users);
		
		i=getIntent();
		val=i.getIntExtra("Val",0);
		
		if(val==1)
			emailuser=i.getStringExtra("docemail");
		else
			emailuser=i.getStringExtra("patemail");
				
		Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
				
		new progress1().execute();
	}
	
	class progress1 extends AsyncTask<Void, Void, Integer>
	{
	
		ProgressDialog pDialog = new ProgressDialog(Blocked_Users.this);
		
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
		protected Integer doInBackground(Void... arg0) 
		{
					list.add("---> Go to Home");
			
					Block_User bu=new Block_User();
					emaillist=bu.fetch_block_user(emailuser);
				
					if(emaillist.size()>0)
					{
						for(String s: emaillist)
						{
							if(val==1)
							{
								ParseQuery q = new ParseQuery("Patient");
								q.whereEqualTo("Email", s);
								try
								{
									ParseObject po=q.getFirst();
									if (po!= null) 
									{
									   	detail=po.getString("First_name")+" "+po.getString("Last_name");
									   	list.add(detail);
									}
								}
								catch(ParseException e)
								{
									System.out.println(e.getCode());
								}
							}
							else
							{
								ParseQuery q = new ParseQuery("Doctor");
								q.whereEqualTo("Email", s);
								try
								{
									ParseObject po=q.getFirst();
									if (po!= null) 
									{
									   	detail="Dr. "+po.getString("First_name")+" "+po.getString("Last_name");
									   	list.add(detail);
									}
								}
								catch(ParseException e)
								{
									System.out.println(e.getCode());
								}
							}
						}
					}
					else
						errorflag=1;
			
					return errorflag;
		}
		
		@Override
        protected void onPostExecute(Integer f) 
		{
			pDialog.dismiss();
			
			if(f==1)
				Toast.makeText(Blocked_Users.this, "No blocked user to display !!", 0).show();
			
			l=(ListView)findViewById(R.id.Blockeduserslist);
			ArrayAdapter<String> ad=new ArrayAdapter<String>(Blocked_Users.this, android.R.layout.simple_list_item_1,list);
			l.setAdapter(ad);
						
			l.setOnItemClickListener(new OnItemClickListener()
			{
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
						long arg3) 
				{
					if(arg2==0)
					{
						if(val==0)
						{
							Intent ihome=new Intent(Blocked_Users.this,Patient_Home.class);
							startActivity(ihome);
						}
						else
						{
							Intent ihome=new Intent(Blocked_Users.this,Doctor_Home.class);
							startActivity(ihome);
						}
					}	
					else
					{
						final int selected=arg2-1;
						AlertDialog.Builder ab1=new AlertDialog.Builder(Blocked_Users.this);
						ab1.setTitle("Confirm remove");
						ab1.setMessage("Do you want to remove this user from blocked user's list ?");
						ab1.setPositiveButton("Yes", new DialogInterface.OnClickListener()
						{
												
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								int arr[]={selected,arg2};
								new progress2().execute(arr);
							}
						});
						ab1.setNegativeButton("No",null);
						ab1.show();
					}
				}
			});
		}
	}
	
	class progress2 extends AsyncTask<int[], Void, Integer>
	{
	
		ProgressDialog pDialog = new ProgressDialog(Blocked_Users.this);
		
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
		protected Integer doInBackground(int[]... selected) 
		{
			int sel=selected[0][0];
			
			Block_User bu1=new Block_User();
			bu1.del_block_user(emailuser, emaillist.get(sel));
			
			return selected[0][1];
		}
		
		@Override
        protected void onPostExecute(Integer f) 
		{
			pDialog.dismiss();
			int sel=f;
			
			Toast.makeText(Blocked_Users.this, "User successfully removed from blocked user's list !!  ", 0).show();
			
			list.remove(sel);
			l=(ListView)findViewById(R.id.Blockeduserslist);
			ArrayAdapter<String> ad1=new ArrayAdapter<String>(Blocked_Users.this, android.R.layout.simple_list_item_1,list);
			l.setAdapter(ad1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_blocked__users, menu);
		return true;
	}

	@Override
	public void onBackPressed() 
	{
	    return;
	}
}
