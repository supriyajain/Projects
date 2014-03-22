package com.example.patient_module;

import java.util.ArrayList;
import java.util.List;

import Doctor_Visiting_Day_Time.Doctor_Delete_Day_Time;
import Doctor_Visiting_Day_Time.Doctor_Save_Day_time;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;

public class Doctor_Select_Day_Time extends Activity {

	Intent i;
	String day,email;
	
	String mornfromhour="None",mornfrommins="None",morntohour="None",morntomins="None";
	String noonfromhour="None",noonfrommins="None",noontohour="None",noontomins="None";
	String evenfromhour="None",evenfrommins="None",eventohour="None",eventomins="None";
	int mfh,mfm,mth,mtm,nfh,nfm,nth,ntm,efh,efm,eth,etm=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor__select__day__time);
		
		i=getIntent();
		email=i.getStringExtra("Doc_ID");
		
		List<String> lmins=new ArrayList<String>();
		lmins.add("00");
		lmins.add("15");
		lmins.add("30");
		lmins.add("45");
		final ArrayAdapter<String> aMins=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lmins);
		
		List<String> lmorningfromhour=new ArrayList<String>();
		lmorningfromhour.add("None");
		lmorningfromhour.add("05");
		lmorningfromhour.add("06");
		lmorningfromhour.add("07");
		lmorningfromhour.add("08");
		lmorningfromhour.add("09");
		lmorningfromhour.add("10");
		lmorningfromhour.add("11");
		final ArrayAdapter<String> aMorningFromHour=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lmorningfromhour);
		
		List<String> lmorningTohour=new ArrayList<String>();
		lmorningTohour.add("None");
		lmorningTohour.add("06");
		lmorningTohour.add("07");
		lmorningTohour.add("08");
		lmorningTohour.add("09");
		lmorningTohour.add("10");
		lmorningTohour.add("11");
		lmorningTohour.add("12");
		final ArrayAdapter<String> aMorningToHour=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lmorningTohour);
		
		List<String> lnoonfromhour=new ArrayList<String>();
		lnoonfromhour.add("None");
		lnoonfromhour.add("12");
		lnoonfromhour.add("13");
		lnoonfromhour.add("14");
		lnoonfromhour.add("15");
		lnoonfromhour.add("16");
		lnoonfromhour.add("17");
		final ArrayAdapter<String> aNoonFromHour=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lnoonfromhour);
		
		List<String> lnoonTohour=new ArrayList<String>();
		lnoonTohour.add("None");
		lnoonTohour.add("13");
		lnoonTohour.add("14");
		lnoonTohour.add("15");
		lnoonTohour.add("16");
		lnoonTohour.add("17");
		lnoonTohour.add("18");
		final ArrayAdapter<String> aNoonToHour=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lnoonTohour);
		
		List<String> lEveningfromhour=new ArrayList<String>();
		lEveningfromhour.add("None");
		lEveningfromhour.add("18");
		lEveningfromhour.add("19");
		lEveningfromhour.add("20");
		lEveningfromhour.add("21");
		lEveningfromhour.add("22");
		lEveningfromhour.add("23");
		final ArrayAdapter<String> aEveningFromHour=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lEveningfromhour);
		
		List<String> lEveningTohour=new ArrayList<String>();
		lEveningTohour.add("None");
		lEveningTohour.add("19");
		lEveningTohour.add("20");
		lEveningTohour.add("21");
		lEveningTohour.add("22");
		lEveningTohour.add("23");
		lEveningTohour.add("24");
		final ArrayAdapter<String> aEveningToHour=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lEveningTohour);
		
		final CheckBox mon=(CheckBox)findViewById(R.id.MonCkeckbox);
		mon.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if(isChecked)
				{
					AlertDialog.Builder b1=new AlertDialog.Builder(Doctor_Select_Day_Time.this);
					LayoutInflater inflater=Doctor_Select_Day_Time.this.getLayoutInflater();
					View v=inflater.inflate( R.layout.doctor_select_time,null);
					b1.setView(v);
					b1.setTitle("Select Visiting Time");
					
					final Spinner MorningFromHourSpinner = (Spinner)v.findViewById(R.id.MorningFromHour);
			        MorningFromHourSpinner.setAdapter(aMorningFromHour);
			        
			        final Spinner MorningFromMinsSpinner = (Spinner)v.findViewById(R.id.MorningFromMins);
			        MorningFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner MorningToHoursSpinner = (Spinner)v.findViewById(R.id.MorningToHour);
			        MorningToHoursSpinner.setAdapter(aMorningToHour);
			        
			        final Spinner MorningToMinsSpinner = (Spinner)v.findViewById(R.id.MorningToMins);
			        MorningToMinsSpinner.setAdapter(aMins);
			        
			        final Spinner NoonFromHourSpinner = (Spinner)v.findViewById(R.id.NoonFromHour);
			        NoonFromHourSpinner.setAdapter(aNoonFromHour);
			        
			        final Spinner NoonFromMinsSpinner = (Spinner)v.findViewById(R.id.NoonFromMins);
			        NoonFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner NoonToHoursSpinner = (Spinner)v.findViewById(R.id.NoonToHour);
			        NoonToHoursSpinner.setAdapter(aNoonToHour);
			        
			        final Spinner NoonToMinsSpinner = (Spinner)v.findViewById(R.id.NoonToMins);
			        NoonToMinsSpinner.setAdapter(aMins);
			        
			        final Spinner EveningFromHourSpinner = (Spinner)v.findViewById(R.id.EveningFromHour);
			        EveningFromHourSpinner.setAdapter(aEveningFromHour);
			        
			        final Spinner EveningFromMinsSpinner = (Spinner)v.findViewById(R.id.EveningFromMins);
			        EveningFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner EveningToHoursSpinner = (Spinner)v.findViewById(R.id.EveningToHour);
			        EveningToHoursSpinner.setAdapter(aEveningToHour);
			        
			        final Spinner EveningToMinsSpinner = (Spinner)v.findViewById(R.id.EveningToMins);
			        EveningToMinsSpinner.setAdapter(aMins);
			        
			        if(!mornfromhour.equals("None") && !mornfrommins.equals("None") && !morntohour.equals("None") && !morntomins.equals("None"))
			        {
			        	MorningFromHourSpinner.setSelection(mfh);
			        	MorningFromMinsSpinner.setSelection(mfm);
			        	MorningToHoursSpinner.setSelection(mth);
			        	MorningToMinsSpinner.setSelection(mtm);
			        }
			        
			        if(!noonfromhour.equals("None") && !noonfrommins.equals("None") && !noontohour.equals("None") && !noontomins.equals("None"))
			        {
			        	NoonFromHourSpinner.setSelection(nfh);
			        	NoonFromMinsSpinner.setSelection(nfm);
			        	NoonToHoursSpinner.setSelection(nth);
			        	NoonToMinsSpinner.setSelection(ntm);
			        }
			        
			        if(!evenfromhour.equals("None") && !evenfrommins.equals("None") && !eventohour.equals("None") && !eventomins.equals("None"))
			        {
			        	EveningFromHourSpinner.setSelection(efh);
			        	EveningFromMinsSpinner.setSelection(efm);
			        	EveningToHoursSpinner.setSelection(eth);
			        	EveningToMinsSpinner.setSelection(etm);
			        }

			        
			        b1.setPositiveButton("OK",new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							String M1=(String)MorningFromHourSpinner.getSelectedItem();
							String M2=(String)MorningFromMinsSpinner.getSelectedItem();
							String M3=(String)MorningToHoursSpinner.getSelectedItem();
							String M4=(String)MorningToMinsSpinner.getSelectedItem();
							String Morning;
							if(!(M1.equals("None")) && !(M3.equals("None")))
							{
								if(Integer.parseInt(M1)<Integer.parseInt(M3))
								{
									Morning=M1+":"+M2+" - "+M3+":"+M4;
									mornfromhour=M1;
									mfh=MorningFromHourSpinner.getSelectedItemPosition();
									mornfrommins=M2;
									mfm=MorningFromMinsSpinner.getSelectedItemPosition();
									morntohour=M3;
									mth=MorningToHoursSpinner.getSelectedItemPosition();
									morntomins=M4;
									mtm=MorningToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Morning="None";
								}
							}
							else
							{
								Morning="None";
							}
							
							String N1=(String)NoonFromHourSpinner.getSelectedItem();
							String N2=(String)NoonFromMinsSpinner.getSelectedItem();
							String N3=(String)NoonToHoursSpinner.getSelectedItem();
							String N4=(String)NoonToMinsSpinner.getSelectedItem();
							String Afternoon;
							if(!(N1.equals("None")) && !(N3.equals("None")))
							{
								if(Integer.parseInt(N1)<Integer.parseInt(N3))
								{
									Afternoon=N1+":"+N2+" - "+N3+":"+N4;
									noonfromhour=N1;
									nfh=NoonFromHourSpinner.getSelectedItemPosition();
									noonfrommins=N2;
									nfm=NoonFromMinsSpinner.getSelectedItemPosition();
									noontohour=N3;
									nth=NoonToHoursSpinner.getSelectedItemPosition();
									noontomins=N4;
									ntm=NoonToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Afternoon="None";
								}
							}
							else
							{
								Afternoon="None";
							}
							
							String E1=(String)EveningFromHourSpinner.getSelectedItem();
							String E2=(String)EveningFromMinsSpinner.getSelectedItem();
							String E3=(String)EveningToHoursSpinner.getSelectedItem();
							String E4=(String)EveningToMinsSpinner.getSelectedItem();
							String Evening;
							if(!(E1.equals("None")) && !(E3.equals("None")))
							{
								if(Integer.parseInt(E1)<Integer.parseInt(E3))
								{
									Evening=E1+":"+E2+" - "+E3+":"+E4;
									evenfromhour=E1;
									efh=EveningFromHourSpinner.getSelectedItemPosition();
									evenfrommins=E2;
									efm=EveningFromMinsSpinner.getSelectedItemPosition();
									eventohour=E3;
									eth=EveningToHoursSpinner.getSelectedItemPosition();
									eventomins=E4;
									etm=EveningToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Evening="None";
								}
							}
							else
							{
								Evening="None";
							}
							
							day=mon.getText().toString();
							
							Doctor_Save_Day_time dsdt=new Doctor_Save_Day_time();
							dsdt.save_day_time(email, day, Morning, Afternoon, Evening);
							Toast.makeText(Doctor_Select_Day_Time.this, "Time Saved for= "+day+" Morning= "+Morning+" Noon= "+Afternoon+" Evening= "+Evening, 0).show();
						}	
					});
					b1.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							day=mon.getText().toString();
							
							Doctor_Save_Day_time dsdt=new Doctor_Save_Day_time();
							dsdt.save_day_time(email, day, "None", "None", "None");
							Toast.makeText(Doctor_Select_Day_Time.this, "Time Saved for= "+day+" Morning, Noon, Evening= None", 0).show();
						}
					});
					b1.show();
				}
				else
				{
					day=mon.getText().toString();
					
					Doctor_Delete_Day_Time dddt=new Doctor_Delete_Day_Time();
					dddt.Delete_Day_Time(email, day);
					Toast.makeText(Doctor_Select_Day_Time.this, "Time Deleted for= "+day, 0).show();
				}
			}
		});
		
		final CheckBox tue=(CheckBox)findViewById(R.id.TueCkeckbox);
		tue.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if(isChecked)
				{
					AlertDialog.Builder b1=new AlertDialog.Builder(Doctor_Select_Day_Time.this);
					LayoutInflater inflater=Doctor_Select_Day_Time.this.getLayoutInflater();
					View v=inflater.inflate( R.layout.doctor_select_time,null);
					b1.setView(v);
					b1.setTitle("Select Visiting Time");
					
					final Spinner MorningFromHourSpinner = (Spinner)v.findViewById(R.id.MorningFromHour);
			        MorningFromHourSpinner.setAdapter(aMorningFromHour);
			        
			        final Spinner MorningFromMinsSpinner = (Spinner)v.findViewById(R.id.MorningFromMins);
			        MorningFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner MorningToHoursSpinner = (Spinner)v.findViewById(R.id.MorningToHour);
			        MorningToHoursSpinner.setAdapter(aMorningToHour);
			        
			        final Spinner MorningToMinsSpinner = (Spinner)v.findViewById(R.id.MorningToMins);
			        MorningToMinsSpinner.setAdapter(aMins);
			        
			        final Spinner NoonFromHourSpinner = (Spinner)v.findViewById(R.id.NoonFromHour);
			        NoonFromHourSpinner.setAdapter(aNoonFromHour);
			        
			        final Spinner NoonFromMinsSpinner = (Spinner)v.findViewById(R.id.NoonFromMins);
			        NoonFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner NoonToHoursSpinner = (Spinner)v.findViewById(R.id.NoonToHour);
			        NoonToHoursSpinner.setAdapter(aNoonToHour);
			        
			        final Spinner NoonToMinsSpinner = (Spinner)v.findViewById(R.id.NoonToMins);
			        NoonToMinsSpinner.setAdapter(aMins);
			        
			        final Spinner EveningFromHourSpinner = (Spinner)v.findViewById(R.id.EveningFromHour);
			        EveningFromHourSpinner.setAdapter(aEveningFromHour);
			        
			        final Spinner EveningFromMinsSpinner = (Spinner)v.findViewById(R.id.EveningFromMins);
			        EveningFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner EveningToHoursSpinner = (Spinner)v.findViewById(R.id.EveningToHour);
			        EveningToHoursSpinner.setAdapter(aEveningToHour);
			        
			        final Spinner EveningToMinsSpinner = (Spinner)v.findViewById(R.id.EveningToMins);
			        EveningToMinsSpinner.setAdapter(aMins);
			        
			        if(!mornfromhour.equals("None") && !mornfrommins.equals("None") && !morntohour.equals("None") && !morntomins.equals("None"))
			        {
			        	MorningFromHourSpinner.setSelection(mfh);
			        	MorningFromMinsSpinner.setSelection(mfm);
			        	MorningToHoursSpinner.setSelection(mth);
			        	MorningToMinsSpinner.setSelection(mtm);
			        }
			        
			        if(!noonfromhour.equals("None") && !noonfrommins.equals("None") && !noontohour.equals("None") && !noontomins.equals("None"))
			        {
			        	NoonFromHourSpinner.setSelection(nfh);
			        	NoonFromMinsSpinner.setSelection(nfm);
			        	NoonToHoursSpinner.setSelection(nth);
			        	NoonToMinsSpinner.setSelection(ntm);
			        }
			        
			        if(!evenfromhour.equals("None") && !evenfrommins.equals("None") && !eventohour.equals("None") && !eventomins.equals("None"))
			        {
			        	EveningFromHourSpinner.setSelection(efh);
			        	EveningFromMinsSpinner.setSelection(efm);
			        	EveningToHoursSpinner.setSelection(eth);
			        	EveningToMinsSpinner.setSelection(etm);
			        }

			        b1.setPositiveButton("OK",new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							String M1=(String)MorningFromHourSpinner.getSelectedItem();
							String M2=(String)MorningFromMinsSpinner.getSelectedItem();
							String M3=(String)MorningToHoursSpinner.getSelectedItem();
							String M4=(String)MorningToMinsSpinner.getSelectedItem();
							String Morning;
							if(!(M1.equals("None")) && !(M3.equals("None")))
							{
								if(Integer.parseInt(M1)<Integer.parseInt(M3))
								{
									Morning=M1+":"+M2+" - "+M3+":"+M4;
									mornfromhour=M1;
									mfh=MorningFromHourSpinner.getSelectedItemPosition();
									mornfrommins=M2;
									mfm=MorningFromMinsSpinner.getSelectedItemPosition();
									morntohour=M3;
									mth=MorningToHoursSpinner.getSelectedItemPosition();
									morntomins=M4;
									mtm=MorningToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Morning="None";
								}
							}
							else
							{
								Morning="None";
							}
							
							String N1=(String)NoonFromHourSpinner.getSelectedItem();
							String N2=(String)NoonFromMinsSpinner.getSelectedItem();
							String N3=(String)NoonToHoursSpinner.getSelectedItem();
							String N4=(String)NoonToMinsSpinner.getSelectedItem();
							String Afternoon;
							if(!(N1.equals("None")) && !(N3.equals("None")))
							{
								if(Integer.parseInt(N1)<Integer.parseInt(N3))
								{
									Afternoon=N1+":"+N2+" - "+N3+":"+N4;
									noonfromhour=N1;
									nfh=NoonFromHourSpinner.getSelectedItemPosition();
									noonfrommins=N2;
									nfm=NoonFromMinsSpinner.getSelectedItemPosition();
									noontohour=N3;
									nth=NoonToHoursSpinner.getSelectedItemPosition();
									noontomins=N4;
									ntm=NoonToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Afternoon="None";
								}
							}
							else
							{
								Afternoon="None";
							}
							
							String E1=(String)EveningFromHourSpinner.getSelectedItem();
							String E2=(String)EveningFromMinsSpinner.getSelectedItem();
							String E3=(String)EveningToHoursSpinner.getSelectedItem();
							String E4=(String)EveningToMinsSpinner.getSelectedItem();
							String Evening;
							if(!(E1.equals("None")) && !(E3.equals("None")))
							{
								if(Integer.parseInt(E1)<Integer.parseInt(E3))
								{
									Evening=E1+":"+E2+" - "+E3+":"+E4;
									evenfromhour=E1;
									efh=EveningFromHourSpinner.getSelectedItemPosition();
									evenfrommins=E2;
									efm=EveningFromMinsSpinner.getSelectedItemPosition();
									eventohour=E3;
									eth=EveningToHoursSpinner.getSelectedItemPosition();
									eventomins=E4;
									etm=EveningToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Evening="None";
								}
							}
							else
							{
								Evening="None";
							}
							
							day=tue.getText().toString();
							Doctor_Save_Day_time dsdt=new Doctor_Save_Day_time();
							dsdt.save_day_time(email, day, Morning, Afternoon, Evening);
							Toast.makeText(Doctor_Select_Day_Time.this, "Time Saved for= "+day+" Morning= "+Morning+" Noon= "+Afternoon+" Evening= "+Evening, 0).show();
						}	
					});
					b1.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							day=tue.getText().toString();
							Doctor_Save_Day_time dsdt=new Doctor_Save_Day_time();
							dsdt.save_day_time(email, day, "None", "None", "None");
							Toast.makeText(Doctor_Select_Day_Time.this, "Time Saved for= "+day+" Morning, Noon, Evening= None", 0).show();
						}
					});
					b1.show();
				}
				else
				{
					day=tue.getText().toString();
					
					Doctor_Delete_Day_Time dddt=new Doctor_Delete_Day_Time();
					dddt.Delete_Day_Time(email, day);
					Toast.makeText(Doctor_Select_Day_Time.this, "Time deleted for= "+day, 0).show();
				}
			}
		});
		
		final CheckBox wed=(CheckBox)findViewById(R.id.WedCkeckbox);
		wed.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if(isChecked)
				{
					AlertDialog.Builder b1=new AlertDialog.Builder(Doctor_Select_Day_Time.this);
					LayoutInflater inflater=Doctor_Select_Day_Time.this.getLayoutInflater();
					View v=inflater.inflate( R.layout.doctor_select_time,null);
					b1.setView(v);
					b1.setTitle("Select Visiting Time");
					
					final Spinner MorningFromHourSpinner = (Spinner)v.findViewById(R.id.MorningFromHour);
			        MorningFromHourSpinner.setAdapter(aMorningFromHour);
			        
			        final Spinner MorningFromMinsSpinner = (Spinner)v.findViewById(R.id.MorningFromMins);
			        MorningFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner MorningToHoursSpinner = (Spinner)v.findViewById(R.id.MorningToHour);
			        MorningToHoursSpinner.setAdapter(aMorningToHour);
			        
			        final Spinner MorningToMinsSpinner = (Spinner)v.findViewById(R.id.MorningToMins);
			        MorningToMinsSpinner.setAdapter(aMins);
			        
			        final Spinner NoonFromHourSpinner = (Spinner)v.findViewById(R.id.NoonFromHour);
			        NoonFromHourSpinner.setAdapter(aNoonFromHour);
			        
			        final Spinner NoonFromMinsSpinner = (Spinner)v.findViewById(R.id.NoonFromMins);
			        NoonFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner NoonToHoursSpinner = (Spinner)v.findViewById(R.id.NoonToHour);
			        NoonToHoursSpinner.setAdapter(aNoonToHour);
			        
			        final Spinner NoonToMinsSpinner = (Spinner)v.findViewById(R.id.NoonToMins);
			        NoonToMinsSpinner.setAdapter(aMins);
			        
			        final Spinner EveningFromHourSpinner = (Spinner)v.findViewById(R.id.EveningFromHour);
			        EveningFromHourSpinner.setAdapter(aEveningFromHour);
			        
			        final Spinner EveningFromMinsSpinner = (Spinner)v.findViewById(R.id.EveningFromMins);
			        EveningFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner EveningToHoursSpinner = (Spinner)v.findViewById(R.id.EveningToHour);
			        EveningToHoursSpinner.setAdapter(aEveningToHour);
			        
			        final Spinner EveningToMinsSpinner = (Spinner)v.findViewById(R.id.EveningToMins);
			        EveningToMinsSpinner.setAdapter(aMins);
			        
			        if(!mornfromhour.equals("None") && !mornfrommins.equals("None") && !morntohour.equals("None") && !morntomins.equals("None"))
			        {
			        	MorningFromHourSpinner.setSelection(mfh);
			        	MorningFromMinsSpinner.setSelection(mfm);
			        	MorningToHoursSpinner.setSelection(mth);
			        	MorningToMinsSpinner.setSelection(mtm);
			        }
			        
			        if(!noonfromhour.equals("None") && !noonfrommins.equals("None") && !noontohour.equals("None") && !noontomins.equals("None"))
			        {
			        	NoonFromHourSpinner.setSelection(nfh);
			        	NoonFromMinsSpinner.setSelection(nfm);
			        	NoonToHoursSpinner.setSelection(nth);
			        	NoonToMinsSpinner.setSelection(ntm);
			        }
			        
			        if(!evenfromhour.equals("None") && !evenfrommins.equals("None") && !eventohour.equals("None") && !eventomins.equals("None"))
			        {
			        	EveningFromHourSpinner.setSelection(efh);
			        	EveningFromMinsSpinner.setSelection(efm);
			        	EveningToHoursSpinner.setSelection(eth);
			        	EveningToMinsSpinner.setSelection(etm);
			        }

			        b1.setPositiveButton("OK",new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							String M1=(String)MorningFromHourSpinner.getSelectedItem();
							String M2=(String)MorningFromMinsSpinner.getSelectedItem();
							String M3=(String)MorningToHoursSpinner.getSelectedItem();
							String M4=(String)MorningToMinsSpinner.getSelectedItem();
							String Morning;
							if(!(M1.equals("None")) && !(M3.equals("None")))
							{
								if(Integer.parseInt(M1)<Integer.parseInt(M3))
								{
									Morning=M1+":"+M2+" - "+M3+":"+M4;
									mornfromhour=M1;
									mfh=MorningFromHourSpinner.getSelectedItemPosition();
									mornfrommins=M2;
									mfm=MorningFromMinsSpinner.getSelectedItemPosition();
									morntohour=M3;
									mth=MorningToHoursSpinner.getSelectedItemPosition();
									morntomins=M4;
									mtm=MorningToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Morning="None";
								}
							}
							else
							{
								Morning="None";
							}
							
							String N1=(String)NoonFromHourSpinner.getSelectedItem();
							String N2=(String)NoonFromMinsSpinner.getSelectedItem();
							String N3=(String)NoonToHoursSpinner.getSelectedItem();
							String N4=(String)NoonToMinsSpinner.getSelectedItem();
							String Afternoon;
							if(!(N1.equals("None")) && !(N3.equals("None")))
							{
								if(Integer.parseInt(N1)<Integer.parseInt(N3))
								{
									Afternoon=N1+":"+N2+" - "+N3+":"+N4;
									noonfromhour=N1;
									nfh=NoonFromHourSpinner.getSelectedItemPosition();
									noonfrommins=N2;
									nfm=NoonFromMinsSpinner.getSelectedItemPosition();
									noontohour=N3;
									nth=NoonToHoursSpinner.getSelectedItemPosition();
									noontomins=N4;
									ntm=NoonToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Afternoon="None";
								}
							}
							else
							{
								Afternoon="None";
							}
							
							String E1=(String)EveningFromHourSpinner.getSelectedItem();
							String E2=(String)EveningFromMinsSpinner.getSelectedItem();
							String E3=(String)EveningToHoursSpinner.getSelectedItem();
							String E4=(String)EveningToMinsSpinner.getSelectedItem();
							String Evening;
							if(!(E1.equals("None")) && !(E3.equals("None")))
							{
								if(Integer.parseInt(E1)<Integer.parseInt(E3))
								{
									Evening=E1+":"+E2+" - "+E3+":"+E4;
									evenfromhour=E1;
									efh=EveningFromHourSpinner.getSelectedItemPosition();
									evenfrommins=E2;
									efm=EveningFromMinsSpinner.getSelectedItemPosition();
									eventohour=E3;
									eth=EveningToHoursSpinner.getSelectedItemPosition();
									eventomins=E4;
									etm=EveningToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Evening="None";
								}
							}
							else
							{
								Evening="None";
							}
							
							day=wed.getText().toString();
							Doctor_Save_Day_time dsdt=new Doctor_Save_Day_time();
							dsdt.save_day_time(email, day, Morning, Afternoon, Evening);
							Toast.makeText(Doctor_Select_Day_Time.this, "Time Saved for= "+day+" Morning= "+Morning+" Noon= "+Afternoon+" Evening= "+Evening, 0).show();
						}	
					});
					b1.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							day=wed.getText().toString();
							Doctor_Save_Day_time dsdt=new Doctor_Save_Day_time();
							dsdt.save_day_time(email, day, "None", "None", "None");
							Toast.makeText(Doctor_Select_Day_Time.this, "Time Saved for= "+day+" Morning, Noon, Evening= None", 0).show();
						}
					});
					b1.show();
				}
				else
				{
					day=wed.getText().toString();
					
					Doctor_Delete_Day_Time dddt=new Doctor_Delete_Day_Time();
					dddt.Delete_Day_Time(email, day);
					Toast.makeText(Doctor_Select_Day_Time.this, "Time deleted for= "+day, 0).show();
				}
			}
		});
		
		final CheckBox thus=(CheckBox)findViewById(R.id.ThusCkeckbox);
		thus.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if(isChecked)
				{
					AlertDialog.Builder b1=new AlertDialog.Builder(Doctor_Select_Day_Time.this);
					LayoutInflater inflater=Doctor_Select_Day_Time.this.getLayoutInflater();
					View v=inflater.inflate( R.layout.doctor_select_time,null);
					b1.setView(v);
					b1.setTitle("Select Visiting Time");
					
					final Spinner MorningFromHourSpinner = (Spinner)v.findViewById(R.id.MorningFromHour);
			        MorningFromHourSpinner.setAdapter(aMorningFromHour);
			        
			        final Spinner MorningFromMinsSpinner = (Spinner)v.findViewById(R.id.MorningFromMins);
			        MorningFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner MorningToHoursSpinner = (Spinner)v.findViewById(R.id.MorningToHour);
			        MorningToHoursSpinner.setAdapter(aMorningToHour);
			        
			        final Spinner MorningToMinsSpinner = (Spinner)v.findViewById(R.id.MorningToMins);
			        MorningToMinsSpinner.setAdapter(aMins);
			        
			        final Spinner NoonFromHourSpinner = (Spinner)v.findViewById(R.id.NoonFromHour);
			        NoonFromHourSpinner.setAdapter(aNoonFromHour);
			        
			        final Spinner NoonFromMinsSpinner = (Spinner)v.findViewById(R.id.NoonFromMins);
			        NoonFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner NoonToHoursSpinner = (Spinner)v.findViewById(R.id.NoonToHour);
			        NoonToHoursSpinner.setAdapter(aNoonToHour);
			        
			        final Spinner NoonToMinsSpinner = (Spinner)v.findViewById(R.id.NoonToMins);
			        NoonToMinsSpinner.setAdapter(aMins);
			        
			        final Spinner EveningFromHourSpinner = (Spinner)v.findViewById(R.id.EveningFromHour);
			        EveningFromHourSpinner.setAdapter(aEveningFromHour);
			        
			        final Spinner EveningFromMinsSpinner = (Spinner)v.findViewById(R.id.EveningFromMins);
			        EveningFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner EveningToHoursSpinner = (Spinner)v.findViewById(R.id.EveningToHour);
			        EveningToHoursSpinner.setAdapter(aEveningToHour);
			        
			        final Spinner EveningToMinsSpinner = (Spinner)v.findViewById(R.id.EveningToMins);
			        EveningToMinsSpinner.setAdapter(aMins);
			        
			        if(!mornfromhour.equals("None") && !mornfrommins.equals("None") && !morntohour.equals("None") && !morntomins.equals("None"))
			        {
			        	MorningFromHourSpinner.setSelection(mfh);
			        	MorningFromMinsSpinner.setSelection(mfm);
			        	MorningToHoursSpinner.setSelection(mth);
			        	MorningToMinsSpinner.setSelection(mtm);
			        }
			        
			        if(!noonfromhour.equals("None") && !noonfrommins.equals("None") && !noontohour.equals("None") && !noontomins.equals("None"))
			        {
			        	NoonFromHourSpinner.setSelection(nfh);
			        	NoonFromMinsSpinner.setSelection(nfm);
			        	NoonToHoursSpinner.setSelection(nth);
			        	NoonToMinsSpinner.setSelection(ntm);
			        }
			        
			        if(!evenfromhour.equals("None") && !evenfrommins.equals("None") && !eventohour.equals("None") && !eventomins.equals("None"))
			        {
			        	EveningFromHourSpinner.setSelection(efh);
			        	EveningFromMinsSpinner.setSelection(efm);
			        	EveningToHoursSpinner.setSelection(eth);
			        	EveningToMinsSpinner.setSelection(etm);
			        }

			        b1.setPositiveButton("OK",new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							String M1=(String)MorningFromHourSpinner.getSelectedItem();
							String M2=(String)MorningFromMinsSpinner.getSelectedItem();
							String M3=(String)MorningToHoursSpinner.getSelectedItem();
							String M4=(String)MorningToMinsSpinner.getSelectedItem();
							String Morning;
							if(!(M1.equals("None")) && !(M3.equals("None")))
							{
								if(Integer.parseInt(M1)<Integer.parseInt(M3))
								{
									Morning=M1+":"+M2+" - "+M3+":"+M4;
									mornfromhour=M1;
									mfh=MorningFromHourSpinner.getSelectedItemPosition();
									mornfrommins=M2;
									mfm=MorningFromMinsSpinner.getSelectedItemPosition();
									morntohour=M3;
									mth=MorningToHoursSpinner.getSelectedItemPosition();
									morntomins=M4;
									mtm=MorningToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Morning="None";
								}
							}
							else
							{
								Morning="None";
							}
							
							String N1=(String)NoonFromHourSpinner.getSelectedItem();
							String N2=(String)NoonFromMinsSpinner.getSelectedItem();
							String N3=(String)NoonToHoursSpinner.getSelectedItem();
							String N4=(String)NoonToMinsSpinner.getSelectedItem();
							String Afternoon;
							if(!(N1.equals("None")) && !(N3.equals("None")))
							{
								if(Integer.parseInt(N1)<Integer.parseInt(N3))
								{
									Afternoon=N1+":"+N2+" - "+N3+":"+N4;
									noonfromhour=N1;
									nfh=NoonFromHourSpinner.getSelectedItemPosition();
									noonfrommins=N2;
									nfm=NoonFromMinsSpinner.getSelectedItemPosition();
									noontohour=N3;
									nth=NoonToHoursSpinner.getSelectedItemPosition();
									noontomins=N4;
									ntm=NoonToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Afternoon="None";
								}
							}
							else
							{
								Afternoon="None";
							}
							
							String E1=(String)EveningFromHourSpinner.getSelectedItem();
							String E2=(String)EveningFromMinsSpinner.getSelectedItem();
							String E3=(String)EveningToHoursSpinner.getSelectedItem();
							String E4=(String)EveningToMinsSpinner.getSelectedItem();
							String Evening;
							if(!(E1.equals("None")) && !(E3.equals("None")))
							{
								if(Integer.parseInt(E1)<Integer.parseInt(E3))
								{
									Evening=E1+":"+E2+" - "+E3+":"+E4;
									evenfromhour=E1;
									efh=EveningFromHourSpinner.getSelectedItemPosition();
									evenfrommins=E2;
									efm=EveningFromMinsSpinner.getSelectedItemPosition();
									eventohour=E3;
									eth=EveningToHoursSpinner.getSelectedItemPosition();
									eventomins=E4;
									etm=EveningToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Evening="None";
								}
							}
							else
							{
								Evening="None";
							}
							
							day=thus.getText().toString();
							Doctor_Save_Day_time dsdt=new Doctor_Save_Day_time();
							dsdt.save_day_time(email, day, Morning, Afternoon, Evening);
							Toast.makeText(Doctor_Select_Day_Time.this, "Time Saved for= "+day+" Morning= "+Morning+" Noon= "+Afternoon+" Evening= "+Evening, 0).show();
						}	
					});
					b1.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							day=thus.getText().toString();
							Doctor_Save_Day_time dsdt=new Doctor_Save_Day_time();
							dsdt.save_day_time(email, day, "None", "None", "None");
							Toast.makeText(Doctor_Select_Day_Time.this, "Time Saved for= "+day+" Morning, Noon, Evening= None", 0).show();
						}
					});
					b1.show();
				}
				else
				{
					day=thus.getText().toString();
					Doctor_Delete_Day_Time dddt=new Doctor_Delete_Day_Time();
					dddt.Delete_Day_Time(email, day);
					Toast.makeText(Doctor_Select_Day_Time.this, "Time deleted for= "+day, 0).show();
				}
			}
		});
		
		final CheckBox fri=(CheckBox)findViewById(R.id.FriCkeckbox);
		fri.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if(isChecked)
				{
					AlertDialog.Builder b1=new AlertDialog.Builder(Doctor_Select_Day_Time.this);
					LayoutInflater inflater=Doctor_Select_Day_Time.this.getLayoutInflater();
					View v=inflater.inflate( R.layout.doctor_select_time,null);
					b1.setView(v);
					b1.setTitle("Select Visiting Time");
					
					final Spinner MorningFromHourSpinner = (Spinner)v.findViewById(R.id.MorningFromHour);
			        MorningFromHourSpinner.setAdapter(aMorningFromHour);
			        
			        final Spinner MorningFromMinsSpinner = (Spinner)v.findViewById(R.id.MorningFromMins);
			        MorningFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner MorningToHoursSpinner = (Spinner)v.findViewById(R.id.MorningToHour);
			        MorningToHoursSpinner.setAdapter(aMorningToHour);
			        
			        final Spinner MorningToMinsSpinner = (Spinner)v.findViewById(R.id.MorningToMins);
			        MorningToMinsSpinner.setAdapter(aMins);
			        
			        final Spinner NoonFromHourSpinner = (Spinner)v.findViewById(R.id.NoonFromHour);
			        NoonFromHourSpinner.setAdapter(aNoonFromHour);
			        
			        final Spinner NoonFromMinsSpinner = (Spinner)v.findViewById(R.id.NoonFromMins);
			        NoonFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner NoonToHoursSpinner = (Spinner)v.findViewById(R.id.NoonToHour);
			        NoonToHoursSpinner.setAdapter(aNoonToHour);
			        
			        final Spinner NoonToMinsSpinner = (Spinner)v.findViewById(R.id.NoonToMins);
			        NoonToMinsSpinner.setAdapter(aMins);
			        
			        final Spinner EveningFromHourSpinner = (Spinner)v.findViewById(R.id.EveningFromHour);
			        EveningFromHourSpinner.setAdapter(aEveningFromHour);
			        
			        final Spinner EveningFromMinsSpinner = (Spinner)v.findViewById(R.id.EveningFromMins);
			        EveningFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner EveningToHoursSpinner = (Spinner)v.findViewById(R.id.EveningToHour);
			        EveningToHoursSpinner.setAdapter(aEveningToHour);
			        
			        final Spinner EveningToMinsSpinner = (Spinner)v.findViewById(R.id.EveningToMins);
			        EveningToMinsSpinner.setAdapter(aMins);
			        
			        if(!mornfromhour.equals("None") && !mornfrommins.equals("None") && !morntohour.equals("None") && !morntomins.equals("None"))
			        {
			        	MorningFromHourSpinner.setSelection(mfh);
			        	MorningFromMinsSpinner.setSelection(mfm);
			        	MorningToHoursSpinner.setSelection(mth);
			        	MorningToMinsSpinner.setSelection(mtm);
			        }
			        
			        if(!noonfromhour.equals("None") && !noonfrommins.equals("None") && !noontohour.equals("None") && !noontomins.equals("None"))
			        {
			        	NoonFromHourSpinner.setSelection(nfh);
			        	NoonFromMinsSpinner.setSelection(nfm);
			        	NoonToHoursSpinner.setSelection(nth);
			        	NoonToMinsSpinner.setSelection(ntm);
			        }
			        
			        if(!evenfromhour.equals("None") && !evenfrommins.equals("None") && !eventohour.equals("None") && !eventomins.equals("None"))
			        {
			        	EveningFromHourSpinner.setSelection(efh);
			        	EveningFromMinsSpinner.setSelection(efm);
			        	EveningToHoursSpinner.setSelection(eth);
			        	EveningToMinsSpinner.setSelection(etm);
			        }

			        b1.setPositiveButton("OK",new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							String M1=(String)MorningFromHourSpinner.getSelectedItem();
							String M2=(String)MorningFromMinsSpinner.getSelectedItem();
							String M3=(String)MorningToHoursSpinner.getSelectedItem();
							String M4=(String)MorningToMinsSpinner.getSelectedItem();
							String Morning;
							if(!(M1.equals("None")) && !(M3.equals("None")))
							{
								if(Integer.parseInt(M1)<Integer.parseInt(M3))
								{
									Morning=M1+":"+M2+" - "+M3+":"+M4;
									mornfromhour=M1;
									mfh=MorningFromHourSpinner.getSelectedItemPosition();
									mornfrommins=M2;
									mfm=MorningFromMinsSpinner.getSelectedItemPosition();
									morntohour=M3;
									mth=MorningToHoursSpinner.getSelectedItemPosition();
									morntomins=M4;
									mtm=MorningToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Morning="None";
								}
							}
							else
							{
								Morning="None";
							}
							
							String N1=(String)NoonFromHourSpinner.getSelectedItem();
							String N2=(String)NoonFromMinsSpinner.getSelectedItem();
							String N3=(String)NoonToHoursSpinner.getSelectedItem();
							String N4=(String)NoonToMinsSpinner.getSelectedItem();
							String Afternoon;
							if(!(N1.equals("None")) && !(N3.equals("None")))
							{
								if(Integer.parseInt(N1)<Integer.parseInt(N3))
								{
									Afternoon=N1+":"+N2+" - "+N3+":"+N4;
									noonfromhour=N1;
									nfh=NoonFromHourSpinner.getSelectedItemPosition();
									noonfrommins=N2;
									nfm=NoonFromMinsSpinner.getSelectedItemPosition();
									noontohour=N3;
									nth=NoonToHoursSpinner.getSelectedItemPosition();
									noontomins=N4;
									ntm=NoonToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Afternoon="None";
								}
							}
							else
							{
								Afternoon="None";
							}
							
							String E1=(String)EveningFromHourSpinner.getSelectedItem();
							String E2=(String)EveningFromMinsSpinner.getSelectedItem();
							String E3=(String)EveningToHoursSpinner.getSelectedItem();
							String E4=(String)EveningToMinsSpinner.getSelectedItem();
							String Evening;
							if(!(E1.equals("None")) && !(E3.equals("None")))
							{
								if(Integer.parseInt(E1)<Integer.parseInt(E3))
								{
									Evening=E1+":"+E2+" - "+E3+":"+E4;
									evenfromhour=E1;
									efh=EveningFromHourSpinner.getSelectedItemPosition();
									evenfrommins=E2;
									efm=EveningFromMinsSpinner.getSelectedItemPosition();
									eventohour=E3;
									eth=EveningToHoursSpinner.getSelectedItemPosition();
									eventomins=E4;
									etm=EveningToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Evening="None";
								}
							}
							else
							{
								Evening="None";
							}
							
							day=fri.getText().toString();
							Doctor_Save_Day_time dsdt=new Doctor_Save_Day_time();
							dsdt.save_day_time(email, day, Morning, Afternoon, Evening);
							Toast.makeText(Doctor_Select_Day_Time.this, "Time Saved for= "+day+" Morning= "+Morning+" Noon= "+Afternoon+" Evening= "+Evening, 0).show();
						}	
					});
					b1.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							day=fri.getText().toString();
							Doctor_Save_Day_time dsdt=new Doctor_Save_Day_time();
							dsdt.save_day_time(email, day, "None", "None", "None");
							Toast.makeText(Doctor_Select_Day_Time.this, "Time Saved for= "+day+" Morning, Noon, Evening= None", 0).show();
						}
					});
					b1.show();
				}
				else
				{
					day=fri.getText().toString();
					Doctor_Delete_Day_Time dddt=new Doctor_Delete_Day_Time();
					dddt.Delete_Day_Time(email, day);
					Toast.makeText(Doctor_Select_Day_Time.this, "Time deleted for= "+day, 0).show();
				}
			}
		});
		
		final CheckBox sat=(CheckBox)findViewById(R.id.SatCkeckbox);
		sat.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if(isChecked)
				{
					AlertDialog.Builder b1=new AlertDialog.Builder(Doctor_Select_Day_Time.this);
					LayoutInflater inflater=Doctor_Select_Day_Time.this.getLayoutInflater();
					View v=inflater.inflate( R.layout.doctor_select_time,null);
					b1.setView(v);
					b1.setTitle("Select Visiting Time");
					
					final Spinner MorningFromHourSpinner = (Spinner)v.findViewById(R.id.MorningFromHour);
			        MorningFromHourSpinner.setAdapter(aMorningFromHour);
			        
			        final Spinner MorningFromMinsSpinner = (Spinner)v.findViewById(R.id.MorningFromMins);
			        MorningFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner MorningToHoursSpinner = (Spinner)v.findViewById(R.id.MorningToHour);
			        MorningToHoursSpinner.setAdapter(aMorningToHour);
			        
			        final Spinner MorningToMinsSpinner = (Spinner)v.findViewById(R.id.MorningToMins);
			        MorningToMinsSpinner.setAdapter(aMins);
			        
			        final Spinner NoonFromHourSpinner = (Spinner)v.findViewById(R.id.NoonFromHour);
			        NoonFromHourSpinner.setAdapter(aNoonFromHour);
			        
			        final Spinner NoonFromMinsSpinner = (Spinner)v.findViewById(R.id.NoonFromMins);
			        NoonFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner NoonToHoursSpinner = (Spinner)v.findViewById(R.id.NoonToHour);
			        NoonToHoursSpinner.setAdapter(aNoonToHour);
			        
			        final Spinner NoonToMinsSpinner = (Spinner)v.findViewById(R.id.NoonToMins);
			        NoonToMinsSpinner.setAdapter(aMins);
			        
			        final Spinner EveningFromHourSpinner = (Spinner)v.findViewById(R.id.EveningFromHour);
			        EveningFromHourSpinner.setAdapter(aEveningFromHour);
			        
			        final Spinner EveningFromMinsSpinner = (Spinner)v.findViewById(R.id.EveningFromMins);
			        EveningFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner EveningToHoursSpinner = (Spinner)v.findViewById(R.id.EveningToHour);
			        EveningToHoursSpinner.setAdapter(aEveningToHour);
			        
			        final Spinner EveningToMinsSpinner = (Spinner)v.findViewById(R.id.EveningToMins);
			        EveningToMinsSpinner.setAdapter(aMins);
			        
			        if(!mornfromhour.equals("None") && !mornfrommins.equals("None") && !morntohour.equals("None") && !morntomins.equals("None"))
			        {
			        	MorningFromHourSpinner.setSelection(mfh);
			        	MorningFromMinsSpinner.setSelection(mfm);
			        	MorningToHoursSpinner.setSelection(mth);
			        	MorningToMinsSpinner.setSelection(mtm);
			        }
			        
			        if(!noonfromhour.equals("None") && !noonfrommins.equals("None") && !noontohour.equals("None") && !noontomins.equals("None"))
			        {
			        	NoonFromHourSpinner.setSelection(nfh);
			        	NoonFromMinsSpinner.setSelection(nfm);
			        	NoonToHoursSpinner.setSelection(nth);
			        	NoonToMinsSpinner.setSelection(ntm);
			        }
			        
			        if(!evenfromhour.equals("None") && !evenfrommins.equals("None") && !eventohour.equals("None") && !eventomins.equals("None"))
			        {
			        	EveningFromHourSpinner.setSelection(efh);
			        	EveningFromMinsSpinner.setSelection(efm);
			        	EveningToHoursSpinner.setSelection(eth);
			        	EveningToMinsSpinner.setSelection(etm);
			        }

			        b1.setPositiveButton("OK",new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							String M1=(String)MorningFromHourSpinner.getSelectedItem();
							String M2=(String)MorningFromMinsSpinner.getSelectedItem();
							String M3=(String)MorningToHoursSpinner.getSelectedItem();
							String M4=(String)MorningToMinsSpinner.getSelectedItem();
							String Morning;
							if(!(M1.equals("None")) && !(M3.equals("None")))
							{
								if(Integer.parseInt(M1)<Integer.parseInt(M3))
								{
									Morning=M1+":"+M2+" - "+M3+":"+M4;
									mornfromhour=M1;
									mfh=MorningFromHourSpinner.getSelectedItemPosition();
									mornfrommins=M2;
									mfm=MorningFromMinsSpinner.getSelectedItemPosition();
									morntohour=M3;
									mth=MorningToHoursSpinner.getSelectedItemPosition();
									morntomins=M4;
									mtm=MorningToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Morning="None";
								}
							}
							else
							{
								Morning="None";
							}
							
							String N1=(String)NoonFromHourSpinner.getSelectedItem();
							String N2=(String)NoonFromMinsSpinner.getSelectedItem();
							String N3=(String)NoonToHoursSpinner.getSelectedItem();
							String N4=(String)NoonToMinsSpinner.getSelectedItem();
							String Afternoon;
							if(!(N1.equals("None")) && !(N3.equals("None")))
							{
								if(Integer.parseInt(N1)<Integer.parseInt(N3))
								{
									Afternoon=N1+":"+N2+" - "+N3+":"+N4;
									noonfromhour=N1;
									nfh=NoonFromHourSpinner.getSelectedItemPosition();
									noonfrommins=N2;
									nfm=NoonFromMinsSpinner.getSelectedItemPosition();
									noontohour=N3;
									nth=NoonToHoursSpinner.getSelectedItemPosition();
									noontomins=N4;
									ntm=NoonToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Afternoon="None";
								}
							}
							else
							{
								Afternoon="None";
							}
							
							String E1=(String)EveningFromHourSpinner.getSelectedItem();
							String E2=(String)EveningFromMinsSpinner.getSelectedItem();
							String E3=(String)EveningToHoursSpinner.getSelectedItem();
							String E4=(String)EveningToMinsSpinner.getSelectedItem();
							String Evening;
							if(!(E1.equals("None")) && !(E3.equals("None")))
							{
								if(Integer.parseInt(E1)<Integer.parseInt(E3))
								{
									Evening=E1+":"+E2+" - "+E3+":"+E4;
									evenfromhour=E1;
									efh=EveningFromHourSpinner.getSelectedItemPosition();
									evenfrommins=E2;
									efm=EveningFromMinsSpinner.getSelectedItemPosition();
									eventohour=E3;
									eth=EveningToHoursSpinner.getSelectedItemPosition();
									eventomins=E4;
									etm=EveningToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Evening="None";
								}
							}
							else
							{
								Evening="None";
							}
							
							day=sat.getText().toString();
							Doctor_Save_Day_time dsdt=new Doctor_Save_Day_time();
							dsdt.save_day_time(email, day, Morning, Afternoon, Evening);
							Toast.makeText(Doctor_Select_Day_Time.this, "Time Saved for= "+day+" Morning= "+Morning+" Noon= "+Afternoon+" Evening= "+Evening, 0).show();
						}	
					});
					b1.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							day=sat.getText().toString();
							Doctor_Save_Day_time dsdt=new Doctor_Save_Day_time();
							dsdt.save_day_time(email, day, "None", "None", "None");
							Toast.makeText(Doctor_Select_Day_Time.this, "Time Saved for= "+day+" Morning, Noon, Evening= None", 0).show();
						}
					});
					b1.show();
				}
				else
				{
					day=sat.getText().toString();
					Doctor_Delete_Day_Time dddt=new Doctor_Delete_Day_Time();
					dddt.Delete_Day_Time(email, day);
					Toast.makeText(Doctor_Select_Day_Time.this, "Time deleted for= "+day, 0).show();
				}
			}
		});
		
		final CheckBox sun=(CheckBox)findViewById(R.id.SunCkeckbox);
		sun.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if(isChecked)
				{
					AlertDialog.Builder b1=new AlertDialog.Builder(Doctor_Select_Day_Time.this);
					LayoutInflater inflater=Doctor_Select_Day_Time.this.getLayoutInflater();
					View v=inflater.inflate( R.layout.doctor_select_time,null);
					b1.setView(v);
					b1.setTitle("Select Visiting Time");
					
					final Spinner MorningFromHourSpinner = (Spinner)v.findViewById(R.id.MorningFromHour);
			        MorningFromHourSpinner.setAdapter(aMorningFromHour);
			        
			        final Spinner MorningFromMinsSpinner = (Spinner)v.findViewById(R.id.MorningFromMins);
			        MorningFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner MorningToHoursSpinner = (Spinner)v.findViewById(R.id.MorningToHour);
			        MorningToHoursSpinner.setAdapter(aMorningToHour);
			        
			        final Spinner MorningToMinsSpinner = (Spinner)v.findViewById(R.id.MorningToMins);
			        MorningToMinsSpinner.setAdapter(aMins);
			        
			        final Spinner NoonFromHourSpinner = (Spinner)v.findViewById(R.id.NoonFromHour);
			        NoonFromHourSpinner.setAdapter(aNoonFromHour);
			        
			        final Spinner NoonFromMinsSpinner = (Spinner)v.findViewById(R.id.NoonFromMins);
			        NoonFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner NoonToHoursSpinner = (Spinner)v.findViewById(R.id.NoonToHour);
			        NoonToHoursSpinner.setAdapter(aNoonToHour);
			        
			        final Spinner NoonToMinsSpinner = (Spinner)v.findViewById(R.id.NoonToMins);
			        NoonToMinsSpinner.setAdapter(aMins);
			        
			        final Spinner EveningFromHourSpinner = (Spinner)v.findViewById(R.id.EveningFromHour);
			        EveningFromHourSpinner.setAdapter(aEveningFromHour);
			        
			        final Spinner EveningFromMinsSpinner = (Spinner)v.findViewById(R.id.EveningFromMins);
			        EveningFromMinsSpinner.setAdapter(aMins);
			        
			        final Spinner EveningToHoursSpinner = (Spinner)v.findViewById(R.id.EveningToHour);
			        EveningToHoursSpinner.setAdapter(aEveningToHour);
			        
			        final Spinner EveningToMinsSpinner = (Spinner)v.findViewById(R.id.EveningToMins);
			        EveningToMinsSpinner.setAdapter(aMins);
			        
			        if(!mornfromhour.equals("None") && !mornfrommins.equals("None") && !morntohour.equals("None") && !morntomins.equals("None"))
			        {
			        	MorningFromHourSpinner.setSelection(mfh);
			        	MorningFromMinsSpinner.setSelection(mfm);
			        	MorningToHoursSpinner.setSelection(mth);
			        	MorningToMinsSpinner.setSelection(mtm);
			        }
			        
			        if(!noonfromhour.equals("None") && !noonfrommins.equals("None") && !noontohour.equals("None") && !noontomins.equals("None"))
			        {
			        	NoonFromHourSpinner.setSelection(nfh);
			        	NoonFromMinsSpinner.setSelection(nfm);
			        	NoonToHoursSpinner.setSelection(nth);
			        	NoonToMinsSpinner.setSelection(ntm);
			        }
			        
			        if(!evenfromhour.equals("None") && !evenfrommins.equals("None") && !eventohour.equals("None") && !eventomins.equals("None"))
			        {
			        	EveningFromHourSpinner.setSelection(efh);
			        	EveningFromMinsSpinner.setSelection(efm);
			        	EveningToHoursSpinner.setSelection(eth);
			        	EveningToMinsSpinner.setSelection(etm);
			        }

			        b1.setPositiveButton("OK",new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							String M1=(String)MorningFromHourSpinner.getSelectedItem();
							String M2=(String)MorningFromMinsSpinner.getSelectedItem();
							String M3=(String)MorningToHoursSpinner.getSelectedItem();
							String M4=(String)MorningToMinsSpinner.getSelectedItem();
							String Morning;
							if(!(M1.equals("None")) && !(M3.equals("None")))
							{
								if(Integer.parseInt(M1)<Integer.parseInt(M3))
								{
									Morning=M1+":"+M2+" - "+M3+":"+M4;
									mornfromhour=M1;
									mfh=MorningFromHourSpinner.getSelectedItemPosition();
									mornfrommins=M2;
									mfm=MorningFromMinsSpinner.getSelectedItemPosition();
									morntohour=M3;
									mth=MorningToHoursSpinner.getSelectedItemPosition();
									morntomins=M4;
									mtm=MorningToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Morning="None";
								}
							}
							else
							{
								Morning="None";
							}
							
							String N1=(String)NoonFromHourSpinner.getSelectedItem();
							String N2=(String)NoonFromMinsSpinner.getSelectedItem();
							String N3=(String)NoonToHoursSpinner.getSelectedItem();
							String N4=(String)NoonToMinsSpinner.getSelectedItem();
							String Afternoon;
							if(!(N1.equals("None")) && !(N3.equals("None")))
							{
								if(Integer.parseInt(N1)<Integer.parseInt(N3))
								{
									Afternoon=N1+":"+N2+" - "+N3+":"+N4;
									noonfromhour=N1;
									nfh=NoonFromHourSpinner.getSelectedItemPosition();
									noonfrommins=N2;
									nfm=NoonFromMinsSpinner.getSelectedItemPosition();
									noontohour=N3;
									nth=NoonToHoursSpinner.getSelectedItemPosition();
									noontomins=N4;
									ntm=NoonToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Afternoon="None";
								}
							}
							else
							{
								Afternoon="None";
							}
							
							String E1=(String)EveningFromHourSpinner.getSelectedItem();
							String E2=(String)EveningFromMinsSpinner.getSelectedItem();
							String E3=(String)EveningToHoursSpinner.getSelectedItem();
							String E4=(String)EveningToMinsSpinner.getSelectedItem();
							String Evening;
							if(!(E1.equals("None")) && !(E3.equals("None")))
							{
								if(Integer.parseInt(E1)<Integer.parseInt(E3))
								{
									Evening=E1+":"+E2+" - "+E3+":"+E4;
									evenfromhour=E1;
									efh=EveningFromHourSpinner.getSelectedItemPosition();
									evenfrommins=E2;
									efm=EveningFromMinsSpinner.getSelectedItemPosition();
									eventohour=E3;
									eth=EveningToHoursSpinner.getSelectedItemPosition();
									eventomins=E4;
									etm=EveningToMinsSpinner.getSelectedItemPosition();
								}
								else
								{
									Evening="None";
								}
							}
							else
							{
								Evening="None";
							}
							
							day=sun.getText().toString();
							Doctor_Save_Day_time dsdt=new Doctor_Save_Day_time();
							dsdt.save_day_time(email, day, Morning, Afternoon, Evening);
							Toast.makeText(Doctor_Select_Day_Time.this, "Time Saved for= "+day+" Morning= "+Morning+" Noon= "+Afternoon+" Evening= "+Evening, 0).show();
						}	
					});
					b1.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							day=sun.getText().toString();
							Doctor_Save_Day_time dsdt=new Doctor_Save_Day_time();
							dsdt.save_day_time(email, day, "None", "None", "None");
							Toast.makeText(Doctor_Select_Day_Time.this, "Time Saved for= "+day+" Morning, Noon, Evening= None", 0).show();
						}
					});
					b1.show();
				}
				else
				{
					day=sun.getText().toString();
					Doctor_Delete_Day_Time dddt=new Doctor_Delete_Day_Time();
					dddt.Delete_Day_Time(email, day);
					Toast.makeText(Doctor_Select_Day_Time.this, "Time deleted for= "+day, 0).show();
				}
			}
		});
		
		
		
		Button submit=(Button)findViewById(R.id.DocSelectDaySubmit);
		submit.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder ab=new AlertDialog.Builder(Doctor_Select_Day_Time.this);
				ab.setTitle("Verify Your Email");
				ab.setMessage("A verification mail has been sent to the email ID you provided during registration.\nPlease click on that link to start using this software");
				ab.setPositiveButton("OK", new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						Intent i2=new Intent(Doctor_Select_Day_Time.this,Login_As.class);
						i2.putExtra("LoginAs", "Doctor");
						startActivity(i2);
					}
				});
				ab.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_doctor__select__day__time,
				menu);
		return true;
	}

	@Override
	public void onBackPressed() 
	{
	    return;
	}
}
