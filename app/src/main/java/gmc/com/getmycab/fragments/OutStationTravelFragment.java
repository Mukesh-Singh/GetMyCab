package gmc.com.getmycab.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppConstants;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.Utils.DialogUtil;
import gmc.com.getmycab.activity.CabSearchResultActivity;
import gmc.com.getmycab.asyntask.BaseAsyncTask;
import gmc.com.getmycab.bean.Car;
import gmc.com.getmycab.bean.City;
import gmc.com.getmycab.networkapi.ApiCaller;
import gmc.com.getmycab.networkapi.ApiServiceUrl;
import gmc.com.getmycab.networkapi.CreateJsonFor;
import gmc.com.getmycab.networkapi.ServiceUtil;
import gmc.com.getmycab.parser.ResponseParser;

/**
 * Created by Baba on 9/5/2015.
 */
public class OutStationTravelFragment extends Fragment implements View.OnClickListener, BaseAsyncTask.ServiceCallable {
    private RadioGroup mRadioGroup;
    private RadioButton mRoundtripRadio, mMulticityRadio;
    private Button getMyCabButton;

    private TextView mRoundTripFromCity, mRoundTripToCity, mRoundTripTravelDate, mRoundTripTravelTime;
    EditText mRoundTripTravelDuration;


    private TextView mMulticityFromCtity, mMulticityToCity, mMulticityTravelDate, mMulticityTravelTime;
    EditText mMulticityTravelDuration;
    ImageView mMulticityAddMore;
    Button mRemovecity;
    LinearLayout mMulticityAddMoreViewLL;

    LinearLayout mRoundTripLLView, mMulticityLLView;
    ScrollView mScrollView;


