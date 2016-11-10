package com.crazyvoice.util;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.content.Context;
import android.os.StrictMode;
/**
 * 处理登录相关
 * @author admin
 *
 */
public class ClientConServer {
	public static XMPPConnection connection;
	private Context context;

	public ClientConServer(Context context) {
		this.context = context;
	}

	public boolean login(String a, String p) {
		InitServer initServer = new InitServer();
		connection = initServer.connectServer();
		try {

			connection.connect();
			connection.login(a, p);
			Presence presence = new Presence(Presence.Type.available);
			presence.setStatus("hehe");
			connection.sendPacket(presence);
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return false;
	}

}