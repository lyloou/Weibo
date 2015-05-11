package com.lyloou.weibo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyloou.weibo.R;
import com.lyloou.weibo.util.CommonUtil;
import com.lyloou.weibo.util.LU;
import com.lyloou.weibo.util.NetUtil;
import com.sina.weibo.sdk.openapi.models.Status;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by lilou on 2015/5/8.
 */
public class MyWeiboAdapter extends BaseAdapter {

    private List<Status> statuses;
    private LayoutInflater mInflater;


    public MyWeiboAdapter(Context context, List<Status> status) {
        this.statuses = status;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return statuses == null ? 0 : statuses.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return (Long.parseLong(statuses.get(position).id));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        Status s = statuses.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.home_item, null);

            //用户姓名
            holder.weiboUserName = (TextView) convertView.findViewById(R.id.id_home_item_user_name_tv);

            //用户图像
            holder.weiboUserPic = (ImageView) convertView.findViewById(R.id.id_home_item_user_head_iv);

            //微博内容
            holder.weiboContent = (TextView) convertView.findViewById(R.id.id_home_item_content_text_tv);

            //用户认证

            //微博发表时间
            holder.weiboTime = (TextView) convertView.findViewById(R.id.id_home_item_weibo_time_tv);

            //微博来源
            holder.weiboSource = (TextView) convertView.findViewById(R.id.id_home_item_weibo_source_tv);

            //微博图片
            if (!CommonUtil.isEmpty(s.thumbnail_pic)) {
                holder.weiboPic = (ImageView) convertView.findViewById(R.id.id_home_item_content_img_iv);
                holder.weiboPic.setVisibility(View.VISIBLE);
            }

            //微博转发
            //微博用户姓名+微博内容+微博图片
            if (s.retweeted_status != null) {
                holder.weiboForwarding = (LinearLayout) convertView.findViewById(R.id.id_home_item_forwarding_llyt);
                holder.weiboForwarding.setVisibility(View.VISIBLE);
                holder.weiboForwardingContent = (TextView) convertView.findViewById(R.id.id_home_item_forwarding_text_tv);
//                holder.weiboForwardingContent.setText(s.retweeted_status.text);
                if (!CommonUtil.isEmpty(s.retweeted_status.thumbnail_pic)) {
                    holder.weiboForwardingPic = (ImageView) convertView.findViewById(R.id.id_home_item_forwarding_img_iv);
                    holder.weiboForwardingPic.setVisibility(View.VISIBLE);
                }
            }


            //微博转发数
            holder.weiboForwardingCount = (TextView) convertView.findViewById(R.id.id_home_item_weibo_forwarding_count_tv);
            holder.weiboForwardingCountllyt = (LinearLayout) convertView.findViewById(R.id.id_home_item_weibo_forwarding_count_llyt);
            //微博评论数
            holder.weiboCommentsCount = (TextView) convertView.findViewById(R.id.id_home_item_weibo_forwarding_comment_count_tv);
            holder.weiboCommentsCountllyt = (LinearLayout) convertView.findViewById(R.id.id_home_item_weibo_forwarding_comment_count_llyt);

            //微博赞数
            holder.weiboPraiseCount = (TextView) convertView.findViewById(R.id.id_home_item_weibo_prise_count_tv);
            holder.weiboPraiseCountllyt = (LinearLayout) convertView.findViewById(R.id.id_home_item_weibo_prise_count_llyt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ////设值

        //用户姓名
        holder.weiboUserName.setText(s.user.name);
        holder.weiboUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LU.log("我被点击了哦");
            }
        });

        //用户图像
        CommonUtil.loadImageWithImgURLAndImageView(s.user.profile_image_url, holder.weiboUserPic);
        holder.weiboUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LU.log("我是头像哦");
            }
        });


        //微博内容
        holder.weiboContent.setText(s.text);

        //用户认证

        //微博发表时间
        Date oldDate = CommonUtil.strToDate(s.created_at);
        Date newDate = new Date(System.currentTimeMillis());
        String dateString = CommonUtil.getTimeStr(oldDate, newDate);
        holder.weiboTime.setText(dateString);

        //微博来源
        holder.weiboSource.setText(Html.fromHtml("<font color=\"#9b9b9b\">" + "来源:" + s.source + "</font>"));

        //微博图片

        if (!CommonUtil.isEmpty(s.thumbnail_pic) && holder.weiboPic != null) {
            CommonUtil.loadImageWithImgURLAndImageView(s.thumbnail_pic, holder.weiboPic);
        }

        //微博是否有转发
        //微博用户姓名+微博内容+微博图片
        if (s.retweeted_status != null && holder.weiboForwardingContent != null) {
            //微博转发内容
            holder.weiboForwardingContent.setText("@" + s.retweeted_status.user.name + ": " + s.retweeted_status.text);

            //微博转发图像
            if (!CommonUtil.isEmpty(s.retweeted_status.thumbnail_pic) && holder.weiboForwardingPic != null) {
                CommonUtil.loadImageWithImgURLAndImageView(s.retweeted_status.thumbnail_pic, holder.weiboForwardingPic);
            }
            holder.weiboForwarding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LU.log("我是转发区域");
                }
            });
        }

        //微博转发数
        if (s.reposts_count > 0) {
            holder.weiboForwardingCount.setText("" + s.reposts_count);
        }
        holder.weiboForwardingCountllyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LU.log("转发数");
            }
        });

        //微博评论数
        if (s.comments_count > 0) {
            holder.weiboCommentsCount.setText("" + s.comments_count);
        }
        holder.weiboCommentsCountllyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LU.log("评论数");
            }
        });

        //微博赞数
        if (s.attitudes_count > 0) {
            holder.weiboPraiseCount.setText("" + s.attitudes_count);
        }
        holder.weiboPraiseCountllyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LU.log("赞数");
            }
        });

        return convertView;
    }

    private class ViewHolder {
        //用户姓名
        TextView weiboUserName;

        //用户图像
        ImageView weiboUserPic;

        //微博内容
        TextView weiboContent;


        //用户认证

        //微博发表时间
        TextView weiboTime;
        //微博来源
        TextView weiboSource;
        //微博图片
        ImageView weiboPic;

        //微博是否有转发
        LinearLayout weiboForwarding;

        //微博用户姓名+微博内容+微博图片
        TextView weiboForwardingContent;
        ImageView weiboForwardingPic;

        //微博转发数
        TextView weiboForwardingCount;
        LinearLayout weiboForwardingCountllyt;

        //微博评论数
        TextView weiboCommentsCount;
        LinearLayout weiboCommentsCountllyt;

        //微博赞数
        TextView weiboPraiseCount;
        LinearLayout weiboPraiseCountllyt;
    }


}
