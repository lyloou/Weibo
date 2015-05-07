package com.lyloou.weibo.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyloou.weibo.R;
import com.lyloou.weibo.app.*;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;


public class HomeFragment extends BaseFragment {
	StatusesAPI api;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.d(LogUtil.TAG,"HomeFragment-onCreateView");
		View view = inflater.inflate(R.layout.fragment, null);
		TextView textView = (TextView) view.findViewById(R.id.id_txt_content_tv);
		textView.setText(refresh(null));

		api = new StatusesAPI(getActivity(), Constants.APP_KEY,MyApplication.accessToken);
		api.friendsTimeline(0,0,15,1,false,0,false,new WeiboRequestListener());

		return view;
	}

	@Override
	public String refresh(String str) {

		return "主页";
	}

	private class WeiboRequestListener implements RequestListener {
		@Override
		public void onComplete(String s) {
			Log.d(LogUtil.TAG,"HomeFragment-onCreateView");
			Log.d(LogUtil.TAG,s);
		}

		@Override
		public void onWeiboException(WeiboException e) {

		}
	}
}
