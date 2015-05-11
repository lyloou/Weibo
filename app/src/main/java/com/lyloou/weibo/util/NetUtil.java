package com.lyloou.weibo.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;


public class NetUtil {


    public static Bitmap getNetImage(String address) throws Exception {

        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        InputStream is = conn.getInputStream();
        byte[] imagebytes = StreamTool.getBytes(is);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);
        is.close();
        conn.disconnect();
        return bitmap;


    }


}

class StreamTool {
    public static byte[] getBytes(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        is.close();
        bos.flush();
        byte[] result = bos.toByteArray();
        return result;
    }
}