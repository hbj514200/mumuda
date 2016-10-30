package immd.yxd.com.immd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class contentActivity extends AppCompatActivity implements View.OnClickListener {

    private String quan_link = "";
    private String goosid = "";
    private String ali_click = "";
    private String pic = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        view_init();

    }

    private void view_init() {
        ImageView tab_back = (ImageView) findViewById(R.id.tab_back);
        tab_back.setOnClickListener(this);
        Button xiadan = (Button) findViewById(R.id.dibu_xiadan);
        xiadan.setOnClickListener(this);
        Button linjuan = (Button) findViewById(R.id.dibu_linjuan);
        linjuan.setOnClickListener(this);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.content_scrollview);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);      //滚动布局定位到顶部
            }
        });


        ImageView imageView = (ImageView) findViewById(R.id.item_imageview);
        TextView yuanjia = (TextView) findViewById(R.id.item_yuanjia);
        TextView juan = (TextView) findViewById(R.id.item_juan);
        TextView xiangqin = (TextView) findViewById(R.id.item_xiangqin);
        TextView tianmao = (TextView) findViewById(R.id.item_tiaomao);
        TextView qian = (TextView) findViewById(R.id.item_qian);
        TextView time = (TextView) findViewById(R.id.item_youxiaoqi);

        getImageView(imageView, getIntent().getStringExtra("pic"));
        String[] youxiaoqi = getIntent().getStringExtra("quan_time").split(" ");    //返回的时间带有时分秒
        time.setText( "有效期：" + youxiaoqi[0]  );
        yuanjia.setText( "原价:  " + getIntent().getStringExtra("org_price") );
        juan.setText( "劵值： " + getIntent().getStringExtra("quan_price") );
        xiangqin.setText( getIntent().getStringExtra("title") );
        qian.setText( "¥：" + getIntent().getStringExtra("price") );
        goosid = getIntent().getStringExtra("goodsid");
        ali_click = getIntent().getStringExtra("ali_click");
        quan_link = getIntent().getStringExtra("quan_link");
        pic = getIntent().getStringExtra("pic");
        if (getIntent().getStringExtra("istmall").equals("1")) tianmao.setText("天猫");
        else                                                   tianmao.setVisibility(View.INVISIBLE);
    }

    public void getImageView(final ImageView imageView, String url){
        RequestQueue mQueue;
        mQueue = Volley.newRequestQueue(contentActivity.this);
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {    //最大宽度和高度，会压缩
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mQueue.add(imageRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tab_back :
                finish();
                break;
            case R.id.dibu_linjuan :
                Intent intent = new Intent(contentActivity.this, webActivity.class);
                intent.putExtra("url", quan_link);
                startActivity(intent);
                break;
            case R.id.dibu_xiadan :
                Intent intent2 = new Intent(contentActivity.this, webActivity.class);
                intent2.putExtra("url", ali_click);
                startActivity(intent2);
                break;
        }
    }
}
