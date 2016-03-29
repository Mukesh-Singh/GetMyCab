package gmc.com.getmycab.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppConstants;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.Utils.DialogUtil;
import gmc.com.getmycab.asyntask.BaseAsyncTask;
import gmc.com.getmycab.base.BaseActivityWithBackHeader;
import gmc.com.getmycab.bean.BookingBean;
import gmc.com.getmycab.bean.Car;
import gmc.com.getmycab.bean.CouponDiscount;
import gmc.com.getmycab.bean.ReferralDiscount;
import gmc.com.getmycab.ccavenue.CcAvenueConstants;
import gmc.com.getmycab.ccavenue.activity.WebViewActivity;
import gmc.com.getmycab.ccavenue.utility.AvenuesParams;
import gmc.com.getmycab.ccavenue.utility.ServiceUtility;
import gmc.com.getmycab.networkapi.ApiCaller;
import gmc.com.getmycab.networkapi.ApiServiceUrl;
import gmc.com.getmycab.parser.ResponseParser;

/**
 * Created by Baba on 9/13/2015.
 */
public class CouponsActivity extends BaseActivityWithBackHeader implements View.OnClickListener,BaseAsyncTask.ServiceCallable{

    EditText inputCoupon,inputReferal,inputRequirement;
    Button couponApply,referalApply,paynow;
    RadioButton advancePayment;
    CheckBox policy;
    String discountType;
    TextView applyResult;
    ReferralDiscount referralDiscount;
    CouponDiscount couponDiscount;
    TextView termAndCondition;
    float advancePaymentAmt=0.0f;
    int discountAmt=0;
    String mOrderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_activity);
        setTitle("PAYMENT");
        mOrderId=getIntent().getStringExtra("orderid");
        enableHeaderBackButton();
        initUI();

    }


    private void initUI() {
        inputCoupon=(EditText)findViewById(R.id.coupon_activity_coupon_input);
        inputReferal=(EditText)findViewById(R.id.coupon_activity_referal_input);
        inputRequirement=(EditText)findViewById(R.id.coupon_activity_specific_requirment);
        couponApply=(Button)findViewById(R.id.coupon_activity_coupon_apply);
        referalApply=(Button)findViewById(R.id.coupon_activity_referal_apply);
        paynow=(Button)findViewById(R.id.coupon_activity_pay_now);
        advancePayment=(RadioButton)findViewById(R.id.coupon_activity_pay_advance);
        policy=(CheckBox)findViewById(R.id.coupon_activity_policy_checkbox);
        applyResult=(TextView)findViewById(R.id.coupon_activity_apply_result);
        termAndCondition=(TextView)findViewById(R.id.check_termAndCondition);
        int amount=AppUtil.getPreferenceInt(AppConstants.BOOKING_AMOUNT);
        advancePaymentAmt= ((amount*20)/100);

        advancePayment.setText(getString(R.string.advance)+" Rs "+advancePaymentAmt);
        couponApply.setOnClickListener(this);
        referalApply.setOnClickListener(this);
        paynow.setOnClickListener(this);
        termAndCondition.setOnClickListener(this);
        inputCoupon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyResult.setVisibility(View.GONE);
                //inputReferal.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputReferal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                applyResult.setVisibility(View.GONE);
                //inputCoupon.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==couponApply.getId()){
            discountType="coupon";
            if (isValid()){
                callDiscountWebService("discount",inputCoupon.getText().toString());
                inputReferal.setText("");
            }

        }
        else if (v.getId()==referalApply.getId()){
            discountType="referal";
            if (isValid()){
                callDiscountWebService("referral",inputReferal.getText().toString());
                inputCoupon.setText("");
            }
        }
        else if (v.getId()==paynow.getId()){
            if (policy.isChecked()) {
                if (mOrderId != null && !mOrderId.trim().isEmpty()) {
                    //callBookingApi();
                    moveToCcAvenuePayment();
                }
                else
                    AppUtil.showToastMessage("Unable to find Order Id.");

            }
            else {
                AppUtil.showToastMessage("Please accept Term and Condition");
            }
        }
        else if (v.getId()==termAndCondition.getId()){
            startActivity(new Intent(this,TermAndConditionActivity.class));
        }

    }
    
