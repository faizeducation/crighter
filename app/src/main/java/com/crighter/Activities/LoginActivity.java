package com.crighter.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.crighter.Utilities.StartUpAndLuncher;
import com.crighter.VolleyLibraryFiles.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout tv_sign_up;
    EditText  et_email, et_password;
    TextView tv_forgot_passowrd;
    RelativeLayout bt_login;
    ProgressBar progressbar;
    private final int ACCESS_FINE_LOCATION = 11;
    private final int CAMERA_PER = 12;
    private final int RECORD_AUDIO_PER = 13;
    private final int STORAGE_PERMISSION = 14;
    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 15;

    TextView tv_sign_up_o, tv_login_o;

    Typeface custom_font;
    Typeface custom_font2;

    RelativeLayout rl_how_it_work;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_USER_CRED, 0);
        String userID = SPref.getStringPref(sharedPreferences, SPref.USER_ID);
        String username = SPref.getStringPref(sharedPreferences, SPref.AUTH_FULLNAME);

        Log.e("TAG", "the current boolean is " + username);
        Log.e("TAG", "the current boolean is 11 " + userID);
        if (!userID.isEmpty() && userID!=null){
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }

        shwoingAlert();
        init();
        //permissions();
        signupClickHandler();
        loginClickHandler();
        forgotPassword();
        onHowitWorkClickHandler();
        startMobileWithOnlyNumber92(et_email);

        //startService(new Intent(getApplicationContext(), LockService.class));


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void permissions()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            cameraPermission();
        }else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (!Settings.canDrawOverlays(this)) {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                            Uri.parse("package:" + getPackageName()));
