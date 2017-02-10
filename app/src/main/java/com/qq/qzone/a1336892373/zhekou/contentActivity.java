package com.qq.qzone.a1336892373.zhekou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.qq.qzone.a1336892373.zhekou.Mine.MineCallback;
import com.qq.qzone.a1336892373.zhekou.Mine.MyUrl;
import com.qq.qzone.a1336892373.zhekou.tools.httpConn;

public class contentActivity extends AppCompatActivity implements View.OnClickListener, MineCallback {

    private httpConn connect;
    private String quan_link = "";
    private String goosid = "";
    private String ali_click = "";
    private String pic = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        connect = httpConn.newInstance(this);
        view_init();
        new MyUrl().getUrl(ali_click, this);
    }

    private void view_init() {
        ImageView tab_back = (ImageView) findViewById(R.id.tab_back);
        tab_back.setOnClickListener(this);
        Button xiadan = (Button) findViewById(R.id.dibu_xiadan);
        xiadan.setOnClickListener(this);
        Button linjuan = (Button) findViewById(R.id.dibu_linjuan);
        linjuan.setOnClickListener(this);

        ImageView imageView = (ImageView) findViewById(R.id.item_imageview);
        TextView yuanjia = (TextView) findViewById(R.id.item_yuanjia);
        TextView juan = (TextView) findViewById(R.id.item_juan);
        TextView xiangqin = (TextView) findViewById(R.id.item_xiangqin);
        TextView tianmao = (TextView) findViewById(R.id.item_tiaomao);
        TextView qian = (TextView) findViewById(R.id.item_qian);
        TextView time = (TextView) findViewById(R.id.item_youxiaoqi);

        connect.displayImg( imageView, getIntent().getStringExtra("pic"), 0);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tab_back :
                finish();
                break;
            case R.id.dibu_linjuan :
                Intent intent = new Intent(contentActivity.this, webActivity.class);
                intent.putExtra("leixin", 1);
                intent.putExtra("url", quan_link);
                startActivity(intent);
                break;
            case R.id.dibu_xiadan :
                Intent intent2 = new Intent(contentActivity.this, webActivity.class);
                intent2.putExtra("leixin", 2);
                intent2.putExtra("url", ali_click);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void setMineUrl(String mineUrl) {
        this.ali_click = mineUrl;
    }

}
