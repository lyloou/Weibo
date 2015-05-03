package com.lyloou.weibo.view;

import android.app.Fragment;


public class FactoryFragment {  
    public static Fragment getInstanceByIndex(int index) {  
        Fragment fragment = null;  
        switch (index) {  
            case 0:  
            	fragment = new HomeFragment();  
                break;  
            case 1:  
                fragment = new TradeRecordGVFragment();  
                break;  
            case 2:  
                fragment = new MoneyRecordFragment();  
                break;  
            case 3:  
            	fragment = new CustomerServiceFragment();  
                break;  
        }  
        return fragment;  
    }  
}  