package immd.yxd.com.immd;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * Created by a on 2016/5/13.
 */
public class MyListViewAdapter2 extends BaseAdapter{
    private  String[][] allData;
    private  Context context;
    private  int selectIndex;

    public MyListViewAdapter2(String[][] allData, Context context, int selectIndex) {
        this.allData=allData;
        this.context=context;
        this.selectIndex=selectIndex;
    }

    @Override
    public int getCount() {
        return allData[selectIndex].length;
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
            vh.tv= (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }

        vh.tv.setText(allData[selectIndex][position]);

        return convertView;
    }

    public void setIndex(int index){
        selectIndex=index;
    }

    class ViewHolder{
        TextView tv;
    }
}
