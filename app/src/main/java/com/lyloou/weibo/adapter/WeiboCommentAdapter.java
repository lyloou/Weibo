package com.lyloou.weibo.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyloou.weibo.R;
import com.lyloou.weibo.util.CommonUtil;
import com.lyloou.weibo.util.LU;
import com.lyloou.weibo.activity.WeiboDetailActivity;
import com.lyloou.weibo.fragment.WeiboDetailFragment;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.Date;
import java.util.List;

/**
 * Created by lilou on 2015/5/8.
 */
public class WeiboCommentAdapter extends BaseAdapter {

    private List<Comment> commentList;
    private LayoutInflater mInflater;
    private Status mStatus;
    private Context mContext;

    public WeiboCommentAdapter(Context context, List<Comment> status) {
        this.commentList = status;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }


    @Override
    public int getCount() {
        return commentList == null ? 1 : commentList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0)
            return 0;
        else
            return position;

    }

    @Override
    public long getItemId(int position) {
        if (position == 0)
            return 0;
        else
            return (Long.parseLong(commentList.get(position - 1).id));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            convertView = mInflater.inflate(R.layout.home_item_detail_main, null);

            mStatus = WeiboDetailFragment.statusStatic;


            //用户姓名
            TextView weiboUserName = (TextView) convertView.findViewById(R.id.id_home_item_detail_user_name_tv);
            weiboUserName.setText(mStatus.user.name);
            weiboUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LU.log("我被点击了哦");
                }
            });

            //用户图像
            ImageView weiboUserPic = (ImageView) convertView.findViewById(R.id.id_home_item_detail_user_head_iv);
            CommonUtil.loadImageWithImgURLAndImageView(mStatus.user.profile_image_url, weiboUserPic);
            weiboUserPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LU.log("我是头像哦");
                }
            });

            //微博内容
            TextView weiboContent = (TextView) convertView.findViewById(R.id.id_home_item_detail_content_text_tv);
            weiboContent.setText(Html.fromHtml(CommonUtil.atBlue(mStatus.text)));

            //用户认证

            //微博发表时间
            TextView weiboTime = (TextView) convertView.findViewById(R.id.id_home_item_detail_weibo_time_tv);
            Date oldDate = CommonUtil.strToDate(mStatus.created_at);
            Date newDate = new Date(System.currentTimeMillis());
            String dateString = CommonUtil.getTimeStr(oldDate, newDate);
            weiboTime.setText(dateString);

            //微博来源
            TextView weiboSource = (TextView) convertView.findViewById(R.id.id_home_item_detail_weibo_source_tv);
            weiboSource.setText(Html.fromHtml("<font color=\"#9b9b9b\">" + "来源:" + mStatus.source + "</font>"));

            //微博图片
            if (!CommonUtil.isEmpty(mStatus.bmiddle_pic)) {
                ImageView weiboPic = (ImageView) convertView.findViewById(R.id.id_home_item_detail_content_img_iv);
                weiboPic.setVisibility(View.VISIBLE);
                CommonUtil.loadImageWithImgURLAndImageView(mStatus.bmiddle_pic, weiboPic);
            }


            LinearLayout weiboForwarding = null;
            ImageView weiboForwardingPic = null;
            TextView weiboForwardingContent = null;
            //微博转发
            //微博用户姓名+微博内容+微博图片
            if (mStatus.retweeted_status != null) {
                weiboForwarding = (LinearLayout) convertView.findViewById(R.id.id_home_item_detail_forwarding_llyt);
                weiboForwarding.setVisibility(View.VISIBLE);
                weiboForwardingContent = (TextView) convertView.findViewById(R.id.id_home_item_detail_forwarding_text_tv);
//                 weiboForwardingContent.setText(s.retweeted_status.text);
                if (!CommonUtil.isEmpty(mStatus.retweeted_status.thumbnail_pic)) {
                    weiboForwardingPic = (ImageView) convertView.findViewById(R.id.id_home_item_detail_forwarding_img_iv);
                    weiboForwardingPic.setVisibility(View.VISIBLE);
                }
            }
            if (mStatus.retweeted_status != null && weiboForwardingContent != null) {
                //微博转发内容
                String strForwarding = "@" + mStatus.retweeted_status.user.name + ": " + mStatus.retweeted_status.text;
                weiboForwardingContent.setText(Html.fromHtml(CommonUtil.atBlue(strForwarding)));

                //微博转发图像
                if (!CommonUtil.isEmpty(mStatus.retweeted_status.thumbnail_pic) && weiboForwardingPic != null) {
                    CommonUtil.loadImageWithImgURLAndImageView(mStatus.retweeted_status.thumbnail_pic, weiboForwardingPic);
                }
                weiboForwarding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LU.log("我是转发区域");

                        Status statusRetweeted = mStatus.retweeted_status;
                        Intent intent = new Intent(mContext, WeiboDetailActivity.class);
                        intent.putExtra(WeiboDetailFragment.ARGUMENT, mStatus.id);
                        WeiboDetailFragment.statusStatic = statusRetweeted;
                        mContext.startActivity(intent);
                    }
                });
            }
            return convertView;
        }

        //很不错 这个地方的holderView不会出现WeiboMainAdapter中的问题, 高兴..哈哈哈.
        ViewHolder holder;
        Comment comment = commentList.get(position-1);
        if (convertView == null || convertView.getTag() == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.home_item_detail_comment_item, parent,false);

            //评论楼层
            holder.floorNum = (TextView) convertView.findViewById(R.id.id_home_item_detail_comment_item_floor_tv);

            //用户姓名
            holder.commentUserName = (TextView) convertView.findViewById(R.id.id_home_item_detail_comment_item_user_name_tv);


            //评论内容
            holder.commentContent = (TextView) convertView.findViewById(R.id.id_home_item_detail_comment_item_content_tv);


            //评论时间
            holder.commentTime = (TextView) convertView.findViewById(R.id.id_home_item_detail_comment_item_time_tv);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ////设值
        //评论楼层
        holder.floorNum.setText("#" + position + "楼");

        //用户姓名
        holder.commentUserName.setText(comment.user.name);
        holder.commentUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LU.log("我被点击了哦");
            }
        });

        //微博内容
        holder.commentContent.setText(Html.fromHtml(CommonUtil.atBlue(comment.text)));

        //微博发表时间
        Date oldDate = CommonUtil.strToDate(comment.created_at);
        Date newDate = new Date(System.currentTimeMillis());
        String dateString = CommonUtil.getTimeStr(oldDate, newDate);
        holder.commentTime.setText(dateString);

        return convertView;

    }

    private class ViewHolder {

        //用户楼层号
        TextView floorNum;

        //用户姓名
        TextView commentUserName;


        //评论内容
        TextView commentContent;


        //评论时间
        TextView commentTime;


    }


}
