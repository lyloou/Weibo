package com.lyloou.weibo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.lyloou.weibo.R;

public class WelcomeActivity extends Activity {

	protected static final String TAG = "Weibo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		ImageView iv_welcome = (ImageView) findViewById(R.id.id_welcome);
		AlphaAnimation aa = new AlphaAnimation(0.0f,1.0f);
		aa.setDuration(3000);
		iv_welcome.setAnimation(aa);
		iv_welcome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "点击了图片");
				Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		aa.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
	}

}
