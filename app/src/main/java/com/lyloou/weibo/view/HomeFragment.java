package com.lyloou.weibo.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lyloou.weibo.R;
import com.lyloou.weibo.app.Constants;
import com.lyloou.weibo.app.LogUtil;
import com.lyloou.weibo.app.MyApplication;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;


public class HomeFragment extends BaseFragment {
	StatusesAPI api;
	ListView listView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.d(LogUtil.TAG,"HomeFragment-onCreateView");
		View view = inflater.inflate(R.layout.home, null);

//		TextView textView = (TextView) view.findViewById(R.id.id_txt_content_tv);
//		textView.setText(refresh(null));

		listView = (ListView) view.findViewById(R.id.id_home_lv);
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
			Log.d(LogUtil.TAG,"结果是--: "+s);

			/*
			准备添加gson来解析获取的数据, 但是出错了.
			 */
			/*
			Gson gson = new Gson();
			WeiboItem weiboitem = gson.fromJson(s, WeiboItem.class);

			MyWeiboAdapter adapter = new MyWeiboAdapter(getActivity(),weiboitem.getStatuses());
			listView.setAdapter(adapter);
*/

		}

		@Override
		public void onWeiboException(WeiboException e) {

		}
	}
}
