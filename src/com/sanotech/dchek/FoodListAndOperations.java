package com.sanotech.dchek;

import java.sql.SQLException;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.sanotech.dchek.adapters.DatabaseAdapter;
import com.sanotech.dchek.adapters.FoodPagerAdapter;
import com.sanotech.dchek.models.FoodListEntity;

public class FoodListAndOperations extends FragmentActivity implements TabListener {
    private ViewPager viewPager;
    private FoodPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles "Main",
    private String[] tabs = { "Common",  "Custom" };
    private ArrayList<FoodListEntity> foodList = new ArrayList<FoodListEntity>();
    private DatabaseAdapter dbAdapter;
    long intakeDateTimeToSet ;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_food_activity);
        dbAdapter = new DatabaseAdapter(FoodListAndOperations.this);

     /*   ActionBar actionBar1 = getActionBar();
		actionBar1.setDisplayHomeAsUpEnabled(true);
		actionBar1.setTitle("Dashboard");*/
		
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
         intakeDateTimeToSet   = getIntent().getExtras().getLong(AppConstant.INTAKE_DATE_TIME_TO_SET);
       fatchFoodList();
        mAdapter = new FoodPagerAdapter(getSupportFragmentManager(), intakeDateTimeToSet, foodList);
 
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Deshboard");
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);       
 
        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
 
        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
                if (position == 0) {
                	foodList.clear();
                	fatchFoodList();
                	 mAdapter = new FoodPagerAdapter(getSupportFragmentManager(), intakeDateTimeToSet, foodList);
                	 mAdapter.notifyDataSetChanged();
				}
              
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        
       
    }

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		 viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	private void fatchFoodList() {
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

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		
		switch (item.getItemId()) {
        case android.R.id.home:
            // go to previous screen when app icon in action bar is clicked
            Intent intent = new Intent(this, UserCustomeFoodList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
    }
		return super.onOptionsItemSelected(item);
	}
}
