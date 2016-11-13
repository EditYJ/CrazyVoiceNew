package com.crazyvoice.activity;

import java.util.Collection;

import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.crazyvoice.app.R;
import com.crazyvoice.test.queryServerRoomTest;
import com.crazyvoice.util.ClientConServer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * ��½����
 * 
 * @author admin
 * 
 */
public class LogIn extends Activity implements OnClickListener {
	private EditText accountEditText;
	private EditText passwordEditText;
	private Button loginButton;
	private Button registButton;
	private Button testButton;
	private boolean loginResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_log_in);
		init();
	}

	/**
	 * ���õ���¼�
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// ��¼��ť����¼���Ŀ������ת��Ƶ����ȡҳ��
		if (v.getId() == R.id.login_button) {
			String account = accountEditText.getText().toString().trim();
			String password = passwordEditText.getText().toString().trim();
			if (account.equals("") || password.equals("")) {
				Toast.makeText(LogIn.this,
						"UserName or PassWord should not be NULL",
						Toast.LENGTH_SHORT).show();
			} else {
				ClientConServer ccs = new ClientConServer(LogIn.this);
				loginResult = ccs.login(account, password);
				if (loginResult) {
					Toast.makeText(LogIn.this, "Sucessful", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LogIn.this, QuestionsList.class);
					//Bundle bundle=new Bundle();
					startActivity(intent);
				} else {
					Toast.makeText(LogIn.this, "Fail", Toast.LENGTH_SHORT).show();
				}
			}
		} else if (v.getId() == R.id.enter_regist_button) {// ע�ᰴť����¼�����ת��ע��ҳ��
			Intent intent = new Intent();
			intent.setClass(LogIn.this, RegistActicity.class);
			startActivity(intent);
		}else if(v.getId() == R.id.Test_button){
			Intent intent = new Intent();
			intent.setClass(LogIn.this, queryServerRoomTest.class);
			startActivity(intent);
		}
	}

	/**
	 * ��ʼ�����
	 */
	public void init() {
		accountEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		loginButton = (Button) findViewById(R.id.login_button);
		registButton = (Button) findViewById(R.id.enter_regist_button);
		testButton=(Button) findViewById(R.id.Test_button);
		loginButton.setOnClickListener(this);
		registButton.setOnClickListener(this);
		testButton.setOnClickListener(this);
		SmackAndroid.init(LogIn.this);// ��ʼ��Asmackƽ̨,����ģ�����ᱨ��
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_in, menu);
		return true;
	}
	/*
	 * public boolean connectServer(){
	 * 
	 * }
	 */

}
