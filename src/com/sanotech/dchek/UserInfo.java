package com.sanotech.dchek;

import java.io.File;
import java.sql.SQLException;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sanotech.dchek.adapters.DatabaseAdapter;
import com.sanotech.dchek.fragments.SelectDateFragment;

public class UserInfo extends Activity implements OnClickListener,
		DateTimeListener {
	private static final int PICK_IMAGE = 1;
	private static final int PICK_Camera_IMAGE = 2;
	private Button btnCamera, btnSave, btnBrowse;
	private ImageView userImage, imgCalander;
	private Uri imageUri;
	private Bitmap bitmap;
	private String filePath = null;
	private TextView usersDob;
	private DatabaseAdapter dbAdapter;
	private EditText userFname, userLname, userHeight, userWeight, userPhNo,
			userEmailId;
	private FooterView footer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		footer = (FooterView) findViewById(R.id.footer);
	    footer.initFooter(this);
	    
	    
	    ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Dashboard");
		
		dbAdapter = new DatabaseAdapter(getApplicationContext());

		btnCamera = (Button) findViewById(R.id.btn_capture);
		btnBrowse = (Button) findViewById(R.id.btn_browse_gallery);
		userImage = (ImageView) findViewById(R.id.img_user_info);
		btnSave = (Button) findViewById(R.id.btn_save_user_info);
		imgCalander = (ImageView) findViewById(R.id.img_cal);

		usersDob = (TextView) findViewById(R.id.tv_bd_txt);
		userFname = (EditText) findViewById(R.id.edt_user_fname);
		userLname = (EditText) findViewById(R.id.edt_user_lname);
		userHeight = (EditText) findViewById(R.id.edt_height);
		userWeight = (EditText) findViewById(R.id.edt_weight);
		userPhNo = (EditText) findViewById(R.id.edt_user_phone);
		userEmailId = (EditText) findViewById(R.id.edt_user_email_add);

		btnBrowse.setOnClickListener(this);
		btnCamera.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		imgCalander.setOnClickListener(this);
		String imgUri = PreferenceManager.getDefaultSharedPreferences(UserInfo.this).getString(AppConstant.LAST_SEVE_IMAGE, null);
		
		
		if (imgUri != null ) {
			File f = new File(imgUri);
			if (f.exists()) {
				userImage.setImageBitmap(DChackUtils.decodeFile(imgUri));
			}
			
		}
		try {
			dbAdapter.openDataBase();

			Cursor c = AppConstant.mDataBase.rawQuery("SELECT * From "
					+ AppConstant.TAB_USERS, null);
			if (c.getCount() > 0) {
				c.moveToFirst();
				userFname.setText(c.getString(c
						.getColumnIndex(AppConstant.USERS_FIRST_NAME)));
				userLname.setText(c.getString(c
						.getColumnIndex(AppConstant.USERS_LAST_NAME)));
				usersDob.setText(c.getString(c
						.getColumnIndex(AppConstant.USERS_DOB)));
				userHeight.setText(c.getString(c
						.getColumnIndex(AppConstant.USERS_HEIGHT)));
				userWeight.setText(c.getString(c
						.getColumnIndex(AppConstant.USERS_WEIGHT)));
				userPhNo.setText(c.getString(c
						.getColumnIndex(AppConstant.USERS_PHONE_NO)));
				userEmailId.setText(c.getString(c
						.getColumnIndex(AppConstant.USERS_EMAIL_ID)));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Uri selectedImageUri = null;
		switch (requestCode) {
		case PICK_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				selectedImageUri = data.getData();
			}
			break;
		case PICK_Camera_IMAGE:
			if (resultCode == RESULT_OK) {
				// use imageUri here to access the image
				selectedImageUri = imageUri;

			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Picture was not taken",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Picture was not taken",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		if (selectedImageUri != null) {
			try {
				// OI FILE Manager
				String filemanagerstring = selectedImageUri.getPath();

				// MEDIA GALLERY
				String selectedImagePath = getPath(selectedImageUri);

				if (selectedImagePath != null) {
					filePath = selectedImagePath;
				} else if (filemanagerstring != null) {
					filePath = filemanagerstring;
				} else {
					Toast.makeText(getApplicationContext(), "Unknown path",
							Toast.LENGTH_LONG).show();
					Log.e("Bitmap", "Unknown path");
				}
				File f = new File(filePath);
				
				if (filePath != null &&  f.exists()) {
					Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
					editor.putString(AppConstant.LAST_SEVE_IMAGE, filePath);
					editor.commit();
					userImage.setImageBitmap(DChackUtils.decodeFile(filePath));
				} else {
					bitmap = null;
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Internal error",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_capture:
			String fileName = "new-photo-name.jpg";
			// create parameters for Intent with filename
			ContentValues values = new ContentValues();
			values.put(MediaStore.Images.Media.TITLE, fileName);
			values.put(MediaStore.Images.Media.DESCRIPTION,
					"Image captured by camera");
			// imageUri is the current activity attribute, define and save it
			// for later usage (also in onSaveInstanceState)
			imageUri = getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			// create new Intent
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, PICK_Camera_IMAGE);
			break;
		case R.id.btn_browse_gallery:
			try {
				Intent gintent = new Intent();
				gintent.setType("image/*");
				gintent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(gintent, "Select Picture"),
						PICK_IMAGE);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
			break;
		case R.id.btn_save_user_info:
			String date = null;
			String firstName = userFname.getText().toString();
			String lastName = userLname.getText().toString();
			String phNo = userPhNo.getText().toString();
			String email = userEmailId.getText().toString();
			
			
			if (validation()) {

				try {
					dbAdapter.openDataBase();

					/*
					 * ContentValues cv = new ContentValues();
					 * cv.put("DateOfBirth", dob.getText().toString());
					 * AppConstant.mDataBase.insert("Users", null, cv);
					 */

					Cursor c = AppConstant.mDataBase.rawQuery("SELECT * From "
							+ AppConstant.TAB_USERS, null);
					if (c.getCount() > 0) {
						c.moveToFirst();
						int userId = c.getInt(c
								.getColumnIndex(AppConstant.USERS_USERID));

						ContentValues contentValue = new ContentValues();
						contentValue.put(AppConstant.USERS_FIRST_NAME,
								firstName);
						contentValue.put(AppConstant.USERS_LAST_NAME, lastName);
						contentValue.put(AppConstant.USERS_DOB, usersDob
								.getText().toString());
						contentValue.put(AppConstant.USERS_HEIGHT, userHeight
								.getText().toString() == null ? "" : userHeight
								.getText().toString());
						contentValue.put(AppConstant.USERS_WEIGHT, userWeight
								.getText().toString() == null ? "" : userWeight
								.getText().toString());
						contentValue.put(AppConstant.USERS_EMAIL_ID, email);
						contentValue.put(AppConstant.USERS_PHONE_NO, phNo);
						contentValue.put(AppConstant.USERS_MOBILE_TYPE,
								getDeviceName());

						AppConstant.mDataBase.update(AppConstant.TAB_USERS,
								contentValue, AppConstant.USERS_USERID + "="
										+ userId, null);
						Toast.makeText(UserInfo.this,
								"User Information updated successfully",
								Toast.LENGTH_SHORT).show();
					} else {
						ContentValues contentValue = new ContentValues();
						contentValue.put(AppConstant.USERS_FIRST_NAME,
								firstName);
						contentValue.put(AppConstant.USERS_LAST_NAME, lastName);
						contentValue.put(AppConstant.USERS_DOB, usersDob
								.getText().toString());
						contentValue.put(AppConstant.USERS_HEIGHT, userHeight
								.getText().toString());
						contentValue.put(AppConstant.USERS_WEIGHT, userWeight
								.getText().toString());
						contentValue.put(AppConstant.USERS_EMAIL_ID, email);
						contentValue.put(AppConstant.USERS_PHONE_NO, phNo);
						contentValue.put(AppConstant.USERS_MOBILE_TYPE,
								getDeviceName());

						AppConstant.mDataBase.insert(AppConstant.TAB_USERS,
								null, contentValue);
						Toast.makeText(UserInfo.this,
								"User Information save successfully",
								Toast.LENGTH_SHORT).show();
					}
					c.close();
					dbAdapter.close();
					startActivity(new Intent(UserInfo.this, DashBoard.class));
					finish();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// Toast.makeText(UserInfo.this, date.toString(),
			// Toast.LENGTH_SHORT).show();
			break;

		case R.id.img_cal:
			SelectDateFragment dateFragment = new SelectDateFragment(
					UserInfo.this, this);
			dateFragment.show(UserInfo.this.getFragmentManager(), "DatePicker");
			break;
		default:
			break;
		}

	}

	private String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	

	@Override
	public void setDate(String date) {
		// TODO Auto-generated method stub
		usersDob.setText(date);
	}

	@Override
	public void setTime(String time) {
		// TODO Auto-generated method stub

	}

	private String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return model.toUpperCase(Locale.getDefault());
		} else {
			return manufacturer.toUpperCase(Locale.getDefault()) + " "
					+ model.toUpperCase(Locale.getDefault());
		}
	}
	
	private boolean validation(){
		String firstName = userFname.getText().toString();
		String lastName = userLname.getText().toString();
		String phNo = userPhNo.getText().toString();
		String email = userEmailId.getText().toString();
		
		if (firstName== null || firstName.trim().isEmpty()) {
			userFname.setError(getResources().getString(R.string.first_name));
			return false;
		}else if (lastName == null || lastName.trim().isEmpty()) {
			userLname.setError(getResources().getString(R.string.last_name));
			return false;
		}else if (phNo == null || phNo.trim().isEmpty() ) {
			userPhNo.setError(getResources().getString(R.string.enter_phone_no));
			return false;
		}else if (email == null || email.trim().isEmpty() ) {
			userEmailId.setError(getResources().getString(R.string.enter_email));
			return false;
		}else {
			if (phNo.length() < 6) {
				userPhNo.setError(getResources().getString(R.string.enter_phone_no));
				return false;
			}else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
				userEmailId.setError(getResources().getString(R.string.enter_email));
				
				return false;
			}else {
				return true;
			}
		
			
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		
		switch (item.getItemId()) {
        case android.R.id.home:
            // go to previous screen when app icon in action bar is clicked
            Intent intent = new Intent(this, DashBoard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
    }
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		//do nothing
	}
}
