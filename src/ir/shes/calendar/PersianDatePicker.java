package ir.shes.calendar;

import ir.shes.calendar.util.PersianCalendar;
import ir.shes.calendar.util.PersianCalendarConstants;
import ir.shes.calendar.util.PersianCalendarUtils;

import java.util.Date;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.*;
import android.util.*;
import ir.shes.calendar.*;
import java.util.*;
import android.graphics.*;
import android.text.*;
import ir.shes.calendar.util.*;

public class PersianDatePicker extends LinearLayout implements View.OnClickListener,View.OnLongClickListener {

	private OnDateChangedListener mListener;
/*	private NumberPicker yearNumberPicker;
	private NumberPicker monthNumberPicker;
	private NumberPicker dayNumberPicker;

	private int minYear;
	private int maxYear;
	private int yearRange;
	
	private boolean displayDescription;
	private TextView descriptionTextView;
	*/
	private TextView mTitleView,mTitleUp,mTitleDown;
    private ImageButton mPrev;
    private ImageButton mNext;
    private LinearLayout mWeeksView;

    private final LayoutInflater mInflater;
    private final RecycleBin mRecycleBin = new RecycleBin();

    //private ResizeManager mResizeManager;

    private TextView mSelectionText;
    private LinearLayout mHeader;
	private PersianCalendar pCalendar;
	private DayView lastDaySelected=null;
	int selectedYear,selectedMonth,selectedDay;
	int currentYear,currentMonth,currentDay;
	public ResourceUtils eventCalendar;
	@Override
	
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTitleView = (TextView) findViewById(R.id.title);
		mTitleUp = (TextView) findViewById(R.id.titleUp);
		mTitleDown= (TextView) findViewById(R.id.titleDown);
        mPrev = (ImageButton) findViewById(R.id.prev);
        mNext = (ImageButton) findViewById(R.id.next);
        mWeeksView = (LinearLayout) findViewById(R.id.weeks);

        mHeader = (LinearLayout) findViewById(R.id.header);
        mSelectionText = (TextView) findViewById(R.id.selection_title);

        mPrev.setOnClickListener(this);
        mNext.setOnClickListener(this);
		mPrev.setOnLongClickListener(this);
        mNext.setOnLongClickListener(this);
		populateDays();
		
