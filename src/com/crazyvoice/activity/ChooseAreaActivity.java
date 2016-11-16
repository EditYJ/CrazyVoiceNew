package com.crazyvoice.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;

import com.crazyvoice.app.R;
import com.crazyvoice.db.CrazyVoiceDB;
import com.crazyvoice.model.Category;
import com.crazyvoice.model.Channel;
import com.crazyvoice.util.ClientConServer;
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
	//������Ϣ�洢���
	private HashMap<String, String> chatRoomMap = new HashMap<String, String>();
	private List<String> roomsList = new ArrayList<String>();
	private XMPPConnection connection=ClientConServer.connection;
	private MultiUserChat chat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SmackAndroid.init(ChooseAreaActivity.this);// ��ʼ��Asmackƽ̨,����ģ�����ᱨ��
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
					Log.d("ChooseAreaActivity", selectChannel.getChannelName());
					String roomName = DeleteBlank(selectChannel.getChannelName());
					//��ѯ�����Ƿ񷿼��Ѿ������������淿����Ϣ.����������򴴽����䡣
					if(!queryRoom(roomName)){
						creatRoom(roomName);
						Log.d("ChooseAreaActivity", selectChannel.getChannelName()+"�����ɹ�����");
					}else{
						try {
							chat.join("yj����");
							Log.d("ChooseAreaActivity", "yj����ɹ�����");
						} catch (XMPPException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
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
	/*
	 * ��ѯƵ��������Ƶ�������ȴ����ݿ��ѯ������ӷ������ϲ�ѯ��
	 */
	private void queryChannel() {
		// TODO Auto-generated method stub
		channelList = crazyVoiceDB.loadChannel(selectCategory.getId());
		if (channelList.size() > 0) {
			dataList.clear();
			showProgressDialog();
			for (Channel channel : channelList) {
				//����һ����Щ����������¿����ڵ����ʱ��ִ�У�������ȡƵ���б���ӳپͻ��󽵵͡�
//				String roomName = DeleteBlank(channel.getChannelName());
				//��ѯ�����Ƿ񷿼��Ѿ������������淿����Ϣ.����������򴴽����䡣
//				if(!queryRoom(roomName)){
//					creatRoom(roomName);
//				}
				dataList.add(channel.getChannelName());
			}
			closeProgressDialog();
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleview.setText("ѡ��Ƶ��");
		} else {
			queryFromServer(selectCategory.getCategoryCode(), "channel");
		}
		currentLevel=LEVEL_CHANNEL;
	}
	
	/**
	 * ����Ƶ�����ƴ������췿��
	 * @param channelName
	 */
	private void creatRoom(String channelName) {
		// TODO Auto-generated method stub
		try {
			chat = new MultiUserChat(connection,
					channelName+"@conference.gswtek-022");
			chat.create(connection.getUser());
			// ��ȡ���������ñ�
			Form form = chat.getConfigurationForm();
			// ����ԭʼ������һ��Ҫ�ύ���±�
			Form submitForm = form.createAnswerForm();
			// /////////////////////////////////////////////////////////////////////////////////////
			// ���ύ�ı����Ĭ�ϴ�
			for (Iterator<FormField> fields = form.getFields(); fields.hasNext();) {
				FormField field = (FormField) fields.next();
				if (!FormField.TYPE_HIDDEN.equals(field.getType())&& field.getVariable() != null) {
					// ����Ĭ��ֵ��Ϊ��
					submitForm.setDefaultAnswer(field.getVariable());
				}
			}
			// ////////////////////////////////////////////////////////////////////////////////////
			submitForm.setAnswer("muc#roomconfig_roomdesc", channelName);//�޸ķ�������
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);//�����ǳ־õ�
			submitForm.setAnswer("muc#roomconfig_enablelogging", true);//������ע����ǳƵ�¼
			// ��������ɵı�������������������
			chat.sendConfigurationForm(submitForm);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ��ѯ�����Ƿ񷿼��Ѿ������������淿����Ϣ
	 * @param channelName
	 */
	private boolean queryRoom(String channelName) {
		// TODO Auto-generated method stub
		try {
			Collection<HostedRoom> rooms=MultiUserChat.getHostedRooms(
					connection, "conference."+connection.getServiceName());
			if (rooms != null && !rooms.isEmpty()) {
				for (HostedRoom entry : rooms) {
					RoomInfo info = MultiUserChat.getRoomInfo(connection,entry.getJid());
					roomsList.add(info.getDescription());
					chatRoomMap.put(info.getRoom(), entry.getJid());
				}
				//�жϷ������Ƿ��Ѿ�����
				if(roomsList.contains(channelName)){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * �����ݿ��ѯƵ�������Ƶ��
	 * @param code
	 * @param type
	 */
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
	public String DeleteBlank(String name) {
		return name.replace(" ", "");
	}

}