package gmc.com.getmycab.asyntask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import gmc.com.getmycab.Utils.DialogUtil;
import gmc.com.getmycab.networkapi.ApiCaller;
import gmc.com.getmycab.networkapi.ApiCaller.RequestType;
import gmc.com.getmycab.networkapi.ApiServiceUrl;

public class BaseAsyncTask extends AsyncTask<Void, Void, Object> {

	private String mServiceUrl;
	private JSONObject mJsonObject;
	private ApiCaller.RequestType mRequestType;
	private ProgressDialog mProgressDialog;
	//private Dialog mProgressDialog;
	private Context mContext;
	private ServiceCallable mCallback;
	private boolean isProgressDialogShow = true;

	// For multipart only
	private int mPostType;
	private String mFilePath = "";
	private boolean istOtherUrl;

	public void setIsOtherUlr(boolean isOtherUrl){
		this.istOtherUrl =isOtherUrl;
	}

	public interface ServiceCallable {
		void onServiceResultSuccess(String url, boolean success,
									String message, String response);

		void onServiceResultFailure(String url, String error);
	}

	public BaseAsyncTask(Context context, ServiceCallable callback,
			ApiCaller.RequestType requestType, String url, JSONObject jsonParameter) {

		mRequestType = requestType;
		mServiceUrl = url;
		mJsonObject = jsonParameter;
		mContext = context;
		mCallback = callback;
	}

	public BaseAsyncTask(Context context, boolean showProgressDialog,
			ServiceCallable callback, ApiCaller.RequestType requestType, String url,
			JSONObject jsonParameter) {
		this(context, callback, requestType, url, jsonParameter);
		isProgressDialogShow = showProgressDialog;
	}

	public BaseAsyncTask(Context context, ServiceCallable callback,
			ApiCaller.RequestType requestType, String url, JSONObject jsonParameter,
			int messageType, String attachMentPaths) {
		this(context, callback, requestType, url, jsonParameter);
		mPostType = messageType;
		mFilePath = attachMentPaths;

	}

	@Override
	protected void onPreExecute() {
		if (isProgressDialogShow){
			mProgressDialog =
			(ProgressDialog) DialogUtil.showProgressDialog(mContext);
					
		}
		super.onPreExecute();
	}

	@Override
	protected Object doInBackground(Void... params) {
		String url;
		if (istOtherUrl){
			url=mServiceUrl;
		}
		else
			url=ApiServiceUrl.BASE_URL + mServiceUrl;

		if (mRequestType == RequestType.HTTP_POST ) {
//			if(url.contains(ApiServiceUrl.BOOKING_HISTORY)){
//				//Fake calling for bugfixing in booking history service
//				ApiCaller.getCaller().getServiceResponce(mRequestType,
//						url, mJsonObject);
//			}
			return ApiCaller.getCaller().getServiceResponce(mRequestType,
					url, mJsonObject);
		}
		else if (mRequestType == RequestType.HTTP_POST_REQUEST_WITH_IMAGE)
			return "";
		else
			return "";
	}

	@Override
	protected void onPostExecute(final Object response) {
		super.onPostExecute(response);

		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			try {
				mProgressDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		if (response instanceof Exception) {
			// Toast.makeText(mContext,
			// "Something went wrong, please try again",Toast.LENGTH_SHORT
			// ).show();
			mCallback.onServiceResultFailure(mServiceUrl,
					((Exception) response).getMessage());
		} else if (response instanceof String) {

			if (mServiceUrl.equalsIgnoreCase(ApiServiceUrl.SEARCH_CAB)){
				if (response!=null) {
					mCallback.onServiceResultSuccess(mServiceUrl, true,"", response.toString()
					);
				}
			}else {

				JSONObject obj;
				try {
					obj = new JSONObject(response.toString());
					boolean result = obj.optBoolean("Result");
					String msg = obj.optString("msg");
					// String data=obj.getString("data");

					mCallback.onServiceResultSuccess(mServiceUrl, result, msg,
							response.toString());

				} catch (JSONException e) {


					mCallback.onServiceResultSuccess(mServiceUrl, false, "Un-Recoverable Internal error happened. Sorry for incovenience. Please report this issue to info@getmecab.com",
							"");
					e.printStackTrace();
				}
			}

		}

	}

}
