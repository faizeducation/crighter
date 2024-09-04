package com.crighter.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crighter.R;
import com.crighter.URLS.APIURLS;
import com.crighter.VolleyLibraryFiles.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActiity extends AppCompatActivity {

    TextView tv_back_to_login;
    RelativeLayout bt_register;
    Spinner sp_relation;
    RadioGroup rg_auth;
    EditText et_auth_phone;
    EditText et_auth_email;
    EditText et_auth_fullname;
    RadioGroup user_rg;
    EditText et_user_password_again;
    EditText et_user_password;
    EditText et_user_phone;
    EditText et_user_email;
    EditText et_user_fullname;
    ProgressBar progressbar;
    String phoneNumber;

    TextView tv_registration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_actiity);

        init();
        backtoLoginClickHandler();
        //startAuthUserMobileWithOnlyNumber92();
        startAuthUserMobileWithOnlyNumber92();
        startMobileWithOnlyNumber92();
        gettingIntentValue();
    }

    private void init()
    {
        tv_back_to_login = (TextView) findViewById(R.id.tv_back_to_login);
        bt_register = (RelativeLayout) findViewById(R.id.bt_register);
        sp_relation = (Spinner) findViewById(R.id.sp_relation);
        rg_auth = (RadioGroup) findViewById(R.id.rg_auth);
        et_auth_phone = (EditText) findViewById(R.id.et_auth_phone);
        et_auth_email = (EditText) findViewById(R.id.et_auth_email);
        et_auth_fullname = (EditText) findViewById(R.id.et_auth_fullname);
        et_user_password_again = (EditText) findViewById(R.id.et_user_password_again);
        et_user_password = (EditText) findViewById(R.id.et_user_password);
        et_user_phone = (EditText) findViewById(R.id.et_user_phone);
        et_user_email = (EditText) findViewById(R.id.et_user_email);
        et_user_fullname = (EditText) findViewById(R.id.et_user_fullname);
        user_rg = (RadioGroup) findViewById(R.id.user_rg);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        tv_registration = (TextView) findViewById(R.id.tv_registration);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/office_square.otf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(),  "fonts/century_gothic.ttf");
        tv_registration.setTypeface(custom_font);
        et_user_fullname.setTypeface(custom_font2);
        et_user_email.setTypeface(custom_font2);
        et_user_phone.setTypeface(custom_font2);
        et_user_password.setTypeface(custom_font2);
        et_user_password_again.setTypeface(custom_font2);


        TextView tv_gender = (TextView) findViewById(R.id.tv_gender);
        user_rg.check(user_rg.getChildAt(0).getId());
        tv_gender.setTypeface(custom_font2);
        int id1 = user_rg.getChildAt(0).getId();
        RadioButton rd1 = (RadioButton) findViewById(id1);
        rd1.setTypeface(custom_font2);

        int id2 = user_rg.getChildAt(0).getId();
        RadioButton rd2 = (RadioButton) findViewById(id2);
        rd2.setTypeface(custom_font2);

        int id3 = user_rg.getChildAt(0).getId();
        RadioButton rd3 = (RadioButton) findViewById(id3);
        rd3.setTypeface(custom_font2);

        et_auth_fullname.setTypeface(custom_font2);
        et_auth_email.setTypeface(custom_font2);
        et_auth_phone.setTypeface(custom_font2);
        TextView tv_aut_gender = (TextView) findViewById(R.id.tv_aut_gender);
        tv_aut_gender.setTypeface(custom_font2);
        rg_auth.check(rg_auth.getChildAt(0).getId());
        tv_gender.setTypeface(custom_font2);
        int id11 = rg_auth.getChildAt(0).getId();
        RadioButton rd11 = (RadioButton) findViewById(id11);
        rd11.setTypeface(custom_font2);

        int id22 = rg_auth.getChildAt(0).getId();
        RadioButton rd22 = (RadioButton) findViewById(id22);
        rd22.setTypeface(custom_font2);

        int id33 = rg_auth.getChildAt(0).getId();
        RadioButton rd33 = (RadioButton) findViewById(id33);
        rd33.setTypeface(custom_font2);

        TextView tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setTypeface(custom_font);
        tv_back_to_login.setTypeface(custom_font2);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(RegistrationActiity.this,
                R.array.select_relation, R.layout.spinner_item_sale_man);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_relation.setAdapter(adapter);

        registerClickHandler();
    }

    private void gettingIntentValue()
    {
        /*Intent i = getIntent();
        phoneNumber = i.getStringExtra("phone");
        et_user_phone.setText(phoneNumber);*/

    }
    private void backtoLoginClickHandler()
    {
        tv_back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActiity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void registerClickHandler()
    {

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userFullName = et_user_fullname.getText().toString();
                String userPhone = et_user_phone.getText().toString();
                String userEmail = et_user_email.getText().toString();
                int radioButtonID = user_rg.getCheckedRadioButtonId();
                String authFullname = et_auth_fullname.getText().toString();
                String authPhone = et_auth_phone.getText().toString();
                String authEmail = et_auth_email.getText().toString();
                int radioButtonIDAuth = rg_auth.getCheckedRadioButtonId();
                String password = et_user_password.getText().toString();
                String againPassword = et_user_password_again.getText().toString();
                int itemPositionForRelation = sp_relation.getSelectedItemPosition();
                String realation = sp_relation.getSelectedItem().toString();

                final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

                if(userFullName.length()==0){
                    et_user_fullname.setError("Should not be empty");
                    et_user_fullname.setFocusable(true);
                    et_user_fullname.setAnimation(animShake);
                }
                else if (userEmail.length()==0){
                    et_user_email.setError("Should not be empty");
                    et_user_email.setFocusable(true);
                    et_user_email.setAnimation(animShake);
                }
                else if (!emailValidator(userEmail)){
                    et_user_email.setError("invalid email");
                    et_user_email.setFocusable(true);
                    et_user_email.setAnimation(animShake);
                }
                else if (userPhone.length()==0){
                    et_user_phone.setError("Should not be empty");
                    et_user_phone.setFocusable(true);
                    et_user_phone.setAnimation(animShake);
                }
                else if (userPhone.length()<7 ){
                    et_user_phone.setError("invalid phone");
                    et_user_phone.setFocusable(true);
                    et_user_phone.setAnimation(animShake);
                }

                else if (password.length()==0){
                    et_user_password.setError("Should not be empty");
                    et_user_password.setFocusable(true);
                    et_user_password.setAnimation(animShake);
                }
                else if (password.length()<5){
                    et_user_password.setError("Length should more than 5 cherecters");
                    Toast.makeText(RegistrationActiity.this, "more then 5 cherecters allow", Toast.LENGTH_LONG).show();
                    et_user_password.setFocusable(true);
                    et_user_password.setAnimation(animShake);
                }
                else if (againPassword.length()==0){
                    et_user_password_again.setError("Should not be empty");
                    et_user_password_again.setFocusable(true);
                    et_user_password_again.setAnimation(animShake);
                }
                else if (!password.equals(againPassword)){
                    Toast.makeText(RegistrationActiity.this, "Password not match", Toast.LENGTH_LONG).show();
                }

                else if (radioButtonID==-1){
                    user_rg.setAnimation(animShake);
                    Toast.makeText(RegistrationActiity.this, "Please Select Gendar", Toast.LENGTH_LONG).show();
                }
                else if (authFullname.length()==0){
                    et_auth_fullname.setError("Should not be empty");
                    et_auth_fullname.setFocusable(true);
                    et_auth_fullname.setAnimation(animShake);
                }
                else if (authEmail.length()==0){
                    et_auth_email.setError("Should not be empty");
                    et_auth_email.setFocusable(true);
                    et_auth_email.setAnimation(animShake);
                }
                else if (!emailValidator(authEmail)){
                    et_auth_email.setError("invalid email");
                    et_auth_email.setFocusable(true);
                    et_auth_email.setAnimation(animShake);
                }
                else if (authPhone.length()==0){
                    et_auth_phone.setError("should not be empty");
                    et_auth_phone.setFocusable(true);
                    et_auth_phone.setAnimation(animShake);
                }
                else if (authPhone.length()<7){
                    et_auth_phone.setError("invalid phone");
                    et_auth_phone.setFocusable(true);
                    et_auth_phone.setAnimation(animShake);
                }

                else if (radioButtonIDAuth==-1){
                    user_rg.setAnimation(animShake);
                    Toast.makeText(RegistrationActiity.this, "Please select gendar", Toast.LENGTH_LONG).show();
                }
                else if (itemPositionForRelation == 0){
                    Toast.makeText(RegistrationActiity.this, "Please Select Relation", Toast.LENGTH_LONG).show();
                }
                else {
                    RadioButton radioButton = (RadioButton) user_rg.findViewById(radioButtonID);
                    String userGender = radioButton.getText().toString();
                    RadioButton radioButtonAuth = (RadioButton) rg_auth.findViewById(radioButtonIDAuth);
                    String userGenderAuth = radioButton.getText().toString();

                    userPhone = "+"+userPhone;
                    authPhone = "+"+authPhone;

                    Log.e("TAG", "here is detail to send user userFullName " + userFullName);
                    Log.e("TAG", "here is detail to send user userPhone " + userPhone);
                    Log.e("TAG", "here is detail to send user userPhone " + userPhone);
                    Log.e("TAG", "here is detail to send user userGender " + userGender);
                    Log.e("TAG", "here is detail to send user password " + password);
                    Log.e("TAG", "here is detail to send user authFullname " + authFullname);
                    Log.e("TAG", "here is detail to send user authPhone " + authPhone);
                    Log.e("TAG", "here is detail to send user authEmail " + authEmail);
                    Log.e("TAG", "here is detail to send user userGender " + userGender);
                    Log.e("TAG", "here is detail to send user userGenderAuth " + userGenderAuth);
                    Log.e("TAG", "here is detail to send user realation" + realation);

                    Intent i = new Intent(RegistrationActiity.this, PhoneAuthActivity.class);
                    i.putExtra("userFullName", userFullName);
                    i.putExtra("userEmail", userEmail);
                    i.putExtra("userPhone", userPhone);
                    i.putExtra("userGender", userGender);
                    i.putExtra("authFullname", authFullname);
                    i.putExtra("authEmail", authEmail);
                    i.putExtra("authPhone", authPhone);
                    i.putExtra("userGenderAuth", userGenderAuth);
                    i.putExtra("realation", realation);
                    i.putExtra("password", password);
                    startActivity(i);
                    //registrationService(userFullName, userEmail, userPhone, userGender, authFullname, authEmail, authPhone, userGenderAuth, realation, password);

                }

            }
        });

    }

    public void startAuthUserMobileWithOnlyNumber92()
    {

        et_user_phone.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();


                if (x.startsWith("+")){

                    Toast.makeText(RegistrationActiity.this, "Please enter your number starting with country code(e.g 921234567890)", Toast.LENGTH_LONG).show();
                    et_user_phone.setText("");
                }

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }
        });//end for login editText

    }

    public void startMobileWithOnlyNumber92()
    {

        et_auth_phone.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();


                if (x.startsWith("+")){

                    Toast.makeText(RegistrationActiity.this, "Please enter your number starting with country code(e.g 921234567890)", Toast.LENGTH_LONG).show();
                    et_auth_phone.setText("");
                }


            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }
        });//end for login editText

    }

    public static boolean emailValidator(final String mailAddress) {

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();
    }

    //logining user serverice
    private void registrationService(final String userfullName, final String userEmail, final String userPhone, final String userGendar,
                                     final String authfullName, final String authEmail, final String authPhone, final String authGendar, final String relation, final String password){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        progressbar.setVisibility(View.VISIBLE);


        StringRequest strReq = new StringRequest(Request.Method.POST, APIURLS.REGISTRATION_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Login Response: " + response.toString());
                //hideDialog();
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String mesage = jObj.getString("msg");
                        if (mesage.equals("Register Successfully")) {
                            Toast.makeText(RegistrationActiity.this, "You have register successfully Please login to use your account", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegistrationActiity.this, LoginActivity.class));
                            finish();
                        }


                    } else {
                        String errorMsg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
                progressbar.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userfullname", userfullName);
                params.put("userphone", userPhone);
                params.put("useremail", userEmail);
                params.put("usergender", userGendar);
                params.put("userudid", "asdfasdf3asdf32sadf3");
                params.put("userpassword", password);
                params.put("authfullname", authfullName);
                params.put("authphone", authPhone);
                params.put("authemail", authEmail);
                params.put("authgender", authGendar);
                params.put("realtionwithauth", relation);
                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }//end of registration service
}
