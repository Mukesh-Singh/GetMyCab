package gmc.com.getmycab.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppConstants;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.Utils.DialogUtil;
import gmc.com.getmycab.asyntask.BaseAsyncTask;
import gmc.com.getmycab.base.BaseActivityWithBackHeader;
import gmc.com.getmycab.bean.BookingDetailsBean;
import gmc.com.getmycab.ccavenue.CcAvenueConstants;
import gmc.com.getmycab.ccavenue.activity.WebViewActivity;
import gmc.com.getmycab.networkapi.ApiCaller;
import gmc.com.getmycab.networkapi.ApiServiceUrl;
import gmc.com.getmycab.parser.ResponseParser;

/**
 * Created by Baba on 10/1/2015.
 */
public class BookingDetailsActivity extends BaseActivityWithBackHeader{
    TextView paymentSuccess,bookingId,tripDetils,fareDetails,paymentDetils,operatorDetails,customerDetials;
    String bId;
    public boolean isfromPayment;
    String amt;
    private String stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        setTitle("Car Ticket");
        enableHeaderBackButton();
        initUi();
        //callGetTransactjionId();
        callDetailsApi();

    }

    void initUi(){
        paymentSuccess=(TextView)findViewById(R.id.row0);
        bookingId=(TextView)findViewById(R.id.row1);
        tripDetils=(TextView)findViewById(R.id.row2);
        fareDetails=(TextView)findViewById(R.id.row3);
        paymentDetils=(TextView)findViewById(R.id.row4);
        operatorDetails=(TextView)findViewById(R.id.row5);
        //customerDetials=(TextView)findViewById(R.id.row6);
        isfromPayment=getIntent().getBooleanExtra("from_payment", false);
        bId=getIntent().getStringExtra("booking_id");

        if (isfromPayment) {
            if (getIntent().hasExtra("transStatus")) {
                amt=getIntent().getStringExtra("amt");
                stat = getIntent().getStringExtra("transStatus");
                paymentSuccess.setVisibility(View.VISIBLE);
                paymentSuccess.setText("" + stat + "");
                if (stat.equalsIgnoreCase(WebViewActivity.TRANSACTION_SUCCESS)) {
                    callGetTransactjionId();
                }

            }
        }

    }

    private void updateUi(BookingDetailsBean d) {

        SpannableString bookingid=new SpannableString(getBlackSpanableText("GetMeCab Booking No: ")+d.getOrderId()+"\n");

        SpannableString bookingDtls= new SpannableString(
                getBlackSpanableText("Trip Detail: ")+d.getOutstationTravelType()+"\n"+
                getBlackSpanableText("Pick-up Date & Time: ")+d.getBookingStartTime()+"\n"+
                getBlackSpanableText("Car Type: ")+d.getCarModel()+"\n"+
                getBlackSpanableText("Pick-up Location: ")+d.getAreaSelcted().replace("null","")+"\n");

        SpannableString fd= new SpannableString(
                getItalic("Fare: ") + d.getTotal()+"\n"+
                        getItalic("Service Tax (5.80%): ") +d.getServiceTax()+"\n");

//        int pendAmt= 0;
//        try{
//            pendAmt=Integer.parseInt(d.getTotal())+Integer.parseInt(d.getServiceTax())-Integer.parseInt()
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }

        SpannableString pendingamt= new SpannableString("\n"+
           getItalic("(The pending amount has to be paid at the end of the trip.)"));

        SpannableString operatorName= new SpannableString(
                getBlackSpanableText("Car Operator/Owner Name: ")+d.getOperator()+"\n"
        +"We wish you a pleasant and comfortable journey.\n");




        bookingId.setText(bookingid);
        tripDetils.setText(bookingDtls);
        fareDetails.setText(fd);
        paymentDetils.setText(pendingamt);
        operatorDetails.setText(operatorName);

    }



    private SpannableString getItalic(String text){
        SpannableString spannable= new SpannableString(text);
        spannable.setSpan(new StyleSpan(Typeface.ITALIC), 0, text.length(), 0);

        return spannable;
    }


    private SpannableString getBlackSpanableText(String text){
        SpannableString spannable= new SpannableString(text);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), 0);
        SpannableString boldSpan= new SpannableString(spannable);
        boldSpan.setSpan(new ForegroundColorSpan(getResources().getColor(
                        android.R.color.black)), 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return boldSpan;
    }

    private  void callGetTransactjionId(){
        JSONObject jsonObject= new JSONObject();
        //String userId= AppUtil.getPreference(AppConstants.PREF_KEY_CUSTOMER_ID);
        String url=ApiServiceUrl.BOOKING_SUCCESS.replace("OID",bId);
        url=url.replace("MI", CcAvenueConstants.MERCHANT_ID);
        url=url.replace("AMT",amt+"");
        BaseAsyncTask asyncTask= new BaseAsyncTask(this, new BaseAsyncTask.ServiceCallable() {
            @Override
            public void onServiceResultSuccess(String url, boolean success, String message, String response) {
                Log.e("Response", response.toString());

                if (success){
                    stat=stat+"\n"+message;
                    paymentSuccess.setText(stat);
                    Log.e("status:",stat);

                }


            }

            @Override
            public void onServiceResultFailure(String url, String error) {
                DialogUtil.showServerErrorDialog(BookingDetailsActivity.this);
            }
        }, ApiCaller.RequestType.HTTP_POST, url,jsonObject);
        asyncTask.setIsOtherUlr(true);

        asyncTask.execute();
    }



    private void callDetailsApi(){
        JSONObject jsonObject= new JSONObject();
        String userId= AppUtil.getPreference(AppConstants.PREF_KEY_CUSTOMER_ID);
        String url=ApiServiceUrl.BOOKING_BOOKING_DETAILS.replace("OID",bId);
        BaseAsyncTask asyncTask= new BaseAsyncTask(this, new BaseAsyncTask.ServiceCallable() {
            @Override
            public void onServiceResultSuccess(String url, boolean success, String message, String response) {
                Log.e("Response", response.toString());

                if (success){
                    BookingDetailsBean dtls= ResponseParser.parseIntoBookingDetailsBean(response);
                //BookingDetailsBean dtls= ResponseParser.parseIntoBookingDetailsBean(AppConstants.detisl);
                if (dtls!=null ){
                    updateUi(dtls);
                }
//                else
//                    AppUtil.showToastMessage(message);
            }


            }

            @Override
            public void onServiceResultFailure(String url, String error) {
                DialogUtil.showServerErrorDialog(BookingDetailsActivity.this);
            }
        }, ApiCaller.RequestType.HTTP_POST, url+userId,jsonObject);
        asyncTask.setIsOtherUlr(true);

        asyncTask.execute();
    }

    public void onBackPressed() {
        if (isfromPayment)
            backIcon.performClick();
        else
            finish();
    }


}