    private final String CITY = "CITY";
    private final String DATE = "DATE";
    private final String TIME = "TIME";
    private String searchedMoreCity;
    private JSONObject jsonObject;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.out_station_travel, container, false);
        initUi(view);
        return view;
    }

    private void initUi(View view) {
        mRadioGroup = (RadioGroup) view.findViewById(R.id.out_station_radioGroup);
        mRoundtripRadio = (RadioButton) view.findViewById(R.id.out_station_round_trip_radio);
        mMulticityRadio = (RadioButton) view.findViewById(R.id.out_station_multicity_radio);
        mRoundTripFromCity = (TextView) view.findViewById(R.id.one_way_travel_fromcity);
        //setClickListnerOnDropDown(mRoundTripFromCity, CITY);
        mRoundTripFromCity.setOnClickListener(this);

        mRoundTripToCity = (TextView) view.findViewById(R.id.one_way_travel_tocity);
        //setClickListnerOnDropDown(mRoundTripToCity, CITY);
        mRoundTripToCity.setOnClickListener(this);

        mRoundTripTravelDate = (TextView) view.findViewById(R.id.one_way_travel_date);
        setClickListnerOnDropDown(mRoundTripTravelDate, DATE);
        mRoundTripTravelTime = (TextView) view.findViewById(R.id.one_way_travel_time);
        setClickListnerOnDropDown(mRoundTripTravelTime, TIME);
        mRoundTripTravelDuration = (EditText) view.findViewById(R.id.one_way_travel_duration);
        mRoundTripTravelDuration.setVisibility(View.VISIBLE);

        mMulticityFromCtity = (TextView) view.findViewById(R.id.multicity_trip_fromcity);
        //setClickListnerOnDropDown(mMulticityFromCtity, CITY);
        mMulticityFromCtity.setOnClickListener(this);

        mMulticityToCity = (TextView) view.findViewById(R.id.multicity_trip_tocity);
        //setClickListnerOnDropDown(mMulticityToCity, CITY);
        mMulticityToCity.setOnClickListener(this);

        mMulticityTravelDate = (TextView) view.findViewById(R.id.multicity_trip_date);
        setClickListnerOnDropDown(mMulticityTravelDate, DATE);
        mMulticityTravelTime = (TextView) view.findViewById(R.id.multicity_trip_time);
        setClickListnerOnDropDown(mMulticityTravelTime, TIME);
        mMulticityTravelDuration = (EditText) view.findViewById(R.id.multicity_trip_travel_duration);

        mMulticityAddMore = (ImageView) view.findViewById(R.id.multicity_trip_addcity_image);
        mRemovecity = (Button) view.findViewById(R.id.multicity_trip_remove_image);
        mRemovecity.setVisibility(View.GONE);
        mMulticityAddMoreViewLL = (LinearLayout) view.findViewById(R.id.multicity_trip_addmorecity_ll);
        //addmoreCity();

        getMyCabButton = (Button) view.findViewById(R.id.out_station_multicity_getmecabbutton);
        mScrollView=(ScrollView)view.findViewById(R.id.out_station_travel_scrollview);


        getMyCabButton.setOnClickListener(this);
        mMulticityAddMore.setOnClickListener(this);
        mRemovecity.setOnClickListener(this);


        mRoundTripLLView = (LinearLayout) view.findViewById(R.id.out_station_round_trip_view);
        mMulticityLLView = (LinearLayout) view.findViewById(R.id.out_station_multicity_view);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                if (id == mRoundtripRadio.getId()) {
                    mRoundTripLLView.setVisibility(View.VISIBLE);
                    mMulticityLLView.setVisibility(View.GONE);
                } else if (id == mMulticityRadio.getId()) {
                    mRoundTripLLView.setVisibility(View.GONE);
                    mMulticityLLView.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void  callCityListForRoundTripSource()
    {
        if (AppConstants.roundTripSource==null || AppConstants.roundTripSource.size()==0) {
            ServiceUtil.callCityListServies(getActivity(), CreateJsonFor.roundTrip(""), new BaseAsyncTask.ServiceCallable() {
                @Override
                public void onServiceResultSuccess(String url, boolean success, String message, String response) {
                    AppConstants.roundTripSource= ResponseParser.parseIntoCityList(response);
                    DialogUtil.showCityListDialog(getActivity(),AppConstants.roundTripSource,mRoundTripFromCity);
                }

                @Override
                public void onServiceResultFailure(String url, String error) {
                    DialogUtil.showServerErrorDialog(getActivity());
                }
            });
        }
        else {
            DialogUtil.showCityListDialog(getActivity(), AppConstants.roundTripSource, mRoundTripFromCity);
        }
    }

    private void  callCityListForRoundTripDestination()
    {
        if (mRoundTripFromCity.getTag()!=null) {
            ServiceUtil.callCityListServies(getActivity(), CreateJsonFor.roundTrip(mRoundTripFromCity.getTag().toString()), new BaseAsyncTask.ServiceCallable() {
                @Override
                public void onServiceResultSuccess(String url, boolean success, String message, String response) {
                    List<City> destinationList = ResponseParser.parseIntoCityList(response);
                    DialogUtil.showCityListDialog(getActivity(), destinationList, mRoundTripToCity);
                }

                @Override
                public void onServiceResultFailure(String url, String error) {
                    DialogUtil.showServerErrorDialog(getActivity());
                }
            });
        }
        else {
            DialogUtil.showOk(getActivity(), "Select source city first");
        }
    }




    private void  callCityListForMultiCityTripSource()
    {
        if (AppConstants.multiCitySource==null || AppConstants.multiCitySource.size()==0) {
            ServiceUtil.callCityListServies(getActivity(), CreateJsonFor.roundTrip(""), new BaseAsyncTask.ServiceCallable() {
                @Override
                public void onServiceResultSuccess(String url, boolean success, String message, String response) {
                    AppConstants.multiCitySource= ResponseParser.parseIntoCityList(response);
                    DialogUtil.showCityListDialog(getActivity(),AppConstants.multiCitySource,mMulticityFromCtity);
                }

                @Override
                public void onServiceResultFailure(String url, String error) {
                    DialogUtil.showServerErrorDialog(getActivity());
                }
            });
        }
        else {
            DialogUtil.showCityListDialog(getActivity(), AppConstants.multiCitySource, mMulticityFromCtity);
        }
    }

    private void removeAllMoreCities(){
        for (int i=0;i<mMulticityAddMoreViewLL.getChildCount();i++){
            mRemovecity.performClick();
        }
    }


    private void  callCityListForMultiCityDestination(TextView sourceText,final TextView destinationText, final boolean addmore)
    {
        if (sourceText.getTag()!=null) {
            ServiceUtil.callCityListServies(getActivity(), CreateJsonFor.roundTrip(sourceText.getTag().toString()), new BaseAsyncTask.ServiceCallable() {
                @Override
                public void onServiceResultSuccess(String url, boolean success, String message, String response) {
                    List<City> destinationList = ResponseParser.parseIntoCityList(response);
                    if(!addmore) {
                        DialogUtil.showCityListDialog(getActivity(), destinationList, destinationText);
                        removeAllMoreCities();
                    }
                    else
                        DialogUtil.showCityListDialogAndAddmore(getActivity(), destinationList, destinationText, OutStationTravelFragment.this);
                }

                @Override
                public void onServiceResultFailure(String url, String error) {
                    DialogUtil.showServerErrorDialog(getActivity());
                }
            });
        }
        else {
            DialogUtil.showOk(getActivity(), "Select source city first");
        }
    }



    private void setClickListnerOnDropDown(final TextView textView, final String dropdownType) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dropdownType.equalsIgnoreCase(DATE)) {
                    DialogUtil.showDatePicker(getActivity(), textView);
                } else if (dropdownType.equalsIgnoreCase(TIME)) {
                    DialogUtil.showTimePickerDialog(getActivity(), textView);
                }
            }
        });
    }


    private boolean isValidRoundTrip() {
        Resources resources = getResources();
        if (mRoundTripFromCity.getText().toString().equalsIgnoreCase(resources.getString(R.string.fromcity))) {
            AppUtil.showToastMessage(resources.getString(R.string.err_fromcity));
            return false;
        } else if (mRoundTripToCity.getText().toString().equalsIgnoreCase(resources.getString(R.string.tocity))) {
            AppUtil.showToastMessage(resources.getString(R.string.err_tocity));
            return false;
        } else if (mRoundTripTravelDate.getText().toString().equalsIgnoreCase(resources.getString(R.string.traveldate))) {
            AppUtil.showToastMessage(resources.getString(R.string.err_traveldate));
            return false;
        } else if (mRoundTripTravelTime.getText().toString().equalsIgnoreCase(resources.getString(R.string.traveltime))) {
            AppUtil.showToastMessage(resources.getString(R.string.err_traveltime));
            return false;
        } else if (mRoundTripTravelDuration.getText().toString().length() <= 0) {
            AppUtil.showToastMessage(resources.getString(R.string.err_duration));
            return false;
        }
        return true;
    }

    private boolean isValidMultiCity() {

        Resources resources = getResources();
        if (mMulticityFromCtity.getText().toString().equalsIgnoreCase(resources.getString(R.string.fromcity))) {
            AppUtil.showToastMessage(resources.getString(R.string.err_fromcity));
            return false;
        } else if (mMulticityToCity.getText().toString().equalsIgnoreCase(resources.getString(R.string.tocity))) {
            AppUtil.showToastMessage(resources.getString(R.string.err_tocity));
            return false;
        } else if (mMulticityTravelDate.getText().toString().equalsIgnoreCase(resources.getString(R.string.traveldate))) {
            AppUtil.showToastMessage(resources.getString(R.string.err_traveldate));
            return false;
        } else if (mMulticityTravelTime.getText().toString().equalsIgnoreCase(resources.getString(R.string.traveltime))) {
            AppUtil.showToastMessage(resources.getString(R.string.err_traveltime));
            return false;
        } else if (mMulticityTravelDuration.getText().toString().length() <= 0) {
            AppUtil.showToastMessage(resources.getString(R.string.err_duration));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == getMyCabButton.getId()) {
            boolean isvalid = false;
            if (mRadioGroup.getCheckedRadioButtonId() == mRoundtripRadio.getId())
                isvalid = isValidRoundTrip();
            else if (mRadioGroup.getCheckedRadioButtonId() == mMulticityRadio.getId())
                isvalid = isValidMultiCity();

            if (isvalid) {
                //Call webservice
                callWebService();

            }
        } else if (v.getId() == mMulticityAddMore.getId()) {
            addmoreCity();
        } else if (v.getId() == mRemovecity.getId()) {
            removeCity();
        }

        //New

        else if (v.getId()==mRoundTripFromCity.getId()){
            callCityListForRoundTripSource();
        }
        else if (v.getId()==mRoundTripToCity.getId()){
            callCityListForRoundTripDestination();
        }
        else if (v.getId()==mMulticityFromCtity.getId()){
            callCityListForMultiCityTripSource();
        }
        else if (v.getId()==mMulticityToCity.getId()){
            callCityListForMultiCityDestination(mMulticityFromCtity,mMulticityToCity,false);
        }


    }

    public void addmoreCity() {
        if (mMulticityToCity.getTag()==null) {
            AppUtil.showToastMessage("Select destination city first.");
            return;
        }

        try {
            View v = null;
            if (mMulticityAddMoreViewLL.getChildCount() > 0)
                v = mMulticityAddMoreViewLL.getChildAt(mMulticityAddMoreViewLL.getChildCount() - 1);

            String fromcity = "";
            String fromCityTag = "";
            if (v != null) {
                fromcity = ((TextView) v.findViewById(R.id.addmore_tocity)).getText().toString();
                fromCityTag = ((TextView) v.findViewById(R.id.addmore_tocity)).getTag().toString();
                if (fromCityTag.equalsIgnoreCase(mMulticityFromCtity.getTag().toString()))
                    return;
            } else {
                fromcity = mMulticityToCity.getText().toString();
                fromCityTag = mMulticityToCity.getTag().toString();
            }

            View view = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_from_to, null);
            final TextView fromCity = (TextView) view.findViewById(R.id.addmore_fromcity);
            final TextView toCity = (TextView) view.findViewById(R.id.addmore_tocity);
            fromCity.setText(fromcity);
            fromCity.setTag(fromCityTag);

            toCity.setText(mMulticityFromCtity.getText().toString());
            toCity.setTag(mMulticityFromCtity.getTag().toString());


            toCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callCityListForMultiCityDestination(fromCity, toCity,true);
                }
            });


            mMulticityAddMoreViewLL.addView(view, mMulticityAddMoreViewLL.getChildCount());
            mRemovecity.setVisibility(View.VISIBLE);
            mScrollView.post(new Runnable() {
                @Override
                public void run() {
                    mScrollView.smoothScrollBy(0,1000);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }


    private void removeCity() {
        if (mMulticityAddMoreViewLL.getChildAt(0) != null) {
            mMulticityAddMoreViewLL.removeViewAt(mMulticityAddMoreViewLL.getChildCount() - 1);

            //set to city in previous list
            try {
                View v = null;
                if (mMulticityAddMoreViewLL.getChildCount() > 0)
                    v = mMulticityAddMoreViewLL.getChildAt(mMulticityAddMoreViewLL.getChildCount() - 1);

                if (v != null) {
                    TextView tocity = (TextView) v.findViewById(R.id.addmore_tocity);
                    tocity.setText(mMulticityFromCtity.getText().toString());
                    tocity.setTag(mMulticityFromCtity.getTag().toString());
                }
           }
            catch (Exception e){
                e.printStackTrace();
            }



            }
        if (mMulticityAddMoreViewLL.getChildCount() <= 0) {

            mRemovecity.setVisibility(View.GONE);
        }
    }


    private void callWebService() {
        new BaseAsyncTask(getActivity(), true, this, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.SEARCH_CAB, getJsonParams()).execute();
    }

    private JSONObject getJsonParams() {
        jsonObject = new JSONObject();
        try {
            jsonObject.put("tourType", "Outstation");
            if (mRadioGroup.getCheckedRadioButtonId() == mRoundtripRadio.getId()) {
                jsonObject.put("travel_type", "Roundtrip");
                jsonObject.put("from_city", mRoundTripFromCity.getText().toString());
                jsonObject.put("to_city", mRoundTripToCity.getText().toString());
                jsonObject.put("date", mRoundTripTravelDate.getTag().toString());
                jsonObject.put("time", mRoundTripTravelTime.getText().toString());
                jsonObject.put("duration", mRoundTripTravelDuration.getText().toString());
                jsonObject.put("moreCites", "");


            } else if (mRadioGroup.getCheckedRadioButtonId() == mMulticityRadio.getId()) {
                jsonObject.put("travel_type", "Multicity");
                jsonObject.put("from_city", mMulticityFromCtity.getText().toString());
                jsonObject.put("to_city", mMulticityToCity.getText().toString());
                jsonObject.put("date", mMulticityTravelDate.getTag().toString());
                jsonObject.put("time", mMulticityTravelTime.getText().toString());
                String morcities = "";
                for (int i = 0; i < mMulticityAddMoreViewLL.getChildCount(); i++) {
                    View v = mMulticityAddMoreViewLL.getChildAt(i);
                    String from=((TextView)v.findViewById(R.id.addmore_fromcity)).getText().toString();
                    String tocity = ((TextView) v.findViewById(R.id.addmore_tocity)).getText().toString();
                    morcities = morcities + tocity + ",";

                }

                if(morcities.length()<=0){
                    morcities=mMulticityFromCtity.getText().toString()+","+mMulticityToCity.getText().toString();
                }
                else {
                    morcities=mMulticityFromCtity.getText().toString()
                            +","+mMulticityToCity.getText().toString()+","+
                            morcities/*+mMulticityFromCtity.getText().toString()*/;
                    morcities=morcities.substring(0,morcities.length()-1);
                }

                jsonObject.put("moreCites", morcities);

                jsonObject.put("duration", mMulticityTravelDuration.getText().toString());
                searchedMoreCity=morcities;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    @Override
    public void onServiceResultSuccess(String url, boolean success, String message, String response) {
        //if (success){
        if (url.equalsIgnoreCase(ApiServiceUrl.SEARCH_CAB)) {
            if (response.length() > 0) {
                List<Car> list = ResponseParser.parseIntoCarList(response);
                if (list == null || list.size()==0) {
                    DialogUtil.showOk(getActivity(),getString(R.string.no_result_for_cab));
                } else {

                    saveDateTime();
                    AppUtil.savePreference(AppConstants.CAB_SEARCH_RESULT_JSON, response);
                    AppUtil.savePreference(AppConstants.BOOKING_QUERY_JSON,jsonObject.toString());
                    startActivity(new Intent(getActivity(), CabSearchResultActivity.class));
                }
            }
        }

    }

    @Override
    public void onServiceResultFailure(String url, String error) {
        DialogUtil.showServerErrorDialog(getActivity());
    }

    private void saveDateTime() {

        if (mRoundtripRadio.isChecked()) {
            AppUtil.savePreference(AppConstants.CAB_SEARCH_DATE, mRoundTripTravelDate.getTag().toString());
            AppUtil.savePreference(AppConstants.CAB_SEARCH_TIME, mRoundTripTravelTime.getText().toString());
            int d = 0;
            try {
                d = Integer.parseInt(mRoundTripTravelDuration.getText().toString().trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
            AppUtil.savePreference(AppConstants.CAB_SEARCH_DURATION, d);
            AppUtil.savePreference(AppConstants.CAB_SEARCH_FROM_TO, "Outstation, " + mRoundTripFromCity.getText().toString() + "->" + mRoundTripToCity.getText().toString());
        } else {
            AppUtil.savePreference(AppConstants.CAB_SEARCH_DATE, mMulticityTravelDate.getTag().toString());
            AppUtil.savePreference(AppConstants.CAB_SEARCH_TIME, mMulticityTravelTime.getText().toString());
            int d = 0;
            try {
                d = Integer.parseInt(mMulticityTravelDuration.getText().toString().trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
            AppUtil.savePreference(AppConstants.CAB_SEARCH_DURATION, d);

            searchedMoreCity= searchedMoreCity.replace(",", "->");
            AppUtil.savePreference(AppConstants.CAB_SEARCH_FROM_TO, "Outstation, " + /*mMulticityFromCtity.getText().toString() + " - " + mMulticityToCity.getText().toString()*/searchedMoreCity);
            searchedMoreCity="";
        }

    }
}
