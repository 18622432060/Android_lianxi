package com.lp.douyu.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.lp.douyu.R;

public class MainActivity extends Activity {

	private Button butChannel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 必须设置在setContentView前
		setContentView(R.layout.activity_main);

		butChannel = (Button) findViewById(R.id.but_channel);
		butChannel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),ChannelActivity.class);
				startActivity(intent);
			}
		});
	}
	
}