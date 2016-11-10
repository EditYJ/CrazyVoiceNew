package com.crazyvoice.activity;

import org.jivesoftware.smack.SmackAndroid;

import com.crazyvoice.app.R;
import com.crazyvoice.util.RegistServer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * ×¢²á½çÃæ
 * 
 * @author admin
 * 
 */
public class RegistActicity extends Activity implements OnClickListener {
	private EditText reName;
	private EditText rePwd;
	private Button reButton;
	private String registResult;
	private RegistServer registServer = new RegistServer(RegistActicity.this);
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(RegistActicity.this, msg.obj.toString(),
					Toast.LENGTH_SHORT).show();
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist);
		init();
	}

	public void init() {
		reName = (EditText) findViewById(R.id.regist_name);
		rePwd = (EditText) findViewById(R.id.regist_pwd);
		reButton = (Button) findViewById(R.id.regist_button);
		reButton.setOnClickListener(this);
		SmackAndroid.init(RegistActicity.this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.regist_button) {

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					int restult = registServer.regist(reName.getText().toString().trim(),rePwd.getText().toString().trim());
					if (restult == 1) {
						registResult = "Success!";
					} else {
						registResult = "Unsuccess!";
					}
					Message Msg = new Message();
					Msg.obj = registResult;
					handler.sendMessage(Msg);
				}
			}).start();
		}
	}
}
