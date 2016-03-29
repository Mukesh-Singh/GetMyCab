package gmc.com.getmycab.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppConstants;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.Utils.DialogUtil;
import gmc.com.getmycab.asyntask.BaseAsyncTask;
import gmc.com.getmycab.base.BaseActivityWithBackHeader;
import gmc.com.getmycab.gps.GpsTracker;
import gmc.com.getmycab.networkapi.ApiCaller;
import gmc.com.getmycab.networkapi.ApiServiceUrl;

/**
 * Created by Baba on 9/9/2015.
 */
public class SendLocationActivity extends BaseActivityWithBackHeader implements View.OnClickListener,BaseAsyncTask.ServiceCallable{
    private EditText name,emailId;
    private Button submit;
    private GpsTracker gpsTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_location_activity);
        enableHeaderBackButton();
        setTitle("SEND LOCATION");
        AppUtil.checkGps();
        initUi();

    }

    private void initUi() {
        name=(EditText)findViewById(R.id.send_location_name);
        emailId=(EditText)findViewById(R.id.send_location_email);
        submit=(Button)findViewById(R.id.send_location_submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==submit.getId())
        {
            if (isValid()) {
                gpsTracker = new GpsTracker(this);
                if (gpsTracker.getIsGPSTrackingEnabled()) {
                    //TODO
                    callApiForSendLocation();
                } else {
                    gpsTracker.showSettingsAlert();
                }
            }
        }
    }

    private boolean isValid(){
        if (AppUtil.isEmpty(name)){
            AppUtil.setError(name, "Please enter Name");
            return false;
        }
        else if (AppUtil.isEmpty(emailId)){
            AppUtil.setError(emailId, "Please enter Email id");
            return false;
        }
        else  if (!AppUtil.isValidEmail(emailId)){
            AppUtil.setError(emailId,"Invalid email id");
            return false;
        }


        return true;
    }

    private void callApiForSendLocation() {
            JSONObject jsonObject= new JSONObject();
            try {
                jsonObject.put("customer_id",AppUtil.getPreference(AppConstants.PREF_KEY_CUSTOMER_ID));
                jsonObject.put("name",name.getText().toString());
                jsonObject.put("email",emailId.getText().toString());
                String message="Hi, My current location is \n";
                if (gpsTracker .getAddressLine(this)!=null || gpsTracker .getAddressLine(this).equalsIgnoreCase("null") ||gpsTracker .getAddressLine(this).length()!=0)
                    message=message+gpsTracker.getAddressLine(this)+"\n";
                if (gpsTracker .getLocality(this)!=null || gpsTracker .getLocality(this).equalsIgnoreCase("null") ||gpsTracker .getLocality(this).length()!=0)
                    message=message+gpsTracker.getLocality(this)+"\n";

                jsonObject.put("message",message+" from GetMeCab");

            }
            catch (Exception e){
                e.printStackTrace();
            }
            new BaseAsyncTask(this,true,this, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.SEND_LOCATION,jsonObject).execute();

        }

    @Override
    public void onServiceResultSuccess(String url, boolean success, String message, String response) {
        AppUtil.showToastMessage(message);
        if (success){
            finish();
        }
    }

    @Override
    public void onServiceResultFailure(String url, String error) {
        DialogUtil.showServerErrorDialog(this);
    }
}
