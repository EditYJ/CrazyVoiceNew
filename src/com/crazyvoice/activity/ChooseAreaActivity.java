package com.crazyvoice.activity;

import java.util.ArrayList;
import java.util.List;

import com.crazyvoice.app.R;
import com.crazyvoice.db.CrazyVoiceDB;
import com.crazyvoice.model.Category;
import com.crazyvoice.model.Channel;
import com.crazyvoice.util.HttpCallbackListener;
import com.crazyvoice.util.HttpUtil;
import com.crazyvoice.util.Utility;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	// 设定等级
	public static final int LEVEL_CATEGORY = 0;
	public static final int LEVEL_CHANNEL = 1;
	// 进程框
	private ProgressDialog progressDialog;
	// 界面
	private TextView titleview;
	private ListView listView;
	// 数据库
	private CrazyVoiceDB crazyVoiceDB;
	// Listview数组相关
	private ArrayAdapter<String> adapter;
	private List<String> dataList = new ArrayList<String>();
	// 频道分类列表
	private List<Category> categoryList;
	// 频道列表
	private List<Channel> channelList;
	// 选中的频道分类
	private Category selectCategory;
	// 选中的频道
	private Channel selectChannel;
	// 当前选中的级别
	private int currentLevel;
	// 配置接口key和接口地址
	public static final String KEY = "ae23e53fafb4207a70cf8bef18905c0e";
	public static final String URL = "http://japi.juhe.cn/tv/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleview = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		crazyVoiceDB = CrazyVoiceDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				// TODO Auto-generated method stub
				if (currentLevel == LEVEL_CATEGORY) {
					selectCategory = categoryList.get(index);
					queryChannel();
				} else if (currentLevel == LEVEL_CHANNEL) {
					selectChannel = channelList.get(index);
					Log.d("ChooseAreaActivity", "读取频道信息");
				}
			}

		});
		queryCategory();
	}

	/*
	 * 查询所有频道分类，优先从数据库查询，无则从服务器上查询。
	 */
	private void queryCategory() {
		// TODO Auto-generated method stub
		categoryList = crazyVoiceDB.loadCategory();
		if (categoryList.size() > 0) {
			dataList.clear();
			for (Category category : categoryList) {
				dataList.add(category.getCategoryName());
			} 
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleview.setText("选择频道分类");
		} else {
			queryFromServer(null, "category");
		}
		currentLevel=LEVEL_CATEGORY;
	}

	private void queryChannel() {
		// TODO Auto-generated method stub
		channelList = crazyVoiceDB.loadChannel(selectCategory.getId());
		if (channelList.size() > 0) {
			dataList.clear();
			for (Channel channel : channelList) {
				dataList.add(channel.getChannelName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleview.setText("选择频道");
		} else {
			queryFromServer(selectCategory.getCategoryCode(), "channel");
		}
		currentLevel=LEVEL_CHANNEL;
	}

	private void queryFromServer(final String code, final String type) {
		// TODO Auto-generated method stub
		String address = null;
		if ("category".equals(type)) {
			address = URL + "getCategory?key=" + KEY;
		} else if ("channel".equals(type)) {
			address = URL + "getChannel?pId=" + code + "&key=" + KEY;
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String reponse) {
				// TODO Auto-generated method stub
				boolean result = false;
				if ("category".equals(type)) {
					result = Utility.handleCategoryResponse(crazyVoiceDB,
							reponse);
				} else if ("channel".equals(type)) {
					result = Utility.handleChannelResponse(crazyVoiceDB,
							reponse);
				}
				if (result) {
					runOnUiThread(new Runnable() {
						public void run() {
							closeProgressDialog();
							if ("category".equals(type)) {
								queryCategory();
							} else if ("channel".equals(type)) {
								queryChannel();
							}
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败！！",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});

	}

	/*
	 * 显示进度对话框
	 */
	private void showProgressDialog() {
		// TODO Auto-generated method stub
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载... ...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/*
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		// TODO Auto-generated method stub
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	/*
	 * back键的逻辑编写
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (currentLevel == LEVEL_CHANNEL) {
			queryCategory();
		} else {
			finish();
		}
	}

}