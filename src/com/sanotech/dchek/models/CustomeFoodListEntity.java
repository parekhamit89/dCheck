package com.sanotech.dchek.models;

import java.io.Serializable;

public class CustomeFoodListEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int userFoodId;
	public int foodId;
	public int userID;
	public String foodName;
	public int  foodCal;
	public String savingQuantity;
	public String savingDateTme;
	
	public int getUserFoodId() {
		return userFoodId;
	}
	public void setUserFoodId(int userFoodId) {
		this.userFoodId = userFoodId;
	}
	public int getFoodId() {
		return foodId;
	}
	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
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
	
	
}
