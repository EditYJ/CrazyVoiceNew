package com.crazyvoice.model;
/**
 * ��Ϣ��ʵ����
 * @author yujie
 *
 */
public class Msg {
	public static final int TYPE_RECEIVED = 0;//��ʾ����һ���յ�����Ϣ
	public static final int TYPE_SENT = 1;//��ʾ����һ�����͵���Ϣ
	private String content;//��Ϣ����
	private int type;//��Ϣ����
	public Msg(String content, int type) {
		this.content = content;
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public int getType() {
		return type;
	}
	
}
