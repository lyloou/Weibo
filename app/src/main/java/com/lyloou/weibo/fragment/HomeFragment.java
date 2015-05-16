package com.lyloou.weibo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lyloou.weibo.R;
import com.lyloou.weibo.adapter.WeiboMainAdapter;
import com.lyloou.weibo.constant.Constants;
import com.lyloou.weibo.util.CommonUtil;
import com.lyloou.weibo.util.LU;
import com.lyloou.weibo.constant.MyApplication;
import com.lyloou.weibo.activity.WeiboDetailActivity;
import com.lyloou.weibo.activity.WeiboUpdateActivity;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;


public class HomeFragment extends BaseFragment {
    // 设置点击「加载更多后」一次多少个微博
    private static final int ADDED_ITEM_COUNT = 15;

    private static int MAX_ID = 0;
    private ListView listView;
    private TextView loadText;
    private TextView userNameText;
    private User user;
    private ImageView refreshImg;
    private ImageView updateWeibo;
    private StatusesAPI mStatusesAPI;
    private Button loadMoreBtn;
    private int indexOld;
    private int topY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LU.log("1-onCreateView");
        View view = inflater.inflate(R.layout.home, null);

        listView = (ListView) view.findViewById(R.id.id_home_lv);
//        listView.setItemsCanFocus(false);

        //首页等待加载数据文字
        loadText = (TextView) view.findViewById(R.id.id_home_loadtext_tv);

        //首页用户名称
        userNameText = (TextView) view.findViewById(R.id.id_home_top_username_tv);

        //刷新
        refreshImg = (ImageView) view.findViewById(R.id.id_home_top_refresh_iv);
        refreshImg.setClickable(true);

        //写微博
        updateWeibo = (ImageView) view.findViewById(R.id.id_home_top_compose_iv);
        updateWeibo.setClickable(true);

        // 在Listview下面添加「加载更多」
        View loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.home_load_more, null);
        listView.addFooterView(loadMoreView);
        loadMoreBtn = (Button) loadMoreView.findViewById(R.id.id_home_load_more_btn);
        loadMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoreWeibo();

            }
        });

        //加载列表
        loadNewWeibo();

        // 加载用户姓名
        CommonUtil.setUserNameInTextView(getActivity(), MyApplication.accessToken, userNameText);


        //刷新列表
        refreshImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.refresh_anim);
                refreshImg.startAnimation(animation);
                loadNewWeibo();
            }
        });

        //写微博
        updateWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeiboUpdateActivity.class);
                startActivityForResult(intent,0);
            }
        });
        return view;
    }

    @Override
    public String refresh(String str) {

        return "主页";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            loadNewWeibo();
        }
    }

    //重新加载
    private  void  loadNewWeibo() {
        MAX_ID = 15;
        mStatusesAPI = new StatusesAPI(getActivity(), Constants.APP_KEY, MyApplication.accessToken);
        mStatusesAPI.friendsTimeline(0L, 0L, MAX_ID, 1, false, 0, false, new WeiboRequestListener());
    }

    //加载更多微博
    private void loadMoreWeibo() {
        MAX_ID = MAX_ID + ADDED_ITEM_COUNT;
        //加载更多的时候, 为了体验更好(不会重新跳转到顶部), 记录位置.
        indexOld = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        topY = (v == null) ? 0 : v.getTop();
        loadMoreBtn.setText("请稍后......");
        mStatusesAPI.friendsTimeline(0L, 0L, MAX_ID, 1, false, 0, false, new WeiboRequestListener());

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


            if(statuses!=null) {
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
                    //加载完成后,使刷新按钮停止.
                    refreshImg.clearAnimation();
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
}
