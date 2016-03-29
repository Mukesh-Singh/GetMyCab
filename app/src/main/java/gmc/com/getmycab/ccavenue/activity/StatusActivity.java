package gmc.com.getmycab.ccavenue.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import gmc.com.getmycab.R;
import gmc.com.getmycab.base.BaseActivityWithBackHeader;
import gmc.com.getmycab.networkapi.ApiServiceUrl;


public class StatusActivity extends BaseActivityWithBackHeader {
	private WebView webView;
	private ProgressBar mProgerssbar;
	private TextView statusTextView;
	boolean loadingFinished = true;
	boolean redirect = false;

	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_status);
		setTitle("Booking Status");
		initUi();
		enableHeaderBackButton();
		Intent mainIntent = getIntent();

		String status=mainIntent.getStringExtra("transStatus");
		String orderId=mainIntent.getStringExtra("orderid");
		if (status.equalsIgnoreCase("success")|| status.equalsIgnoreCase("unknow")){
			webView.setVisibility(View.VISIBLE);
			statusTextView.setVisibility(View.GONE);
			setPageLoadLisetner();
			webView.loadUrl(ApiServiceUrl.DOMAIN_NAME + "index.php?route=payment/ccavenue/callback&order_id="+orderId);

		}
		else {
			webView.setVisibility(View.GONE);
			statusTextView.setVisibility(View.VISIBLE);
			statusTextView.setText(status);
		}
	}
	private void initUi(){
		webView=(WebView)findViewById(R.id.status_webView);
		mProgerssbar=(ProgressBar)findViewById(R.id.status_progressBar);
		statusTextView=(TextView)findViewById(R.id.status_fail_textview);
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
				mProgerssbar.setVisibility(View.VISIBLE);
				//SHOW LOADING IF IT ISNT ALREADY VISIBLE
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if(!redirect){
					loadingFinished = true;
				}

				if(loadingFinished && !redirect){
					//HIDE LOADING IT HAS FINISHED
					mProgerssbar.setVisibility(View.GONE);
				} else{
					redirect = false;
				}

			}
		});
	}

	@Override
	public void onBackPressed() {
		backIcon.performClick();
	}
}