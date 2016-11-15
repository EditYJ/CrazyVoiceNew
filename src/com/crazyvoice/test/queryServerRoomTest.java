package com.crazyvoice.test;

import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;

import com.crazyvoice.activity.RegistActicity;
import com.crazyvoice.app.R;
import com.crazyvoice.util.ClientConServer;
import com.crazyvoice.util.InitServer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
/**
 * ��ѯ���������
 * @author yujie
 *
 */
public class queryServerRoomTest extends Activity {
	public static TextView text;
	private static final String TAG = "NetUitl01";
	private static final String TAGT = "NetUitl02";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test);
		init();
		initHostRoom();
	}
	private void initHostRoom() {
		InitServer initServer=new InitServer();
		XMPPConnection connection=initServer.connectServer();
		Collection<HostedRoom> hostedrooms;
		try {
			connection.connect();
			connection.login("user2", "passw0rd");
			//��ȡ����������һ������û�л��������
			hostedrooms=MultiUserChat.getHostedRooms(connection, connection.getServiceName());
			for(HostedRoom entry:hostedrooms){
		        Log.i(TAG, "����������֣�" + entry.getName() + " - ID:" + entry.getJid()+"***"+connection.getServiceName());
		        //��ȡ��������еķ��䣬���һ���������ϻ������������������Jid
		        Collection<HostedRoom> hostedrooms2=MultiUserChat.getHostedRooms(connection, entry.getJid());
		        for(HostedRoom entry2:hostedrooms2){
					RoomInfo info = MultiUserChat.getRoomInfo(
							connection,entry2.getJid());
			        Log.i(TAGT, "�������֣�" + info.getRoom() + " - ID:" + entry2.getJid());
			        //���뷿�䲿�ֵĲ��ԣ����뷿����Ҫ��ȡ����Jid
			        MultiUserChat e=new MultiUserChat(connection, entry2.getJid());
			        e.join("����ɹ�");
			        Log.v(TAGT, "join success"); 
		        }
			}
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void init() {
		// TODO Auto-generated method stub
		TextView text = (TextView) findViewById(R.id.test_text);
		SmackAndroid.init(queryServerRoomTest.this);
	}
}
