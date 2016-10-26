package immd.yxd.com.immd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class welcome_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tiaozhuan();
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
