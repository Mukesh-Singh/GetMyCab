package gmc.com.getmycab.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import gmc.com.getmycab.R;
import gmc.com.getmycab.base.BaseActivityWithBackHeader;
import gmc.com.getmycab.networkapi.ApiServiceUrl;

/**
 * Created by Baba on 9/19/2015.
 */
public class TermAndConditionActivity extends BaseActivityWithBackHeader{
    ProgressBar mProgressBar;
    boolean loadingFinished = true;
    boolean redirect = false;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_and_condition);
        setTitle("TERM AND CONDITION");
        enableHeaderBackButton();
         webView= (WebView) findViewById(R.id.webview_termAndConditin);
        mProgressBar=(ProgressBar)findViewById(R.id.termAndcondition_progressBar);
        webView.loadUrl(ApiServiceUrl.DOMAIN_NAME+"index.php?route=common/terms");
        setPageLoadLisetner();




    }

    private void setPageLoadLisetner(){

        webView.setWebViewClient(new WebViewClient());


        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                webView.loadUrl(urlNewString);
                return true;
            }

            public void onPageStarted(WebView view, String url) {
                loadingFinished = false;
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(!redirect){
                    loadingFinished = true;
                }

                if(loadingFinished && !redirect){
                    //HIDE LOADING IT HAS FINISHED
                    mProgressBar.setVisibility(View.GONE);
                } else{
                    redirect = false;
                }

            }
        });
    }


}
