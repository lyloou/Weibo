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
import com.lyloou.weibo.view.WeiboDetailActivity;
import com.lyloou.weibo.view.WeiboDetailFragment;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.Date;
import java.util.List;

/**
 * Created by lilou on 2015/5/8.
 */
public class WeiboMainAdapter extends BaseAdapter {

    private List<Status> mStatuses;
    private LayoutInflater mInflater;
    private Context mContext;
    private Status mStatus;
    private Status mRetweedStatus;


    public WeiboMainAdapter(Context context, List<Status> statuses) {
        this.mStatuses = statuses;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mStatuses == null ? 0 : mStatuses.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return (Long.parseLong(mStatuses.get(position).id));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //异步问题导致的图片和部分文字错位,这里借用convertView=null强制重新加载布局的方式. 待优化.
        ViewHolder holder;
        mStatus = mStatuses.get(position);

        //获得微博图片地址
        String weiboPicURL = mStatus.thumbnail_pic;
        //判断图片地址是否存在
        boolean isExistedWeiboPic = !CommonUtil.isEmpty(weiboPicURL);

        //判断是否有转发

        String weiboRetweetedPicURL = null;
        mRetweedStatus = mStatus.retweeted_status;
        boolean isExistedRetweeted = (mRetweedStatus != null);
        boolean isExistedWeiboRetweetedPic = false;
        if (isExistedRetweeted) {
            //转发图片地址
            weiboRetweetedPicURL = mRetweedStatus.thumbnail_pic;
            //是否存在转发图片
            isExistedWeiboRetweetedPic = (!CommonUtil.isEmpty(weiboRetweetedPicURL));
        }

        //TODO 怎样才能解决convert不这样设置, 微博不乱套, 图片不乱套呢?????, 这个问题先搁着, 回头要解决.
        convertView = null;
        if (convertView == null || convertView.getTag() == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.home_item, parent, false);

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
            if (isExistedWeiboPic) {
                holder.weiboPic = (ImageView) convertView.findViewById(R.id.id_home_item_content_img_iv);
            }

            //微博转发
            //微博用户姓名+微博内容+微博图片
            if (isExistedRetweeted) {
                holder.weiboForwarding = (LinearLayout) convertView.findViewById(R.id.id_home_item_forwarding_llyt);
                holder.weiboForwardingContent = (TextView) convertView.findViewById(R.id.id_home_item_forwarding_text_tv);
                if (isExistedWeiboRetweetedPic) {
                    holder.weiboForwardingPic = (ImageView) convertView.findViewById(R.id.id_home_item_forwarding_img_iv);

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
        holder.weiboUserName.setText(mStatus.user.name);
        holder.weiboUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LU.log("我被点击了哦");
            }
        });

        //用户图像
        CommonUtil.loadImageWithImgURLAndImageView(mStatus.user.profile_image_url, holder.weiboUserPic);
        holder.weiboUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LU.log("我是头像哦");
            }
        });


        //微博内容
        holder.weiboContent.setText(Html.fromHtml(CommonUtil.atBlue(mStatus.text)));

        //用户认证

        //微博发表时间
        Date oldDate = CommonUtil.strToDate(mStatus.created_at);
        Date newDate = new Date(System.currentTimeMillis());
        String dateString = CommonUtil.getTimeStr(oldDate, newDate);
        holder.weiboTime.setText(dateString);

        //微博来源
        holder.weiboSource.setText(Html.fromHtml("<font color=\"#9b9b9b\">" + "来源:" + mStatus.source + "</font>"));

        //微博图片

        if (isExistedWeiboPic && holder.weiboPic != null) {
            //有图片的话现将默认图片放过去
            holder.weiboPic.setVisibility(View.VISIBLE);
            CommonUtil.loadImageWithImgURLAndImageView(weiboPicURL, holder.weiboPic);
        }

        //微博是否有转发
        //微博用户姓名+微博内容+微博图片
        if (isExistedRetweeted ) {
            holder.weiboForwarding.setVisibility(View.VISIBLE);
            //微博转发内容
            String strForwarding = "@" + mRetweedStatus.user.name + ": " + mRetweedStatus.text;
            holder.weiboForwardingContent.setText(Html.fromHtml(CommonUtil.atBlue(strForwarding)));

            //微博转发图像
            if (isExistedWeiboRetweetedPic) {
                holder.weiboForwardingPic.setVisibility(View.VISIBLE);
                CommonUtil.loadImageWithImgURLAndImageView(weiboRetweetedPicURL, holder.weiboForwardingPic);
            }
/*
            //TODO 又一个问题, 如何才能解决有的转发点击不了呢
            //设置转发区域点击事件--跳转到转发微博的详细界面
            holder.weiboForwarding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LU.log("我是转发区域");
                    Intent intent = new Intent(mContext, WeiboDetailActivity.class);
                    intent.putExtra(WeiboDetailFragment.ARGUMENT, mStatus.id);
                    WeiboDetailFragment.statusStatic = mRetweedStatus;
                    mContext.startActivity(intent);
                }
            });
            */
        }

        //微博转发数
        if (mStatus.reposts_count > 0) {
            holder.weiboForwardingCount.setText("" + mStatus.reposts_count);
        }
        holder.weiboForwardingCountllyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LU.log("转发数");
            }
        });

        //微博评论数
        if (mStatus.comments_count > 0) {
            holder.weiboCommentsCount.setText("" + mStatus.comments_count);
        }
        holder.weiboCommentsCountllyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LU.log("评论数");
            }
        });

        //微博赞数
        if (mStatus.attitudes_count > 0) {
            holder.weiboPraiseCount.setText("" + mStatus.attitudes_count);
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
