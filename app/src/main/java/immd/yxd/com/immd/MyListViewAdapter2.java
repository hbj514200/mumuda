package immd.yxd.com.immd;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import immd.yxd.com.immd.goods.fenlei_data;

public class MyListViewAdapter2 extends BaseAdapter{
    private  fenlei_data[][] dataList;
    private  Context context;
    private  int selectIndex;
    RequestQueue mQueue;

    public MyListViewAdapter2(fenlei_data[][] dataList, Context context, int selectIndex) {
        this.dataList=dataList;
        this.context=context;
        this.selectIndex=selectIndex;
        mQueue = Volley.newRequestQueue(context);
    }

    @Override
    public int getCount() {
        for(int i= 0; i<dataList[selectIndex].length; i++)
            if (dataList[selectIndex][i] == null)   return i;
        return dataList[selectIndex].length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_listview_2,null);
            vh=new ViewHolder();
            vh.im = (ImageView) convertView.findViewById(R.id.item_im);
            vh.tv = (TextView) convertView.findViewById(R.id.item_tv);
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }

        if(dataList[selectIndex][position] != null){
            vh.tv.setText( dataList[selectIndex][position].getName() );
            getImageView( vh.im, dataList[selectIndex][position].getIcon_url() );
        }

        return convertView;
    }

    public void setIndex(int index){
        selectIndex=index;
    }

    class ViewHolder{
        ImageView im;
        TextView tv;
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

}
