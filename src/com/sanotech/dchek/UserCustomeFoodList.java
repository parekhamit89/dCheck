package com.sanotech.dchek;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sanotech.dchek.adapters.DatabaseAdapter;
import com.sanotech.dchek.adapters.EntryAdapter;
import com.sanotech.dchek.models.CustomeFoodListEntity;
import com.sanotech.dchek.models.FoodEntity;
import com.sanotech.dchek.models.Item;
import com.sanotech.dchek.models.SectionItem;


public class UserCustomeFoodList extends Activity implements OnClickListener {
	//private ListView listHeader;
	private ListView listItems;
	//private SeparatedListAdapter adapter;
	private ImageView imgNext, imgPrevious;
	private TextView dayDate;
	private Calendar calender = Calendar.getInstance();
	private long formattedDate;
	private DatabaseAdapter dbAdapter;
	List<CustomeFoodListEntity> customeFoodList = new ArrayList<CustomeFoodListEntity>();
	private FooterView footer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custome_food_list);
		 footer = (FooterView) findViewById(R.id.footer);
		    footer.initFooter(this);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Dashboard");
		dbAdapter = new DatabaseAdapter(UserCustomeFoodList.this);

		dayDate = (TextView) findViewById(R.id.tv_day_date);
		imgNext = (ImageView) findViewById(R.id.img_next);
		imgPrevious = (ImageView) findViewById(R.id.img_previous);
		imgNext.setOnClickListener(this);
		imgPrevious.setOnClickListener(this);
		//listHeader = (ListView) this.findViewById(R.id.ls_header);
		// Get a reference to the ListView holder
		listItems = (ListView) this.findViewById(R.id.ls_items);

		
		//df = new SimpleDateFormat("dd-MM-yyyy");
		formattedDate = dateInMilisecond();
		dayDate.setText(new SimpleDateFormat("dd-MM-yyyy EEEE").format(calender.getTime()));
		arrangeListview();
	
		// Listen for Click events
		listItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long duration) {
				//String item = (String) adapter.getItem(position);
				/*Toast.makeText(getApplicationContext(), ""+position,
						Toast.LENGTH_SHORT).show();*/
				CustomeFoodListEntity entity = (CustomeFoodListEntity) view.getTag();
				Intent intent= new Intent(UserCustomeFoodList.this, LogFoodConsumption.class);
				intent.putExtra(AppConstant.INTAKE_DATE_TIME_TO_SET, entity.getSavingDateTme());
				intent.putExtra(AppConstant.INTAKE_FOODS, entity);
				startActivity(intent);
				//Toast.makeText(UserCustomeFoodList.this, entity.getFoodName(), Toast.LENGTH_SHORT).show();
				
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.custome_food_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			
			Intent intent = new Intent(UserCustomeFoodList.this,
					FoodListAndOperations.class);
			intent.putExtra(AppConstant.INTAKE_DATE_TIME_TO_SET, formattedDate);
			startActivity(intent);
			break;
		 case android.R.id.home:
	            // go to previous screen when app icon in action bar is clicked
	            Intent intentHome = new Intent(this, DashBoard.class);
	            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intentHome);
	            finish();
	            return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_next:
			calender.add(Calendar.DATE, 1);
			formattedDate = dateInMilisecond();
			arrangeListview();
			Log.v("NEXT DATE : ", " "+formattedDate);
			dayDate.setText(new SimpleDateFormat("dd-MM-yyyy EEEE").format(calender.getTime()));
			break;
		case R.id.img_previous:
			calender.add(Calendar.DATE, -1);
			formattedDate = dateInMilisecond();
			arrangeListview();
			Log.v("PREVIOUS DATE : ", ""+formattedDate);
			dayDate.setText(new SimpleDateFormat("dd-MM-yyyy EEEE").format(calender.getTime()));
			break;

		default:
			break;
		}

	}

	private List<CustomeFoodListEntity> fatchFoodList() {

		List<CustomeFoodListEntity> fatchedCustomeFoodList = new ArrayList<CustomeFoodListEntity>();

		try {
			ArrayList<Integer> userFoodId = new ArrayList<Integer>();
			ArrayList<Integer> userId = new ArrayList<Integer>();
			ArrayList<Integer> foodId = new ArrayList<Integer>();

			dbAdapter.openDataBase();

			Cursor c = AppConstant.mDataBase.rawQuery("SELECT * From "
					+ AppConstant.TAB_USER_FOOD_INTAKE + " WHERE "
					+ AppConstant.USER_FOOD_INTAKE_TIME + " = "
					+ formattedDate , null);
			if (c.getCount() > 0) {
				c.moveToFirst();
				for (int i = 0; i < c.getCount(); i++) {
					userFoodId
							.add(c.getInt(c
									.getColumnIndex(AppConstant.USER_FOOD_INTAKE_USER_FOOD_ID)));
					foodId.add(c.getInt(c
							.getColumnIndex(AppConstant.USER_FOOD_INTAKE_FOOD_ID)));
					userId.add(c.getInt(c
							.getColumnIndex(AppConstant.USER_FOOD_INTAKE_USER_ID)));
					c.moveToNext();
					
				}

			}
			c.close();
			if (userFoodId.size() > 0) {

				for (int userFoodId_one : userFoodId) {

					Cursor intakeCursor = AppConstant.mDataBase
							.rawQuery(
									"SELECT * From "
											+ AppConstant.TAB_CUSTOME_FOOD_LIST
											+ " WHERE "
											+ AppConstant.CUSTOME_FOOD_LIST_USER_FOOD_ID
											+ " = " + userFoodId_one ,
									null);
					if (intakeCursor.getCount() > 0) {

						intakeCursor.moveToFirst();
						for (int i = 0; i < intakeCursor.getCount(); i++) {
							final CustomeFoodListEntity CFLE = new CustomeFoodListEntity();

							CFLE.setUserFoodId(userFoodId_one);
							CFLE.setFoodId(intakeCursor.getInt(intakeCursor
									.getColumnIndex(AppConstant.CUSTOME_FOOD_LIST_FOOD_ID)));
							CFLE.setUserID(intakeCursor.getInt(intakeCursor
									.getColumnIndex(AppConstant.CUSTOME_FOOD_LIST_USER_ID)));
							CFLE.setFoodId(intakeCursor.getInt(intakeCursor
									.getColumnIndex(AppConstant.CUSTOME_FOOD_LIST_FOOD_ID)));
							CFLE.setFoodName(intakeCursor.getString(intakeCursor
									.getColumnIndex(AppConstant.CUSTOME_FOOD_LIST_FOOD_NAME)));
							CFLE.setFoodCal(intakeCursor.getInt(intakeCursor
									.getColumnIndex(AppConstant.CUSTOME_FOOD_LIST_FOOD_CAL)));
							CFLE.setSavingQuantity(intakeCursor.getString(intakeCursor
									.getColumnIndex(AppConstant.CUSTOME_FOOD_LIST_SERVING_QUANTITY)));
							CFLE.setSavingDateTme(intakeCursor.getString(intakeCursor
									.getColumnIndex(AppConstant.CUSTOME_FOOD_LIST_SERVING_DATETIME)));

							fatchedCustomeFoodList.add(CFLE);
							intakeCursor.moveToNext();
						}
						
					}
					intakeCursor.close();
				}
				dbAdapter.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fatchedCustomeFoodList;

	}
	
	private void arrangeListview(){
		customeFoodList = fatchFoodList();
		
	/*	final ArrayAdapter<String> headerEntryAdapter = new ArrayAdapter<String>(
				this, R.layout.list_header, new String[] {});

		listHeader.setAdapter(headerEntryAdapter);
		listHeader.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long duration) {
				String item = headerEntryAdapter.getItem(position);
				Toast.makeText(getApplicationContext(), item,
						Toast.LENGTH_SHORT).show();
			}
		});*/

		HashMap<String, List<CustomeFoodListEntity>> detailMap = new HashMap<String, List<CustomeFoodListEntity>>();

		// Create the ListView Adapter
		//adapter = new SeparatedListAdapter(this);
		// ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this,
		// R.layout.list_item, notes);

		// Add Sections
		List<CustomeFoodListEntity> lunchFood = new ArrayList<CustomeFoodListEntity>();
		List<CustomeFoodListEntity> breakFastFood = new ArrayList<CustomeFoodListEntity>();
		List<CustomeFoodListEntity> morningSnack = new ArrayList<CustomeFoodListEntity>();
		List<CustomeFoodListEntity> afterNoonSanck = new ArrayList<CustomeFoodListEntity>();
		List<CustomeFoodListEntity> dinner = new ArrayList<CustomeFoodListEntity>();
		List<CustomeFoodListEntity> anyTime = new ArrayList<CustomeFoodListEntity>();

		for (CustomeFoodListEntity foodEntity : customeFoodList) {
				String intakeTime = foodEntity.getSavingDateTme();

			if (intakeTime.equalsIgnoreCase("Breakfast")) {
				breakFastFood.add(foodEntity);
				detailMap.put(intakeTime.toUpperCase(Locale.getDefault()),
						breakFastFood);

			} else if (intakeTime.equalsIgnoreCase("Morning snack")) {
				morningSnack.add(foodEntity);
				detailMap.put(intakeTime.toUpperCase(Locale.getDefault()),
						morningSnack);

			} else if (intakeTime.equalsIgnoreCase("Lunch")) {
				lunchFood.add(foodEntity);
				detailMap.put(intakeTime.toUpperCase(Locale.getDefault()),
						lunchFood);

			} else if (intakeTime.equalsIgnoreCase("Afternoon Snack")) {
				afterNoonSanck.add(foodEntity);
				detailMap.put(intakeTime.toUpperCase(Locale.getDefault()),
						afterNoonSanck);

			} else if (intakeTime.equalsIgnoreCase("Dinner")) {
				dinner.add(foodEntity);
				detailMap.put(intakeTime.toUpperCase(Locale.getDefault()),
						dinner);

			} else {
				anyTime.add(foodEntity);
				detailMap.put("Anytime", dinner);

			}
		}
		/*if (detailMap.size()== 0) {
			ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this,
					R.layout.list_item, new String[]{""});
			adapter.addSection(" 0 Calorie Eaten \n 1653 Calories left toyour budget", listadapter);
		}*/
		ArrayList<Item> items = new ArrayList<Item>();

		for (String key : detailMap.keySet()) {
			/*ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this,
					R.layout.list_item, detailMap.get(key));
			*/
			items.add(new SectionItem(key));

			
			for (int i = 0; i < detailMap.get(key).size(); i++) {
				items.add(new FoodEntity(detailMap.get(key).get(i)));
				

			}
			//adapter.addSection(key, items);
			
		}

		EntryAdapter adapter = new EntryAdapter(this, items);

		// Set the adapter on the ListView holder
		listItems.setAdapter(adapter);

	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		//adapter.notifyDataSetChanged();
		
		arrangeListview();
	}
	
	@Override
	public void onBackPressed() {
		// do nothing
	}
	
	private long dateInMilisecond(){
		
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df.format(calender.getTime());
		Date date1 = null;
		try {
			date1 = (Date)df.parse(formattedDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date1.getTime();
	}
}
