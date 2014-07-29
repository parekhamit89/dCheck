package com.sanotech.dchek;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sanotech.dchek.adapters.DatabaseAdapter;
import com.sanotech.dchek.models.CustomeFoodListEntity;

public class DashBoard extends Activity implements OnRefreshListener,
		OnClickListener, OnTouchListener {
	private SwipeRefreshLayout swipeLayout;
	private LinearLayout bgLevelValues, drAppointment, calorieDetails;
	private ImageView imgUser,imgBGIndicator;
	private DatabaseAdapter dbAdapter;
	private TextView todayCalories, weekCalories, nextVisit, lastVisit, todayBGLevel,weekBGLevel;
	private FooterView footer;
	private ScrollView scrollView; 
	boolean isBlockScroll = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_activity);

		 footer = (FooterView) findViewById(R.id.rl_icons);
	    footer.initFooter(this);
		
	   /* ActionBar actionBar = getActionBar();
	    actionBar.hide();*/
		dbAdapter = new DatabaseAdapter(DashBoard.this);

		imgUser = (ImageView) findViewById(R.id.img_user);
		todayCalories = (TextView) findViewById(R.id.tv_today_cl_value);
		weekCalories = (TextView) findViewById(R.id.tv_week_cl_value);
		nextVisit = (TextView) findViewById(R.id.tv_next_dr_value);
		lastVisit = (TextView) findViewById(R.id.tv_last_dr_value);
		todayBGLevel = (TextView)findViewById(R.id.tv_today_bg_value);
		weekBGLevel = (TextView)findViewById(R.id.tv_week_bg_value);
		scrollView = (ScrollView)findViewById(R.id.scrollView);
		imgBGIndicator = (ImageView)findViewById(R.id.img_bg_indicator);
		
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		bgLevelValues = (LinearLayout) findViewById(R.id.ll_bg_values);
		bgLevelValues.setOnTouchListener(mTouchListener);

		drAppointment = (LinearLayout) findViewById(R.id.ll_dr_values);
		drAppointment.setOnTouchListener(mTouchListener);

		calorieDetails = (LinearLayout) findViewById(R.id.ll_cl_values);
		calorieDetails.setOnTouchListener(mTouchListener);
		imgUser.setOnClickListener(this);
		((TextView)findViewById(R.id.tv_updated_date)).setText("Last updated 2 hrs ago");
		
		DatabaseAdapter dataBaseAdapter = new DatabaseAdapter(DashBoard.this);
		try {
			dataBaseAdapter.createDataBase();
			dataBaseAdapter.openDataBase();
			dataBaseAdapter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String imgUri = PreferenceManager.getDefaultSharedPreferences(
				DashBoard.this).getString(AppConstant.LAST_SEVE_IMAGE, null);
		
		
		if (imgUri != null ) {
			File f = new File(imgUri);
			if (f.exists()) {
				imgUser.setImageBitmap(DChackUtils.decodeFile(imgUri));
			}
			
		}
		
		scrollView.setOnTouchListener(this);
		getBgDetail();
		calorieCount();
		getDoctorDetail();
		getUserDetail();
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeLayout.setRefreshing(false);
			}
		}, 5000);
	}

	boolean mItemPressed = false;
	boolean mSwiping = false;
	int SWIPE_DURATION = 250;
	int MOVE_DURATION = 150;
	View.OnTouchListener mTouchListener = new View.OnTouchListener() {

		float mDownX;
		private int mSwipeSlop = -1;

		@SuppressLint("NewApi")
		@Override
		public boolean onTouch(final View v, MotionEvent event) {
		
			
			//scrollView.setEnabled(false);
			isBlockScroll =  true;
			if (mSwipeSlop < 0) {
				mSwipeSlop = ViewConfiguration.get(DashBoard.this)
						.getScaledTouchSlop();
			}
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (mItemPressed) {
					// Multi-item swipes not handled
					return false;
				}
				mItemPressed = true;
				mDownX = event.getX();
				break;
			case MotionEvent.ACTION_CANCEL:
				v.setAlpha((float) 1.0);
				v.setTranslationX((float) 0.0);
				mItemPressed = false;
				break;
			case MotionEvent.ACTION_MOVE: {
				//swipeLayout.setEnabled(false);
				
				
				float x = event.getX() + v.getTranslationX();
				float deltaX = x - mDownX;
				float deltaXAbs = Math.abs(deltaX);
				// this allow animation from left to right
				if (deltaX > 0) {
					if (!mSwiping) {
						if (deltaXAbs > mSwipeSlop) {
							mSwiping = true;

							// ll_bg_level.requestDisallowInterceptTouchEvent(true);
							// mBackgroundContainer.showBackground(v.getTop(),
							// v.getHeight());
						}
					}
					if (mSwiping) {
						v.setTranslationX((x - mDownX));
						v.setAlpha(1 - deltaXAbs / v.getWidth());
					}
				}
			}
				break;
			case MotionEvent.ACTION_UP: {
				// User let go - figure out whether to animate the view out, or
				// back into place
				if (mSwiping) {
					float x = event.getX() + v.getTranslationX();
					float deltaX = x - mDownX;
					float deltaXAbs = Math.abs(deltaX);
					float fractionCovered;
					float endX;
					float endAlpha;
					final boolean remove;
					if (deltaXAbs > v.getWidth() / 4) {
						// Greater than a quarter of the width - animate it out
						fractionCovered = deltaXAbs / v.getWidth();
						endX = deltaX < 0 ? -v.getWidth() : v.getWidth();
						endAlpha = 0;
						remove = true;
					} else {
						// Not far enough - animate it back
						fractionCovered = 1 - (deltaXAbs / v.getWidth());
						endX = 0;
						endAlpha = 1;
						remove = false;
					}
					// Animate position and alpha of swiped item
					// NOTE: This is a simplified version of swipe behavior, for
					// the
					// purposes of this demo about animation. A real version
					// should use
					// velocity (via the VelocityTracker class) to send the item
					// off or
					// back at an appropriate speed.
					long duration = (int) ((1 - fractionCovered) * SWIPE_DURATION);
					// ll_bg_level.setEnabled(false);
					v.animate().setDuration(duration).alpha(endAlpha)
							.translationX(endX).withEndAction(new Runnable() {
								@Override
								public void run() {
									// Restore animated values
									v.setAlpha((float) 1.0);
									v.setTranslationX(0);
									if (remove) {
										// animateRemoval(mListView, v);
										// ll_bg_level.setEnabled(true);
										if (v.getId() == R.id.ll_bg_values) {
											startActivity(new Intent(
													DashBoard.this,
													BGLevel.class));
											finish();

										} else if (v.getId() == R.id.ll_dr_values) {
											startActivity(new Intent(
													DashBoard.this,
													DoctorInfo.class));
											finish();
										} else if (v.getId() == R.id.ll_cl_values) {
											startActivity(new Intent(
													DashBoard.this,
													UserCustomeFoodList.class));
											finish();
										}

									} else {
										mSwiping = false;

									}
								}
							});
					
				}
				
				swipeLayout.setEnabled(true);
				isBlockScroll =  false;
				
				scrollView.setEnabled(true);
				
				
			}
				mItemPressed = false;
				break;
			default:
				return false;
			}
			return true;
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_user:
			startActivity(new Intent(DashBoard.this, UserInfo.class));
			finish();
			break;

		default:
			break;
		}

	}
	@Override
	protected void onRestart() {
		super.onRestart();
		calorieCount();
		getDoctorDetail();
		getBgDetail();
		footer.initFooter(this);
		getUserDetail();
	}
	private void calorieCount() {

		try {
			dbAdapter.openDataBase();
			Cursor todayCalorie = AppConstant.mDataBase.rawQuery(
					"SELECT * FROM " + AppConstant.TAB_USER_FOOD_INTAKE
							+ " WHERE " + AppConstant.USER_FOOD_INTAKE_TIME
							+ " >= " + getTodayDateStamp() , null);
			// dbAdapter.close();

			List<CustomeFoodListEntity> todaysDetail = new ArrayList<CustomeFoodListEntity>();
			todaysDetail.addAll(getFoodList(todayCalorie));
			int temp = 0;
			for (int i = 0; i < todaysDetail.size(); i++) {
				temp += (todaysDetail.get(i).getFoodCal() * Integer
						.parseInt(todaysDetail.get(i).getSavingQuantity()));
			}
			todayCalories.setText(String.valueOf(temp) + " cal");
			if (temp > 2500) {
				todayCalories.setTextColor(Color.RED);
			} else if (temp > 1900 && temp < 2500) {
				todayCalories.setTextColor(Color.YELLOW);
			} else {
				todayCalories.setTextColor(Color.GREEN);
			}
			todayCalorie.close();

			Cursor weekCalorie = AppConstant.mDataBase.rawQuery(
					"SELECT * FROM " + AppConstant.TAB_USER_FOOD_INTAKE
							+ " WHERE " + AppConstant.USER_FOOD_INTAKE_TIME
							+ " <='" + DChackUtils.getdate() + "' AND "
							+ AppConstant.USER_FOOD_INTAKE_TIME + " >= '"
							+ getDateOfWeeks() + "'", null);
			List<CustomeFoodListEntity> weekDetail = new ArrayList<CustomeFoodListEntity>();
			weekDetail.addAll(getFoodList(weekCalorie));

			int temp1 = 0;
			for (int i = 0; i < weekDetail.size(); i++) {
				temp1 += (Integer.parseInt(weekDetail.get(i)
						.getSavingQuantity()) * weekDetail.get(i).getFoodCal());
			}

			weekCalories.setText((temp1 / 7) + " cal (avg)");
			weekCalorie.close();
			dbAdapter.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

	private List<CustomeFoodListEntity> getFoodList(Cursor cursor) {

		List<CustomeFoodListEntity> fatchedCustomeFoodList = new ArrayList<CustomeFoodListEntity>();

		ArrayList<Integer> userFoodId = new ArrayList<Integer>();
		ArrayList<Integer> userId = new ArrayList<Integer>();
		ArrayList<Integer> foodId = new ArrayList<Integer>();

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++) {
				userFoodId
						.add(cursor.getInt(cursor
								.getColumnIndex(AppConstant.USER_FOOD_INTAKE_USER_FOOD_ID)));
				foodId.add(cursor.getInt(cursor
						.getColumnIndex(AppConstant.USER_FOOD_INTAKE_FOOD_ID)));
				userId.add(cursor.getInt(cursor
						.getColumnIndex(AppConstant.USER_FOOD_INTAKE_USER_ID)));
				cursor.moveToNext();

			}

		}
		cursor.close();
		if (userFoodId.size() > 0) {

			for (int userFoodId_one : userFoodId) {

				Cursor intakeCursor = AppConstant.mDataBase.rawQuery(
						"SELECT * From " + AppConstant.TAB_CUSTOME_FOOD_LIST
								+ " WHERE "
								+ AppConstant.CUSTOME_FOOD_LIST_USER_FOOD_ID
								+ " = " + userFoodId_one, null);
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

		}

		return fatchedCustomeFoodList;

	}

	private void getDoctorDetail() {
		try {
			dbAdapter.openDataBase();
			Cursor cursorVisit = AppConstant.mDataBase.rawQuery("SELECT * FROM "
					+ AppConstant.TAB_DOCTOR_VISIT, null);
			String lVisit, nVisit;
			if (cursorVisit.getCount() > 0) {
				cursorVisit.moveToLast();
				lVisit = cursorVisit.getString(cursorVisit
						.getColumnIndex(AppConstant.DOCTOR_VISIT_LAST_VISIT));
				nVisit = cursorVisit.getString(cursorVisit
						.getColumnIndex(AppConstant.DOCTOR_VISIT_NEXT_VISIT));
				cursorVisit.close();

				nextVisit.setText(nVisit.equalsIgnoreCase("no visit")?"no visit": DChackUtils.getTimeinString(Long.parseLong(nVisit)));
				lastVisit.setText(lVisit.equalsIgnoreCase("no visit")?"no visit": DChackUtils.getTimeinString(Long.parseLong(lVisit)));

			}
			dbAdapter.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getBgDetail(){
		try {
			dbAdapter.openDataBase();
			Cursor todayBgCursor = AppConstant.mDataBase.rawQuery("SELECT * FROM "
					+ AppConstant.TAB_USER_BG_LEVEL +" WHERE "+AppConstant.BG_LEVEL_RECORDED_TIME+ " >= " + getTodayDateStamp(), null);

			if (todayBgCursor.getCount()  > 0) {
				todayBgCursor.moveToLast();
				int temp = 0;
				//for (int i = 0; i < todayBgCursor.getCount(); i++) {
				temp = Integer.parseInt(todayBgCursor.getString(todayBgCursor.getColumnIndex(AppConstant.BG_LEVEL_LEVEL)));
				//todayBgCursor.moveToNext();
				//}
				todayBGLevel.setText(temp+" mg/dL");
				
				if (temp > 140) {
					imgBGIndicator.setBackgroundColor(Color.RED);
				}else if (temp > 100 && temp < 140  ) {
					imgBGIndicator.setBackgroundColor(Color.YELLOW);
				}else {
					imgBGIndicator.setBackgroundColor(Color.GREEN);
				}
			}else {
				todayBGLevel.setText("Yet to log");
			}
			todayBgCursor.close();
			
			
			Cursor weekBgCursor= AppConstant.mDataBase.rawQuery(
					"SELECT * FROM " + AppConstant.TAB_USER_BG_LEVEL
							+ " WHERE " + AppConstant.BG_LEVEL_RECORDED_TIME+ " <= " + DChackUtils.getdate()+" AND "+AppConstant.BG_LEVEL_RECORDED_TIME+ " >= " + getDateOfWeeks() , null);
			
			//BETWEEN datetime('now', ' -6 days') AND datetime('now', 'localtime')
			int grandTotal = 0;
			if (weekBgCursor.getCount()  > 0) {
				int temp=0;
				weekBgCursor.moveToFirst();
				
				for (int i = 0; i < weekBgCursor.getCount(); i++) {
					temp += weekBgCursor.getInt(weekBgCursor.getColumnIndex(AppConstant.BG_LEVEL_LEVEL));
					weekBgCursor.moveToNext();
				}//temp/weekBgCursor.getCount()
				grandTotal = temp/weekBgCursor.getCount();
				weekBGLevel.setText(grandTotal +" mg/dL (avg)");
			}else {
				weekBGLevel.setText("Yet to log");
			}
			weekBgCursor.close();
			dbAdapter.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void getUserDetail(){
		try {
			dbAdapter.openDataBase();
			
			Cursor userInfoCursor = AppConstant.mDataBase.rawQuery("SELECT * FROM "+AppConstant.TAB_USERS, null);
			
			if (userInfoCursor.getCount() > 0) {
				userInfoCursor.moveToLast();
				String name = userInfoCursor.getString(userInfoCursor.getColumnIndex(AppConstant.USERS_FIRST_NAME));
				((TextView)findViewById(R.id.tv_user_name)).setText("Hello "+name);
			}
			userInfoCursor.close();
			dbAdapter.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private long getDateOfWeeks() {
		Calendar calender = Calendar.getInstance();

		calender.add(Calendar.DATE, -7);
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
		
		String weekDate = df.format(calender.getTime());
		Date date1 = null;
		try {
			 date1 = (Date)df.parse(df.format(calender.getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date1.getTime();
	}
	
	private long getTodayDateStamp(){
		Calendar calender = Calendar.getInstance();

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		
		String weekDate = df.format(calender.getTime());
		Date date1 = null;
		try {
			 date1 = (Date)df.parse(df.format(calender.getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date1.getTime();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.scrollView) {
			
		}
		return false;
	}

}
