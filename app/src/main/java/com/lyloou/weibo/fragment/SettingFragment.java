package com.lyloou.weibo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lyloou.weibo.R;
import com.lyloou.weibo.activity.LoginActivity;
import com.lyloou.weibo.constant.MyApplication;
import com.lyloou.weibo.util.AccessTokenKeeper;
import com.lyloou.weibo.util.LU;

public class SettingFragment extends BaseFragment implements View.OnClickListener {
	private LinearLayout exitLLyt;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting, null);
		exitLLyt = (LinearLayout)view.findViewById(R.id.id_setting_exit_llyt);
		exitLLyt.setOnClickListener(this);
		return view;
	}

	@Override
	public String refresh(String str) {

		return "设置";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.id_setting_exit_llyt:
				LU.log("注销当前用户。");
				AccessTokenKeeper.clear(getActivity());
				getActivity().finish();
				break;
		}
	}
}