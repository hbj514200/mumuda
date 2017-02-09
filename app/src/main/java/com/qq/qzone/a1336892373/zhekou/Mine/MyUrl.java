package com.qq.qzone.a1336892373.zhekou.Mine;

import com.qq.qzone.a1336892373.zhekou.tools.httpConn;

public class MyUrl {

    public void getUrl(String source, MineCallback callback){
        String md5 = httpConn.stringToMD5(source);
    }

}
