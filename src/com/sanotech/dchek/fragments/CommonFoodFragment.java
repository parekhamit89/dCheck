package com.sanotech.dchek.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.sanotech.dchek.AppConstant;
import com.sanotech.dchek.LogFoodConsumption;
import com.sanotech.dchek.R;
import com.sanotech.dchek.adapters.DatabaseAdapter;
import com.sanotech.dchek.adapters.FoodListAdapter;
import com.sanotech.dchek.models.FoodListEntity;

@SuppressLint("ValidFragment")
public class CommonFoodFragment extends Fragment implements OnItemClickListener {

	private DatabaseAdapter dbAdapter;

	private ArrayList<FoodListEntity> foodList = new ArrayList<FoodListEntity>();
	private ArrayList<FoodListEntity> foodListBackup = new ArrayList<FoodListEntity>();

	private ListView listview;
	private EditText listSearch;
	private long intakeDateTimeToSet;
	private FoodListAdapter adapter;

	public CommonFoodFragment(long intakeDateTimeToSet, ArrayList<FoodListEntity> foodList) {
		this.intakeDateTimeToSet = intakeDateTimeToSet;
		this.foodList = foodList;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		dbAdapter = new DatabaseAdapter(getActivity());

		View rootView = inflater.inflate(R.layout.fragment_common_food,
				container, false);
		listview = (ListView) rootView.findViewById(R.id.ls_common_food);
		listSearch = (EditText) rootView.findViewById(R.id.edt_search_food);

		listview.setOnItemClickListener(this);

		//fatchFoodList();
		if (foodList.size() > 0) {
			adapter = new FoodListAdapter(getActivity(), foodList);
			/*
			 * ArrayAdapter<String> adapter = new
			 * ArrayAdapter<String>(getActivity(),
			 * android.R.layout.simple_list_item_1, items);
			 */
			listview.setAdapter(adapter);

		}

		listSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (foodList.size() > 0) {
					foodListBackup.addAll(foodList);
					List<FoodListEntity> list = filter(s.toString(),
							foodListBackup, true);
					// foodList.addAll(list);
					adapter.setDataSet((ArrayList<FoodListEntity>) list);
					adapter.notifyDataSetChanged();
				}
				

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		return rootView;
	}

	/*private void fatchFoodList() {
		try {
			dbAdapter.openDataBase();

			Cursor cursor = AppConstant.mDataBase.rawQuery("SELECT * From "
					+ AppConstant.TAB_FOOD_LIST, null);
			cursor.moveToFirst();
			if (cursor.getCount() > 0) {
				for (int i = 0; i < cursor.getCount(); i++) {

					FoodListEntity fle = new FoodListEntity();
					fle.setFoodId(cursor.getInt(cursor
							.getColumnIndex(AppConstant.FOOD_LIST_FOOD_ID)));
					fle.setFoodName(cursor.getString(cursor
							.getColumnIndex(AppConstant.FOOD_LIST_FOOD_NAME)));
					fle.setFoodCal(cursor.getInt(cursor
							.getColumnIndex(AppConstant.FOOD_LIST_FOOD_CAL)));
					fle.setSavingDateTme(cursor.getString(cursor
							.getColumnIndex(AppConstant.FOOD_LIST_SERVING_DATETIME)));
					fle.setSavingQuantity(cursor.getString(cursor
							.getColumnIndex(AppConstant.FOOD_LIST_SERVING_QUANTITY)));

					foodList.add(fle);
					cursor.moveToNext();
				}
			}
			cursor.close();
			dbAdapter.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		FoodListEntity selectedFood = (FoodListEntity) view.findViewById(
				R.id.ls_tv_item).getTag();

		Intent intent = new Intent(getActivity(), LogFoodConsumption.class);
		intent.putExtra("SlectedFood", selectedFood);
		intent.putExtra(AppConstant.INTAKE_DATE_TIME_TO_SET,
				this.intakeDateTimeToSet);
		startActivity(intent);
		//getActivity().finish();
	}

	private List<FoodListEntity> filter(String string,
			Iterable<FoodListEntity> iterable, boolean byName) {

		if (iterable == null)
			return new LinkedList<FoodListEntity>();
		else {
			List<FoodListEntity> collected = new ArrayList<FoodListEntity>();
			ArrayList<String> name = new ArrayList<String>();
			Iterator<FoodListEntity> iterator = iterable.iterator();
			if (iterator == null)
				return collected;

			while (iterator.hasNext()) {
				FoodListEntity item = iterator.next();

				if (item.getFoodName().contains(string)) {
					name.add(item.getFoodName().toString());
					collected.add(item);
				}

			}
			foodListBackup.clear();

			return collected;
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	//	fatchFoodList();
	}
	
	
}