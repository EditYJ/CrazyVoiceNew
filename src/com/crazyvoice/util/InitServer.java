package com.crazyvoice.util;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;

import android.os.StrictMode;

/**
 * ���ӷ�������ʼ��
 * 
 * @author admin
 * 
 */
public class InitServer {
	private static String IP = "172.16.21.202";
	private static int PORT = 5222;
	public static XMPPConnection connection;

	public XMPPConnection connectServer() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);// �Ͽ�ģʽ
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
