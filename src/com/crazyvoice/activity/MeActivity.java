package com.crazyvoice.activity;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.VCard;

import com.crazyvoice.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MeActivity extends Activity implements OnClickListener{
	private Button upButton;
	private Button getButton;
	private ImageView imgView;
	Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBar();
		setContentView(R.layout.me_activity);
		init();
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	private void init() {
		// TODO Auto-generated method stub
		upButton=(Button) findViewById(R.id.up_img);
		getButton=(Button) findViewById(R.id.get_img);
		imgView=(ImageView) findViewById(R.id.img);
	}

	private void setBar() {
		// TODO Auto-generated method stub
		
	}
	
}
