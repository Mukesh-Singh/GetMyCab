package gmc.com.getmycab.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
public class OutStationTravelFragment extends Fragment implements View.OnClickListener, BaseAsyncTask.ServiceCallable {
    private RadioGroup mRadioGroup;
    private RadioButton mRoundtripRadio, mMulticityRadio;
    private Button getMyCabButton;

    private AutoCompleteTextView mRoundTripFromCity, mRoundTripToCity;
    private TextView  mRoundTripTravelDate, mRoundTripTravelTime;
    EditText mRoundTripTravelDuration;


    private AutoCompleteTextView mMulticityFromCtity, mMulticityToCity;
    private TextView  mMulticityTravelDate, mMulticityTravelTime;
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


    private ProgressBar mRoundTripDestiProgress,mMultiDestProgress;

    //for round trip
    private City mSelectedSourceCityForRound, mSelectedDestinatinCityForRound;
    private ArrayAdapter<City> destAdapterForRound;
    private List<City> mDestinationCityListRound = new ArrayList<>();


    private City mSelectedSourceCityMultiCity, mSelectedDestinatinCityMultiCity;
    private ArrayAdapter<City> destAdapterMultiCity;
    private List<City> mDestinationCityListMultiCity = new ArrayList<>();



//    private ArrayAdapter<City> destAdapter;
//    private List<City> mDestinationCityList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.out_station_travel, container, false);
        initUi(view);
        return view;
    }


    private City getCityObjectFor(List<City> list,String cityName) {
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

//    private City getCityObjectForMulti(List<City> list,String cityName) {
//        if (list==null || list.size()<0)
//            return null;
//
//        City city = new City();
//        city.setCityName(cityName);
//        if(AppConstants.multiCitySource.contains(city)) {
//            int index=AppConstants.multiCitySource.indexOf(city);
//            if (index>=0) {
//                return list.get(index);
//            }
//        }
//
//        return null;
//    }




    private void initUi(View view) {
        mRadioGroup = (RadioGroup) view.findViewById(R.id.out_station_radioGroup);
        mRoundtripRadio = (RadioButton) view.findViewById(R.id.out_station_round_trip_radio);
        mMulticityRadio = (RadioButton) view.findViewById(R.id.out_station_multicity_radio);
        mRoundTripFromCity = (AutoCompleteTextView) view.findViewById(R.id.one_way_travel_fromcity);
        //setClickListnerOnDropDown(mRoundTripFromCity, CITY);
        //mRoundTripFromCity.setOnClickListener(this);

        mRoundTripToCity = (AutoCompleteTextView) view.findViewById(R.id.one_way_travel_tocity);
        //setClickListnerOnDropDown(mRoundTripToCity, CITY);
        //mRoundTripToCity.setOnClickListener(this);

        ArrayAdapter<City> adapter =
                new ArrayAdapter<City>(getActivity(), android.R.layout.simple_list_item_1, AppConstants.roundTripSource);
        mRoundTripFromCity.setAdapter(adapter);

        mRoundTripFromCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedSourceCityForRound = getCityObjectFor(AppConstants.roundTripSource,adapterView.getItemAtPosition(i).toString());
                callCityListForRoundTripDestination();
            }
        });

        mRoundTripTravelDate = (TextView) view.findViewById(R.id.one_way_travel_date);
        setClickListnerOnDropDown(mRoundTripTravelDate, DATE);
        mRoundTripTravelTime = (TextView) view.findViewById(R.id.one_way_travel_time);
        setClickListnerOnDropDown(mRoundTripTravelTime, TIME);
        mRoundTripTravelDuration = (EditText) view.findViewById(R.id.one_way_travel_duration);
        mRoundTripTravelDuration.setVisibility(View.VISIBLE);

        mMulticityFromCtity = (AutoCompleteTextView) view.findViewById(R.id.multicity_trip_fromcity);

        //mMulticityFromCtity.setOnClickListener(this);


        ArrayAdapter<City> adapterMulti =
                new ArrayAdapter<City>(getActivity(), android.R.layout.simple_list_item_1, AppConstants.multiCitySource);
        mMulticityFromCtity.setAdapter(adapterMulti);

        mMulticityFromCtity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedSourceCityMultiCity = getCityObjectFor(AppConstants.multiCitySource,adapterView.getItemAtPosition(i).toString());
                mMulticityFromCtity.setTag(mSelectedSourceCityMultiCity.getCitycod());
                callCityListForMultiCityTripDestination();
            }
        });

        mMulticityToCity = (AutoCompleteTextView) view.findViewById(R.id.multicity_trip_tocity);

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


        mRoundTripDestiProgress= (ProgressBar) view.findViewById(R.id.one_way_travel_to_city_progress);
        mMultiDestProgress= (ProgressBar) view.findViewById(R.id.multicity_trip_travel_to_city_progress);



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

    private void setDestinationAdapterForRound(final List<City>destList){
        mDestinationCityListRound.clear();
        if (destList!=null && destList.size()>0) {
            mDestinationCityListRound.addAll(destList);
        }
        else
            return;

            destAdapterForRound = new ArrayAdapter<City>(getActivity(), android.R.layout.simple_list_item_1, mDestinationCityListRound);
            mRoundTripToCity.setAdapter(destAdapterForRound);
            mRoundTripToCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mSelectedDestinatinCityForRound = mDestinationCityListRound.get(i);
                }
            });



    }


    private void setDestinationAdapterForMultiCity(final List<City>destList){
        mDestinationCityListMultiCity.clear();
        if (destList!=null && destList.size()>0) {
            mDestinationCityListMultiCity.addAll(destList);
        }
        else
            return;

            destAdapterMultiCity = new ArrayAdapter<City>(getActivity(), android.R.layout.simple_list_item_1, mDestinationCityListMultiCity);
            mMulticityToCity.setAdapter(destAdapterMultiCity);
            mMulticityToCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mSelectedDestinatinCityMultiCity = getCityObjectFor(destList,adapterView.getItemAtPosition(i).toString());
                    mMulticityToCity.setTag(mSelectedDestinatinCityMultiCity.getCitycod());
                    removeAllMoreCities();
                    //addmoreCity();

                }
            });



    }

    private void setDestinationAdapterForMultiCityAddMore( final List<City> destinationList,final AutoCompleteTextView textView){
        ArrayAdapter destAdapterMultiCity = new ArrayAdapter<City>(getActivity(), android.R.layout.simple_list_item_1, destinationList);
        textView.setAdapter(destAdapterMultiCity);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    City temp = getCityObjectFor(destinationList,adapterView.getItemAtPosition(i).toString());
                    textView.setTag(temp);
                    addmoreCity();

                }
            });


    }





    private void  callCityListForRoundTripDestination()
    {
            mRoundTripDestiProgress.setVisibility(View.VISIBLE);
            ServiceUtil.callCityListServies(getActivity(),false, CreateJsonFor.roundTrip(mSelectedSourceCityForRound.getCitycod()), new BaseAsyncTask.ServiceCallable() {
                @Override
                public void onServiceResultSuccess(String url, boolean success, String message, String response) {
                    List<City> destinationList = ResponseParser.parseIntoCityList(response);
                    mRoundTripDestiProgress.setVisibility(View.GONE);
                    setDestinationAdapterForRound(destinationList);
                }

                @Override
                public void onServiceResultFailure(String url, String error) {
                    DialogUtil.showServerErrorDialog(getActivity());
                    mRoundTripDestiProgress.setVisibility(View.GONE);
                }
            });

    }



    private void  callCityListForMultiCityTripDestination()
    {
        //if (mRoundTripFromCity.getTag()!=null) {
        mMultiDestProgress.setVisibility(View.VISIBLE);
        ServiceUtil.callCityListServies(getActivity(),false, CreateJsonFor.multiCity(mSelectedSourceCityMultiCity.getCitycod()), new BaseAsyncTask.ServiceCallable() {
            @Override
            public void onServiceResultSuccess(String url, boolean success, String message, String response) {
                List<City> destinationList = ResponseParser.parseIntoCityList(response);
                //DialogUtil.showCityListDialog(getActivity(), destinationList, mRoundTripToCity);
                mMultiDestProgress.setVisibility(View.GONE);
                setDestinationAdapterForMultiCity(destinationList);

            }

            @Override
            public void onServiceResultFailure(String url, String error) {
                DialogUtil.showServerErrorDialog(getActivity());
                mMultiDestProgress.setVisibility(View.GONE);
            }
        });

    }




    private void removeAllMoreCities(){
        for (int i=mMulticityAddMoreViewLL.getChildCount()-1;i>=0;i--){
            //mRemovecity.performClick();
            mMulticityAddMoreViewLL.removeViewAt(i);
        }
    }


    private void  callCityListForMultiCityDestination(TextView sourceText,final AutoCompleteTextView destinationText, final boolean addmore,final ProgressBar progressBar)
    {
        if (sourceText.getTag()!=null) {
            progressBar.setVisibility(View.VISIBLE);
            ServiceUtil.callCityListServies(getActivity(), CreateJsonFor.multiCity(sourceText.getTag().toString()), new BaseAsyncTask.ServiceCallable() {
                @Override
                public void onServiceResultSuccess(String url, boolean success, String message, String response) {
                    List<City> destinationList = ResponseParser.parseIntoCityList(response);
                    progressBar.setVisibility(View.GONE);
                    if(addmore) {
                        //DialogUtil.showCityListDialog(getActivity(), destinationList, destinationText);
                        //removeAllMoreCities();
                        setDestinationAdapterForMultiCityAddMore(destinationList,destinationText);
                    }
//                    else
//                        DialogUtil.showCityListDialogAndAddmore(getActivity(), destinationList, destinationText, OutStationTravelFragment.this);
                }

                @Override
                public void onServiceResultFailure(String url, String error) {
                    progressBar.setVisibility(View.GONE);
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
        if (mRoundTripFromCity.getText().toString().length()<=0) {
            AppUtil.showToastMessage(resources.getString(R.string.err_fromcity));
            return false;
        } else if (mRoundTripToCity.getText().toString().length()<=0) {
            AppUtil.showToastMessage(resources.getString(R.string.err_tocity));
            return false;
        } else if (mRoundTripTravelDate.getText().toString().length()<=0) {
            AppUtil.showToastMessage(resources.getString(R.string.err_traveldate));
            return false;
        } else if (mRoundTripTravelTime.getText().toString().length()<=0) {
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
        if (mMulticityFromCtity.getText().toString().length()<=0) {
            AppUtil.showToastMessage(resources.getString(R.string.err_fromcity));
            return false;
        } else if (mMulticityToCity.getText().toString().length()<=0) {
            AppUtil.showToastMessage(resources.getString(R.string.err_tocity));
            return false;
        } else if (mMulticityTravelDate.getText().toString().length()<=0) {
            AppUtil.showToastMessage(resources.getString(R.string.err_traveldate));
            return false;
        } else if (mMulticityTravelTime.getText().toString().length()<=0) {
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
            //callCityListForRoundTripSource();
        }
        else if (v.getId()==mRoundTripToCity.getId()){
            //callCityListForRoundTripDestination();
        }
        else if (v.getId()==mMulticityFromCtity.getId()){
            //callCityListForMultiCityTripSource();
        }
        else if (v.getId()==mMulticityToCity.getId()){
            //callCityListForMultiCityDestination(mMulticityFromCtity,mMulticityToCity,false);
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
            final AutoCompleteTextView toCity = (AutoCompleteTextView) view.findViewById(R.id.addmore_tocity);
            final ProgressBar progressBar=(ProgressBar)view.findViewById(R.id.addmore_progress);
            fromCity.setText(fromcity);
            fromCity.setTag(fromCityTag);

            toCity.setText(mMulticityFromCtity.getText().toString());
            toCity.setTag(mMulticityFromCtity.getTag().toString());
            callCityListForMultiCityDestination(fromCity, toCity,true,progressBar);


//            toCity.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    callCityListForMultiCityDestination(fromCity, toCity,true);
//                }
//            });


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
                    Intent outstation=new Intent(getActivity(), CabSearchResultActivity.class);
                    outstation.putExtra(Constants.EXTRA_IS_ONE_WAY,false);
                    startActivity(outstation);
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
