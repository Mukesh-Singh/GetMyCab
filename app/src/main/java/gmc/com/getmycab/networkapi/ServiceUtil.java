package gmc.com.getmycab.networkapi;

import android.content.Context;

import org.json.JSONObject;

import gmc.com.getmycab.asyntask.BaseAsyncTask;

/**
 * Created by Baba on 9/6/2015.
 */
public class ServiceUtil {
    public static void callStateListServies(Context context,BaseAsyncTask.ServiceCallable callable){
        JSONObject jsonObject= new JSONObject();
        try {

        }
        catch (Exception e){
            e.printStackTrace();
        }
        new BaseAsyncTask(context,true,callable, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.STATE_LIST,jsonObject).execute();
    }
        public static void callCityListServies(Context context,JSONObject jsonObject,BaseAsyncTask.ServiceCallable callable){
//            JSONObject jsonObject= new JSONObject();
//            try {
//
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
            new BaseAsyncTask(context,true,callable, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.CITY_LIST_TRAVEL,jsonObject).execute();
        }

    public static void callCityListServies(Context context,boolean showProgessDialog,JSONObject jsonObject,BaseAsyncTask.ServiceCallable callable){
//            JSONObject jsonObject= new JSONObject();
//            try {
//
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
        new BaseAsyncTask(context,showProgessDialog,callable, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.CITY_LIST_TRAVEL,jsonObject).execute();
    }
}
