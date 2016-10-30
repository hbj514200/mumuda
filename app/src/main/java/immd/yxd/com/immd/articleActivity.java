package immd.yxd.com.immd;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class articleActivity extends AppCompatActivity implements View.OnClickListener {
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        mQueue = Volley.newRequestQueue(articleActivity.this);

        view_init();
    }

    private void view_init(){
        article_listview_Fragment fragment = (article_listview_Fragment) getSupportFragmentManager().findFragmentById(R.id.content_grid_fragment);
        fragment.setGoodsid( getIntent().getStringExtra("id") );

        ImageView imageView = (ImageView) findViewById(R.id.item_imageview);
        TextView title = (TextView) findViewById(R.id.item_title);
        TextView xiangqin = (TextView) findViewById(R.id.item_xiangqin);
        ImageView back_button = (ImageView) findViewById(R.id.tab_back);

        getImageView( imageView, getIntent().getStringExtra("pic") );
        title.setText( getIntent().getStringExtra("title") );
        xiangqin.setText( getIntent().getStringExtra("desc") );
        back_button.setOnClickListener(this);
    }

    public void getImageView(final ImageView imageView, String url){
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
        }
    }

}
