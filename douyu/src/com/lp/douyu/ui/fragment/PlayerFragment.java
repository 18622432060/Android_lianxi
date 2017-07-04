package com.lp.douyu.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lp.douyu.R;
import com.nineoldandroids.view.ViewPropertyAnimator;

@SuppressWarnings("deprecation")
public class PlayerFragment extends Fragment {
	
	private static final int offsetX = 190 ;//X轴偏移量根据 屏幕分辨率手动调整
	private static final int offsetY = 105 ;// Y轴偏移量根据 屏幕分辨率手动调整
	private static final int animaTime = 400;//动画时间毫秒

	private static int showCurrent = 04;// 当前显示状态
	private static final int SHOW_EMPTY = 01;// 屏幕什么都没有
	private static final int SHOW_TOPANDBOTTOM = 02;// 只显示上下框
	private static final int SHOW_LV_RIGHT = 03;// 显示右边ListView
	private static final int SHOW_SMALL_VIEW = 04;// 显示小视图

	private View flTop;
	private FrameLayout flBottom;
	private RelativeLayout rlMain;
	private ListView lvRight;
	private Button btnChannel;
	private Button btnZoom;

	List<String> list = new ArrayList<String>();
	long lastTime = 0;
	Timer timer = null;
	private FrameLayout flPlayer;
	private FrameLayout flPlayerTemp;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.channel_player_main, null);

		flTop = (View) view.findViewById(R.id.fl_top);
		flBottom = (FrameLayout) view.findViewById(R.id.fl_bottom);
		rlMain = (RelativeLayout) view.findViewById(R.id.rl_main);
		lvRight = (ListView) view.findViewById(R.id.lv_right);
		btnChannel = (Button) view.findViewById(R.id.btn_channel);
		btnZoom = (Button) view.findViewById(R.id.btn_zoom);

		for (int i = 0; i < 20; i++) {
			list.add("频道" + i);
		}
		lvRight.setAdapter(new MyAdapter());
		flPlayer = (FrameLayout) getActivity().findViewById(R.id.fl_player);
		flPlayerTemp = (FrameLayout) getActivity().findViewById(R.id.fl_player_temp);

		btnZoom.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if ("+".equals(btnZoom.getText() + "")) {
					double width = getActivity().getWindowManager().getDefaultDisplay().getWidth() + 0.000;
					double height = getActivity().getWindowManager().getDefaultDisplay().getHeight() + 0.000;
					float x = (float) (width / flPlayer.getMeasuredWidth());
					float y = (float) (height / flPlayer.getMeasuredHeight() - 0.1);
					ViewPropertyAnimator.animate(flPlayer).scaleX(x).scaleY(y).translationXBy(offsetX).translationYBy(offsetY).setDuration(300).start();
					btnZoom.setText("-");
					btnZoom.setVisibility(View.GONE);
					showCurrent = SHOW_TOPANDBOTTOM;
				} else {
					float x = (float) ((flPlayerTemp.getMeasuredWidth() + 0.000) / flPlayer.getMeasuredWidth());
					float y = (float) ((flPlayerTemp.getMeasuredHeight() + 0.000) / flPlayer.getMeasuredHeight());
					ViewPropertyAnimator.animate(flPlayer).scaleX(x).scaleY(y).translationXBy(-offsetX).translationYBy(-offsetY).setDuration(300).start();
					btnZoom.setText("+");
					btnZoom.setVisibility(View.GONE);
					showCurrent = SHOW_SMALL_VIEW;
				}
				timer = new Timer();
				timer.schedule(new TimerTask() {
					public void run() {
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								btnZoom.setVisibility(View.VISIBLE);
							}
						});
						timer.cancel();
					}
				}, 300);
			}
		});

		rlMain.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (showCurrent == SHOW_SMALL_VIEW) {

				} else {
					switch (showCurrent) {
						case SHOW_EMPTY:
							showCurrent = SHOW_TOPANDBOTTOM;
							ViewPropertyAnimator.animate(flTop).translationY(-flTop.getHeight()).setDuration(animaTime).start();
							ViewPropertyAnimator.animate(flBottom).translationY(flBottom.getHeight()).setDuration(animaTime).start();
							break;
						case SHOW_TOPANDBOTTOM:
							showCurrent = SHOW_EMPTY;
							ViewPropertyAnimator.animate(flTop).translationY(0).setDuration(animaTime).start();
							ViewPropertyAnimator.animate(flBottom).translationY(0).setDuration(animaTime).start();
							break;
						case SHOW_LV_RIGHT:
							showCurrent = SHOW_TOPANDBOTTOM;
							ViewPropertyAnimator.animate(flTop).translationY(0).setDuration(animaTime).start();
							ViewPropertyAnimator.animate(flBottom).translationY(0).setDuration(animaTime).start();
							ViewPropertyAnimator.animate(lvRight).translationX(lvRight.getWidth()).setDuration(300).start();
							break;
						default:
							break;
					}
					lastTime = System.currentTimeMillis();
				}
				return false;
			}
		});
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (showCurrent != SHOW_SMALL_VIEW) {
						SystemClock.sleep(1000);
						if (System.currentTimeMillis() - lastTime > 1500 && getActivity() != null) {
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									if (showCurrent == SHOW_TOPANDBOTTOM) {
										showCurrent = SHOW_EMPTY;
										ViewPropertyAnimator.animate(flTop).translationY(-flTop.getHeight()).setDuration(animaTime).start();
										ViewPropertyAnimator.animate(flBottom).translationY(flBottom.getHeight()).setDuration(animaTime).start();
									}
								}
							});
						}
					}
				}
			}
		}).start();

		btnChannel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (showCurrent != SHOW_SMALL_VIEW) {
					showCurrent = SHOW_LV_RIGHT;// 点击频道按钮后只显示右边频道列表
					lvRight.setVisibility(View.VISIBLE);
					ViewPropertyAnimator.animate(lvRight).translationX(0).setDuration(300).start();
					ViewPropertyAnimator.animate(flTop).translationY(-flTop.getHeight()).setDuration(animaTime).start();
					ViewPropertyAnimator.animate(flBottom).translationY(flBottom.getHeight()).setDuration(animaTime).start();
				}
			}
		});
		return view;
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
			TextView tv = new TextView(getActivity().getApplicationContext());
			tv.setHeight(100);
			tv.setText("" + list.get(position));
			return tv;
		}

	}

}