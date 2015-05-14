package com.lyloou.weibo.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lyloou.weibo.R;
import com.lyloou.weibo.app.AccessTokenKeeper;
import com.lyloou.weibo.app.Constants;
import com.lyloou.weibo.app.MyApplication;
import com.lyloou.weibo.util.CommonUtil;
import com.lyloou.weibo.util.LU;
import com.lyloou.weibo.util.NetUtil;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

public class LoginActivity extends Activity implements OnClickListener {
    private Oauth2AccessToken accessToken;
    private TextView userText;
    private ImageView userHead;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userText = (TextView) findViewById(R.id.id_login_user_name_tv);
        userHead = (ImageView) findViewById(R.id.id_login_user_head_iv);
        Button loginBtn = (Button) findViewById(R.id.id_auth_login_btn);
        Button addUserBtn = (Button) findViewById(R.id.id_auth_add_user_btn);
        loginBtn.setOnClickListener(this);
        addUserBtn.setOnClickListener(this);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("正在加载......");
//        progressDialog.setCancelable(false);
        progressDialog.show();

        // 获取本地accessToken,如果有的话.
        accessToken = AccessTokenKeeper.readAccessToken(this);
        if (accessToken!=null && !TextUtils.isEmpty(accessToken.getToken())) {
            UsersAPI usersAPI = new UsersAPI(this, Constants.APP_KEY, accessToken);
            usersAPI.show(Long.parseLong(accessToken.getUid()), new RequestListener() {
                @Override
                public void onComplete(String response) {
                    Log.d(LU.TAG, "===============" + response);
                    user = User.parse(response);
                    String userName = null;
                    if(user!=null) {
                        userName = user.name;
                        //更换用户名称
                        userText.setText(userName);
                        //根据用户图像url来获取用户图像drawable,新启动一个线程;
                        CommonUtil.loadImageWithImgURLAndImageView(user.avatar_large, userHead);
                    }

                    // 弹出是否登陆
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("【" + userName + "】已登陆,是否继续使用此账号登陆?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    MyApplication.accessToken = accessToken;
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    progressDialog.dismiss();
                }

                @Override
                public void onWeiboException(WeiboException e) {

                }
            });

//            String userName = accessToken.getUid();

//            // 弹出是否登陆
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("「"+userName + "」已登陆,是否继续使用此账号登陆?")
//                    .setCancelable(false)
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            MyApplication.accessToken = accessToken;
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                        }
//                    })
//                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//            AlertDialog alert = builder.create();
//            alert.show();
        }


    }


    public void refresh(Object... obj) {
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_auth_add_user_btn:
                // 授权信息初始化
                AuthInfo mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
                SsoHandler mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
                mSsoHandler.authorizeWeb(new AuthListener()); //通过网页授权
                break;
            case R.id.id_auth_login_btn:
                //如果用户已经登陆过,则可以使用此按钮
                if (!TextUtils.isEmpty(accessToken.getToken())) {
                    MyApplication.accessToken = accessToken;
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "请点击「添加用户」!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {

            // 从 Bundle 中解析 Token
            Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
                Toast.makeText(LoginActivity.this,
                        "授权成功", Toast.LENGTH_SHORT).show();
                Log.d(LU.TAG, "授权成功");
                MyApplication.accessToken = mAccessToken;
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                Log.d(LU.TAG, "授权失败");
                String code = values.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Log.d(LU.TAG, "取消授权");
            Toast.makeText(LoginActivity.this,
                    "取消授权", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
}
