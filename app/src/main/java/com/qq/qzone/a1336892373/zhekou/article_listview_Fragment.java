package com.qq.qzone.a1336892373.zhekou;

import android.content.Context;
import android.content.Intent;
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
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import com.qq.qzone.a1336892373.zhekou.goods.baicai_data;
import com.qq.qzone.a1336892373.zhekou.tools.httpConn;
import com.qq.qzone.a1336892373.zhekou.tools.myIntent;

public class article_listview_Fragment extends Fragment implements AdapterView.OnItemClickListener {

    private httpConn connect;
    private String goodsid;
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
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);
        connect = httpConn.newInstance(getActivity());
        listView = (ListView) view.findViewById(R.id.baicaijia_listview);
        listView.setDivider(null);      //去掉listview分割线
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
            }

            connect.displayImg(viewHolder.imageView, dataList.get(position).getPic(), 0);
            viewHolder.juan.setText( "劵："+dataList.get(position).getQuan_price() );
            viewHolder.qian.setText( "¥：" + dataList.get(position).getPrice() );
            viewHolder.xiangqin.setText( dataList.get(position).getDesc() );
            viewHolder.title.setText( dataList.get(position).getTitle() );

            return view;
        }

    }

    public void jsonOK(List<baicai_data> dataList){
        View header = getHeader();
        listView.addHeaderView(header);
        adapter = new myadapter(getActivity(), R.layout.article_listview_item, dataList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void getStrs(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String  url = "http://119.29.32.91/index.php?m=api&c=index&a=article&count=20&id=" + goodsid + "&page=" + (page++);
                    String response = connect.getData(url);

                    JSONArray jsonArray=new JSONObject(response).getJSONArray("msg");

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
                    page--;
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = myIntent.getIntent( dataList.get(position), new Intent(getActivity(), contentActivity.class) );
        startActivity(intent);
    }

    private View getHeader(){
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.article_listview_header, null);
        ImageView imageView = (ImageView) header.findViewById(R.id.item_imageview);
        TextView title = (TextView) header.findViewById(R.id.item_title);
        TextView xiangqin = (TextView) header.findViewById(R.id.item_xiangqin);

        connect.getImageView( imageView, getActivity().getIntent().getStringExtra("pic"), 0 );
        title.setText( getActivity().getIntent().getStringExtra("title") );
        xiangqin.setText( getActivity().getIntent().getStringExtra("desc") );
        return header;
    }

}