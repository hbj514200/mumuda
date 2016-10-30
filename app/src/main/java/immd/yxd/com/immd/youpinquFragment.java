package immd.yxd.com.immd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
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
import immd.yxd.com.immd.goods.article_data;
import immd.yxd.com.immd.tools.myIntent;

public class youpinquFragment extends Fragment implements AdapterView.OnItemClickListener {

    RequestQueue mQueue;
    private ListView listView;
    private int page = 1;                                                                            //列表页数
    private myadapter adapter;
    private List<article_data> dataList = new ArrayList<article_data>();
    private Handler myhandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1)  jsonOK(dataList);
            if (msg.what == 2)  loadMore();
            super.handleMessage(msg);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baicaijia, container, false);
        listView = (ListView) view.findViewById(R.id.baicaijia_listview);
        listView.setOnItemClickListener(this);
        mQueue = Volley.newRequestQueue(getActivity());

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
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

        listView.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override public void onScroll(AbsListView absListView, int i, int i1, int i2) { }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState){
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        getStrs();
                    }
                }
            }
        });

        getStrs();
        return view;
    }

    public class myadapter extends ArrayAdapter<article_data> {

        public myadapter(Context context, int resouId, List<article_data> object) {
            super(context, resouId, object);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if (convertView == null){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.article_item, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) view.findViewById(R.id.item_imageview);
                if (viewHolder.imageView != null)   viewHolder.imageView.setVisibility(View.INVISIBLE);
                getImageView( viewHolder.imageView, dataList.get(position).getPic() );
                viewHolder.title = (TextView) view.findViewById(R.id.item_title);
                viewHolder.xiangqin = (TextView) view.findViewById(R.id.item_xiangqin);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.title.setText( dataList.get(position).getTitle() );
            viewHolder.xiangqin.setText( dataList.get(position).getDesc() );

            return view;
        }

    }

    public void jsonOK(List<article_data> dataList){
        adapter = new myadapter(getActivity(), R.layout.article_item, dataList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadMore(){
        adapter.notifyDataSetChanged();
        listView.setSelection(page*30-60-4);
    }

    public void getStrs(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://119.29.32.91/index.php?m=api&c=index&a=artList&count=30&page="+page++);
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
                        article_data data = new article_data();

                        data.setId( jsonObject.getString("id") );
                        data.setPic( jsonObject.getString("pic") );
                        data.setTitle( jsonObject.getString("title") );
                        data.setDesc( jsonObject.getString("desc") );
                        if (i==0)
                        /**
                         * 切记要删，限制了只获取1个， 因为 第二个文章pic网址为1  ，不合法会崩溃
                         */
                            dataList.add(data);
                    }
                    if (page==2){
                        Message message1 = new Message();   message1.what = 1;      myhandler.sendMessage(message1);  //第一次
                    } else {
                        Message message2 = new Message();   message2.what = 2;      myhandler.sendMessage(message2);  //load more
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    Log.i("baicaijia", "getStr抛出异常");
                }
            }
        }).start();
    }

    class ViewHolder {
        ImageView imageView;
        TextView xiangqin;
        TextView title;
    }

    public void getImageView(final ImageView imageView, String url){
        /**
         * url = url.replace("//", "%%%%%");                               //网址转换，解决了imumuda的域名访问问题
         url = url.replace("/", "//");
         url = url.replace("%%%%%", "//");
         */
        //url = "http://www.imumuda.cn//Uploads//2016-10-23//580c657fad04c.jpg";

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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(getActivity(), articleActivity.class);
        intent = myIntent.getArticleIntent( dataList.get(position), intent );
        startActivity(intent);
    }

}