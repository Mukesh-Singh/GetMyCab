package gmc.com.getmycab.Utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gmc.com.getmycab.R;
import gmc.com.getmycab.bean.City;
import gmc.com.getmycab.bean.State;
import gmc.com.getmycab.custome.CustomTimePickerDialog;
import gmc.com.getmycab.fragments.OutStationTravelFragment;

/**
 * Created by Baba on 9/6/2015.
 */
public class DialogUtil {
    public static void showStateListDialog(final Context context, final List<State> list, final TextView setIntoTextView){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                context, R.style.AppTheme);
        //builderSingle.setIcon(R.drawable.ic_launcher);
        List<String> stateList= new ArrayList<String>();
        for (int i=0;i<list.size();i++){
            stateList.add(list.get(i).getStateName());
        }

        builderSingle.setTitle("Select state");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.select_dialog_singlechoice,stateList);

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        setIntoTextView.setText(strName);
                        setIntoTextView.setTag(list.get(which).getStateCode());
                    }
                });
        builderSingle.show();

    }

    public static void showCityListDialog(final Context context, final List<City> list, final TextView setIntoTextView){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                context, R.style.AppTheme);
        //builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Select city");

        List<String> cites= new ArrayList<String>();
        for (int i=0;i<list.size();i++){
            cites.add(list.get(i).getCityName());
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.select_dialog_singlechoice,cites);

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        setIntoTextView.setText(strName);
                        setIntoTextView.setTag(list.get(which).getCitycod());
                    }
                });
        builderSingle.show();

    }

    public static void showCityListDialogAndAddmore(final Context context, final List<City> list, final TextView setIntoTextView, final Fragment fragment){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                context, R.style.AppTheme);
        //builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Select city");

        List<String> cites= new ArrayList<String>();
        for (int i=0;i<list.size();i++){
            cites.add(list.get(i).getCityName());
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.select_dialog_singlechoice,cites);

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        setIntoTextView.setText(strName);
                        setIntoTextView.setTag(list.get(which).getCitycod());
                        if (fragment!=null && fragment instanceof OutStationTravelFragment){
                            OutStationTravelFragment frg=   (OutStationTravelFragment)fragment;
                            frg.addmoreCity();
                        }

                    }
                });
        builderSingle.show();
    }

    public static Dialog showProgressDialog(Context mContext) {
        try {
            if (mContext != null && !((Activity) mContext).isFinishing()) {
                ProgressDialog mProgressDialog = ProgressDialog.show(mContext,
                        "", "Please wait...");
                mProgressDialog.setCancelable(true);
                return mProgressDialog;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ProgressDialog(mContext);
    }

    public static void showServerErrorDialog(Context context){
        showOk(context, "Server not responding. Check your network connection or try later.");
    }

    public static void showOk(final Context context,String message){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                context, R.style.AppTheme);
        builderSingle.setTitle("GetMyCab");
        builderSingle.setMessage(message);


        builderSingle.setNegativeButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        builderSingle.show();

    }

    public static void showDatePicker(Context context, final TextView textView) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog mDialog = new DatePickerDialog(context,R.style.AppTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat df = new SimpleDateFormat(
                                "EEE, MMM. dd, yyyy");
                        Date date = new Date((year - 1900), monthOfYear,
                                dayOfMonth);


                        String da=dayOfMonth+"";
                        if (dayOfMonth<10){
                            da="0"+dayOfMonth;
                        }
                        String m=(monthOfYear + 1)+"";
                        if ((monthOfYear + 1)<10){
                            m="0"+(monthOfYear + 1);
                        }



                        textView.setTag(da + "-" + m
                                + "-" + year);
                        textView.setText(df.format(date));
                        // System.out.println(view.get);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        mDialog.setTitle("Select Date:");
        mDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public static void showTimePickerDialog(Context context,
                                            final TextView textview) {
        Calendar calendar = Calendar.getInstance();
        String selectedTime;
        CustomTimePickerDialog mTimePicker = new CustomTimePickerDialog(
                context, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay,
                                  int minute) {

                textview.setText(updateTime(hourOfDay, minute));

            }
        }, calendar.getTime().getHours(), calendar.getTime()
                .getMinutes(), false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.setCancelable(false);
        mTimePicker.show();

    }
    public static String updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }

}
