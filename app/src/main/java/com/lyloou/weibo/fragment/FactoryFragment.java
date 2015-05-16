package com.lyloou.weibo.fragment;

import android.app.Fragment;


public class FactoryFragment {  
    public static Fragment getInstanceByIndex(int index) {  
        Fragment fragment = null;  
        switch (index) {  
            case 0:  
            	fragment = new HomeFragment();  
                break;  
            case 1:  
                fragment = new MineFragment();
                break;  
            case 2:  
                fragment = new MessageFragment();
                break;  
            case 3:  
            	fragment = new SettingFragment();
                break;  
        }  
        return fragment;  
    }  
}  