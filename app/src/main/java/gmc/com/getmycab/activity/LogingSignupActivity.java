package gmc.com.getmycab.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import gmc.com.getmycab.R;

/**
 * Created by Baba on 9/2/2015.
 */
public class LogingSignupActivity extends Activity implements View.OnClickListener{
    Button mSignInBtn,mSignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_activity);
        mSignInBtn =(Button)findViewById(R.id.login_signup_activity_signin_btn);
        mSignUpBtn=(Button)findViewById(R.id.login_signup_activity_signup_btn);
        mSignInBtn.setOnClickListener(this);
        mSignUpBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId()==mSignInBtn.getId())
            startActivity(new Intent(this,SignInActivity.class));
        else if (view.getId()==mSignUpBtn.getId())
            startActivity(new Intent(this,SignUpActivity.class));
    }
}
