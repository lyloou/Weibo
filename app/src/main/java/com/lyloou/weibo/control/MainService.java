package com.lyloou.weibo.control;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.lyloou.weibo.model.Task;
import com.lyloou.weibo.view.IWeiboActivity;

public class MainService extends Service implements Runnable {
	
	
	
	private static final String TAG = "Weibo";
	private static Queue<Task> mTasks =new LinkedList<Task>();;
	private static ArrayList<Activity> mLists = new ArrayList<Activity>();;
	private boolean flag;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Task.WEIBO_LOGIN:
				Log.d(TAG, "������case" +"msg��ֵ��"+msg.obj.toString());
				IWeiboActivity activity = (IWeiboActivity)getActivityByName("com.lyloou.weibo.view.LoginActivity");
				activity.refresh(msg.obj);
				break;
				
			}
		};
	};
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d(TAG, "ִ����onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "ֹͣ�˷���,ִ����onDestroy");
		super.onDestroy();
	}

	@Override
	public void run() {
		Task task;
		while(flag){
			task = mTasks.poll();
			if(task!=null){
				
				doTask(task);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected Activity getActivityByName(String string) {
		Log.d(TAG, "������getActivityByName");
		if(mLists!=null){
			for(Activity activity : mLists){
				if(activity.getClass().getName().indexOf(string)>0){
					Log.d(TAG, "ȥ����ֵActivity");
					return activity;
				}
			}
		}
		Log.d(TAG, "����û��ȥ��ֵ��getActivityByName");
		return null;
	}

	public static void addActivity(Activity activity){
		mLists.add(activity);
	}
	
	// ��������
	private void doTask(Task task) {
		Log.d(TAG, "ִ����һ������");
		Message msg = handler.obtainMessage();
		msg.what = task.getTaskId();
		msg.obj = "��½�ɹ�";
		msg.sendToTarget();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "MainServiceִ����");
		flag = true;
		new Thread(this).start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public static void newTask(Task t) {
		mTasks.add(t);
	}

}