//                    startActivityForResult(intent, 101);
//                }
//            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            audioRocordingPermission();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            storagePermission();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        permissions();
        smsPermissionDialog();
        locationsPermissionDialog();



    }

    private void init()
    {
        tv_sign_up = (RelativeLayout) findViewById(R.id.tv_sign_up);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_forgot_passowrd = (TextView) findViewById(R.id.tv_forgot_passowrd);
        bt_login = (RelativeLayout) findViewById(R.id.bt_login);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        rl_how_it_work = (RelativeLayout) findViewById(R.id.rl_how_it_work);

        tv_sign_up_o = (TextView) findViewById(R.id.tv_sign_up_o);
        tv_login_o = (TextView) findViewById(R.id.tv_login_o);
        custom_font = Typeface.createFromAsset(getAssets(),  "fonts/office_square.otf");
        custom_font2 = Typeface.createFromAsset(getAssets(),  "fonts/century_gothic.ttf");


        tv_sign_up_o.setTypeface(custom_font);
        tv_login_o.setTypeface(custom_font);
        tv_forgot_passowrd.setTypeface(custom_font);
        et_email.setTypeface(custom_font2);
        et_password.setTypeface(custom_font2);

    }

    private void signupClickHandler()
    {
        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActiity.class));
                //Intent i = new Intent(LoginActivity.this, PhoneAuthActivity.class);
                // startActivity(i);
                // finish();
            }
        });
    }

    private void loginClickHandler()
    {
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                if (email.length()==0){
                    et_email.setError("Please Enter Email");
                }
                else if(email.length()>3){
                    if (email.startsWith("+")){
                        if (email.length()<7){
                            et_email.setError("Invalid Mobile Number");
                        }
                        else if (password.length()<5){
                            et_password.setError("should be more than 5 cherecters");
                        }
                        else {
                            //calling login server here
                            Log.e("TAG", "the text from field is " + email);
                            Log.e("TAG", "the password is " + password);
                            loginingUserService(email, password);
                        }
                    }
                    else {
                        if (!emailValidator(email)){
                            et_email.setError("Invalid Email");
                        }
                        else if (password.length()<5){
                            et_password.setError("should be more than 5 cherecters");
                        }
                        else {
                            //calling login server here
                            Log.e("TAG", "the text from field is " + email);
                            Log.e("TAG", "the password is " + password);

                            loginingUserService(email, password);
                        }
                    }
                }
            }
        });
    }

    private void forgotPassword()
    {
        tv_forgot_passowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //caliing forgot password screen

                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.forgot_password_mobile_email_dialog);
                final EditText etForgotPass = (EditText) dialog.findViewById(R.id.et_forgot_pass_email);
                final TextView tv_fp = (TextView) dialog.findViewById(R.id.tv_fp);
                final TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_detail);
                final TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
                final TextView tv_submit = (TextView) dialog.findViewById(R.id.tv_submit);
                final RelativeLayout rl_cancel = (RelativeLayout) dialog.findViewById(R.id.rl_cancel);
                Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/office_square.otf");
                Typeface custom_font2 = Typeface.createFromAsset(getAssets(),  "fonts/century_gothic.ttf");
                tv_fp.setTypeface(custom_font);
                tv_detail.setTypeface(custom_font);
                tv_fp.setTypeface(custom_font);
                tv_cancel.setTypeface(custom_font);
                tv_submit.setTypeface(custom_font);
                etForgotPass.setTypeface(custom_font2);

                RelativeLayout btForgotPassSubmit = (RelativeLayout) dialog.findViewById(R.id.bt_submit_for_forgot_pass);
                startMobileWithOnlyNumber92(etForgotPass);
                btForgotPassSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String textFromEt = etForgotPass.getText().toString();
                        if (textFromEt.startsWith("+")){
                            if (textFromEt.length()<7){
                                etForgotPass.setError("Invalid phone number");
                                Toast.makeText(LoginActivity.this, "Please enter valid number", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                dialog.dismiss();
                                Log.e("TAG", "the use text is " + textFromEt);
                                forgotPassword(textFromEt);
                            }

                        }
                        else if (!textFromEt.startsWith("+")){
                            if (!emailValidator(textFromEt)){
                                etForgotPass.setError("Invalid email address");
                                Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                            }else {
                                dialog.dismiss();
                                Log.e("TAG", "the use text is " + textFromEt);
                                forgotPassword(textFromEt);
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
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                dialog.setCancelable(false);
                dialog.show();

            }
        });
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
    private void loginingUserService(final String usertext, final String userphone){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        progressbar.setVisibility(View.VISIBLE);


        StringRequest strReq = new StringRequest(Request.Method.POST, APIURLS.LOGIN_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Login Response: " + response.toString());
                //hideDialog();
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e("TAG", "json objec is " + jObj);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String mesage = jObj.getString("msg");

                        String userdata =  jObj.getString("userdata");
                        JSONObject innerObnject = new JSONObject(userdata);
                        String user_id =  innerObnject.getString("user_id");
                        String userfullname =  innerObnject.getString("userfullname");
                        String userphone =  innerObnject.getString("userphone");
                        String useremail =  innerObnject.getString("useremail");
                        String usergender =  innerObnject.getString("usergender");
                        String authfullname =  innerObnject.getString("authfullname");
                        String authphone =  innerObnject.getString("authphone");
                        String authemail =  innerObnject.getString("authemail");
                        String authgender =  innerObnject.getString("authgender");
                        String realtionwithauth =  innerObnject.getString("realtionwithauth");
                        String userverifystatus = innerObnject.getString("userverifystatus");

                        Log.e("TAG", "user_id " + user_id);
                        Log.e("TAG", "user userfullname " + userfullname);
                        Log.e("TAG", "user userphone " + userphone);
                        Log.e("TAG", "user useremail " + useremail);
                        Log.e("TAG", "user usergender " + usergender);
                        Log.e("TAG", "user authfullname " + authfullname);
                        Log.e("TAG", "user authphone " + authphone);
                        Log.e("TAG", "user authemail " + authemail);
                        Log.e("TAG", "user authgender " + authgender);
                        Log.e("TAG", "user realtionwithauth " + realtionwithauth);

                        Toast.makeText(LoginActivity.this, "Sign In Successfully", Toast.LENGTH_SHORT).show();
                        //adding data to preff
                        SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_USER_CRED, 0);
                        SPref.StoreStringPrefAll(sharedPreferences, user_id, userfullname, userphone, useremail, usergender, authfullname, authphone, authemail, authgender, realtionwithauth, userverifystatus);
                        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();

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
                Log.e("TAG", "Registration Error: " + error.getLocalizedMessage());
                if (error.getMessage() != null)
                {
                    Toast.makeText(getApplicationContext(),
                            error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                //hideDialog();
                progressbar.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();

                params.put("usercredentials", usertext);
                params.put("userpassword", userphone);
                SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_USER_CRED, 0);
                String FUID = SPref.getStringPref(sharedPreferences, SPref.FIREBASE_KEY);
                params.put("userudid", FUID);

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

    //forgot_password user serverice
    private void forgotPassword(final String usertext){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        progressbar.setVisibility(View.VISIBLE);


        StringRequest strReq = new StringRequest(Request.Method.POST, APIURLS.FORGOT_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "forgotpassword Response: " + response.toString());
                //hideDialog();
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e("TAG", "json objec is " + jObj);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String mesage = jObj.getString("msg");
                        if (mesage.contains("Verification Code")){
                            final String code = jObj.getString("code");
                            final Dialog dialog = new Dialog(LoginActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.setContentView(R.layout.forgot_password_code);
                            final EditText et_verification_code = (EditText) dialog.findViewById(R.id.et_verification_code);
                            final RelativeLayout bt_submit_for_forgot_pass = (RelativeLayout) dialog.findViewById(R.id.bt_submit_for_forgot_pass);
                            TextView tv_fp = (TextView) dialog.findViewById(R.id.tv_fp);
                            TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_detail);
                            RelativeLayout rl_cancel = (RelativeLayout) dialog.findViewById(R.id.rl_cancel);
                            TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
                            TextView tv_submit = (TextView) dialog.findViewById(R.id.tv_submit);
                            tv_fp.setTypeface(custom_font);
                            tv_detail.setTypeface(custom_font);
                            et_verification_code.setTypeface(custom_font2);
                            tv_cancel.setTypeface(custom_font2);
                            tv_submit.setTypeface(custom_font2);

                            tv_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            bt_submit_for_forgot_pass.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String mCode = et_verification_code.getText().toString();

                                    Log.e("Tag", "the code is here " + mCode);

                                    if (mCode.length()==0)
                                    {
                                        et_verification_code.setError("Should not be empty");
                                    }
                                    else if (!mCode.matches(code))
                                    {
                                        et_verification_code.setError("Code Not Match");
                                    }
                                    else {
                                        dialog.dismiss();
                                        dialogSettingNewPassword(usertext);
                                    }
                                }
                            });

                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                            dialog.setCancelable(false);
                            dialog.show();
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

                params.put("usercredentials", usertext);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }//end of forgotpassword service

    //reset password user serverice
    private void resetPasswored(final String usertext, final String password){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        progressbar.setVisibility(View.VISIBLE);


        StringRequest strReq = new StringRequest(Request.Method.POST, APIURLS.UPDATE_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "update password Response: " + response.toString());
                //hideDialog();
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e("TAG", "json objec is " + jObj);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String mesage = jObj.getString("msg");

                        Toast.makeText(LoginActivity.this, "Password reseted please login", Toast.LENGTH_LONG).show();

                    } else {
                        String errorMsg = jObj.getString("msg");
                        //Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
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

                params.put("usercredentials", usertext);
                params.put("userpassword", password);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }//end of update password service

    public void startMobileWithOnlyNumber92(final EditText et_email)
    {

        et_email.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();


                if(x.startsWith("0"))
                {

                    Toast.makeText(LoginActivity.this, "Please enter your number starting with country code(e.g +921234567890) or email", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }

                if (x.startsWith("1")){

                    Toast.makeText(LoginActivity.this, "Please enter your number starting with country code(e.g +921234567890) or email", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }
                if (x.startsWith("2")){

                    Toast.makeText(LoginActivity.this, "Please enter your number starting with country code(e.g +921234567890) or email", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }
                if (x.startsWith("3")){
                    Toast.makeText(LoginActivity.this, "Please enter your number starting with country code(e.g +921234567890) or email", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }

                if (x.startsWith("4")){

                    Toast.makeText(LoginActivity.this, "Please enter your number starting with country code(e.g +921234567890) or email", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }
                if (x.startsWith("5")){

                    Toast.makeText(LoginActivity.this, "Please enter your number starting with country code(e.g +921234567890) or email", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }
                if (x.startsWith("6")){

                    Toast.makeText(LoginActivity.this, "Please enter your number starting with country code(e.g +921234567890) or email", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }
                if (x.startsWith("7")){

                    Toast.makeText(LoginActivity.this, "Please enter your number starting with country code(e.g +921234567890) or email", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }
                if (x.startsWith("8")){

                    Toast.makeText(LoginActivity.this, "Please enter your number starting with country code(e.g +921234567890) or email", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }
                if (x.startsWith("9")){

                    Toast.makeText(LoginActivity.this, "Please enter your number starting with country code(e.g +921234567890) or email", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }

                if (x.startsWith("+")){
                    if (x.length()==15){
                        //doctorSignInEmail.setText(x);
                        et_email.setFilters(new InputFilter[] {new InputFilter.LengthFilter(15)});

                    }
                }
                else {
                    et_email.setFilters(new InputFilter[] {new InputFilter.LengthFilter(120)});
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void locationPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void storagePermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE }, STORAGE_PERMISSION);
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void cameraPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PER);
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void audioRocordingPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PER);
            return;
        }
    }

    private void dialogSettingNewPassword(final String usertext)
    {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.set_new_password);
        final EditText et_set_password = (EditText) dialog.findViewById(R.id.et_set_password);
        final EditText et_set_password_again = (EditText) dialog.findViewById(R.id.et_set_password_again);
        RelativeLayout bt_reset_save = (RelativeLayout) dialog.findViewById(R.id.bt_reset_save);

        TextView tv_fp = (TextView) dialog.findViewById(R.id.tv_fp);
        TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_detail);
        RelativeLayout rl_cancel = (RelativeLayout) dialog.findViewById(R.id.rl_cancel);
        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        TextView tv_submit = (TextView) dialog.findViewById(R.id.tv_submit);
        tv_fp.setTypeface(custom_font);
        tv_detail.setTypeface(custom_font);
        et_set_password.setTypeface(custom_font2);
        et_set_password_again.setTypeface(custom_font2);
        tv_cancel.setTypeface(custom_font2);
        tv_submit.setTypeface(custom_font2);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        bt_reset_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = et_set_password.getText().toString();
                String password2 = et_set_password_again.getText().toString();
                if (password1.length()==0){
                    et_set_password.setError("Should not be empty");
                }
                else if (password2.length()==0){
                    et_set_password_again.setError("Should not be empty");
                }
                else if (!password1.matches(password2)){
                    Toast.makeText(LoginActivity.this, "Password not matched", Toast.LENGTH_SHORT).show();
                }
                else {

                    dialog.dismiss();
                    resetPasswored(usertext, password1);

                }
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.setCancelable(false);
        dialog.show();
    }
    private void shwoingAlert()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_NAME_FOR_ALERT, 0);
        boolean isShowed = SPref.gettingIsDialogShown(sharedPreferences);
        Log.e("TAG", "the current boolean is " + isShowed);
        if (!isShowed)
        {
            StartUpAndLuncher.checkingDeviceForOPPO(LoginActivity.this);
        }

    }
    public void requestpermisson(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }else{         //already has permission granted

        }
    }
    private void onHowitWorkClickHandler()
    {
        rl_how_it_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, HowItWorks.class));
            }
        });
    }

    private void smsPermissionDialog()
    {
        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custome_permission_dialog);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_detail);
        RelativeLayout rl_cancel = (RelativeLayout) dialog.findViewById(R.id.rl_cancel);
        RelativeLayout bt_submit_for_forgot_pass = (RelativeLayout) dialog.findViewById(R.id.bt_submit_for_forgot_pass);
        tv_detail.setText(getResources().getString(R.string.sms_permission_description));
        rl_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bt_submit_for_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        requestpermisson();
                    }
                }
            }
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        SharedPreferences sharedPreferences  = getSharedPreferences("sms_permission", 0);
        if (!dialog.isShowing()) {
            boolean isDialogShown = sharedPreferences.getBoolean("pp", false);
            if (!isDialogShown){
                dialog.show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("pp", true);
                editor.commit();
            }
        }
    }

    private void locationsPermissionDialog()
    {
        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custome_permission_dialog);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_detail);
        RelativeLayout rl_cancel = (RelativeLayout) dialog.findViewById(R.id.rl_cancel);
        RelativeLayout bt_submit_for_forgot_pass = (RelativeLayout) dialog.findViewById(R.id.bt_submit_for_forgot_pass);
        tv_title.setText(getResources().getString(R.string.location_permission_title));
        tv_detail.setText(getResources().getString(R.string.why_location_permission_require_details));
        rl_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        bt_submit_for_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationPermission();
                }
            }
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        SharedPreferences sharedPreferences  = getSharedPreferences("location_permission", 0);
        if (!dialog.isShowing()) {
            boolean isDialogShown = sharedPreferences.getBoolean("location", false);
            if (!isDialogShown){
                dialog.show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("location", true);
                editor.commit();
            }
        }
    }
}
