package com.lyloou.weibo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lyloou.weibo.R;
import com.lyloou.weibo.app.AccessTokenKeeper;
import com.lyloou.weibo.app.Constants;
import com.lyloou.weibo.app.LogUtil;
import com.lyloou.weibo.app.MyApplication;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class LoginActivity extends Activity implements IWeiboActivity, OnClickListener {
	private AuthInfo mAuthInfo;
	private SsoHandler mSsoHandler;
	private Oauth2AccessToken mAccessToken;
	private TextView mTokenText;
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

		// 获取本地accessToken,如果有的话.
		Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(this);
		if(!TextUtils.isEmpty(accessToken.getToken())){
			MyApplication.accessToken = accessToken;
			Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(intent);
			return ;
		}

		Button loginBtn = (Button) findViewById(R.id.id_auth_login_btn);
		Button addUserBtn = (Button) findViewById(R.id.id_auth_add_user_btn);
		// 授权信息初始化
		mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);

		mTokenText = (TextView) findViewById(R.id.textView1);

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
				Toast.makeText(this,"clicked!",Toast.LENGTH_SHORT).show();
				mSsoHandler.authorizeWeb(new AuthListener()); //通过网页授权
				break;
			case R.id.id_auth_login_btn:
				break;
		}
	}

	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {

//			String accessToken = values.getString("access_token");
//			String expires_in = values.getString("expires_in");
//
//			Log.d(TAG,accessToken+"---"+expires_in);

			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// 显示 Token
//				updateTokenView(false);

				// 保存 Token 到 SharedPreferences
				AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
				Toast.makeText(LoginActivity.this,
						"授权成功", Toast.LENGTH_SHORT).show();
				Log.d(LogUtil.TAG,"授权成功");
				MyApplication.accessToken = mAccessToken;
				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				Log.d(LogUtil.TAG,"授权失败");
				String code = values.getString("code");
				String message = "授权失败";
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onCancel() {
			Log.d(LogUtil.TAG,"取消授权");
			Toast.makeText(LoginActivity.this,
					"取消授权", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(LoginActivity.this,
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		private void updateTokenView(boolean hasExisted) {
			Log.d(LogUtil.TAG,"更新text");
			String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA).format(
					new java.util.Date(mAccessToken.getExpiresTime()));
			String format = "Token：%1$s \n有效期：%2$s";
			mTokenText.setText(String.format(format, mAccessToken.getToken(), date));

			String message = String.format(format, mAccessToken.getToken(), date);
			if (hasExisted) {
				message ="Token 仍在有效期内，无需再次登录。"+ "\n" + message;
			}
			mTokenText.setText(message);
		}
	}
}
