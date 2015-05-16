package com.lyloou.weibo.activity;


import android.app.Fragment;

import com.lyloou.weibo.fragment.WeiboCommentFragment;
import com.lyloou.weibo.util.FragmentActivityUtil;

/**
 * Created by lilou on 2015/5/14.
 */
public class WeiboCommentActivity extends FragmentActivityUtil {
    @Override
    protected Fragment createFragment() {
        String weiboID = getIntent().getStringExtra(WeiboCommentFragment.ARGUMENT);
        return WeiboCommentFragment.newInstance(weiboID);
    }
}
