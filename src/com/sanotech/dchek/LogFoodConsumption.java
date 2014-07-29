package com.sanotech.dchek;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sanotech.dchek.adapters.DatabaseAdapter;
import com.sanotech.dchek.models.CustomeFoodListEntity;
import com.sanotech.dchek.models.FoodListEntity;

public class LogFoodConsumption extends Activity implements OnClickListener,
		OnItemSelectedListener {

	private Spinner spQuntiyUnit, spWhen;
	private EditText edtQuantity;
	private String selectedTime;
	DatabaseAdapter dbAdapter;
	private FoodListEntity foodSelected;
	private Calendar calender = Calendar.getInstance();
	private long intakeDateTimeToSet;
	private CustomeFoodListEntity customeFoodList;
	private TextView foodName, calorie;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_food_cunsumption);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Dashboard");
		
		edtQuantity = (EditText) findViewById(R.id.edt_quantity);
		foodName = (TextView)findViewById(R.id.tv_food_name);
		calorie= (TextView)findViewById(R.id.tv_cal);
		String saveQuantity;
		ArrayList<String> list = new ArrayList<String>();
		dbAdapter = new DatabaseAdapter(LogFoodConsumption.this);
		spQuntiyUnit = (Spinner) findViewById(R.id.sp_quentity);
		spWhen = (Spinner) findViewById(R.id.sp_when);

		intakeDateTimeToSet = getIntent().getExtras().getLong(
				AppConstant.INTAKE_DATE_TIME_TO_SET);
		
		if (getIntent().getExtras().getSerializable(AppConstant.SELECTED_FOOD) != null) {
			foodSelected = (FoodListEntity) getIntent().getSerializableExtra(
					AppConstant.SELECTED_FOOD);
			
			foodName.setText(foodSelected.getFoodName());
			calorie.setText(String.valueOf(foodSelected.getFoodCal()) + " Cal");
			saveQuantity = foodSelected.getSavingQuantity();
			list.add(saveQuantity);
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, list);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spQuntiyUnit.setAdapter(dataAdapter);
			spQuntiyUnit.setAdapter(dataAdapter);
			

		}else if (getIntent().getExtras().getSerializable(AppConstant.INTAKE_FOODS) != null) {
			customeFoodList = (CustomeFoodListEntity) getIntent().getExtras()
					.getSerializable(AppConstant.INTAKE_FOODS);
			
			edtQuantity.setText(customeFoodList.getSavingQuantity());
			foodName.setText(customeFoodList.getFoodName());
			calorie.setText(String.valueOf(customeFoodList.getFoodCal()) + " Cal");
			String unit = "number";
			try {
				dbAdapter.openDataBase();
			 
			Cursor unitCursor = AppConstant.mDataBase.rawQuery("SELECT "+AppConstant.FOOD_LIST_SERVING_QUANTITY+" FROM "+AppConstant.TAB_FOOD_LIST+" WHERE "+AppConstant.FOOD_LIST_FOOD_ID +" = "+customeFoodList.getFoodId(), null);
			if (unitCursor.getCount() > 0) {
				unitCursor.moveToLast();
				unit = unitCursor.getString(unitCursor.getColumnIndex(AppConstant.FOOD_LIST_SERVING_QUANTITY));
				
			}
			unitCursor.close();
			dbAdapter.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			list.add(unit);
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, list);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spQuntiyUnit.setAdapter(dataAdapter);
			String[] servingTime = getResources().getStringArray(R.array.cl_intake_time);
			//customeFoodList.getSavingDateTme()
			int i = Arrays.asList(servingTime).indexOf(customeFoodList.getSavingDateTme());
			
			spWhen.setSelection(i);;

		}
		
		((Button) findViewById(R.id.btn_log_save)).setOnClickListener(this);
		spWhen.setOnItemSelectedListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_log_save) {
			if (customeFoodList != null) {
				try {
					
					String quantity = edtQuantity.getText().toString();
					
					dbAdapter.openDataBase();
					ContentValues values = new ContentValues();
					values.put(AppConstant.CUSTOME_FOOD_LIST_FOOD_NAME,
							customeFoodList.getFoodName());
					values.put(AppConstant.CUSTOME_FOOD_LIST_FOOD_ID,
							customeFoodList.getFoodId());
					values.put(AppConstant.CUSTOME_FOOD_LIST_FOOD_CAL, customeFoodList.getFoodCal());
					values.put(AppConstant.CUSTOME_FOOD_LIST_SERVING_QUANTITY,
							quantity);
					values.put(AppConstant.CUSTOME_FOOD_LIST_SERVING_DATETIME,
							selectedTime);
					
					AppConstant.mDataBase.update(AppConstant.TAB_CUSTOME_FOOD_LIST, values, AppConstant.CUSTOME_FOOD_LIST_USER_FOOD_ID + " = " +customeFoodList.getUserFoodId(), null);
					dbAdapter.close();
					
				} catch (Exception e) {
					
					Toast.makeText(LogFoodConsumption.this, "updation of data is not processed", Toast.LENGTH_SHORT).show();
					Log.e("DB Updation", "Updation error while updating the custome food list ");
					e.printStackTrace();
				}
				this.finish();
				
			}else {
				
			
			String quantity = edtQuantity.getText().toString();
			// int quantity = 0;
			if (quantity.isEmpty() || quantity == null) {
				return;
			}
			
			int calorie = foodSelected.getFoodCal();

			if (selectedTime != null) {
				try {
					dbAdapter.openDataBase();
					ContentValues values = new ContentValues();
					values.put(AppConstant.CUSTOME_FOOD_LIST_FOOD_NAME,
							foodSelected.getFoodName());
					values.put(AppConstant.CUSTOME_FOOD_LIST_FOOD_ID,
							foodSelected.getFoodId());
					values.put(AppConstant.CUSTOME_FOOD_LIST_FOOD_CAL, calorie);
					values.put(AppConstant.CUSTOME_FOOD_LIST_SERVING_QUANTITY,
							quantity);
					values.put(AppConstant.CUSTOME_FOOD_LIST_SERVING_DATETIME,
							selectedTime);

					long rowId = AppConstant.mDataBase.insert(
							AppConstant.TAB_CUSTOME_FOOD_LIST, null, values);
					// dbAdapter.close();

					Cursor c = AppConstant.mDataBase.rawQuery("SELECT "
							+ AppConstant.CUSTOME_FOOD_LIST_USER_FOOD_ID
							+ " FROM " + AppConstant.TAB_CUSTOME_FOOD_LIST
							+ " WHERE rowid = " + rowId, null);
					int userFoodId = 0;
					if (c.getCount() > 0) {
						c.moveToFirst();
						userFoodId = c
								.getInt(c
										.getColumnIndex(AppConstant.CUSTOME_FOOD_LIST_USER_FOOD_ID));

					}
					c.close();
					/*
					 * SimpleDateFormat df = new SimpleDateFormat(
					 * "dd-MMM-yyyy EEEE"); String formattedDate =
					 * df.format(calender.getTime());
					 */
					if (userFoodId != 0) {
						ContentValues intakeValues = new ContentValues();
						intakeValues.put(
								AppConstant.USER_FOOD_INTAKE_USER_FOOD_ID,
								userFoodId);
						intakeValues.put(AppConstant.USER_FOOD_INTAKE_TIME,
								intakeDateTimeToSet);
						intakeValues.put(AppConstant.USER_FOOD_INTAKE_FOOD_ID,
								foodSelected.getFoodId());
						intakeValues.put(AppConstant.USER_FOOD_INTAKE_USER_ID,
								0);
						AppConstant.mDataBase.insert(
								AppConstant.TAB_USER_FOOD_INTAKE, null,
								intakeValues);
					}
					dbAdapter.close();
					this.finish();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}}
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		selectedTime = parent.getItemAtPosition(position).toString();

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		selectedTime = "Anytime";
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		
		/*switch (item.getItemId()) {
        case android.R.id.home:
            // go to previous screen when app icon in action bar is clicked
            Intent intent = new Intent(this, DashBoard.class);
           // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
    }*/ this.finish();
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (edtQuantity.getText().toString() != null || !(edtQuantity.getText().toString().isEmpty())) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					LogFoodConsumption.this);
	 
				// set title
				alertDialogBuilder.setTitle("Security Alert!!");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("Your Data is Not Saved, would you like to continue...")
					.setCancelable(false)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
							LogFoodConsumption.super.onBackPressed();
						}
					  })
					.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
		}
		
		
	}
}
