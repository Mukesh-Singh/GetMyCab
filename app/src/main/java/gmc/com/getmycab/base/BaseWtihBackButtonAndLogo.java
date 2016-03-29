package gmc.com.getmycab.base;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppUtil;

/**
 * Created by Baba on 9/7/2015.
 */
public class BaseWtihBackButtonAndLogo extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void enableHeaderBackButton() {
        ImageView back = (ImageView) findViewById(R.id.back_button_header_back_ImageView);
        if (back != null) {
            back.setOnClickListener(   new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    public void enableCallButton(){
        ImageView call = (ImageView) findViewById(R.id.header_call);

        if (call != null) {
            call.setVisibility(View.VISIBLE);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtil.callToGetMeCab();
                }
            });
        }
    }

}
