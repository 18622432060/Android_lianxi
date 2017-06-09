package com.itheima74.qqslidemenu.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.itheima74.qqslidemenu.R;

/**
 * 
 * 关键类和方法的介绍
 * @author Administrator
 *
 */
public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
