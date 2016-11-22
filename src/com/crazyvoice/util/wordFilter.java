package com.crazyvoice.util;

import java.util.ArrayList;
import java.util.List;

/**
 * From 内的字符解析(/cctv-1综合@conference.gswtek-022/user2@gswtek-022/Smack)
 * 
 * @param 俞杰
 */
public class wordFilter {
	public static List<String> cutstring(String Stence) {
		List<String> stringlist = new ArrayList<String>();// 用来存储解析出来的元素
		for (int i = 0; i < Stence.length(); i++) {
			if (Stence.charAt(i) == '/') {
				String temp = "";// 存储单词
				int wordlength = i;
				while (wordlength < Stence.length() - 1
						&& Stence.charAt(++wordlength) != '/') {
					temp += Stence.charAt(wordlength);
					// System.out.println(temp);
				}
				stringlist.add(temp);
			}
		}
		return stringlist;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String m_sentence = "/cctv-1综合@conference.gswtek-022/user2@gswtek-022/Smack";
		List<String> m_list = cutstring(m_sentence);
		System.out.println(m_list.size());
		for (String tmp : m_list) {
			System.out.println(tmp);
		}

	}
}
