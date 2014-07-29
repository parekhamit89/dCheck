package com.sanotech.dchek;

import java.sql.SQLException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sanotech.dchek.adapters.DatabaseAdapter;
import com.sanotech.dchek.fragments.AppointmentDateFragment;
import com.sanotech.dchek.fragments.SelectTimeFragment;
import com.sanotech.dchek.models.DoctorsDetailEntity;

public class DoctorInfo extends Activity implements OnClickListener,
		DateTimeListener {
	private String date;
	private String time;
	private EditText edtDrFname, edtDrLName, edtHospName, edtPhoneNo, edtEmail;
	TextView tvDateTime;
	private DatabaseAdapter dbAdapter;
	DoctorsDetailEntity doctorEntity;
	private FooterView footer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_info);
		footer = (FooterView) findViewById(R.id.footer);
	    footer.initFooter(this);
	    
	    ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Dashboard");
		
		((ImageView) findViewById(R.id.img_dr_cal)).setOnClickListener(this);
		((Button) findViewById(R.id.btn_dr_save)).setOnClickListener(this);
		((TextView) findViewById(R.id.tv_next_visit)).setOnClickListener(this);
		edtDrFname = (EditText) findViewById(R.id.edt_dr_first_name);
		edtDrLName = (EditText) findViewById(R.id.edt_dr_last_name);
		edtHospName = (EditText) findViewById(R.id.edt_hospital_name);
		edtPhoneNo = (EditText) findViewById(R.id.edt_dr_phone_no);
		edtEmail = (EditText) findViewById(R.id.edt_dr_email_add);
		tvDateTime = (TextView) findViewById(R.id.tv_next_visit);
		dbAdapter = new DatabaseAdapter(DoctorInfo.this);
		doctorEntity = new DoctorsDetailEntity();
		try {
			dbAdapter.openDataBase();

			Cursor cursor = AppConstant.mDataBase.rawQuery("SELECT * FROM "
					+ AppConstant.TAB_DOCTOR_INFO, null);

			if (cursor.getCount() > 0) {
				cursor.moveToLast();
				doctorEntity.setDrFname(cursor.getString(cursor
						.getColumnIndex(AppConstant.DOCTOR_INFO_FIRST_NAME)));
				doctorEntity.setDrLname(cursor.getString(cursor
						.getColumnIndex(AppConstant.DOCTOR_INFO_LAST_NAME)));
				doctorEntity
						.setDrHospital(cursor.getString(cursor
								.getColumnIndex(AppConstant.DOCTOR_INFO_HOSPITAL_NAME)));
				doctorEntity.setDrEmail(cursor.getString(cursor
						.getColumnIndex(AppConstant.DOCTOR_INFO_EMAIL_ID)));
				doctorEntity.setDrPhoNo(cursor.getString(cursor
						.getColumnIndex(AppConstant.DOCTOR_INFO_PH_NO)));
				doctorEntity.setDrId(cursor.getInt(cursor
						.getColumnIndex(AppConstant.DOCTOR_INFO_ID)));
				cursor.close();

			}
			if (doctorEntity.getDrId() > -1) {

				Cursor cursorVisit = AppConstant.mDataBase.rawQuery(
						"SELECT * FROM " + AppConstant.TAB_DOCTOR_VISIT
								+ " WHERE "
								+ AppConstant.DOCTOR_VISIT_DOCTOR_ID + " = "
								+ doctorEntity.getDrId(), null);

				if (cursorVisit.getCount() > 0) {
					cursorVisit.moveToLast();
					doctorEntity
							.setDrLastVisit(cursorVisit.getString(cursorVisit
									.getColumnIndex(AppConstant.DOCTOR_VISIT_LAST_VISIT)));
					doctorEntity
							.setDrNextVisit(cursorVisit.getString(cursorVisit
									.getColumnIndex(AppConstant.DOCTOR_VISIT_NEXT_VISIT)));
					cursorVisit.close();

					edtDrFname.setText(doctorEntity.getDrFname());
					edtDrLName.setText(doctorEntity.getDrLname());
					edtHospName.setText(doctorEntity.getDrHospital());
					edtEmail.setText(doctorEntity.getDrEmail());
					edtPhoneNo.setText(doctorEntity.getDrPhoNo());
					tvDateTime.setText(doctorEntity.getDrNextVisit().equalsIgnoreCase("no visit")?"":DChackUtils.getTimeinString(Long.parseLong(doctorEntity.getDrNextVisit())));
				}
			}
			dbAdapter.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_dr_cal:
			AppointmentDateFragment dateFragment = new AppointmentDateFragment(
					DoctorInfo.this, this);
			dateFragment.show(DoctorInfo.this.getFragmentManager(),
					"DatePicker");
			break;
		case R.id.btn_dr_save:
			String fname = edtDrFname.getText().toString();
			String lname = edtDrLName.getText().toString();
			String nextVisit = tvDateTime.getText().toString();
			try {
				dbAdapter.openDataBase();

				if (validation()) {
					if (doctorEntity.getDrFname() != null) {

						if (fname.equalsIgnoreCase(doctorEntity.getDrFname())
								&& lname.equalsIgnoreCase(doctorEntity
										.getDrLname())) {
							// only insertion in to doctore visit
							if (isUpDatedFields()) {
//nextVisit.equalsIgnoreCase(DChackUtils.getTimeinString(Long.parseLong(doctorEntity.getDrNextVisit())))
								ContentValues values = new ContentValues();
								values.put(AppConstant.DOCTOR_VISIT_DOCTOR_ID,
										doctorEntity.getDrId());
								values.put(AppConstant.DOCTOR_VISIT_LAST_VISIT,
										doctorEntity.getDrNextVisit());
								values.put(AppConstant.DOCTOR_VISIT_NEXT_VISIT,
										DChackUtils.getTimeStemp(tvDateTime.getText().toString()));
								AppConstant.mDataBase.insert(
										AppConstant.TAB_DOCTOR_VISIT, null,
										values);

							} else {
								Toast.makeText(DoctorInfo.this,
										"Nothing is updated yet",
										Toast.LENGTH_SHORT).show();

							}
							dbAdapter.close();
							startActivity(new Intent(DoctorInfo.this, DashBoard.class));
							finish();
							
						} else {
							insertDoctorInfo();
							dbAdapter.close();
							startActivity(new Intent(DoctorInfo.this, DashBoard.class));
							
							finish();
						}
					} else {
						// insert
						insertDoctorInfo();
						dbAdapter.close();
						startActivity(new Intent(DoctorInfo.this, DashBoard.class));
						
						finish();
					}

				} /*else {
					Toast.makeText(
							DoctorInfo.this,
							"Please enter all require values in respctive fields",
							Toast.LENGTH_SHORT).show();
				}*/

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void setDate(String date) {
		this.date = date;

		SelectTimeFragment timeFragment = new SelectTimeFragment(
				DoctorInfo.this, this);
		timeFragment.show(DoctorInfo.this.getFragmentManager(), "DatePicker");

	}

	@Override
	public void setTime(String time) {

		this.time = time;
		tvDateTime.setText(DChackUtils.getdate( this.date + "  " + this.time));
	}

	private void insertDoctorInfo() {
		ContentValues infoValues = new ContentValues();

		infoValues.put(AppConstant.DOCTOR_INFO_FIRST_NAME, edtDrFname.getText().toString());
		infoValues.put(AppConstant.DOCTOR_INFO_LAST_NAME, edtDrLName.getText()
				.toString());
		infoValues.put(AppConstant.DOCTOR_INFO_HOSPITAL_NAME, edtHospName
				.getText().toString());
		infoValues.put(AppConstant.DOCTOR_INFO_EMAIL_ID, edtEmail.getText()
				.toString());
		infoValues.put(AppConstant.DOCTOR_INFO_PH_NO, edtPhoneNo.getText()
				.toString());
		long rowid = AppConstant.mDataBase.insert(AppConstant.TAB_DOCTOR_INFO,
				null, infoValues);

		Cursor cursor = AppConstant.mDataBase
				.rawQuery("SELECT * FROM " + AppConstant.TAB_DOCTOR_INFO
						+ " WHERE rowid = " + rowid, null);
		int doctorIdNew = 0;
		if (cursor.getCount() > 0) {
			cursor.moveToLast();
			doctorIdNew = cursor.getInt(cursor
					.getColumnIndex(AppConstant.DOCTOR_INFO_ID));
		}
		
		String nextVisit = tvDateTime.getText().toString();
		if (nextVisit == null || nextVisit.isEmpty()) {
			nextVisit = "No Visit";
		}
		
		ContentValues values = new ContentValues();
		values.put(AppConstant.DOCTOR_VISIT_DOCTOR_ID, doctorIdNew);
		values.put(AppConstant.DOCTOR_VISIT_LAST_VISIT,
				(doctorEntity.getDrNextVisit() == null ? "no visit"
						: String.valueOf(DChackUtils.getTimeStemp(doctorEntity.getDrNextVisit()))));
		values.put(AppConstant.DOCTOR_VISIT_NEXT_VISIT, (nextVisit=="No Visit"?nextVisit : String.valueOf(DChackUtils.getTimeStemp( nextVisit))));

		AppConstant.mDataBase
				.insert(AppConstant.TAB_DOCTOR_VISIT, null, values);

		finish();
	}
	
	private boolean validation(){
		
		String lname = edtDrLName.getText().toString();
		String nextVisit = tvDateTime.getText().toString();
		if (edtDrFname.getText().toString() == null || edtDrFname.getText().toString().trim().isEmpty()) {
			edtDrFname.setError("Please Enter Doctor First Name");
			return false;
		}else if (lname == null || lname.trim().isEmpty()) {
			edtDrLName.setError("Please Enter Doctor Last Name");
			return false;
		}else if (edtPhoneNo.getText().toString() ==null || edtPhoneNo.getText().toString().trim().isEmpty()) {
			edtPhoneNo.setError("Please Enter Doctor Phone no");
			return false;
		}else if (edtEmail.getText().toString() ==null || edtEmail.getText().toString().trim().isEmpty()) {
			edtEmail.setError("Please Enter Doctor Email Address");
			return false;
		}else {
			if (edtPhoneNo.getText().toString().length() < 6) {
				edtPhoneNo.setError("Please Enter Doctor Phone no");
				return false;
			}else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
				edtEmail.setError(getResources().getString(R.string.enter_email));
				
				return false;
			}else {
				return true;
			}
		}
		
	}
	// for dictor date validation
	/*else if (nextVisit== null || nextVisit.trim().isEmpty() ) {
	tvDateTime.setError("Please Enter Date & Time of next visit");
	return false; 
}*/
	
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
		//do nothing
	}
	private boolean isUpDatedFields(){
		boolean isUpdate= false;
		if (doctorEntity.getDrNextVisit() ==  null) {
			isUpdate = true;
		}else {
			
			if (doctorEntity.getDrNextVisit() !=null && doctorEntity.getDrNextVisit().equalsIgnoreCase("no visit")) {
				isUpdate = true;
			}else if (doctorEntity.getDrNextVisit() !=null && !doctorEntity.getDrNextVisit().equalsIgnoreCase("no visit")) {
				if (tvDateTime.getText().toString().equalsIgnoreCase(DChackUtils.getTimeinString(Long.parseLong(doctorEntity.getDrNextVisit())))) {
					
					isUpdate= false;
				}else {
					isUpdate= true;
				}
			} 
		}
		return isUpdate;
	}
}
