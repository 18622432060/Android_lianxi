package com.lp.riddle;

import java.util.ArrayList;

import android.R.color;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lp.riddle.bean.Light;
@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements OnClickListener {

	static private int ROW_NULL = 4;
	static private int WINDOW_WIDTH;

	private GridView gv;
	private TextView tv;
	private EditText et;
	private Button bt;

	private Button btFour;
	private Button btSix;
	private Button btEight;
	private Button btTen;

	private boolean flag;
	private int currect;
	private int max;

	private ArrayList<Light> list = null;

	private ArrayList<Light> listTemp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gv = (GridView) findViewById(R.id.gv);
		tv = (TextView) findViewById(R.id.tv);
		et = (EditText) findViewById(R.id.et);
		bt = (Button) findViewById(R.id.bt);

		btFour = (Button) findViewById(R.id.bt_four);
		btSix = (Button) findViewById(R.id.bt_six);
		btEight = (Button) findViewById(R.id.bt_eight);
		btTen = (Button) findViewById(R.id.bt_ten);
		
		btFour.setOnClickListener(this);
		btSix.setOnClickListener(this);
		btEight.setOnClickListener(this);
		btTen.setOnClickListener(this);

		bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (et.getText() != null && !"".equals(et.getText()) && Integer.valueOf(et.getText().toString()) > 0) {
					flag = true;
					currect = 0;
					max = Integer.valueOf(et.getText().toString());
					listTemp = list;
					tv.setText("当前步数：0 /" + max);
				} else {
					Toast.makeText(getApplicationContext(), "请输入正确的数值", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		ROW_NULL = gv.getNumColumns();
		list = new ArrayList<Light>();
		Light light = null;
		for (int i = 0; i < ROW_NULL * ROW_NULL; i++) {
			light = new Light();
			list.add(light);
		}
		gv.setAdapter(new MyAdapter());
		WINDOW_WIDTH = getWindowManager().getDefaultDisplay().getWidth();
		super.onWindowFocusChanged(hasFocus);
	}

	private class MyAdapter extends BaseAdapter {

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.grid_item_light, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (list.get(position).toggle) {
				holder.bt.setImageResource(color.white);
			} else {
				holder.bt.setImageResource(color.holo_orange_light);
			}
			holder.bt.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// 1.自己变
					list.get(position).toggle = !list.get(position).toggle;

					// 2.周围的变
					gv.invalidateViews();

					// (1)上方
					int top = position - ROW_NULL;
					if (top >= 0) {
						list.get(top).toggle = !list.get(top).toggle;
					}
					// (2)下方
					int bot = position + ROW_NULL;
					if (bot < ROW_NULL * ROW_NULL && bot < ROW_NULL * ROW_NULL) {
						list.get(bot).toggle = !list.get(bot).toggle;
					}
					// (3)左方
					int left = position - 1;
					if (left % ROW_NULL != ROW_NULL - 1 && left >= 0) {
						list.get(left).toggle = !list.get(left).toggle;
					}
					// (4)右方
					int right = position + 1;
					if (right % ROW_NULL != 0 && right < ROW_NULL * ROW_NULL) {
						list.get(right).toggle = !list.get(right).toggle;
					}
					gv.invalidateViews();
					if (flag) {
						currect++;
						if (currect == max) {
							Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
							list = listTemp;
							flag = !flag;
							tv.setText("当前步数：0 /" + max);
						} else {
							tv.setText("当前步数：" + currect + "/" + max);
							// 判断成功
							Boolean temp = true;
							for (Light l : list) {
								temp = temp && l.toggle;
							}

							if (temp) {
								flag = !flag;
								Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
							}

						}
					}

				}
			});
			return convertView;
		}
	}

	static class ViewHolder {

		private ImageButton bt;

		public ViewHolder(View view) {
			bt = (ImageButton) view.findViewById(R.id.bt);
			int j = WINDOW_WIDTH / ROW_NULL;

			bt.getLayoutParams().height = j;
			bt.getLayoutParams().width = j;
			bt.requestLayout();
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_four:
			ROW_NULL = 4;
			break;
		case R.id.bt_six:
			ROW_NULL = 6;
			break;
		case R.id.bt_eight:
			ROW_NULL = 8;
			break;
		case R.id.bt_ten:
			ROW_NULL = 10;
			break;
		default:
			break;
		}
		list = new ArrayList<Light>();
		Light light = null;
		for (int i = 0; i < ROW_NULL * ROW_NULL; i++) {
			light = new Light();
			list.add(light);
		}
		gv.setNumColumns(ROW_NULL);
		gv.setAdapter(new MyAdapter());
		gv.invalidateViews();
	}

}
