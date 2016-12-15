package com.crazyvoice.util;

import java.io.IOException;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;

import com.crazyvoice.model.ServerInfor;

import android.os.StrictMode;

/**
 * 连接服务器初始化
 * 
 * @author admin
 * 
 */
public class InitServer {
	private ServerInfor serverInfor;
	private SetServerInfor setServer=new SetServerInfor();
	private static String IP = null;
	private static int PORT;
	public static XMPPConnection connection;

	public XMPPConnection connectServer(){
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);// 严苛模式
		try {
			serverInfor=setServer.getServerInfor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IP=serverInfor.getServerIp();
		PORT=Integer.parseInt(serverInfor.getPort());
		ConnectionConfiguration config = new ConnectionConfiguration(IP, PORT);
		config.setSelfSignedCertificateEnabled(true);
		config.setSASLAuthenticationEnabled(false);
		config.setDebuggerEnabled(false);
		config.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
		// Class.forName("org.jivesoftware.smackx.ServiceDiscoveryManager",
		// true, ClientConServer.class.getClassLoader());
		connection = new XMPPConnection(config);
		return connection;

	}
}
