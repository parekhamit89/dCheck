package com.sanotech.dchek;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.sanotech.dchek.adapters.DatabaseAdapter;
import com.sanotech.dchek.fragments.SelectDateFragment;
import com.sanotech.dchek.fragments.SelectTimeFragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class BGLevel extends Activity implements OnClickListener,
		DateTimeListener, OnItemSelectedListener {

	EditText edtNote, edtBGLevel;
	Spinner spSelecteTime;
	private String selectedTime;
	TextView tvDateTime;
	private String currentDate, currentTime;
	DatabaseAdapter dbAdapter;
	private FooterView footer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bg_level);
		footer = (FooterView) findViewById(R.id.footer);
		footer.initFooter(this);

		//for action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Dashboard");
		
		((ImageView) findViewById(R.id.img_bg_cal)).setOnClickListener(this);
		((Spinner) findViewById(R.id.sp_bg_during))
				.setOnItemSelectedListener(this);
		((Button) findViewById(R.id.btn_bg_level_save))
				.setOnClickListener(this);
		edtNote = (EditText) findViewById(R.id.edt_bg_txt_note);
		edtBGLevel = (EditText) findViewById(R.id.edt_bg_level);
		tvDateTime = (TextView) findViewById(R.id.tv_bg_cur_date);
		tvDateTime.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm aa").format(Calendar.getInstance().getTime()));
		dbAdapter = new DatabaseAdapter(BGLevel.this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_bg_cal:
			SelectDateFragment dateFragment = new SelectDateFragment(
					BGLevel.this, this);
			dateFragment.show(BGLevel.this.getFragmentManager(), "DatePicker");
			break;
		case R.id.btn_bg_level_save:
			String bgLevel = edtBGLevel.getText().toString();
			String note = edtNote.getText().toString();

			if (vaidation()) {
				try {
					dbAdapter.openDataBase();

					ContentValues values = new ContentValues();
					values.put(AppConstant.BG_LEVEL_LEVEL, bgLevel);
					values.put(AppConstant.BG_LEVEL_NOTE, note);
					values.put(AppConstant.BG_LEVEL_RECORDED_TIME, DChackUtils.getTimeStemp(tvDateTime.getText().toString()));
					values.put(AppConstant.BG_LEVEL_TIME_CHECK, selectedTime);
					values.put(AppConstant.BG_LEVEL_USER_ID, String.valueOf(0));
					AppConstant.mDataBase.insert(AppConstant.TAB_USER_BG_LEVEL,
							null, values);

					dbAdapter.close();
					finish();
					startActivity(new Intent(BGLevel.this, DashBoard.class));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			break;
		default:
			break;
		}
	}

	/*
	 * public String getdate() { Calendar calender = Calendar.getInstance();
	 * SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy"); String
	 * formattedDate = df.format(calender.getTime()); return formattedDate; }
	 */

	@Override
	public void setDate(String date) {
		this.currentDate = date;

		SelectTimeFragment timeFragment = new SelectTimeFragment(BGLevel.this,
				this);
		timeFragment.show(BGLevel.this.getFragmentManager(), "DatePicker");

	}

	@Override
	public void setTime(String time) {
		// TODO Auto-generated method stub
		this.currentTime = time;
		tvDateTime.setText(DChackUtils.getdate( this.currentDate + "  " + this.currentTime));
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

		selectedTime = parent.getItemAtPosition(position).toString();

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean vaidation(){
		
		if ( tvDateTime.getText().toString() == null || tvDateTime.getText().toString().trim().isEmpty()) {
			tvDateTime.setError("Please Enter Date");
			return false;
			
		}else if (edtBGLevel.getText().toString()== null || edtBGLevel.getText().toString().trim().isEmpty()) {
			
			edtBGLevel.setError("Please Enter Glucose Level");
			return false;
		}else {
			return true;
		}
		
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		
		switch (item.getItemId()) {
        case android.R.id.home:
            // go to previous screen when app icon in action bar is clicked
            Intent intent = new Intent(this, DashBoard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
    }
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
	// this willl disable back button
	}
}
