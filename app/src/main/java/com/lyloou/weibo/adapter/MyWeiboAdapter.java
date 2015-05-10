package com.lyloou.weibo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lyloou.weibo.R;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.List;

/**
 * Created by lilou on 2015/5/8.
 */
public class MyWeiboAdapter extends BaseAdapter {

    private List<Status> status;
    private LayoutInflater mInflater;

    public MyWeiboAdapter(Context context,List<Status> status) {
        this.status = status;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return status==null?0:status.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return (Long.parseLong(status.get(position).id));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.home_item,null);
            holder.textUser = (TextView) convertView.findViewById(R.id.id_home_item_title_tv);
            holder.textWeibo = (TextView) convertView.findViewById(R.id.id_home_item_content_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Status s = status.get(position);
        holder.textUser.setText(s.user.name);
        holder.textWeibo.setText(s.text);
        return convertView;
    }

    private class ViewHolder {
        TextView textUser;
        TextView textWeibo;
    }
}
