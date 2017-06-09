package com.itheima.swipedelete;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.swipedelete.R;
import com.itheima.swipedelete.SwipeLayout.OnSwipeStateChangeListener;

public class MainActivity extends Activity {
	
	private ListView listview;
	private ArrayList<String> list = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listview = (ListView) findViewById(R.id.listview);
		//1.准备数据
		for (int i = 0; i < 30; i++) {
			list.add("name - "+i);
		}
		listview.setAdapter(new MyAdapter());
		listview.setOnScrollListener(new OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState==OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
					//如果垂直滑动，则需要关闭已经打开的layout
					SwipeLayoutManager.getInstance().closeCurrentLayout();
				}
			}
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class MyAdapter extends BaseAdapter implements OnSwipeStateChangeListener{
		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView=View.inflate(MainActivity.this, R.layout.adapter_list, null);
			}
			ViewHolder holder = ViewHolder.getHolder(convertView);
			
			holder.tv_name.setText(list.get(position));
			
			holder.swipeLayout.setTag(position);
			holder.swipeLayout.setOnSwipeStateChangeListener(this);
			
			return convertView;
		}
		@Override
		public void onOpen(Object tag) {
			Toast.makeText(MainActivity.this,"第"+(Integer)tag+"个打开", Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onClose(Object tag) {
			Toast.makeText(MainActivity.this,"第"+(Integer)tag+"个关闭", Toast.LENGTH_SHORT).show();
		}
	}
	
	static class ViewHolder{
		TextView tv_name,tv_delete;
		SwipeLayout swipeLayout;
		public ViewHolder(View convertView){
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
			swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipeLayout);
		}
		public static ViewHolder getHolder(View convertView){
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if(holder==null){
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}
}