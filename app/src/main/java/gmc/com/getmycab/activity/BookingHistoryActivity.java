package gmc.com.getmycab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppConstants;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.Utils.DialogUtil;
import gmc.com.getmycab.adapters.BookingHistoryAdapter;
import gmc.com.getmycab.asyntask.BaseAsyncTask;
import gmc.com.getmycab.base.BaseActivityWithBackHeader;
import gmc.com.getmycab.bean.BookingBean;
import gmc.com.getmycab.bean.BookingHistoryBean;
import gmc.com.getmycab.networkapi.ApiCaller;
import gmc.com.getmycab.networkapi.ApiServiceUrl;
import gmc.com.getmycab.parser.ResponseParser;

/**
 * Created by Baba on 9/12/2015.
 */
public class BookingHistoryActivity extends BaseActivityWithBackHeader{
    ListView listView;
    BookingHistoryAdapter adapter;
    List<BookingHistoryBean> dataList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_history_activity);
        enableHeaderBackButton();
        setTitle("Booking History");
        listView=(ListView)findViewById(R.id.booking_history_list);
        adapter= new BookingHistoryAdapter(this,dataList);
        listView.setAdapter(adapter);
        callBookingHistoryApi();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(BookingHistoryActivity.this,BookingDetailsActivity.class);
                intent.putExtra("from_payment",false);
                intent.putExtra("booking_id",dataList.get(position).getOrderId());
                startActivity(intent);
            }
        });

    }

    private void callBookingHistoryApi(){
        JSONObject jsonObject= new JSONObject();
        String userId= AppUtil.getPreference(AppConstants.PREF_KEY_CUSTOMER_ID);
        BaseAsyncTask asyncTask= new BaseAsyncTask(this, new BaseAsyncTask.ServiceCallable() {
            @Override
            public void onServiceResultSuccess(String url, boolean success, String message, String response) {
                Log.e("Response", response.toString());
                AppUtil.showToastMessage(message);
                if (success){
                   // List<BookingHistoryBean> lst= ResponseParser.parseIntoBookingHistoryBeanList(AppConstants.temp);
                    List<BookingHistoryBean> lst= ResponseParser.parseIntoBookingHistoryBeanList(response);
                    if (lst!=null && lst.size()>0){
                        dataList.addAll(lst);
                        adapter.notifyDataSetChanged();
                    }
                }


            }

            @Override
            public void onServiceResultFailure(String url, String error) {
                DialogUtil.showServerErrorDialog(BookingHistoryActivity.this);
            }
        }, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.BOOKING_HISTORY+userId,jsonObject);
        asyncTask.setIsOtherUlr(true);

        asyncTask.execute();
    }

}
