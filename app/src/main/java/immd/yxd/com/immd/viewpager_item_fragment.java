package immd.yxd.com.immd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import immd.yxd.com.immd.goods.baicai_data;
import immd.yxd.com.immd.tools.CallBack;
import immd.yxd.com.immd.tools.httpConn;
import immd.yxd.com.immd.tools.myIntent;
import immd.yxd.com.immd.tools.shuaxin;

public class viewpager_item_fragment extends Fragment implements AdapterView.OnItemClickListener, CallBack {
    public int Num = 1;
    private httpConn connect;
    private GridView gridView;
    private int page = 1;                                                                            //列表页数
    public int ftype = 1;
    public int stype = 1;
    private boolean wangwenOK = false;
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
        gridView.setOnItemClickListener(this);
        connect = httpConn.newInstance(getActivity());

        shuaxin.xiala(view, R.id.swipe_container, this);
        shuaxin.shangla(gridView, this);

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

            String juan_st = "劵:¥ "+dataList.get(position).getQuan_price();

            connect.getImageView( viewHolder.imageView, dataList.get(position).getPic(), 300 );
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
        adapter = new myadapter(getActivity(), R.layout.grid_item, dataList);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (dataList.size() == 0 && wangwenOK ){
            Toast toast = Toast.makeText(getActivity(), "该分类暂时没有商品", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 10);
            toast.show();
        }
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
                    Log.i("测试", "Num: "+Num );
                    String url;
                    if (Num==514200)    url = "http://119.29.32.91/index.php?m=api&c=index&a=goods&count=30&page="+(page++);         //第一项全部数据。貌似不需要加这个参数
                    else if (Num==1000) url = "http://119.29.32.91/index.php?m=api&c=index&a=goods&count=30&ftype="+ftype+"&stype="+stype+"&page="+(page++);       //好像是分类列表那里用的
                    else if (Num==9)    url = "http://119.29.32.91/index.php?m=api&c=index&a=advance&page="+(page++);               //预告，单独接口
                    else                url = "http://119.29.32.91/index.php?m=api&c=index&a=goods&count=30&page="+(page++)+"&ftype="+(Num);                       //应该是福利园首页
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
                        data.setQuan_time( jsonObject.getString("quan_time") );
                        data.setQuan_price( jsonObject.getString("quan_price") );
                        data.setQuan_link( jsonObject.getString("quan_link") );
                        dataList.add(data);
                    }
                    if (page==2){
                        wangwenOK = true;
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
        Intent intent = myIntent.getIntent(dataList.get(position), new Intent(getActivity(), contentActivity.class));
        startActivity(intent);
    }

}