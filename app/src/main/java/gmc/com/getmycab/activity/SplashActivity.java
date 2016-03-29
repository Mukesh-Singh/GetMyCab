package gmc.com.getmycab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import org.json.JSONObject;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppConstants;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.base.BaseActivity;
import gmc.com.getmycab.gps.GpsTracker;
import gmc.com.getmycab.networkapi.ApiCaller;
import gmc.com.getmycab.networkapi.ApiServiceUrl;
import gmc.com.getmycab.networkapi.CreateJsonFor;
import gmc.com.getmycab.parser.ResponseParser;

/**
 * Created by Baba on 9/1/2015.
 */
public class SplashActivity extends BaseActivity{
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        getStateList();
      //  getCityList();

        getOneWaySource();
        getRoundTripSource();
        getMultiCitySource();

        GpsTracker gpsTracker = new GpsTracker(this);

        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            Log.e("Latitude",gpsTracker.getLatitude()+"");
        }

        handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if ( AppUtil.getPreferenceBoolean(AppConstants.PREF_KEY_IS_LOGEDIN)){
                    String response=AppUtil.getPreference(AppConstants.PREF_KEY_USER_DETAILS_JSON);
                    //AppConstants.USER_DETAILS= ResponseParser.parseIntoUserDetails(response);
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }
                else {
                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                }

                finish();
            }
        },3000);
    }

    private void getStateList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject=new JSONObject();
                try {

                }
                catch (Exception e){
                    e.printStackTrace();
                }
               Object objects= ApiCaller.getCaller().getServiceResponce(ApiCaller.RequestType.HTTP_POST,
                        ApiServiceUrl.BASE_URL + ApiServiceUrl.STATE_LIST, jsonObject);

                if (objects instanceof Exception) {

                } else if (objects instanceof String) {

                    try {
                        JSONObject ob= new JSONObject(objects.toString());
                        if (ob.optBoolean("Result")){
                            AppConstants.stateList=ResponseParser.parseIntoStateList(objects.toString());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }

            }
        }).start();
    }
//    @Deprecated
//    private void getCityList() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                JSONObject jsonObject = new JSONObject();
//                try {
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Object object = ApiCaller.getCaller().getServiceResponce(ApiCaller.RequestType.HTTP_POST,
//                        ApiServiceUrl.BASE_URL + ApiServiceUrl.CITY_LIST, jsonObject);
//
//                if (object instanceof Exception) {
//
//                } else if (object instanceof String) {
//
//
//                    try {
//                        JSONObject ob = new JSONObject(object.toString());
//                        if (ob.optBoolean("Result")) {
//                            AppConstants.cityList = ResponseParser.parseIntoCityList(object.toString());
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//
//                    }
//
//                }
//
//            }
//        }).start();
//    }

        //OnewaySource

    private void getOneWaySource() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = CreateJsonFor.oneWaySource("");
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Object object = ApiCaller.getCaller().getServiceResponce(ApiCaller.RequestType.HTTP_POST,
                        ApiServiceUrl.BASE_URL + ApiServiceUrl.CITY_LIST_TRAVEL, jsonObject);

                if (object instanceof Exception) {

                } else if (object instanceof String) {


                    try {
                        JSONObject ob = new JSONObject(object.toString());
                        if (ob.optBoolean("Result")) {
                            AppConstants.oneWaySource = ResponseParser.parseIntoCityList(object.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }

            }
        }).start();
    }

    //RoundTripSource

    private void getRoundTripSource() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = CreateJsonFor.roundTrip("");
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Object object = ApiCaller.getCaller().getServiceResponce(ApiCaller.RequestType.HTTP_POST,
                        ApiServiceUrl.BASE_URL + ApiServiceUrl.CITY_LIST_TRAVEL, jsonObject);

                if (object instanceof Exception) {

                } else if (object instanceof String) {


                    try {
                        JSONObject ob= new JSONObject(object.toString());
                        if (ob.optBoolean("Result")){
                            AppConstants.roundTripSource=ResponseParser.parseIntoCityList(object.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }

            }
        }).start();

    }

    //MultiCitySource

    private void getMultiCitySource() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = CreateJsonFor.multiCity("");
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Object object = ApiCaller.getCaller().getServiceResponce(ApiCaller.RequestType.HTTP_POST,
                        ApiServiceUrl.BASE_URL + ApiServiceUrl.CITY_LIST_TRAVEL, jsonObject);

                if (object instanceof Exception) {

                } else if (object instanceof String) {


                    try {
                        JSONObject ob = new JSONObject(object.toString());
                        if (ob.optBoolean("Result")) {
                            AppConstants.multiCitySource = ResponseParser.parseIntoCityList(object.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }

            }
        }).start();
    }



}
