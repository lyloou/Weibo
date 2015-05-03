package com.lyloou.weibo.test;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lyloou.weibo.R;
import com.lyloou.weibo.test.MyService.MyBinder;

public class Service_Activity extends Activity implements OnClickListener{

	private ServiceConnection conn = new ServiceConnection() {

		private MyBinder mBind;

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBind = (MyService.MyBinder)service;
			mBind.print("onServiceConnected:"+"service--"+service +" | name--"+name.getClassName());
		}
	};
	private boolean mIsBind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity_service);

		Button start_btn = (Button) findViewById(R.id.id_test_start_service_button_btn);
		Button bind_btn = (Button) findViewById(R.id.id_test_bind_service_button_btn);
		Button unbind_btn = (Button) findViewById(R.id.id_test_unbind_service_button_btn);

		start_btn.setOnClickListener(this);
		bind_btn.setOnClickListener(this);
		unbind_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch(v.getId()){
			case R.id.id_test_start_service_button_btn:
				intent.setClass(this, MyService.class);
				startService(intent);
				break;
			case R.id.id_test_bind_service_button_btn:
				if(mIsBind){
					unbindService(conn);
				}
				intent.setClass(this, MyService.class);
				bindService(intent, conn, BIND_AUTO_CREATE);
				mIsBind = true;
				break;
			case R.id.id_test_unbind_service_button_btn:
				if(mIsBind){
					unbindService(conn);
					mIsBind = false;
				}
				break;
		}
	}

}
