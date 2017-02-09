package com.qq.qzone.a1336892373.zhekou;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class fenlei_list_Activity extends AppCompatActivity {

    //分类Activity中点击后打开的列表Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenlei_list_);

        ImageView back_button = (ImageView) findViewById(R.id.tab_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewpager_item_fragment fragment = new viewpager_item_fragment();
        FragmentManager fm = getSupportFragmentManager();
        fragment.ftype = Integer.parseInt( getIntent().getStringExtra("ftype") );
        fragment.stype = Integer.parseInt( getIntent().getStringExtra("stype") );
        fragment.Num = 1000;
        fm.beginTransaction().add(R.id.feneli_list_fragment, fragment).commit();
    }

}
