package Block_User;


import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Block_User 
{
	
	int flag=0;
	
	public void save_block_user(String emailuser, String emailblockuser)
	{
		ParseObject val=new ParseObject("Block_User");
		val.put("User_ID", emailuser);
		val.put("Block_User_ID", emailblockuser);
		val.saveEventually();
	}
	
	
	public void del_block_user(String emailuser, String emailblockuser)
	{
		ParseQuery query = new ParseQuery("Block_User");
		query.whereEqualTo("User_ID", emailuser);
		query.whereEqualTo("Block_User_ID",emailblockuser);
		try
		{
			ParseObject val=query.getFirst();
			if (val != null) 
			{
		    	val.deleteInBackground();
			}
		}
		catch(ParseException e)
		{
			System.out.println(e.getCode());
		}
	}
	
	
	public ArrayList<String> fetch_block_user(String emailuser)
	{
		final ArrayList<String> data=new ArrayList<String>();
		
		ParseQuery query = new ParseQuery("Block_User");
		query.whereEqualTo("User_ID", emailuser);
		try
		{
			List<ParseObject> objects=query.find();	
			for(ParseObject object: objects)
		    {
		    		String pemail=object.getString("Block_User_ID");
		    		if(!data.contains(pemail))
						data.add(pemail);
		    }
		}
		catch(ParseException e)
		{
			System.out.println(e.getCode());
		}
		  
		return data;
	}
	
	public int Verify_block_user(String emailuser, String emailblockuser)
	{
		ParseQuery query = new ParseQuery("Block_User");
		query.whereEqualTo("User_ID", emailuser);
		query.whereEqualTo("Block_User_ID", emailblockuser);
		try
		{
			ParseObject o=query.getFirst();
			if (o != null) 
				flag=1;
		}
		catch(ParseException e)
		{
			System.out.println(e.getCode());
		}
		
		return flag;
	}
}
