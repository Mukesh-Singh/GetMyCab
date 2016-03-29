package gmc.com.getmycab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppConstants;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.Utils.DialogUtil;
import gmc.com.getmycab.asyntask.BaseAsyncTask;
import gmc.com.getmycab.base.BaseActivityWithBackHeader;
import gmc.com.getmycab.bean.MyProfileBean;
import gmc.com.getmycab.networkapi.ApiCaller;
import gmc.com.getmycab.networkapi.ApiServiceUrl;
import gmc.com.getmycab.parser.ResponseParser;

/**
 * Created by Baba on 9/11/2015.
 */
public class MyProfileActivity extends BaseActivityWithBackHeader implements View.OnClickListener,BaseAsyncTask.ServiceCallable{
    TextView name,email,phone,city,address,password,state,country;
    static MyProfileBean profileBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        enableImageMenu();
        enableHeaderBackButton();
        setTitle("My Profile");
        initUi();
        callGetProfileWebService();
    }

    private void initUi() {
        name=(TextView)findViewById(R.id.profile_name);
        email=(TextView)findViewById(R.id.profile_email);
        phone=(TextView)findViewById(R.id.profile_phone);
        city=(TextView)findViewById(R.id.profile_city);
        address=(TextView)findViewById(R.id.profile_address);
        password=(TextView)findViewById(R.id.profile_password);
        state=(TextView)findViewById(R.id.profile_state);
        country=(TextView)findViewById(R.id.profile_country);
        password.setOnClickListener(this);

//        if (AppConstants.USER_DETAILS==null) {
//            String response = AppUtil.getPreference(AppConstants.PREF_KEY_USER_DETAILS_JSON);
//            AppConstants.USER_DETAILS = ResponseParser.parseIntoUserDetails(response);
//        }
//        name.setText(AppConstants.USER_DETAILS.getFirstname()+" "+AppConstants.USER_DETAILS.getLastname());
//        email.setText(AppConstants.USER_DETAILS.getEmail());
//        phone.setText(AppConstants.USER_DETAILS.getTelephone());
//        city.setText(AppConstants.USER_DETAILS.get);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (profileBean==null)
            callGetProfileWebService();
        else
            updateUi();
    }

    private void updateUi() {
        if (profileBean==null)
            return;
        name.setText(profileBean.getFirstname()+" "+profileBean.getLastname());
        email.setText(profileBean.getEmail()+"");
        phone.setText(profileBean.getTelephone()+"");
        city.setText(profileBean.getCity()+"");
        address.setText(profileBean.getAddress()+"");
        state.setText(profileBean.getState()+"");
        country.setText(profileBean.getCountry()+"");

    }

    private void callGetProfileWebService(){

        JSONObject jsonObject= new JSONObject();
        try{
            jsonObject.put("customer_id",AppUtil.getPreference(AppConstants.PREF_KEY_CUSTOMER_ID));

        }catch (Exception e){
            e.printStackTrace();
        }

        new BaseAsyncTask(this,true,this, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.GET_PROFILE,jsonObject).execute();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==password.getId()){
            startActivity(new Intent(this,ChagePasswordActivity.class));
        }
    }

    @Override
    public void onServiceResultSuccess(String url, boolean success, String message, String response) {
        if (success){
            profileBean=ResponseParser.parseIntoMyProfile(response);
            updateUi();
        }
        else
            AppUtil.showToastMessage(message);
    }



    @Override
    public void onServiceResultFailure(String url, String error) {
        DialogUtil.showServerErrorDialog(this);
    }

    public void moveToEdit() {
        startActivity(new Intent(this,MyProfileEditActivity.class));
    }
}
