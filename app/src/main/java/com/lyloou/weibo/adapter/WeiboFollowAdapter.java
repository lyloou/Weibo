package com.lyloou.weibo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyloou.weibo.R;
import com.lyloou.weibo.util.CommonUtil;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.List;

/**
 * Created by lilou on 2015/5/8.
 */
public class WeiboFollowAdapter extends BaseAdapter {

    private List<User> userList;
    private LayoutInflater mInflater;
    private User user;
    private Context mContext;

    public WeiboFollowAdapter(Context context, List<User> users) {
        this.userList = users;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }


    @Override
    public int getCount() {
        return userList == null ? 1 : userList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;

    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(user.id);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //很不错 这个地方的holderView不会出现WeiboMainAdapter中的问题, 高兴..哈哈哈.
        ViewHolder holder;
        user = userList.get(position);
        if (convertView == null || convertView.getTag() == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.mine_follow_item, parent, false);

            //初始化
            holder.userName = (TextView) convertView.findViewById(R.id.id_mine_follow_item_user_name_tv);
            holder.userHead = (ImageView) convertView.findViewById(R.id.id_mine_follow_item_user_head_iv);
            holder.userDesc = (TextView) convertView.findViewById(R.id.id_mine_follow_item_weibo_content_tv);


            ////设值
            // 用户名
            holder.userName.setText(user.name);

            //用户图像
            CommonUtil.loadImageWithImgURLAndImageView(user.profile_image_url, holder.userHead);

            //个人描述
            holder.userDesc.setText("个人描述：" + user.description);
        }
        return convertView;

    }

    private class ViewHolder {

        //用户名
        TextView userName;

        //用户图像
        ImageView userHead;

        //个人描述
        TextView userDesc;
    }


}
