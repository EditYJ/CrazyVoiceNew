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
	// �趨�ȼ�
	public static final int LEVEL_CATEGORY = 0;
	public static final int LEVEL_CHANNEL = 1;
	// ���̿�
	private ProgressDialog progressDialog;
	// ����
	private TextView titleview;
	private ListView listView;
	// ���ݿ�
	private CrazyVoiceDB crazyVoiceDB;
	// Listview�������
	private ArrayAdapter<String> adapter;
	private List<String> dataList = new ArrayList<String>();
	// Ƶ�������б�
	private List<Category> categoryList;
	// Ƶ���б�
	private List<Channel> channelList;
	// ѡ�е�Ƶ������
	private Category selectCategory;
	// ѡ�е�Ƶ��
	private Channel selectChannel;
	// ��ǰѡ�еļ���
	private int currentLevel;
	// ���ýӿ�key�ͽӿڵ�ַ
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
					Log.d("ChooseAreaActivity", "��ȡƵ����Ϣ");
				}
			}

		});
		queryCategory();
	}

	/*
	 * ��ѯ����Ƶ�����࣬���ȴ����ݿ��ѯ������ӷ������ϲ�ѯ��
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
			titleview.setText("ѡ��Ƶ������");
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
			titleview.setText("ѡ��Ƶ��");
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
						Toast.makeText(ChooseAreaActivity.this, "����ʧ�ܣ���",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});

	}

	/*
	 * ��ʾ���ȶԻ���
	 */
	private void showProgressDialog() {
		// TODO Auto-generated method stub
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���... ...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/*
	 * �رս��ȶԻ���
	 */
	private void closeProgressDialog() {
		// TODO Auto-generated method stub
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	/*
	 * back�����߼���д
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