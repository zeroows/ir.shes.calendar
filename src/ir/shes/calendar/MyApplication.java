package ir.shes.calendar;
import android.app.*;
import ir.shes.calendar.util.*;
import android.content.*;
import java.io.*;
import java.util.*;

public class MyApplication extends Application {
	
	public PersianCalendar pCalendar;
	int hAdjust;
	
	@Override
	public void onCreate() {
		super.onCreate();
		if (loadConfig(getCacheDir().getParent(),getApplicationContext()))
			pCalendar=new PersianCalendar(getApplicationContext(),hAdjust);
		else
			pCalendar=new PersianCalendar(getApplicationContext());
			
	}

	@Override
	public void onTerminate()
	{
		// TODO: Implement this method
		super.onTerminate();
		writeConfig(getCacheDir().getParent(),getApplicationContext());
	}
		
	public void writeConfig(String  configPath,Context context)
	{

		HashMap<String,String>	config =new HashMap<String,String>();
		File storagePath = new File(configPath); 
	
		try{	
			File myFile = new File(storagePath, "config"); 

	 		FileOutputStream fos =new  FileOutputStream(myFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			config.put("hAdjust",String.valueOf(pCalendar.hAdjust));
			oos.writeObject(config);
			oos.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean loadConfig(String  configPath,Context context)
	{

		HashMap<String,String>	config ;
		try 
		{
			File storagePath = new File(configPath); 
			File myFile = new File(storagePath, "config"); 
			FileInputStream fileInputStream = new FileInputStream(myFile);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			config = (HashMap<String,String>)objectInputStream.readObject();
			if (config !=null)
			if (config.containsKey("hAdjust")) hAdjust=Integer.parseInt( config.get("hAdjust"));
			return true;
		}
		catch(ClassNotFoundException | IOException | ClassCastException e) {
			e.printStackTrace();
			return false;
		}
	}		
		
	}
