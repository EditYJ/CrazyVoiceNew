package com.crazyvoice.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CrazyVoiceOpenHelper extends SQLiteOpenHelper {

	/*
	 * �������
	 */

	// Ƶ�������
	public static final String CREATE_CATEGORY = "create table Category("
			+ "id integer primary key autoincrement, " + "category_name text, "
			+ "category_code text)";

	// Ƶ����
	public static final String CREATE_CHANNEL = "create table Channel("
			+ "id integer primary key autoincrement, " + "channel_name text, "
			+ "channel_code text, " + "category_id integer)";

	public CrazyVoiceOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_CATEGORY); // ����Category��
		db.execSQL(CREATE_CHANNEL); // ����Channel��
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}