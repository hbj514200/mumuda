package com.qq.qzone.a1336892373.zhekou.tools;

import android.content.Intent;
import com.qq.qzone.a1336892373.zhekou.goods.article_data;
import com.qq.qzone.a1336892373.zhekou.goods.baicai_data;

public class myIntent {

    public static Intent getIntent(baicai_data data, Intent intent){
        intent.putExtra("quan_time", data.getQuan_time() );
        intent.putExtra("goodsid", data.getGoodsID() );
        intent.putExtra("price", data.getPrice() );
        intent.putExtra("org_price", data.getOrg_Price() );
        intent.putExtra("istmall", data.getIsTmall() );
        intent.putExtra("ali_click", data.getAli_click() );
        intent.putExtra("pic", data.getPic() );
        intent.putExtra("title", data.getTitle() );
        intent.putExtra("quan_price", data.getQuan_price() );
        intent.putExtra("quan_link", data.getQuan_link() );
        return intent;
    }

    public static Intent getArticleIntent(article_data data, Intent intent){
        intent.putExtra("id", data.getId() );
        intent.putExtra("title", data.getTitle() );
        intent.putExtra("pic", data.getPic() );
        intent.putExtra("desc", data.getDesc() );
        return intent;
    }

}
