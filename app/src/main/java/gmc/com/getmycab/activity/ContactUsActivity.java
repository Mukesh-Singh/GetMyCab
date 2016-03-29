package gmc.com.getmycab.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import gmc.com.getmycab.R;
import gmc.com.getmycab.base.BaseActivityWithBackHeader;

/**
 * Created by Baba on 9/9/2015.
 */
public class ContactUsActivity extends BaseActivityWithBackHeader {
    private TextView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_activity);
        webView=(TextView)findViewById(R.id.webview_content);
        enableHeaderBackButton();
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        if (title != null)
            setTitle(title);

        if (url != null && url.length() > 0)
            webView.setText(url);
    }
}
