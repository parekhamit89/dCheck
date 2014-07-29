package com.sanotech.dchek;

import android.database.sqlite.SQLiteDatabase;

public class AppConstant {
	
	public static final String LAST_SEVE_IMAGE = "ImageSave";
	
	public static final String INTAKE_DATE_TIME_TO_SET="IntakeDateTime";
	public static final String INTAKE_FOODS="IntakeFoodEntity";
	public static final String SELECTED_FOOD ="SlectedFood";
	
	public static  SQLiteDatabase mDataBase; 
	public static final String TAB_USERS = "Users";
	public static final String TAB_DOCTOR_VISIT = "DoctorVisit";
	public static final String TAB_USER_BG_LEVEL = "UserBGLevel";
	public static final String TAB_FOOD_LIST = "FoodList";
	public static final String TAB_CUSTOME_FOOD_LIST = "UserCustomeFoodList";
	public static final String TAB_DOCTOR_INFO = "DoctorInfo";
	public static final String TAB_USER_FOOD_INTAKE = "UserFoodIntake";
	
	// declaration of Users table field 
	public static final String USERS_USERID = "UserId";
	public static final String USERS_FIRST_NAME = "FirstName"; 
	public static final String USERS_LAST_NAME = "LastName";
	public static final String USERS_SEX = "Sex";
	public static final String USERS_DOB = "DateOfBirth";
	public static final String USERS_HEIGHT = "Height";
	public static final String USERS_WEIGHT = "Weight";
	public static final String USERS_USER_NAME = "UserName";
	public static final String USERS_EMAIL_ID = "EmailId";
	public static final String USERS_PHONE_NO = "PhoneNumber";
	public static final String USERS_MOBILE_TYPE = "MobileType";
	public static final String USERS_ALIMENT_ID = "AlimentId";
	
	//Doctore Vist Field
	public static final String DOCTOR_VISIT_USER_ID = "UserId";
	public static final String DOCTOR_VISIT_DOCTOR_ID = "DoctorId";
	public static final String DOCTOR_VISIT_LAST_VISIT = "LastVisit";
	public static final String DOCTOR_VISIT_NEXT_VISIT = "NextVisit"; 
	
	//User bg level
	public static final String BG_LEVEL_USER_ID = "UserId";
	public static final String BG_LEVEL_LEVEL ="BGLevel";
	public static final String BG_LEVEL_RECORDED_TIME = "RecordedTime";
	public static final String BG_LEVEL_TIME_CHECK ="TimeOfCheck";
	public static final String BG_LEVEL_NOTE = "Notes";
	
	//Food List
	public static final String FOOD_LIST_FOOD_ID ="FoodId";
	public static final String FOOD_LIST_FOOD_NAME = "FoodName";
	public static final String FOOD_LIST_FOOD_CAL = "FoodCal";
	public static final String FOOD_LIST_SERVING_QUANTITY = "ServingQuantity";
	public static final String FOOD_LIST_SERVING_DATETIME = "ServingDateTime";
	
	//UserCustomer Food List
	public static final String CUSTOME_FOOD_LIST_USER_FOOD_ID = "UserFoodId";
	public static final String CUSTOME_FOOD_LIST_FOOD_ID = "FoodId";
	public static final String CUSTOME_FOOD_LIST_USER_ID = "UserId";
	public static final String CUSTOME_FOOD_LIST_FOOD_NAME = "FoodName";
	public static final String CUSTOME_FOOD_LIST_FOOD_CAL ="FoodCal";
	public static final String CUSTOME_FOOD_LIST_SERVING_QUANTITY  = "ServingQuantity";
	public static final String CUSTOME_FOOD_LIST_SERVING_DATETIME = "ServingDateTime";

	//Doctor Info
	public static final String DOCTOR_INFO_ID = "DoctorId";
	public static final String DOCTOR_INFO_FIRST_NAME = "FirstName";
	public static final String DOCTOR_INFO_LAST_NAME = "LastName";
	public static final String DOCTOR_INFO_HOSPITAL_NAME = "HospitalName";
	public static final String DOCTOR_INFO_PH_NO = "PhoneNumber";
	public static final String DOCTOR_INFO_EMAIL_ID = "EmailId";
	
	// FoodInteke time
	public static final String USER_FOOD_INTAKE_USER_FOOD_ID = "UserFoodId";
	public static final String USER_FOOD_INTAKE_FOOD_ID ="FoodId"; 
	public static final String USER_FOOD_INTAKE_USER_ID = "UserId";
	public static final String USER_FOOD_INTAKE_TIME = "FoodIntakeTime";
}