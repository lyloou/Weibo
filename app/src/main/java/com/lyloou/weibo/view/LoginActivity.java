package com.lyloou.weibo.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lyloou.weibo.R;

public class LoginActivity extends Activity implements IWeiboActivity {

	protected static final String TAG = "Weibo";

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Button loginBtn = (Button) findViewById(R.id.id_login_btn);
		Button addUserBtn = (Button) findViewById(R.id.id_add_user_btn);
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		addUserBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

	}

	@Override
	public void init() {
	}

	public void refresh(Object... obj) {
	}

}
