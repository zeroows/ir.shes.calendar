package ir.shes.calendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import ir.shes.calendar.util.*;
import android.content.*;

public class MyWidgetProvider extends AppWidgetProvider {
	PersianCalendar pCalendar;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		// initializing widget layout
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
				
		pCalendar=new PersianCalendar(context);
		// register for button event
		Intent configIntent = new Intent(context, MainActivity.class);

    PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);

    remoteViews.setOnClickPendingIntent(R.id.title, configPendingIntent);
		
		// updating view with initial data
		remoteViews.setTextViewText(R.id.title, getTitle());
		remoteViews.setTextViewText(R.id.desc, getDesc());

		// request for widget update
		pushWidgetUpdate(context, remoteViews);
	}

	

	private String getDesc() {
		
		return pCalendar.getTodayEvent();
	}

	private String getTitle() {
		return pCalendar.getPersianLongDateAndTime();
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context,
				MyWidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(myWidget, remoteViews);
	}
}
