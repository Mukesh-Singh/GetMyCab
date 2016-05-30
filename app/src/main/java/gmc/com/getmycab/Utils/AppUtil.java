package gmc.com.getmycab.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import gmc.com.getmycab.bean.Car;
import gmc.com.getmycab.gps.GpsTracker;

/**
 * Created by Baba on 9/5/2015.
 */
public class AppUtil {
    private static Context mcontext;
    public static void initialize(Context context){
        mcontext=context;
    }
    public static boolean isEmpty(EditText editText){
        return editText.getText().toString().isEmpty();

    }
    public static boolean isValidEmail(EditText editText){
        if (editText.getText().toString().isEmpty()){
            return false;
        }
        else if (editText.getText().toString().contains(".") && editText.getText().toString().contains("@") )
            return true;
        else return false;


    }
    public static void setError(EditText editText,String error){
        editText.setError(error);
    }


    public static void showToastMessage(String message){
        Toast.makeText(mcontext,message,Toast.LENGTH_SHORT).show();
    }
    public static void showInputFieldEmptyError(String inputField){
        showToastMessage("Please enter " + inputField + " !");
    }
    public static void savePreference(String key,String value){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(mcontext);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static void savePreference(String key,int value){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(mcontext);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void savePreference(String key,boolean value){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(mcontext);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String  getPreference(String key){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(mcontext);
        try {
            if(preferences.contains(key))
                return preferences.getString(key,"");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
    public static int getPreferenceInt(String key){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(mcontext);
        try {
            if(preferences.contains(key))
                return preferences.getInt(key, 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    public static boolean  getPreferenceBoolean(String key){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(mcontext);
        try {
            if(preferences.contains(key))
                return preferences.getBoolean(key, false);
        }
        catch (Exception e){
            e.printStackTrace();

        }

        return false;
    }

    public  static void callToGetMeCab(){
        Intent intent= new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+919015154545"));
        mcontext.startActivity(intent);
    }

    public static void checkGps(){
        GpsTracker gpsTracker= new GpsTracker(mcontext);
        if (gpsTracker.getIsGPSTrackingEnabled()){

        }
        else
            gpsTracker.showSettingsAlert();
    }

    public static String formatedDate(String dateString){
        SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2= new SimpleDateFormat("EEEE MMM dd, yyyy");
        String formatedD="";
        try {
            Date date =sdf.parse(dateString);
            formatedD=sdf2.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formatedD;
    }


    public static String  getAdditionalCostTextForOutStation(Car car){

        String tem="";
        int day=AppUtil.getPreferenceInt(AppConstants.CAB_SEARCH_DURATION);
        String source=AppUtil.getPreference(AppConstants.CAB_SEARCH_FROM_TO);
        if (source!=null) {
             if (source.contains("Manali") || source.contains("manali") || source.contains("Mussoorie")||source.contains("mussoorie")||source.contains("Nandi")||source.contains("nandi"))
                tem = "*     Green tax or any other tax on actual basis\n";
        }
       tem=tem+"*     Driver Allowance (DA) Rs. "+getReadableString(String.valueOf(Integer.parseInt(car.getPriceDetails().getBaseDriverCharge())*day))+"\n"+
                   "*     DA for night if drives between 10pm-5am Rs 250 per night.\n"+
                   "*     State Tax (State Permit) on actual basis whenever enter new state\n"+
                   "*     Any Toll tax or parking charges on actual basis.";

        return tem;
    }

    public static String  getNoteForOutStation(Car car){
        String tem="*    Driver would take care of his food and stay\n"+
                "*     Min. charge per day is "+car.getPriceDetails().getMinimumKmPerDay()+" kms\n"+
                "*     One day means one calendar day (12am - 11:59pm)\n"+
                "*     AC will be switched off in hilly areas\n"+
                "*     Local travel is not allowed in source city";

        return tem;
    }


    public static String  getAdditionalCostTextForOneWay(Car car){
        String tem="";
        String source=AppUtil.getPreference(AppConstants.CAB_SEARCH_FROM_TO);
        if (source!=null) {
            if (source.contains("Agra") || source.contains("agra"))
                tem = "*    Yamuna Express Highway Toll Rs 360/- one side\n";
            else if (source.contains("Manali") || source.contains("manali") || source.contains("Mussoorie")||source.contains("mussoorie")||source.contains("Nandi")||source.contains("nandi"))
                tem = "*     Green tax on actual basis\n";
            else if (source.contains("umbai") && source.contains("une") )
                tem = "*     Above price is applicable between Mumbai Airport and Pune Railway Station. Additional price may be charged for other pick / drop locations from / in both the cities that may vary from Rs 200 to Rs 800 depending on the distance\n";
        }


        tem=  tem+  "*     Any parking or airport entry charges\n"+
                "*     Extra per pick up or drop shall be charged @ Rs 250/- within 10kms\n"+
                "*     Waiting charges Rs 250/- per hour\n";


        return tem;
    }

    public static String  getNoteForOneWay(Car car){
        String tem="*    AC will be switched off in hilly areas\n"+
                "*     Local travel is not allowed in source city\n";

        return tem;
    }




    private static String getReadableString(String str){
        if (str==null || str.length()==0)
            return "0";
        else
            return str;
    }


}
