package immd.yxd.com.immd;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView Tab_name;
    private ImageView dibu_zuo;
    private ImageView dibu_zhong;
    private ImageView dibu_you;
    private fuliyuanFragment f1;
    youpinquFragment f2;
    private baicaijiaFragment f3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView searchButton = (TextView) findViewById(R.id.toolbar_search);
        TextView fenleiButton = (TextView) findViewById(R.id.toolbar_fenlei);
        searchButton.setOnClickListener(this);
        fenleiButton.setOnClickListener(this);
        Tab_name = (TextView) findViewById(R.id.toolbar_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bindView();
    }

    //UI组件初始化与事件绑定
    private void bindView() {
        dibu_zuo = (ImageView) findViewById(R.id.dibu_zuo);
        dibu_zhong = (ImageView) findViewById(R.id.dibu_zhong);
        dibu_you = (ImageView) findViewById(R.id.dibu_you);

        dibu_zuo.setOnClickListener(this);
        dibu_zhong.setOnClickListener(this);
        dibu_you.setOnClickListener(this);

        zuo_xiangyin();
    }

    public void selected(){
        //重置所有文本的选中状态
        dibu_zuo.setSelected(false);
        dibu_zhong.setSelected(false);
        dibu_you.setSelected(false);
    }


    public void hideAllFragment(FragmentTransaction transaction){
        //隐藏所有Fragment
        if(f1!=null){
            transaction.hide(f1);
        }
        if(f2!=null){
            transaction.hide(f2);
        }
        if(f3!=null){
            transaction.hide(f3);
        }
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch(view.getId()){
            case R.id.dibu_zuo:
                zuo_xiangyin();
                break;

            case R.id.dibu_zhong:
                selected();
                Tab_name.setText("优品区");
                dibu_zhong.setSelected(true);
                if(f2==null){
                    f2 = new youpinquFragment();
                    transaction.add(R.id.fragment_container,f2);
                }else{
                    transaction.show(f2);
                }
                break;

            case R.id.dibu_you:
                selected();
                Tab_name.setText("白菜价");
                dibu_you.setSelected(true);
                if(f3==null){
                    f3 = new baicaijiaFragment();
                    transaction.add(R.id.fragment_container,f3);
                }else{
                    transaction.show(f3);
                }
                break;
            case R.id.toolbar_search:
                startActivity( new Intent(MainActivity.this, SearchActivity.class) );
                break;
            case R.id.toolbar_fenlei:
                startActivity( new Intent(MainActivity.this, fenleiActivity.class) );
                break;
        }

        transaction.commit();
    }

    private void zuo_xiangyin(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        selected();                             //第一次进入默认做个选中，   故单独拉出来
        dibu_zuo.setSelected(true);
        Tab_name.setText("福利园");
        if(f1==null){
            f1 = new fuliyuanFragment();
            transaction.add(R.id.fragment_container,f1);
        }else{
            transaction.show(f1);
        }
        transaction.commit();
    }

}
