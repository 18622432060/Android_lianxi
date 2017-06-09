package com.lp.ymxk;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
public class MainActivity extends Activity {
	private int windowMaxHeight = 0;
	private int startY = 0;
	private int startX = 0;

	List list = new ArrayList<String>();
	private ListView lv;
	private ImageView iv;

	boolean flag = false;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 必须设置在setContentView前
		setContentView(R.layout.activity_main);
		lv = (ListView) findViewById(R.id.lv);
		iv = (ImageView) findViewById(R.id.iv);
		ViewHelper.setAlpha(iv, 0.2f);
		for (int i = 0; i < 150; i++) {
			list.add("行" + i);
		}
		lv.setAdapter(new MyAdapter());
		lv.setVerticalScrollBarEnabled(false);
		lv.setOnScrollListener(new OnScrollListener() {
			// scrollState : 滑动状态
			// SCROLL_STATE_IDLE : 空闲的状态
			// SCROLL_STATE_TOUCH_SCROLL : 缓慢滑动的状态
			// SCROLL_STATE_FLING : 快速滑动
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				ViewHelper.setAlpha(iv, 0.8f);
				if (scrollState == OnScrollListener.SCROLL_STATE_FLING || scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
					flag = true;
					handler.removeCallbacksAndMessages(null);
					handler.postDelayed(new Runnable() {
						public void run() {
							ViewPropertyAnimator.animate(iv).alpha(0.2f).setDuration(450).start();
						}
					}, 1500);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				double a = (windowMaxHeight +0.00) / (totalItemCount - visibleItemCount);
				if (flag) {
					ViewHelper.setTranslationY(iv, (float) (firstVisibleItem * a));
				}
			}
		});
		iv.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				flag = false;
				ViewHelper.setAlpha(iv, 0.8f);
				handler.removeCallbacksAndMessages(null);
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						startX = (int) event.getRawX();
						startY = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_MOVE:
						int newX = (int) event.getRawX();
						int newY = (int) event.getRawY();
						int offsetX = (int) (newX - startX);
						int offsetY = (int) (newY - startY);

						double d = ViewHelper.getTranslationY(iv) / windowMaxHeight;
						int current = (int) (list.size() * d);
						
						int tempX = (int) (iv.getTranslationX() + offsetX);
						int tempY = (int) (iv.getTranslationY() + offsetY);
						if (tempX < 0) {
							tempX = 0;
						}
						
						if (tempY < 0) {
							tempY = 0;
							current = 0;
						}
						if (tempX > lv.getMeasuredWidth()- iv.getWidth()) {
							tempX = lv.getMeasuredWidth()- iv.getWidth() ;
						}
						
						if (tempY > windowMaxHeight ) {
							tempY = windowMaxHeight  ;
							current = list.size();
						}
						ViewHelper.setTranslationX(iv, tempX);
						ViewHelper.setTranslationY(iv, tempY);
						System.out.println("总位置：" + windowMaxHeight);
						System.out.println("temp:" + tempY);
						System.out.println("位置：" + ViewHelper.getTranslationY(iv));
						lv.setSelection(current);
						startX = newX;
						startY = newY;
						break;
					case MotionEvent.ACTION_UP:
						flag = true;
						handler.postDelayed(new Runnable() {
							public void run() {
								ViewPropertyAnimator.animate(iv).alpha(0.2f).setDuration(450).start();
							}
						}, 1500);
						break;
					default:
						break;
				}
				return true;
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		//获取status_bar_height资源的ID  
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");  
	    //根据资源ID获取响应的尺寸值  
		int systemNotificationBarHeight = getResources().getDimensionPixelSize(resourceId);  
		if(true){
			ViewHelper.setTranslationX(iv, getWindowManager().getDefaultDisplay().getWidth() - iv.getWidth());
			windowMaxHeight = getWindowManager().getDefaultDisplay().getHeight() - iv.getHeight() -systemNotificationBarHeight;
		}
		super.onWindowFocusChanged(hasFocus);
	}
	
	class MyAdapter extends BaseAdapter {

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(getApplicationContext());
			tv.setHeight(100);
			tv.setText("" + list.get(position));
			return tv;
		}

	}

}