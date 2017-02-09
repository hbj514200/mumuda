package com.qq.qzone.a1336892373.zhekou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import com.qq.qzone.a1336892373.zhekou.goods.fenlei_data;

public class fenleiActivity extends Activity {

    private TextView you_title;
    RequestQueue mQueue;
    private int selectIndex=0;
    private ListView mListView1;
    private GridView mGridView2;
    private MyListViewAdapter1 adapter1;
    private fenlei_data[][] dataList = new fenlei_data[15][80];
    private String[] mMenus = {"女装","男装","鞋包","配饰","美食","数码","美妆","文体"};
    private MyListViewAdapter2 adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenlei);
        initView();
    }

    private void initView() {
        getStrs();
        you_title = (TextView) findViewById(R.id.fenlei_you_title);
        mQueue = Volley.newRequestQueue(fenleiActivity.this);
        ImageView back_button = (ImageView) findViewById(R.id.tab_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mListView1= (ListView) findViewById(R.id.list_item_1);
        mGridView2= (GridView) findViewById(R.id.list_item_2);

        adapter1=new MyListViewAdapter1(mMenus,this,selectIndex);
        adapter2=new MyListViewAdapter2(dataList,this,selectIndex);
        mListView1.setAdapter(adapter1);
        mGridView2.setAdapter(adapter2);

        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                you_title.setText( mMenus[position] );
                selectIndex=position;
                //把下标传过去，然后刷新adapter
                adapter1.setIndex(position);
                adapter1.notifyDataSetChanged();
                //当点击某个item的时候让这个item自动滑动到listview的顶部(下面item够多，如果点击的是最后一个就不能到达顶部了)
                mListView1.smoothScrollToPositionFromTop(position,0);

                adapter2.setIndex(position);
                mGridView2.setAdapter(adapter2);
            }
        });

        mGridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(fenleiActivity.this, fenlei_list_Activity.class);
                intent.putExtra("ftype", ""+(selectIndex+1) );
                intent.putExtra("stype", ""+(position+1) );
                startActivity( intent );
            }
        });

    }

    public void getStrs(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonArray=new JSONObject( getJson() ).getJSONObject("data").getJSONArray("categories");

                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        JSONArray array = jsonObject.getJSONArray("subcategories");
                        for (int j=0; j<array.length(); j++){
                            JSONObject object = (JSONObject) array.get(j);
                            fenlei_data data = new fenlei_data();
                            data.setIcon_url( object.getString("icon_url") );
                            data.setName( object.getString("name") );
                            dataList[i][j] = data;
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    Log.i("baicaijia", "getStr抛出异常");
                }
            }
        }).start();
    }

    private String getJson(){
        return "{\n" +
                "    \"code\": 200,\n" +
                "    \"data\": {\n" +
                "        \"categories\": [\n" +
                "            {\n" +
                "                \"icon_url\": \"http://img03.liwushuo.com/image/150615/3nc5tejwl.png-pw144\",\n" +
                "                \"id\": 1,\n" +
                "                \"name\": \"女装\",\n" +
                "                \"order\": 11,\n" +
                "                \"status\": 0,\n" +
                "                \"subcategories\": [\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/1_1.png\",\n" +
                "                        \"id\": 7,\n" +
                "                        \"items_count\": -45,\n" +
                "                        \"name\": \"短袖\",\n" +
                "                        \"order\": 7,\n" +
                "                        \"parent_id\": 1,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/1_2.png\",\n" +
                "                        \"id\": 6,\n" +
                "                        \"items_count\": -210,\n" +
                "                        \"name\": \"外套\",\n" +
                "                        \"order\": 6,\n" +
                "                        \"parent_id\": 1,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/1_3.png\",\n" +
                "                        \"id\": 5,\n" +
                "                        \"items_count\": -18,\n" +
                "                        \"name\": \"背心\",\n" +
                "                        \"order\": 5,\n" +
                "                        \"parent_id\": 1,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/1_4.png\",\n" +
                "                        \"id\": 4,\n" +
                "                        \"items_count\": -131,\n" +
                "                        \"name\": \"泳衣\",\n" +
                "                        \"order\": 4,\n" +
                "                        \"parent_id\": 1,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/1_5.png\",\n" +
                "                        \"id\": 3,\n" +
                "                        \"items_count\": -68,\n" +
                "                        \"name\": \"内衣\",\n" +
                "                        \"order\": 3,\n" +
                "                        \"parent_id\": 1,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/1_6.png\",\n" +
                "                        \"id\": 2,\n" +
                "                        \"items_count\": -72,\n" +
                "                        \"name\": \"长裤\",\n" +
                "                        \"order\": 2,\n" +
                "                        \"parent_id\": 1,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/1_7.png\",\n" +
                "                        \"id\": 1,\n" +
                "                        \"items_count\": -469,\n" +
                "                        \"name\": \"短裤\",\n" +
                "                        \"order\": 1,\n" +
                "                        \"parent_id\": 1,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/1_8.png\",\n" +
                "                        \"id\": 2,\n" +
                "                        \"items_count\": -72,\n" +
                "                        \"name\": \"套装\",\n" +
                "                        \"order\": 2,\n" +
                "                        \"parent_id\": 1,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/1_9.png\",\n" +
                "                                  \"id\": 2,\n" +
                "                                  \"items_count\": -72,\n" +
                "                                  \"name\": \"半身群\",\n" +
                "                                  \"order\": 2,\n" +
                "                                  \"parent_id\": 1,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/1_10.png\",\n" +
                "                                  \"id\": 2,\n" +
                "                                  \"items_count\": -72,\n" +
                "                                  \"name\": \"连衣裙\",\n" +
                "                                  \"order\": 2,\n" +
                "                                  \"parent_id\": 1,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/1_11.png\",\n" +
                "                                  \"id\": 2,\n" +
                "                                  \"items_count\": -72,\n" +
                "                                  \"name\": \"运动装\",\n" +
                "                                  \"order\": 2,\n" +
                "                                  \"parent_id\": 1,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/1_12.png\",\n" +
                "                                  \"id\": 2,\n" +
                "                                  \"items_count\": -72,\n" +
                "                                  \"name\": \"卫衣\",\n" +
                "                                  \"order\": 2,\n" +
                "                                  \"parent_id\": 1,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/1_13.png\",\n" +
                "                                  \"id\": 2,\n" +
                "                                  \"items_count\": -72,\n" +
                "                                  \"name\": \"长袖\",\n" +
                "                                  \"order\": 2,\n" +
                "                                  \"parent_id\": 1,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/1_14.png\",\n" +
                "                                  \"id\": 2,\n" +
                "                                  \"items_count\": -72,\n" +
                "                                  \"name\": \"衬衫\",\n" +
                "                                  \"order\": 2,\n" +
                "                                  \"parent_id\": 1,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/1_15.png\",\n" +
                "                                  \"id\": 2,\n" +
                "                                  \"items_count\": -72,\n" +
                "                                  \"name\": \"毛衣\",\n" +
                "                                  \"order\": 2,\n" +
                "                                  \"parent_id\": 1,\n" +
                "                                  \"status\": 0\n" +
                "                                  }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"icon_url\": \"http://img01.liwushuo.com/image/150427/as9ys97f0.png-pw144\",\n" +
                "                \"id\": 2,\n" +
                "                \"name\": \"男装 \",\n" +
                "                \"order\": 10,\n" +
                "                \"status\": 0,\n" +
                "                \"subcategories\": [\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/2_1.png\",\n" +
                "                        \"id\": 13,\n" +
                "                        \"items_count\": -33,\n" +
                "                        \"name\": \"衬衫\",\n" +
                "                        \"order\": 6,\n" +
                "                        \"parent_id\": 2,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/2_2.png\",\n" +
                "                        \"id\": 12,\n" +
                "                        \"items_count\": -94,\n" +
                "                        \"name\": \"短袖\",\n" +
                "                        \"order\": 5,\n" +
                "                        \"parent_id\": 2,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/2_3.png\",\n" +
                "                        \"id\": 11,\n" +
                "                        \"items_count\": 200,\n" +
                "                        \"name\": \"短裤\",\n" +
                "                        \"order\": 4,\n" +
                "                        \"parent_id\": 2,\n" +
                "                        \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/2_4.png\",\n" +
                "                                  \"id\": 10,\n" +
                "                                  \"items_count\": 22,\n" +
                "                                  \"name\": \"长裤\",\n" +
                "                                  \"order\": 3,\n" +
                "                                  \"parent_id\": 2,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/2_5.png\",\n" +
                "                        \"id\": 10,\n" +
                "                        \"items_count\": 22,\n" +
                "                        \"name\": \"外套\",\n" +
                "                        \"order\": 3,\n" +
                "                        \"parent_id\": 2,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/2_6.png\",\n" +
                "                        \"id\": 9,\n" +
                "                        \"items_count\": -210,\n" +
                "                        \"name\": \"卫衣\",\n" +
                "                        \"order\": 2,\n" +
                "                        \"parent_id\": 2,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/2_7.png\",\n" +
                "                        \"id\": 8,\n" +
                "                        \"items_count\": 72,\n" +
                "                        \"name\": \"内衣\",\n" +
                "                        \"order\": 1,\n" +
                "                        \"parent_id\": 2,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/2_8.png\",\n" +
                "                        \"id\": 14,\n" +
                "                        \"items_count\": 4,\n" +
                "                        \"name\": \"毛衣\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 2,\n" +
                "                        \"status\": 0\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"icon_url\": \"http://img03.liwushuo.com/image/150615/ap97rx8is.png-pw144\",\n" +
                "                \"id\": 3,\n" +
                "                \"name\": \"鞋包\",\n" +
                "                \"order\": 9,\n" +
                "                \"status\": 0,\n" +
                "                \"subcategories\": [\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/3_1.png\",\n" +
                "                        \"id\": 18,\n" +
                "                        \"items_count\": 40,\n" +
                "                        \"name\": \"单肩包\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 3,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/3_2.png\",\n" +
                "                        \"id\": 29,\n" +
                "                        \"items_count\": 75,\n" +
                "                        \"name\": \"双肩包\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 3,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/3_3.png\",\n" +
                "                        \"id\": 28,\n" +
                "                        \"items_count\": -434,\n" +
                "                        \"name\": \"手拿包\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 3,\n" +
                "                        \"status\": 0\n" +
                "                                  },{\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/3_4.png\",\n" +
                "                                  \"id\": 28,\n" +
                "                                  \"items_count\": -434,\n" +
                "                                  \"name\": \"手提包\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 3,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  \n" +
                "                                  \n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/3_5.png\",\n" +
                "                        \"id\": 27,\n" +
                "                        \"items_count\": 6,\n" +
                "                        \"name\": \"化妆包\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 3,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/3_6.png\",\n" +
                "                        \"id\": 26,\n" +
                "                        \"items_count\": 415,\n" +
                "                        \"name\": \"女钱包\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 3,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/3_7.png\",\n" +
                "                        \"id\": 25,\n" +
                "                        \"items_count\": 190,\n" +
                "                        \"name\": \"行李箱\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 3,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/3_8.png\",\n" +
                "                        \"id\": 24,\n" +
                "                        \"items_count\": 71,\n" +
                "                        \"name\": \"男钱包\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 3,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/3_9.png\",\n" +
                "                        \"id\": 23,\n" +
                "                        \"items_count\": -132,\n" +
                "                        \"name\": \"帆布包\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 3,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/3_10.png\",\n" +
                "                        \"id\": 22,\n" +
                "                        \"items_count\": 376,\n" +
                "                        \"name\": \"平底鞋\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 3,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/3_11.png\",\n" +
                "                        \"id\": 19,\n" +
                "                        \"items_count\": -23,\n" +
                "                        \"name\": \"高跟鞋\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 3,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/3_12.png\",\n" +
                "                        \"id\": 95,\n" +
                "                        \"items_count\": 27,\n" +
                "                        \"name\": \"凉鞋\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 3,\n" +
                "                        \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/3_13.png\",\n" +
                "                                  \"id\": 95,\n" +
                "                                  \"items_count\": 27,\n" +
                "                                  \"name\": \"休闲鞋\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 3,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/3_14.png\",\n" +
                "                                  \"id\": 95,\n" +
                "                                  \"items_count\": 27,\n" +
                "                                  \"name\": \"运动鞋\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 3,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/3_15.png\",\n" +
                "                                  \"id\": 95,\n" +
                "                                  \"items_count\": 27,\n" +
                "                                  \"name\": \"家居鞋\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 3,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/3_16.png\",\n" +
                "                                  \"id\": 95,\n" +
                "                                  \"items_count\": 27,\n" +
                "                                  \"name\": \"帆布鞋\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 3,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/3_17.png\",\n" +
                "                                  \"id\": 95,\n" +
                "                                  \"items_count\": 27,\n" +
                "                                  \"name\": \"雨鞋\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 3,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/3_18.png\",\n" +
                "                                  \"id\": 95,\n" +
                "                                  \"items_count\": 27,\n" +
                "                                  \"name\": \"短靴\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 3,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/3_19.png\",\n" +
                "                                  \"id\": 95,\n" +
                "                                  \"items_count\": 27,\n" +
                "                                  \"name\": \"皮鞋\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 3,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/3_20.png\",\n" +
                "                                  \"id\": 95,\n" +
                "                                  \"items_count\": 27,\n" +
                "                                  \"name\": \"男休闲鞋\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 3,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/3_21.png\",\n" +
                "                                  \"id\": 95,\n" +
                "                                  \"items_count\": 27,\n" +
                "                                  \"name\": \"男运动鞋\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 3,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/3_22.png\",\n" +
                "                                  \"id\": 95,\n" +
                "                                  \"items_count\": 27,\n" +
                "                                  \"name\": \"男帆布鞋\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 3,\n" +
                "                                  \"status\": 0\n" +
                "                                  }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"icon_url\": \"http://img03.liwushuo.com/image/150427/qk1wazhvq.png-pw144\",\n" +
                "                \"id\": 4,\n" +
                "                \"name\": \"配饰\",\n" +
                "                \"order\": 8,\n" +
                "                \"status\": 0,\n" +
                "                \"subcategories\": [\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/4_1.png\",\n" +
                "                        \"id\": 30,\n" +
                "                        \"items_count\": 81,\n" +
                "                        \"name\": \"公仔\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 4,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/4_2.png\",\n" +
                "                        \"id\": 31,\n" +
                "                        \"items_count\": -131,\n" +
                "                        \"name\": \"手饰\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 4,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/4_3.png\",\n" +
                "                        \"id\": 32,\n" +
                "                        \"items_count\": 259,\n" +
                "                        \"name\": \"手表\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 4,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/4_4.png\",\n" +
                "                        \"id\": 33,\n" +
                "                        \"items_count\": -116,\n" +
                "                        \"name\": \"帽子\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 4,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/4_5.png\",\n" +
                "                        \"id\": 34,\n" +
                "                        \"items_count\": 109,\n" +
                "                        \"name\": \"镜框\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 4,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/4_6.png\",\n" +
                "                        \"id\": 35,\n" +
                "                        \"items_count\": 42,\n" +
                "                        \"name\": \"发饰\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 4,\n" +
                "                        \"status\": 0\n" +
                "                                  },{\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/4_7.png\",\n" +
                "                                  \"id\": 35,\n" +
                "                                  \"items_count\": 42,\n" +
                "                                  \"name\": \"耳饰\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 4,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/4_8.png\",\n" +
                "                        \"id\": 38,\n" +
                "                        \"items_count\": 51,\n" +
                "                        \"name\": \"手套\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 4,\n" +
                "                        \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/4_9.png\",\n" +
                "                                  \"id\": 38,\n" +
                "                                  \"items_count\": 51,\n" +
                "                                  \"name\": \"围巾\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 4,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/4_10.png\",\n" +
                "                                  \"id\": 38,\n" +
                "                                  \"items_count\": 51,\n" +
                "                                  \"name\": \"项链\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 4,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/4_11.png\",\n" +
                "                                  \"id\": 38,\n" +
                "                                  \"items_count\": 51,\n" +
                "                                  \"name\": \"领带\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 4,\n" +
                "                                  \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/4_12.png\",\n" +
                "                                  \"id\": 38,\n" +
                "                                  \"items_count\": 51,\n" +
                "                                  \"name\": \"袜子\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 4,\n" +
                "                                  \"status\": 0\n" +
                "                                  }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"icon_url\": \"http://img02.liwushuo.com/image/150615/p3k0won53.png-pw144\",\n" +
                "                \"id\": 5,\n" +
                "                \"name\": \"美食\",\n" +
                "                \"order\": 7,\n" +
                "                \"status\": 0,\n" +
                "                \"subcategories\": [\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/5_1.png\",\n" +
                "                        \"id\": 81,\n" +
                "                        \"items_count\": -11,\n" +
                "                        \"name\": \"速食\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 5,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/5_2.png\",\n" +
                "                        \"id\": 97,\n" +
                "                        \"items_count\": 349,\n" +
                "                        \"name\": \"糖果糕点\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 5,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/5_3.png\",\n" +
                "                        \"id\": 92,\n" +
                "                        \"items_count\": 93,\n" +
                "                        \"name\": \"饼干\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 5,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/5_4.png\",\n" +
                "                        \"id\": 89,\n" +
                "                        \"items_count\": -89,\n" +
                "                        \"name\": \"巧可力\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 5,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/5_5.png\",\n" +
                "                        \"id\": 88,\n" +
                "                        \"items_count\": 14,\n" +
                "                        \"name\": \"膨化食品\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 5,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/5_6.png\",\n" +
                "                        \"id\": 87,\n" +
                "                        \"items_count\": 8,\n" +
                "                        \"name\": \"咖啡\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 5,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/5_7.png\",\n" +
                "                        \"id\": 86,\n" +
                "                        \"items_count\": -3,\n" +
                "                        \"name\": \"酒\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 5,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/5_8.png\",\n" +
                "                        \"id\": 85,\n" +
                "                        \"items_count\": 66,\n" +
                "                        \"name\": \"坚果\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 5,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/5_9.png\",\n" +
                "                        \"id\": 84,\n" +
                "                        \"items_count\": -141,\n" +
                "                        \"name\": \"凉果\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 5,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/5_10.png\",\n" +
                "                        \"id\": 83,\n" +
                "                        \"items_count\": 81,\n" +
                "                        \"name\": \"饮料\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 5,\n" +
                "                        \"status\": 0\n" +
                "                    }\n" +
                "                                  \n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"icon_url\": \"http://img03.liwushuo.com/image/150615/qybe9x9i9.png-pw144\",\n" +
                "                \"id\": 6,\n" +
                "                \"name\": \"数码\",\n" +
                "                \"order\": 6,\n" +
                "                \"status\": 0,\n" +
                "                \"subcategories\": [\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/6_1.png\",\n" +
                "                        \"id\": 39,\n" +
                "                        \"items_count\": 40,\n" +
                "                        \"name\": \"电脑周边\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 6,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/6_2.png\",\n" +
                "                        \"id\": 40,\n" +
                "                        \"items_count\": 39,\n" +
                "                        \"name\": \"耳机/音响\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 6,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/6_3.png\",\n" +
                "                        \"id\": 41,\n" +
                "                        \"items_count\": -69,\n" +
                "                        \"name\": \"摄影周边\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 6,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/6_4.png\",\n" +
                "                        \"id\": 42,\n" +
                "                        \"items_count\": 63,\n" +
                "                        \"name\": \"手机壳\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 6,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/6_5.png\",\n" +
                "                        \"id\": 43,\n" +
                "                        \"items_count\": 40,\n" +
                "                        \"name\": \"手机周边\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 6,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/6_6.png\",\n" +
                "                        \"id\": 46,\n" +
                "                        \"items_count\": -131,\n" +
                "                        \"name\": \"移动电源\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 6,\n" +
                "                        \"status\": 0\n" +
                "                                  },\n" +
                "                                  {\n" +
                "                                  \"icon_url\": \"http://www.imumuda.cn/image/6_7.png\",\n" +
                "                                  \"id\": 46,\n" +
                "                                  \"items_count\": -131,\n" +
                "                                  \"name\": \"剃须刀\",\n" +
                "                                  \"order\": 0,\n" +
                "                                  \"parent_id\": 6,\n" +
                "                                  \"status\": 0\n" +
                "                                  }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"icon_url\": \"http://img01.liwushuo.com/image/150427/cu47bnpjd.png-pw144\",\n" +
                "                \"id\": 7,\n" +
                "                \"name\": \"美妆\",\n" +
                "                \"order\": 5,\n" +
                "                \"status\": 0,\n" +
                "                \"subcategories\": [\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/7_1.png\",\n" +
                "                        \"id\": 47,\n" +
                "                        \"items_count\": 23,\n" +
                "                        \"name\": \"美容护肤\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 7,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/7_2.png\",\n" +
                "                        \"id\": 48,\n" +
                "                        \"items_count\": -93,\n" +
                "                        \"name\": \"彩妆香水\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 7,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/7_3.png\",\n" +
                "                        \"id\": 49,\n" +
                "                        \"items_count\": -17,\n" +
                "                        \"name\": \"护理按摩\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 7,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/7_4.png\",\n" +
                "                        \"id\": 50,\n" +
                "                        \"items_count\": -35,\n" +
                "                        \"name\": \"美发护发\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 7,\n" +
                "                        \"status\": 0\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"icon_url\": \"http://img03.liwushuo.com/image/150615/6f7muy9up.png-pw144\",\n" +
                "                \"id\": 19,\n" +
                "                \"name\": \"文体\",\n" +
                "                \"order\": 3,\n" +
                "                \"status\": 0,\n" +
                "                \"subcategories\": [\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/8_1.png\",\n" +
                "                        \"id\": 143,\n" +
                "                        \"items_count\": -56,\n" +
                "                        \"name\": \"户外装备\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 19,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/8_2.png\",\n" +
                "                        \"id\": 154,\n" +
                "                        \"items_count\": 15,\n" +
                "                        \"name\": \"运动健身\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 19,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/8_3.png\",\n" +
                "                        \"id\": 155,\n" +
                "                        \"items_count\": 86,\n" +
                "                        \"name\": \"办公收纳\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 19,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/8_4.png\",\n" +
                "                        \"id\": 157,\n" +
                "                        \"items_count\": -127,\n" +
                "                        \"name\": \"本子\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 19,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/8_5.png\",\n" +
                "                        \"id\": 159,\n" +
                "                        \"items_count\": -90,\n" +
                "                        \"name\": \"笔\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 19,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/8_6.png\",\n" +
                "                        \"id\": 160,\n" +
                "                        \"items_count\": 161,\n" +
                "                        \"name\": \"贺卡\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 19,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/8_7.png\",\n" +
                "                        \"id\": 162,\n" +
                "                        \"items_count\": -31,\n" +
                "                        \"name\": \"卡包\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 19,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/8_8.png\",\n" +
                "                        \"id\": 163,\n" +
                "                        \"items_count\": 16,\n" +
                "                        \"name\": \"相框\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 19,\n" +
                "                        \"status\": 0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"icon_url\": \"http://www.imumuda.cn/image/8_9.png\",\n" +
                "                        \"id\": 181,\n" +
                "                        \"items_count\": -7,\n" +
                "                        \"name\": \"书签\",\n" +
                "                        \"order\": 0,\n" +
                "                        \"parent_id\": 19,\n" +
                "                        \"status\": 0\n" +
                "                    }\n" +
                "                    \n" +
                "               ]\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"message\": \"OK\"\n" +
                "}";
    }


}