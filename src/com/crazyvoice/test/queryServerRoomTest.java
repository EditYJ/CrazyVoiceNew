package com.crazyvoice.test;

import org.jivesoftware.smack.SmackAndroid;

import com.crazyvoice.activity.RegistActicity;
import com.crazyvoice.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class queryServerRoomTest extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		TextView text=(TextView) findViewById(R.id.test_text);
		SmackAndroid.init(queryServerRoomTest.this);
	}
}
