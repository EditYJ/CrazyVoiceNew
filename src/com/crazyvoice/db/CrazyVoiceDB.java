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
 * 这个类将会把一些常用的数据库操作封装起来，以方便我们后面使用.
 */
public class CrazyVoiceDB {
	public static final String DB_NAME = "crazy_voice";// 数据库名
	public static final int VERSION = 1;// 数据库版本
	public static CrazyVoiceDB crazyVoiceDB;
	private SQLiteDatabase db;

	/*
	 * 将构造方法私有化
	 */
	private CrazyVoiceDB(Context context){
		CrazyVoiceOpenHelper dbhelper=new CrazyVoiceOpenHelper(context, DB_NAME, null, VERSION);
		db=dbhelper.getWritableDatabase();
	}
	
	/*
	 * 获取CrazyVoiceDB的实例
	 */
	public synchronized static CrazyVoiceDB getInstance(Context context) {
		if (crazyVoiceDB == null) {
			crazyVoiceDB = new CrazyVoiceDB(context);
		}
		return crazyVoiceDB;
		}
	
	/*
	 * 将Server实例存入数据库
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
	 * 将Category实例存入数据库
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
	 * 从数据库读取频道分类信息
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
	 * 将channel实例存入数据库
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
	 * 将programe实例存入数据库
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
	 * 从数据库读取节目数据
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
	 * 删除某个表中所有数据
	 */
	public void deleteDate(String table){
		 db.execSQL("delete from "+table);
	}
}
