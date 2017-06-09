package com.lp.blood.ui;

import android.app.Activity;
import android.os.Bundle;
import butterknife.ButterKnife;

import com.lp.blood.R;

/**
 * 血网天赋
 * 
 * @author Administrator
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		ButterKnife.inject(this);
	}
}