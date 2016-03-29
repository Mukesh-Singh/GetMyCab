package gmc.com.getmycab.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import gmc.com.getmycab.activity.CabSearchResultActivity;
import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppConstants;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.Utils.DialogUtil;
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
public class OneWayTravelFragment extends Fragment implements View.OnClickListener, BaseAsyncTask.ServiceCallable {
    private TextView mRoundTripFromCity, mRoundTripToCity, mRoundTripTravelDate, mRoundTripTravelTime;
    EditText mRoundTripTravelDuration;
    Button mGetmycab;
    private JSONObject jsonObject;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_way_travel, container, false);
        initUi(view);
        return view;
    }

    private void initUi(View view) {
        mRoundTripFromCity = (TextView) view.findViewById(R.id.one_way_travel_fromcity);
        mRoundTripToCity = (TextView) view.findViewById(R.id.one_way_travel_tocity);
        mRoundTripTravelDate = (TextView) view.findViewById(R.id.one_way_travel_date);
        mRoundTripTravelTime = (TextView) view.findViewById(R.id.one_way_travel_time);
        mRoundTripTravelDuration = (EditText) view.findViewById(R.id.one_way_travel_duration);
        mGetmycab = (Button) view.findViewById(R.id.one_way_travel_getmecab);
        mGetmycab.setOnClickListener(this);
        mRoundTripTravelDuration.setVisibility(View.GONE);

        mRoundTripFromCity.setOnClickListener(this);
        mRoundTripToCity.setOnClickListener(this);
        mRoundTripTravelDate.setOnClickListener(this);
        mRoundTripTravelTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mGetmycab.getId()) {
            if (isValid()) {
                callWebservice();

            }
        } else if (v.getId() == mRoundTripFromCity.getId()) {
            callCityListForSource();

        } else if (v.getId() == mRoundTripToCity.getId()) {
            callCityListForDestination();
        } else if (v.getId() == mRoundTripTravelDate.getId()) {
            DialogUtil.showDatePicker(getActivity(), mRoundTripTravelDate);
        } else if (v.getId() == mRoundTripTravelTime.getId()) {
            DialogUtil.showTimePickerDialog(getActivity(), mRoundTripTravelTime);
        }
    }

    private void callCityListForSource() {
        if (AppConstants.oneWaySource == null || AppConstants.oneWaySource.size() == 0) {
            ServiceUtil.callCityListServies(getActivity(), CreateJsonFor.oneWaySource(""), new BaseAsyncTask.ServiceCallable() {
                @Override
                public void onServiceResultSuccess(String url, boolean success, String message, String response) {
                    AppConstants.oneWaySource = ResponseParser.parseIntoCityList(response);
                    DialogUtil.showCityListDialog(getActivity(), AppConstants.oneWaySource, mRoundTripFromCity);
                }

                @Override
                public void onServiceResultFailure(String url, String error) {
                    DialogUtil.showServerErrorDialog(getActivity());
                }
            });
        } else {
            DialogUtil.showCityListDialog(getActivity(), AppConstants.oneWaySource, mRoundTripFromCity);
        }
    }

    private void callCityListForDestination() {
        if (mRoundTripFromCity.getTag() != null) {
            ServiceUtil.callCityListServies(getActivity(), CreateJsonFor.oneWaySource(mRoundTripFromCity.getTag().toString()), new BaseAsyncTask.ServiceCallable() {
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
        } else {
            DialogUtil.showOk(getActivity(), "Select source city first");
        }
    }


    private boolean isValid() {
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
        }

        return true;
    }


    private void callWebservice() {
        jsonObject = new JSONObject();
        try {

            jsonObject.put("travel_type", "Oneway");
            jsonObject.put("tourType", "Outstation");
            jsonObject.put("from_city", mRoundTripFromCity.getText().toString());
            jsonObject.put("to_city", mRoundTripToCity.getText().toString());
            jsonObject.put("date", mRoundTripTravelDate.getTag().toString());
            jsonObject.put("time", mRoundTripTravelTime.getText().toString());
            jsonObject.put("duration", "1");
            jsonObject.put("moreCites", "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        new BaseAsyncTask(getActivity(), true, this, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.SEARCH_CAB, jsonObject).execute();

    }


    @Override
    public void onServiceResultSuccess(String url, boolean success, String message, String response) {
        //if (success){
        if (url.equalsIgnoreCase(ApiServiceUrl.SEARCH_CAB)) {
            if (response.length() > 0) {
                List<Car> list = ResponseParser.parseIntoCarList(response);
                if (list == null|| list.size()==0) {
                    DialogUtil.showOk(getActivity(),getString(R.string.no_result_for_cab));
                } else {

                    AppUtil.savePreference(AppConstants.CAB_SEARCH_DATE, mRoundTripTravelDate.getTag().toString());
                    AppUtil.savePreference(AppConstants.CAB_SEARCH_TIME, mRoundTripTravelTime.getText().toString());
                    AppUtil.savePreference(AppConstants.CAB_SEARCH_DURATION, 1);
                    AppUtil.savePreference(AppConstants.CAB_SEARCH_FROM_TO, "Oneway, " + mRoundTripFromCity.getText().toString() + " - " + mRoundTripToCity.getText().toString());
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
}
