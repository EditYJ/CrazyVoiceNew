package com.crazyvoice.test;

import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.SmackAndroid;
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
import android.view.Window;
import android.widget.TextView;

public class queryServerRoomTest extends Activity {
	public static String rom = "room: ";
	public static TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test);
		init();
		InitServer initServer=new InitServer();
		Connection connection=initServer.connectServer();
		try {
			connection.connect();
			connection.login("yujie", "yujie726");
			List<String> col = getConferenceServices(connection.getServiceName(), connection); 
			Collection<HostedRoom> rooms = MultiUserChat.getHostedRooms(
					connection,
					connection.getServiceName());
			if (rooms != null && !rooms.isEmpty()) {
				for (HostedRoom entry : rooms) {
					RoomInfo info = MultiUserChat.getRoomInfo(
							ClientConServer.connection, entry.getJid());
					rom = rom + info.getDescription() + " ";
				}
				text.setText(rom);
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
