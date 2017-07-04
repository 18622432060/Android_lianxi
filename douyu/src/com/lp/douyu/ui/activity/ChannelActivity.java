package com.lp.douyu.ui.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;

import com.lp.douyu.R;
import com.lp.douyu.ui.fragment.PlayerFragment;

@SuppressWarnings("deprecation")
public class ChannelActivity extends FragmentActivity {

	private FrameLayout flPlayer;
	private FrameLayout flPlayerTemp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 必须设置在setContentView前
		setContentView(R.layout.channel_main);
		flPlayer = (FrameLayout) findViewById(R.id.fl_player);
		flPlayerTemp = (FrameLayout) findViewById(R.id.fl_player_temp);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fl_player, new PlayerFragment());
		transaction.commit();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		//获取status_bar_height资源的ID  
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");  
	    //根据资源ID获取响应的尺寸值  
		int systemNotificationBarHeight = getResources().getDimensionPixelSize(resourceId);  
	    int[] num = {0,0} ;
		flPlayerTemp.getLocationOnScreen(num);//700 460 100 0
		AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(flPlayerTemp.getMeasuredWidth(),flPlayerTemp.getLayoutParams().height,num[0],num[1]-systemNotificationBarHeight);
		flPlayer.setLayoutParams(params);
		super.onWindowFocusChanged(hasFocus);
	}

}