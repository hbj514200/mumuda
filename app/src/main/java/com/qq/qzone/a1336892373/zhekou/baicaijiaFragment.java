package com.qq.qzone.a1336892373.zhekou;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import com.qq.qzone.a1336892373.zhekou.goods.baicai_data;
import com.qq.qzone.a1336892373.zhekou.tools.CallBack;
import com.qq.qzone.a1336892373.zhekou.tools.httpConn;
import com.qq.qzone.a1336892373.zhekou.tools.myIntent;
import com.qq.qzone.a1336892373.zhekou.tools.shuaxin;

public class baicaijiaFragment extends Fragment implements AdapterView.OnItemClickListener , CallBack {

    private httpConn connect;
    private ListView listView;
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
        View view = inflater.inflate(R.layout.fragment_baicaijia, container, false);
        listView = (ListView) view.findViewById(R.id.baicaijia_listview);
        connect = httpConn.newInstance(getActivity());
        listView.setOnItemClickListener(this);

        shuaxin.xiala(view, R.id.swipe_container, this);
        shuaxin.shangla(listView, this);

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
                view = LayoutInflater.from(getActivity()).inflate(R.layout.baicaijia_item, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) view.findViewById(R.id.item_imageview);
                viewHolder.yuanjia = (TextView) view.findViewById(R.id.item_yuanjia);
                viewHolder.yuanjia.getPaint().setAntiAlias(true);//抗锯齿
                viewHolder.yuanjia.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
                viewHolder.juan = (TextView) view.findViewById(R.id.item_juan);
                viewHolder.xiangqin = (TextView) view.findViewById(R.id.item_xiangqin);
                viewHolder.tianmao = (TextView) view.findViewById(R.id.item_tiaomao);
                viewHolder.qian = (TextView) view.findViewById(R.id.item_qian);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            String juan_st = " 劵:¥ "+dataList.get(position).getQuan_price();

            connect.getImageView(viewHolder.imageView, dataList.get(position).getPic(), 300);
            viewHolder.yuanjia.setText( dataList.get(position).getOrg_Price() );
            viewHolder.juan.setText( juan_st );
            viewHolder.qian.setText( dataList.get(position).getPrice() );
            viewHolder.xiangqin.setText( dataList.get(position).getTitle() );
            if (dataList.get(position).getIsTmall().equals("1")) viewHolder.tianmao.setText("天猫");
            else                                                 viewHolder.tianmao.setVisibility(View.INVISIBLE);

            return view;
        }

    }

    public void jsonOK(List<baicai_data> dataList){
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_foot, null);
        listView.addFooterView(footer);
        adapter = new myadapter(getActivity(), R.layout.baicaijia_item, dataList);
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
                    String url = "http://119.29.32.91/index.php?m=api&c=index&a=lowCost&count=30&page="+(page++);
                    String response = httpConn.getData(url);

                    JSONArray jsonArray=new JSONObject(response).getJSONArray("msg");
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
                        data.setQuan_time( jsonObject.getString("quan_time") );
                        data.setQuan_link( jsonObject.getString("quan_link") );
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
                    page--;
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = myIntent.getIntent( dataList.get(position), new Intent(getActivity(), contentActivity.class) );
        startActivity(intent);
    }

}