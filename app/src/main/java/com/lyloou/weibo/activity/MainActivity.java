package com.lyloou.weibo.activity;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyloou.weibo.R;
import com.lyloou.weibo.fragment.FactoryFragment;
import com.lyloou.weibo.util.LU;

public class MainActivity extends Activity implements OnClickListener {
    private LinearLayout mTabBtnHome;
    private LinearLayout mTabBtnMine;
    private LinearLayout mTabBtnMessage;
    private LinearLayout mTabBtnSetting;
    private FragmentManager fragmentManager;
    private ImageButton trade_record_ibtn;

    private Fragment mHomeFragment;
    private Fragment mMineFragment;
    private Fragment mMessageFragment;
    private Fragment mSettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LU.TAG, "授权成功");
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
                this.finish();
//                System.exit(0);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /* 初始化底部视图和点击监听器 */
    private void initView() {
        mTabBtnHome = (LinearLayout) findViewById(R.id.id_glb_bottom_home_llyt);
        mTabBtnMine = (LinearLayout) findViewById(R.id.id_glb_bottom_mine_llyt);
        mTabBtnMessage = (LinearLayout) findViewById(R.id.id_glb_bottom_message_llyt);
        mTabBtnSetting = (LinearLayout) findViewById(R.id.id_glb_bottom_setting_llyt);

        mTabBtnHome.setOnClickListener(this);
        mTabBtnMine.setOnClickListener(this);
        mTabBtnMessage.setOnClickListener(this);
        mTabBtnSetting.setOnClickListener(this);
    }

    // 设置点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_glb_bottom_home_llyt:
                selectTab(0);
                break;

            case R.id.id_glb_bottom_mine_llyt:
                selectTab(1);
                break;

            case R.id.id_glb_bottom_message_llyt:
                selectTab(2);
                break;

            case R.id.id_glb_bottom_setting_llyt:
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
    /* 根据i来实例化fragment,用来替换当前的内容「id_glb_content_flyt」 */
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
                if (mMineFragment == null) {
                    mMineFragment = FactoryFragment.getInstanceByIndex(1);
                    transaction.add(R.id.id_glb_content_flyt, mMineFragment);
                } else {
                    transaction.show(mMineFragment);
                }
                break;
            case 2:
                if (mMessageFragment == null) {
                    mMessageFragment = FactoryFragment.getInstanceByIndex(2);
                    transaction.add(R.id.id_glb_content_flyt, mMessageFragment);
                } else {
                    transaction.show(mMessageFragment);
                }
                break;
            case 3:
                if (mSettingFragment == null) {
                    mSettingFragment = FactoryFragment.getInstanceByIndex(3);
                    transaction.add(R.id.id_glb_content_flyt, mSettingFragment);
                } else {
                    transaction.show(mSettingFragment);
                }
                break;
        }


		/* 对 选中tab更改图片和更改文字颜色 */
        changeView(i);
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mMineFragment != null) {
            transaction.hide(mMineFragment);
        }
        if (mMessageFragment != null) {
            transaction.hide(mMessageFragment);
        }
        if (mSettingFragment != null) {
            transaction.hide(mSettingFragment);
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
                ((ImageButton) mTabBtnMine
                        .findViewById(R.id.id_glb_bottom_mine_image_ibtn))
                        .setImageResource(R.mipmap.glb_bottom_mine_pressed);
                ((TextView) mTabBtnMine
                        .findViewById(R.id.id_glb_bottom_mine_text_tv))
                        .setTextColor(Color.rgb(255, 126, 71));
                break;
            case 2:
                ((ImageButton) mTabBtnMessage
                        .findViewById(R.id.id_glb_bottom_message_image_ibtn))
                        .setImageResource(R.mipmap.glb_bottom_message_pressed);
                ((TextView) mTabBtnMessage
                        .findViewById(R.id.id_glb_bottom_message_text_tv))
                        .setTextColor(Color.rgb(255, 126, 71));
                break;
            case 3:
                ((ImageButton) mTabBtnSetting
                        .findViewById(R.id.id_glb_bottom_setting_image_ibtn))
                        .setImageResource(R.mipmap.glb_bottom_setting_pressed);
                ((TextView) mTabBtnSetting
                        .findViewById(R.id.id_glb_bottom_setting_text_tv))
                        .setTextColor(Color.rgb(255, 126, 71));
        }

    }

    /* 重置按钮颜色 */
    private void resetBtn() {
        ((ImageButton) mTabBtnHome
                .findViewById(R.id.id_glb_bottom_home_image_ibtn))
                .setImageResource(R.mipmap.glb_bottom_home_normal);

        ((ImageButton) mTabBtnMine
                .findViewById(R.id.id_glb_bottom_mine_image_ibtn))
                .setImageResource(R.mipmap.glb_bottom_mine_normal);

        ((ImageButton) mTabBtnMessage
                .findViewById(R.id.id_glb_bottom_message_image_ibtn))
                .setImageResource(R.mipmap.glb_bottom_message_normal);

        ((ImageButton) mTabBtnSetting
                .findViewById(R.id.id_glb_bottom_setting_image_ibtn))
                .setImageResource(R.mipmap.glb_bottom_setting_normal);
    }

    /* 重置文本颜色 */
    private void resetText() {
        ((TextView) mTabBtnHome.findViewById(R.id.id_glb_bottom_home_text_tv))
                .setTextColor(Color.rgb(102, 102, 102));

        ((TextView) mTabBtnMine
                .findViewById(R.id.id_glb_bottom_mine_text_tv))
                .setTextColor(Color.rgb(102, 102, 102));

        ((TextView) mTabBtnMessage
                .findViewById(R.id.id_glb_bottom_message_text_tv))
                .setTextColor(Color.rgb(102, 102, 102));

        ((TextView) mTabBtnSetting
                .findViewById(R.id.id_glb_bottom_setting_text_tv))
                .setTextColor(Color.rgb(102, 102, 102));
    }

}
