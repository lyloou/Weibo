package com.lyloou.weibo.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lyloou.weibo.R;
import com.lyloou.weibo.control.MainService;
import com.lyloou.weibo.model.Task;
import com.lyloou.weibo.view.IWeiboActivity;

public class StructureActivity extends Activity implements IWeiboActivity {

	protected static final String TAG = "Weibo";
	private Button logBtn;
	private static EditText et_info;
	private static TextView tv_info;
	Handler handler  = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==1){
				Log.d(TAG, "��ʱ��ˢ���˰�");
				Toast.makeText(getApplicationContext(), "ֵ��:::"+msg.obj.toString(), 3000).show();;
				String str  = msg.obj.toString();
				et_info.invalidate();
				et_info.setText(str);
				

				logBtn.setText("HELLOWORLD");
				et_info.postInvalidate();
				tv_info.setText(str);
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity_structure);
		Log.d(TAG, "ִ����һ��onCreate");
		logBtn = (Button) findViewById(R.id.button1);

		et_info = (EditText) findViewById(R.id.et_Text1);
		tv_info = (TextView) findViewById(R.id.tv_Text2);

		// Activity�������漴��������
		Intent intent = new Intent(StructureActivity.this, MainService.class);
		startService(intent);
		MainService.addActivity(StructureActivity.this);
		
		// ��������¼�`
		logBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "����˰�ť");
				Task t = new Task(Task.WEIBO_LOGIN, null);
				MainService.newTask(t);
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d(TAG, "ִ����onPause");
		Intent intent = new Intent(StructureActivity.this, MainService.class);
		stopService(intent);
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "ִ����onStop");
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		// TODO Auto-generated method stub
		finish();
		super.onDestroy();
	}

	@Override
	public void init() {
	}

	public void refresh(Object... obj) {
		Log.d(TAG, "ˢ��edittext"+"ȡ����ֵ:"+obj[0].toString());
		Message msg = handler.obtainMessage();
		msg.what = 1;
		msg.obj = obj[0].toString();
		msg.sendToTarget();
	}

}
