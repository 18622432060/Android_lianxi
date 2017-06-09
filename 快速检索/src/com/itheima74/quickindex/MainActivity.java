package com.itheima74.quickindex;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.quickindex.R;
import com.itheima74.quickindex.QuickIndexBar.OnTouchLetterListener;
import com.itheima74.quickindex.view.SwipeLayoutManager;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class MainActivity extends Activity {
	private QuickIndexBar quickIndexBar;
	private ListView listview;
	private TextView currentWord;
	
	private ArrayList<Friend> friends = new ArrayList<Friend>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		quickIndexBar = (QuickIndexBar) findViewById(R.id.quickIndexBar);
		listview = (ListView) findViewById(R.id.listview);
		currentWord = (TextView) findViewById(R.id.currentWord);
		//1.准备数据
		fillList();
		//2.对数据进行排序
		Collections.sort(friends);
		//3.设置Adapter
		listview.setAdapter(new MyAdapter(this,friends));
		
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
		
		quickIndexBar.setOnTouchLetterListener(new OnTouchLetterListener() {
			@Override
			public void onTouchLetter(String letter) {
				//根据当前触摸的字母，去集合中找那个item的首字母和letter一样，然后将对应的item放到屏幕顶端
				for (int i = 0; i < friends.size(); i++) {
					String firstWord = friends.get(i).getPinyin().charAt(0)+"";
					if(letter.equals(firstWord)){
						//说明找到了，那么应该讲当前的item放到屏幕顶端
						listview.setSelection(i);
						break;//只需要找到第一个就行
					}
				}
				
				//显示当前触摸的字母
				showCurrentWord(letter);
			}
		});
		
		
		//通过缩小currentWord来隐藏
		ViewHelper.setScaleX(currentWord, 0);
		ViewHelper.setScaleY(currentWord, 0);
		ViewHelper.setAlpha(currentWord, 0);
		
//		Log.e("tag", PinYinUtil.getPinyin("黑    马"));//HEIMA
//		Log.e("tag", PinYinUtil.getPinyin("#黑**马"));//#HEI**MA
//		Log.e("tag", PinYinUtil.getPinyin("O(∩_∩)O~黑。，马"));//HEIMA
	}
	private boolean isScale = false;
	private Handler handler = new Handler();
	protected void showCurrentWord(String letter) {
		currentWord.setText(letter);
		if(!isScale){
			isScale = true;
			ViewPropertyAnimator.animate(currentWord).scaleX(1f).alpha(1).setInterpolator(new OvershootInterpolator()).setDuration(450).start();
			ViewPropertyAnimator.animate(currentWord).scaleY(1f).alpha(1).setInterpolator(new OvershootInterpolator()).setDuration(450).start();
		}
		
		//先移除之前的任务
		handler.removeCallbacksAndMessages(null);
		
		//延时隐藏currentWord
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
//				currentWord.setVisibility(View.INVISIBLE);
				ViewPropertyAnimator.animate(currentWord).scaleX(0f).alpha(0).setDuration(450).start();
				ViewPropertyAnimator.animate(currentWord).scaleY(0f).alpha(0).setDuration(450).start();
				isScale = false;
			}
		}, 1500);
	}

	private void fillList() {
		// 虚拟数据
 	    String [] name =   new String [] { "宋江", "卢俊义", "吴用", "公孙胜", "关胜", "林冲", "秦明", "呼延灼", "花荣", "柴进", "李应", "朱仝", "鲁智深", "武松", "董平", "张清", "杨志", "徐宁", "索超", "戴宗", "刘唐", "李逵", "史进", "穆弘", "雷横", "李俊", "阮小二", "张横", "阮小五", " 张顺", "阮小七", "杨雄", "石秀", "解珍", " 解宝", "燕青", "朱武", "黄信", "孙立", "宣赞", "郝思文", "韩滔", "彭玘", "单廷珪", "魏定国", "萧让", "裴宣", "欧鹏", "邓飞", " 燕顺", "杨林", "凌振", "蒋敬", "吕方", "郭 盛", "安道全", "皇甫端", "王英", "扈三娘", "鲍旭", "樊瑞", "孔明", "孔亮", "项充", "李衮", "金大坚", "马麟", "童威", "童猛", "孟康", "侯健", "陈达", "杨春", "郑天寿", "陶宗旺", "宋清", "乐和", "龚旺", "丁得孙", "穆春", "曹正", "宋万", "杜迁", "薛永", "施恩",
 	    	"典韦","许褚","夏侯敦","夏侯渊","张辽","张郃","徐晃","于禁","乐进","曹仁","曹洪","李典","邓艾","钟会","羊祜","王双","文聘","文鸯","牛金","夏侯霸","夏侯威","郝昭","陈泰","庞德","杜预","李通","王基","郭淮","牵招","田豫","关羽","张飞","赵云","黄忠","马超","魏延","严颜","李严","王平","陈到","周仓","关平","廖化","马忠","张翼","张嶷","吴懿","高翔","马岱","关兴","张苞","张南","姜维","霍峻","霍弋","罗宪","宗预","糜芳","周瑜","陆逊","太史慈","甘宁","周泰","凌统","凌操","黄盖","程普","祖茂","韩当","吕蒙","丁奉","徐盛","陆抗","孙桓","朱桓","朱然","全琮","吕岱","贺齐","孙瑜","孙翊","孙韶","潘璋","董袭","陈武","蒋欣","文丑","颜良","高览","吕布","高顺","臧霸","张任","纪灵","华雄","胡轸","黄祖","郭汜","李傕","张燕"
 	    };
 	    for(String s:name ){
 			friends.add(new Friend(s.trim()));
 	    }
	}

}