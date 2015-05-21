package com.lyloou.weibo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyloou.weibo.R;
import com.lyloou.weibo.activity.MineFollowActivity;
import com.lyloou.weibo.activity.MineWeiboActivity;
import com.lyloou.weibo.constant.Constants;
import com.lyloou.weibo.constant.MyApplication;
import com.lyloou.weibo.util.CommonUtil;
import com.lyloou.weibo.util.LU;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private Oauth2AccessToken accessToken;
    private User user;
    private ImageView userHeadIV;
    private TextView userNameTV;
    private TextView userDescTV;
    private TextView countWeiboTV;
    private TextView countFollowTV;
    private TextView countfansTV;
    private LinearLayout userInfoLLyt;
    private LinearLayout countWeiboLLyt;
    private LinearLayout countFollwerLLyt;
    private LinearLayout countFansLLyt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine, container, false);

        //用户图像
        userHeadIV = (ImageView) view.findViewById(R.id.id_mine_user_head_iv);

        //用户名
        userNameTV = (TextView) view.findViewById(R.id.id_mine_user_name_tv);

        //用户简介
        userDescTV = (TextView) view.findViewById(R.id.id_mine_user_description_tv);

        //微博数
        countWeiboTV = (TextView) view.findViewById(R.id.id_mine_count_weibo_tv);

        //关注数
        countFollowTV = (TextView) view.findViewById(R.id.id_mine_count_follow_tv);

        //粉丝数
        countfansTV = (TextView) view.findViewById(R.id.id_mine_count_fans_tv);

        //初始化控件
        userInfoLLyt = (LinearLayout)view.findViewById(R.id.id_mine_user_info_llyt);
        countWeiboLLyt = (LinearLayout)view.findViewById(R.id.id_mine_count_weibo_llyt);
        countFollwerLLyt= (LinearLayout)view.findViewById(R.id.id_mine_count_follow_llyt);
        countFansLLyt= (LinearLayout)view.findViewById(R.id.id_mine_count_fans_llyt);

        //设置控件点击事件
        countWeiboLLyt.setOnClickListener(this);
        countFollwerLLyt.setOnClickListener(this);
        countFansLLyt.setOnClickListener(this);
        userInfoLLyt.setOnClickListener(this);

        accessToken = MyApplication.accessToken;

        if (accessToken == null || TextUtils.isEmpty(accessToken.getToken())) {
        }

        if (accessToken != null && !TextUtils.isEmpty(accessToken.getToken())) {
            //已经登陆过的话，就去加载用户信息。
            UsersAPI usersAPI = new UsersAPI(getActivity(), Constants.APP_KEY, accessToken);
            usersAPI.show(Long.parseLong(accessToken.getUid()), new RequestListener() {
                @Override
                public void onComplete(String response) {

                    user = User.parse(response);
                    String userName = null;
                    if (user != null) {
                        userName = user.name;

                        //更换用户名称
                        userNameTV.setText(userName);
                        //根据用户图像url来获取用户图像drawable,新启动一个线程;
                        CommonUtil.loadImageWithImgURLAndImageView(user.avatar_large, userHeadIV);
                        //用户描述
                        userDescTV.setText("简介："+user.description);

                        //微博数
                        countWeiboTV.setText(user.statuses_count+"");

                        //关注数
                        countFollowTV.setText(user.friends_count+"");

                        //粉丝数
                        countfansTV.setText(user.followers_count+"");
                    }


                }

                @Override
                public void onWeiboException(WeiboException e) {

                }
            });


        }
        return view;
    }

    @Override
    public String refresh(String str) {

        return "我的";
    }

    //设置点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_mine_user_info_llyt:
                LU.log(v.getId() + " userInfo被点击了");

                break;
            case R.id.id_mine_count_weibo_llyt:
                LU.log(v.getId()+" weibo被点击了");
                Intent intent = new Intent(getActivity(), MineWeiboActivity.class);
                intent.putExtra(MineWeiboFragment.ARGUMENT,user.id);
                startActivity(intent);
                break;
            case R.id.id_mine_count_follow_llyt:
                LU.log(v.getId()+" follow被点击了");
                Intent intent2 = new Intent(getActivity(), MineFollowActivity.class);
                intent2.putExtra(MineFollowFragment.ARGUMENT, user.id);
                startActivity(intent2);
                break;
            case R.id.id_mine_count_fans_llyt:
                LU.log(v.getId()+" fans被点击了");
                break;
        }
    }
}
