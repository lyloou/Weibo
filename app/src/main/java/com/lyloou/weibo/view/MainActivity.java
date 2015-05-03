package com.lyloou.weibo.view;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyloou.weibo.R;

public class MainActivity extends Activity implements OnClickListener {
    private LinearLayout mTabBtnHome;
    private LinearLayout mTabBtnTradeRecord;
    private LinearLayout mTabBtnMoneyRecord;
    private LinearLayout mTabBtnCustomerService;
    private FragmentManager fragmentManager;
    private ImageButton trade_record_ibtn;

    private Fragment mHomeFragment;
    private Fragment mTradeRecordFragment;
    private Fragment mCustomerServiceFragment;
    private Fragment mMoneyRecordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		/* 取消显示title栏目, 稍后自定义 */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity);
        initView();

		/* 设置默认fragment */
        fragmentManager = getFragmentManager();

        // 默认「主页」被选中
        selectTab(0);

    }

    /* 为防止按错键(back退出), 添加「再按一次退出程序」的提醒功能 */
    private long exitTime = 0; // 不能作为局部变量, 每一次点击都会重新赋值为0, 所以是退不出的.

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /* 初始化底部视图和点击监听器 */
    private void initView() {
        mTabBtnHome = (LinearLayout) findViewById(R.id.id_glb_bottom_home_llyt);
        mTabBtnTradeRecord = (LinearLayout) findViewById(R.id.id_glb_bottom_trade_record_llyt);
        mTabBtnMoneyRecord = (LinearLayout) findViewById(R.id.id_glb_bottom_money_record_llyt);
        mTabBtnCustomerService = (LinearLayout) findViewById(R.id.id_glb_bottom_customer_service_llyt);

        mTabBtnHome.setOnClickListener(this);
        mTabBtnTradeRecord.setOnClickListener(this);
        mTabBtnMoneyRecord.setOnClickListener(this);
        mTabBtnCustomerService.setOnClickListener(this);
    }

    // 设置点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_glb_bottom_home_llyt:
                selectTab(0);
                break;

            case R.id.id_glb_bottom_trade_record_llyt:
                selectTab(1);
                break;

            case R.id.id_glb_bottom_money_record_llyt:
                selectTab(2);
                break;

            case R.id.id_glb_bottom_customer_service_llyt:
                selectTab(3);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /* 启用相对的fragment */
    private void selectTab(int i) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (i) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = FactoryFragment.getInstanceByIndex(0);
                    transaction.add(R.id.id_glb_content_flyt, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                }
                break;
            case 1:
                if (mTradeRecordFragment == null) {
                    mTradeRecordFragment = FactoryFragment.getInstanceByIndex(1);
                    transaction.add(R.id.id_glb_content_flyt, mTradeRecordFragment);
                } else {
                    transaction.show(mTradeRecordFragment);
                }
                break;
            case 2:
                if (mMoneyRecordFragment == null) {
                    mMoneyRecordFragment = FactoryFragment.getInstanceByIndex(2);
                    transaction.add(R.id.id_glb_content_flyt, mMoneyRecordFragment);
                } else {
                    transaction.show(mMoneyRecordFragment);
                }
                break;
            case 3:
                if (mCustomerServiceFragment == null) {
                    mCustomerServiceFragment = FactoryFragment.getInstanceByIndex(3);
                    transaction.add(R.id.id_glb_content_flyt, mCustomerServiceFragment);
                } else {
                    transaction.show(mCustomerServiceFragment);
                }
                break;
        }
        /* 根据i来实例化fragment,用来替换当前的内容「id_glb_content_flyt」 */

		/* 对 选中tab更改图片和更改文字颜色 */
        changeView(i);
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mTradeRecordFragment != null) {
            transaction.hide(mTradeRecordFragment);
        }
        if (mMoneyRecordFragment != null) {
            transaction.hide(mMoneyRecordFragment);
        }
        if (mCustomerServiceFragment != null) {
            transaction.hide(mCustomerServiceFragment);
        }

    }

    /* 为选中的tab更改图片和更改文字颜色 */
    private void changeView(int i) {
		/* 重置按钮颜色 */
        resetBtn();
		/* 重置文字颜色 */
        resetText();
		/* 设置选中的按钮颜色和文字颜色 */
        switch (i) {
            case 0:
                ((ImageButton) mTabBtnHome
                        .findViewById(R.id.id_glb_bottom_home_image_ibtn))
                        .setImageResource(R.mipmap.glb_bottom_home_pressed);
                ((TextView) mTabBtnHome
                        .findViewById(R.id.id_glb_bottom_home_text_tv))
                        .setTextColor(Color.rgb(255, 126, 71));
                break;
            case 1:
                ((ImageButton) mTabBtnTradeRecord
                        .findViewById(R.id.id_glb_bottom_trade_record_ibtn))
                        .setImageResource(R.mipmap.glb_bottom_money_record_pressed);
                ((TextView) mTabBtnTradeRecord
                        .findViewById(R.id.id_glb_bottom_trade_record_tv))
                        .setTextColor(Color.rgb(255, 126, 71));
                break;
            case 2:
                ((ImageButton) mTabBtnMoneyRecord
                        .findViewById(R.id.id_glb_bottom_money_record_ibtn))
                        .setImageResource(R.mipmap.glb_bottom_trade_record_pressed);
                ((TextView) mTabBtnMoneyRecord
                        .findViewById(R.id.id_glb_bottom_money_record_tv))
                        .setTextColor(Color.rgb(255, 126, 71));
                break;
            case 3:
                ((ImageButton) mTabBtnCustomerService
                        .findViewById(R.id.id_glb_bottom_customer_server_ibtn))
                        .setImageResource(R.mipmap.glb_bottom_customer_service_pressed);
                ((TextView) mTabBtnCustomerService
                        .findViewById(R.id.id_glb_bottom_customer_server_tv))
                        .setTextColor(Color.rgb(255, 126, 71));
        }

    }

    /* 重置按钮颜色 */
    private void resetBtn() {
        ((ImageButton) mTabBtnHome
                .findViewById(R.id.id_glb_bottom_home_image_ibtn))
                .setImageResource(R.mipmap.glb_bottom_home_normal);

        ((ImageButton) mTabBtnTradeRecord
                .findViewById(R.id.id_glb_bottom_trade_record_ibtn))
                .setImageResource(R.mipmap.glb_bottom_money_record_normal);

        ((ImageButton) mTabBtnMoneyRecord
                .findViewById(R.id.id_glb_bottom_money_record_ibtn))
                .setImageResource(R.mipmap.glb_bottom_trade_record_normal);

        ((ImageButton) mTabBtnCustomerService
                .findViewById(R.id.id_glb_bottom_customer_server_ibtn))
                .setImageResource(R.mipmap.glb_bottom_customer_service_normal);
    }

    /* 重置文本颜色 */
    private void resetText() {
        ((TextView) mTabBtnHome.findViewById(R.id.id_glb_bottom_home_text_tv))
                .setTextColor(Color.rgb(102, 102, 102));

        ((TextView) mTabBtnTradeRecord
                .findViewById(R.id.id_glb_bottom_trade_record_tv))
                .setTextColor(Color.rgb(102, 102, 102));

        ((TextView) mTabBtnMoneyRecord
                .findViewById(R.id.id_glb_bottom_money_record_tv))
                .setTextColor(Color.rgb(102, 102, 102));

        ((TextView) mTabBtnCustomerService
                .findViewById(R.id.id_glb_bottom_customer_server_tv))
                .setTextColor(Color.rgb(102, 102, 102));
    }

}
