package com.example.hackathon_client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MypageEditActivity extends AppCompatActivity {

    private RequestQueue queue;
    private static final String TAG = "MYPAGE_EDIT";
    public TextView user_email;
    public TextView user_name;
    public TextView user_gender;

    public EditText user_phone;
    public RadioGroup major;
    public RadioGroup type;
    public EditText user_career;
    public EditText user_interest;

    public Button edit_ok;
    public RadioButton simcom;
    public RadioButton global;
    public RadioButton fresh;
    public RadioButton senior;
    public RadioButton graduate;

    public String url;
    public StringRequest stringRequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_edit);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        url = "http://ec2-3-37-147-187.ap-northeast-2.compute.amazonaws.com/api/user"+user.email;

        user_email = findViewById(R.id.user_email);
        user_name = findViewById(R.id.user_name);
        user_gender = findViewById(R.id.user_gender);
        user_phone = findViewById(R.id.user_phone);
        user_career = findViewById(R.id.user_career);
        user_interest = findViewById(R.id.user_interest);
        edit_ok = findViewById(R.id.editOK_btn);

        major = findViewById(R.id.major);
        type = findViewById(R.id.type);

        simcom = findViewById(R.id.computer);
        global = findViewById(R.id.globalSW);
        fresh = findViewById(R.id.postman);
        senior = findViewById(R.id.student);
        graduate = findViewById(R.id.graduate);

        if(user.major.equals("심화컴퓨터공학과")){
            simcom.setChecked(true);
        } else{
            global.setChecked(true);
        }

        if(user.userType.equals("신입생")){
            fresh.setChecked(true);
        } else if(user.userType.equals("졸업생")){
            graduate.setChecked(true);
        } else{
            senior.setChecked(true);
        }

        user_email.setText(user.email);
        user_name.setText(user.name);
        user_gender.setText(user.gender);

        user_phone.setHint(user.tel_number);
        user_career.setHint(user.career);
        user_interest.setHint(user.interest);

        edit_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int selectedId_major = major.getCheckedRadioButtonId();
                    RadioButton radioButton_major = (RadioButton) findViewById(selectedId_major);
                    user.major = radioButton_major.getText().toString();

                    if(user.major.equals("심컴")){
                        user.major = "ADVANCED";
                    } else{
                        user.major = "GLOBALSW";
                    }

                    int selectedId_type = type.getCheckedRadioButtonId();
                    RadioButton radioButton_type = (RadioButton) findViewById(selectedId_type);
                    user.userType = radioButton_type.getText().toString();

                    if(user.userType.equals("신입생")){
                        user.userType = "FRESHMAN";
                    } else if(user.userType.equals("재학생")){
                        user.userType = "SENIOR";
                    } else{
                        user.userType = "GRADUATE";
                    }

                    user.career = user_career.getText().toString();
                    user.interest = user_interest.getText().toString();

                    push_mypage(user_phone.getText().toString(),user.major, user.userType, user.career, user.interest);

                    Intent intent = new Intent(MypageEditActivity.this, MypageActivity.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    void push_mypage(String phone, String user_major, String user_type, String user_career, String user_interest){
        stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MypageEditActivity.this, response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tel_number", phone);
                params.put("userType", user_type);
                params.put("major", user_major);
                params.put("career", user_career);
                params.put("interest", user_interest);

                return params;
            }
        };
        stringRequest.setTag(TAG);

        queue.add(stringRequest);
    }


}
