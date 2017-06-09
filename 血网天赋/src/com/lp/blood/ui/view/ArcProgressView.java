package com.lp.blood.ui.view;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import com.lp.blood.manager.ThreadManager;
import com.lp.blood.util.UIUtils;

public class ArcProgressView extends View {

	private Paint paint;
	
	private int speed = 30;//其实速度  越大越慢
	private int rateSpeed = 20;//加速率  越大越快
	
	public ArcProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ArcProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ArcProgressView(Context context) {
		super(context);
		init();
	}

	private void init() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);// 设置抗锯齿
		paint.setColor(getResources().getColor(R.color.holo_red_light));
	}

	private RectF rf = new RectF(0f, 0, 0, 0);
	private Arc larc = new Arc(-90, 0);
	private Arc rarc = new Arc(-90, 0);
	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int parentWidth = getMeasuredWidth();
		int parentHeight = getMeasuredHeight();
		rf = new RectF(0, 0 , parentWidth,parentHeight);
		// 让整体画布往上偏移
//		canvas.translate(0, -Utils.getStatusBarHeight(getResources()));
		paint.setStyle(Style.FILL_AND_STROKE);
		canvas.drawArc(rf, larc.begin, larc.end, true, paint);
		canvas.drawArc(rf, rarc.begin, rarc.end, true, paint);
	}

	private boolean arcStratIsRun = true ;
	private boolean arcbackIsRun = true ;
	private int blood = 0;
	
	public void setBlood(int blood){
		this.blood = blood;
	}
	
	public int getTempBlood(){
		double temp  = (double) (blood - 5000) / 3000;
		return (int) (temp * 180);
	}

	public synchronized void startAnima() {
		arcStratIsRun = true;
		arcbackIsRun = false;
		STATE_CURRECT = STATE_START_RUN;
		ThreadManager.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				int i=0;
				while (true) {
					sleepTime(i);
					if (arcIsRun() && STATE_CURRECT == STATE_START_RUN) {
						double d = larc.end / 180d;
						int num = (int) (d * 30);
						if (num >= 0 && num <= 30) {
							animaListener.currectProgress(70 + num);
						}
						if(larc.end + 1>= getTempBlood()||rarc.end - 1 <= -getTempBlood()){
							stopAnima();
							STATE_CURRECT = STATE_STOP;
							ThreadManager.getThreadPool().cancel(this);
							break;
						}
						if (larc.end + 1 > 180 || rarc.end - 1 < -180) {
							ThreadManager.getThreadPool().cancel(this);
							STATE_CURRECT = STATE_START_END;
							break;
						}
						larc.end = larc.end + 1;
						rarc.end = rarc.end - 1;
						UIUtils.runOnUIThread(new Runnable() {
							@Override
							public void run() {
								invalidate();
							}
						});
					} else {
						ThreadManager.getThreadPool().cancel(this);
					}
				}
			}
		});
		
	}
	
	private Boolean arcIsRun(){
		return arcStratIsRun;
	}
	
	private Boolean arcbackIsRun(){
		return arcbackIsRun;
	}
	
	public synchronized void stopAnima(){
		arcStratIsRun = false;
	}
	
	public synchronized void backAnima(){
		STATE_CURRECT = STATE_BACK_RUN;
		stopAnima();
		arcbackIsRun = true;
		ThreadManager.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				int i = 0;
				while (true) {
					i = sleepTime(i);
					if (arcbackIsRun() && STATE_CURRECT == STATE_BACK_RUN) {
						double d = larc.end / 180d;
						int num = (int) (d * 100);
						if (num >= 0 && num <= 100) {
							animaListener.currectProgress(num);
						}
						larc.end = larc.end - 1;
						rarc.end = rarc.end + 1;
						if (larc.end - 1 <= 0 || rarc.end + 1 >= 0) {
							ThreadManager.getThreadPool().cancel(this);
							STATE_CURRECT = STATE_BACK_END;
							larc.end = 0;
							rarc.end = 0;
							break;
						}
						larc.end = larc.end - 0.2f;
						rarc.end = rarc.end + 0.2f;
						UIUtils.runOnUIThread(new Runnable() {
							@Override
							public void run() {
								invalidate();
							}
						});
					} else {
						ThreadManager.getThreadPool().cancel(this);
					}
				}
			}

			
		});
	}
	
	private int sleepTime(int i) {
		int time = speed - i < 0 ? 5 : speed - i;
		SystemClock.sleep(time);
		i+=rateSpeed;
		return i;
	}
	
	final int STATE_NORUN = 01;// 未运行
	final int STATE_START_RUN = 02;// 开始运行中
	final int STATE_START_END = 03;// 开始运行结束
	final int STATE_BACK_RUN = 04;// 回退运行中
	final int STATE_BACK_END = 05;// 回退结束
	final int STATE_STOP = 06;// 未运行
	public int STATE_CURRECT = STATE_NORUN;// 当前运行状态

	public int runState(){
		return STATE_CURRECT;
	}
	
	private AnimaListener animaListener;
	
	public void setAnimaListener(AnimaListener animaListener){
		this.animaListener = animaListener;
	}
	
	interface AnimaListener{
		
		 void currectProgress(int progress);
	}

	class Arc {

		public Arc(float begin, float end) {
			this.begin = begin;
			this.end = end;
		}

		float begin;
		float end;

	}

}