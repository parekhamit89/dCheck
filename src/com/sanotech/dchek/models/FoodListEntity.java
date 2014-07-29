package com.sanotech.dchek.models;

import java.io.Serializable;

public class FoodListEntity implements Serializable{
	
	public int foodId;
	public int getFoodId() {
		return foodId;
	}
	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public int getFoodCal() {
		return foodCal;
	}
	public void setFoodCal(int foodCal) {
		this.foodCal = foodCal;
	}
	public String getSavingQuantity() {
		return savingQuantity;
	}
	public void setSavingQuantity(String savingQuantity) {
		this.savingQuantity = savingQuantity;
	}
	public String getSavingDateTme() {
		return savingDateTme;
	}
	public void setSavingDateTme(String savingDateTme) {
		this.savingDateTme = savingDateTme;
	}
	public String foodName;
	public int  foodCal;
	public String savingQuantity;
	public String savingDateTme;
}
