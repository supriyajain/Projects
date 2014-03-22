package com.example.patient_module;

import java.util.ArrayList;
import java.util.Calendar;
import com.example.patient_module.R;
import com.parse.Parse;
import Doctor_Available_Appointment.Doctor_Fetch_Available_Appoinment_Date_Time;
import Doctor_Available_Appointment.Doctor_Insert_Updated_Available_Date_Time;
import Doctor_Available_Appointment.Doctor_Remove_Available_Date_Time;
import Doctor_Details.Patient_Fetch_Doctor_Data;
import Doctor_Visiting_Day_Time.Doctor_Fetch_Day_Time;
import Patient_Book_Appointment.Doctor_Verify_Full_Booked_Appointment;
import Patient_Book_Appointment.Patient_Book_Appointment;
import Patient_Book_Appointment.Patient_Verify_Booked_Appointment;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Book_Appointment extends Activity 
{
	
	String patemail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
					super.onCreate(savedInstanceState);
					setContentView(R.layout.activity_book__appointment);
					
					Intent intent=getIntent();
					patemail=intent.getStringExtra("patemail");
					
					Parse.initialize(this, "nYNzCuNXDvHtqCAWtoxXX93fhfQc86YkTC6nej7q", "MWQdRS5iS3rgyd6drhqGm0GZSXUNFpknSajxYcdG");
					
					Patient_Fetch_Doctor_Data pfdd=new Patient_Fetch_Doctor_Data();
					final ArrayList<ArrayList<String>> values=pfdd.Fetch_Doctor_Data();
					
					if(values.size()<1)
					{
						AlertDialog.Builder adb0=new AlertDialog.Builder(Book_Appointment.this);
						adb0.setTitle("No records");
						adb0.setMessage("There is no any doctor's record available to display !");
						adb0.setPositiveButton("OK", new DialogInterface.OnClickListener() 
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								Intent ihome=new Intent(Book_Appointment.this,Patient_Home.class);
								startActivity(ihome);
							}
						});
						adb0.show();
					}
					else	
					{
						final ArrayList<String> Doclist=new ArrayList<String>();
						final ArrayList<ArrayList<ArrayList<String>>> alldocdays=new ArrayList<ArrayList<ArrayList<String>>>();
						
						final String email[]=new String[values.size()];
						final int TimeInterval[]=new int[values.size()];
						int z=0;
						
						for(ArrayList<String> s: values)
						{
							String docdetails="Dr. "+s.get(0)+" "+s.get(1)+"\nSpecialization: "+s.get(7)+"\n\nClinic's Address: "+s.get(4)+"\nContact Number: "+s.get(5)+"\nEmail: "+s.get(6)+"\n\nVisiting Time:-\n";
							
							email[z]=s.get(6);
							TimeInterval[z]=Integer.parseInt(s.get(8));
							z++;
							
							Doctor_Fetch_Day_Time dfdt=new Doctor_Fetch_Day_Time();
							ArrayList<ArrayList<String>> timevalues=dfdt.Fetch_Day_Time(s.get(6));
							String daytime="";
							
							for(ArrayList<String> str:timevalues)
							{
								String time=str.get(1)+": \n";
								for(int i=2;i<str.size();i++)
								{
									if(!(str.get(i).equals("None")))
										time=time+str.get(i)+", ";
								}
								if(time.contains(","))
								{
									time=time.substring(0, time.lastIndexOf(","));
									daytime=daytime+time+"\n";
								}
							}
							docdetails=docdetails+daytime;
							Doclist.add(docdetails);
							
							alldocdays.add(timevalues);
						}
						
						final ListView l=(ListView)findViewById(R.id.bookapplist);
						ArrayAdapter<String> a=new ArrayAdapter<String>(Book_Appointment.this, android.R.layout.simple_list_item_1,Doclist);
						l.setAdapter(a);
						l.setOnItemClickListener(new OnItemClickListener() 
						{
				
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3)
							{
								final String docemail=email[arg2];
								final int TimeInt=TimeInterval[arg2];
								final ArrayList<ArrayList<String>> days=alldocdays.get(arg2);
								
								ArrayList<String> availabledays=new ArrayList<String>();
								
								for(ArrayList<String> a: days)
								{
									String day=a.get(1);
									
									Calendar c=Calendar.getInstance();
									int todaydate=c.get(Calendar.DATE);
									int todaymonth=c.get(Calendar.MONTH)+1;
									int todayyear=c.get(Calendar.YEAR);
									final String idate=todaydate+"/"+todaymonth+"/"+todayyear;
									
									int currentday=c.get(Calendar.DAY_OF_WEEK);
									int setday=1;
									
									if(day.equals("Sunday"))
										setday=1;
									if(day.equals("Monday"))
										setday=2;
									if(day.equals("Tuesday"))
										setday=3;
									if(day.equals("Wednesday"))
										setday=4;
									if(day.equals("Thursday"))
										setday=5;
									if(day.equals("Friday"))
										setday=6;
									if(day.equals("Saturday"))
										setday=7;
									
									int numb=0;
									int set_date=00,set_month=00,set_year=00;
									String set_app_date="",display_date_day="";
									
									if(currentday<setday)
									{
										numb=setday-currentday;
										
										if(todaymonth==01 || todaymonth==03 || todaymonth==05 || todaymonth==07 || todaymonth==Integer.parseInt("08") || todaymonth==10 || todaymonth==12)
										{
											if((todaydate+numb)<=31)
											{
												set_date=todaydate+numb;
												set_month=todaymonth;
												set_year=todayyear;
											}
											else
											{
												set_date=(todaydate+numb)-31;
												if(todaymonth<=10)
												{
													set_month=todaymonth+1;
													set_year=todayyear;
												}
												else
												{
													set_month=01;
													set_year=todayyear+1;
												}
											}
										}
										else if(todaymonth==04 || todaymonth==06 || todaymonth==Integer.parseInt("09") || todaymonth==11)
										{
											if((todaydate+numb)<=30)
											{
												set_date=todaydate+numb;
												set_month=todaymonth;
												set_year=todayyear;
											}
											else
											{
												set_date=(todaydate+numb)-30;
												set_month=todaymonth+1;
												set_year=todayyear;
												
											}
										}
										else if(todaymonth==02)
										{
											if(todayyear%4==0)
											{
												if((todaydate+numb)<=29)
												{
													set_date=todaydate+numb;
													set_month=todaymonth;
													set_year=todayyear;
												}
												else
												{
													set_date=(todaydate+numb)-29;
													set_month=todaymonth+1;
													set_year=todayyear;
													
												}
											}
											else
											{
												if((todaydate+numb)<=28)
												{
													set_date=todaydate+numb;
													set_month=todaymonth;
													set_year=todayyear;
												}
												else
												{
													set_date=(todaydate+numb)-28;
													set_month=todaymonth+1;
													set_year=todayyear;
													
												}
											}
										}
										
										set_app_date=set_date+"/"+set_month+"/"+set_year;
										display_date_day=day+", "+set_app_date;
										availabledays.add(display_date_day);
									}
									else if(currentday==setday)
									{
										set_app_date=idate;
										display_date_day=day+", "+set_app_date;
										availabledays.add(display_date_day);
									}
								}
								
								final String avaDays[]=new String[availabledays.size()];
								for(int i=0;i<availabledays.size();i++)
								{
									avaDays[i]=availabledays.get(i);
								}
								
								AlertDialog.Builder adb=new AlertDialog.Builder(Book_Appointment.this);
								
								if(avaDays.length<1)
								{
									adb.setTitle("Date Unavailable");
									adb.setMessage("No available date for this week !\nPlease book appointment for next week ! ");
								}
								else
								{
									adb.setTitle("Select Appointment Date");
									adb.setItems(avaDays, new DialogInterface.OnClickListener()
									{
									
										@Override
										public void onClick(DialogInterface dialog, int which) 
										{
											final String temp=avaDays[which];
											final int seldaynumb=which;
											final String selectedDay=temp.substring(0, temp.indexOf(","));
											final String selecteddate=temp.substring(temp.indexOf(",")+2);
											ArrayList<String> avaShift=new ArrayList<String>();
											
											for(ArrayList<String> a: days)
											{
												if(a.get(1).equals(selectedDay))
												{
													Calendar c=Calendar.getInstance();
													int todaydate=c.get(Calendar.DATE);
													int todaymonth=c.get(Calendar.MONTH)+1;
													int todayyear=c.get(Calendar.YEAR);
													String idate=todaydate+"/"+todaymonth+"/"+todayyear;
													
													int ihour=c.get(Calendar.HOUR_OF_DAY);
													int ishift=c.get(Calendar.AM_PM);
													
													if(selecteddate.equals(idate))
													{
														if(!a.get(2).equals("None") && ishift!=Calendar.PM)
														{
															avaShift.add("Morning");
														}
														if(!a.get(3).equals("None") && ihour<19)
														{
															avaShift.add("Afternoon");
														}
														if(!a.get(4).equals("None"))
														{
															avaShift.add("Evening");
														}
													}
													else
													{
														if(!a.get(2).equals("None"))
														{
															avaShift.add("Morning");
														}
														if(!a.get(3).equals("None"))
														{
															avaShift.add("Afternoon");
														}
														if(!a.get(4).equals("None"))
														{
															avaShift.add("Evening");
														}
													}
												}
											}
											
											if(avaShift.size()>0)
											{
												final String shift[]=new String[avaShift.size()];
												for(int i=0;i<avaShift.size();i++)
												{
													shift[i]=avaShift.get(i);
												}
												
												AlertDialog.Builder a1=new AlertDialog.Builder(Book_Appointment.this);
												a1.setTitle("Select Shift");
												a1.setItems(shift, new DialogInterface.OnClickListener()
												{
													
													@Override
													public void onClick(DialogInterface dialog, int which) 
													{
														final String selectedshift=shift[which];
														final int selshiftnumb=which;
														
														Patient_Verify_Booked_Appointment pvba=new Patient_Verify_Booked_Appointment();
														int flag=pvba.BookedVerify(docemail, patemail, selecteddate, selectedshift);
														if(flag==1)
														{
															AlertDialog.Builder ae=new AlertDialog.Builder(Book_Appointment.this);
															ae.setTitle("Appointment Already Booked");
															ae.setMessage("You have already booked appointment for the doctor for same date and shift !\nPlease Select another date or shift !");
															ae.setPositiveButton("OK", null);
															ae.show();
														}
														else
														{
															Doctor_Fetch_Available_Appoinment_Date_Time dfaadt=new Doctor_Fetch_Available_Appoinment_Date_Time();
															final ArrayList<String> timeList=dfaadt.Fetch_Date_Time(docemail, selecteddate, selectedshift);
														
															final ArrayList<ArrayList<String>> timeslots=new ArrayList<ArrayList<String>>();
														
															if(timeList.size()>0)
															{
																System.out.println("Inside first if");
																for(String time: timeList)
																{
																	int FromHour=Integer.parseInt(time.substring(0, time.indexOf(":")));
																	int FromMins=Integer.parseInt(time.substring(time.indexOf(":")+1,time.indexOf("-")-1));
																	int ToHour=Integer.parseInt(time.substring(time.indexOf("-")+2,time.lastIndexOf(":")));
																	int ToMins=Integer.parseInt(time.substring(time.lastIndexOf(":")+1));
																
																	ArrayList<String> slots=new ArrayList<String>();
															
																	int hour=FromHour;
																	int mins=FromMins;
																	String old=hour+":"+mins;
																	
																	if(mins<10)
																	{
																		String smins="0"+mins;
																		old=hour+":"+smins;
																	}
																	
																	if((ToHour-FromHour)>=1)
																	{
																		while(hour<ToHour)
																		{
																			while(mins<60)
																			{
																				mins=mins+TimeInt;
																				if(mins>=60)
																					break;
																				String strTime=hour+":"+mins;
																				if(mins<10)
																				{
																					String smins="0"+mins;
																					strTime=hour+":"+smins;
																				}
																				
																				slots.add(old+" - "+strTime);
																				old=strTime;
																			}
																	
																			if(mins==60)
																				mins=0;
																			else
																				mins=mins-60;
																	
																			hour=hour+1;
																			String st=hour+":"+mins;
																			if(mins<10)
																			{
																				String smins="0"+mins;
																				st=hour+":"+smins;
																			}
																			slots.add(old+" - "+st);
																			old=st;
																	
																			if(hour==ToHour && mins!=ToMins)
																			{
																				while(mins<ToMins)
																				{
																					mins=mins+TimeInt;
																					if(mins>ToMins)
																						break;
																					String strTime=hour+":"+mins;
																					if(mins<10)
																					{
																						String smins="0"+mins;
																						strTime=hour+":"+smins;
																					}
																					slots.add(old+" - "+strTime);
																					old=strTime;
																				}
																			}
																		}
																	}
																	else
																	{
																		while(mins<=ToMins)
																		{
																			mins=mins+TimeInt;
																			if(mins>ToMins)
																				break;
																			String strTime=hour+":"+mins;
																			if(mins<10)
																			{
																				String smins="0"+mins;
																				strTime=hour+":"+smins;
																			}
																			slots.add(old+" - "+strTime);
																			old=strTime;
																		}
																	}
																
																	timeslots.add(slots);
																}
															
																ArrayList<String> slotlist=new ArrayList<String>();
																for(ArrayList<String> al: timeslots)
																{
																	for(int i=0;i<al.size();i++)
																	{
																		slotlist.add(al.get(i));
																	}
																}
																
																Calendar c1=Calendar.getInstance();
																int todaydate1=c1.get(Calendar.DATE);
																int todaymonth1=c1.get(Calendar.MONTH)+1;
																int todayyear1=c1.get(Calendar.YEAR);
																String idate1=todaydate1+"/"+todaymonth1+"/"+todayyear1;
																
																int ihour1=c1.get(Calendar.HOUR_OF_DAY);
																
																final String slotarray[]=new String[slotlist.size()];
																for(int i=0;i<slotlist.size();i++)
																{
																	if(selecteddate.equals(idate1))
																	{
																		String h=slotlist.get(i);
																		int hour=Integer.parseInt(h.substring(0, h.indexOf(":")));
																		if(hour>ihour1)
																			slotarray[i]=slotlist.get(i);
																	}
																	else
																		slotarray[i]=slotlist.get(i);
																}
															
																AlertDialog.Builder a3=new AlertDialog.Builder(Book_Appointment.this);
																a3.setTitle("Select Time");
																a3.setItems(slotarray, new DialogInterface.OnClickListener()
																{
																
																	@Override
																	public void onClick(DialogInterface dialog, int which) 
																	{
																		String selectedTimeSlot=slotarray[which];
																		int position=0;
																		for(int i=0;i<timeslots.size();i++)
																		{
																			if(timeslots.get(i).contains(selectedTimeSlot))
																			{
																				position=i;
																				break;
																			}
																		}
																		String t=timeList.get(position);
																		String prevStart=t.substring(0, t.indexOf("-")-1);
																		String prevEnd=t.substring(t.indexOf("-")+2);
																		String selStart=selectedTimeSlot.substring(0, selectedTimeSlot.indexOf("-")-1);
																		String selEnd=selectedTimeSlot.substring(selectedTimeSlot.indexOf("-")+2);
																		String remainingTime=""; 
																		
																		if(selStart.length()<5)
																			selStart="0"+selStart;
																		
																		if(selEnd.length()<5)
																			selEnd="0"+selEnd;
																		
																		System.out.println("prevStart="+prevStart+"prevEnd="+prevEnd+"selStart="+selStart+"SelEnd="+selEnd);
																	
																		Doctor_Remove_Available_Date_Time dradt=new Doctor_Remove_Available_Date_Time();
																		dradt.Remove_Available_Day_Time(docemail, selecteddate, selectedshift, t);
																	
																		Doctor_Insert_Updated_Available_Date_Time diuadt=new Doctor_Insert_Updated_Available_Date_Time();
																	
																		if(prevStart.equals(selStart) && prevEnd.equals(selEnd))
																		{
																			System.out.println("Inside both condition if");
																		}
																		else if(prevStart.equals(selStart))
																		{
																			remainingTime=selEnd+" - "+prevEnd;
																			System.out.println("Inside start same if , remaining time= "+remainingTime);
																			diuadt.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																		}
																		else if(prevEnd.equals(selEnd))
																		{
																			remainingTime=prevStart+" - "+selStart;
																			System.out.println("Inside end same if , remaining time= "+remainingTime);
																			diuadt.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																		}
																		else
																		{
																			remainingTime=prevStart+" - "+selStart;
																			System.out.println("Inside else 1, remaining time= "+remainingTime);
																			diuadt.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																			remainingTime=selEnd+" - "+prevEnd;
																			System.out.println("Inside else 2, remaining time= "+remainingTime);
																			Doctor_Insert_Updated_Available_Date_Time diuadt2=new Doctor_Insert_Updated_Available_Date_Time();
																			diuadt2.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																		}
																	
																		Patient_Book_Appointment pba=new Patient_Book_Appointment();
																		pba.Insert_Date_Time(docemail, patemail, selecteddate, selectedDay, selectedshift, selectedTimeSlot);
																		Toast.makeText(Book_Appointment.this, "Appointment Successfully Booked !", 0).show();
																		
																		Intent i=new Intent(Book_Appointment.this,Patient_Home.class);
																		startActivity(i);
																	}
																});
																a3.setNegativeButton("Cancel", null);
																a3.show();
															}
															else
															{
																Doctor_Verify_Full_Booked_Appointment dvfba=new Doctor_Verify_Full_Booked_Appointment();
																int flag2=dvfba.FullBookedVerify(docemail, selecteddate, selectedshift);
																System.out.println("Doc Email= "+docemail+" Date Selected= "+selecteddate+" Shift Selected= "+selectedshift);
																if(flag2==1)
																{
																	AlertDialog.Builder ae1=new AlertDialog.Builder(Book_Appointment.this);
																	ae1.setTitle("Appointment Full");
																	ae1.setMessage("Appointment for the doctor for this shift is Full !\n Please Select another shift or date!");
																	ae1.setPositiveButton("OK", null);
																	ae1.show();
																}
																else
																{
																	final String time;
																	if(shift[selshiftnumb].equals("Morning"))
																		time=days.get(seldaynumb).get(2);
																	else if(shift[selshiftnumb].equals("Afternoon"))
																		time=days.get(seldaynumb).get(3);
																	else
																		time=days.get(seldaynumb).get(4);
																	
																	System.out.println("Time= "+time);
																	
																	int FromHour=Integer.parseInt(time.substring(0, time.indexOf(":")));
																	int FromMins=Integer.parseInt(time.substring(time.indexOf(":")+1,time.indexOf("-")-1));
																	int ToHour=Integer.parseInt(time.substring(time.indexOf("-")+2,time.lastIndexOf(":")));
																	int ToMins=Integer.parseInt(time.substring(time.lastIndexOf(":")+1));
																
																	ArrayList<String> slots=new ArrayList<String>();
															
																	int hour=FromHour;
																	int mins=FromMins;
																	String old=hour+":"+mins;
																	if(mins<10)
																	{
																		String smins="0"+mins;
																		old=hour+":"+smins;
																	}
															
																	if((ToHour-FromHour)>=1)
																	{
																		while(hour<ToHour)
																		{
																			while(mins<60)
																			{
																				mins=mins+TimeInt;
																				if(mins>=60)
																					break;
																				String strTime=hour+":"+mins;
																				if(mins<10)
																				{
																					String smins="0"+mins;
																					strTime=hour+":"+smins;
																				}
																				slots.add(old+" - "+strTime);
																				old=strTime;
																			}
																	
																			if(mins==60)
																				mins=0;
																			else
																				mins=mins-60;
																	
																			hour=hour+1;
																			String st=hour+":"+mins;
																			if(mins<10)
																			{
																				String smins="0"+mins;
																				st=hour+":"+smins;
																			}
																			slots.add(old+" - "+st);
																			old=st;
																	
																			if(hour==ToHour && mins!=ToMins)
																			{
																				while(mins<ToMins)
																				{
																					mins=mins+TimeInt;
																					if(mins>ToMins)
																						break;
																					String strTime=hour+":"+mins;
																					if(mins<10)
																					{
																						String smins="0"+mins;
																						strTime=hour+":"+smins;
																					}
																					slots.add(old+" - "+strTime);
																					old=strTime;
																				}
																			}
																		}
																	}
																	else
																	{
																		while(mins<=ToMins)
																		{
																			mins=mins+TimeInt;
																			if(mins>ToMins)
																				break;
																			String strTime=hour+":"+mins;
																			if(mins<10)
																			{
																				String smins="0"+mins;
																				strTime=hour+":"+smins;
																			}
																			slots.add(old+" - "+strTime);
																			old=strTime;
																		}
																	}
																	
																	Calendar c2=Calendar.getInstance();
																	int todaydate2=c2.get(Calendar.DATE);
																	int todaymonth2=c2.get(Calendar.MONTH)+1;
																	int todayyear2=c2.get(Calendar.YEAR);
																	String idate2=todaydate2+"/"+todaymonth2+"/"+todayyear2;
																	
																	int ihour2=c2.get(Calendar.HOUR_OF_DAY);
																	
																	final String slotarray[]=new String[slots.size()];
																	for(int i=0;i<slots.size();i++)
																	{
																		if(selecteddate.equals(idate2))
																		{
																			String h=slots.get(i);
																			int hour2=Integer.parseInt(h.substring(0, h.indexOf(":")));
																			if(hour2>ihour2)
																				slotarray[i]=slots.get(i);
																		}
																		else
																			slotarray[i]=slots.get(i);
																	}
																
																	AlertDialog.Builder a3=new AlertDialog.Builder(Book_Appointment.this);
																	a3.setTitle("Select Time");
																	a3.setItems(slotarray, new DialogInterface.OnClickListener()
																	{
																	
																		@Override
																		public void onClick(DialogInterface dialog, int which) 
																		{
																			String selectedTimeSlot=slotarray[which];
																			String prevStart=time.substring(0, time.indexOf("-")-1);
																			String prevEnd=time.substring(time.indexOf("-")+2);
																			String selStart=selectedTimeSlot.substring(0, selectedTimeSlot.indexOf("-")-1);
																			String selEnd=selectedTimeSlot.substring(selectedTimeSlot.indexOf("-")+2);
																			String remainingTime="";
																			
																			if(selStart.length()<5)
																				selStart="0"+selStart;
																			
																			if(selEnd.length()<5)
																				selEnd="0"+selEnd;
																			
																			System.out.println("prevStart="+prevStart+"prevEnd="+prevEnd+"selStart="+selStart+"SelEnd="+selEnd);
																		
																			Doctor_Insert_Updated_Available_Date_Time diuadt=new Doctor_Insert_Updated_Available_Date_Time();
																		
																			if(prevStart.equals(selStart) && prevEnd.equals(selEnd))
																			{
																				System.out.println("Inside both condition if");
																			}
																			else if(prevStart.equals(selStart))
																			{
																				remainingTime=selEnd+" - "+prevEnd;
																				System.out.println("Inside start same if , remaining time= "+remainingTime);
																				diuadt.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																			}
																			else if(prevEnd.equals(selEnd))
																			{
																				remainingTime=prevStart+" - "+selStart;
																				System.out.println("Inside end same if , remaining time= "+remainingTime);
																				diuadt.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																			}
																			else
																			{
																				remainingTime=prevStart+" - "+selStart;
																				System.out.println("Inside else 1, remaining time= "+remainingTime);
																				diuadt.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																				remainingTime=selEnd+" - "+prevEnd;
																				System.out.println("Inside else 2, remaining time= "+remainingTime);
																				Doctor_Insert_Updated_Available_Date_Time diuadt2=new Doctor_Insert_Updated_Available_Date_Time();
																				diuadt2.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																			}
																		
																			Patient_Book_Appointment pba=new Patient_Book_Appointment();
																			pba.Insert_Date_Time(docemail, patemail, selecteddate, selectedDay, selectedshift, selectedTimeSlot);
																			Toast.makeText(Book_Appointment.this, "Appointment Successfully Booked !", 0).show();
																			
																			Intent i2=new Intent(Book_Appointment.this,Patient_Home.class);
																			startActivity(i2);
																		}
																	});
																	a3.setNegativeButton("Cancel", null);
																	a3.show();
																
																}
															}
														}
													}
												});
												a1.setNegativeButton("Cancel", null);
												a1.show();
											}
											else
											{
												AlertDialog.Builder a11=new AlertDialog.Builder(Book_Appointment.this);
												a11.setTitle("No Available Time");
												a11.setMessage("There is no available timeslot for this day ! Please select another date for booking your appointment !");
												a11.setPositiveButton("OK", null);
												a11.show();
											}
										}
									});
								}
								adb.setPositiveButton("Book for Next Week", new DialogInterface.OnClickListener() 
								{
									
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										ArrayList<String> nextDays=new ArrayList<String>();
										
										for(ArrayList<String> a: days)
										{
											String day=a.get(1);
											
											Calendar c=Calendar.getInstance();
											int todaydate=c.get(Calendar.DATE);
											int todaymonth=c.get(Calendar.MONTH)+1;
											int todayyear=c.get(Calendar.YEAR);
											
											int currentday=c.get(Calendar.DAY_OF_WEEK);
											int setday=1;
										
											if(day.equals("Sunday"))
												setday=1;
											if(day.equals("Monday"))
												setday=2;
											if(day.equals("Tuesday"))
												setday=3;
											if(day.equals("Wednesday"))
												setday=4;
											if(day.equals("Thursday"))
												setday=5;
											if(day.equals("Friday"))
												setday=6;
											if(day.equals("Saturday"))
												setday=7;
										
											int numb=7;
											if(currentday<setday)
											{
												numb=(setday-currentday)+7;
											}
											else if(currentday>setday)
											{
												numb=7-(currentday-setday);
											}
										
											int set_date=00,set_month=00,set_year=00;
											
											if(todaymonth==01 || todaymonth==03 || todaymonth==05 || todaymonth==07 || todaymonth==Integer.parseInt("08") || todaymonth==10 || todaymonth==12)
											{
												if((todaydate+numb)<=31)
												{
													set_date=todaydate+numb;
													set_month=todaymonth;
													set_year=todayyear;
												}
												else
												{
													set_date=(todaydate+numb)-31;
													if(todaymonth<=10)
													{
														set_month=todaymonth+1;
														set_year=todayyear;
													}
													else
													{
														set_month=01;
														set_year=todayyear+1;
													}
												}
											}
											else if(todaymonth==04 || todaymonth==06 || todaymonth==Integer.parseInt("09") || todaymonth==11)
											{
												if((todaydate+numb)<=30)
												{
													set_date=todaydate+numb;
													set_month=todaymonth;
													set_year=todayyear;
												}
												else
												{
													set_date=(todaydate+numb)-30;
													set_month=todaymonth+1;
													set_year=todayyear;
													
												}
											}
											else if(todaymonth==02)
											{
												if(todayyear%4==0)
												{
													if((todaydate+numb)<=29)
													{
														set_date=todaydate+numb;
														set_month=todaymonth;
														set_year=todayyear;
													}
													else
													{
														set_date=(todaydate+numb)-29;
														set_month=todaymonth+1;
														set_year=todayyear;
														
													}
												}
												else
												{
													if((todaydate+numb)<=28)
													{
														set_date=todaydate+numb;
														set_month=todaymonth;
														set_year=todayyear;
													}
													else
													{
														set_date=(todaydate+numb)-28;
														set_month=todaymonth+1;
														set_year=todayyear;
														
													}
												}
											}
											
											String set_next_date=set_date+"/"+set_month+"/"+set_year;
											String display_date_day=day+", "+set_next_date;
											nextDays.add(display_date_day);
										}
										
										final String nextDates[]=new String[nextDays.size()];
										for(int i=0;i<nextDays.size();i++)
										{
											nextDates[i]=nextDays.get(i);
										}
										
										AlertDialog.Builder a2=new AlertDialog.Builder(Book_Appointment.this);
										a2.setTitle("Select Date");
										a2.setItems(nextDates, new DialogInterface.OnClickListener()
										{
											
											@Override
											public void onClick(DialogInterface dialog, int which) 
											{
												final String temp=nextDates[which];
												final String selectedDay=temp.substring(0, temp.indexOf(","));
												final int seldaynumb=which;
												final String selecteddate=temp.substring(temp.indexOf(",")+2);
												
												ArrayList<String> avaShift=new ArrayList<String>();
												
												for(ArrayList<String> a: days)
												{
													if(a.get(1).equals(selectedDay))
													{
														if(!a.get(2).equals("None"))
														{
															avaShift.add("Morning");
														}
														if(!a.get(3).equals("None"))
														{
															avaShift.add("Afternoon");
														}
														if(!a.get(4).equals("None"))
														{
															avaShift.add("Evening");
														}
													}
												}
												
												if(avaShift.size()>0)
												{
													final String shift[]=new String[avaShift.size()];
													for(int i=0;i<avaShift.size();i++)
													{
														shift[i]=avaShift.get(i);
													}
													
													AlertDialog.Builder a1=new AlertDialog.Builder(Book_Appointment.this);
													a1.setTitle("Select Shift");
													a1.setItems(shift, new DialogInterface.OnClickListener()
													{
														
														@Override
														public void onClick(DialogInterface dialog, int which) 
														{
															final String selectedshift=shift[which];
															final int selshiftnumb=which;
															
															Patient_Verify_Booked_Appointment pvba=new Patient_Verify_Booked_Appointment();
															int flag=pvba.BookedVerify(docemail, patemail, selecteddate, selectedshift);
															if(flag==1)
															{
																AlertDialog.Builder ae=new AlertDialog.Builder(Book_Appointment.this);
																ae.setTitle("Appointment Already Booked");
																ae.setMessage("You have already booked appointment for the doctor for same date and shift !\n Please Select another date or shift !");
																ae.setPositiveButton("OK", null);
																ae.show();
															}
															else
															{
																Doctor_Fetch_Available_Appoinment_Date_Time dfaadt=new Doctor_Fetch_Available_Appoinment_Date_Time();
																final ArrayList<String> timeList=dfaadt.Fetch_Date_Time(docemail, selecteddate, selectedshift);
															
																final ArrayList<ArrayList<String>> timeslots=new ArrayList<ArrayList<String>>();
															
																if(timeList.size()>0)
																{
																	System.out.println("Inside 2nd if");
																	for(String time: timeList)
																	{
																		int FromHour=Integer.parseInt(time.substring(0, time.indexOf(":")));
																		int FromMins=Integer.parseInt(time.substring(time.indexOf(":")+1,time.indexOf("-")-1));
																		int ToHour=Integer.parseInt(time.substring(time.indexOf("-")+2,time.lastIndexOf(":")));
																		int ToMins=Integer.parseInt(time.substring(time.lastIndexOf(":")+1));
																	
																		ArrayList<String> slots=new ArrayList<String>();
																
																		int hour=FromHour;
																		int mins=FromMins;
																		String old=hour+":"+mins;
																		if(mins<10)
																		{
																			String smins="0"+mins;
																			old=hour+":"+smins;
																		}
																
																		if((ToHour-FromHour)>=1)
																		{
																			while(hour<ToHour)
																			{
																				while(mins<60)
																				{
																					mins=mins+TimeInt;
																					if(mins>=60)
																						break;
																					String strTime=hour+":"+mins;
																					if(mins<10)
																					{
																						String smins="0"+mins;
																						strTime=hour+":"+smins;
																					}
																					slots.add(old+" - "+strTime);
																					old=strTime;
																				}
																		
																				if(mins==60)
																					mins=0;
																				else
																					mins=mins-60;
																		
																				hour=hour+1;
																				String st=hour+":"+mins;
																				if(mins<10)
																				{
																					String smins="0"+mins;
																					st=hour+":"+smins;
																				}
																				slots.add(old+" - "+st);
																				old=st;
																		
																				if(hour==ToHour && mins!=ToMins)
																				{
																					while(mins<ToMins)
																					{
																						mins=mins+TimeInt;
																						if(mins>ToMins)
																							break;
																						String strTime=hour+":"+mins;
																						if(mins<10)
																						{
																							String smins="0"+mins;
																							strTime=hour+":"+smins;
																						}
																						slots.add(old+" - "+strTime);
																						old=strTime;
																					}
																				}
																			}
																		}
																		else
																		{
																			while(mins<=ToMins)
																			{
																				mins=mins+TimeInt;
																				if(mins>ToMins)
																					break;
																				String strTime=hour+":"+mins;
																				if(mins<10)
																				{
																					String smins="0"+mins;
																					strTime=hour+":"+smins;
																				}
																				slots.add(old+" - "+strTime);
																				old=strTime;
																			}
																		}
																	
																		timeslots.add(slots);
																	}
																
																	ArrayList<String> slotlist=new ArrayList<String>();
																	for(ArrayList<String> al: timeslots)
																	{
																		for(int i=0;i<al.size();i++)
																		{
																			slotlist.add(al.get(i));
																		}
																	}
																
																	final String slotarray[]=new String[slotlist.size()];
																	for(int i=0;i<slotlist.size();i++)
																	{
																		slotarray[i]=slotlist.get(i);
																	}
																
																	AlertDialog.Builder a3=new AlertDialog.Builder(Book_Appointment.this);
																	a3.setTitle("Select Time");
																	a3.setItems(slotarray, new DialogInterface.OnClickListener()
																	{
																	
																		@Override
																		public void onClick(DialogInterface dialog, int which) 
																		{
																			String selectedTimeSlot=slotarray[which];
																			int position=0;
																			for(int i=0;i<timeslots.size();i++)
																			{
																				if(timeslots.get(i).contains(selectedTimeSlot))
																				{
																					position=i;
																					break;
																				}
																			}
																			String t=timeList.get(position);
																			String prevStart=t.substring(0, t.indexOf("-")-1);
																			String prevEnd=t.substring(t.indexOf("-")+2);
																			String selStart=selectedTimeSlot.substring(0, selectedTimeSlot.indexOf("-")-1);
																			String selEnd=selectedTimeSlot.substring(selectedTimeSlot.indexOf("-")+2);
																			String remainingTime=""; 
																			
																			if(selStart.length()<5)
																				selStart="0"+selStart;
																			
																			if(selEnd.length()<5)
																				selEnd="0"+selEnd;
																			
																			System.out.println("prevStart="+prevStart+"prevEnd="+prevEnd+"selStart="+selStart+"SelEnd="+selEnd);
																		
																			Doctor_Remove_Available_Date_Time dradt=new Doctor_Remove_Available_Date_Time();
																			dradt.Remove_Available_Day_Time(docemail, selecteddate, selectedshift, t);
																		
																			Doctor_Insert_Updated_Available_Date_Time diuadt=new Doctor_Insert_Updated_Available_Date_Time();
																		
																			if(prevStart.equals(selStart) && prevEnd.equals(selEnd))
																			{
																				System.out.println("Inside both condition if");
																			}
																			else if(prevStart.equals(selStart))
																			{
																				remainingTime=selEnd+" - "+prevEnd;
																				System.out.println("Inside start same if , remaining time= "+remainingTime);
																				diuadt.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																			}
																			else if(prevEnd.equals(selEnd))
																			{
																				remainingTime=prevStart+" - "+selStart;
																				System.out.println("Inside end same if , remaining time= "+remainingTime);
																				diuadt.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																			}
																			else
																			{
																				remainingTime=prevStart+" - "+selStart;
																				System.out.println("Inside else 1, remaining time= "+remainingTime);
																				diuadt.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																				remainingTime=selEnd+" - "+prevEnd;
																				System.out.println("Inside else 2, remaining time= "+remainingTime);
																				Doctor_Insert_Updated_Available_Date_Time diuadt2=new Doctor_Insert_Updated_Available_Date_Time();
																				diuadt2.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																			}
																		
																			Patient_Book_Appointment pba=new Patient_Book_Appointment();
																			pba.Insert_Date_Time(docemail, patemail, selecteddate, selectedDay, selectedshift, selectedTimeSlot);
																			Toast.makeText(Book_Appointment.this, "Appointment Successfully Booked !", 0).show();
																			
																			Intent i3=new Intent(Book_Appointment.this,Patient_Home.class);
																			startActivity(i3);
																		}
																	});
																	a3.setNegativeButton("Cancel", null);
																	a3.show();
																}
																else
																{
																	Doctor_Verify_Full_Booked_Appointment dvfba=new Doctor_Verify_Full_Booked_Appointment();
																	int flag2=dvfba.FullBookedVerify(docemail, selecteddate, selectedshift);
																	if(flag2==1)
																	{
																		AlertDialog.Builder ae1=new AlertDialog.Builder(Book_Appointment.this);
																		ae1.setTitle("Appointment Full");
																		ae1.setMessage("Appointment for the doctor for this shift is Full !\nPlease Select another shift or date!");
																		ae1.setPositiveButton("OK", null);
																		ae1.show();
																	}
																	else
																	{
																		final String time;
																		if(shift[selshiftnumb].equals("Morning"))
																			time=days.get(seldaynumb).get(2);
																		else if(shift[selshiftnumb].equals("Afternoon"))
																			time=days.get(seldaynumb).get(3);
																		else
																			time=days.get(seldaynumb).get(4);
																		
																		int FromHour=Integer.parseInt(time.substring(0, time.indexOf(":")));
																		int FromMins=Integer.parseInt(time.substring(time.indexOf(":")+1,time.indexOf("-")-1));
																		int ToHour=Integer.parseInt(time.substring(time.indexOf("-")+2,time.lastIndexOf(":")));
																		int ToMins=Integer.parseInt(time.substring(time.lastIndexOf(":")+1));
																	
																		ArrayList<String> slots=new ArrayList<String>();
																
																		int hour=FromHour;
																		int mins=FromMins;
																		String old=hour+":"+mins;
																		if(mins<10)
																		{
																			String smins="0"+mins;
																			old=hour+":"+smins;
																		}
																
																		if((ToHour-FromHour)>=1)
																		{
																			while(hour<ToHour)
																			{
																				while(mins<60)
																				{
																					mins=mins+TimeInt;
																					if(mins>=60)
																						break;
																					String strTime=hour+":"+mins;
																					if(mins<10)
																					{
																						String smins="0"+mins;
																						strTime=hour+":"+smins;
																					}
																					slots.add(old+" - "+strTime);
																					old=strTime;
																				}
																		
																				if(mins==60)
																					mins=0;
																				else
																					mins=mins-60;
																		
																				hour=hour+1;
																				String st=hour+":"+mins;
																				if(mins<10)
																				{
																					String smins="0"+mins;
																					st=hour+":"+smins;
																				}
																				slots.add(old+" - "+st);
																				old=st;
																		
																				if(hour==ToHour && mins!=ToMins)
																				{
																					while(mins<ToMins)
																					{
																						mins=mins+TimeInt;
																						if(mins>ToMins)
																							break;
																						String strTime=hour+":"+mins;
																						if(mins<10)
																						{
																							String smins="0"+mins;
																							strTime=hour+":"+smins;
																						}
																						slots.add(old+" - "+strTime);
																						old=strTime;
																					}
																				}
																			}
																		}
																		else
																		{
																			while(mins<=ToMins)
																			{
																				mins=mins+TimeInt;
																				if(mins>ToMins)
																					break;
																				String strTime=hour+":"+mins;
																				if(mins<10)
																				{
																					String smins="0"+mins;
																					strTime=hour+":"+smins;
																				}
																				slots.add(old+" - "+strTime);
																				old=strTime;
																			}
																		}
																		
																		final String slotarray[]=new String[slots.size()];
																		for(int i=0;i<slots.size();i++)
																		{
																			slotarray[i]=slots.get(i);
																		}
																	
																		AlertDialog.Builder a3=new AlertDialog.Builder(Book_Appointment.this);
																		a3.setTitle("Select Time");
																		a3.setItems(slotarray, new DialogInterface.OnClickListener()
																		{
																		
																			@Override
																			public void onClick(DialogInterface dialog, int which) 
																			{
																				String selectedTimeSlot=slotarray[which];
																				String prevStart=time.substring(0, time.indexOf("-")-1);
																				String prevEnd=time.substring(time.indexOf("-")+2);
																				String selStart=selectedTimeSlot.substring(0, selectedTimeSlot.indexOf("-")-1);
																				String selEnd=selectedTimeSlot.substring(selectedTimeSlot.indexOf("-")+2);
																				String remainingTime=""; 
																				
																				if(selStart.length()<5)
																					selStart="0"+selStart;
																				
																				if(selEnd.length()<5)
																					selEnd="0"+selEnd;
																				
																				System.out.println("prevStart="+prevStart+"prevEnd="+prevEnd+"selStart="+selStart+"SelEnd="+selEnd);
																				
																				Doctor_Insert_Updated_Available_Date_Time diuadt=new Doctor_Insert_Updated_Available_Date_Time();
																			
																				if(prevStart.equals(selStart) && prevEnd.equals(selEnd))
																				{
																					System.out.println("Inside both condition if");
																				}
																				else if(prevStart.equals(selStart))
																				{
																					remainingTime=selEnd+" - "+prevEnd;
																					System.out.println("Inside start same if , remaining time= "+remainingTime);
																					diuadt.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																				}
																				else if(prevEnd.equals(selEnd))
																				{
																					remainingTime=prevStart+" - "+selStart;
																					System.out.println("Inside end same if , remaining time= "+remainingTime);
																					diuadt.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																				}
																				else
																				{
																					remainingTime=prevStart+" - "+selStart;
																					System.out.println("Inside else 1, remaining time= "+remainingTime);
																					diuadt.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																					remainingTime=selEnd+" - "+prevEnd;
																					System.out.println("Inside else 2, remaining time= "+remainingTime);
																					Doctor_Insert_Updated_Available_Date_Time diuadt2=new Doctor_Insert_Updated_Available_Date_Time();
																					diuadt2.Insert_Date_Time(docemail, selecteddate, selectedDay, selectedshift, remainingTime);
																				}
																			
																				Patient_Book_Appointment pba=new Patient_Book_Appointment();
																				pba.Insert_Date_Time(docemail, patemail, selecteddate, selectedDay, selectedshift, selectedTimeSlot);
																				Toast.makeText(Book_Appointment.this, "Appointment Successfully Booked !", 0).show();
																				
																				Intent i4=new Intent(Book_Appointment.this,Patient_Home.class);
																				startActivity(i4);
																			}
																		});
																		a3.setNegativeButton("Cancel", null);
																		a3.show();
																	
																	}
																}
															}
														}
													});
													a1.setNegativeButton("Cancel", null);
													a1.show();
												}
												else
												{
													AlertDialog.Builder a11=new AlertDialog.Builder(Book_Appointment.this);
													a11.setTitle("No Available Time");
													a11.setMessage("There is no available timeslot for this day ! Please select another date for booking your appointment !");
													a11.setPositiveButton("OK", null);
													a11.show();
												}
											}
										});
										a2.setNegativeButton("Cancel", null);
										a2.show();
									}
								});
								adb.setNegativeButton("Cancel", null);
								adb.show();
							}
						});
					}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_book__appointment, menu);
		return true;
	}

	@Override
	public void onBackPressed() 
	{
		Intent i4=new Intent(Book_Appointment.this,Patient_Home.class);
		startActivity(i4);
		return;
	}
}
