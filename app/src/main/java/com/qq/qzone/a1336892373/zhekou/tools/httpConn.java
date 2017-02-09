package com.qq.qzone.a1336892373.zhekou.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.qq.qzone.a1336892373.zhekou.R;

/**
 * 单例类，  对于图片网络请求队列，避免产生太多重复的对象占用内存.
 */

public class httpConn {
    private String mulu;
    private RequestQueue mQueue;
    private static httpConn connect;
    private Handler myhandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            ImageView imageView = (ImageView) msg.obj;
            imageView.setImageBitmap( (Bitmap) msg.getData().getParcelable("bitmap") );
            imageView.setVisibility(View.VISIBLE);
            super.handleMessage(msg);
        }
    };

    public static httpConn newInstance(Context context){
        if (connect == null)    return new httpConn(context);
        else                    return connect;
    }

    private httpConn(Context context){
        mQueue = Volley.newRequestQueue( context );
        mulu = Environment.getExternalStorageDirectory().getPath()+"/Immd/";
    }

    public static String getData(String url_st) throws IOException{
        URL url = new URL(url_st);
        HttpURLConnection connection = null;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(8000);

        InputStream in = connection.getInputStream();
        BufferedReader reader = new BufferedReader( new InputStreamReader(in));

        StringBuilder response = new StringBuilder();
        String line;
        while ( (line=reader.readLine()) != null ){
            response.append(line);
        }
        connection.disconnect();
        return response.toString();
    }

    public void getImageView(final ImageView imageView, final String url, int yasuo){
        final String md5 = stringToMD5(url);
        final File file = new File(mulu + md5 +".jpg");
        if ( file.exists() ){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("bitmap", BitmapFactory.decodeFile( mulu+ md5 +".jpg" ));
                    message.setData(bundle);
                    message.obj = imageView;
                    myhandler.sendMessage(message);
                }
            }).start();
            return;
        }

        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                        imageView.setVisibility(View.VISIBLE);
                        saveBitmapFile(response, file);
                    }
                }, yasuo, yasuo, Bitmap.Config.RGB_565, new Response.ErrorListener() {    //最大宽度和高度，会压缩
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mQueue.add(imageRequest);
        //压缩参数， 例250，   图片若大于250*250将被压缩至250，   0则不压缩
    }

    public void displayImg(ImageView imageView, String url, int yasuo){
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache() );
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,R.drawable.im_loading, R.drawable.im_loading);
        imageLoader.get(url,listener, yasuo, yasuo);

    }

    private void saveBitmapFile(Bitmap bitmap, File file){
        //将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String stringToMD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }


}
