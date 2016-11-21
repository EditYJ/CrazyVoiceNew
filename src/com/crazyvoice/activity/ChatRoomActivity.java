package com.crazyvoice.activity;

import java.util.Date;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.DelayInformation;

import com.crazyvoice.activity.ChatRoom.ChatPacketListener;
import com.crazyvoice.app.R;
import com.crazyvoice.util.ClientConServer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 聊天室
 * @author 俞杰
 *
 */
public class ChatRoomActivity extends Activity {
	private final int RECIEVE = 1;
	Handler handler;
	private MultiUserChat muc;
	private EditText commentEditText;
	private TextView contentTextView;
	private TextView titleTextView;
	private Button sendButton;
	//消息监听器
	class ChatPacketListener implements PacketListener{
		private String _number;
		private Date _lastDate;
		private MultiUserChat _muc;
		private String _roomName;
		
		public ChatPacketListener(MultiUserChat muc) {
			_number = "0";
			_lastDate = new Date(0);
			_muc = muc;
			_roomName = muc.getRoom();
		}
		@Override
		public void processPacket(Packet packet) {
			// TODO Auto-generated method stub
			Message message = (Message) packet;
			String from = message.getFrom();
			if(message.getBody() != null){
				DelayInformation inf = (DelayInformation) message.getExtension(
						"x", "jabber:x:delay");
				Date sentDate;
				if (inf != null) {
					sentDate = inf.getStamp();
				} else {
					sentDate = new Date();
				}
				android.os.Message msg = new android.os.Message();
				msg.what = RECIEVE;
				Bundle bd = new Bundle();
				bd.putString("date", sentDate.toString());
				bd.putString("from", from);
				bd.putString("body", message.getBody());
				msg.setData(bd);
				// System.out.println("ddx on 09:55: " +
				// msg.getData().getString("body"));
				handler.sendMessage(msg);
			}
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SmackAndroid.init(ChatRoomActivity.this);
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);//左右滑动效果
		setBar();
		setContentView(R.layout.activity_chat_room);
		init();
		handler=new Handler(){
			@Override
			public void handleMessage(android.os.Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 1: {
					String from = msg.getData().getString("from");
					String result = msg.getData().getString("body");
					String date = msg.getData().getString("date");
					contentTextView.setText(contentTextView.getText() + "\n"
							+ from + "    " + date + "\n" + result);
				}
					break;
				default:
					break;
				}
			}
		};
		Intent intent=getIntent();
		String roomname=intent.getStringExtra("roomName");
		titleTextView.setText(roomname);
		setTitle(roomname);
		try {
			//DiscussionHistory()
			//控制历史消息的类
			//The DiscussionHistory class controls the number of characters or messages to receive when entering a room
			DiscussionHistory history = new DiscussionHistory();
			history.setMaxStanzas(5);
			muc=new MultiUserChat(ClientConServer.connection, roomname+"@conference.gswtek-022");
			String name = ClientConServer.connection.getUser();
			//SmackConfiguration.getPacketReplyTimeout() 
			//Returns the number of milliseconds to wait for a response from the server.
			muc.join(name, "123", history, SmackConfiguration.getPacketReplyTimeout());
			//设置发送按钮事件
			sendButton.setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							String comment = commentEditText.getText().toString();
							try {
								muc.sendMessage(comment);
								commentEditText.setText(null);
							} catch (XMPPException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
			//加入监听，监听消息包
			ChatPacketListener chatListener = new ChatPacketListener(muc);
			muc.addMessageListener(chatListener);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置标题菜单栏
	 */
	private void setBar() {
		// TODO Auto-generated method stub
		ActionBar bar=getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home: 
			finish();
		}
		return true;
	}
	/*
	 * 初始化部件
	 */
	public void init(){
		commentEditText=(EditText) findViewById(R.id.comment);
		contentTextView=(TextView) findViewById(R.id.content);
		contentTextView
		.setMovementMethod(ScrollingMovementMethod.getInstance());
		titleTextView=(TextView) findViewById(R.id.room_name);
		sendButton=(Button) findViewById(R.id.send_button);
	}
}
