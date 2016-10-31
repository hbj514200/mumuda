package immd.yxd.com.immd;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class webActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        ImageView back_button = (ImageView) findViewById(R.id.tab_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (check_taobap())     taobao();
        else                    loadUrl();
    }

    private void loadUrl(){
        String ali_url = getIntent().getStringExtra("url");
        WebView webView = (WebView) findViewById(R.id.xiadan_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(ali_url);
    }

    private void taobao(){
        Intent intent= new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse( getIntent().getStringExtra("url") );
        intent.setData(content_url);
        startActivity(intent);
    }

    private boolean check_taobap(){
            final PackageManager packageManager = getPackageManager();
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
            for ( int i = 0; i < pinfo.size(); i++ ) {
                if(pinfo.get(i).packageName.equalsIgnoreCase("com.taobao.taobao"))
                    return true;
            }
            return false;
        }

    }
