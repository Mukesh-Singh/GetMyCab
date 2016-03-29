package gmc.com.getmycab.ccavenue.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppConstants;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.activity.BookingDetailsActivity;
import gmc.com.getmycab.base.BaseActivityWithBackHeader;
import gmc.com.getmycab.ccavenue.utility.AvenuesParams;
import gmc.com.getmycab.ccavenue.utility.Constants;
import gmc.com.getmycab.ccavenue.utility.RSAUtility;
import gmc.com.getmycab.ccavenue.utility.ServiceHandler;
import gmc.com.getmycab.ccavenue.utility.ServiceUtility;

public class WebViewActivity extends BaseActivityWithBackHeader {
	private ProgressDialog dialog;
	Intent mainIntent;
	String html, encVal;
	String orderId ="";
	public static final String TRANSACTION_SUCCESS="Transaction Successful!";
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_webview);
		setTitle("PAYMENT");
		enableHeaderBackButton();
		mainIntent = getIntent();
		orderId =mainIntent.getStringExtra(AvenuesParams.ORDER_ID);
		
		// Calling async task to get display content
		new RenderView().execute();
	}
	
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class RenderView extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			dialog = new ProgressDialog(WebViewActivity.this);
			dialog.setMessage("Please wait...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
	
			// Making a request to url and getting response
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
			params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
	
			String vResponse = sh.makeServiceCall(mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL), ServiceHandler.POST, params);
			System.out.println(vResponse);
			if(!ServiceUtility.chkNull(vResponse).equals("")
					&& ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR")==-1){
				StringBuffer vEncVal = new StringBuffer("");
				vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
				vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
				encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);

			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (dialog.isShowing())
				dialog.dismiss();
			
			@SuppressWarnings("unused")
			class MyJavaScriptInterface
			{
				@JavascriptInterface
			    public void processHTML(String html)
			    {
			        // process the html as needed by the app
					Log.e("Response"," html String \n"+html);
			    	String status = null;
			    	if(html.indexOf("Failure")!=-1){
			    		status = "Transaction Failed!";
			    	}else if(html.indexOf("Success")!=-1){
			    		status = TRANSACTION_SUCCESS;
			    	}else if(html.indexOf("Aborted")!=-1){
			    		status = "Transaction Cancelled!";
			    	}else{
			    		status = "Transaction Status Unknown";
			    	}
			    	//Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
//			    	Intent intent = new Intent(getApplicationContext(),StatusActivity.class);
//					intent.putExtra("transStatus", status);
//					intent.putExtra("orderid", orderId);
//					startActivity(intent);
					Intent intent= new Intent(WebViewActivity.this,BookingDetailsActivity.class);
					intent.putExtra("from_payment",true);
					intent.putExtra("booking_id",orderId);
					intent.putExtra("transStatus", status);
					intent.putExtra("amt", mainIntent.getStringExtra(AvenuesParams.AMOUNT));
					startActivity(intent);
			    }
			}
			
			final WebView webview = (WebView) findViewById(R.id.webview);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
			webview.setWebViewClient(new WebViewClient(){
				@Override  
	    	    public void onPageFinished(WebView view, String url) {
	    	        super.onPageFinished(webview, url);
	    	        if(url.indexOf("/ccavResponseHandler.php")!=-1){
	    	        	webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
	    	        }
	    	    }  

	    	    @Override
	    	    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
	    	        Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
	    	    }
			});
			
			/* An instance of this class will be registered as a JavaScript interface */
			StringBuffer params = new StringBuffer();
			params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE,mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID,mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID,mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL,mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL,mainIntent.getStringExtra(AvenuesParams.CANCEL_URL)));

            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_NAME, AppUtil.getPreference(AppConstants.PREF_KEY_USER_NAME)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_COUNTRY,"India"));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_EMAIL,AppUtil.getPreference(AppConstants.PREF_KEY_USER_EMAIL_ID)));

			params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL,URLEncoder.encode(encVal)));
			
			String vPostParams = params.substring(0,params.length()-1);
			try {
				webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
			} catch (Exception e) {
				showToast("Exception occured while opening webview.");
			}
		}
	}

	@Override
	public void onBackPressed() {
		showPaymentCancelDialog();
	}

	public void showToast(String msg) {
		Toast.makeText(this,  msg, Toast.LENGTH_LONG).show();
	}

	private void showPaymentCancelDialog(){
		//AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(this,R.style.AppTheme);



		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				this);
        builderSingle.setTitle("Cancel Transaction");
        builderSingle.setMessage("Your payment is under process. Pressing back would cancel your current transaction. Do you want to cancel anyway?");

		builderSingle.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
		builderSingle.setNegativeButton("No",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});


        builderSingle.show();
	}
} 