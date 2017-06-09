package com.itheima74.quickindex;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itheima.quickindex.R;
import com.itheima74.quickindex.view.SwipeLayout;
import com.itheima74.quickindex.view.SwipeLayout.OnSwipeStateChangeListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class MyAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<Friend> list;
	
	public MyAdapter(Context context,ArrayList<Friend> list) {
		super();
		this.context = context;
		this.list = list;
	}

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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = View.inflate(context, R.layout.adapter_friend,null);
		}
		final ViewHolder holder = ViewHolder.getHolder(convertView);
		//设置数据
		Friend friend = list.get(position);
		holder.name.setText(friend.getName());
		
		String currentWord = friend.getPinyin().charAt(0)+"";
		if(position > 0){
			//获取上一个item的首字母
			String lastWord = list.get(position-1).getPinyin().charAt(0)+"";
			//拿当前的首字母和上一个首字母比较
			if(currentWord.equals(lastWord)){
				//说明首字母相同，需要隐藏当前item的first_word
				holder.first_word.setVisibility(View.GONE);
			} else {
				//不一样，需要显示当前的首字母
				//由于布局是复用的，所以在需要显示的时候，再次将first_word设置为可见
				holder.first_word.setVisibility(View.VISIBLE);
				holder.first_word.setText(currentWord);
			}
		}else {
			holder.first_word.setVisibility(View.VISIBLE);
			holder.first_word.setText(currentWord);
		}
		
		ViewHelper.setScaleX(convertView, 0.5f);
		ViewHelper.setScaleY(convertView, 0.5f);
		//以属性动画放大
		ViewPropertyAnimator.animate(convertView).scaleX(1).setDuration(350).start();
		ViewPropertyAnimator.animate(convertView).scaleY(1).setDuration(350).start();
		
		holder.tv_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				list.remove(position);
				MyAdapter.this.notifyDataSetChanged();		
				holder.swipeLayout.close();//把状态划回来
			}
		});
		
		holder.swipeLayout.setTag(position);
		holder.swipeLayout.setOnSwipeStateChangeListener(new OnSwipeStateChangeListener() {
			@Override
			public void onOpen(Object tag) {
				System.out.println("第"+(Integer)tag+"个打开");
			}
			@Override
			public void onClose(Object tag) {
				System.out.println("第"+(Integer)tag+"个关闭");
			}
		});
		return convertView;
	}

	static class ViewHolder{
		
		TextView name,first_word,tv_delete;
		SwipeLayout swipeLayout;

		public ViewHolder(View convertView){
			name = (TextView) convertView.findViewById(R.id.name);
			first_word = (TextView) convertView.findViewById(R.id.first_word);
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