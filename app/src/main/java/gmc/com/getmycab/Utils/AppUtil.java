package gmc.com.getmycab.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import java.util.Objects;

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




}
