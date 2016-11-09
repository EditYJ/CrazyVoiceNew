package com.crazyvoice.db;

import java.util.ArrayList;
import java.util.List;

import com.crazyvoice.model.Category;
import com.crazyvoice.model.Channel;

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
	
}
