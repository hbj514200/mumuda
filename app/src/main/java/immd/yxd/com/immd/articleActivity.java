package immd.yxd.com.immd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import immd.yxd.com.immd.tools.httpConn;

public class articleActivity extends AppCompatActivity implements View.OnClickListener {
    private httpConn connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        connect = httpConn.newInstance(this);

        view_init();
    }

    private void view_init(){
        article_listview_Fragment fragment = (article_listview_Fragment) getSupportFragmentManager().findFragmentById(R.id.content_grid_fragment);
        fragment.setGoodsid( getIntent().getStringExtra("id") );

        ImageView imageView = (ImageView) findViewById(R.id.item_imageview);
        TextView title = (TextView) findViewById(R.id.item_title);
        TextView xiangqin = (TextView) findViewById(R.id.item_xiangqin);
        ImageView back_button = (ImageView) findViewById(R.id.tab_back);

        connect.getImageView( imageView, getIntent().getStringExtra("pic"), 0 );
        title.setText( getIntent().getStringExtra("title") );
        xiangqin.setText( getIntent().getStringExtra("desc") );
        back_button.setOnClickListener(this);
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
