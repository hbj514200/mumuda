package immd.yxd.com.immd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import immd.yxd.com.immd.goods.baicai_data;
import immd.yxd.com.immd.tools.myIntent;

public class fenleiActivity extends Activity {

    RequestQueue mQueue;
    private int selectIndex=0;
    private ListView mListView1,mListView2;
    private MyListViewAdapter1 adapter1;
    private String[][] dataList = { {"data","data","data","data","data","data","data","data","data","data","data","data","data","data","data"},{"data","data","data"},{"data","data","data"},{"data","data","data"},
            {"data","data","data"},{"data","data","data"},{"data","data","data"},{"data","data","data"},{"data","data","data"},{"data","data","data"},
            {"data","data","data"},{"data","data","data"},{"data","data","data"},{"data","data","data"} };
    private String[] mMenus = {"全部","女装","男装","鞋包","配饰","美食","数码","美妆","文体"};
    private MyListViewAdapter2 adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenlei);
        initView();
    }

    private void initView() {
        mQueue = Volley.newRequestQueue(fenleiActivity.this);
        ImageView back_button = (ImageView) findViewById(R.id.tab_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mListView1= (ListView) findViewById(R.id.list_item_1);
        mListView2= (ListView) findViewById(R.id.list_item_2);

        adapter1=new MyListViewAdapter1(mMenus,this,selectIndex);
        adapter2=new MyListViewAdapter2(dataList,this,selectIndex);
        mListView1.setAdapter(adapter1);
        mListView2.setAdapter(adapter2);

        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectIndex=position;
                //把下标传过去，然后刷新adapter
                adapter1.setIndex(position);
                adapter1.notifyDataSetChanged();
                //当点击某个item的时候让这个item自动滑动到listview的顶部(下面item够多，如果点击的是最后一个就不能到达顶部了)
                mListView1.smoothScrollToPositionFromTop(position,0);

                adapter2.setIndex(position);
                mListView2.setAdapter(adapter2);
            }
        });

        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.showToast(fenleiActivity.this,dataList[selectIndex][position]);
            }
        });
    }

}