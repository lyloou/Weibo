package com.lyloou.weibo.activity;


import android.app.Fragment;

import com.lyloou.weibo.fragment.WeiboDetailFragment;
import com.lyloou.weibo.util.FragmentActivityUtil;

/**
 * Created by lilou on 2015/5/14.
 */
public class WeiboDetailActivity extends FragmentActivityUtil {
    @Override
    protected Fragment createFragment() {
        String weiboID = getIntent().getStringExtra(WeiboDetailFragment.ARGUMENT);
        return WeiboDetailFragment.newInstance(weiboID);
    }
}