		populateMonthLayout();
      
    }
	@Override
    public void onClick(View v) {
     
        if (pCalendar != null) {
            int id = v.getId();
            if (id == R.id.prev) {
                pCalendar.prev();
                populateMonthLayout();
                
            } else if (id == R.id.next) {
             
                pCalendar.next();
                  
				populateMonthLayout();
                
            }

        }
		
		
    }
	
	@Override
    public boolean onLongClick(View v) {

        if (pCalendar != null) {
            int id = v.getId();
            if (id == R.id.prev) {
                pCalendar.prevYear();
                populateMonthLayout();

            } else if (id == R.id.next) {

                pCalendar.nextYear();

				populateMonthLayout();

            }

        }
return true;

    }
	private final static int rtl7(int i)
	{
		return 6-i;
	}
	private void populateDays() {

		LinearLayout layout = (LinearLayout) findViewById(R.id.days);

	for (int i = 0; i < 7; i++) {
			TextView textView = (TextView) layout.getChildAt(rtl7(i));
			textView.setText(PersianCalendarConstants.persianWeekDays[i]);

	
		}
		

    }
	private void cacheView(int index) {
        View view = mWeeksView.getChildAt(index);
        if(view != null) {
            mWeeksView.removeViewAt(index);
            mRecycleBin.addView(view);
        }
    }
	public PersianCalendar getPersianCalendar() {
        
        return pCalendar;
    }
	private View getView() {
        View view = mRecycleBin.recycleView();
        if (view == null) {
            view = mInflater.inflate(R.layout.week_layout, this, false);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        return view;
    }
	private WeekView getWeekView(int index) {
	
        int cnt = mWeeksView.getChildCount();

        if(cnt < index + 1) {
            for (int i = cnt; i < index + 1; i++) {
                View view = getView();
                mWeeksView.addView(view);
            }
        }

        return (WeekView) mWeeksView.getChildAt(index);
    }
	private final DayView getDayView(int week,int day)
	{
		WeekView weekView = getWeekView(week);
		return (DayView) weekView.getChildAt(rtl7(day));
		
	}
	private void resetDayView(int week,int day)
	{
		DayView d=getDayView(week,day);
		d.clearText();
		d.setEnabled(false);
		d.setOnClickListener(null);
	}
	public boolean getHVacation(int day)
	{
		int dayMonth=getHDayMonth(day);
		if (eventCalendar.vacationH.containsKey(dayMonth)) 
			return eventCalendar.vacationH.get(dayMonth);
		return false;

	}
	public boolean getPVacation(int day)
	{
		int dayMonth=getPDayMonth(day);
		if (eventCalendar.vacationP.containsKey(dayMonth)) 
			return eventCalendar.vacationP.get(dayMonth);
		return false;

	}
	public boolean getGVacation(int day)
	{
		int dayMonth=getGDayMonth(day);
		if (eventCalendar.vacationG.containsKey(dayMonth)) 
			return eventCalendar.vacationG.get(dayMonth);
		return false;

	}
	public boolean isVacation(int day)
	{
		
		return (getPVacation(day) || getHVacation(day) || getGVacation(day)) ; 

	}
	public String getHEvent(int day)
	{
		int dayMonth=getHDayMonth(day);
		if (eventCalendar.eventH.containsKey(dayMonth)) 
			return eventCalendar.eventH.get(dayMonth);
		return "";

	}
	public String getPEvent(int day)
	{
		int dayMonth=getPDayMonth(day);
		if (eventCalendar.eventP.containsKey(dayMonth)) 
			return eventCalendar.eventP.get(dayMonth);
		return "";

	}
	public String getGEvent(int day)
	{
		int dayMonth=getGDayMonth(day);
		if (eventCalendar.eventG.containsKey(dayMonth)) 
			return eventCalendar.eventG.get(dayMonth);
		return "";

	}
	public boolean hasEvent(int day)
	{

		return !(getPEvent(day)+getHEvent(day)+getGEvent(day)).equals("") ; 

	}
	public int getHDayMonth(int i)
	{
		
		return (pCalendar.persianHMonths[i]+1)*100+pCalendar.persianHDays[i];
			
	}
	public int getPDayMonth(int i)
	{
		return pCalendar.getPersianMonth()*100+i;
		
	}
	public int getGDayMonth(int i)
	{
		return (pCalendar.persianGMonths[i]+1)*100+pCalendar.persianGDays[i];
		
	}
	private void populateMonthLayout() {

	  int wDay=pCalendar.persianMonthFirstDayWeekDay;
	  int wCnt=0;
	  int cnt=pCalendar.persianWeekCount;
	  String gMonth1=pCalendar.getMonthForInt(pCalendar.gMonth1);
	  String gMonth2=pCalendar.getMonthForInt(pCalendar.gMonth2);
	  String gYear=pCalendar.gYear1==pCalendar.gYear2 ? Integer.toString(pCalendar.gYear1) : pCalendar.gYear1+"-"+pCalendar.gYear2;
	  
	  String hMonth1=PersianCalendarConstants.iMonthNames[pCalendar.hMonth1];
	  String hMonth2=PersianCalendarConstants.iMonthNames[pCalendar.hMonth2];
		String hYear=pCalendar.hYear1==pCalendar.hYear2 ? PersianCalendarConstants.toArabicNumbers(pCalendar.hYear1) : PersianCalendarConstants.toArabicNumbers(pCalendar.hYear1)+"-"+PersianCalendarConstants.toArabicNumbers(pCalendar.hYear2);
	  
		mTitleView.setText(pCalendar.getPersianMonthName()+ "-" + PersianCalendarConstants.toArabicNumbers(pCalendar.getPersianYear()));
		mTitleUp.setText(gMonth1+ "/"+gMonth2+"-" + gYear);
		mTitleDown.setText(hMonth1+ "/"+hMonth2+"-" + hYear);
		
		for (int i=0;i<wDay;i++)
	
			resetDayView(wCnt,i);
		
		
		for (int i=1;;i++)
	{
		final int day=i;
		String hDay=PersianCalendarConstants.toArabicNumbers( pCalendar.persianHDays[i]);
		String gDay=""+pCalendar.persianGDays[i];
		
		
		DayView dayView =getDayView(wCnt,wDay);
		
		
		
		dayView.setTextUp(gDay,!getGEvent(day).equals(""),getGVacation(day));
		dayView.setText(PersianCalendarConstants.toArabicNumbers(day),!getPEvent(day).equals(""),getPVacation(day) || wDay==6);
		dayView.setTextDown(hDay,!getHEvent(day).equals(""),getHVacation(day));
		
		if (currentYear == pCalendar.getPersianYear() &&
			pCalendar.getPersianMonth()==  currentMonth &&
			i== currentDay)
		{
		 dayView.setCurrent(true);
		 
}
else {
	dayView.setCurrent(false);
	dayView.setBackgroundColor(Color.BLACK);
}
	
		dayView.setEnabled(true);

		
			dayView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						if (mListener != null &&  ((DayView)v).isEnabled()) {
							if (lastDaySelected!=null) 
					
							lastDaySelected.setSelected(false);
									
							
						((DayView)v).setSelected(true);
						
							lastDaySelected=(DayView)v;
							
							int year=pCalendar.getPersianYear();
							int month=pCalendar.getPersianMonth();
							pCalendar.setPersianDate(year,month,day);
							mListener.onDateChanged(pCalendar,year, month,day);
						}
					
					}
				});
		
			
		
		
		if (i==pCalendar.persianMonthDays) break;
		if (wDay<6) wDay++; 
		else {
			wDay=0;
		    wCnt++;
		}
		
	}
	
		for (int i=wDay+1;i<7;i++)
			resetDayView(wCnt,i);
			
        int childCnt = mWeeksView.getChildCount();
        if (cnt < childCnt) {
            for (int i = cnt; i < childCnt; i++) {
                cacheView(i);
            }
        }

    }

    

    
	public PersianDatePicker(Context context) {
		this(context, null, -1);
	}

	public PersianDatePicker(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public PersianDatePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	//	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    eventCalendar=new ResourceUtils(context);
		mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.calendar_layout, this);
		pCalendar = new PersianCalendar();
		currentDay=pCalendar.getPersianDay();
		currentMonth=pCalendar.getPersianMonth();
		currentYear=pCalendar.getPersianYear();
		/*
		yearNumberPicker = (NumberPicker) view.findViewById(R.id.yearNumberPicker);
		monthNumberPicker = (NumberPicker) view.findViewById(R.id.monthNumberPicker);
		dayNumberPicker = (NumberPicker) view.findViewById(R.id.dayNumberPicker);
		descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);

		//PersianCalendar
		
//pCalendar.goToMonthLastDay();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PersianDatePicker, 0, 0);

		boolean disableSoftKeyboard = a.getBoolean(R.styleable.PersianDatePicker_disableSoftKeyboard, false);
		if(disableSoftKeyboard)
		{
			yearNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
			monthNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
			dayNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);  
		}


				
				
		yearRange = a.getInteger(R.styleable.PersianDatePicker_yearRange, 10);

		minYear = a.getInt(R.styleable.PersianDatePicker_minYear, pCalendar.getPersianYear() - yearRange);
		maxYear = a.getInt(R.styleable.PersianDatePicker_maxYear, pCalendar.getPersianYear() + yearRange);
		yearNumberPicker.setMinValue(minYear);
		yearNumberPicker.setMaxValue(maxYear);

		
		selectedYear = a.getInt(R.styleable.PersianDatePicker_selectedYear, pCalendar.getPersianYear());
		if (selectedYear > maxYear || selectedYear < minYear) {
			throw new IllegalArgumentException(String.format("Selected year (%d) must be between minYear(%d) and maxYear(%d)", selectedYear, minYear, maxYear));
		}
		yearNumberPicker.setValue(selectedYear);
		yearNumberPicker.setOnValueChangedListener(dateChangeListener);

		
		boolean displayMonthNames = a.getBoolean(R.styleable.PersianDatePicker_displayMonthNames, false);
		monthNumberPicker.setMinValue(1);
		monthNumberPicker.setMaxValue(12);
		if (displayMonthNames) {
			monthNumberPicker.setDisplayedValues(PersianCalendarConstants.persianMonthNames);
		}
		selectedMonth = a.getInteger(R.styleable.PersianDatePicker_selectedMonth, pCalendar.getPersianMonth());
		if (selectedMonth < 1 || selectedMonth > 12) {
			throw new IllegalArgumentException(String.format("Selected month (%d) must be between 1 and 12", selectedMonth));
		}
		monthNumberPicker.setValue(selectedMonth);
		monthNumberPicker.setOnValueChangedListener(dateChangeListener);

	
		dayNumberPicker.setMinValue(1);
		dayNumberPicker.setMaxValue(31);
		 selectedDay = a.getInteger(R.styleable.PersianDatePicker_selectedDay, pCalendar.getPersianDay());
		if (selectedDay > 31 || selectedDay < 1) {
			throw new IllegalArgumentException(String.format("Selected day (%d) must be between 1 and 31", selectedDay));
		}
		if (selectedMonth > 6 && selectedMonth < 12 && selectedDay == 31) {
			selectedDay = 30;
		} else {
			boolean isLeapYear = PersianCalendarUtils.isPersianLeapYear(selectedYear);
			if (isLeapYear && selectedDay == 31) {
				selectedDay = 30;
			} else if (selectedDay > 29) {
				selectedDay = 29;
			}
		}
		dayNumberPicker.setValue(selectedDay);
		dayNumberPicker.setOnValueChangedListener(dateChangeListener);
		
	
		displayDescription = a.getBoolean(R.styleable.PersianDatePicker_displayDescription, false);
		if( displayDescription ) {
			descriptionTextView.setVisibility(View.VISIBLE);
		}
		
		a.recycle();*/
	}
