package com.sanotech.dchek.models;


public class FoodEntity implements Item{

	public final CustomeFoodListEntity entity;

	public FoodEntity(CustomeFoodListEntity foodlistEnttity) {
		this.entity = foodlistEnttity;
		
	}
	
	public CustomeFoodListEntity getEntity() {
		return entity;
	}
	@Override
	public boolean isSection() {
		return false;
	}

	

	@Override
	public boolean isEntity() {
		// TODO Auto-generated method stub
		return true;
	}

}
