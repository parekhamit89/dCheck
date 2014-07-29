package com.sanotech.dchek;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FooterView extends RelativeLayout  {
Context mContext;
	public FooterView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}
	
	public FooterView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}

	public FooterView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	}

	public void initFooter(Context context) {
		this.mContext = context;
        inflateFooter();
}
private ImageView imgHome, imgData, imgInfo, imgFriends;
private void inflateFooter() {
    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    inflater.inflate(R.layout.footer, this);
    imgHome =(ImageView)this.findViewById(R.id.img_home);
    imgData = (ImageView)this.findViewById(R.id.img_data);
    imgInfo = (ImageView)this.findViewById(R.id.img_info);
    imgFriends = (ImageView)this.findViewById(R.id.img_frinds);
    
    
    imgHome.setOnClickListener(homeOnClickListener);    
    imgData.setOnClickListener(myDataOnClickListener);
    imgFriends.setOnClickListener(friendsOnClickListener);
    if (this.mContext instanceof DashBoard) {
    	imgHome.setImageResource(R.drawable.home_blue);
    	 imgData.setImageResource(R.drawable.data_gray);
         imgInfo.setImageResource(R.drawable.info_gray);
         imgFriends.setImageResource(R.drawable.friend_gray);
	}else if (this.mContext instanceof UserInfo) {
		 imgData.setImageResource(R.drawable.data_blue);
	        
	        imgHome.setImageResource(R.drawable.home_gray);
	        imgInfo.setImageResource(R.drawable.info_gray);
	        imgFriends.setImageResource(R.drawable.friend_gray);
	}
}

private OnClickListener homeOnClickListener = new OnClickListener() {
	@Override
	public void onClick(View v) {
		
		
        getContext().startActivity(new Intent(getContext(), DashBoard.class));
        imgHome.setImageResource(R.drawable.home_blue);
        
        imgData.setImageResource(R.drawable.data_gray);
        imgInfo.setImageResource(R.drawable.info_gray);
        imgFriends.setImageResource(R.drawable.friend_gray);
        ((Activity) mContext).finish();
		
    }
};  
private OnClickListener myDataOnClickListener = new OnClickListener() {
	@Override
	public void onClick(View v) {
		
		
        getContext().startActivity(new Intent(getContext(), UserInfo.class));
        imgData.setImageResource(R.drawable.data_blue);

        imgHome.setImageResource(R.drawable.home_gray);
        imgInfo.setImageResource(R.drawable.info_gray);
        imgFriends.setImageResource(R.drawable.friend_gray);
        
			((Activity) mContext).finish();
		
		
    }
}; 
private OnClickListener friendsOnClickListener = new OnClickListener() {
	@Override
	public void onClick(View v) {
        /*getContext().startActivity(new Intent(getContext(), UserInfo.class));
        imgData.setImageResource(R.drawable.data_blue);*/
		Log.d("friends log", "this is friends button");
		//Toast.makeText(this.mContext, "This is inform my friends", Toast.LENGTH_SHORT).
		imgData.setImageResource(R.drawable.data_gray);
        
        imgHome.setImageResource(R.drawable.home_gray);
        imgInfo.setImageResource(R.drawable.info_gray);
        imgFriends.setImageResource(R.drawable.friend_blue);
        
    }
}; 
}
