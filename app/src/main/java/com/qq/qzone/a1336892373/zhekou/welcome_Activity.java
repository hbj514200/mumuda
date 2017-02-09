package com.qq.qzone.a1336892373.zhekou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;

public class welcome_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tiaozhuan();

        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Immd/");
        if (!file.exists())      file.mkdirs();
        //chuan jian mu lu
    }

    private void tiaozhuan(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try { Thread.sleep(800); } catch (Exception e) { }
                startActivity( new Intent(welcome_Activity.this, MainActivity.class));
                finish();
            }
        }).start();
    }

}
