package ir.shes.calendar.util;
import java.util.*;
import android.content.res.*;
import android.content.*;
import org.xmlpull.v1.*;
import android.util.*;
import ir.shes.calendar.*;
/*
Map resource in `res/xml`:

<?xml version="1.0" encoding="utf-8"?>
<map linked="true">
<entry key="key1">value1</entry>
<entry key="key2">value2</entry>
<entry key="key3">value3</entry>
</map>

Resource parser:
*/
public final class ResourceUtils {
	public final static String XCalendarH="ObservedHijriCalendar";
	public final static String XCalendarG="GregorianCalendar";
	public final static String XCalendarP="PersianCalendar";
	public static HashMap<Integer,String> eventG,eventH,eventP;
	public static HashMap<Integer,Boolean> vacationG,vacationH,vacationP;
	
	public ResourceUtils(Context c)
	{
		eventP=new HashMap<Integer,String>();
		eventH=new HashMap<Integer,String>();
		eventG=new HashMap<Integer,String>();
		vacationP=new HashMap<Integer,Boolean>();
		vacationH=new HashMap<Integer,Boolean>();
		vacationG=new HashMap<Integer,Boolean>();
		getHashMapResource(c,R.xml.events_gregorian);
		getHashMapResource(c,R.xml.events_hijri);
		getHashMapResource(c,R.xml.events_persian);
		getHashMapResource(c,R.xml.events_misc);
	}
	public final static void getHashMapResource(Context c, int hashMapResId) {
	//	Map<String,String> map = null;
		XmlResourceParser parser = c.getResources().getXml(hashMapResId);

		String key = null, value = null;
		String title,day,month,isVacation,xCalendar;

		try {
			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					Log.d("utils","Start document");
				} else if (eventType == XmlPullParser.START_TAG) {
					if (parser.getName().equals("XCalendarEvents")) {
				
					//	map =  new HashMap<String, String>();
					} else if (parser.getName().equals("Event")) {
						title = parser.getAttributeValue(null, "Title");
						day = parser.getAttributeValue(null, "Day");
					
						month = parser.getAttributeValue(null, "Month");
						
						isVacation = parser.getAttributeValue(null, "IsVacation");
						
						
						xCalendar = parser.getAttributeValue(null, "XCalendar");
						if (null == title) {
							parser.close();
							return ;
						}
						if (day!=null && month!=null && xCalendar!=null) 
						{
							int dayMonth=Integer.parseInt(month)*100+Integer.parseInt(day);
							boolean vacation=false;
							if (isVacation!=null) if (isVacation.equals("1")) vacation=true;
							
							
					        if (xCalendar.equals( XCalendarP)){
									eventP.put(dayMonth,title);
									if (vacation) vacationP.put(dayMonth,true);
							} else  if (xCalendar.equals( XCalendarH)){
								
									eventH.put(dayMonth,title);
									if (vacation) vacationH.put(dayMonth,true);
							} else  if (xCalendar.equals( XCalendarG)){
								
								
									eventG.put(dayMonth,title);
									if (vacation) vacationG.put(dayMonth,true);
								
							}
						}
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					if (parser.getName().equals("Event")) {
						
					//	map.put(key, value);
						key = null;
						value = null;
					}
				} else if (eventType == XmlPullParser.TEXT) {
					if (null != key) {
						value = parser.getText();
					}
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ;
		}

		//return map;
	}
	
}
