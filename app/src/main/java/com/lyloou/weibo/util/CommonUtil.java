package com.lyloou.weibo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lyloou.weibo.app.Constants;
import com.lyloou.weibo.app.MyApplication;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommonUtil {
    public static boolean isEmpty(String s) {
        return (null == s || "".equals(s));
    }

    public static String strToDateStr(String strDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.CHINA);
        Date date = formatter.parse(strDate);
        formatter = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
        return formatter.format(date);
    }

    // 将微博的日期字符串转换为Date对象
    public static Date strToDate(String str) {
        // sample：Tue May 31 17:46:55 +0800 2011
        // E：周 MMM：字符串形式的月，如果只有两个M，表示数值形式的月 Z表示时区（＋0800）
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy",
                Locale.US);
        Date result = null;
        try {
            result = sdf.parse(str);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;

    }

    public static String getTimeStr(Date oldTime, Date currentDate) {
        long time1 = currentDate.getTime();

        long time2 = oldTime.getTime();

        long time = (time1 - time2) / 1000;

        if (time >= 0 && time < 60) {
            return "刚才";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.US);
            return sdf.format(oldTime);
        }
    }


    public static void loadImageWithImgURLAndImageView(String imgUrl, final ImageView iv){
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Bitmap weiboImg = (Bitmap) msg.obj;
                        iv.setImageBitmap(weiboImg);
                        break;
                }
            }
        };
        loadImage(imgUrl,handler);
    }


    private static void loadImage(final String userHeadUrl, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap userImg = null;
                try {
                    userImg = NetUtil.getNetImage(userHeadUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (userImg != null) {
                    Message msg = handler.obtainMessage();
                    msg.obj = userImg;
                    msg.what = 1;
                    msg.sendToTarget();
                }
            }
        }).start();
    }


    public static String atBlue(String s) {

        StringBuilder sb = new StringBuilder();
        int commonTextColor = Color.BLACK;
        int signColor = Color.BLUE;

        int state = 1;
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            switch (state) {
                case 1: // 普通字符状态
                    // 遇到@
                    if (s.charAt(i) == '@') {
                        state = 2;
                        str += s.charAt(i);
                    }
                    // 遇到#
                    else if (s.charAt(i) == '#') {
                        str += s.charAt(i);
                        state = 3;
                    }
                    // 添加普通字符
                    else {
                        if (commonTextColor == Color.BLACK)
                            sb.append(s.charAt(i));
                        else
                            sb.append("<font color='" + commonTextColor + "'>"
                                    + s.charAt(i) + "</font>");
                    }
                    break;
                case 2: // 处理遇到@的情况
                    // 处理@后面的普通字符
                    if (Character.isJavaIdentifierPart(s.charAt(i))) {
                        str += s.charAt(i);
                    }

                    else {
                        // 如果只有一个@，作为普通字符处理
                        if ("@".equals(str)) {
                            sb.append(str);
                        }
                        // 将@及后面的普通字符变成蓝色
                        else {
                            sb.append(setTextColor(str, String.valueOf(signColor)));
                        }
                        // @后面有#的情况，首先应将#添加到str里，这个值可能会变成蓝色，也可以作为普通字符，要看后面还有没有#了
                        if (s.charAt(i) == '#') {

                            str = String.valueOf(s.charAt(i));
                            state = 3;
                        }
                        // @后面还有个@的情况，和#类似
                        else if (s.charAt(i) == '@') {
                            str = String.valueOf(s.charAt(i));
                            state = 2;
                        }
                        // @后面有除了@、#的其他特殊字符。需要将这个字符作为普通字符处理
                        else {
                            if (commonTextColor == Color.BLACK)
                                sb.append(s.charAt(i));
                            else
                                sb.append("<font color='" + commonTextColor + "'>"
                                        + s.charAt(i) + "</font>");
                            state = 1;
                            str = "";
                        }
                    }
                    break;
                case 3: // 处理遇到#的情况
                    // 前面已经遇到一个#了，这里处理结束的#
                    if (s.charAt(i) == '#') {
                        str += s.charAt(i);
                        sb.append(setTextColor(str, String.valueOf(signColor)));
                        str = "";
                        state = 1;

                    }
                    // 如果#后面有@，那么看一下后面是否还有#，如果没有#，前面的#作废，按遇到@处理
                    else if (s.charAt(i) == '@') {
                        if (s.substring(i).indexOf("#") < 0) {
                            sb.append(str);
                            str = String.valueOf(s.charAt(i));
                            state = 2;

                        } else {
                            str += s.charAt(i);
                        }
                    }
                    // 处理#...#之间的普通字符
                    else {
                        str += s.charAt(i);
                    }
                    break;
            }

        }
        if (state == 1 || state == 3) {
            sb.append(str);
        } else if (state == 2) {
            if ("@".equals(str)) {
                sb.append(str);
            } else {
                sb.append(setTextColor(str, String.valueOf(signColor)));
            }

        }

        return sb.toString();

    }

    public static String setTextColor(String s, String color) {
        String result = "<font color='" + color + "'>" + s + "</font>";

        return result;
    }


    //让Listview可以嵌套在scrollview中.
    public static void useListViewInScrollView(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 根据三个参数设置用户名到textview上
     * @param context
     * @param accessToken
     * @param userNameText
     */
    public static void setUserNameInTextView(Context context,Oauth2AccessToken accessToken, final TextView userNameText){
        setUserNameInTextView(context,accessToken,Long.parseLong(accessToken.getUid()),userNameText);
    }

    /**
     * 根据四个参数来设置用户名到textview上
     * @param context
     * @param accessToken
     * @param uId
     * @param userNameText
     */
    public static void setUserNameInTextView(Context context,Oauth2AccessToken accessToken, Long uId,final TextView userNameText){
        UsersAPI usersAPI= new UsersAPI(context, Constants.APP_KEY, accessToken);
        usersAPI.show(uId, new RequestListener() {
            @Override
            public void onComplete(String s) {
                LU.log("查找用户名成功.");
                User user = User.parse(s);
                if(user!=null)
                    userNameText.setText(user.name);
            }

            @Override
            public void onWeiboException(WeiboException e) {

            }
        });
    }
}
