package gmc.com.getmycab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import gmc.com.getmycab.networkapi.CreateJsonFor;
import gmc.com.getmycab.networkapi.ServiceUtil;
import gmc.com.getmycab.parser.ResponseParser;

/**
 * Created by mukesh on 2/9/15.
 */
public class SignUpActivity extends BaseActivityWithBackHeader implements View.OnClickListener, BaseAsyncTask.ServiceCallable {
    private EditText mName, mEmail, mPassword, mPhone, mAddress;
    private TextView mState, mCity;
    private Button mContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        setTitle("SIGN UP");
        enableHeaderBackButton();
        initUi();
        AppUtil.checkGps();
    }

    private void initUi() {
        mName = (EditText) findViewById(R.id.signup_activity_name);
        mEmail = (EditText) findViewById(R.id.signup_activity_email);
        mPassword = (EditText) findViewById(R.id.signup_activity_password);
        mPhone = (EditText) findViewById(R.id.signup_activity_phone);
        mAddress = (EditText) findViewById(R.id.signup_activity_address);
        mCity = (TextView) findViewById(R.id.signup_activity_citydropdown);
        mState = (TextView) findViewById(R.id.signup_activity_statedropdown);
        mContinue = (Button) findViewById(R.id.signup_activity_continuebutton);
        mContinue.setOnClickListener(this);
        mState.setOnClickListener(this);
        mCity.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mContinue.getId()) {
            if (isValid()) {
                callWebservice();

            }
        } else if (v.getId() == mState.getId()) {
            if (AppConstants.stateList == null || AppConstants.stateList.size() == 0)
                ServiceUtil.callStateListServies(this, this);
            else
                DialogUtil.showStateListDialog(this, AppConstants.stateList, mState);

        } else if (v.getId() == mCity.getId()) {
            if (AppConstants.multiCitySource == null || AppConstants.multiCitySource.size() == 0)
                ServiceUtil.callCityListServies(this, CreateJsonFor.multiCity(""), this);
            else
                DialogUtil.showCityListDialog(this, AppConstants.multiCitySource, mCity);
            //DialogUtil.showListDialog(this, AppConstants.cityList,mCity);
        }
    }

    private boolean isValid() {
        if (AppUtil.isEmpty(mName)) {
            AppUtil.setError(mName, "Please enter Name");
            return false;
        } else if (AppUtil.isEmpty(mEmail)) {
            AppUtil.setError(mName, "Please enter Email id");
            return false;
        } else if (!AppUtil.isValidEmail(mEmail)) {
            AppUtil.setError(mEmail, "Invalid email id");
            return false;
        } else if (AppUtil.isEmpty(mPassword)) {
            AppUtil.setError(mPassword, "Please enter Password");
            return false;
        } else if (AppUtil.isEmpty(mPhone)) {
            AppUtil.setError(mPhone, "Please enter Phone No.");
            return false;

        } else if (mPhone.getText().toString().trim().length() < 10) {
            AppUtil.setError(mPhone, "Invalid Phone No.");
            return false;

        } else if (AppUtil.isEmpty(mAddress)) {
            AppUtil.setError(mAddress, "Please enter Address");
            return false;
        }

        return true;
    }

    private void callWebservice() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", mName.getText().toString());
            jsonObject.put("email", mEmail.getText().toString());
            jsonObject.put("password", mPassword.getText().toString());
            jsonObject.put("phone", mPhone.getText().toString());
            jsonObject.put("address", mAddress.getText().toString());
            jsonObject.put("zone", mState.getText().toString());
            jsonObject.put("city", mCity.getText().toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
        new BaseAsyncTask(this, true, this, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.SIGNUP, jsonObject).execute();

    }


    @Override
    public void onServiceResultSuccess(String url, boolean success, String message, String response) {
        if (success) {
            if (url.equalsIgnoreCase(ApiServiceUrl.STATE_LIST)) {
                AppConstants.stateList = ResponseParser.parseIntoStateList(response);
                DialogUtil.showStateListDialog(this, AppConstants.stateList, mState);
            } else if (url.equalsIgnoreCase(ApiServiceUrl.CITY_LIST_TRAVEL)) {
                AppConstants.multiCitySource = ResponseParser.parseIntoCityList(response);
                DialogUtil.showCityListDialog(this, AppConstants.multiCitySource, mCity);
            } else if (url.equalsIgnoreCase(ApiServiceUrl.SIGNUP)) {
                AppUtil.showToastMessage(message + ", Please login using your email id and password.");
                saveUserDetails();
                startActivity(new Intent(this, SignInActivity.class));
                finish();
            }


        } else {
            AppUtil.showToastMessage(message);
        }

    }

    @Override
    public void onServiceResultFailure(String url, String error) {
        DialogUtil.showServerErrorDialog(this);
    }

    private void saveUserDetails() {
//       AppUtil.savePreference(AppConstants.PREF_KEY_IS_LOGEDIN,true);
        AppUtil.savePreference(AppConstants.PREF_KEY_USER_NAME, mName.getText().toString());
        AppUtil.savePreference(AppConstants.PREF_KEY_USER_EMAIL_ID, mEmail.getText().toString());
        AppUtil.savePreference(AppConstants.PREF_KEY_USER_PASSWORD, mPassword.getText().toString());
        AppUtil.savePreference(AppConstants.PREF_KEY_USER_PHONE, mPhone.getText().toString());
        AppUtil.savePreference(AppConstants.PREF_KEY_USER_ADDRESS, mAddress.getText().toString());
        AppUtil.savePreference(AppConstants.PREF_KEY_USER_STATE, mState.getText().toString());
        AppUtil.savePreference(AppConstants.PREF_KEY_USER_CITY, mCity.getText().toString());

        GpsTracker gpsTracker = new GpsTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            AppUtil.savePreference(AppConstants.PREF_KEY_LATITUDE, gpsTracker.getLatitude() + "");
            AppUtil.savePreference(AppConstants.PREF_KEY_LONGITUDE, gpsTracker.getLongitude() + "");
            AppUtil.savePreference(AppConstants.PREF_KEY_CURRENT_ADDRESS, "" + gpsTracker.getAddressLine(this));
        } else {
            AppUtil.savePreference(AppConstants.PREF_KEY_LATITUDE, "0.0");
            AppUtil.savePreference(AppConstants.PREF_KEY_LONGITUDE, "0.0");
            AppUtil.savePreference(AppConstants.PREF_KEY_CURRENT_ADDRESS, "");
        }
    }
}

