package com.lyloou.weibo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lyloou.weibo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageFragment extends BaseFragment {
    private List<Map<String, Object>> dataLists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message, null);

        ListView listView = (ListView) view.findViewById(R.id.id_message_lv);
        String[] textDatas = {"@我的", "评论", "赞","群组聊天","未关注人私信","公告"};
        dataLists = new ArrayList<>();
        int[] imgDatas = {R.mipmap.message_at, R.mipmap.message_comments,
                R.mipmap.message_good,R.mipmap.message_groupchat,
                R.mipmap.message_stranger,R.mipmap.message_notice};
        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                getDatas(textDatas, imgDatas),
                R.layout.message_item,
                new String[]{"pic", "text"},
                new int[]{R.id.id_message_item_iv, R.id.id_message_item_tv});

        listView.setAdapter(adapter);
        return view;
    }

    private List<Map<String, Object>> getDatas(String[] textData, int[] imgDatas) {

        for (int i = 0; i < textData.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("pic", imgDatas[i]);
            map.put("text", textData[i]);
            dataLists.add(map);
        }
        return dataLists;
    }

    @Override
    public String refresh(String str) {

        return "消息";
    }
}