package com.lyloou.weibo.view;


import android.app.Fragment;

import com.lyloou.weibo.util.FragmentActivityUtil;
import com.sina.weibo.sdk.openapi.models.Status;

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