/*
	NumberPicker.OnValueChangeListener dateChangeListener = new NumberPicker.OnValueChangeListener() {

		@Override
		public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
			int year = yearNumberPicker.getValue();
			boolean isLeapYear = PersianCalendarUtils.isPersianLeapYear(year);

			int month = monthNumberPicker.getValue();
			int day = dayNumberPicker.getValue();

			if (month < 7) {
				dayNumberPicker.setMinValue(1);
				dayNumberPicker.setMaxValue(31);
			} else if (month > 6 && month < 12) {
				if (day == 31) {
					dayNumberPicker.setValue(30);
				}
				dayNumberPicker.setMinValue(1);
				dayNumberPicker.setMaxValue(30);
			} else if (month == 12) {
				if (isLeapYear) {
					if (day == 31) {
						dayNumberPicker.setValue(30);
					}
					dayNumberPicker.setMinValue(1);
					dayNumberPicker.setMaxValue(30);
				} else {
					if (day > 29) {
						dayNumberPicker.setValue(29);
					}
					dayNumberPicker.setMinValue(1);
					dayNumberPicker.setMaxValue(29);
				}
			}
			
			// Set description
			if( displayDescription ) {
				descriptionTextView.setText(getDisplayPersianDate().getPersianLongDate());
			}
			
            if (mListener != null) {
                mListener.onDateChanged(yearNumberPicker.getValue(), monthNumberPicker.getValue(),
                        dayNumberPicker.getValue());
            }

		}

	};*/

    public void setOnDateChangedListener(OnDateChangedListener onDateChangedListener) {
        mListener = onDateChangedListener;
    }

    /**
     * The callback used to indicate the user changed the date.
	 * A class that wants to be notified when the date of PersianDatePicker
	 * changes should implement this interface and register itself as the
	 * listener of date change events using the PersianDataPicker's
	 * setOnDateChangedListener method.
     */
    public interface OnDateChangedListener {

        /**
         * Called upon a date change.
         *
         * @param newYear  The year that was set.
         * @param newMonth The month that was set (1-12)
         * @param newDay   The day of the month that was set.
         */
        void onDateChanged(PersianCalendar persianCalendar, int newYear, int newMonth, int newDay);
    }
