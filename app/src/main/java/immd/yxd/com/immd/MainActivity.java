package immd.yxd.com.immd;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView dibu_zuo;
    private ImageView dibu_zhong;
    private ImageView dibu_you;
    private FrameLayout ly_content;
    private fuliyuanFragment f1, f2;
    private baicaijiaFragment f3;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bindView();
    }

    //UI组件初始化与事件绑定
    private void bindView() {
        dibu_zuo = (ImageView) findViewById(R.id.dibu_zuo);
        dibu_zhong = (ImageView) findViewById(R.id.dibu_zhong);
        dibu_you = (ImageView) findViewById(R.id.dibu_you);
        ly_content = (FrameLayout) findViewById(R.id.fragment_container);

        dibu_zuo.setOnClickListener(this);
        dibu_zhong.setOnClickListener(this);
        dibu_you.setOnClickListener(this);
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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch(view.getId()){
            case R.id.dibu_zuo:
                selected();
                dibu_zuo.setSelected(true);
                if(f1==null){
                    f1 = new fuliyuanFragment();
                    transaction.add(R.id.fragment_container,f1);
                }else{
                    transaction.show(f1);
                }
                break;

            case R.id.dibu_zhong:
                selected();
                dibu_zhong.setSelected(true);
                if(f2==null){
                    f2 = new fuliyuanFragment();
                    transaction.add(R.id.fragment_container,f2);
                }else{
                    transaction.show(f2);
                }
                break;

            case R.id.dibu_you:
                selected();
                dibu_you.setSelected(true);
                if(f3==null){
                    f3 = new baicaijiaFragment();
                    transaction.add(R.id.fragment_container,f3);
                }else{
                    transaction.show(f3);
                }
                break;
        }

        transaction.commit();
    }

}
