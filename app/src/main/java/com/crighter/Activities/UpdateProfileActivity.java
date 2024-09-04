package com.crighter.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.crighter.Preferences.SPref;
import com.crighter.R;
import com.crighter.URLS.APIURLS;
import com.crighter.VolleyLibraryFiles.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProfileActivity extends AppCompatActivity {

    RelativeLayout bt_register;
    Spinner sp_relation;
    RadioGroup rg_auth;
    EditText et_auth_phone;
    EditText et_auth_email;
    EditText et_auth_fullname;
    RadioGroup user_rg;
    EditText et_user_phone;
    EditText et_user_email;
    EditText et_user_fullname;
    ProgressBar progressbar;
    TextView tv_email_not_verified;
    Typeface custom_font;
    Typeface custom_font2;

    ImageView tv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);



        init();
        startAuthUserMobileWithOnlyNumber92();
        startMobileWithOnlyNumber92(et_auth_phone);
        showVerificationEmailDialog();
        showingAlertForChangePhonNumber();
        onEmailClickHandler();
        isUserVarified();
        imageViewBackClickHandler();
    }

    private void init()
    {

        custom_font = Typeface.createFromAsset(getAssets(),  "fonts/office_square.otf");
        custom_font2 = Typeface.createFromAsset(getAssets(),  "fonts/century_gothic.ttf");


        bt_register = (RelativeLayout) findViewById(R.id.bt_register);
        sp_relation = (Spinner) findViewById(R.id.sp_relation);
        rg_auth = (RadioGroup) findViewById(R.id.rg_auth);
        et_auth_phone = (EditText) findViewById(R.id.et_auth_phone);
        et_auth_email = (EditText) findViewById(R.id.et_auth_email);
        et_auth_fullname = (EditText) findViewById(R.id.et_auth_fullname);
        et_user_phone = (EditText) findViewById(R.id.et_user_phone);
        et_user_email = (EditText) findViewById(R.id.et_user_email);
        et_user_email.setFocusable(false);
        et_user_fullname = (EditText) findViewById(R.id.et_user_fullname);
        user_rg = (RadioGroup) findViewById(R.id.user_rg);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        tv_email_not_verified = (TextView) findViewById(R.id.tv_email_not_verified);

        tv_back =(ImageView) findViewById(R.id.tv_back);

        SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_USER_CRED, 0);
        String USER_FULLNAME = SPref.getStringPref(sharedPreferences, SPref.USER_FULLNAME);
        String USER_PHONE = SPref.getStringPref(sharedPreferences, SPref.USER_PHONE);
        String USER_EMAIL = SPref.getStringPref(sharedPreferences, SPref.USER_EMAIL);
        String USER_GENDER = SPref.getStringPref(sharedPreferences, SPref.USER_GENDER);
        String AUTH_FULLNAME = SPref.getStringPref(sharedPreferences, SPref.AUTH_FULLNAME);
        String AUTH_PHONE = SPref.getStringPref(sharedPreferences, SPref.AUTH_PHONE);
        String AUTH_EMAIL = SPref.getStringPref(sharedPreferences, SPref.AUTH_EMAIL);
        String AUTH_GENDER = SPref.getStringPref(sharedPreferences, SPref.AUTH_GENDER);
        String RELATION = SPref.getStringPref(sharedPreferences, SPref.RELATION);
        String userID = SPref.getStringPref(sharedPreferences, SPref.USER_ID);


        et_user_fullname.setText(USER_FULLNAME);
        et_user_phone.setText(USER_PHONE);
        et_user_phone.setFocusable(false);
        et_user_email.setText(USER_EMAIL);
        et_auth_fullname.setText(AUTH_FULLNAME);
        et_auth_phone.setText(AUTH_PHONE);
        et_auth_email.setText(AUTH_EMAIL);

        Log.e("TAG", "the pref user gender is " + USER_GENDER);
        Log.e("TAG", "the pref auth gender is " + AUTH_GENDER);
        Log.e("TAG", "the pref user relation is " + RELATION);
        Log.e("TAG", "the pref user relation is " + userID);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(UpdateProfileActivity.this,
                R.array.select_relation, R.layout.spinner_item_sale_man);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_relation.setAdapter(adapter);

        if (RELATION.equals("Father")){sp_relation.setSelection(1);}
        if (RELATION.equals("Mother")){sp_relation.setSelection(2);}
        if (RELATION.equals("Grand Father")){sp_relation.setSelection(3);}
        if (RELATION.equals("Grand Mother")){sp_relation.setSelection(4);}
        if (RELATION.equals("Brother")){sp_relation.setSelection(5);}
        if (RELATION.equals("Sister")){sp_relation.setSelection(6);}
        if (RELATION.equals("Uncle")){sp_relation.setSelection(7);}
        if (RELATION.equals("Friend")){sp_relation.setSelection(8);}

        et_user_phone.setTypeface(custom_font2);
        et_user_email.setTypeface(custom_font2);
        et_user_fullname.setTypeface(custom_font2);

        et_auth_fullname.setTypeface(custom_font2);
        et_auth_email.setTypeface(custom_font2);
        et_auth_phone.setTypeface(custom_font2);
        TextView tv_aut_gender = (TextView) findViewById(R.id.tv_aut_gender);
        tv_aut_gender.setTypeface(custom_font2);
        rg_auth.check(rg_auth.getChildAt(0).getId());
        TextView tv_gender = (TextView) findViewById(R.id.tv_gender);
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

        tv_email_not_verified.setTypeface(custom_font);
        TextView tv_update = (TextView) findViewById(R.id.tv_update);
        tv_update.setTypeface(custom_font2);

        if (AUTH_GENDER.equals("Male")) {
            rg_auth.check(rg_auth.getChildAt(0).getId());
        }
        if (AUTH_GENDER.equals("Female")){
            rg_auth.check(rg_auth.getChildAt(1).getId());
        }
        if (AUTH_GENDER.equals("Other")){
            rg_auth.check(rg_auth.getChildAt(2).getId());
        }
        if (USER_GENDER.equals("Male")) {
            user_rg.check(user_rg.getChildAt(0).getId());
        }
        if (USER_GENDER.equals("Female")){
            user_rg.check(user_rg.getChildAt(1).getId());
        }
        if (USER_GENDER.equals("Other")){
            user_rg.check(user_rg.getChildAt(2).getId());
        }

        updateClickHandler(userID);

    }

    private void isUserVarified()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_USER_CRED, 0);
        String vStatus = SPref.getStringPref(sharedPreferences, SPref.EVERIFY_STATUS);
        if (vStatus.equals("1")) {
            tv_email_not_verified.setVisibility(View.GONE);
        }
        else {
            tv_email_not_verified.setVisibility(View.VISIBLE);
        }
    }

    private void updateClickHandler(final String USER_ID)
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
                else if (userPhone.length()<7){
                    et_user_phone.setError("invalid phone");
                    et_user_phone.setFocusable(true);
                    et_user_phone.setAnimation(animShake);
                }
                else if (!userPhone.startsWith("+")){
                    et_user_phone.setError("invalid phone");
                    et_user_phone.setFocusable(true);
                    et_user_phone.setAnimation(animShake);
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
                    et_auth_phone.setError("inalid phone");
                    et_auth_phone.setFocusable(true);
                    et_auth_phone.setAnimation(animShake);
                }
                else if (!authPhone.startsWith("+")){
                    et_auth_phone.setError("inalid phone");
                    et_auth_phone.setFocusable(true);
                    et_auth_phone.setAnimation(animShake);
                }

                else {

                    RadioButton radioButton = (RadioButton) user_rg.findViewById(radioButtonID);
                    String userGender = radioButton.getText().toString();
                    RadioButton radioButtonAuth = (RadioButton) rg_auth.findViewById(radioButtonIDAuth);
                    String userGenderAuth = radioButtonAuth.getText().toString();

                    updatingUserProfile(userFullName, userEmail, userPhone, userGender, authFullname, authEmail, authPhone, userGenderAuth, realation, USER_ID);

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

                if (x.startsWith("0")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    et_user_phone.setText("");
                }
                if (x.startsWith("1")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    et_user_phone.setText("");
                }
                if (x.startsWith("2")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    et_user_phone.setText("");
                }
                if (x.startsWith("3")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    et_user_phone.setText("");
                }

                if (x.startsWith("4")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    et_user_phone.setText("");
                }
                if (x.startsWith("5")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    et_user_phone.setText("");
                }
                if (x.startsWith("6")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    et_user_phone.setText("");
                }
                if (x.startsWith("7")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    et_user_phone.setText("");
                }
                if (x.startsWith("8")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    et_user_phone.setText("");
                }
                if (x.startsWith("9")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    et_user_phone.setText("");
                }

                if (x.startsWith("+")){
                    if (x.length()==15) {
                        //doctorSignInEmail.setText(x);
                        et_user_phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});

                    }
                }
                else {
                    et_user_phone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(120)});
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

    public void startMobileWithOnlyNumber92(EditText editText)
    {

        editText.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();

                if (x.startsWith("0")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    editText.setText("");
                }

                if (x.startsWith("1")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    editText.setText("");
                }
                if (x.startsWith("2")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    editText.setText("");
                }
                if (x.startsWith("3")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    editText.setText("");
                }

                if (x.startsWith("4")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    editText.setText("");
                }
                if (x.startsWith("5")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    editText.setText("");
                }
                if (x.startsWith("6")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    editText.setText("");
                }
                if (x.startsWith("7")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    editText.setText("");
                }
                if (x.startsWith("8")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    editText.setText("");
                }
                if (x.startsWith("9")){

                    Toast.makeText(UpdateProfileActivity.this, "Please enter your number starting with country code(e.g +921234567890)", Toast.LENGTH_LONG).show();
                    et_auth_phone.setText("");
                }

                if (x.startsWith("+")){
                    if (x.length()==15){
                        //doctorSignInEmail.setText(x);
                        editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});

                    }
                }
                else {
                    editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(120)});
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
    private void updatingUserProfile(final String userfullName, final String userEmail, final String userPhone, final String userGendar,
                                     final String authfullName, final String authEmail, final String authPhone, final String authGendar, final String relation, final String userid){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        progressbar.setVisibility(View.VISIBLE);
        Log.e("TAG", "here is detail to send user userGender " + userGendar);
        Log.e("TAG", "here is detail to send user userGenderAuth " + authGendar);


        StringRequest strReq = new StringRequest(Request.Method.POST, APIURLS.UPDATE_USER, new Response.Listener<String>() {

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
                        if (mesage.equals("Profile updated")) {
                            Toast.makeText(UpdateProfileActivity.this, "Your Profile Updated Successfully", Toast.LENGTH_LONG).show();
                            SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_USER_CRED, 0);
                            SPref.StrogingSingleString(sharedPreferences, SPref.USER_FULLNAME, userfullName);
                            SPref.StrogingSingleString(sharedPreferences, SPref.USER_EMAIL, userEmail);
                            SPref.StrogingSingleString(sharedPreferences, SPref.USER_PHONE, userPhone);
                            SPref.StrogingSingleString(sharedPreferences, SPref.USER_GENDER, userGendar);
                            SPref.StrogingSingleString(sharedPreferences, SPref.AUTH_FULLNAME, authfullName);
                            SPref.StrogingSingleString(sharedPreferences, SPref.AUTH_EMAIL, authEmail);
                            SPref.StrogingSingleString(sharedPreferences, SPref.AUTH_PHONE, authPhone);
                            SPref.StrogingSingleString(sharedPreferences, SPref.AUTH_GENDER, authGendar);
                            SPref.StrogingSingleString(sharedPreferences, SPref.RELATION, relation);

                            Intent i = new Intent(UpdateProfileActivity.this, DashboardActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
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
                params.put("user_id", userid);
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

    private void showVerificationEmailDialog()
    {
        tv_email_not_verified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(UpdateProfileActivity.this);
                alert.setTitle("Email Verification Process");
                alert.setMessage("Click Verify to send Verfication Email " + et_user_email.getText().toString());
                alert.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callingEmailAuthAPI(et_user_email.getText().toString());
                    }
                });
                alert.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });
    }

    private void callingEmailAuthAPI(String email)
    {
        String cancel_req_tag = "register";
        progressbar.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST, APIURLS.EMAIL_AUTH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "auth email Response: " + response.toString());
                //hideDialog();
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String mesage = jObj.getString("msg");
                        if (mesage.equals("Check your Registered Email for Verification Code")) {
                            //Toast.makeText(UpdateProfileActivity.this, "Your Profile Updated Successfully", Toast.LENGTH_LONG).show();
                            String code = jObj.getString("code");
                            shwoingVerifcationCodeDialog(code, "normal");
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
                params.put("useremail", email);
                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }//end of email auth service

    private void shwoingVerifcationCodeDialog(final String mCode, String status)
    {
        Dialog dialog = new Dialog(UpdateProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_email_verification);
        TextView tv_fp = (TextView) dialog.findViewById(R.id.tv_fp);
        TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_detail);
        EditText et_verify_code = (EditText) dialog.findViewById(R.id.et_verify_code);
        RelativeLayout bt_verify = (RelativeLayout) dialog.findViewById(R.id.bt_verify);
        RelativeLayout rl_cancel = (RelativeLayout) dialog.findViewById(R.id.rl_cancel);
        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        TextView tv_submit = (TextView) dialog.findViewById(R.id.tv_submit);

        tv_fp.setTypeface(custom_font);
        tv_detail.setTypeface(custom_font);
        et_verify_code.setTypeface(custom_font);
        tv_fp.setTypeface(custom_font);
        tv_cancel.setTypeface(custom_font2);
        tv_submit.setTypeface(custom_font2);

        bt_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = et_verify_code.getText().toString();
                if (code.length()==0)
                {
                    et_verify_code.setError("Should not be empty");
                }
                else if (!code.equals(mCode))
                {
                    et_verify_code.setError("Incorrect Code");
                }
                else {
                    if (code.equals(mCode)) {

                        if (status.equals("normal")) {
                            verifySucessAPI(et_user_email.getText().toString());
                            dialog.dismiss();
                        }
                        else if (status.contains("@"))
                        {
                            SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_USER_CRED, 0);
                            String USER_FULLNAME = SPref.getStringPref(sharedPreferences, SPref.USER_FULLNAME);
                            String USER_PHONE = SPref.getStringPref(sharedPreferences, SPref.USER_PHONE);
                            String USER_GENDER = SPref.getStringPref(sharedPreferences, SPref.USER_GENDER);
                            String AUTH_FULLNAME = SPref.getStringPref(sharedPreferences, SPref.AUTH_FULLNAME);
                            String AUTH_PHONE = SPref.getStringPref(sharedPreferences, SPref.AUTH_PHONE);
                            String AUTH_EMAIL = SPref.getStringPref(sharedPreferences, SPref.AUTH_EMAIL);
                            String AUTH_GENDER = SPref.getStringPref(sharedPreferences, SPref.AUTH_GENDER);
                            String RELATION = SPref.getStringPref(sharedPreferences, SPref.RELATION);
                            String userID = SPref.getStringPref(sharedPreferences, SPref.USER_ID);



                            updatingUserProfile(USER_FULLNAME, status, USER_PHONE, USER_GENDER,
                                    AUTH_FULLNAME, AUTH_PHONE, AUTH_EMAIL, AUTH_GENDER, RELATION, userID);

                            dialog.dismiss();

                        }
                    }

                }
            }
        });
        rl_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.show();

    }

    private void verifySucessAPI(String email){
        String cancel_req_tag = "register";
        progressbar.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST, APIURLS.VERIFY_EMAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "verify email Response: " + response.toString());
                //hideDialog();
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String mesage = jObj.getString("msg");
                        if (mesage.equals("Email verified")) {
                            Toast.makeText(UpdateProfileActivity.this, "Email Verified Successfully", Toast.LENGTH_LONG).show();
                            SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_USER_CRED, 0);
                            SPref.StrogingSingleString(sharedPreferences, SPref.EVERIFY_STATUS, "1");
                            Intent i = new Intent(UpdateProfileActivity.this, DashboardActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
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
                params.put("useremail", email);
                params.put("userverifystatus", "1");
                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);

    }

    private void showingAlertForChangePhonNumber()
    {
        et_user_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(UpdateProfileActivity.this);
                alert.setTitle("Alert!");
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setMessage("Do you want to change "+et_user_phone.getText().toString() + ". Press 'Yes' Button to update  or press 'No' for cancel dialog");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        shwoingNewPhoneDialog();

                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });
    }

    private void onEmailClickHandler()
    {
        et_user_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(UpdateProfileActivity.this);
                alert.setTitle("Alert!");
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setMessage("Do you want to change "+et_user_email.getText().toString() + ". Press 'Yes' to update email or press 'No' for cancel dialog");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        shwoingNewEmailDialog();

                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });
    }

    private void shwoingNewEmailDialog()
    {
        Dialog dialog = new Dialog(UpdateProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_taking_email);
        EditText et_new_email = (EditText) dialog.findViewById(R.id.et_new_email);
        RelativeLayout bt_send = (RelativeLayout) dialog.findViewById(R.id.bt_send);
        TextView tv_fp = (TextView) dialog.findViewById(R.id.tv_fp);
        TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_detail);
        RelativeLayout rl_cancel = (RelativeLayout) dialog.findViewById(R.id.rl_cancel);
        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        TextView tv_submit = (TextView) dialog.findViewById(R.id.tv_submit);

        tv_fp.setTypeface(custom_font);
        tv_detail.setTypeface(custom_font);
        et_new_email.setTypeface(custom_font2);
        tv_cancel.setTypeface(custom_font2);
        tv_submit.setTypeface(custom_font2);


        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_new_email.getText().toString();
                if (email.length()==0)
                {
                    et_new_email.setError("Should not be empty");
                }else if (!emailValidator(email)){
                    et_new_email.setError("Invalid Email");
                }
                else {
                    dialog.dismiss();
                    callingUpdateEmailAPI(email);
                }

            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.show();

    }

    private void shwoingNewPhoneDialog()
    {
        Dialog dialog = new Dialog(UpdateProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.custome_update_phone);
        EditText et_phone = (EditText) dialog.findViewById(R.id.et_phone);
        RelativeLayout bt_send = (RelativeLayout) dialog.findViewById(R.id.bt_send);
        TextView tv_fp = (TextView) dialog.findViewById(R.id.tv_fp);
        TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_detail);
        RelativeLayout rl_cancel = (RelativeLayout) dialog.findViewById(R.id.rl_cancel);
        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        TextView tv_submit = (TextView) dialog.findViewById(R.id.tv_submit);

        tv_fp.setTypeface(custom_font);
        tv_detail.setTypeface(custom_font);
        et_phone.setTypeface(custom_font2);
        tv_cancel.setTypeface(custom_font2);
        tv_submit.setTypeface(custom_font2);

        //startMobileWithOnlyNumber92(et_phone);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();
                if (phone.length()==0) {

                    et_phone.setError("Should not be empty");
                }
                else if (phone.length()<7)
                {
                    et_phone.setError("Invalid Phone Number");
                }

                else {
                    dialog.dismiss();
                    phone = "+"+phone;
                    SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_USER_CRED, 0);
                    String USER_FULLNAME = SPref.getStringPref(sharedPreferences, SPref.USER_FULLNAME);
                    String USER_EMAIL = SPref.getStringPref(sharedPreferences, SPref.USER_EMAIL);
                    String USER_GENDER = SPref.getStringPref(sharedPreferences, SPref.USER_GENDER);
                    String AUTH_FULLNAME = SPref.getStringPref(sharedPreferences, SPref.AUTH_FULLNAME);
                    String AUTH_PHONE = SPref.getStringPref(sharedPreferences, SPref.AUTH_PHONE);
                    String AUTH_EMAIL = SPref.getStringPref(sharedPreferences, SPref.AUTH_EMAIL);
                    String AUTH_GENDER = SPref.getStringPref(sharedPreferences, SPref.AUTH_GENDER);
                    String RELATION = SPref.getStringPref(sharedPreferences, SPref.RELATION);
                    String userID = SPref.getStringPref(sharedPreferences, SPref.USER_ID);

                    Intent i = new Intent(UpdateProfileActivity.this, PhoneAuthActivity.class);
                    i.putExtra("USER_FULLNAME", USER_FULLNAME);
                    i.putExtra("USER_PHONE", phone);
                    i.putExtra("USER_EMAIL", USER_EMAIL);
                    i.putExtra("USER_GENDER", USER_GENDER);
                    i.putExtra("AUTH_FULLNAME", AUTH_FULLNAME);
                    i.putExtra("AUTH_PHONE", AUTH_PHONE);
                    i.putExtra("AUTH_EMAIL", AUTH_EMAIL);
                    i.putExtra("AUTH_GENDER", AUTH_GENDER);
                    i.putExtra("RELATION", RELATION);
                    i.putExtra("userID", userID);
                    startActivity(i);
                    finish();
                    //callingUpdateEmailAPI(phone);
                }

            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.show();

    }

    private void callingUpdateEmailAPI(String email)
    {
        String cancel_req_tag = "register";
        progressbar.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST, APIURLS.UPDATE_EMAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "update email Response: " + response.toString());
                //hideDialog();
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String mesage = jObj.getString("msg");
                        //Toast.makeText(UpdateProfileActivity.this, "Your Profile Updated Successfully", Toast.LENGTH_LONG).show();
                        String code = jObj.getString("code");
                        shwoingVerifcationCodeDialog(code, email);

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
                params.put("email", email);
                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void imageViewBackClickHandler(){
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
