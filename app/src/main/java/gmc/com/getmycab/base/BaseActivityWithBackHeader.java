package gmc.com.getmycab.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import gmc.com.getmycab.activity.BookingConfirmationActivity;
import gmc.com.getmycab.R;
import gmc.com.getmycab.activity.BookingDetailsActivity;
import gmc.com.getmycab.activity.HomeActivity;
import gmc.com.getmycab.activity.MyProfileActivity;
import gmc.com.getmycab.activity.MyProfileEditActivity;
import gmc.com.getmycab.ccavenue.activity.StatusActivity;
import gmc.com.getmycab.ccavenue.activity.WebViewActivity;

/**
 * Created by Baba on 9/5/2015.
 */
public class BaseActivityWithBackHeader extends BaseActivity {

    public ImageView backIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setTitle(String title){
        TextView textView= (TextView) findViewById(R.id.back_button_header_title_TextView);
        if (textView!=null)
            textView.setText(title+"");
    }
    public void enableHeaderBackButton(){
         backIcon=(ImageView)findViewById(R.id.back_button_header_back_ImageView);
        if (backIcon!=null){
            backIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BaseActivityWithBackHeader.this instanceof WebViewActivity) {
                        WebViewActivity activity=(WebViewActivity)BaseActivityWithBackHeader.this;
                        activity.onBackPressed();
                    } else if (BaseActivityWithBackHeader.this instanceof StatusActivity) {
                        Intent intent = new Intent(BaseActivityWithBackHeader.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else if (BaseActivityWithBackHeader.this instanceof BookingDetailsActivity) {
                        BookingDetailsActivity activity=(BookingDetailsActivity)BaseActivityWithBackHeader.this;
                        if (activity.isfromPayment) {
                            Intent intent = new Intent(BaseActivityWithBackHeader.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            finish();
                        }

                    }
                    else
                        onBackPressed();

                }
            });
        }
    }
    public void enableImageMenu(){
        ImageView menuIcon =(ImageView)findViewById(R.id.menuImage);

        if (menuIcon !=null){
            if (menuIcon.getVisibility()!=View.VISIBLE)
                menuIcon.setVisibility(View.VISIBLE);
            menuIcon.setImageResource(R.drawable.edit_icon);
            menuIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BaseActivityWithBackHeader.this instanceof BookingConfirmationActivity) {
                        BookingConfirmationActivity activity = (BookingConfirmationActivity) BaseActivityWithBackHeader.this;
                        activity.moveHome();

                    } else if (BaseActivityWithBackHeader.this instanceof MyProfileActivity) {
                        MyProfileActivity activity = (MyProfileActivity) BaseActivityWithBackHeader.this;
                        activity.moveToEdit();
                    }
                }
            });
        }
    }
    public void enableTextMenu1Enable(){
        TextView icon=(TextView)findViewById(R.id.menutextview);
        if (icon.getVisibility()!=View.VISIBLE)
            icon.setVisibility(View.VISIBLE);
        if (icon!=null){
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BaseActivityWithBackHeader.this instanceof MyProfileEditActivity) {
                        MyProfileEditActivity activity=(MyProfileEditActivity)BaseActivityWithBackHeader.this;
                        activity.callWebServiceForUpdateData();

                    } else if (BaseActivityWithBackHeader.this instanceof MyProfileActivity) {

                    }
                }
            });
        }
    }



}
