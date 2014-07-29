package com.sanotech.dchek.fragments;

import java.text.DecimalFormat;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import com.sanotech.dchek.DateTimeListener;

@SuppressLint("ValidFragment")
public class AppointmentDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	Context context;
	DateTimeListener dateTimeListener;
		public AppointmentDateFragment(Context c, DateTimeListener listener) {
		this.context =c;
		this.dateTimeListener = listener;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	final Calendar calendar = Calendar.getInstance();
	int yy = calendar.get(Calendar.YEAR);
	int mm = calendar.get(Calendar.MONTH);
	int dd = calendar.get(Calendar.DAY_OF_MONTH);
	int hr =calendar.get(Calendar.HOUR);
	int mn =calendar.get(Calendar.MINUTE);
	// new TimePickerDialog(MainActivity.this, this, hr	, mn, false)
	
	//new DatePickerDialog(MainActivity.this, this, yy, mm, dd)
	//return new DatePickerDialog(this.context, this, yy, mm, dd);
	DatePickerDialog dpd = new DatePickerDialog(this.context, this, yy, mm, dd);
	dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());
	dpd.show();

	return dpd;
	
	}
	 
	public void onDateSet(DatePicker view, int yy, int mm, int dd) {
	//Toast.makeText(this.context, dd+"/"+mm+"/"+yy, Toast.LENGTH_SHORT).show();
		mm= mm+1;
		DecimalFormat formatter = new DecimalFormat("00");
		String aFormatted = formatter.format(mm);
	this.dateTimeListener.setDate(dd+"-"+aFormatted+"-"+yy);
	}

	}
