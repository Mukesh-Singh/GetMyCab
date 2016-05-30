package gmc.com.getmycab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppConstants;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.Utils.DialogUtil;
import gmc.com.getmycab.asyntask.BaseAsyncTask;
import gmc.com.getmycab.base.BaseActivityWithBackHeader;
import gmc.com.getmycab.bean.BookingBean;
import gmc.com.getmycab.bean.Car;
import gmc.com.getmycab.networkapi.ApiCaller;
import gmc.com.getmycab.networkapi.ApiServiceUrl;
import gmc.com.getmycab.parser.ResponseParser;

/**
 * Created by Baba on 9/8/2015.
 */
public class BookingConfirmationActivity  extends BaseActivityWithBackHeader implements View.OnClickListener,BaseAsyncTask.ServiceCallable{

    private TextView carname,totalprice,perkmprice,esitmatedDistance,cartype,dateTime,bookinupto,tripType,totalbreakpu;
    private EditText pikupAddress;
    private Button booknow;
    private ImageView dropdown;
    private LinearLayout detailsLayout;
    private Car car;

    //Price Details
    TextView farePerkm, estimatedCharge, additionalCoset, distanceDetails, noteText;
    LinearLayout priceDetailsLayou;
    ImageView priceDropDown;
//
    private LinearLayout priceDetailsDropDownLayout,bookingDetialsDropDwonLayout;
    private ScrollView mScrollVeiw;
    private String detailsType="";
    private int totalAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);
        enableHeaderBackButton();
        enableImageMenu();
        setTitle("BOOKING CONFIRMATION");
        List<Car> list= ResponseParser.parseIntoCarList(AppUtil.getPreference(AppConstants.CAB_SEARCH_RESULT_JSON));
        intiUI();
        int pos=getIntent().getIntExtra("position", -1);
        if (pos!=-1)
            car=list.get(pos);

        detailsType=getIntent().getStringExtra("from");

        updateUI();

        if (detailsType.equalsIgnoreCase("book")){
            bookingDetialsDropDwonLayout.performClick();
        }
        else if (detailsType.equalsIgnoreCase("fare")){
            priceDetailsDropDownLayout.performClick();
        }




    }

