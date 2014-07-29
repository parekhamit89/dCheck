package com.sanotech.dchek.adapters;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sanotech.dchek.fragments.CommonFoodFragment;
import com.sanotech.dchek.fragments.CustomeFoodFragment;
import com.sanotech.dchek.models.FoodListEntity;
 
public class FoodPagerAdapter extends FragmentPagerAdapter  {
 long intakeDateTimeToSet;
 ArrayList<FoodListEntity> foodList = new ArrayList<FoodListEntity>();
    public FoodPagerAdapter(FragmentManager fm, long intakeDateTimeToSet,ArrayList<FoodListEntity> foodList) {
        super(fm);
        this.intakeDateTimeToSet = intakeDateTimeToSet;
        this.foodList = foodList;
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            Fragment commontFragment = new CommonFoodFragment(this.intakeDateTimeToSet, this.foodList);
        	
            return commontFragment;
        
        case 1:
            // Movies fragment activity
        	Fragment costomeFragment = new CustomeFoodFragment(this.intakeDateTimeToSet);
        	
            return costomeFragment;
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
 
}