package com.crazyvoice.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import android.content.Context;

import com.crazyvoice.model.ServerInfor;

public class SetServerInfor {
	static ServerInfor serverInfor=new ServerInfor();
	public static String ip=null;
	public static String port;
	public static String name=null;
	InputStream in = SetServerInfor.class.getResourceAsStream("/assets/webset.properties"); 
	//获取连接服务器所需要的信息
	public ServerInfor getServerInfor() throws IOException{
		Properties prop = new Properties();
		prop.load(in);
		in.close();
		ip=prop.getProperty("ip");
		port=prop.getProperty("port");
		name=prop.getProperty("name");
		serverInfor.setServerIp(ip);
		serverInfor.setPort(port);
		serverInfor.setServerName(name);
		return serverInfor;
	}
	//设置连接服务器所需要的信息
	public void setServerInfor(ServerInfor serverInfor) throws IOException{
		Properties prop = new Properties();
		prop.load(in);
		in.close();
		ip=serverInfor.getServerIp();
		port=serverInfor.getPort();
		name=serverInfor.getServerName();
		prop.setProperty("ip", ip);
		prop.setProperty("port", String.valueOf(port));
		prop.setProperty("name", name);
		OutputStream fos = new FileOutputStream("/assets/webset.properties");
		prop.store(fos, "update");
		fos.close();
	}
}
