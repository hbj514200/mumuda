package immd.yxd.com.immd.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 单例类，  对于图片网络请求队列，避免产生太多重复的对象占用内存.
 */

public class httpConn {
    private RequestQueue mQueue;
    private static httpConn connect;

    public static httpConn newInstance(Context context){
        if (connect == null)    return new httpConn(context);
        else                    return connect;
    }

    private httpConn(Context context){
        mQueue = Volley.newRequestQueue( context );
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

    public void getImageView(final ImageView imageView, String url, int yasuo){
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }, yasuo, yasuo, Bitmap.Config.RGB_565, new Response.ErrorListener() {    //最大宽度和高度，会压缩
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mQueue.add(imageRequest);
        //压缩参数， 例250，   图片若大于250*250将被压缩至250，   0则不压缩
    }

}
