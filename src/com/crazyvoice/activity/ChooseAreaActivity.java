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
import com.crazyvoice.model.Program;
import com.crazyvoice.util.ClientConServer;
import com.crazyvoice.util.HttpCallbackListener;
import com.crazyvoice.util.HttpUtil;
import com.crazyvoice.util.Utility;

import android.R.string;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	// private TextView titleview;
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
	// 房间信息存储相关
	private HashMap<String, String> chatRoomMap = new HashMap<String, String>();
	private List<String> roomsList = new ArrayList<String>();
	private XMPPConnection connection = ClientConServer.connection;
	private MultiUserChat chat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SmackAndroid.init(ChooseAreaActivity.this);// 初始化Asmack平台,必须的，否则会报错
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);// 左右滑动效果
		setBar();
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		// titleview = (TextView) findViewById(R.id.title_text);
		// 自己做的标题栏，准备启用action bar制作标题菜单栏
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
					String roomName = DeleteBlank(selectChannel
							.getChannelName());
					// 查询房间是否房间已经创建，并保存房间信息.如果不存在则创建房间。
					if (!queryRoom(roomName)) {
						creatRoom(roomName);
						Log.d("ChooseAreaActivity",
								selectChannel.getChannelName() + "创建成功！！");
					}
					// else{
					// try {
					// chat = new MultiUserChat(connection,
					// roomName+"@conference.gswtek-022");
					// chat.join("yj加入");
					// Log.d("ChooseAreaActivity", "yj加入成功！！");
					// chat.sendMessage("我来啦！！！");
					// } catch (XMPPException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// }
					Intent intent = new Intent(ChooseAreaActivity.this,
							ChatRoomActivity.class);
					intent.putExtra("roomName", roomName);
					intent.putExtra("channelcode", selectChannel.getChannelCode());
					startActivity(intent);
				}
			}
		});
		queryCategory();
	}

	/**
	 * 设置标题菜单栏
	 */
	private void setBar() {
		// TODO Auto-generated method stub
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	// 设置菜单栏按钮事件
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			if (currentLevel == LEVEL_CHANNEL) {
				queryCategory();
			} else {
				new AlertDialog.Builder(ChooseAreaActivity.this)
				// 设置对话框标题
				.setTitle("系统提示")
				// 设置显示的内容						
				.setMessage("是否注销账号？")
				// 添加确定按钮
				.setPositiveButton("确定",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {// 确定按钮的响应事件
						// TODO Auto-generated method stub
						connection.disconnect();
						finish();
					}
				})
				.setNegativeButton("返回",new DialogInterface.OnClickListener() {// 添加返回按钮
					@Override
					public void onClick(DialogInterface dialog,int which) {// 响应事件
						// TODO Auto-generated method stub
						Log.i("alertdialog", " 请保存数据！");
					}
				}).show();// 在按键响应事件中显示此对话框
			}
		}
		return true;
	}

	/**
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
			setTitle("选择频道分类");
			// titleview.setText("选择频道分类");
		} else {
			queryFromServer(null, "category");
		}
		currentLevel = LEVEL_CATEGORY;
	}

	/*
	 * 查询频道分类下频道，优先从数据库查询，无则从服务器上查询。
	 */
	private void queryChannel() {
		// TODO Auto-generated method stub
		channelList = crazyVoiceDB.loadChannel(selectCategory.getId());
		if (channelList.size() > 0) {
			dataList.clear();
			showProgressDialog();
			for (Channel channel : channelList) {
				// 想了一下这些创建房间的事可以在点击的时候执行，这样获取频道列表的延迟就会大大降低。
				// String roomName = DeleteBlank(channel.getChannelName());
				// 查询房间是否房间已经创建，并保存房间信息.如果不存在则创建房间。
				// if(!queryRoom(roomName)){
				// creatRoom(roomName);
				// }
				dataList.add(channel.getChannelName());
			}
			closeProgressDialog();
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			setTitle("选择频道");
			// titleview.setText("选择频道");
		} else {
			queryFromServer(selectCategory.getCategoryCode(), "channel");
		}
		currentLevel = LEVEL_CHANNEL;
	}

	/**
	 * 根据频道名称创建聊天房间
	 * 
	 * @param channelName
	 */
	private void creatRoom(String channelName) {
		// TODO Auto-generated method stub
		try {
			chat = new MultiUserChat(connection, channelName
					+ "@conference.gswtek-022");
			chat.create(connection.getUser());
			// 获取聊天室配置表单
			Form form = chat.getConfigurationForm();
			// 根据原始表单创建一个要提交的新表单
			Form submitForm = form.createAnswerForm();
			// /////////////////////////////////////////////////////////////////////////////////////
			// 向提交的表单添加默认答复
			for (Iterator<FormField> fields = form.getFields(); fields
					.hasNext();) {
				FormField field = (FormField) fields.next();
				if (!FormField.TYPE_HIDDEN.equals(field.getType())
						&& field.getVariable() != null) {
					// 设置默认值作为答复
					submitForm.setDefaultAnswer(field.getVariable());
				}
			}
			// ////////////////////////////////////////////////////////////////////////////////////
			submitForm.setAnswer("muc#roomconfig_roomdesc", channelName);// 修改房间描述
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);// 房间是持久的
			submitForm.setAnswer("muc#roomconfig_enablelogging", true);// 仅允许注册的昵称登录
			// 发送已完成的表单到服务器配置聊天室
			chat.sendConfigurationForm(submitForm);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 查询房间是否房间已经创建，并保存房间信息
	 * 
	 * @param channelName
	 */
	private boolean queryRoom(String channelName) {
		// TODO Auto-generated method stub
		try {
			Collection<HostedRoom> rooms = MultiUserChat.getHostedRooms(
					connection, "conference." + connection.getServiceName());
			if (rooms != null && !rooms.isEmpty()) {
				for (HostedRoom entry : rooms) {
					RoomInfo info = MultiUserChat.getRoomInfo(connection,
							entry.getJid());
					roomsList.add(info.getDescription());
					chatRoomMap.put(info.getRoom(), entry.getJid());
				}
				// 判断房间名是否已经存在
				if (roomsList.contains(channelName)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 从服务器根据电视频道查询该电视频道下面的电视节目List<Program>
	 * @param code
	 */
	public static List<Program> queryPrograme(final String code){
		String address=null;
		address=URL+"getProgram?code="+code+"&date=&key="+KEY;
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String reponse) {
				// TODO Auto-generated method stub
				Utility.handleProgramResponse(reponse);
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				Log.d("queryPrograme", "查询节目失败");
			}
		});
		return Utility.programs;
	}
	/**
	 * 从服务器查询频道分类和频道
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

	public String DeleteBlank(String name) {
		return name.replace(" ", "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.choose_area_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
}