/*
	public Date getDisplayDate() {
		PersianCalendar displayPersianDate = new PersianCalendar();
		displayPersianDate.setPersianDate(yearNumberPicker.getValue(), monthNumberPicker.getValue(), dayNumberPicker.getValue());
		return displayPersianDate.getTime();
	}

	public void setDisplayDate(Date displayDate) {
		setDisplayPersianDate(new PersianCalendar(displayDate.getTime()));
	}

	public PersianCalendar getDisplayPersianDate() {
		PersianCalendar displayPersianDate = new PersianCalendar();
		displayPersianDate.setPersianDate(yearNumberPicker.getValue(), monthNumberPicker.getValue(), dayNumberPicker.getValue());
		return displayPersianDate;
	}

	public void setDisplayPersianDate(PersianCalendar displayPersianDate) {
		int year = displayPersianDate.getPersianYear();
		int month = displayPersianDate.getPersianMonth();
		int day = displayPersianDate.getPersianDay();
		if (month > 6 && month < 12 && day == 31) {
			day = 30;
		} else {
			boolean isLeapYear = PersianCalendarUtils.isPersianLeapYear(year);
			if (isLeapYear && day == 31) {
				day = 30;
			} else if (day > 29) {
				day = 29;
			}
		}
		dayNumberPicker.setValue(day);

		minYear = year - yearRange;
		maxYear = year + yearRange;
		yearNumberPicker.setMinValue(minYear);
		yearNumberPicker.setMaxValue(maxYear);

		yearNumberPicker.setValue(year);
		monthNumberPicker.setValue(month);
		dayNumberPicker.setValue(day);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		// begin boilerplate code that allows parent classes to save state
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		// end

		ss.datetime = this.getDisplayDate().getTime();
		return ss;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// begin boilerplate code so parent classes can restore state
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		// end

		setDisplayDate(new Date(ss.datetime));
	}

	static class SavedState extends BaseSavedState {
		long datetime;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			this.datetime = in.readLong();
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeLong(this.datetime);
		}

		// required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
	*/
	private class RecycleBin {

        private final Queue<View> mViews = new LinkedList<>();

        
        public View recycleView() {
            return mViews.poll();
        }

        public void addView(View view) {
            mViews.add(view);
        }

    }
}
