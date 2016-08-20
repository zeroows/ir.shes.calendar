package ir.shes.calendar;
import android.app.*;
import android.os.*;
import ir.shes.calendar.util.*;
import java.util.*;
import android.widget.*;
import java.text.*;
import org.joda.time.*;
import org.joda.time.chrono.*;

public class MainActivity extends Activity implements PersianDatePicker.OnDateChangedListener
{
	PersianDatePicker persianDatePicker;
	TextView mText1,mText2,mText3;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mText1=(TextView) findViewById(R.id.shamsi);
		mText2=(TextView) findViewById(R.id.hejri);
		
		mText3=(TextView) findViewById(R.id.miladi);
		persianDatePicker=(PersianDatePicker)findViewById(R.id.PersianDatePicker);
		persianDatePicker.setOnDateChangedListener(this);
	
		PersianCalendar pCalendar = persianDatePicker.getPersianCalendar();
		showCalendar(pCalendar);
		//persianCalendar.setPersianDate(1393, 6, 28);
	//	persianDatePicker.setDisplayPersianDate(persianCalendar);
		//Date d = persianDatePicker.getDisplayDate();

		//PersianCalendar pCal = persianDatePicker.getDisplayPersianDate();
		
	}
private void showCalendar(PersianCalendar pCalendar)
{
	int day=pCalendar.getPersianDay();
	mText1.setText(pCalendar.getPersianLongDate()+" "+persianDatePicker.getPEvent(day));
	mText2.setText(pCalendar.writeIslamicDate()+" "+persianDatePicker.getHEvent(day)); 
 
	
	SimpleDateFormat df = new SimpleDateFormat("EEEE yyyy-MMMM(MM)-dd");
	String formattedDate = df.format(pCalendar.getTime());
	mText3.setText(formattedDate+" "+persianDatePicker.getGEvent(day));
	
}
	@Override
	public void onDateChanged(PersianCalendar pCalendar, int newYear, int newMonth, int newDay)
	{
		// TODO: Implement this method
		showCalendar(pCalendar);
		
	}

	
}
