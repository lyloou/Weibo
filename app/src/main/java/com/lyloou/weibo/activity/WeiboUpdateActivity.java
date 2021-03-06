package com.lyloou.weibo.activity;


import android.app.Fragment;

import com.lyloou.weibo.fragment.WeiboUpdateFragment;
import com.lyloou.weibo.util.FragmentActivityUtil;

/**
 * Created by lilou on 2015/5/14.
 */
public class WeiboUpdateActivity extends FragmentActivityUtil {
    @Override
    protected Fragment createFragment() {
        String weiboID = getIntent().getStringExtra(WeiboUpdateFragment.ARGUMENT);
        return WeiboUpdateFragment.newInstance(weiboID);
    }
}
