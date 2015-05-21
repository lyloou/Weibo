package com.lyloou.weibo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lyloou.weibo.R;
import com.lyloou.weibo.activity.WeiboDetailActivity;
import com.lyloou.weibo.adapter.WeiboMainAdapter;
import com.lyloou.weibo.constant.Constants;
import com.lyloou.weibo.constant.MyApplication;
import com.lyloou.weibo.util.LU;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

/**
 * Created by lilou on 2015/5/14.
 */
public class MineWeiboFragment extends BaseFragment {
    public static final String ARGUMENT = "mine_weibo_arg";
    private static int MAX_ID = 0;
    private StatusesAPI mStatusesAPI;
    private static final int ADDED_ITEM_COUNT = 15;
    private int indexOld;
    private int topY;
    private ListView listView;
    private TextView loadText;
    private Button loadMoreBtn;
    private ImageView backIV;
    private String userUID;


    public static MineWeiboFragment newInstance(String arg) {
        //数据传递
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, arg);
        MineWeiboFragment mineWeiboFragment = new MineWeiboFragment();
        mineWeiboFragment.setArguments(bundle);
        return mineWeiboFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userUID = bundle.getString(ARGUMENT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mine_weibo, container, false);
        //微博列表
        listView = (ListView) view.findViewById(R.id.id_mine_weibo_lv);
        //初始化时的加载页面
        loadText = (TextView) view.findViewById(R.id.id_mine_weibo_loadtext_tv);

        //加载更多按钮
        View loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.home_load_more, null);
        loadMoreBtn = (Button) loadMoreView.findViewById(R.id.id_home_load_more_btn);
        listView.addFooterView(loadMoreView);
        loadMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoreWeibo();

            }
        });

        //设置返回
        backIV = (ImageView) view.findViewById(R.id.id_mine_weibo_top_back_iv);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        //初始化微博
        loadNewWeibo();

        return view;
    }

    //重新加载
    private void loadNewWeibo() {
        MAX_ID = 15;
        mStatusesAPI = new StatusesAPI(getActivity(), Constants.APP_KEY, MyApplication.accessToken);
        mStatusesAPI.userTimeline(Long.parseLong(userUID), null, 0L, 0L, MAX_ID, 1, false, 0, false, new WeiboRequestListener());
    }

    //加载更多微博
    private void loadMoreWeibo() {
        MAX_ID = MAX_ID + ADDED_ITEM_COUNT;
        //加载更多的时候, 为了体验更好(不会重新跳转到顶部), 记录位置.
        indexOld = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        topY = (v == null) ? 0 : v.getTop();
        loadMoreBtn.setText("请稍后......");
        mStatusesAPI.userTimeline(Long.parseLong(userUID), null, 0L, 0L, MAX_ID, 1, false, 0, false, new WeiboRequestListener());
    }


    private class WeiboRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            LU.log("2-onCreateView");
            LU.log("结果是--: " + response);

			/*
            准备添加gson来解析获取的数据, 但是出错了.
			 */

            final StatusList statuses = StatusList.parse(response);


            if (statuses != null) {
                WeiboMainAdapter adapter = new WeiboMainAdapter(getActivity(), statuses.statusList);
                listView.setAdapter(adapter);
                loadText.setVisibility(View.GONE);
                if (MAX_ID > 15) {
                    listView.setSelectionFromTop(indexOld, topY);
                    loadMoreBtn.setText("加载更多......");
                    //弹出成功提示
                    if (statuses.total_number > 0) {
                        Toast.makeText(getActivity(),
                                "加载了 " + ADDED_ITEM_COUNT + " 条微博",
                                Toast.LENGTH_LONG).show();
                    }
                } else {

                    //表示是刷新或初始化;
                    listView.setVisibility(View.VISIBLE);
                }

                //设置监听事件, 点击item进入详细界面
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        LU.log("点击了第" + position + " 个哦!");
                        //将点击的微博发送到微博详细界面去
                        Status status = statuses.statusList.get(position);

                        Intent intent = new Intent(getActivity(), WeiboDetailActivity.class);
                        intent.putExtra(WeiboDetailFragment.ARGUMENT, status.id);
                        WeiboDetailFragment.statusStatic = status;
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    }

    @Override
    public String refresh(String str) {
        return null;
    }
}
