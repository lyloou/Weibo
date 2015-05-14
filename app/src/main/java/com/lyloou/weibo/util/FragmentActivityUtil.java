package com.lyloou.weibo.util;

/**
 * Created by lilou on 2015/5/14.
 */
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;


import com.lyloou.weibo.R;

public abstract class FragmentActivityUtil extends Activity
{
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        FragmentManager fm = getFragmentManager();
        Fragment fragment =fm.findFragmentById(R.id.id_fragment_container);

        if(fragment == null )
        {
            fragment = createFragment() ;

            fm.beginTransaction().add(R.id.id_fragment_container,fragment).commit();
        }
    }

}  
