package com.crazyvoice.util;

import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.crazyvoice.db.CrazyVoiceDB;
import com.crazyvoice.model.Category;
import com.crazyvoice.model.Channel;

/*
 * 处理服务器返回数据
 */
public class Utility {
	/*
	 * 处理频道分类信息
	 */
	public synchronized static boolean handleCategoryResponse(
			CrazyVoiceDB crazyVoiceDB, String reponse) {
		try {
			JSONObject object = new JSONObject(reponse);
			Log.d("CrazyVoice_Utility_Category",
					object.get("error_code") + ":" + object.get("reason"));
			if (object.getInt("error_code") == 0) {
				String result = object.getString("result");
				Log.d("CrazyVoice_Utility_Category", result);
				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String id = jsonObject.getString("id");
					String name = jsonObject.getString("name");
					Category category = new Category();
					category.setCategoryCode(id);
					category.setCategoryName(name);
					crazyVoiceDB.saveCategory(category);
				}
				return true;
			} else {
				Log.d("CrazyVoice_Utility_Category", object.get("error_code") + ":"
						+ object.get("reason"));
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	/*
	 * 处理频道信息
	 */
	public static boolean handleChannelResponse(CrazyVoiceDB crazyVoiceDB,
			String reponse) {
		try {
			JSONObject object = new JSONObject(reponse);
			Log.d("CrazyVoice_Utility_Channel",
					object.get("error_code") + ":" + object.get("reason"));
			if (object.getInt("error_code") == 0) {
				String result = object.getString("result");
				Log.d("CrazyVoice_Utility_Channel", result);
				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String rel = jsonObject.getString("rel");
					String channelName = jsonObject.getString("channelName");
					String pid = jsonObject.getString("pId");
					//String url=jsonObject.getString("url");
					Channel channel = new Channel();
					channel.setChannelCode(rel);
					channel.setCategoryId(Integer.parseInt(pid));
					channel.setChannelName(channelName);
					//channel.setUrl(channelName);
					crazyVoiceDB.saveChannel(channel);
				}
				return true;
			} else {
				Log.d("CrazyVoice_Utility_Channel", object.get("error_code") + ":"
						+ object.get("reason"));
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;

	}
}
