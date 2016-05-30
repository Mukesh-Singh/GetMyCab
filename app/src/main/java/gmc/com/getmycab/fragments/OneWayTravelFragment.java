package gmc.com.getmycab.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppConstants;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.Utils.DialogUtil;
import gmc.com.getmycab.activity.CabSearchResultActivity;
import gmc.com.getmycab.asyntask.BaseAsyncTask;
import gmc.com.getmycab.bean.Car;
import gmc.com.getmycab.bean.City;
import gmc.com.getmycab.ccavenue.utility.Constants;
import gmc.com.getmycab.networkapi.ApiCaller;
import gmc.com.getmycab.networkapi.ApiServiceUrl;
import gmc.com.getmycab.networkapi.CreateJsonFor;
import gmc.com.getmycab.networkapi.ServiceUtil;
import gmc.com.getmycab.parser.ResponseParser;

/**
 * Created by Baba on 9/5/2015.
 */
public class OneWayTravelFragment extends Fragment implements View.OnClickListener, BaseAsyncTask.ServiceCallable {
    private AutoCompleteTextView mRoundTripFromCity, mRoundTripToCity;
    private TextView mRoundTripTravelDate, mRoundTripTravelTime;
    EditText mRoundTripTravelDuration;
    Button mGetmycab;
    private JSONObject jsonObject;
    private ProgressBar toCityProgress;

    private City mSelectedSourceCity, mSelectedDestinatinCity;
    private ArrayAdapter<City> destAdapter;
    private List<City> mDestinationCityList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_way_travel, container, false);
        initUi(view);
        return view;
    }

    private void initUi(View view) {
        mRoundTripFromCity = (AutoCompleteTextView) view.findViewById(R.id.one_way_travel_fromcity);
        mRoundTripToCity = (AutoCompleteTextView) view.findViewById(R.id.one_way_travel_tocity);

        mRoundTripTravelDate = (TextView) view.findViewById(R.id.one_way_travel_date);
        mRoundTripTravelTime = (TextView) view.findViewById(R.id.one_way_travel_time);
        mRoundTripTravelDuration = (EditText) view.findViewById(R.id.one_way_travel_duration);
        mGetmycab = (Button) view.findViewById(R.id.one_way_travel_getmecab);
        mGetmycab.setOnClickListener(this);
        mRoundTripTravelDuration.setVisibility(View.GONE);


        toCityProgress = (ProgressBar) view.findViewById(R.id.one_way_travel_to_city_progress);
        toCityProgress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.MULTIPLY);

        //mRoundTripFromCity.setOnClickListener(this);
        //mRoundTripToCity.setOnClickListener(this);

        mRoundTripTravelDate.setOnClickListener(this);
        mRoundTripTravelTime.setOnClickListener(this);

        //test here


        ArrayAdapter<City> adapter =
                new ArrayAdapter<City>(getActivity(), android.R.layout.simple_list_item_1, AppConstants.oneWaySource);
        mRoundTripFromCity.setAdapter(adapter);

        mRoundTripFromCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedSourceCity = getCityObject(AppConstants.oneWaySource,adapterView.getItemAtPosition(i).toString());
                callCityListForDestination();
            }
        });
//        mRoundTripFromCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    callCityListForDestination();
//                }
//            }
//        });


    }

    //sample <code>
    // </code>

    private City getCityObject(List<City> list,String cityName) {
        if (list==null || list.size()<0)
            return null;

            City city = new City();
            city.setCityName(cityName);
            if(list.contains(city)) {
                int index=list.indexOf(city);
                if (index>=0) {
                    return list.get(index);
                }
            }

        return null;
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == mGetmycab.getId()) {
            if (isValid()) {
                callWebservice();

            }

        } else if (v.getId() == mRoundTripFromCity.getId()) {
            callCityListForSource();

        }
//        else if (v.getId() == mRoundTripToCity.getId()) {
//            callCityListForDestination();
//        }
        else if (v.getId() == mRoundTripTravelDate.getId()) {
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

    private void setDestinationAdapter(final List<City>destList){
        mDestinationCityList.clear();
        if (destList!=null && destList.size()>0) {
            mDestinationCityList.addAll(destList);
        }
        else
            return;

                destAdapter = new ArrayAdapter<City>(getActivity(), android.R.layout.simple_list_item_1, mDestinationCityList);
                mRoundTripToCity.setAdapter(destAdapter);
                mRoundTripToCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        mSelectedDestinatinCity = mDestinationCityList.get(i);
                        mSelectedDestinatinCity= getCityObject(destList,adapterView.getItemAtPosition(i).toString());
                    }
                });



    }





    private void callCityListForDestination() {
        //if (mRoundTripFromCity.getTag() != null) {
        toCityProgress.setVisibility(View.VISIBLE);
            ServiceUtil.callCityListServies(getActivity(),false, CreateJsonFor.oneWaySource(mSelectedSourceCity.getCitycod()), new BaseAsyncTask.ServiceCallable() {
                @Override
                public void onServiceResultSuccess(String url, boolean success, String message, String response) {
                    List<City> destinationList = ResponseParser.parseIntoCityList(response);
                    //DialogUtil.showCityListDialog(getActivity(), destinationList, mRoundTripToCity);
                    toCityProgress.setVisibility(View.GONE);
                    setDestinationAdapter(destinationList);

                }

                @Override
                public void onServiceResultFailure(String url, String error) {
                    DialogUtil.showServerErrorDialog(getActivity());
                    toCityProgress.setVisibility(View.GONE);
                }
            });
//        } else {
//            DialogUtil.showOk(getActivity(), "Select source city first");
//        }
    }


    private boolean isValid() {
        Resources resources = getResources();
        if (mRoundTripFromCity.getText().length()<=0) {
            AppUtil.showToastMessage(resources.getString(R.string.err_fromcity));
            return false;
        } else if (mRoundTripToCity.getText().length()<=0) {
            AppUtil.showToastMessage(resources.getString(R.string.err_tocity));
            return false;
        } else if (mRoundTripTravelDate.getText().length()<= 0) {
            AppUtil.showToastMessage(resources.getString(R.string.err_traveldate));
            return false;
        } else if (mRoundTripTravelTime.getText().length()<= 0) {
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
                    Intent onewayIntent=new Intent(getActivity(), CabSearchResultActivity.class);
                    onewayIntent.putExtra(Constants.EXTRA_IS_ONE_WAY,true);
                    startActivity(onewayIntent);
                }
            }
        }

    }

    @Override
    public void onServiceResultFailure(String url, String error) {
        DialogUtil.showServerErrorDialog(getActivity());
    }
}
