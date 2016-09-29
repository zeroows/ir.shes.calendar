package ir.shes.calendar;
import android.app.*;
import android.os.*;
import ir.shes.calendar.util.*;
import java.util.*;
import android.widget.*;
import java.text.*;
import android.content.*;

public class MainActivity extends Activity implements PersianDatePicker.OnDateChangedListener
{
	PersianDatePicker persianDatePicker;
	TextView mText1,mText2,mText3;
	BroadcastReceiver tickReceiver;
	MyApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		app=(MyApplication) getApplicationContext();
		setContentView(R.layout.main);
		mText1=(TextView) findViewById(R.id.shamsi);
		mText2=(TextView) findViewById(R.id.hejri);
		
		mText3=(TextView) findViewById(R.id.miladi);
		persianDatePicker=(PersianDatePicker)findViewById(R.id.PersianDatePicker);
		persianDatePicker.setPersianCalendar(app.pCalendar);
		persianDatePicker.setOnDateChangedListener(this);
	
		PersianCalendar pCalendar = persianDatePicker.getPersianCalendar();
		showCalendar(pCalendar);
		
	}

	
private void showCalendar(PersianCalendar pCalendar)
{
	int day=pCalendar.getPersianDay();
	mText1.setText(pCalendar.getPersianLongDate()+" "+pCalendar.getPEvent(day));
	mText2.setText(pCalendar.writeIslamicDate()+" "+pCalendar.getHEvent(day)); 
 
	
	SimpleDateFormat df = new SimpleDateFormat("EEEE yyyy-MMMM(MM)-dd");
	String formattedDate = df.format(pCalendar.getTime());
	mText3.setText(formattedDate+" "+pCalendar.getGEvent(day));
	
}
	@Override
	public void onDateChanged(PersianCalendar pCalendar)
	{
		// TODO: Implement this method
		app.pCalendar=pCalendar;
		showCalendar(pCalendar);
		
	}

	@Override
	public void onHijriAdjust(PersianCalendar persianCalendar, int hijriAdjust)
	{
		// TODO: Implement this method
		app.pCalendar=persianCalendar;
		showCalendar(persianCalendar);
		app.writeConfig(getCacheDir().getParent(),app);
	}


	@Override
	public void onAddClicked(PersianCalendar persianCalendar)
	{
		// TODO: Implement this method
	}


	
}
