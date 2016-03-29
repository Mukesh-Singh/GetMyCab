package gmc.com.getmycab.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

import gmc.com.getmycab.Utils.AppUtil;

/**
 * Created by Baba on 9/1/2015.
 */
public class BaseActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtil.initialize(this);
    }


}
