package immd.yxd.com.immd;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import immd.yxd.com.immd.goods.fenlei_data;
import immd.yxd.com.immd.tools.httpConn;

public class MyListViewAdapter2 extends BaseAdapter{
    private  fenlei_data[][] dataList;
    private  Context context;
    private  int selectIndex;
    private httpConn connect;

    public MyListViewAdapter2(fenlei_data[][] dataList, Context context, int selectIndex) {
        this.dataList=dataList;
        this.context=context;
        this.selectIndex=selectIndex;
        connect = httpConn.newInstance(context);
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
            connect.getImageView( vh.im, dataList[selectIndex][position].getIcon_url(), 200);
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

}
