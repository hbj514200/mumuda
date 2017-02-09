package com.qq.qzone.a1336892373.zhekou;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.qq.qzone.a1336892373.zhekou.goods.article_data;
import com.qq.qzone.a1336892373.zhekou.tools.CallBack;
import com.qq.qzone.a1336892373.zhekou.tools.httpConn;
import com.qq.qzone.a1336892373.zhekou.tools.myIntent;
import com.qq.qzone.a1336892373.zhekou.tools.shuaxin;

public class youpinquFragment extends Fragment implements AdapterView.OnItemClickListener, CallBack {

    private httpConn connect;
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
        connect = httpConn.newInstance( getActivity() );
        view.setBackgroundColor(Color.parseColor("#ffffff"));

        shuaxin.xiala(view, R.id.swipe_container, this);         //下拉刷新
        shuaxin.shangla(listView, this);

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
                viewHolder.title = (TextView) view.findViewById(R.id.item_title);
                viewHolder.xiangqin = (TextView) view.findViewById(R.id.item_xiangqin);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            connect.displayImg( viewHolder.imageView, dataList.get(position).getPic(), 0);
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
                    String url = "http://119.29.32.91/index.php?m=api&c=index&a=artList&count=30&page=" + (page++);
                    String response = httpConn.getData(url);

                    JSONArray jsonArray=new JSONObject(response).getJSONArray("msg");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                        article_data data = new article_data();

                        data.setId( jsonObject.getString("id") );
                        data.setPic( jsonObject.getString("pic") );
                        data.setTitle( jsonObject.getString("title") );
                        data.setDesc( jsonObject.getString("desc") );
                        if ( !jsonObject.getString("pic").equals("1") )
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
        TextView xiangqin;
        TextView title;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(getActivity(), articleActivity.class);
        intent = myIntent.getArticleIntent( dataList.get(position), intent );
        startActivity(intent);
    }

}