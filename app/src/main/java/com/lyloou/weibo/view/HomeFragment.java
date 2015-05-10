package com.lyloou.weibo.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lyloou.weibo.R;
import com.lyloou.weibo.adapter.MyWeiboAdapter;
import com.lyloou.weibo.app.Constants;
import com.lyloou.weibo.app.LU;
import com.lyloou.weibo.app.MyApplication;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.StatusList;


public class HomeFragment extends BaseFragment {
    private StatusesAPI mStatusesAPI;
    private ListView listView;
    private TextView loadText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LU.TAG, "1-onCreateView");
        View view = inflater.inflate(R.layout.home, null);

//		TextView textView = (TextView) view.findViewById(R.id.id_txt_content_tv);
//		textView.setText(refresh(null));

        listView = (ListView) view.findViewById(R.id.id_home_lv);
        listView.setItemsCanFocus(false);
        loadText = (TextView) view.findViewById(R.id.id_home_loadtext_tv);
        mStatusesAPI = new StatusesAPI(getActivity(), Constants.APP_KEY, MyApplication.accessToken);
        mStatusesAPI.friendsTimeline(0L, 0L, 15, 1, false, 0, false, new WeiboRequestListener());

        return view;
    }

    @Override
    public String refresh(String str) {

        return "主页";
    }

    private class WeiboRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            Log.d(LU.TAG, "2-onCreateView");
            Log.d(LU.TAG, "结果是--: " + response);

			/*
            准备添加gson来解析获取的数据, 但是出错了.
			 */

            StatusList statuses = StatusList.parse(response);
            if (statuses != null && statuses.total_number > 0) {
                Toast.makeText(getActivity(),
                        "获取微博信息流成功, 条数: " + statuses.statusList.size(),
                        Toast.LENGTH_LONG).show();
            }


            MyWeiboAdapter adapter = new MyWeiboAdapter(getActivity(), statuses.statusList);
            listView.setAdapter(adapter);
            loadText.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
//			adapter.notifyDataSetChanged();
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    }
}
