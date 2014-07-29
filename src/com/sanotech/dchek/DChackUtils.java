package com.sanotech.dchek;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;

public class DChackUtils {
	
	
	
public static Bitmap decodeFile(String filePath) {
		
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		 o2.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
		return getRoundedShape(bitmap);

	}

	private static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		// TODO Auto-generated method stub
		int targetWidth = 500;
		int targetHeight = 500;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,

		targetHeight, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}
	

	public static long getdate() {
		Calendar calender = Calendar.getInstance();
		
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
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
	
	
	public static String getdate(String date) {
		
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault());
		Date date1 = null;
		try {
			date1 = (Date)df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new SimpleDateFormat("dd-MM-yyyy hh:mm aa").format(date1);
	}
	
	public static long getTimeStemp(String date){// 
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
		Date date1 = null;
		try {
			date1 = (Date)df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return date1.getTime();
		
	}
	
	public static String getTimeinString(long millis){
		Date date=new Date(millis);
		return  new SimpleDateFormat("dd-MM-yyyy hh:mm aa").format(date);
		
	}
}
