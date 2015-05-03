package com.lyloou.weibo.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyloou.weibo.R;


public class HomeFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment, null);
		TextView textView = (TextView) view.findViewById(R.id.id_txt_content_tv);
		textView.setText(refresh(null));
		return view;
	}

	@Override
	public String refresh(String str) {

		return "主页";
	}

}
