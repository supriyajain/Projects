package com.weathersearch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class MainActivity extends Activity
{

	String loc="",type="",url="",unit="";
	String city="",region="",img="",temp="",data="",link="",feed="",forecastdata="";
	int flag=0;
	Bitmap image;
	AlertDialog alert;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btn=(Button)findViewById(R.id.searchBtn);
		final RadioGroup rg=(RadioGroup)findViewById(R.id.unit);
		final RadioButton rc=(RadioButton)findViewById(R.id.rc);
		final RadioButton rf=(RadioButton)findViewById(R.id.rf);
		final EditText text=(EditText)findViewById(R.id.searchTxt);
		
		btn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				loc=text.getText().toString().trim();
				if(loc=="" || loc==null)
				{
					Toast.makeText(MainActivity.this,"Please enter the Location !",Toast.LENGTH_SHORT).show();
				}
				else
				{
					if(loc.matches("\\d+$"))
					{
						if(loc.length()<5 || loc.length()>5)
						{
							Toast.makeText(MainActivity.this,"Zipcode must be 5 digits\nExample: 90089",Toast.LENGTH_SHORT).show();
						}
						else if(loc.length()==5)
						{
							type="zip";
							int id=rg.getCheckedRadioButtonId();
							if(id!=-1)
							{
								RadioButton rbtn=(RadioButton)findViewById(id);
								if(rbtn.equals(rf))
									unit="f";
								else if(rbtn.equals(rc))
									unit="c";
							}
							url="http://cs-server.usc.edu:38599/examples/servlet/jserv?location="+loc+"&type="+type+"&tempUnit="+unit;
							LongOperation lo=new LongOperation();
							lo.execute(new String[]{url});
						}
					}
					else
					{
						if(loc.matches("^[_0-9a-zA-Z \t\n\r\f'\\-.]+([,][ \t\n\r\f]*[_0-9a-zA-Z \t\n\r\f'\\-.]+){1,2}$"))
						{
							type="city";
							int id=rg.getCheckedRadioButtonId();
							if(id!=-1)
							{
								RadioButton rbtn=(RadioButton)findViewById(id);
								if(rbtn.equals(rf))
									unit="f";
								else if(rbtn.equals(rc))
									unit="c";
							}
							try
							{
								String eloc=URLEncoder.encode(loc,"UTF-8");
								url="http://cs-server.usc.edu:38599/examples/servlet/jserv?location="+eloc+"&type="+type+"&tempUnit="+unit;
								LongOperation lo=new LongOperation();
								lo.execute(new String[]{url});
							}
							catch (UnsupportedEncodingException e) 
							{
								e.printStackTrace();
							}
						}
						else if(loc.matches("[_0-9a-zA-Z \t\n\r\f'\\-.]+"))
						{
							Toast.makeText(MainActivity.this,"Location must include state or country separated by comma\nExample: Los Angeles, CA or Los Angeles, USA or Los Angeles, CA, USA",Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(MainActivity.this,"Location entered is invalid !",Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		});
		
		
		TextView sh1=(TextView)findViewById(R.id.shareweather);
		sh1.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				AlertDialog.Builder b1=new AlertDialog.Builder(MainActivity.this);
				LayoutInflater inflater=MainActivity.this.getLayoutInflater();
				View v1=inflater.inflate(R.layout.shareweather,null);
				b1.setView(v1);
				b1.setTitle("Post to Facebook");
				
				Button btn1=(Button)v1.findViewById(R.id.button1);
				btn1.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
						flag=0;
						posttoFB();
						alert.dismiss();
					}
				});
				
				Button btn2=(Button)v1.findViewById(R.id.button2);
				btn2.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						alert.dismiss();
					}
				});
				alert=b1.create();
				alert.show();
			}
		});
		
		TextView sh2=(TextView)findViewById(R.id.shareforecast);
		sh2.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				AlertDialog.Builder b2=new AlertDialog.Builder(MainActivity.this);
				LayoutInflater inflater=MainActivity.this.getLayoutInflater();
				View v2=inflater.inflate(R.layout.shareweather,null);
				b2.setView(v2);
				b2.setTitle("Post to Facebook");
				
				Button btn1=(Button)v2.findViewById(R.id.button1);
				btn1.setText("Post Weather Forecast");
				btn1.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
						flag=1;
						posttoFB();
						alert.dismiss();
					}
				});
				
				Button btn2=(Button)v2.findViewById(R.id.button2);
				btn2.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						alert.dismiss();
					}
				});
				alert=b2.create();
				alert.show();
			}
		});
		
	}
	
	private class LongOperation extends AsyncTask<String, Void, String>
	{

        @Override
        protected String doInBackground(String... params)
        {
        	String result="";
        	try
        	{
        		result=call(url);
        		JSONObject jobj=new JSONObject(result);
        		if(jobj!=null)
        		{
        			feed=jobj.getJSONObject("weather").getString("feed");
    				if(!feed.equalsIgnoreCase("error"))
    				{
    					img=jobj.getJSONObject("weather").getString("img");
    					URL url1=new URL(img);
    					image=BitmapFactory.decodeStream(url1.openConnection().getInputStream());
    				}
        		}
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
    			AlertDialog.Builder b3=new AlertDialog.Builder(MainActivity.this);
    			b3.setTitle("Error");
    			b3.setMessage(e.getMessage());
    			b3.setPositiveButton("OK", null);
    			b3.show();
        	}
        	return result;
        }
        
        @Override
        protected void onPostExecute(String result)
        {       
        	try
        	{
        	JSONObject jobj=new JSONObject(result);
			if(jobj!=null)
			{
				TextView t1=(TextView)findViewById(R.id.city);
				TextView t2=(TextView)findViewById(R.id.region);
				ImageView i1=(ImageView)findViewById(R.id.wimg);
				TextView t3=(TextView)findViewById(R.id.wdata);
				TextView t4=(TextView)findViewById(R.id.wtemp);
				TextView t5=(TextView)findViewById(R.id.wforecast);
				TableLayout tl=(TableLayout)findViewById(R.id.table);
				TextView share1=(TextView)findViewById(R.id.shareweather);
				TextView share2=(TextView)findViewById(R.id.shareforecast);
				
				feed=jobj.getJSONObject("weather").getString("feed");
				if(feed.equalsIgnoreCase("error"))
				{
					TextView t0=(TextView)findViewById(R.id.city);
					t0.setVisibility(1);
					t0.setText("Weather information can not be found !");
					t2.setVisibility(-1);
					i1.setVisibility(-1);
					t3.setVisibility(-1);
					t4.setVisibility(-1);
					t5.setVisibility(-1);
					tl.setVisibility(-1);
					share1.setVisibility(-1);
					share2.setVisibility(-1);
				}
				else
				{
					city=jobj.getJSONObject("weather").getJSONObject("location").getString("city");
					t1.setVisibility(1);
					t1.setText(city);
					
					region=jobj.getJSONObject("weather").getJSONObject("location").getString("region")+", "+jobj.getJSONObject("weather").getJSONObject("location").getString("country");
					t2.setVisibility(1);
					t2.setText(region);
					
					i1.setVisibility(1);
					i1.setImageBitmap(image);
					
					data=jobj.getJSONObject("weather").getJSONObject("condition").getString("text");
					t3.setVisibility(1);
					t3.setText(data);
					
					temp=jobj.getJSONObject("weather").getJSONObject("condition").getString("temp")+"\u00B0"+unit.toUpperCase();
					t4.setVisibility(1);
					t4.setText(temp);
					
					link=jobj.getJSONObject("weather").getString("link");
					
					t5.setVisibility(1);
					tl.setVisibility(1);
					
					JSONArray arr=jobj.getJSONObject("weather").getJSONArray("forecast");
					
					TextView t11=(TextView)findViewById(R.id.t11);
					t11.setText(arr.getJSONObject(0).getString("day"));
					
					TextView t12=(TextView)findViewById(R.id.t12);
					t12.setText(arr.getJSONObject(0).getString("text"));
					
					TextView t13=(TextView)findViewById(R.id.t13);
					t13.setText(arr.getJSONObject(0).getString("high")+"\u00B0"+unit.toUpperCase());
					
					TextView t14=(TextView)findViewById(R.id.t14);
					t14.setText(arr.getJSONObject(0).getString("low")+"\u00B0"+unit.toUpperCase());
					
					TextView t21=(TextView)findViewById(R.id.t21);
					t21.setText(arr.getJSONObject(1).getString("day"));
					
					TextView t22=(TextView)findViewById(R.id.t22);
					t22.setText(arr.getJSONObject(1).getString("text"));
					
					TextView t23=(TextView)findViewById(R.id.t23);
					t23.setText(arr.getJSONObject(1).getString("high")+"\u00B0"+unit.toUpperCase());
					
					TextView t24=(TextView)findViewById(R.id.t24);
					t24.setText(arr.getJSONObject(1).getString("low")+"\u00B0"+unit.toUpperCase());
					
					TextView t31=(TextView)findViewById(R.id.t31);
					t31.setText(arr.getJSONObject(2).getString("day"));
					
					TextView t32=(TextView)findViewById(R.id.t32);
					t32.setText(arr.getJSONObject(2).getString("text"));
					
					TextView t33=(TextView)findViewById(R.id.t33);
					t33.setText(arr.getJSONObject(2).getString("high")+"\u00B0"+unit.toUpperCase());
					
					TextView t34=(TextView)findViewById(R.id.t34);
					t34.setText(arr.getJSONObject(2).getString("low")+"\u00B0"+unit.toUpperCase());
					
					TextView t41=(TextView)findViewById(R.id.t41);
					t41.setText(arr.getJSONObject(3).getString("day"));
					
					TextView t42=(TextView)findViewById(R.id.t42);
					t42.setText(arr.getJSONObject(3).getString("text"));
					
					TextView t43=(TextView)findViewById(R.id.t43);
					t43.setText(arr.getJSONObject(3).getString("high")+"\u00B0"+unit.toUpperCase());
					
					TextView t44=(TextView)findViewById(R.id.t44);
					t44.setText(arr.getJSONObject(3).getString("low")+"\u00B0"+unit.toUpperCase());
					
					TextView t51=(TextView)findViewById(R.id.t51);
					t51.setText(arr.getJSONObject(4).getString("day"));
					
					TextView t52=(TextView)findViewById(R.id.t52);
					t52.setText(arr.getJSONObject(4).getString("text"));
					
					TextView t53=(TextView)findViewById(R.id.t53);
					t53.setText(arr.getJSONObject(4).getString("high")+"\u00B0"+unit.toUpperCase());
					
					TextView t54=(TextView)findViewById(R.id.t54);
					t54.setText(arr.getJSONObject(4).getString("low")+"\u00B0"+unit.toUpperCase());
					
					share1.setVisibility(1);
					share2.setVisibility(1);
					
					for(int i=0;i<=4;i++)
						forecastdata+=arr.getJSONObject(i).getString("day")+": "+arr.getJSONObject(i).getString("text")+", "+arr.getJSONObject(i).getString("high")+"/"+arr.getJSONObject(i).getString("low")+"\u00B0"+unit.toUpperCase()+"; ";
				}
			}
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
    			AlertDialog.Builder b3=new AlertDialog.Builder(MainActivity.this);
    			b3.setTitle("Error");
    			b3.setMessage(e.getMessage());
    			b3.setPositiveButton("OK", null);
    			b3.show();
        	}
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
        
        public String call(String url)
    	{
    		String result="";
    		try
    		{
    			URL u=new URL(url);
    			URLConnection uc=u.openConnection();
    			if(uc==null)
    			{
    				System.out.println("not able to stablish connection for url "+url);
    			}
    			BufferedReader in=new BufferedReader(new InputStreamReader(uc.getInputStream()));
    			StringBuilder sb = new StringBuilder();
    			String line = null;
    			while ((line = in.readLine()) != null) 
    			{
    				sb.append(line + "\n");
    			}
    			result = sb.toString();
    			in.close();
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    			AlertDialog.Builder b3=new AlertDialog.Builder(MainActivity.this);
    			b3.setTitle("Error");
    			b3.setMessage(e.getMessage());
    			b3.setPositiveButton("OK", null);
    			b3.show();
    		}
    		return result;
    	}
	}
	
		@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void posttoFB()
	{
		Session.openActiveSession(this, true, new Session.StatusCallback() 
		{
			@SuppressWarnings("deprecation")
			@Override
		    public void call(Session session, SessionState state, Exception exception)
			{
		        if (session.isOpened())
		        {
		        	Request.executeMeRequestAsync(session, new Request.GraphUserCallback()
		        	{
		        		@Override
		        		public void onCompleted(GraphUser user, Response response)
		        		{
		        			if (user != null)
		        			{
		        				if(flag==0)
		        					publishFeedDialog1();
		        				else if(flag==1)
		        					publishFeedDialog2();
		        			}
		        		}
		        	});
		        }
			}
		});
	}
	
	 @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data)
	 {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	  }
	 
	 
	 private void publishFeedDialog1() 
	 {
		    Bundle params = new Bundle();
		    params.putString("name", city+", "+region);
		    params.putString("link", feed);
		    params.putString("picture", img);
		    params.putString("caption", "The current condition for "+city+" is "+data+",");
		    params.putString("description", "Temperature is "+temp+",");
		    try
		    {
	        	JSONObject property = new JSONObject();
	        	property.put("text", "here");
				property.put("href", link);
				JSONObject properties = new JSONObject();
				properties.put("Look at details", property);
				params.putString("properties", properties.toString());
	        }
		    catch (JSONException e)
		    {
				e.printStackTrace();
			}
		    
		    WebDialog feedDialog = (
		        new WebDialog.FeedDialogBuilder(MainActivity.this,
		            Session.getActiveSession(),
		            params))
		        .setOnCompleteListener(new OnCompleteListener() {

		            @Override
		            public void onComplete(Bundle values,
		                FacebookException error) {
		                if (error == null) {
		                    final String postId = values.getString("post_id");
		                    if (postId != null) {
		                        Toast.makeText(MainActivity.this,
		                            "Posted Successfully",
		                            Toast.LENGTH_SHORT).show();
		                    } else {
		                        Toast.makeText(MainActivity.this.getApplicationContext(), 
		                            "Publish cancelled", 
		                            Toast.LENGTH_SHORT).show();
		                    }
		                } else if (error instanceof FacebookOperationCanceledException) {
		                    Toast.makeText(MainActivity.this.getApplicationContext(), 
		                        "Publish cancelled", 
		                        Toast.LENGTH_SHORT).show();
		                } else {
		                    Toast.makeText(MainActivity.this.getApplicationContext(), 
		                        "Error posting story", 
		                        Toast.LENGTH_SHORT).show();
		                }
		            }

		        })
		        .build();
		    feedDialog.show();
	 }
	 
	 private void publishFeedDialog2() 
	 {
		 	Bundle params = new Bundle();
		    params.putString("name", city+", "+region);
		    params.putString("link", feed);
		    params.putString("picture", "http://cs-server.usc.edu:38599/examples/servlets/weather.jpg");
		    params.putString("caption", "Weather Forecast for "+city);
		    params.putString("description", forecastdata);
		    try
		    {
	        	JSONObject property = new JSONObject();
	        	property.put("text", "here");
				property.put("href", link);
				JSONObject properties = new JSONObject();
				properties.put("Look at details", property);
				params.putString("properties", properties.toString());
	        }
		    catch (JSONException e)
		    {
				e.printStackTrace();
			}
		    
		    WebDialog feedDialog = (
		        new WebDialog.FeedDialogBuilder(MainActivity.this,
		            Session.getActiveSession(),
		            params))
		        .setOnCompleteListener(new OnCompleteListener() {

		            @Override
		            public void onComplete(Bundle values,
		                FacebookException error) {
		                if (error == null) {
		                    final String postId = values.getString("post_id");
		                    if (postId != null) {
		                        Toast.makeText(MainActivity.this,
		                            "Posted Successfully",
		                            Toast.LENGTH_SHORT).show();
		                    } else {
		                        Toast.makeText(MainActivity.this.getApplicationContext(), 
		                            "Publish cancelled", 
		                            Toast.LENGTH_SHORT).show();
		                    }
		                } else if (error instanceof FacebookOperationCanceledException) {
		                    Toast.makeText(MainActivity.this.getApplicationContext(), 
		                        "Publish cancelled", 
		                        Toast.LENGTH_SHORT).show();
		                } else {
		                    Toast.makeText(MainActivity.this.getApplicationContext(), 
		                        "Error posting story", 
		                        Toast.LENGTH_SHORT).show();
		                }
		            }

		        })
		        .build();
		    feedDialog.show();
	 }
	 
}
