package com.sanotech.dchek.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sanotech.dchek.R;
import com.sanotech.dchek.models.FoodListEntity;

public class FoodListAdapter extends BaseAdapter{
	private final LayoutInflater mInflater;
	private ArrayList<FoodListEntity> foodList = new ArrayList<FoodListEntity>();
	
	public FoodListAdapter(Context context,ArrayList<FoodListEntity> foodList ) {
		super();
		this.foodList = foodList;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public void setDataSet(ArrayList<FoodListEntity> adapterDTOs) {
		this.foodList = adapterDTOs;
		notifyDataSetChanged();
	}

	 @Override 
	 public View getView(int position, View convertView, ViewGroup parent) {
	        View view;
	 
	        if (convertView == null) {
	            view = mInflater.inflate(R.layout.common_food_list_items, parent, false);
	        } else {
	            view = convertView;
	        }
	 
	        ((TextView)view.findViewById(R.id.ls_tv_item)).setText(this.foodList.get(position).getFoodName());
	        ((TextView)view.findViewById(R.id.ls_tv_item)).setTag(this.foodList.get(position));
	        return view;
	    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return foodList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
