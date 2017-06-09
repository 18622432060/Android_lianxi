package com.lp.blood.ui.view;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lp.blood.manager.ThreadManager;
import com.lp.blood.ui.view.ArcProgressView.AnimaListener;
import com.lp.blood.util.UIUtils;
import com.nineoldandroids.view.ViewHelper;

public class BloodView extends FrameLayout {

	private View btn_start;// 开始按钮
	private View btn_end;// 结束按钮
	private View pb;// 进度条
	private View fb;// 进度条
	private View prbg;// 进度条
	private View imageView;
	private TextView tv;
	private TextView tvBlood;
	private ArcProgressView apv;
	private float temp;// 进度条当前的位置

	public BloodView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BloodView(Context context) {
		super(context);
		init();
	}

	public BloodView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
	}

	/**
	 * 当DragLayout的xml布局的结束标签被读取完成会执行该方法，此时会知道自己有几个子View了 一般用来初始化子View的引用
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		btn_start = getChildAt(2);
		pb = getChildAt(0);
		fb = getChildAt(1);
		prbg = getChildAt(3);
		apv = (ArcProgressView) getChildAt(4);
		btn_end = getChildAt(5);
		imageView = getChildAt(6);
		tv = (TextView) getChildAt(7);
		tvBlood = (TextView) getChildAt(8);
	}

	private float height = 0;
	private float cancelLastHight = 0;
	private float heightMax = 0;
	private float i = 0;
	private Boolean flag = false;// 执行进度条的标致

	private int bloodBar = 5000;// 条所需
	private int bloodArc = 3000;// 环所需
	private int bloodUp = bloodBar + bloodArc;// 升级所需
	private int blood = 7100;// 可用

	final int STATE_NULL = 01;// 未运行
	final int STATE_ACTION_DOWN = 02;// 摁下
	final int STATE_ACTION_UP = 03;// 抬起
	public int STATE_CURRECT = STATE_NULL;// 当前运行状态

	final int STATE_BAR_NORUN = 01;// 未运行
	final int STATE_BAR_START_RUN = 02;// 开始运行中
	final int STATE_BAR_START_END = 03;// 开始运行结束
	final int STATE_BAR_BACK_RUN = 04;// 回退运行中
	final int STATE_BAR_BACK_END = 05;// 回退结束
	final int STATE_BAR_STOP = 06;// 开始运行中
	public int STATE_BAR_CURRECT = STATE_BAR_NORUN;// 当前运行状态

	float topfloat = 0f;
	float bottomfloat = 0f;

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		// int left = getPaddingLeft();
		int top = getPaddingTop();
		// int right = getPaddingRight();
		int bottom = getPaddingBottom();

		topfloat = getPaddingTop();
		bottomfloat = getPaddingBottom();

		int parentWidth = getMeasuredWidth();
		int parentHeight = getMeasuredHeight();

		int btnStartWidth = btn_start.getMeasuredWidth();
		// int btnStartHeight = btn_start.getMeasuredHeight();

		int pbEndWidth = pb.getMeasuredWidth();
		// int pbEndHeight = pb.getMeasuredHeight();

		int fbEndWidth = fb.getMeasuredWidth();
		// int fbEndHeight = fb.getMeasuredHeight();

		int btnEndWidth = btn_end.getMeasuredWidth();
		int btnEndHeight = btn_end.getMeasuredHeight();

		int prbgEndWidth = prbg.getMeasuredWidth();
		int prbgHeight = prbg.getMeasuredHeight();

		int imageViewWidth = imageView.getMeasuredWidth();
		int imageViewHeight = imageView.getMeasuredHeight();

		int tvWidth = tv.getMeasuredWidth();
		int tvHeight = tv.getMeasuredHeight();

		// int tvBloodWidth = tvBlood.getMeasuredWidth();
		int tvBloodHeight = tvBlood.getMeasuredHeight();

		// int apvWidth = apv.getMeasuredWidth();
		// int apvHeight = apv.getMeasuredHeight();

		btn_start.layout(parentWidth / 2 - btnStartWidth / 2, top, parentWidth / 2 - btnStartWidth / 2 + btn_start.getMeasuredWidth(), top + btn_start.getMeasuredHeight());
		pb.layout(parentWidth / 2 - pbEndWidth / 2, top + btn_start.getMeasuredHeight(), parentWidth / 2 - pbEndWidth / 2 + pb.getMeasuredWidth(), parentHeight - bottom - btnEndHeight);
		fb.layout(parentWidth / 2 - fbEndWidth / 2, top + btn_start.getMeasuredHeight() - (parentHeight - bottom), parentWidth / 2 - fbEndWidth / 2 + fb.getMeasuredWidth(), top + btn_start.getMeasuredHeight());
		btn_end.layout(parentWidth / 2 - prbgEndWidth / 2 + ((prbgEndWidth - btnEndWidth) / 2), parentHeight - bottom - prbgHeight + ((prbgHeight - btnEndHeight) / 2), parentWidth / 2 - prbgEndWidth / 2 + prbg.getMeasuredWidth() - ((prbgEndWidth - btnEndWidth) / 2), parentHeight - bottom - ((prbgHeight - btnEndHeight) / 2));
		prbg.layout(parentWidth / 2 - prbgEndWidth / 2, parentHeight - bottom - prbgHeight, parentWidth / 2 - prbgEndWidth / 2 + prbg.getMeasuredWidth(), parentHeight - bottom);

		imageView.layout(0, top + btn_start.getMeasuredHeight(), imageViewWidth, top + btn_start.getMeasuredHeight() + imageViewHeight);
		tv.layout(0, top + btn_start.getMeasuredHeight() + imageViewHeight, tvWidth, top + btn_start.getMeasuredHeight() + imageViewHeight + tvHeight);
		tvBlood.layout(0, top + btn_start.getMeasuredHeight() + imageViewHeight + tvHeight, tvWidth, top + btn_start.getMeasuredHeight() + imageViewHeight + tvHeight + tvBloodHeight);
		apv.layout(parentWidth / 2 - prbgEndWidth / 2, parentHeight - bottom - prbgHeight, parentWidth / 2 - prbgEndWidth / 2 + prbg.getMeasuredWidth(), parentHeight - bottom);

		height = ViewHelper.getTranslationY(fb);
		heightMax = parentHeight - btnStartWidth - btnEndHeight - top - bottom;

		btn_end.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (prbg.getVisibility() == View.GONE) {
					return false;
				}
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						STATE_CURRECT = STATE_ACTION_DOWN;
						ThreadManager.getThreadPool().cancel();
						// 1.什么都没有时
						if (height == ViewHelper.getTranslationY(fb)) {
							startBarAnima();
						}
						// 2.Bar正在回退时
						if (STATE_BAR_CURRECT == STATE_BAR_BACK_RUN) {
							startBarAnima();
						}
						// 3.Arc会退时
						if (apv.runState() == apv.STATE_BACK_RUN && heightMax - fb.getMeasuredHeight() < ViewHelper.getTranslationY(fb)&&STATE_BAR_CURRECT==STATE_BAR_START_END) {
							apv.setBlood(blood);
							apv.startAnima();
						}
						break;
					case MotionEvent.ACTION_MOVE:
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						STATE_CURRECT = STATE_ACTION_UP;
						flag = false;
						ThreadManager.getThreadPool().cancel();
						if (apv.runState() == apv.STATE_START_RUN || apv.runState() == apv.STATE_STOP) {// 开始动画运行中说明要回退
							apv.backAnima();
						} else {
							if (apv.runState() != apv.STATE_BACK_RUN && apv.runState() != apv.STATE_STOP) {
								barBack();
							} else {
								System.out.println("state:" + apv.runState());
							}
						}
						break;
					default:
						break;
				}
				return false;
			}
		});

		apv.setAnimaListener(new AnimaListener() {
			@Override
			public void currectProgress(final int progress) {
				UIUtils.runOnUIThread(new Runnable() {
					@Override
					public void run() {
						tv.setText("" + progress + "%");
						String bloodCurrent = blood - bloodUp * progress / 100 + "";
						if (blood - bloodUp * progress / 100 <= 0) {
							apv.stopAnima();
							bloodCurrent = "0";
						}
						tvBlood.setText(bloodCurrent);
						if (apv.runState() == apv.STATE_BACK_END) {
							if (prbg.getVisibility() != View.GONE) {
								barBack();
							}
						}
					}
				});
			}
		});
		tvBlood.setText(""+blood);
	}

	private synchronized void startBarAnima() {
		STATE_BAR_CURRECT = STATE_BAR_START_RUN;
		ThreadManager.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				flag = true;
				i = 0;
				while (flag) {
					SystemClock.sleep(50);
					i += 1;
					temp += i;
					if (heightMax - fb.getMeasuredHeight() < ViewHelper.getTranslationY(fb)) {
						STATE_BAR_CURRECT = STATE_BAR_START_END;
						apv.setBlood(blood);
						apv.startAnima();
						temp = (float) heightMax;
						flag = false;
						ThreadManager.getThreadPool().cancel(this);
						ThreadManager.getThreadPool().execute(new Runnable() {//
							@Override
							public void run() {
								while (true) {
									SystemClock.sleep(100);
									if (apv.runState() == apv.STATE_START_END) {// 结束
										UIUtils.runOnUIThread(new Runnable() {
											@Override
											public void run() {
												if (heightMax - fb.getMeasuredHeight() <= ViewHelper.getTranslationY(fb)) {
													prbg.setVisibility(View.GONE);
												}
											}
										});
										ThreadManager.getThreadPool().cancel(this);
									}
								}								
							}
						});
						ThreadManager.getThreadPool().cancel(this);

						
					}
					double d = temp / heightMax;
					final int num = (int) (d * 70);
					if (num >= 0 && num <= 70) {
						UIUtils.runOnUIThread(new Runnable() {
							@Override
							public void run() {
								tv.setText("" + num + "%");
							}
						});
					}
					if (blood - (bloodUp * num / 100) <= 0) {
						flag = false;
						tvBlood.setText("0");
						ThreadManager.getThreadPool().cancel(this);
					} else {
						UIUtils.runOnUIThread(new Runnable() {
							@Override
							public void run() {
								ViewHelper.setTranslationY(fb, temp);
								tvBlood.setText(blood - bloodUp * num / 100 + "");
							}
						});
					}
				}
			}
		});
	}

	private synchronized void barBack() {
		if (apv.runState() == apv.STATE_START_END || apv.runState() == apv.STATE_START_RUN) {
			apv.backAnima();
			return ;
		}
		STATE_BAR_CURRECT = STATE_BAR_BACK_RUN;
		ThreadManager.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				while (height < ViewHelper.getTranslationY(fb)) {
					SystemClock.sleep(100);
					cancelLastHight = ViewHelper.getTranslationY(fb);
					Boolean flagCancel = true;
					i = 0;
					while (flagCancel) {
						SystemClock.sleep(50);
						if (height >= ViewHelper.getTranslationY(fb) || STATE_BAR_CURRECT == STATE_BAR_START_RUN || STATE_CURRECT == STATE_ACTION_DOWN) {
							ThreadManager.getThreadPool().cancel(this);
							flagCancel = false;
							break;
						}
						// i+=0.5;
						// float temp = cancelLastHight-i*i;
						i += 1;
						cancelLastHight = temp;
						temp = cancelLastHight - i;
						if (height >= temp) {
							temp = height;
						}
						double d = temp / heightMax;
						final int num = (int) (d * 70);
						if (num >= 0 && num <= 70) {
							UIUtils.runOnUIThread(new Runnable() {
								@Override
								public void run() {
									tv.setText("" + num + "%");
									if (blood - bloodUp * num / 100 <= 0) {
										tvBlood.setText(0);
									}else{
										ViewHelper.setTranslationY(fb, temp);
										tvBlood.setText(blood - bloodUp * num / 100 + "");
									}
								}
							});
						}
					}
				}
			}
		});
	}

}