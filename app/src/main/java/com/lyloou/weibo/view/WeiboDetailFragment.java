package com.lyloou.weibo.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lyloou.weibo.R;
import com.lyloou.weibo.adapter.WeiboCommentAdapter;
import com.lyloou.weibo.app.Constants;
import com.lyloou.weibo.app.MyApplication;
import com.lyloou.weibo.util.CommonUtil;
import com.lyloou.weibo.util.LU;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.Status;

/**
 * Created by lilou on 2015/5/14.
 */
public class WeiboDetailFragment extends BaseFragment {
    private ListView commentLV;
    public static final String ARGUMENT = "weibo_id";
    // 使用static的方式传递数值.
    public static Status statusStatic;
    private Status mStatus;
    private TextView commentInit;
    private StatusesAPI mStatusesAPI;


    public static WeiboDetailFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, arg);
        WeiboDetailFragment weiboDetailFragment = new WeiboDetailFragment();
        weiboDetailFragment.setArguments(bundle);
        return weiboDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mStatusesAPI = new StatusesAPI(getActivity(), Constants.APP_KEY, MyApplication.accessToken);
        if (bundle != null) {
            //本来打算使用微博id来获取status的, 但是发现没有可以使用的接口, 就通过static的方式直接传递过来了.
            String weiboIDStr = bundle.getString(ARGUMENT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_item_detail, container, false);
        mStatus = WeiboDetailFragment.statusStatic;

        ImageView topBackIv = (ImageView) view.findViewById(R.id.id_home_item_detail_top_back_iv);
        topBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        //微博转发数
        TextView weiboForwardingCount = (TextView) view.findViewById(R.id.id_home_item_detail_weibo_forwarding_count_tv);
        LinearLayout weiboForwardingCountllyt = (LinearLayout) view.findViewById(R.id.id_home_item_detail_weibo_forwarding_count_llyt);
        weiboForwardingCountllyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LU.log("转发数");
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("转发中......");
                progressDialog.setCancelable(false);
                progressDialog.show();
                mStatusesAPI.repost(Long.parseLong(mStatus.id), null, 0, new RequestListener() {
                    @Override
                    public void onComplete(String s) {
                        LU.log("转发了一条微博" + s);
                        Toast.makeText(getActivity(), "转发成功", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {

                    }
                });
            }
        });

        //微博评论数
        TextView weiboCommentsCount = (TextView) view.findViewById(R.id.id_home_item_detail_weibo_forwarding_comment_count_tv);
        LinearLayout weiboCommentsCountllyt = (LinearLayout) view.findViewById(R.id.id_home_item_detail_weibo_forwarding_comment_count_llyt);
        weiboCommentsCountllyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LU.log("评论数");
            }
        });

        //微博赞数
        TextView weiboPraiseCount = (TextView) view.findViewById(R.id.id_home_item_detail_weibo_prise_count_tv);
        LinearLayout weiboPraiseCountllyt = (LinearLayout) view.findViewById(R.id.id_home_item_detail_weibo_prise_count_llyt);
        weiboPraiseCountllyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LU.log("赞数");
            }
        });

        if (mStatus != null) {
            if (mStatus.reposts_count > 0) {
//                weiboForwardingCount.setText("" + mStatus.reposts_count);
            }
            if (mStatus.comments_count > 0) {
//                weiboCommentsCount.setText("" + mStatus.comments_count);
            }
            if (mStatus.attitudes_count > 0) {
                weiboPraiseCount.setText("" + mStatus.attitudes_count);
            }


            commentLV = (ListView) view.findViewById(R.id.home_item_detail_comment_lv);
            commentInit = (TextView) view.findViewById(R.id.id_home_item_detail_content_init_tv);
            CommonUtil.useListViewInScrollView(commentLV);

            //评论列表展示
            CommentsAPI commentsAPI = new CommentsAPI(getActivity(), Constants.APP_KEY, MyApplication.accessToken);
            commentsAPI.show(Long.parseLong(mStatus.id), 0, 0, 15, 1, 0, new RequestListener() {
                @Override
                public void onComplete(String s) {
                    CommentList commentList = CommentList.parse(s);
                    if (commentList != null) {
                        WeiboCommentAdapter adapter = new WeiboCommentAdapter(getActivity(), commentList.commentList);
                        commentLV.setAdapter(adapter);
                        commentLV.setVisibility(View.VISIBLE);
                        commentInit.setVisibility(View.GONE);
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
        return null;
    }
}
