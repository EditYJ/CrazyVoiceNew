package com.crazyvoice.util;

import java.util.ArrayList;
import java.util.List;

/**
 * From �ڵ��ַ�����(/cctv-1�ۺ�@conference.gswtek-022/user2@gswtek-022/Smack)
 * 
 * @param ���
 */
public class wordFilter {
	public static List<String> cutstring(String Stence) {
		List<String> stringlist = new ArrayList<String>();// �����洢����������Ԫ��
		for (int i = 0; i < Stence.length(); i++) {
			if (Stence.charAt(i) == '/') {
				String temp = "";// �洢����
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
		String m_sentence = "/cctv-1�ۺ�@conference.gswtek-022/user2@gswtek-022/Smack";
		List<String> m_list = cutstring(m_sentence);
		System.out.println(m_list.size());
		for (String tmp : m_list) {
			System.out.println(tmp);
		}

	}
}
