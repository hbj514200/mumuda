package immd.yxd.com.immd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import immd.yxd.com.immd.tools.httpConn;

public class articleActivity extends AppCompatActivity {
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
        ImageView back_button = (ImageView) findViewById(R.id.tab_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
