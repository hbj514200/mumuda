package immd.yxd.com.immd;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
import immd.yxd.com.immd.goods.baicai_data;

public class viewpager_item_fragment extends Fragment {
    public int Num = 1;
    RequestQueue mQueue;
    private GridView gridView;
    private int page = 1;                                                                            //列表页数
    private myadapter adapter;
    private List<baicai_data> dataList = new ArrayList<baicai_data>();
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
        View view = inflater.inflate(R.layout.viewpager_item, container, false);
        gridView  = (GridView) view.findViewById(R.id.viewpager_item_gridview);
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

        gridView.setOnScrollListener(new AbsListView.OnScrollListener(){
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

    public class myadapter extends ArrayAdapter<baicai_data> {

        public myadapter(Context context, int resouId, List<baicai_data> object) {
            super(context, resouId, object);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if (convertView == null){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.grid_item, null);
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

            String juan_st = "劵:¥ "+dataList.get(position).getQuan_price();

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
        //View footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_foot, null);
        /**
         *         gridView.addFooterView(footer);
         */
        Toast.makeText(getActivity(), "datalist长度： "+dataList.size(), Toast.LENGTH_SHORT).show();
        adapter = new myadapter(getActivity(), R.layout.grid_item, dataList);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadMore(){
        adapter.notifyDataSetChanged();
        gridView.setSelection(page*30-60-4);
    }

    public void getStrs(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://119.29.32.91/index.php?m=api&c=index&a=goods&count=30&page="+(page++)+"&ftype="+(Num));
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

                    //JSONArray jsonArray=new JSONObject(response.toString()).getJSONArray("msg");
                    /**
                     * 此处要删掉！！！！
                     */
                    JSONArray jsonArray=new JSONObject(getTest()).getJSONArray("msg");

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
                }, 300, 300, Bitmap.Config.RGB_565, new Response.ErrorListener() {    //最大宽度和高度，会压缩
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mQueue.add(imageRequest);
    }


    /**
     * 这东西一定要删掉！！！！！！！！！！        测试用途
     * @return
     */
    private String getTest(){
        return "{\"status\":\"success\",\"msg\":[{\"goodsid\":\"539051433071\",\"title\":\"秋冬毛呢大衣男中长款青年韩版修身风衣男潮时尚男士加厚呢子外套\",\"quan_price\":\"30\",\"quan_surplus\":\"9192\",\"quan_time\":\"2016-10-31 00:00:00\",\"org_price\":\"99\",\"price\":\"69\",\"quan_link\":\"http:\\/\\/shop.m.taobao.com\\/shop\\/coupon.htm?seller_id=2077960768&activity_id=4e0312d3f3084da4bcb464f98f9720cc\",\"ali_click\":\"https:\\/\\/detail.tmall.com\\/item.htm?id=539051433071\",\"istmall\":\"1\",\"status\":\"1\",\"pic\":\"http:\\/\\/img.alicdn.com\\/imgextra\\/i4\\/2077960768\\/TB24QeOXCmK.eBjSZPfXXce2pXa_!!2077960768.jpg\"},{\"goodsid\":\"539051433071\",\"title\":\"秋冬毛呢大衣男中长款青年韩版修身风衣男潮时尚男士加厚呢子外套\",\"quan_price\":\"30\",\"quan_surplus\":\"9192\",\"quan_time\":\"2016-10-31 00:00:00\",\"org_price\":\"99\",\"price\":\"69\",\"quan_link\":\"http:\\/\\/shop.m.taobao.com\\/shop\\/coupon.htm?seller_id=2077960768&activity_id=4e0312d3f3084da4bcb464f98f9720cc\",\"ali_click\":\"https:\\/\\/detail.tmall.com\\/item.htm?id=539051433071\",\"istmall\":\"1\",\"status\":\"1\",\"pic\":\"http:\\/\\/img.alicdn.com\\/imgextra\\/i4\\/2077960768\\/TB24QeOXCmK.eBjSZPfXXce2pXa_!!2077960768.jpg\"},{\"goodsid\":\"539051433071\",\"title\":\"秋冬毛呢大衣男中长款青年韩版修身风衣男潮时尚男士加厚呢子外套\",\"quan_price\":\"30\",\"quan_surplus\":\"9192\",\"quan_time\":\"2016-10-31 00:00:00\",\"org_price\":\"99\",\"price\":\"69\",\"quan_link\":\"http:\\/\\/shop.m.taobao.com\\/shop\\/coupon.htm?seller_id=2077960768&activity_id=4e0312d3f3084da4bcb464f98f9720cc\",\"ali_click\":\"https:\\/\\/detail.tmall.com\\/item.htm?id=539051433071\",\"istmall\":\"1\",\"status\":\"1\",\"pic\":\"http:\\/\\/img.alicdn.com\\/imgextra\\/i4\\/2077960768\\/TB24QeOXCmK.eBjSZPfXXce2pXa_!!2077960768.jpg\"},{\"goodsid\":\"539051433071\",\"title\":\"秋冬毛呢大衣男中长款青年韩版修身风衣男潮时尚男士加厚呢子外套\",\"quan_price\":\"30\",\"quan_surplus\":\"9192\",\"quan_time\":\"2016-10-31 00:00:00\",\"org_price\":\"99\",\"price\":\"69\",\"quan_link\":\"http:\\/\\/shop.m.taobao.com\\/shop\\/coupon.htm?seller_id=2077960768&activity_id=4e0312d3f3084da4bcb464f98f9720cc\",\"ali_click\":\"https:\\/\\/detail.tmall.com\\/item.htm?id=539051433071\",\"istmall\":\"1\",\"status\":\"1\",\"pic\":\"http:\\/\\/img.alicdn.com\\/imgextra\\/i4\\/2077960768\\/TB24QeOXCmK.eBjSZPfXXce2pXa_!!2077960768.jpg\"},{\"goodsid\":\"539051433071\",\"title\":\"秋冬毛呢大衣男中长款青年韩版修身风衣男潮时尚男士加厚呢子外套\",\"quan_price\":\"30\",\"quan_surplus\":\"9192\",\"quan_time\":\"2016-10-31 00:00:00\",\"org_price\":\"99\",\"price\":\"69\",\"quan_link\":\"http:\\/\\/shop.m.taobao.com\\/shop\\/coupon.htm?seller_id=2077960768&activity_id=4e0312d3f3084da4bcb464f98f9720cc\",\"ali_click\":\"https:\\/\\/detail.tmall.com\\/item.htm?id=539051433071\",\"istmall\":\"1\",\"status\":\"1\",\"pic\":\"http:\\/\\/img.alicdn.com\\/imgextra\\/i4\\/2077960768\\/TB24QeOXCmK.eBjSZPfXXce2pXa_!!2077960768.jpg\"},{\"goodsid\":\"539051433071\",\"title\":\"秋冬毛呢大衣男中长款青年韩版修身风衣男潮时尚男士加厚呢子外套\",\"quan_price\":\"30\",\"quan_surplus\":\"9192\",\"quan_time\":\"2016-10-31 00:00:00\",\"org_price\":\"99\",\"price\":\"69\",\"quan_link\":\"http:\\/\\/shop.m.taobao.com\\/shop\\/coupon.htm?seller_id=2077960768&activity_id=4e0312d3f3084da4bcb464f98f9720cc\",\"ali_click\":\"https:\\/\\/detail.tmall.com\\/item.htm?id=539051433071\",\"istmall\":\"1\",\"status\":\"1\",\"pic\":\"http:\\/\\/img.alicdn.com\\/imgextra\\/i4\\/2077960768\\/TB24QeOXCmK.eBjSZPfXXce2pXa_!!2077960768.jpg\"},{\"goodsid\":\"539051433071\",\"title\":\"秋冬毛呢大衣男中长款青年韩版修身风衣男潮时尚男士加厚呢子外套\",\"quan_price\":\"30\",\"quan_surplus\":\"9192\",\"quan_time\":\"2016-10-31 00:00:00\",\"org_price\":\"99\",\"price\":\"69\",\"quan_link\":\"http:\\/\\/shop.m.taobao.com\\/shop\\/coupon.htm?seller_id=2077960768&activity_id=4e0312d3f3084da4bcb464f98f9720cc\",\"ali_click\":\"https:\\/\\/detail.tmall.com\\/item.htm?id=539051433071\",\"istmall\":\"1\",\"status\":\"1\",\"pic\":\"http:\\/\\/img.alicdn.com\\/imgextra\\/i4\\/2077960768\\/TB24QeOXCmK.eBjSZPfXXce2pXa_!!2077960768.jpg\"},{\"goodsid\":\"539051433071\",\"title\":\"秋冬毛呢大衣男中长款青年韩版修身风衣男潮时尚男士加厚呢子外套\",\"quan_price\":\"30\",\"quan_surplus\":\"9192\",\"quan_time\":\"2016-10-31 00:00:00\",\"org_price\":\"99\",\"price\":\"69\",\"quan_link\":\"http:\\/\\/shop.m.taobao.com\\/shop\\/coupon.htm?seller_id=2077960768&activity_id=4e0312d3f3084da4bcb464f98f9720cc\",\"ali_click\":\"https:\\/\\/detail.tmall.com\\/item.htm?id=539051433071\",\"istmall\":\"1\",\"status\":\"1\",\"pic\":\"http:\\/\\/img.alicdn.com\\/imgextra\\/i4\\/2077960768\\/TB24QeOXCmK.eBjSZPfXXce2pXa_!!2077960768.jpg\"},{\"goodsid\":\"539051433071\",\"title\":\"秋冬毛呢大衣男中长款青年韩版修身风衣男潮时尚男士加厚呢子外套\",\"quan_price\":\"30\",\"quan_surplus\":\"9192\",\"quan_time\":\"2016-10-31 00:00:00\",\"org_price\":\"99\",\"price\":\"69\",\"quan_link\":\"http:\\/\\/shop.m.taobao.com\\/shop\\/coupon.htm?seller_id=2077960768&activity_id=4e0312d3f3084da4bcb464f98f9720cc\",\"ali_click\":\"https:\\/\\/detail.tmall.com\\/item.htm?id=539051433071\",\"istmall\":\"1\",\"status\":\"1\",\"pic\":\"http:\\/\\/img.alicdn.com\\/imgextra\\/i4\\/2077960768\\/TB24QeOXCmK.eBjSZPfXXce2pXa_!!2077960768.jpg\"},{\"goodsid\":\"539051433071\",\"title\":\"秋冬毛呢大衣男中长款青年韩版修身风衣男潮时尚男士加厚呢子外套\",\"quan_price\":\"30\",\"quan_surplus\":\"9192\",\"quan_time\":\"2016-10-31 00:00:00\",\"org_price\":\"99\",\"price\":\"69\",\"quan_link\":\"http:\\/\\/shop.m.taobao.com\\/shop\\/coupon.htm?seller_id=2077960768&activity_id=4e0312d3f3084da4bcb464f98f9720cc\",\"ali_click\":\"https:\\/\\/detail.tmall.com\\/item.htm?id=539051433071\",\"istmall\":\"1\",\"status\":\"1\",\"pic\":\"http:\\/\\/img.alicdn.com\\/imgextra\\/i4\\/2077960768\\/TB24QeOXCmK.eBjSZPfXXce2pXa_!!2077960768.jpg\"},{\"goodsid\":\"539051433071\",\"title\":\"秋冬毛呢大衣男中长款青年韩版修身风衣男潮时尚男士加厚呢子外套\",\"quan_price\":\"30\",\"quan_surplus\":\"9192\",\"quan_time\":\"2016-10-31 00:00:00\",\"org_price\":\"99\",\"price\":\"69\",\"quan_link\":\"http:\\/\\/shop.m.taobao.com\\/shop\\/coupon.htm?seller_id=2077960768&activity_id=4e0312d3f3084da4bcb464f98f9720cc\",\"ali_click\":\"https:\\/\\/detail.tmall.com\\/item.htm?id=539051433071\",\"istmall\":\"1\",\"status\":\"1\",\"pic\":\"http:\\/\\/img.alicdn.com\\/imgextra\\/i4\\/2077960768\\/TB24QeOXCmK.eBjSZPfXXce2pXa_!!2077960768.jpg\"}]}";
    }

}