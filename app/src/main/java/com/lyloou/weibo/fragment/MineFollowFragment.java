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
import com.lyloou.weibo.adapter.WeiboFollowAdapter;
import com.lyloou.weibo.adapter.WeiboMainAdapter;
import com.lyloou.weibo.constant.Constants;
import com.lyloou.weibo.constant.MyApplication;
import com.lyloou.weibo.util.LU;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.UserList;

/**
 * Created by lilou on 2015/5/14.
 */
public class MineFollowFragment extends BaseFragment {
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
    private int index = 0;


    public static MineFollowFragment newInstance(String arg) {
        //数据传递
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, arg);
        MineFollowFragment mineWeiboFragment = new MineFollowFragment();
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

        View view = inflater.inflate(R.layout.mine_follow, container, false);
        //微博列表
        listView = (ListView) view.findViewById(R.id.id_mine_follow_lv);
        //初始化时的加载页面
        loadText = (TextView) view.findViewById(R.id.id_mine_follow_loadtext_tv);

        backIV = (ImageView) view.findViewById(R.id.id_mine_follow_top_back_iv);

        final FriendshipsAPI friendshipsAPI = new FriendshipsAPI(getActivity(),Constants.APP_KEY,MyApplication.accessToken);
        friendshipsAPI.friends(Long.parseLong(userUID),50,0,false,new WeiboRequestListener());

        //加载更多按钮
        View loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.home_load_more, null);
        loadMoreBtn = (Button) loadMoreView.findViewById(R.id.id_home_load_more_btn);
        listView.addFooterView(loadMoreView);

        loadMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendshipsAPI.friends(Long.parseLong(userUID),index=index+50,0,false,new WeiboRequestListener());
            }
        });

        //设置返回
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        //初始化微博

        return view;
    }



    private class WeiboRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            LU.log("2-onCreateView");
            LU.log("结果是--: " + response);

            //获取数据源
            UserList userList = UserList.parse(response);
            //设置adapter
            if(userList!=null) {
                WeiboFollowAdapter adapter = new WeiboFollowAdapter(getActivity(), userList.userArrayList);
                listView.setAdapter(adapter);
                loadText.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getActivity(),"多谢点击"+position,Toast.LENGTH_SHORT).show();
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
