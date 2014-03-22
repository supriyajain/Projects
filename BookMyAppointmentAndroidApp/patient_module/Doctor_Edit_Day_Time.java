package com.example.patient_module;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;


import Doctor_Visiting_Day_Time.Doctor_Delete_Day_Time;
import Doctor_Visiting_Day_Time.Doctor_Fetch_Day_Time;
import Doctor_Visiting_Day_Time.Doctor_Save_Day_time;
import Doctor_Visiting_Day_Time.Doctor_Update_Day_Time;
import Messages.Messages;
import Patient_Book_Appointment.Doctor_Fetch_All_Booked_Appointments;
import Patient_Book_Appointment.Patient_Delete_Booked_Appointment;
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
import android.widget.Spinner;
import android.widget.Toast;

public class Doctor_Edit_Day_Time extends Activity 
{
	
	String email;
	
	String mornfromhour="None",mornfrommins="None",morntohour="None",morntomins="None";
	String noonfromhour="None",noonfrommins="None",noontohour="None",noontomins="None";
	String evenfromhour="None",evenfrommins="None",eventohour="None",eventomins="None";
	int mfh,mfm,mth,mtm,nfh,nfm,nth,ntm,efh,efm,eth,etm=0;
	
	String docname="";
	int timeinterval;
	ParseObject po;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_doctor__edit__day__time);
			
			Intent intent=getIntent();
			email=intent.getStringExtra("docemail");
			
			Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
		
			final List<String> lmins=new ArrayList<String>();
			lmins.add("00");
			lmins.add("15");
			lmins.add("30");
			lmins.add("45");
			final ArrayAdapter<String> aMins=new ArrayAdapter<String>(Doctor_Edit_Day_Time.this,android.R.layout.simple_spinner_item,lmins);
			
			final List<String> lmorningfromhour=new ArrayList<String>();
			lmorningfromhour.add("None");
			lmorningfromhour.add("05");
			lmorningfromhour.add("06");
			lmorningfromhour.add("07");
			lmorningfromhour.add("08");
			lmorningfromhour.add("09");
			lmorningfromhour.add("10");
			lmorningfromhour.add("11");
			final ArrayAdapter<String> aMorningFromHour=new ArrayAdapter<String>(Doctor_Edit_Day_Time.this,android.R.layout.simple_spinner_item,lmorningfromhour);
			
			final List<String> lmorningTohour=new ArrayList<String>();
			lmorningTohour.add("None");
			lmorningTohour.add("06");
			lmorningTohour.add("07");
			lmorningTohour.add("08");
			lmorningTohour.add("09");
			lmorningTohour.add("10");
			lmorningTohour.add("11");
			lmorningTohour.add("12");
			final ArrayAdapter<String> aMorningToHour=new ArrayAdapter<String>(Doctor_Edit_Day_Time.this,android.R.layout.simple_spinner_item,lmorningTohour);
			
			final List<String> lnoonfromhour=new ArrayList<String>();
			lnoonfromhour.add("None");
			lnoonfromhour.add("12");
			lnoonfromhour.add("13");
			lnoonfromhour.add("14");
			lnoonfromhour.add("15");
			lnoonfromhour.add("16");
			lnoonfromhour.add("17");
			final ArrayAdapter<String> aNoonFromHour=new ArrayAdapter<String>(Doctor_Edit_Day_Time.this,android.R.layout.simple_spinner_item,lnoonfromhour);
			
			final List<String> lnoonTohour=new ArrayList<String>();
			lnoonTohour.add("None");
			lnoonTohour.add("13");
			lnoonTohour.add("14");
			lnoonTohour.add("15");
			lnoonTohour.add("16");
			lnoonTohour.add("17");
			lnoonTohour.add("18");
			final ArrayAdapter<String> aNoonToHour=new ArrayAdapter<String>(Doctor_Edit_Day_Time.this,android.R.layout.simple_spinner_item,lnoonTohour);
			
			final List<String> lEveningfromhour=new ArrayList<String>();
			lEveningfromhour.add("None");
			lEveningfromhour.add("18");
			lEveningfromhour.add("19");
			lEveningfromhour.add("20");
			lEveningfromhour.add("21");
			lEveningfromhour.add("22");
			lEveningfromhour.add("23");
			final ArrayAdapter<String> aEveningFromHour=new ArrayAdapter<String>(Doctor_Edit_Day_Time.this,android.R.layout.simple_spinner_item,lEveningfromhour);
			
			final List<String> lEveningTohour=new ArrayList<String>();
			lEveningTohour.add("None");
			lEveningTohour.add("19");
			lEveningTohour.add("20");
			lEveningTohour.add("21");
			lEveningTohour.add("22");
			lEveningTohour.add("23");
			lEveningTohour.add("24");
			final ArrayAdapter<String> aEveningToHour=new ArrayAdapter<String>(Doctor_Edit_Day_Time.this,android.R.layout.simple_spinner_item,lEveningTohour);
			
			
			
			final Button AddDay=(Button)findViewById(R.id.AddDaybtn);
			final Button EditDayTime=(Button)findViewById(R.id.doceditdeletedaytimeBtn);
			
			Doctor_Fetch_Day_Time dfdt=new Doctor_Fetch_Day_Time();
			final ArrayList<ArrayList<String>> values=dfdt.Fetch_Day_Time(email);
			
			if(values.size()<1)
			{
				EditDayTime.setVisibility(EditDayTime.INVISIBLE);
				AddDay.setVisibility(1);
				
				AlertDialog.Builder abdi=new AlertDialog.Builder(Doctor_Edit_Day_Time.this);
				abdi.setTitle("Add Record(s)");
				abdi.setMessage("You have not selected any visiting day and time yet !\nPlease click on 'Add Another Day' button to add visiting day and time !");
				abdi.setPositiveButton("OK", null);
				abdi.show();
			}
			else if(values.size()<7)
			{
				AddDay.setVisibility(1);
			}
			
			final ArrayList<String> DayList=new ArrayList<String>();
			final ArrayList<String[]> EditTimeList=new ArrayList<String[]>();
			
			for(ArrayList<String> s:values)
			{
				String time=s.get(1)+": \n";
				String edittime[]=new String[12];
				int c=0;
				for(int i=2;i<s.size();i++)
				{
					
					if(!(s.get(i).equals("None")))
					{
						time=time+s.get(i)+", ";
						String temp=s.get(i);
						edittime[c]=temp.substring(0, temp.indexOf(":"));
						c++;
						edittime[c]=temp.substring(temp.indexOf(":")+1,temp.indexOf("-"));
						c++;
						edittime[c]=temp.substring(temp.indexOf("-")+2,temp.lastIndexOf(":"));
						c++;
						edittime[c]=temp.substring(temp.lastIndexOf(":")+1);
						c++;
					}
					else
					{
						edittime[c]="None";
						c++;
						edittime[c]="00";
						c++;
						edittime[c]="None";
						c++;
						edittime[c]="00";
						c++;
					}
				}
				if(time.contains(","))
				{
					time=time.substring(0, time.lastIndexOf(","));
					DayList.add(time);
				}
				EditTimeList.add(edittime);
			}
			
			
			EditDayTime.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					
					String daytimelist[]=new String[DayList.size()];
					int q=0;
					for(String y: DayList)
					{
						daytimelist[q]=y;
						q++;
					}
					
					AlertDialog.Builder abdia=new AlertDialog.Builder(Doctor_Edit_Day_Time.this);
					abdia.setTitle("Select a day to edit/delete it");
					abdia.setItems(daytimelist, new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							final int selected=which;
							
							final String options[]={"Edit","Delete"};
							AlertDialog.Builder adb=new AlertDialog.Builder(Doctor_Edit_Day_Time.this);
							adb.setTitle("Select an Option !");
							adb.setItems(options, new DialogInterface.OnClickListener()
							{
								
								@Override
								public void onClick(DialogInterface dialog, int which) 
								{
									if(options[which].equals("Edit"))
									{
										AlertDialog.Builder b1=new AlertDialog.Builder(Doctor_Edit_Day_Time.this);
										LayoutInflater inflater=Doctor_Edit_Day_Time.this.getLayoutInflater();
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
								        
								        final String edittime[]=EditTimeList.get(selected);
								        for(String stime: edittime)
								        	System.out.println(stime);
								        
								        MorningFromHourSpinner.setSelection(lmorningfromhour.indexOf(edittime[0]));
								        MorningFromMinsSpinner.setSelection(lmins.indexOf(edittime[1]));
								        MorningToHoursSpinner.setSelection(lmorningTohour.indexOf(edittime[2]));
								        MorningToMinsSpinner.setSelection(lmins.indexOf(edittime[3]));
								        
								        NoonFromHourSpinner.setSelection(lnoonfromhour.indexOf(edittime[4]));
								        NoonFromMinsSpinner.setSelection(lmins.indexOf(edittime[5]));
								        NoonToHoursSpinner.setSelection(lnoonTohour.indexOf(edittime[6]));
								        NoonToMinsSpinner.setSelection(lmins.indexOf(edittime[7]));
								        
								        EveningFromHourSpinner.setSelection(lEveningfromhour.indexOf(edittime[8]));
								        EveningFromMinsSpinner.setSelection(lmins.indexOf(edittime[9]));
								        EveningToHoursSpinner.setSelection(lEveningTohour.indexOf(edittime[10]));
								        EveningToMinsSpinner.setSelection(lmins.indexOf(edittime[11]));
								        
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
												
												String day=DayList.get(selected).substring(0, DayList.get(selected).indexOf(":"));
												DayList.remove(selected);
												
												Doctor_Update_Day_Time dudt=new Doctor_Update_Day_Time();
												dudt.Update_Day_Time(email, day, Morning, Afternoon, Evening);
												
												int flag1=0,flag2=0,flag3=0;
												
												if(!edittime[0].equals(M1) || !edittime[1].equals(M2) || !edittime[2].equals(M3) || !edittime[3].equals(M4))
												{
													flag1=1;
												}
												
												if(!edittime[4].equals(N1) || !edittime[5].equals(N2) || !edittime[6].equals(N3) || !edittime[7].equals(N4))
												{
													flag2=1;
												}
												
												if(!edittime[8].equals(E1) || !edittime[9].equals(E2) || !edittime[10].equals(E3) || !edittime[11].equals(E4))
												{
													flag3=1;
												}
												
												if(flag1==1 || flag2==1 || flag3==1)
												{
													
													Calendar cal=Calendar.getInstance();
													int date=cal.get(Calendar.DATE);
													int month=cal.get(Calendar.MONTH)+1;
													int year=cal.get(Calendar.YEAR);
													String idate=date+"/"+month+"/"+year;
													
													int hour=cal.get(Calendar.HOUR);
													int min=cal.get(Calendar.MINUTE);
													int shift=cal.get(Calendar.AM_PM);
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
													
													
													Doctor_Fetch_All_Booked_Appointments pfaba=new Doctor_Fetch_All_Booked_Appointments();
													ArrayList<ArrayList<String>> bookeddata=pfaba.FetchAllBookedAppointments(email);
													
													ParseQuery q = new ParseQuery("Doctor");
													q.whereEqualTo("ID", email);
													try
													{
														ParseObject o=q.getFirst();
														if (o!= null) 
													    {
													    	docname="Dr. "+o.getString("First_name")+" "+o.getString("Last_name");
													    }
													}
													catch(ParseException e)
													{
													   	System.out.println(e.getCode());
													}
													
													
													for(ArrayList<String> s: bookeddata)
													{
														if(s.get(3).equals(day))
														{
															String temp=s.get(2);
															int daydate=Integer.parseInt(temp.substring(0, temp.indexOf("/")));
															int daymonth=Integer.parseInt(temp.substring(temp.indexOf("/")+1,temp.lastIndexOf("/")));
															int dayyear=Integer.parseInt(temp.substring(temp.lastIndexOf("/")+1));
															
															if(daydate>=date && daymonth>=month && dayyear>=year)
															{
																Patient_Delete_Booked_Appointment pdba=new Patient_Delete_Booked_Appointment();
																pdba.Delete_Booked_Appointment(email, s.get(1), s.get(2), s.get(5));
																
																String msg=docname+"has recently updated appointment time for "+s.get(2)+" ("+s.get(3)+") !\n"+"Please check if your appointment with "+docname+" on "+s.get(2)+" ("+s.get(3)+") at "+s.get(5)+" is still within doctor's available time period and if its not then please book appointment for next available time slot or date. Thank You !";
																
																Messages m=new Messages();
																m.save_Message(email, s.get(1), idate, time, msg);
																
																ParseQuery pushQuery = ParseInstallation.getQuery();
																pushQuery.whereEqualTo("ID", s.get(1));
																 
																ParsePush push = new ParsePush();
																push.setQuery(pushQuery);
																push.setMessage("You have new message");
																push.sendInBackground();
															}
														}
													}
												}
												
												EditTimeList.remove(selected);
												int c=0;
												
												String time=day+": \n";
												if(!(Morning.equals("None")))
												{
													time=time+Morning+",";
													String temp=Morning;
													edittime[c]=temp.substring(0, temp.indexOf(":"));
													c++;
													edittime[c]=temp.substring(temp.indexOf(":")+1,temp.indexOf("-"));
													c++;
													edittime[c]=temp.substring(temp.indexOf("-")+2,temp.lastIndexOf(":"));
													c++;
													edittime[c]=temp.substring(temp.lastIndexOf(":")+1);
													c++;
												}
												else
												{
													edittime[c]="None";
													c++;
													edittime[c]="00";
													c++;
													edittime[c]="None";
													c++;
													edittime[c]="00";
													c++;
												}
												
												if(!(Afternoon.equals("None")))
												{
													time=time+Afternoon+",";
													String temp=Afternoon;
													edittime[c]=temp.substring(0, temp.indexOf(":"));
													c++;
													edittime[c]=temp.substring(temp.indexOf(":")+1,temp.indexOf("-"));
													c++;
													edittime[c]=temp.substring(temp.indexOf("-")+2,temp.lastIndexOf(":"));
													c++;
													edittime[c]=temp.substring(temp.lastIndexOf(":")+1);
													c++;
												}
												else
												{
													edittime[c]="None";
													c++;
													edittime[c]="00";
													c++;
													edittime[c]="None";
													c++;
													edittime[c]="00";
													c++;
												}
												
												if(!(Evening.equals("None")))
												{
													time=time+Evening;
													String temp=Evening;
													edittime[c]=temp.substring(0, temp.indexOf(":"));
													c++;
													edittime[c]=temp.substring(temp.indexOf(":")+1,temp.indexOf("-"));
													c++;
													edittime[c]=temp.substring(temp.indexOf("-")+2,temp.lastIndexOf(":"));
													c++;
													edittime[c]=temp.substring(temp.lastIndexOf(":")+1);
													c++;
												}
												else
												{
													edittime[c]="None";
													c++;
													edittime[c]="00";
													c++;
													edittime[c]="None";
													c++;
													edittime[c]="00";
													c++;
												}
												
												if(time.lastIndexOf(",")==time.length()-1)
													time=time.substring(0,time.lastIndexOf(","));
												
												DayList.add(time);
												EditTimeList.add(edittime);
												Toast.makeText(Doctor_Edit_Day_Time.this, "Time Saved for= "+day+" Morning= "+Morning+" Noon= "+Afternoon+" Evening= "+Evening, 0).show();
											}	
										});
										b1.setNegativeButton("Cancel", null);
										b1.show();
									}
									else
									{
										String day=DayList.get(selected).substring(0, DayList.get(selected).indexOf(":"));
										DayList.remove(selected);
										EditTimeList.remove(selected);
										
										Doctor_Delete_Day_Time dddt=new Doctor_Delete_Day_Time();
										dddt.Delete_Day_Time(email, day);
									}
								}
							});
							adb.setNegativeButton("Cancel", null);
							adb.show();
						}
					});
					abdia.setNegativeButton("Cancel", null);
					abdia.show();
				}
			});
			
			AddDay.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					EditDayTime.setVisibility(1);
					
					String days[]={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
					final String show[] = new String[7];
					int j=0;
					
					Doctor_Fetch_Day_Time dfdt=new Doctor_Fetch_Day_Time();
					ArrayList<ArrayList<String>> values=dfdt.Fetch_Day_Time(email);
					
					for(int i=0;i<days.length;i++)
					{
						int flag=0;
						for(ArrayList<String> s:values)
						{
							if(s.get(1).equals(days[i]))
							{
								System.out.println("Inside if with= "+s.get(1));
								flag=1;
								break;
							}
						}
						if(flag==0)
						{
							show[j]=days[i];
							System.out.println("added in show= "+show[j]);
							j++;
						}
					}
					if(j<7)
					{
						for(int k=j;k<7;k++)
						{
							show[k]="";
						}
					}
					
					AlertDialog.Builder ab=new AlertDialog.Builder(Doctor_Edit_Day_Time.this);
					ab.setTitle("Select Day");
					ab.setItems(show, new DialogInterface.OnClickListener() 
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							
							final int w=which;
							if(!(show[w].equals("")))
							{
								AlertDialog.Builder b1=new AlertDialog.Builder(Doctor_Edit_Day_Time.this);
								LayoutInflater inflater=Doctor_Edit_Day_Time.this.getLayoutInflater();
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
										
										String day=show[w];
										
										Doctor_Save_Day_time dsdt=new Doctor_Save_Day_time();
										dsdt.save_day_time(email, day, Morning, Afternoon, Evening);
										Toast.makeText(Doctor_Edit_Day_Time.this, "Time Saved for= "+day+" Morning= "+Morning+" Noon= "+Afternoon+" Evening= "+Evening, 0).show();
										
										String edittime[]=new String[12];
										int c=0;
										
										String time=day+": \n";
										if(!(Morning.equals("None")))
										{
											time=time+Morning+",";
											String temp=Morning;
											edittime[c]=temp.substring(0, temp.indexOf(":"));
											c++;
											edittime[c]=temp.substring(temp.indexOf(":")+1,temp.indexOf("-"));
											c++;
											edittime[c]=temp.substring(temp.indexOf("-")+2,temp.lastIndexOf(":"));
											c++;
											edittime[c]=temp.substring(temp.lastIndexOf(":")+1);
											c++;
										}
										else
										{
											edittime[c]="None";
											c++;
											edittime[c]="00";
											c++;
											edittime[c]="None";
											c++;
											edittime[c]="00";
											c++;
										}
										
										if(!(Afternoon.equals("None")))
										{
											time=time+Afternoon+",";
											String temp=Afternoon;
											edittime[c]=temp.substring(0, temp.indexOf(":"));
											c++;
											edittime[c]=temp.substring(temp.indexOf(":")+1,temp.indexOf("-"));
											c++;
											edittime[c]=temp.substring(temp.indexOf("-")+2,temp.lastIndexOf(":"));
											c++;
											edittime[c]=temp.substring(temp.lastIndexOf(":")+1);
											c++;
										}
										else
										{
											edittime[c]="None";
											c++;
											edittime[c]="00";
											c++;
											edittime[c]="None";
											c++;
											edittime[c]="00";
											c++;
										}
										
										if(!(Evening.equals("None")))
										{
											time=time+Evening;
											String temp=Evening;
											edittime[c]=temp.substring(0, temp.indexOf(":"));
											c++;
											edittime[c]=temp.substring(temp.indexOf(":")+1,temp.indexOf("-"));
											c++;
											edittime[c]=temp.substring(temp.indexOf("-")+2,temp.lastIndexOf(":"));
											c++;
											edittime[c]=temp.substring(temp.lastIndexOf(":")+1);
											c++;
										}
										else
										{
											edittime[c]="None";
											c++;
											edittime[c]="00";
											c++;
											edittime[c]="None";
											c++;
											edittime[c]="00";
											c++;
										}
										
										if(time.lastIndexOf(",")==time.length()-1)
											time=time.substring(0,time.lastIndexOf(","));
										
										DayList.add(time);
										EditTimeList.add(edittime);
									
									}	
								});
								b1.setNegativeButton("Cancel", null);
								b1.show();
							}
						}
					});
					ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							if(DayList.size()<1)
							{
								EditDayTime.setVisibility(EditDayTime.INVISIBLE);
								AddDay.setVisibility(1);
							}
						}
					});
					ab.show();
					
					if(DayList.size()>=7)
						AddDay.setVisibility(AddDay.INVISIBLE);
				}
			});
			
			Button Home=(Button)findViewById(R.id.doceditdaytimeHomebtn);
			Home.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					Intent i=new Intent(Doctor_Edit_Day_Time.this, Doctor_Home.class);
					startActivity(i);
				}
			});
			
			Button editTimeInterval=(Button)findViewById(R.id.doceditdaytimeIntervalbtn);
			editTimeInterval.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					ParseQuery q = new ParseQuery("Doctor");
					q.whereEqualTo("Email", email);
					try
					{
						ParseObject o=q.getFirst();
						if (o!= null) 
					    {
					    	po=o;
					    	timeinterval=po.getInt("Time_interval");
					    }
					}
					catch(ParseException e)
					{
					   	System.out.println(e.getCode());
					}
					
					int position=1;
					
					String TimeList[]={"05","10","15","20","25","30","35","40","45","50","55","60"};
					
					for(int i=0;i<TimeList.length;i++)
					{
						if(TimeList[i].equals(""+timeinterval))
						{
							position=i;
							break;
						}
					}
					
					ArrayAdapter<String> ad=new ArrayAdapter<String>(Doctor_Edit_Day_Time.this,android.R.layout.simple_spinner_item,TimeList);
					final Spinner sp=new Spinner(Doctor_Edit_Day_Time.this);
					sp.setAdapter(ad);
					sp.setSelection(position);
	
					
					AlertDialog.Builder ab=new AlertDialog.Builder(Doctor_Edit_Day_Time.this);
					ab.setTitle("Select Time Interval");
					ab.setView(sp);
					ab.setPositiveButton("OK", new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							int Time=Integer.parseInt((String)sp.getSelectedItem());
							po.put("Time_interval", Time);
							po.saveInBackground();
						}
					});
					ab.setNegativeButton("Cancel", null);
					ab.show();
				}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater()
				.inflate(R.menu.activity_doctor__edit__day__time, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() 
	{
	    return;
	}
}

	