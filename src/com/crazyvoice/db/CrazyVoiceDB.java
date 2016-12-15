package com.crazyvoice.db;

import java.util.ArrayList;
import java.util.List;

import com.crazyvoice.model.Category;
import com.crazyvoice.model.Channel;
import com.crazyvoice.model.Program;
import com.crazyvoice.model.ServerInfor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
 * ����ཫ���һЩ���õ����ݿ������װ�������Է������Ǻ���ʹ��.
 */
public class CrazyVoiceDB {
	public static final String DB_NAME = "crazy_voice";// ���ݿ���
	public static final int VERSION = 1;// ���ݿ�汾
	public static CrazyVoiceDB crazyVoiceDB;
	private SQLiteDatabase db;

	/*
	 * �����췽��˽�л�
	 */
	private CrazyVoiceDB(Context context){
		CrazyVoiceOpenHelper dbhelper=new CrazyVoiceOpenHelper(context, DB_NAME, null, VERSION);
		db=dbhelper.getWritableDatabase();
	}
	
	/*
	 * ��ȡCrazyVoiceDB��ʵ��
	 */
	public synchronized static CrazyVoiceDB getInstance(Context context) {
		if (crazyVoiceDB == null) {
			crazyVoiceDB = new CrazyVoiceDB(context);
		}
		return crazyVoiceDB;
		}
	
	/*
	 * ��Serverʵ���������ݿ�
	 */
	public void saveServer(ServerInfor serverInfor){
		if(serverInfor!=null){
			ContentValues values=new ContentValues();
			values.put("ip", serverInfor.getServerIp());
			values.put("port", serverInfor.getPort());
			values.put("name", serverInfor.getServerName());
		}
	}
	public ServerInfor loadServerInfo(){
		Cursor cursor=db.query("Category", null, null, null, null, null, null);
		cursor.moveToLast();
		ServerInfor serverInfor=new ServerInfor();
		serverInfor.setServerIp(cursor.getString(cursor.getColumnIndex("ip")));
		serverInfor.setPort(cursor.getString(cursor.getColumnIndex("port")));
		serverInfor.setServerName(cursor.getString(cursor.getColumnIndex("name")));
		return serverInfor;
	}
	/*
	 * ��Categoryʵ���������ݿ�
	 */
	public void saveCategory(Category category){
		if(category!=null){
			ContentValues values=new ContentValues();
			values.put("category_name", category.getCategoryName());
			values.put("category_code", category.getCategoryCode());
			db.insertOrThrow("Category", null, values);
		}
	}
	
	/*
	 * �����ݿ��ȡƵ��������Ϣ
	 */
	public List<Category> loadCategory(){
		List<Category> list=new ArrayList<Category>();
		Cursor cursor=db.query("Category", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Category category=new Category();
				category.setId(cursor.getInt(cursor.getColumnIndex("id")));
				category.setCategoryName(cursor.getString(cursor.getColumnIndex("category_name")));
				category.setCategoryCode(cursor.getString(cursor.getColumnIndex("category_code")));
				list.add(category);
			}while(cursor.moveToNext());
		}
		return list;
	}
	/*
	 * ��channelʵ���������ݿ�
	 */
	public void saveChannel(Channel channel){
		if(channel!=null){
			ContentValues values=new ContentValues();
			values.put("channel_code", channel.getChannelCode());
			values.put("channel_name", channel.getChannelName());
			values.put("category_id", channel.getCategoryId());
			db.insert("Channel", null, values);
		}
	}
	public List<Channel> loadChannel(int categoryId){
		List<Channel> list= new ArrayList<Channel>();
		Cursor cursor=db.query("Channel", null,  "category_id = ?", new String[] { String.valueOf(categoryId) }, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Channel channel=new Channel();
				channel.setCategoryId(cursor.getInt(cursor.getColumnIndex("category_id")));
				channel.setChannelCode(cursor.getString(cursor.getColumnIndex("channel_code")));
				channel.setChannelName(cursor.getString(cursor.getColumnIndex("channel_name")));
				channel.setId(cursor.getInt(cursor.getColumnIndex("id")));
				list.add(channel);
			}while(cursor.moveToNext());
		}
		return list;
		
	}
	
	/*
	 * ��programeʵ���������ݿ�
	 */
	public void savePrograme(Program program){
		if(program!=null){
			ContentValues values=new ContentValues();
			values.put("cName", program.getcName());
			values.put("pName", program.getpName());
			values.put("pUrl", program.getpUrl());
			values.put("time", program.getTime());
			db.insert("Programe", null, values);
		}
	}
	/*
	 * �����ݿ��ȡ��Ŀ����
	 */
	public List<Program> loadPrograme(String cName){
		List<Program> list= new ArrayList<Program>();
		Cursor cursor=db.query("Programe", null,  "cName = ?", new String[] { cName }, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Program program=new Program();
				program.setcName(cursor.getString(cursor.getColumnIndex("cName")));
				program.setpName(cursor.getString(cursor.getColumnIndex("pName")));
				program.setpUrl(cursor.getString(cursor.getColumnIndex("pUrl")));
				program.setTime(cursor.getString(cursor.getColumnIndex("time")));
				program.setId(cursor.getInt(cursor.getColumnIndex("id")));
				list.add(program);
			}while(cursor.moveToNext());
		}
		return list;
		
	}
	
	/*
	 * ɾ��ĳ��������������
	 */
	public void deleteDate(String table){
		 db.execSQL("delete from "+table);
	}
}
