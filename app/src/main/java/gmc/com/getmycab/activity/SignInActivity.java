package gmc.com.getmycab.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppConstants;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.Utils.DialogUtil;
import gmc.com.getmycab.asyntask.BaseAsyncTask;
import gmc.com.getmycab.base.BaseActivityWithBackHeader;
import gmc.com.getmycab.asyntask.BaseAsyncTask.ServiceCallable;
import gmc.com.getmycab.bean.UserDetails;
import gmc.com.getmycab.networkapi.ApiCaller;
import gmc.com.getmycab.networkapi.ApiServiceUrl;
import gmc.com.getmycab.parser.ResponseParser;

/**
 * Created by mukesh on 2/9/15.
 */
public class SignInActivity extends BaseActivityWithBackHeader implements View.OnClickListener,ServiceCallable {
    Button mSignInBtn,mSignUpBtn;
    EditText email,password;
    CheckBox remmberme;
    TextView forgetPassword;
    UserDetails userDetails;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);
        setTitle("SIGN IN");
        mSignInBtn =(Button)findViewById(R.id.login_signup_activity_signin_btn);
        mSignUpBtn=(Button)findViewById(R.id.login_signup_activity_signup_btn);
        email=(EditText)findViewById(R.id.signin_acitivity_emailEditText);
        password=(EditText)findViewById(R.id.signin_acitivity_passwordEditText);
        remmberme=(CheckBox)findViewById(R.id.signin_activity_remembermeChkbox);
        forgetPassword=(TextView)findViewById(R.id.signin_activity_forgetPassword);

        boolean isremember=AppUtil.getPreferenceBoolean(AppConstants.PREF_KEY_REMEMBER_ME);
        remmberme.setChecked(isremember);
        if (isremember)
            setEmailIdAndPassword();

        mSignInBtn.setBackgroundResource(R.drawable.yellow_rounded_corner_background);
        mSignInBtn.setOnClickListener(this);
        mSignUpBtn.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        enableHeaderBackButton();

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==mSignInBtn.getId()) {
            if (isValid()) {
                callWebService();

            }
        }
        else if (v.getId()==mSignUpBtn.getId()) {

            startActivity(new Intent(this, SignUpActivity.class));
        }
        else if (v.getId()==forgetPassword.getId()){
            Intent intent= new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://www.getmecab.com/index.php?route=account/forgotten"));
            startActivity(intent);
        }


    }

    private void saveEmailPassWordIntoPreferences(){
        if (remmberme.isChecked()){
            AppUtil.savePreference(AppConstants.PREF_KEY_USER_EMAIL_ID,email.getText().toString());
            AppUtil.savePreference(AppConstants.PREF_KEY_USER_PASSWORD,password.getText().toString());
        }
    }

    private void setEmailIdAndPassword(){
       email.setText(AppUtil.getPreference(AppConstants.PREF_KEY_USER_EMAIL_ID));
        password.setText(AppUtil.getPreference(AppConstants.PREF_KEY_USER_PASSWORD));
    }




    private boolean isValid(){
        if (AppUtil.isEmpty(email)){
            AppUtil.setError(email, "Please enter email id");
            return false;
        }
        else  if (!AppUtil.isValidEmail(email)){
            AppUtil.setError(email, "Invalid email id");
            return false;
        }
        else  if (AppUtil.isEmpty(password)){
            AppUtil.setError(password, "Please enter Password");
            return false;
        }
        return true;
    }

    private void callWebService(){
        JSONObject jsonObject= new JSONObject();
        try{
            jsonObject.put("email",email.getText().toString());
            jsonObject.put("password",password.getText().toString());

        }catch (Exception e){
            e.printStackTrace();
        }

        new BaseAsyncTask(this,true,this, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.LOGIN,jsonObject).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void onServiceResultSuccess(String url, boolean success, String message, String response) {
        if (success){
            if (url.equalsIgnoreCase(ApiServiceUrl.LOGIN)){
                AppUtil.savePreference(AppConstants.PREF_KEY_IS_LOGEDIN,true);
                AppUtil.savePreference(AppConstants.PREF_KEY_REMEMBER_ME, remmberme.isChecked());
                AppUtil.savePreference(AppConstants.PREF_KEY_USER_DETAILS_JSON, response);
                userDetails= ResponseParser.parseIntoUserDetails(response);
                AppUtil.savePreference(AppConstants.PREF_KEY_USER_NAME,userDetails.getFirstname());
                AppUtil.savePreference(AppConstants.PREF_KEY_CUSTOMER_ID,userDetails.getCustomerId());
                AppUtil.savePreference(AppConstants.PREF_KEY_USER_EMAIL_ID,userDetails.getEmail().toString());
                AppUtil.savePreference(AppConstants.PREF_KEY_USER_PHONE, userDetails.getTelephone().toString());

                if (remmberme.isChecked()){
                    saveEmailPassWordIntoPreferences();
                }
                startActivity(new Intent(this, HomeActivity.class));
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
}
