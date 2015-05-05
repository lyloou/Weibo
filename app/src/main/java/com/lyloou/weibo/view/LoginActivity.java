package com.lyloou.weibo.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lyloou.weibo.R;
import com.lyloou.weibo.app.Constants;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class LoginActivity extends Activity implements IWeiboActivity, OnClickListener {
	private AuthInfo mAuthInfo;
	private SsoHandler mSsoHandler;
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
		Button loginBtn = (Button) findViewById(R.id.id_auth_login_btn);
		Button addUserBtn = (Button) findViewById(R.id.id_auth_add_user_btn);

		mAuthInfo = new AuthInfo(LoginActivity.this, Constants. APP_KEY,
				Constants. REDIRECT_URL, Constants. SCOPE);
		mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);

		loginBtn.setOnClickListener(this);
		addUserBtn.setOnClickListener(this);

	}

	@Override
	public void init() {
	}

	public void refresh(Object... obj) {
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.id_auth_add_user_btn:
				mSsoHandler.authorize(new AuthListener());
				break;
			case R.id.id_auth_login_btn:
				break;
		}
	}

	private class AuthListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle bundle) {

		}

		@Override
		public void onWeiboException(WeiboException e) {

		}

		@Override
		public void onCancel() {

		}
	}
}
