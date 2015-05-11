package com.lyloou.weibo.util;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

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
}
