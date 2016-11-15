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
 * �������������
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
	 * ����Ĭ�����õ������� ע��ֱ���û�����join������ʱ�������ҲŻᱻ������
	 */
	private void creatRoom() {
		InitServer initServer = new InitServer();
		XMPPConnection connection = initServer.connectServer();
		try {
			connection.connect();
			connection.login("user2", "passw0rd");
			// ����MultiUserChat�������ú�����������
			MultiUserChat chat = new MultiUserChat(connection,
					"room-yujie@conference.gswtek-022");//�����room-yujie�Ƿ����ʶ
			chat.create("yujie-������");//�޸���user2���ǳ�Ϊ"yujie-������"
			// ��ȡ���������ñ�
			Form form = chat.getConfigurationForm();
			// ����ԭʼ������һ��Ҫ�ύ���±�
			Form submitForm = form.createAnswerForm();
			// /////////////////////////////////////////////////////////////////////////////////////
			// ���ύ�ı����Ĭ�ϴ�
			for (Iterator<FormField> fields = form.getFields(); fields
					.hasNext();) {
				FormField field = (FormField) fields.next();
				if (!FormField.TYPE_HIDDEN.equals(field.getType())
						&& field.getVariable() != null) {
					// ����Ĭ��ֵ��Ϊ��
					submitForm.setDefaultAnswer(field.getVariable());
				}
			}
			// ////////////////////////////////////////////////////////////////////////////////////
			/**
			 * Spark����������MUC: �������� text-single muc#roomconfig_roomname ����
			 * text-single muc#roomconfig_roomdesc ����ռ���߸������� boolean
			 * muc#roomconfig_changesubject ��󷿼�ռ�������� list-single
			 * muc#roomconfig_maxusers �� Presence�� Broadcast�Ľ�ɫ list-multi
			 * muc#roomconfig_presencebroadcast �г�Ŀ¼�еķ��� boolean
			 * muc#roomconfig_publicroom �����ǳ־õ� boolean
			 * muc#roomconfig_persistentroom �������ʶȵ� boolean
			 * muc#roomconfig_moderatedroom ������Գ�Ա���� boolean
			 * muc#roomconfig_membersonly ����ռ�������������� boolean
			 * muc#roomconfig_allowinvites ��Ҫ������ܽ��뷿�� boolean
			 * muc#roomconfig_passwordprotectedroom ���� text-private
			 * muc#roomconfig_roomsecret �ܹ�����ռ������ʵ JID �Ľ�ɫ list-single
			 * muc#roomconfig_whois ��¼����Ի� boolean muc#roomconfig_enablelogging
			 * ������ע����ǳƵ�¼ boolean x-muc#roomconfig_reservednick ����ʹ�����޸��ǳ�
			 * boolean x-muc#roomconfig_canchangenick �����û�ע�᷿�� boolean
			 * x-muc#roomconfig_registration �������Ա jid-multi
			 * muc#roomconfig_roomadmins ����ӵ���� jid-multi
			 * muc#roomconfig_roomowners
			 */
			// ������������������
			submitForm.setAnswer("muc#roomconfig_roomname", "room-yujieNew");
			// ����������ӵ����.List<String>װ��ӵ��������
			List<String> owners = new ArrayList<String>();
			owners.add("yujie01-ӵ����");
			owners.add("yujie02-ӵ����");
			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			// �����������ǳ־������ң�����Ҫ����������
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);
			// ������Գ�Ա����
			//submitForm.setAnswer("muc#roomconfig_membersonly", false);
			// ����ռ��������������
			submitForm.setAnswer("muc#roomconfig_allowinvites", true);
			// ���÷�������
			submitForm.setAnswer("muc#roomconfig_roomdesc",
					"yujie create this room");
			// ��������ɵı�������������������
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
