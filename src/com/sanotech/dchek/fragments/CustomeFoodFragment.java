package com.sanotech.dchek.fragments;

import java.sql.SQLException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sanotech.dchek.AppConstant;
import com.sanotech.dchek.R;
import com.sanotech.dchek.adapters.DatabaseAdapter;

@SuppressLint("ValidFragment")
public class CustomeFoodFragment extends Fragment implements
		OnItemSelectedListener {

	private EditText edtFoodName, edtCalories;
	private Spinner spUnit;
	private Button btnSave;
	private String unit = null;
	private DatabaseAdapter dbAdapter;
	public CustomeFoodFragment(long intakeDateTime) {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_custome_food,
				container, false);

		dbAdapter = new DatabaseAdapter(getActivity());
		edtFoodName = (EditText) rootView.findViewById(R.id.edt_cstm_food_name);
		spUnit = (Spinner) rootView.findViewById(R.id.sp_cstm_quantity);
		edtCalories = (EditText) rootView.findViewById(R.id.edt_cstm_calori);

		spUnit.setOnItemSelectedListener(this);

		btnSave = (Button) rootView.findViewById(R.id.btn_cstm_save);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String foodname = edtFoodName.getText().toString();
				String calorie = edtCalories.getText().toString();
				if (foodname != null && !foodname.isEmpty() && unit != null
						&& calorie != null && !calorie.isEmpty()) {
					try {
						dbAdapter.openDataBase();
						ArrayList<String> foodNameList = new ArrayList<String>();
						Cursor foodCursor = AppConstant.mDataBase.rawQuery("SELECT * FROM "+AppConstant.TAB_FOOD_LIST, null);
						if (foodCursor.getCount() > 0) {
							foodCursor.moveToFirst();
							for (int i = 0; i < foodCursor.getCount(); i++) {
							foodNameList.add(foodCursor.getString(foodCursor.getColumnIndex(AppConstant.FOOD_LIST_FOOD_NAME)));
							foodCursor.moveToNext();
							}
						}
						
						if (!foodNameList.contains(foodname)) {
							
						
						ContentValues values = new ContentValues();
						values.put(AppConstant.FOOD_LIST_FOOD_NAME, foodname);
						values.put(AppConstant.FOOD_LIST_FOOD_CAL, calorie);
						values.put(AppConstant.FOOD_LIST_SERVING_QUANTITY, unit);

						long rowId = AppConstant.mDataBase.insert(
								AppConstant.TAB_FOOD_LIST, null, values);

						dbAdapter.close();
						}else {
							Toast.makeText(getActivity().getApplicationContext(), "This Food detail is already added", Toast.LENGTH_SHORT).show();
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					edtFoodName.getText().clear();
					edtCalories.getText().clear();
					getActivity().getActionBar().setSelectedNavigationItem(0);

				}

			}
		});
		return rootView;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		unit = parent.getItemAtPosition(position).toString();

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
