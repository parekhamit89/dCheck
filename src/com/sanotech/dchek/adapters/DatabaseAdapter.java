package com.sanotech.dchek.adapters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import com.sanotech.dchek.AppConstant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseAdapter extends SQLiteOpenHelper {

	private static String TAG = "DataBaseHelper"; // Tag just for the LogCat window
	private static String DB_PATH ;//path of our database
	private static String DB_NAME ="dChack.db";// Database name
	private final Context mContext;

	public DatabaseAdapter(Context context) 
	{
	    super(context, DB_NAME, null, 1);
	    this.mContext = context;
	    DB_PATH = context.getFilesDir().getParentFile().getPath()+ "/databases/";
	   
	}   

	public void createDataBase() throws IOException
	{
	    //If database not exists copy it from the assets

	    boolean mDataBaseExist = checkDataBase();
	    if(!mDataBaseExist)
	    {
	        this.getReadableDatabase();
	        this.close();
	        try 
	        {
	            //Copy the database from assests
	            copyDataBase();
	            Log.e(TAG, "createDatabase database created");
	        } 
	        catch (IOException mIOException) 
	        {
	            throw new Error("ErrorCopyingDataBase");
	        }
	    }
	}
	    //Check that the database exists here: /data/data/your package/databases/Da Name
	    private boolean checkDataBase()
	    {
	        File dbFile = new File(DB_PATH + DB_NAME);
	        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
	        return dbFile.exists();
	    }

	    //Copy the database from assets
	    private void copyDataBase() throws IOException
	    {
	        InputStream mInput = mContext.getAssets().open(DB_NAME);
	        String outFileName = DB_PATH + DB_NAME;
	        OutputStream mOutput = new FileOutputStream(outFileName);
	        byte[] mBuffer = new byte[1024];
	        int mLength;
	        while ((mLength = mInput.read(mBuffer))>0)
	        {
	            mOutput.write(mBuffer, 0, mLength);
	        }
	        mOutput.flush();
	        mOutput.close();
	        mInput.close();
	    }

	    //Open the database, so we can query it
	    public boolean openDataBase() throws SQLException
	    {
	        String mPath = DB_PATH + DB_NAME;
	        //Log.v("mPath", mPath);
	        AppConstant.mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
	        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	        return AppConstant.mDataBase != null;
	    }

	    @Override
	    public synchronized void close() 
	    {
	        if(AppConstant.mDataBase != null)
	        	AppConstant.mDataBase.close();
	        super.close();
	    }

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			AppConstant.mDataBase = db;
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}


}
