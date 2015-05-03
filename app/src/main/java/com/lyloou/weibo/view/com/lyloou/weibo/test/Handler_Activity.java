package com.lyloou.weibo.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.lyloou.weibo.R;

public class Handler_Activity extends Activity {
	protected static final String TAG = "IWeibo";
	Button btn;
	TextView tv;
	int mIndex = 0;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				String text = msg.obj.toString();
				tv.setText(text);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity_handler);

		btn = (Button) findViewById(R.id.id_test_handler_button_btn);
		tv = (TextView) findViewById(R.id.id_test_handler_textView_tv);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 如果在这里睡眠或处理耗时操作就会崩溃
				/*
				 * try { Thread.sleep(3000); } catch (InterruptedException e) {
				 * e.printStackTrace(); }
				 */
				mIndex++;
				Log.d(TAG, "我被点击了哦!" + mIndex);
				new Thread() {
					public void run() {
						// 在子线程这里里就不会崩溃
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Message msg = handler.obtainMessage();
						msg.what = 1;
						msg.obj = " aLouAll";
						msg.sendToTarget();

					}
				}.start();
			}
		});
	}

}
