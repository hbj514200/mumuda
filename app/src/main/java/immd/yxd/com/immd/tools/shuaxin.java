package immd.yxd.com.immd.tools;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;

import immd.yxd.com.immd.R;

import static android.R.id.list;

public class shuaxin {

    public static void xiala(View view, int rouseId, final CallBack callBack){
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById( rouseId );
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_orange_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        callBack.getStrs();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 4000);
            }
        });
    }
    public static void xiala(final SwipeRefreshLayout swipeRefreshLayout, final CallBack callBack){
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_orange_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        callBack.getStrs();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 4000);
            }
        });     //Activity 的重载
    }

    public static void shangla(ListView listView, final CallBack callBack){
        listView.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override public void onScroll(AbsListView absListView, int i, int i1, int i2) { }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState){
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        callBack.getStrs();
                    }
                }
            }
        });
    }
    public static void shangla(GridView gridView, final CallBack callBack){
        gridView.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override public void onScroll(AbsListView absListView, int i, int i1, int i2) { }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState){
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        callBack.getStrs();
                    }
                }
            }
        });           //Gridview的重载
    }

}
