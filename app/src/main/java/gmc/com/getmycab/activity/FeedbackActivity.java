package gmc.com.getmycab.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONObject;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppConstants;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.Utils.DialogUtil;
import gmc.com.getmycab.asyntask.BaseAsyncTask;
import gmc.com.getmycab.base.BaseActivityWithBackHeader;
import gmc.com.getmycab.networkapi.ApiCaller;
import gmc.com.getmycab.networkapi.ApiServiceUrl;

/**
 * Created by Baba on 9/9/2015.
 */
public class FeedbackActivity extends BaseActivityWithBackHeader implements View.OnClickListener,BaseAsyncTask.ServiceCallable{
    RadioGroup radioGroup1,radioGroup2,radioGroup3,radioGroup4;
    EditText feedback;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);
        enableHeaderBackButton();
        setTitle("FEEDBACK");
        initUi();
    }

    private void initUi() {
        radioGroup1=(RadioGroup)findViewById(R.id.feedback_grougp_1);
        radioGroup2=(RadioGroup)findViewById(R.id.feedback_grougp_2);
        radioGroup3=(RadioGroup)findViewById(R.id.feedback_grougp_3);
        radioGroup4=(RadioGroup)findViewById(R.id.feedback_grougp_4);
        feedback=(EditText)findViewById(R.id.feedback_edit_feedback);
        submit=(Button)findViewById(R.id.feedback_submit);
        submit.setOnClickListener(this);
    }

    private void callWebservice(){
        JSONObject jsonObject= new JSONObject();
//        String q1Value="1";
//        String q2Value="1";
//        String q3Value="1";
//        String q4Value="1";
//
//        for (int i=0;i<radioGroup1.getChildCount();i++){
//            RadioButton radioButton=(RadioButton)radioGroup1.getChildAt(i);
//            if (radioButton.isChecked()){
//                q1Value=(i+1)+"";
//            }
//        }
//

        try {
            jsonObject.put("Question1",getSelectedAnswerOfQuestion(radioGroup1));
            jsonObject.put("Question2",getSelectedAnswerOfQuestion(radioGroup2));
            jsonObject.put("Question3",getSelectedAnswerOfQuestion(radioGroup3));
            jsonObject.put("Question4",getSelectedAnswerOfQuestion(radioGroup4));
            jsonObject.put("feedback",feedback.getText().toString());
            jsonObject.put("customer_id", AppUtil.getPreference(AppConstants.PREF_KEY_CUSTOMER_ID));

            //jsonObject.put("order_id", AppConstants.USER_DETAILS.getCustomerId());

        }
        catch (Exception e){
            e.printStackTrace();
        }
        new BaseAsyncTask(this,true,this, ApiCaller.RequestType.HTTP_POST, ApiServiceUrl.FEEDBACK,jsonObject).execute();

    }

    private String getSelectedAnswerOfQuestion(RadioGroup rg){
        String value="1";
        for (int i=0;i<radioGroup1.getChildCount();i++){
            RadioButton radioButton=(RadioButton)radioGroup1.getChildAt(i);
            if (radioButton.isChecked()){
                value=(i+1)+"";
            }
        }
        return value;
    }

    @Override
    public void onClick(View v) {
            callWebservice();
    }

    @Override
    public void onServiceResultSuccess(String url, boolean success, String message, String response) {
        AppUtil.showToastMessage(message);
        if (success)
            finish();
    }

    @Override
    public void onServiceResultFailure(String url, String error) {
        DialogUtil.showServerErrorDialog(this);
    }
}
