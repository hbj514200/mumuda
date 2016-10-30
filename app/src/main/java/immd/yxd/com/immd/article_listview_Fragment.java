package immd.yxd.com.immd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import immd.yxd.com.immd.tools.myIntent;

public class article_listview_Fragment extends Fragment implements AdapterView.OnItemClickListener {

    private String goodsid;
    RequestQueue mQueue;
    private ListView listView;
    private int page = 1;                                                                            //列表页数
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baicaijia, container, false);
        listView = (ListView) view.findViewById(R.id.baicaijia_listview);
        listView.setDivider(null);      //去掉listview分割线
        mQueue = Volley.newRequestQueue(getActivity());
        listView.setOnItemClickListener(this);

        return view;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
        getStrs();
    }

    public class myadapter extends ArrayAdapter<baicai_data>  {

        public myadapter(Context context, int resouId, List<baicai_data> object) {
            super(context, resouId, object);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if (convertView == null){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.article_listview_item, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) view.findViewById(R.id.item_imageview);
                viewHolder.juan = (TextView) view.findViewById(R.id.item_juan);
                viewHolder.xiangqin = (TextView) view.findViewById(R.id.item_xiangqin);
                viewHolder.qian = (TextView) view.findViewById(R.id.item_qian);
                viewHolder.title = (TextView) view.findViewById(R.id.item_title);
                viewHolder.button = (Button) view.findViewById(R.id.item_buy_button);
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = myIntent.getIntent( dataList.get(position), new Intent(getActivity(), contentActivity.class) );
                        startActivity(intent);
                    }
                });
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
                if (viewHolder.imageView != null)   viewHolder.imageView.setVisibility(View.INVISIBLE);
            }

            getImageView( viewHolder.imageView, dataList.get(position).getPic() );
            viewHolder.juan.setText( "劵："+dataList.get(position).getQuan_price() );
            viewHolder.qian.setText( "¥：" + dataList.get(position).getPrice() );
            viewHolder.xiangqin.setText( dataList.get(position).getDesc() );
            viewHolder.title.setText( dataList.get(position).getTitle() );

            return view;
        }

    }

    public void jsonOK(List<baicai_data> dataList){
        adapter = new myadapter(getActivity(), R.layout.article_listview_item, dataList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void getStrs(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://119.29.32.91/index.php?m=api&c=index&a=article&id=" + goodsid);
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

                        data.setGoodsID( jsonObject.getString("id") );
                        data.setPrice( jsonObject.getString("price") );
                        data.setPic( jsonObject.getString("pic") );
                        data.setDesc( jsonObject.getString("desc") );
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
        TextView title;
        ImageView imageView;
        TextView juan;
        TextView xiangqin;
        TextView qian;
        Button button;
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = myIntent.getIntent( dataList.get(position), new Intent(getActivity(), contentActivity.class) );
        startActivity(intent);
    }

}