//    private void callBookingApi(){
//        JSONObject jsonObject= getJsonForBooking("6765");
//        BaseAsyncTask asyncTask= new BaseAsyncTask(this, new BaseAsyncTask.ServiceCallable() {
//            @Override
//            public void onServiceResultSuccess(String url, boolean success, String message, String response) {
//                Log.e("Response", response.toString());
//                //AppUtil.showToastMessage(message);
//                if (response.contains("Success") || response.contains("success")) {
//                    BookingBean bookingBean= ResponseParser.parseIntoBookingBean(response);
//                    moveToCcAvenuePayment(bookingBean.getOrderId());
//                }
//            }
//
//            @Override
//            public void onServiceResultFailure(String url, String error) {
//                DialogUtil.showServerErrorDialog(CouponsActivity.this);
//            }
//        }, ApiCaller.RequestType.HTTP_POST,ApiServiceUrl.BOOKING_URL,jsonObject);
//        asyncTask.setIsOtherUlr(true);
//
//        asyncTask.execute();
//    }
    
    


//    private JSONObject getJsonForBooking(String envId){
//        JSONObject jsonObject= new JSONObject();
//
//        try{
//            JSONObject requestJson= new JSONObject(AppUtil.getPreference(AppConstants.BOOKING_QUERY_JSON));
//            jsonObject.put("uid","mobileapp");
//            jsonObject.put("pwd","mobileapp");
//            jsonObject.put("email",AppUtil.getPreference(AppConstants.PREF_KEY_USER_EMAIL_ID));
//            jsonObject.put("inv_id",envId);
//            jsonObject.put("destinationCity",requestJson.optString("to_city",""));
//            jsonObject.put("outstation_travel_type",requestJson.optString("travel_type",""));
//            jsonObject.put("mc_list",requestJson.optString("moreCites",""));
//            jsonObject.put("travelDate",requestJson.optString("date",""));
//            jsonObject.put("travel_duration",requestJson.optString("duration",""));
//            jsonObject.put("travelTime",requestJson.optString("time",""));
//            jsonObject.put("sourceCity",requestJson.optString("from_city",""));
//            jsonObject.put("ac_type",AppUtil.getPreference(AppConstants.BOOKING_AC_TYPE));
//            jsonObject.put("tourType",requestJson.optString("tourType", ""));
//            jsonObject.put("pickup_address",AppUtil.getPreference(AppConstants.BOOKING_PICKUP_ADDRESS));
//            jsonObject.put("customer_firstname",AppUtil.getPreference(AppConstants.PREF_KEY_USER_NAME));
//            jsonObject.put("customer_lastname","");
//            jsonObject.put("customer_mobile",AppUtil.getPreference(AppConstants.PREF_KEY_USER_PHONE));
//            jsonObject.put("customer_emailid",AppUtil.getPreference(AppConstants.PREF_KEY_USER_EMAIL_ID));
//            jsonObject.put("comment",inputRequirement.getText().toString());
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }




    private boolean isValid(){
        if (discountType.equalsIgnoreCase("coupon")){
            if (AppUtil.isEmpty(inputCoupon)){
                AppUtil.setError(inputCoupon, "Please enter Coupon Code");
                return false;
            }
        }
        else if (discountType.equalsIgnoreCase("referal")){
            if (AppUtil.isEmpty(inputReferal)){
                AppUtil.setError(inputReferal,"Please enter Referral Code");
                return false;
            }
        }

        return true;
    }

    private void updateDiscount(boolean isValid,String amt){
        String resultMessage="";
        if (!isValid){
            resultMessage="Invalid code !!!";
            applyResult.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            discountAmt=0;
        }
        else
        {
            try{
                double d=Double.parseDouble(amt);
                discountAmt= (int) d;
            }
            catch (Exception e){
                e.printStackTrace();
                discountAmt=0;
            }
            resultMessage="Applied successfully.\nYou will get total discount of Rs. " + discountAmt;
            applyResult.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        }
        applyResult.setVisibility(View.VISIBLE);
        applyResult.setText(resultMessage);
        advancePayment.setText(getString(R.string.advance)+" Rs "+(advancePaymentAmt-discountAmt));
    }


    @Override
    public void onServiceResultSuccess(String url, boolean success, String message, String response) {
        //
        if (success){
            if (url.equalsIgnoreCase(ApiServiceUrl.DISCOUNT_COUPON)){
                if (message.contains("not valid")){
                        updateDiscount(false,"");
                }
                else {

                    if (discountType.equalsIgnoreCase("coupon")) {
                        couponDiscount = ResponseParser.parseIntoCouponDiscount(response);
                        referralDiscount = null;
                        updateDiscount(true,couponDiscount.getDiscount());



                    } else if (discountType.equalsIgnoreCase("referal")) {
                        referralDiscount = ResponseParser.parseIntoReferralDiscount(response);
                        couponDiscount = null;
                        updateDiscount(true,referralDiscount.getDiscount());

                    }
                }


            }
        }
        else {
            AppUtil.showToastMessage(message);
        }


    }
    private void moveToCcAvenuePayment(/*String orderId*/){

        String finalAmt=""+(advancePaymentAmt-discountAmt)+"";
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(AvenuesParams.ACCESS_CODE, CcAvenueConstants.ACCESS_CODE);
        intent.putExtra(AvenuesParams.MERCHANT_ID, CcAvenueConstants.MERCHANT_ID);
        intent.putExtra(AvenuesParams.ORDER_ID, mOrderId);
        intent.putExtra(AvenuesParams.CURRENCY, CcAvenueConstants.CURRENCY);
        intent.putExtra(AvenuesParams.AMOUNT, finalAmt/*"1.00"*/);
        intent.putExtra(AvenuesParams.REDIRECT_URL, CcAvenueConstants.REDIRECT_URL);
        intent.putExtra(AvenuesParams.CANCEL_URL, CcAvenueConstants.CANCEL_URL);
        intent.putExtra(AvenuesParams.RSA_KEY_URL, CcAvenueConstants.RSA_URL);


//        intent.putExtra(AvenuesParams.ACCESS_CODE, "4YRUXLSRO20O8NIH");
//        intent.putExtra(AvenuesParams.MERCHANT_ID, "2");
//        intent.putExtra(AvenuesParams.ORDER_ID, "123453");
//        intent.putExtra(AvenuesParams.CURRENCY, "INR");
//        intent.putExtra(AvenuesParams.AMOUNT, "1.00");

  //      intent.putExtra(AvenuesParams.REDIRECT_URL, "http://122.182.6.216/merchant/ccavResponseHandler.jsp");
//        intent.putExtra(AvenuesParams.CANCEL_URL,   "http://122.182.6.216/merchant/ccavResponseHandler.jsp");
        //intent.putExtra(AvenuesParams.RSA_KEY_URL,  "http://122.182.6.216/merchant/GetRSA.jsp");




//        Intent intent = new Intent(this, InitialScreenActivity.class);
        startActivity(intent);
    }

    @Override
    public void onServiceResultFailure(String url, String error) {
        DialogUtil.showServerErrorDialog(this);
    }

   private void callDiscountWebService(String codeType,String code){

       JSONObject jsonObject= new JSONObject();
       try{
           jsonObject.put("code_type",codeType);
           jsonObject.put("code",code);
           jsonObject.put("customer_id", AppUtil.getPreference(AppConstants.PREF_KEY_CUSTOMER_ID));

       }catch (Exception e){
           e.printStackTrace();
       }

       new BaseAsyncTask(this,true,this, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.DISCOUNT_COUPON,jsonObject).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
