package com.lyloou.weibo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyloou.weibo.R;
import com.lyloou.weibo.constant.Constants;
import com.lyloou.weibo.constant.MyApplication;
import com.lyloou.weibo.util.CommonUtil;
import com.lyloou.weibo.util.LU;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.models.Status;

/**
 * Created by lilou on 2015/5/14.
 */
public class WeiboCommentFragment extends BaseFragment {
    public static final String ARGUMENT = "weibo_id";
    //设置限制字数
    public static final int MAX_TEXT_NUM = 140;
    // 使用static的方式传递数值.
    public static Status statusStatic;
    private TextView cancelTv;
    private TextView sendTv;
    private EditText contentET;
    private LinearLayout tipLlyt;
    private TextView tipTv;
    private TextView userName;



    public static WeiboCommentFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, arg);
        WeiboCommentFragment weiboCommentFragment = new WeiboCommentFragment();
        weiboCommentFragment.setArguments(bundle);
        return weiboCommentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String weiboIDStr = bundle.getString(ARGUMENT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_coment, container, false);
        cancelTv = (TextView) view.findViewById(R.id.id_home_coment_top_cancel_tv);
        sendTv = (TextView) view.findViewById(R.id.id_home_coment_top_send_tv);
        contentET = (EditText) view.findViewById(R.id.id_home_coment_content_et);
        tipLlyt = (LinearLayout) view.findViewById(R.id.id_home_coment_bottom_tip_llyt);
        tipTv = (TextView) view.findViewById(R.id.id_home_coment_bottom_tip_tv);
        userName = (TextView)view.findViewById(R.id.id_home_top_username_tv);
        tipTv.setText("还可输入" + MAX_TEXT_NUM + "字");

        // 设置用户姓名
        CommonUtil.setUserNameInTextView(getActivity(), MyApplication.accessToken, userName);

        //取消
        cancelTv.setClickable(true);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        //设置「发送」是否可点击和颜色
        //默认设置不可点击
        sendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentsAPI mCommentsAPI = new CommentsAPI(getActivity(), Constants.APP_KEY, MyApplication.accessToken);
                String content = contentET.getText().toString();
                LU.log("我是发送按钮." + content);
                mCommentsAPI.create(content, Long.parseLong(statusStatic.id), false, new RequestListener() {
                    @Override
                    public void onComplete(String s) {
                        LU.log("已经发送." + s);
                        Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {

                    }
                });

            }
        });

        contentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //字数控制以及「发送」按钮控制.
                int sLen = s.toString().length();
                int num;

                if (sLen > 0) {
                    num = MAX_TEXT_NUM - sLen;
                    sendTvIsOrNotClickable(true);
                    tipTv.setText("还可输入" + num + "字");
                    tipTv.setTextColor(Color.rgb(149, 149, 149));
                } else {
                    sendTvIsOrNotClickable(false);
                }
                if(sLen ==0){
                    tipTv.setText("还可输入" + 140 + "字");
                    sendTvIsOrNotClickable(false);
                    tipTv.setTextColor(Color.rgb(149, 149, 149));
                }
                if (sLen > MAX_TEXT_NUM) {
                    num = sLen - MAX_TEXT_NUM;
                    tipTv.setText("您已超了" + num + "字");
                    tipTv.setTextColor(Color.rgb(222,22,22));
                    sendTvIsOrNotClickable(false);
                }
            }
        });
        return view;
    }

    void sendTvIsOrNotClickable(boolean isOrNot) {
        if (isOrNot) {
            sendTv.setClickable(true);
            sendTv.setTextColor(Color.rgb(239, 170, 78));
        } else {
            sendTv.setClickable(false);
            sendTv.setTextColor(Color.rgb(149, 149, 149));
        }
    }

    @Override
    public String refresh(String str) {
        return null;
    }
}
