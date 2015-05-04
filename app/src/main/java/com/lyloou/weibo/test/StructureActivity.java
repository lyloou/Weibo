package com.lyloou.weibo.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
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

import java.lang.ref.WeakReference;

public class StructureActivity extends Activity implements IWeiboActivity {

    protected static final String TAG = "Weibo";
    private Button logBtn;
    private static EditText et_info;
    private static TextView tv_info;
    Handler handler;/* = new Handler(){ //不采用这种方式, 而是使用内部类
        public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				Log.d(TAG, "是时候刷新了吧");
				Toast.makeText(getApplicationContext(), "值是:::"+msg.obj.toString(), Toast.LENGTH_SHORT).show();
				String str  = msg.obj.toString();
				et_info.invalidate();
				et_info.setText(str);

				logBtn.setText("HELLOWORLD");
				et_info.postInvalidate();
				tv_info.setText(str);
			}
		}
	};*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_structure);
        Log.d(TAG, "执行了一次onCreate");
        logBtn = (Button) findViewById(R.id.button1);

        et_info = (EditText) findViewById(R.id.et_Text1);
        tv_info = (TextView) findViewById(R.id.tv_Text2);

        // Activity启动后随即启动服务
        Intent intent = new Intent(StructureActivity.this, MainService.class);
        startService(intent);
        MainService.addActivity(StructureActivity.this);

        handler = new MyHandler();
        // 监听点击事件`
        logBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "点击了按钮");
                Task t = new Task(Task.WEIBO_LOGIN, null);
                MainService.newTask(t);
            }
        });
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        Log.d(TAG, "执行了onPause");
        Intent intent = new Intent(StructureActivity.this, MainService.class);
        stopService(intent);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "执行了onStop");
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
        Log.d(TAG, "刷新edittext" + "取到了值:" + obj[0].toString());
        Message msg = handler.obtainMessage();
        msg.what = 1;
        msg.obj = obj[0].toString();
        msg.sendToTarget();

        HandlerThread handlerThread = new HandlerThread("子线程");
        Handler handler1 = new Handler(handlerThread.getLooper());
    }

    private class MyHandler extends Handler {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                Log.d(TAG, "是时候刷新了吧");
                Toast.makeText(getApplicationContext(), "值是:::" + msg.obj.toString(), Toast.LENGTH_SHORT).show();
                String str = msg.obj.toString();
                et_info.invalidate();
                et_info.setText(str);

                logBtn.setText("HELLOWORLD");
                et_info.postInvalidate();
                tv_info.setText(str);
            }
        }
    }

    ;

}
