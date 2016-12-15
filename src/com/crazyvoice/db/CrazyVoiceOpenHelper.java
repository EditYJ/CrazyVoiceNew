package com.crazyvoice.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CrazyVoiceOpenHelper extends SQLiteOpenHelper {

	/*
	 * 建表语句
	 */
//网络配置表
	public static final String CREATE_SERVER = "create table Server("
			+ "ip integer primary key , " + "port text, "
			+ "name text)";
	// 频道分类表
	public static final String CREATE_CATEGORY = "create table Category("
			+ "id integer primary key autoincrement, " + "category_name text, "
			+ "category_code text)";

	// 频道表
	public static final String CREATE_CHANNEL = "create table Channel("
			+ "id integer primary key autoincrement, " + "channel_name text, "
			+ "channel_code text, " + "category_id integer)";
	
	//电视节目表
	public static final String CREATE_PROGRAME="create table Programe("
			+ "id integer primary key autoincrement, " + "cName text, "
			+ "pName text, " + "pUrl text, "+"time text)";
	public CrazyVoiceOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_SERVER);//创建Server表
		db.execSQL(CREATE_CATEGORY); // 创建Category表
		db.execSQL(CREATE_CHANNEL); // 创建Channel表
		db.execSQL(CREATE_PROGRAME);//创建Programe表
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
