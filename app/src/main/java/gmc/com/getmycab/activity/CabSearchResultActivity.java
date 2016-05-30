package gmc.com.getmycab.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import java.util.List;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppConstants;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.Utils.DialogUtil;
import gmc.com.getmycab.adapters.CarListAdapter;
import gmc.com.getmycab.base.BaseWtihBackButtonAndLogo;
import gmc.com.getmycab.bean.Car;
import gmc.com.getmycab.ccavenue.utility.Constants;
import gmc.com.getmycab.parser.ResponseParser;

/**
 * Created by Baba on 9/5/2015.
 */
public class CabSearchResultActivity extends BaseWtihBackButtonAndLogo{

    ListView searchedList;
    List<Car> list;
    boolean isOneWay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_activity);
        isOneWay=getIntent().getBooleanExtra(Constants.EXTRA_IS_ONE_WAY,false);
        //setTitle("CAB OPTION");
        enableHeaderBackButton();
        enableCallButton();
        list= ResponseParser.parseIntoCarList(AppUtil.getPreference(AppConstants.CAB_SEARCH_RESULT_JSON));
        searchedList=(ListView)findViewById(R.id.search_result_activity_list);
        searchedList.setSmoothScrollbarEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CarListAdapter adapter= new CarListAdapter(this,list,isOneWay);
        searchedList.setAdapter(adapter);
        if (list==null || list.size()==0)
            DialogUtil.showOk(this,getString(R.string.no_result_for_cab));
    }
    public void scrollList(){
        //searchedList.scrollListBy(1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                searchedList.smoothScrollToPosition(list.size()-1);
            }
        },200);


    }
}
