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
 * Created by Baba on 9/12/2015.
 */
public class ChagePasswordActivity extends BaseActivityWithBackHeader implements View.OnClickListener,BaseAsyncTask.ServiceCallable{
    private EditText newpassword, confirmpassword;
    private Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);
        enableHeaderBackButton();
        setTitle("Change Password");
        AppUtil.checkGps();
        initUi();

    }

    private void initUi() {
        newpassword =(EditText)findViewById(R.id.change_password_newpassword);
        confirmpassword =(EditText)findViewById(R.id.change_password_confirm_password);
        submit=(Button)findViewById(R.id.change_password_submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==submit.getId())
        {
            if (isValid()) {

                    callApiForSendLocation();
            }
        }
    }

    private boolean isValid(){
        if (AppUtil.isEmpty(newpassword)){
            AppUtil.setError(newpassword, "Please new password");
            return false;
        }
        else if (!newpassword.getText().toString().equals(confirmpassword.getText().toString())){
            AppUtil.setError(confirmpassword, "Both password are not same, try again");
            return false;
        }

        return true;
    }

    private void callApiForSendLocation() {
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("new_password", newpassword.getText().toString());
            jsonObject.put("customer_id", AppUtil.getPreference(AppConstants.PREF_KEY_CUSTOMER_ID));


        }
        catch (Exception e){
            e.printStackTrace();
        }
        new BaseAsyncTask(this,true,this, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.CHANGE_PASSWORD,jsonObject).execute();

    }

    @Override
    public void onServiceResultSuccess(String url, boolean success, String message, String response) {
        AppUtil.showToastMessage(message);
        if (success){
            AppUtil.savePreference(AppConstants.PREF_KEY_USER_PASSWORD,newpassword.getText().toString());
            finish();
        }
    }

    @Override
    public void onServiceResultFailure(String url, String error) {
        DialogUtil.showServerErrorDialog(this);
    }
}
