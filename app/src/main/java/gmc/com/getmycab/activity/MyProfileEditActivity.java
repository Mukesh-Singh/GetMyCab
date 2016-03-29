package gmc.com.getmycab.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
public class MyProfileEditActivity extends BaseActivityWithBackHeader implements BaseAsyncTask.ServiceCallable{
    EditText name,email,phone,city,address,/*password,*/state,country;
    MyProfileBean profileBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_edit);
        profileBean=MyProfileActivity.profileBean;
        enableTextMenu1Enable();
        enableHeaderBackButton();
        setTitle("Edit Profile");
        initUi();
        if (profileBean==null)
            callGetProfileWebService();
        else
            updateUi();
    }

    private void initUi() {
        name=(EditText)findViewById(R.id.edit_profile_name);
        email=(EditText)findViewById(R.id.edit_profile_email);
        phone=(EditText)findViewById(R.id.edit_profile_phone);
        city=(EditText)findViewById(R.id.edit_profile_city);
        address=(EditText)findViewById(R.id.edit_profile_address);
        //password=(EditText)findViewById(R.id.edit_profile_password);
        state=(EditText)findViewById(R.id.edit_profile_state);
        country=(EditText)findViewById(R.id.edit_profile_country);

//        if (AppConstants.USER_DETAILS==null) {
//            String response = AppUtil.getPreference(AppConstants.PREF_KEY_USER_DETAILS_JSON);
//            AppConstants.USER_DETAILS = ResponseParser.parseIntoUserDetails(response);
//        }
//        name.setText(AppConstants.USER_DETAILS.getFirstname()+" "+AppConstants.USER_DETAILS.getLastname());
//        email.setText(AppConstants.USER_DETAILS.getEmail());
//        phone.setText(AppConstants.USER_DETAILS.getTelephone());
//        city.setText(AppConstants.USER_DETAILS.get);


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
            jsonObject.put("customer_id", AppUtil.getPreference(AppConstants.PREF_KEY_CUSTOMER_ID));

        }catch (Exception e){
            e.printStackTrace();
        }

        new BaseAsyncTask(this,true,this, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.GET_PROFILE,jsonObject).execute();
    }


    @Override
    public void onServiceResultSuccess(String url, boolean success, String message, String response) {
        if (success){
            if (url.equalsIgnoreCase(ApiServiceUrl.GET_PROFILE)) {
                profileBean = ResponseParser.parseIntoMyProfile(response);
                updateUi();
            }
            else if  (url.equalsIgnoreCase(ApiServiceUrl.UPDATE_PROFILE)) {
                AppUtil.showToastMessage(message);
                savePreferences();
                updateBean();
                finish();
            }

        }
        else
            AppUtil.showToastMessage(message);
    }



    @Override
    public void onServiceResultFailure(String url, String error) {
        DialogUtil.showServerErrorDialog(this);
    }


    public void callWebServiceForUpdateData(){
        if (isValid()){
            JSONObject jsonObject= new JSONObject();
            try {
                jsonObject.put("name",name.getText().toString());
                jsonObject.put("email",email.getText().toString());
                jsonObject.put("password",AppUtil.getPreference(AppConstants.PREF_KEY_USER_PASSWORD));
                jsonObject.put("phone",phone.getText().toString());
                jsonObject.put("address", phone.getText().toString());
                jsonObject.put("zone", state.getText().toString());
                jsonObject.put("city", city.getText().toString());
                jsonObject.put("customer_id", profileBean.getCustomerId());


            }
            catch (Exception e){
                e.printStackTrace();
            }
            new BaseAsyncTask(this,true,this, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.UPDATE_PROFILE,jsonObject).execute();
        }
    }

    private boolean isValid(){
        if (AppUtil.isEmpty(name)){
            AppUtil.setError(name, "Please enter Name");
            return false;
        }
        else if (AppUtil.isEmpty(email)){
            AppUtil.setError(email, "Please enter Email id");
            return false;
        }
        else  if (!AppUtil.isValidEmail(email)){
            AppUtil.setError(email,"Invalid email id");
            return false;
        }

        else  if (AppUtil.isEmpty(phone)){
            AppUtil.setError(phone,"Please enter Phone No.");
            return false;

        }
        else  if (phone.getText().toString().trim().length()<10){
            AppUtil.setError(phone,"Invalid Phone No.");
            return false;

        }
        else  if (AppUtil.isEmpty(address)){
            AppUtil.setError(address,"Please enter Address");
            return false;
        }
//        else  if (AppUtil.isEmpty(state)){
//            AppUtil.setError(state,"Please enter State");
//            return false;
//        }
//        else  if (AppUtil.isEmpty(country)){
//            AppUtil.setError(country,"Please enter country");
//            return false;
//        }

        return true;
    }

    private void savePreferences() {
        AppUtil.savePreference(AppConstants.PREF_KEY_USER_NAME, name.getText().toString());
        AppUtil.savePreference(AppConstants.PREF_KEY_USER_EMAIL_ID,email.getText().toString());
        //AppUtil.savePreference(AppConstants.PREF_KEY_USER_PASSWORD,mPassword.getText().toString());
        AppUtil.savePreference(AppConstants.PREF_KEY_USER_PHONE, phone.getText().toString());
        AppUtil.savePreference(AppConstants.PREF_KEY_USER_ADDRESS,address.getText().toString());
        AppUtil.savePreference(AppConstants.PREF_KEY_USER_STATE,state.getText().toString());
        AppUtil.savePreference(AppConstants.PREF_KEY_USER_CITY,city.getText().toString());
    }
   private void updateBean(){
       if (MyProfileActivity.profileBean!=null) {
           MyProfileActivity.profileBean.setFirstname(name.getText().toString());
           MyProfileActivity.profileBean.setEmail(email.getText().toString());
           MyProfileActivity.profileBean.setTelephone(phone.getText().toString());
           MyProfileActivity.profileBean.setAddress(address.getText().toString());
           MyProfileActivity.profileBean.setCity(city.getText().toString());
           MyProfileActivity.profileBean.setState(state.getText().toString());
           MyProfileActivity.profileBean.setCountry(country.getText().toString());
       }
    }
}