//    private String getCalCulatedPrice(){
//        try {
//            int duration = AppUtil.getPreferenceInt(AppConstants.CAB_SEARCH_DURATION);
//            int baseP = Integer.parseInt(car.getPriceDetails().getBasePrice());
//            int da=Integer.parseInt(car.getPriceDetails().getBaseDriverCharge());
//            int totalDA=(duration*da);
//            int DAPluseBasePrice=baseP+totalDA;
//            double serviceTax=5.8;
//            double tax= ((DAPluseBasePrice*serviceTax)/100);
//            int statTax=0;
//            if (car.getStateTaxDetails().getStateTax()!=null && !car.getStateTaxDetails().getStateTax().isEmpty())
//                statTax=Integer.parseInt(car.getStateTaxDetails().getStateTax());
//            Double total= DAPluseBasePrice+tax+statTax;
//
//            return String.valueOf(total.intValue());
//
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        return car.getPriceDetails().getBasePrice();
//    }

    private String getPriceForOutStation(){
//        int duration = AppUtil.getPreferenceInt(AppConstants.CAB_SEARCH_DURATION);
//        int baseP = Integer.parseInt(car.getPriceDetails().getBasePrice());
//        int distance= 1;
//        try{
//            distance= (int) Float.parseFloat(car.getPriceDetails().getDistance());
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            distance= Integer.parseInt(car.getPriceDetails().getDistance());
//        }
//
//        float f=duration*baseP*distance;
//        return String.valueOf(f);

        return car.getPriceDetails().getBasePrice();


    }



    private String getCalCulatedPriceForOneWay(){
//        try {
//            //int duration = AppUtil.getPreferenceInt(AppConstants.CAB_SEARCH_DURATION);
//            int totalP = Integer.parseInt(car.getPriceDetails().getTotalPrice());
//            double serviceTax=5.8;
//            double tax= ((totalP*serviceTax)/100);
//
//            Double total= totalP+tax;
//
//            return String.valueOf(total.intValue());
//
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        return car.getPriceDetails().getBasePrice();
        return car.getPriceDetails().getTotalPrice();
    }



    private void updateUI() {
        if (car==null)
            return;
        try {
            String f = "Fare : " + getReadableString(car.getPriceDetails().getFare()) + " per km";
            if (car.getPriceDetails().getFare().trim().length() > 0) {
                SpannableString spannable = new SpannableString(f);
                //spannable.setSpan(new StyleSpan(Typeface.BOLD), 7, f.length(), 0);
                //spannable= new SpannableString(spannable);
                spannable.setSpan(
                        new ForegroundColorSpan(getResources().getColor(
                                android.R.color.black)), 7, spannable.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                perkmprice.setText(spannable);
                totalprice.setText("RS. " + getReadableString(/*car.getPriceDetails().getBasePrice())*/getPriceForOutStation()));
                totalAmt=Integer.parseInt(/*car.getPriceDetails().getBasePrice()*/getPriceForOutStation());
                //totalbreakpu.setVisibility(View.VISIBLE);
            } else {
                totalprice.setText("RS. " + getReadableString(/*car.getPriceDetails().getTotalPrice())*/getCalCulatedPriceForOneWay()));
                totalAmt=Integer.parseInt(/*car.getPriceDetails().getTotalPrice()*/getCalCulatedPriceForOneWay());
                perkmprice.setVisibility(View.GONE);
                //totalbreakpu.setVisibility(View.GONE);
            }

            String ac = car.getCarDetails().getAcType();
            String name = car.getCarDetails().getCarModel();
            if (ac.equalsIgnoreCase("1")) {
                name = name + " (AC)";
            } else
                name = name + " (NON AC)";

            this.carname.setText(name);
            String dis = "Estimated distance : " + getReadableString(car.getPriceDetails().getDistance()) + " km";

            if (car.getPriceDetails().getDistance().trim().length() > 0) {
                SpannableString spaDis = new SpannableString(dis);
                spaDis.setSpan(
                        new ForegroundColorSpan(getResources().getColor(
                                android.R.color.black)), 20, spaDis.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                esitmatedDistance.setText(spaDis);
            } else
                esitmatedDistance.setVisibility(View.GONE);

            cartype.setText(name);
            dateTime.setText(AppUtil.getPreference(AppConstants.CAB_SEARCH_DATE) + " on " + AppUtil.getPreference(AppConstants.CAB_SEARCH_TIME));
            int duration = AppUtil.getPreferenceInt(AppConstants.CAB_SEARCH_DURATION);
            if (duration > 0) {
                SimpleDateFormat df = new SimpleDateFormat(
                        "dd-MM-yyyy");
                String sDate = AppUtil.getPreference(AppConstants.CAB_SEARCH_DATE);
                try {
                    Calendar c = Calendar.getInstance();
                    c.setTime(df.parse(sDate));
                    c.add(Calendar.DATE, (duration-1));

                    String nDate = df.format(c.getTime());

                    bookinupto.setText("11:59 PM on " + nDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                bookinupto.setText("11:59 PM on " + AppUtil.getPreference(AppConstants.CAB_SEARCH_DATE));
            }
            tripType.setText(AppUtil.getPreference(AppConstants.CAB_SEARCH_FROM_TO));
            setPriceDetails();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setPriceDetails() {

        LinearLayout distanceDetailsLayout=(LinearLayout)findViewById(R.id.layout_distance_details_ll);
        LinearLayout cab_fareperkm=(LinearLayout)findViewById(R.id.booking_confirmation_details_cabFareDetails_FarePerKmTitle);
        LinearLayout cab_estimatedkm=(LinearLayout)findViewById(R.id.booking_confirmation_details_cabFareDetails_estimatedKmCharged);
        LinearLayout cab_fixedcost=(LinearLayout)findViewById(R.id.fixed_cost_layout);
        TextView fixedcosttext=(TextView)findViewById(R.id.fixed_cost_text);

            try {
                distanceDetails.setVisibility(View.GONE);
                distanceDetailsLayout.setVisibility(View.GONE);

                //for out station
                if (car.getPriceDetails().getFare() != null && car.getPriceDetails().getFare().length() > 0) {
                    cab_estimatedkm.setVisibility(View.VISIBLE);
                    cab_fareperkm.setVisibility(View.VISIBLE);
                    cab_fixedcost.setVisibility(View.GONE);
                    farePerkm.setText("Rs. " + getReadableString(car.getPriceDetails().getFare()) + " / Km");
                    estimatedCharge.setText("" + getReadableString(car.getPriceDetails().getDistance() + ""));


                    additionalCoset.setText(AppUtil.getAdditionalCostTextForOutStation(car));
                    String dist = "";
                    for (int i = 0; i < car.getPriceDetails().getDistanceDetails().size(); i++) {
                        dist = dist + car.getPriceDetails().getDistanceDetails().get(i).getSourceCity() + "    to    " +
                                car.getPriceDetails().getDistanceDetails().get(i).getDestinationCity() + "       " + car.getPriceDetails().getDistanceDetails().get(i).getDistance() + " Km\n";
                    }
                    if (dist.length() > 0) {
                        distanceDetailsLayout.setVisibility(View.VISIBLE);
                        distanceDetails.setVisibility(View.VISIBLE);
                        distanceDetails.setText(dist);
                    } else {
                        distanceDetails.setText("");
                        distanceDetails.setVisibility(View.GONE);
                        distanceDetailsLayout.setVisibility(View.GONE);
                        //findViewById(R.id.booking_confirmation_price_distanceDetailsTitle).setVisibility(View.GONE);
                    }


                    noteText.setText(AppUtil.getNoteForOutStation(car));
                } else {
                    //Oneway
                    cab_estimatedkm.setVisibility(View.GONE);
                    cab_fareperkm.setVisibility(View.VISIBLE);
                    findViewById(R.id.price_details_fareKm).setVisibility(View.GONE);
                    farePerkm.setText("(includes DA, State Tax, Toll and Service Tax)");
                    cab_fixedcost.setVisibility(View.VISIBLE);
                    additionalCoset.setText(AppUtil.getAdditionalCostTextForOneWay(car));
                    noteText.setText(AppUtil.getNoteForOneWay(car));

//                findViewById(R.id.booking_confirmation_details_cabFareDetails_FarePerKmTitle).setVisibility(View.GONE);
//                findViewById(R.id.booking_confirmation_details_cabFareDetails_estimatedKmCharged).setVisibility(View.GONE);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    private String getReadableString(String str){
        if (str==null || str.length()==0)
            return "0";
        else
            return str;
    }


    private void intiUI() {
        carname =(TextView)findViewById(R.id.booking_confirmation_cabname);
        totalprice=(TextView)findViewById(R.id.booking_confirmation_totalfare);
        perkmprice=(TextView)findViewById(R.id.booking_confirmation_perkmfare);;
        esitmatedDistance=(TextView)findViewById(R.id.booking_confirmation_estimateddistance);
        cartype=(TextView)findViewById(R.id.booking_confirmation_cartype);
        dateTime=(TextView)findViewById(R.id.booking_confirmation_pickupdatetime);
        bookinupto=(TextView)findViewById(R.id.booking_confirmation_bookingupto);;
        tripType=(TextView)findViewById(R.id.booking_confirmation_triptype);
        pikupAddress=(EditText)findViewById(R.id.booking_confirmation_pickupaddress);
        booknow=(Button)findViewById(R.id.booking_confirmation_booknow);
        dropdown=(ImageView)findViewById(R.id.booking_confirmation_detailsdropdown);
        detailsLayout=(LinearLayout)findViewById(R.id.booking_confirmation_detailslayout);
        totalbreakpu=(TextView)findViewById(R.id.booking_confirmation_totalfare_msg);
        bookingDetialsDropDwonLayout=(LinearLayout)findViewById(R.id.booking_confirmation_dropDownLayout);
        bookingDetialsDropDwonLayout.setOnClickListener(this);

        mScrollVeiw=(ScrollView)findViewById(R.id.activity_booking_scrollview);

        booknow.setOnClickListener(this);
        //dropdown.setOnClickListener(this);


        //price details
        LinearLayout ll= (LinearLayout)findViewById(R.id.booking_confirmation_price_detailslayout);
        ll.setBackgroundColor(getResources().getColor(R.color.white));

        priceDetailsDropDownLayout=(LinearLayout)findViewById(R.id.booking_confirmation_pricedetailLL);
//
//
        farePerkm =(TextView)findViewById(R.id.booking_confirmation_price_fareperkkm);
        estimatedCharge =(TextView)findViewById(R.id.booking_confirmation_price_estimatedkmCharged);
        additionalCoset =(TextView)findViewById(R.id.booking_confirmation_price_additionalCost);
          distanceDetails =(TextView)findViewById(R.id.booking_confirmation_price_distanceDetails);
        noteText =(TextView)findViewById(R.id.booking_confirmation_price_notes);
        priceDetailsLayou=(LinearLayout)findViewById(R.id.booking_confirmation_price_detailslayout);
        //setPriceDetails();
//
//        priceDropDown=(ImageView)findViewById(R.id.booking_confirmation_price_detailsdropdown);
//        //priceDropDown.setOnClickListener(this);
        priceDetailsDropDownLayout.setOnClickListener(this);
//
//
//        detailsLayout.setVisibility(View.GONE);
//        priceDetailsLayou.setVisibility(View.GONE);

    }



    @Override
    public void onClick(View v) {

        if (v.getId()==bookingDetialsDropDwonLayout.getId()){
            if (detailsLayout.getVisibility()==View.VISIBLE)
                detailsLayout.setVisibility(View.GONE);
            else
                detailsLayout.setVisibility(View.VISIBLE);
        }
        else if (v.getId()==priceDetailsDropDownLayout.getId()){
            if (priceDetailsLayou.getVisibility()==View.VISIBLE)
                priceDetailsLayou.setVisibility(View.GONE);
            else {
                priceDetailsLayou.setVisibility(View.VISIBLE);
            }
            mScrollVeiw.post(new Runnable() {
                @Override
                public void run() {
                    mScrollVeiw.smoothScrollBy(0,1000);
                }
            });
        }

        else if (v.getId()==booknow.getId()){
            if (isValid()) {
                AppUtil.savePreference(AppConstants.BOOKING_AC_TYPE,car.getCarDetails().getAcType());
                AppUtil.savePreference(AppConstants.BOOKING_PICKUP_ADDRESS,pikupAddress.getText().toString());
                AppUtil.savePreference(AppConstants.BOOKING_AMOUNT,totalAmt);
                callBookingApi();
//                Intent intent =new Intent(this, CouponsActivity.class);
//                Bundle b= new Bundle();
//                b.putSerializable("car",car);
//                startActivity(intent);
            }
        }
    }

    private void moveToPayment(String orderid){
        Intent intent= new Intent(this, CouponsActivity.class);
        intent.putExtra("orderid", orderid);
        startActivity(intent);


    }



    private void callBookingApi(){
        JSONObject jsonObject= getJsonForBooking(car.getCarInventoryId());
        BaseAsyncTask asyncTask= new BaseAsyncTask(this, new BaseAsyncTask.ServiceCallable() {
            @Override
            public void onServiceResultSuccess(String url, boolean success, String message, String response) {
                Log.e("Response", response.toString());
                //AppUtil.showToastMessage(message);
                if (response.contains("Success") || response.contains("success")) {
                    BookingBean bookingBean= ResponseParser.parseIntoBookingBean(response);
                    moveToPayment(bookingBean.getOrderId());
                }
                else {
                    DialogUtil.showOk(BookingConfirmationActivity.this,message);
                }
            }

            @Override
            public void onServiceResultFailure(String url, String error) {
                DialogUtil.showServerErrorDialog(BookingConfirmationActivity.this);
            }
        }, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.BOOKING_URL,jsonObject);
        asyncTask.setIsOtherUlr(true);

        asyncTask.execute();
    }

    private JSONObject getJsonForBooking(String envId){
        JSONObject jsonObject= new JSONObject();

        try{
            JSONObject requestJson= new JSONObject(AppUtil.getPreference(AppConstants.BOOKING_QUERY_JSON));
            jsonObject.put("uid","mobileapp");
            jsonObject.put("pwd","mobileapp");
            jsonObject.put("email",AppUtil.getPreference(AppConstants.PREF_KEY_USER_EMAIL_ID));
            jsonObject.put("inv_id",envId);
            jsonObject.put("destinationCity",requestJson.optString("to_city",""));
            jsonObject.put("outstation_travel_type",requestJson.optString("travel_type",""));
            jsonObject.put("mc_list",requestJson.optString("moreCites",""));
            jsonObject.put("travelDate",requestJson.optString("date",""));
            jsonObject.put("travel_duration",requestJson.optString("duration",""));
            jsonObject.put("travelTime",requestJson.optString("time",""));
            jsonObject.put("sourceCity",requestJson.optString("from_city",""));
            jsonObject.put("ac_type",AppUtil.getPreference(AppConstants.BOOKING_AC_TYPE));
            jsonObject.put("tourType",requestJson.optString("tourType", ""));
            jsonObject.put("pickup_address",AppUtil.getPreference(AppConstants.BOOKING_PICKUP_ADDRESS));
            jsonObject.put("customer_firstname",AppUtil.getPreference(AppConstants.PREF_KEY_USER_NAME));
            jsonObject.put("customer_lastname","");
            jsonObject.put("customer_mobile",AppUtil.getPreference(AppConstants.PREF_KEY_USER_PHONE));
            jsonObject.put("customer_emailid",AppUtil.getPreference(AppConstants.PREF_KEY_USER_EMAIL_ID));
            jsonObject.put("comment","No requirement");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }





    private boolean isValid(){
        if (AppUtil.isEmpty(pikupAddress)){
            AppUtil.setError(pikupAddress, "Please enter pick-up address");
            return false;
        }

        return true;
    }

    @Override
    public void onServiceResultSuccess(String url, boolean success, String message, String response) {

    }

    @Override
    public void onServiceResultFailure(String url, String error) {

    }





    public void moveHome() {
        Intent intent= new Intent(this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
