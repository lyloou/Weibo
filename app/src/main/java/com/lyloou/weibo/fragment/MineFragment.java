package com.lyloou.weibo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyloou.weibo.R;

public class MineFragment extends BaseFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine, null);
        return view;
    }

    @Override
    public String refresh(String str) {

        return "我的";
    }
}
