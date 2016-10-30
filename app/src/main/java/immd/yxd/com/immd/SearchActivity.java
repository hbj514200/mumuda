package immd.yxd.com.immd;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.List;

import immd.yxd.com.immd.goods.baicai_data;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    RequestQueue mQueue;
    private ListView listView;
    private String content;                                                                            //列表页数
    private myadapter adapter;
    private List<baicai_data> dataList = new ArrayList<baicai_data>();
    private Handler myhandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1)  jsonOK(dataList);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView = (ListView) findViewById(R.id.baicaijia_listview);
        mQueue = Volley.newRequestQueue(SearchActivity.this);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_orange_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        getStrs();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 4000);
            }
        });

        editText = (EditText) findViewById(R.id.search_edittext);
        ImageView backButton = (ImageView) findViewById(R.id.search_back);
        TextView sousuoButton = (TextView) findViewById(R.id.search_sousuo);
        sousuoButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)  {
                if (actionId== EditorInfo.IME_ACTION_SEND ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {
                    search();       //键盘回车搜索
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_back :
                finish();
                break;
            case R.id.search_sousuo :
                search();
                break;
        }
    }

    private void search(){
        content = editText.getText().toString();
        if (content.equals("")){                                                //搜索内容为空
            Toast toast = Toast.makeText(SearchActivity.this, "请输入内容", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 10);
            toast.show();
        } else {
            getStrs();
        }

    }

    public class myadapter extends ArrayAdapter<baicai_data> {

        public myadapter(Context context, int resouId, List<baicai_data> object) {
            super(context, resouId, object);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if (convertView == null){
                view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_item, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) view.findViewById(R.id.item_imageview);
                viewHolder.yuanjia = (TextView) view.findViewById(R.id.item_yuanjia);
                viewHolder.juan = (TextView) view.findViewById(R.id.item_juan);
                viewHolder.xiangqin = (TextView) view.findViewById(R.id.item_xiangqin);
                viewHolder.tianmao = (TextView) view.findViewById(R.id.item_tiaomao);
                viewHolder.qian = (TextView) view.findViewById(R.id.item_qian);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
                if (viewHolder.imageView != null)   viewHolder.imageView.setVisibility(View.INVISIBLE);
            }

            String juan_st = " 劵:¥ "+dataList.get(position).getQuan_price();

            getImageView( viewHolder.imageView, dataList.get(position).getPic() );
            viewHolder.yuanjia.setText( dataList.get(position).getOrg_Price() );
            viewHolder.juan.setText( juan_st );
            viewHolder.qian.setText( dataList.get(position).getPrice() );
            viewHolder.xiangqin.setText( dataList.get(position).getTitle() );
            if (dataList.get(position).getIsTmall().equals("1")) viewHolder.tianmao.setText("天猫");
            else                                                 viewHolder.tianmao.setText("");

            return view;
        }

    }

    public void jsonOK(List<baicai_data> dataList){
        if (dataList.size() == 0){                                             //没有结果不填充ListView，而是Toast提示
            Toast toast = Toast.makeText(SearchActivity.this, "找不到商品", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 10);
            toast.show();
            return;
        } else {
            closeKeyBorad();
        }
        if (listView.getFooterViewsCount() == 0){
            View footer = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_footer, null);
            listView.addFooterView(footer);
        }
        adapter = new myadapter(SearchActivity.this, R.layout.search_item, dataList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void getStrs(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://119.29.32.91/index.php?m=api&c=index&a=search&key="+content);
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

                    JSONArray jsonArray=new JSONObject(response.toString()).getJSONArray("msg");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                        baicai_data data = new baicai_data();

                        data.setGoodsID( jsonObject.getString("goodsid") );
                        data.setPrice( jsonObject.getString("price") );
                        data.setOrg_Price( jsonObject.getString("org_price") );
                        data.setIsTmall( jsonObject.getString("istmall") );
                        data.setAli_click( jsonObject.getString("ali_click") );
                        data.setPic( jsonObject.getString("pic") );
                        data.setTitle( jsonObject.getString("title") );
                        data.setQuan_price( jsonObject.getString("quan_price") );
                        dataList.add(data);
                    }

                     Message message1 = new Message();   message1.what = 1;      myhandler.sendMessage(message1);  //第一次

                } catch (Exception e){
                    e.printStackTrace();
                    Log.i("baicaijia", "getStr抛出异常");
                }
            }
        }).start();
    }

    class ViewHolder {
        ImageView imageView;
        TextView yuanjia;
        TextView juan;
        TextView xiangqin;
        TextView tianmao;
        TextView qian;
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
                }, 250, 250, Bitmap.Config.RGB_565, new Response.ErrorListener() {    //最大宽度和高度，会压缩
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mQueue.add(imageRequest);
    }

    private void closeKeyBorad(){
        InputMethodManager inputMethodManager = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


}
