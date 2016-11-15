package com.crazyvoice.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.crazyvoice.app.R;
import com.crazyvoice.util.InitServer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

/**
 * 创建房间测试类
 * 
 * @author yujie
 * 
 */
public class CreatServerRoomTest extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test);
		init();
		creatRoom();
	}

	/**
	 * 创建默认配置的聊天室 注：直到用户调用join方法的时候聊天室才会被创建！
	 */
	private void creatRoom() {
		InitServer initServer = new InitServer();
		XMPPConnection connection = initServer.connectServer();
		try {
			connection.connect();
			connection.login("user2", "passw0rd");
			// 创建MultiUserChat对象，设置好聊天室名字
			MultiUserChat chat = new MultiUserChat(connection,
					"room-yujie@conference.gswtek-022");//这里的room-yujie是房间标识
			chat.create("yujie-创建者");//修改了user2的昵称为"yujie-创建者"
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
			/**
			 * Spark会议室设置MUC: 房间名称 text-single muc#roomconfig_roomname 描述
			 * text-single muc#roomconfig_roomdesc 允许占有者更改主题 boolean
			 * muc#roomconfig_changesubject 最大房间占有者人数 list-single
			 * muc#roomconfig_maxusers 其 Presence是 Broadcast的角色 list-multi
			 * muc#roomconfig_presencebroadcast 列出目录中的房间 boolean
			 * muc#roomconfig_publicroom 房间是持久的 boolean
			 * muc#roomconfig_persistentroom 房间是适度的 boolean
			 * muc#roomconfig_moderatedroom 房间仅对成员开放 boolean
			 * muc#roomconfig_membersonly 允许占有者邀请其他人 boolean
			 * muc#roomconfig_allowinvites 需要密码才能进入房间 boolean
			 * muc#roomconfig_passwordprotectedroom 密码 text-private
			 * muc#roomconfig_roomsecret 能够发现占有者真实 JID 的角色 list-single
			 * muc#roomconfig_whois 登录房间对话 boolean muc#roomconfig_enablelogging
			 * 仅允许注册的昵称登录 boolean x-muc#roomconfig_reservednick 允许使用者修改昵称
			 * boolean x-muc#roomconfig_canchangenick 允许用户注册房间 boolean
			 * x-muc#roomconfig_registration 房间管理员 jid-multi
			 * muc#roomconfig_roomadmins 房间拥有者 jid-multi
			 * muc#roomconfig_roomowners
			 */
			// 重新设置聊天室名称
			submitForm.setAnswer("muc#roomconfig_roomname", "room-yujieNew");
			// 设置聊天室拥有者.List<String>装载拥有者名字
			List<String> owners = new ArrayList<String>();
			owners.add("yujie01-拥有者");
			owners.add("yujie02-拥有者");
			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			// 设置聊天室是持久聊天室，即将要被保存下来
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);
			// 房间仅对成员开放
			//submitForm.setAnswer("muc#roomconfig_membersonly", false);
			// 允许占有者邀请其他人
			submitForm.setAnswer("muc#roomconfig_allowinvites", true);
			// 设置房间描述
			submitForm.setAnswer("muc#roomconfig_roomdesc",
					"yujie create this room");
			// 发送已完成的表单到服务器配置聊天室
			chat.sendConfigurationForm(submitForm);
//			chat.join("yu-join");
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		TextView text = (TextView) findViewById(R.id.test_text);
		SmackAndroid.init(CreatServerRoomTest.this);
	}
}
