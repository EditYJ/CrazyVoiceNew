package com.crazyvoice.util;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;

import com.crazyvoice.model.ServerInfor;

import android.content.Context;

public class RegistServer {
	public static XMPPConnection connection;
	private Context context;
	private ServerInfor serverInfor;
	private SetServerInfor setServer=new SetServerInfor();
	public RegistServer(Context context) {
		this.context = context;
	}

	@SuppressWarnings("unused")
	public int regist(String name, String password) {

		InitServer initServer = new InitServer();
		connection = initServer.connectServer();
		try {
			serverInfor=setServer.getServerInfor();
			Registration registration = new Registration();
			registration.setType(IQ.Type.SET);
			registration.setTo(serverInfor.getServerName());
			registration.setUsername(name);
			registration.setPassword(password);
			registration.addAttribute("adroid", "yujie_RegistServer_android");
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					registration.getPacketID()), new PacketTypeFilter(IQ.class));// °ü¹ýÂË
			connection.connect();
			PacketCollector collector = connection
					.createPacketCollector(filter);
			connection.sendPacket(registration);
			IQ result = (IQ) collector.nextResult(SmackConfiguration
					.getPacketReplyTimeout());
			collector.cancel();
			if (result.getType() == IQ.Type.RESULT) {
				return 1;// Success
			} else if (result == null) {
				return 2;// Server is no response
			} else {
				return 0;// Unsuccess
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}
}
