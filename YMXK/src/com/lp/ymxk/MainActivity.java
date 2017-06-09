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
	private final static int SYSTEM_NOTIFICATION_BAR = 25;// 系统通知栏高度
	private int windowMaxHeight = 0;
	private int startY = 0;

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
				double a = (windowMaxHeight- iv.getHeight() - SYSTEM_NOTIFICATION_BAR +0.00) / (totalItemCount - visibleItemCount);
				if (flag) {
					ViewHelper.setTranslationY(iv, (float) (firstVisibleItem * a));
				}
			}
		});

		windowMaxHeight = getWindowManager().getDefaultDisplay().getHeight() - iv.getHeight() - SYSTEM_NOTIFICATION_BAR;
		iv.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				flag = false;
				ViewHelper.setAlpha(iv, 0.8f);
				handler.removeCallbacksAndMessages(null);
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						startY = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_MOVE:
						int newY = (int) event.getRawY();
						int offsetY = (int) (newY - startY);
	
						double d = ViewHelper.getTranslationY(iv) / windowMaxHeight;
						int current = (int) (list.size() * d);
	
						int temp = (int) (iv.getTranslationY() + offsetY);
						if (temp < 0) {
							temp = 0;
							current = 0;
						}
						if (temp > windowMaxHeight - iv.getHeight() - SYSTEM_NOTIFICATION_BAR) {
							temp = windowMaxHeight - iv.getHeight() - SYSTEM_NOTIFICATION_BAR;
							current = list.size();
						}
						ViewHelper.setTranslationY(iv, temp);
						System.out.println("总位置：" + windowMaxHeight);
						System.out.println("temp:" + temp);
						System.out.println("位置：" + ViewHelper.getTranslationY(iv));
	
						lv.setSelection(current);
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