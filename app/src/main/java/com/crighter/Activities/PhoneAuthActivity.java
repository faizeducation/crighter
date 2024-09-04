package com.crighter.Activities;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.crighter.Preferences.SPref;
import com.crighter.R;
import com.crighter.URLS.APIURLS;
import com.crighter.Utilities.CircularSeekBar;
import com.crighter.VolleyLibraryFiles.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    private RelativeLayout mVerifyButton;
    private RelativeLayout mResendButton;

    TextView tv_timer;
    private int timinSecoding = 61;
    private int timeScondReverse = 0;
    CircularSeekBar seekbar;

    //for phone udpate
    String USER_FULLNAME;
    String USER_PHONE;
    String USER_EMAIL;
    String USER_GENDER;
    String AUTH_FULLNAME;
    String AUTH_PHONE;
    String AUTH_EMAIL;
    String AUTH_GENDER;
    String RELATION;
    String userID;
    String PASSWORD;
    boolean isUpdate = false;

    ProgressBar progressbar;
    EditText digist1,digist2,digist3,digist4, digist5, digist6;
    TextView tv_title_text, tv_tag1, tv_tag2;

    TextView tv_resendt;
    TextView tv_verify;

    int indicator = 0;

    boolean codeSend = false;

    private GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
//
//        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/office_square.otf");
//        Typeface custom_font2 = Typeface.createFromAsset(getAssets(),  "fonts/century_gothic.ttf");

//        FirebaseAuth.getInstance().getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        // Assign views

//        tv_resendt = (TextView) findViewById(R.id.tv_resent);
//        tv_resendt.setTypeface(custom_font);
//        tv_verify = (TextView) findViewById(R.id.tv_verify);
//        tv_verify.setTypeface(custom_font);
//
//        mVerifyButton = (RelativeLayout) findViewById(R.id.buttonVerifyPhone);
//        mResendButton = (RelativeLayout) findViewById(R.id.buttonResend);
//        mResendButton.setClickable(false);
//        mResendButton.setEnabled(false);
//        mResendButton.setBackground(getResources().getDrawable(R.drawable.button_riple_disabled));
//        tv_resendt.setTextColor(getResources().getColor(R.color.grey));
//        digist1 = (EditText) findViewById(R.id.digist1);
//        digist2 = (EditText) findViewById(R.id.digist2);
//        digist3 = (EditText) findViewById(R.id.digist3);
//        digist4 = (EditText) findViewById(R.id.digist4);
//        digist5 = (EditText) findViewById(R.id.digist5);
//        digist6 = (EditText) findViewById(R.id.digist6);

//        tv_title_text = (TextView) findViewById(R.id.tv_title_text);
//        tv_tag1 = (TextView) findViewById(R.id.tv_tag1);
//        tv_tag2 = (TextView) findViewById(R.id.tv_tag2);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

////        tv_timer = (TextView) findViewById(R.id.tv_timer);
//
//        tv_title_text.setTypeface(custom_font);
//        tv_tag1.setTypeface(custom_font);
//        tv_tag2.setTypeface(custom_font);
//        digist1.setTypeface(custom_font2);
//        digist2.setTypeface(custom_font2);
//        digist3.setTypeface(custom_font2);
//        digist4.setTypeface(custom_font2);
//        digist5.setTypeface(custom_font2);
//        digist6.setTypeface(custom_font2);
//        tv_timer.setTypeface(custom_font2);
////        TextView tv_verify  = (TextView)  findViewById(R.id.tv_verify);
//        tv_verify.setTypeface(custom_font);


        // Assign click listeners
//        mVerifyButton.setOnClickListener(this);
//        mResendButton.setOnClickListener(this);

        gettingIntentValues();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
//        startGmailSignIn();
        // [END initialize_auth]

        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(USER_EMAIL, PASSWORD)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                            "Registration successful!",
                                            Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar

                            // if the user created intent to login activity
                            verified();
                        }
                        else {

                            // Registration failed
                            Toast.makeText(
                                            getApplicationContext(),
                                            "Registration failed!!"
                                                    + " Please try again later",
                                            Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                        }
                    }
                });

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential credential) {
//                Log.e(TAG, "onVerificationCompleted 1:" + credential);
//                if (!isUpdate) {
//                    if (indicator==0) {
//                        registrationService(USER_FULLNAME, USER_EMAIL, USER_PHONE, USER_GENDER, AUTH_FULLNAME, AUTH_EMAIL, AUTH_PHONE, AUTH_GENDER, RELATION, PASSWORD);
//                    }
//                    indicator = 1;
//                }else {
//                    if (indicator==0) {
//                        updateUserProfileCall(USER_PHONE);
//                    }
//                    indicator = 1;
//                }
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                // This callback is invoked in an invalid request for verification is made,
//                // for instance if the the phone number format is not valid.
//                Log.w(TAG, "onVerificationFailed: "+e.getLocalizedMessage());
//                // [START_EXCLUDE silent]
//                mVerificationInProgress = false;
//                // [END_EXCLUDE]
//                Toast.makeText(PhoneAuthActivity.this, "Code not match", Toast.LENGTH_SHORT).show();
//
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    // Invalid request
//                    // [START_EXCLUDE]
//                    // [END_EXCLUDE]
//                    } else if (e instanceof FirebaseTooManyRequestsException) {
//                    // The SMS quota for the project has been exceeded
//                    // [START_EXCLUDE]
//                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
//                            Snackbar.LENGTH_SHORT).show();
//                    // [END_EXCLUDE]
//                }
//
//                // Show a message and update the UI
//                // [START_EXCLUDE]
//                updateUI(STATE_VERIFY_FAILED);
//                // [END_EXCLUDE]
//            }
//
//            @Override
//            public void onCodeSent(String verificationId,
//                                   PhoneAuthProvider.ForceResendingToken token) {
//                if (!codeSend){
//                Toast.makeText(PhoneAuthActivity.this, "Code has been send to your provided mobile number", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onCodeSent:" + verificationId);
//                // Save verification ID and resending token so we can use them later
//                mVerificationId = verificationId;
//                mResendToken = token;
//
//                // [START_EXCLUDE]
//                // Update UI
//                updateUI(STATE_CODE_SENT);
//                tv_timer.setVisibility(View.VISIBLE);
//                long maxTimeInMilliseconds = 30000;// in your case
//                startTimer(maxTimeInMilliseconds, 1000);
//                //startTiming();
//                mResendButton.setClickable(false);
//                mResendButton.setEnabled(false);
//                mResendButton.setBackground(getResources().getDrawable(R.drawable.button_riple_disabled));
//                tv_resendt.setTextColor(getResources().getColor(R.color.grey));
//
//                    mVerifyButton.setEnabled(true);
//                    mVerifyButton.setEnabled(true);
//                    mVerifyButton.setBackground(getResources().getDrawable(R.drawable.button_ripple));
//                    tv_verify.setTextColor(getResources().getColor(R.color.colorWhite));
//
//                codeSend = true;
//            }
//            }
//        };
        // [END phone_auth_callbacks]

//        tv_timer = (TextView) findViewById(R.id.tv_timer);
//        seekbar = (CircularSeekBar) findViewById(R.id.circularSeekBar1);
//        seekbar.setMax(60);


//        onTextChangeListener();

//        startPhoneNumberVerification(USER_PHONE);
    }

    private void startGmailSignIn() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("960293635897-l0fkqp3vbg5p4rmr1acm39p2kq6todrc.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        resultLauncher.launch(googleSignInClient.getSignInIntent());
    }

    private final ActivityResultLauncher<Intent> resultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                if (signInAccountTask.isSuccessful()) {
                    showToast("Google sign in successful");
                    try {
                        // Initialize sign in account
                        GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                        // Check condition
                        if (googleSignInAccount != null) {
                            // When sign in account is not equal to null initialize auth credential
                            AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                            signInToFirebase(authCredential);
                        }
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                } else {
                    showToast("SignIn Failed");
                    signInAccountTask.addOnFailureListener(e -> Log.e(TAG, "failed: " + e.getLocalizedMessage()));
                }
            });

    private void signInToFirebase(AuthCredential authCredential) {
        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, task -> {
                    // Check condition
                    if (task.isSuccessful()) {
                        // When task is successful redirect to profile activity display Toast
                        showToast("Authentication successful");
                        verified();
                    } else {
                        // When task is unsuccessful display Toast
                        showToast("Authentication Failed :" + task.getException().getMessage());
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(PhoneAuthActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void verified()
    {
        if (!isUpdate) {
            if (indicator==0) {
                registrationService(USER_FULLNAME, USER_EMAIL, USER_PHONE, USER_GENDER, AUTH_FULLNAME, AUTH_EMAIL, AUTH_PHONE, AUTH_GENDER, RELATION, PASSWORD);
            }
            indicator = 1;
        }else {
            if (indicator==0) {
                updateUserProfileCall(USER_PHONE);
            }
            indicator = 1;
        }
    }


    private void gettingIntentValues()
    {
        Intent i = getIntent();
        String userId = i.getStringExtra("userID");
        if (userId!=null) {
            isUpdate = true;
            USER_FULLNAME = i.getStringExtra("USER_FULLNAME");
            USER_PHONE = i.getStringExtra("USER_PHONE");
            Log.e(TAG, "gettingIntentValues: "+USER_PHONE );
            USER_EMAIL = i.getStringExtra("USER_EMAIL");
            USER_GENDER  = i.getStringExtra("USER_GENDER");
            AUTH_FULLNAME = i.getStringExtra("AUTH_FULLNAME");
            AUTH_PHONE = i.getStringExtra("AUTH_PHONE");
            AUTH_EMAIL = i.getStringExtra("AUTH_EMAIL");
            AUTH_GENDER = i.getStringExtra("AUTH_GENDER");
            RELATION = i.getStringExtra("RELATION");
            userID = i.getStringExtra("userID");

        }
        else {
            isUpdate = false;
            USER_FULLNAME = i.getStringExtra("userFullName");
            USER_PHONE = i.getStringExtra("userPhone");
            Log.e(TAG, "gettingIntentValues: "+USER_PHONE );

            USER_EMAIL = i.getStringExtra("userEmail");
            USER_GENDER = i.getStringExtra("userGender");
            AUTH_FULLNAME = i.getStringExtra("authFullname");
            AUTH_EMAIL = i.getStringExtra("authEmail");
            AUTH_PHONE = i.getStringExtra("authPhone");
            AUTH_GENDER = i.getStringExtra("userGenderAuth");
            RELATION = i.getStringExtra("realation");
            PASSWORD = i.getStringExtra("password");

        }
    }

    // [START on_start_check_user]
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//
//        // [START_EXCLUDE]
//        if (mVerificationInProgress) {
//            startPhoneNumberVerification(USER_PHONE);
//        }
//        // [END_EXCLUDE]
//    }
    // [END on_start_check_user]

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
//    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.verifyPhoneNumber(new PhoneAuthOptions.Builder(FirebaseAuth.getInstance()).setTimeout(60L, TimeUnit.SECONDS).setPhoneNumber(phoneNumber).setActivity(this).setCallbacks(mCallbacks).build());
        // [START start_phone_auth]
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNumber,        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                this,               // Activity (for callback binding)
//                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        try {
            Log.e("TAg", "the verificatin code is " + code);
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            // [END verify_with_code]
//            signInWithPhoneAuthCredential(credential);
        }catch (java.lang.IllegalArgumentException e){e.printStackTrace();}
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.e(TAG, "signInWithCredential:success 2");
//
//                            FirebaseUser user = task.getResult().getUser();
//                            // [START_EXCLUDE]
////                            updateUI(STATE_SIGNIN_SUCCESS, user);
//                            // [END_EXCLUDE]
//                            if (!isUpdate) {
//                                registrationService(USER_FULLNAME, USER_EMAIL, USER_PHONE, USER_GENDER, AUTH_FULLNAME, AUTH_EMAIL, AUTH_PHONE, AUTH_GENDER, RELATION, PASSWORD);
//                            }else {
//
//                                updateUserProfileCall(USER_PHONE);
//                            }
//
//
//                        } else {
//                            // Sign in failed, display a message and update the UI
//                            Toast.makeText(PhoneAuthActivity.this, "Please Enter Valid Code", Toast.LENGTH_SHORT).show();
//                            Log.e(TAG, "signInWithCredential:failure", task.getException());
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                // The verification code entered was invalid
//                                // [START_EXCLUDE silent]
//                                // [END_EXCLUDE]
//                            }
//                            // [START_EXCLUDE silent]
//                            // Update UI
////                            updateUI(STATE_SIGNIN_FAILED);
//                            // [END_EXCLUDE]
//                        }
//                    }
//                });
//    }
    // [END sign_in_with_phone]

    private void signOut() {
        mAuth.signOut();
//        updateUI(STATE_INITIALIZED);
    }

//    private void updateUI(int uiState) {
//        updateUI(uiState, mAuth.getCurrentUser(), null);
//    }

//    private void updateUI(FirebaseUser user) {
//        if (user != null) {
//            updateUI(STATE_SIGNIN_SUCCESS, user);
//        } else {
//            updateUI(STATE_INITIALIZED);
//        }
//    }

//    private void updateUI(int uiState, FirebaseUser user) {
//        updateUI(uiState, user, null);
//    }
//
//    private void updateUI(int uiState, PhoneAuthCredential cred) {
//        updateUI(uiState, null, cred);
//    }

//    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
//        switch (uiState) {
//            case STATE_INITIALIZED:
//                // Initialized state, show only the phone number field and start button
//                disableViews(mVerifyButton, mResendButton);
//
//                break;
//            case STATE_CODE_SENT:
//                // Code sent state, show the verification field, the
//                enableViews(mVerifyButton, mResendButton);
//
//                break;
//            case STATE_VERIFY_FAILED:
//                // Verification has failed, show all options
//                enableViews(mVerifyButton, mResendButton);
//
//                break;
//            case STATE_VERIFY_SUCCESS:
//                // Verification has succeeded, proceed to firebase sign in
//                disableViews(mVerifyButton, mResendButton);
//
//
//                // Set the verification text based on the credential
//                if (cred != null) {
//
//                }
//
//                break;
//            case STATE_SIGNIN_FAILED:
//                // No-op, handled by sign-in check
//
//                break;
//            case STATE_SIGNIN_SUCCESS:
//                // Np-op, handled by sign-in check
//                break;
//        }
//
//        if (user == null) {
//            // Signed out
//
//        } else {
//            // Signed in
//
//
//        }
//    }


//    private void enableViews(View... views) {
//        for (View v : views) {
//            v.setEnabled(true);
//        }
//    }
//
//    private void disableViews(View... views) {
//        for (View v : views) {
//            v.setEnabled(false);
//        }
//    }
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.buttonVerifyPhone:
//               String code = "";
//               String f1 = digist1.getText().toString();
//               String f2 = digist2.getText().toString();
//               String f3 = digist3.getText().toString();
//               String f4 = digist4.getText().toString();
//               String f5 = digist5.getText().toString();
//               String f6 = digist6.getText().toString();
//               if (f1.length()==0)
//               {
//                   digist1.setError("Should not be empty");
//               }
//               else if (f2.length()==0){
//                   digist2.setError("Should not be empty");
//               }
//               else if (f3.length()==0){
//                   digist3.setError("Should not be empty");
//               }
//               else if (f4.length()==0){
//                   digist4.setError("Should not be empty");
//               }
//               else if (f5.length()==0){
//                   digist5.setError("Should not be empty");
//               }else if (f6.length()==0){
//                   digist6.setError("Should not be empty");
//               }
//               else {
//                   code = f1+f2+f3+f4+f5+f6;
//                   verifyPhoneNumberWithCode(mVerificationId, code);
//               }
//
//                break;
//            case R.id.buttonResend:
//                resendVerificationCode(USER_PHONE, mResendToken);
//                break;
//        }
//    }


    public void startTimer(final long finish, long tick) {
        TextView tv_timer = (TextView) findViewById(R.id.tv_timer);
        CountDownTimer t = new CountDownTimer(finish, tick) {

            public void onTick(long millisUntilFinished) {
                long remainedSecs = millisUntilFinished / 1000;
                tv_timer.setText("" + (remainedSecs / 60) + ":" + (remainedSecs % 60));// manage it accordign to you
            }

            public void onFinish() {
                tv_timer.setText("00:00:00");
                // Toast.makeText(PhoneAuthActivity.this, "Session Expired Please Try Again", Toast.LENGTH_SHORT).show();
                mResendButton.setClickable(true);
                mResendButton.setEnabled(true);
                mResendButton.setBackground(getResources().getDrawable(R.drawable.button_ripple));
                tv_resendt.setTextColor(getResources().getColor(R.color.colorWhite));
                mVerifyButton.setEnabled(false);
                mVerifyButton.setEnabled(false);
                mVerifyButton.setBackground(getResources().getDrawable(R.drawable.button_riple_disabled));
                tv_verify.setTextColor(getResources().getColor(R.color.grey));
                codeSend = false;
                cancel();

            }
        }.start();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void updateUserProfileCall(String userPhone)
    {
        updatingUserProfile(USER_FULLNAME, USER_EMAIL, userPhone, USER_GENDER, AUTH_FULLNAME, AUTH_EMAIL, AUTH_PHONE, AUTH_GENDER, RELATION, userID);

    }

    //logining user serverice
    private void updatingUserProfile(final String userfullName, final String userEmail, final String userPhone, final String userGendar,
                                     final String authfullName, final String authEmail, final String authPhone, final String authGendar, final String relation, final String userid){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        progressbar.setVisibility(View.VISIBLE);


        StringRequest strReq = new StringRequest(Request.Method.POST, APIURLS.UPDATE_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Update Profile response: " + response.toString());
                //hideDialog();
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String mesage = jObj.getString("msg");
                        if (mesage.equals("Profile updated")) {
                            Toast.makeText(PhoneAuthActivity.this, "Your Profile Updated Successfully", Toast.LENGTH_SHORT).show();
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
                            Intent i = new Intent(PhoneAuthActivity.this, DashboardActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }


                    } else {
                        String errorMsg = jObj.getString("msg " + "Please Login");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                       /* Intent i = new Intent(PhoneAuthActivity.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();*/

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
                        error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void onTextChangeListener()
    {
        digist1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count==1){
                    digist2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digist2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count==1){
                    digist3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digist3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count==1){
                    digist4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digist4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count==1){
                    digist5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digist5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count==1){
                    digist6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        digist6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count==1){
                    mVerifyButton.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

                        Toast.makeText(PhoneAuthActivity.this, "You have register successfully Please login to your account", Toast.LENGTH_SHORT).show();
                        alertDialogForRegistSuccess();
//                        tv_timer.setVisibility(View.GONE);



                    } else {
                        String errorMsg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        finish();
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
                        error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void alertDialogForRegistSuccess()
    {
        Dialog dialog = new Dialog(PhoneAuthActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.register_success_custome_dialog);

        TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_detail);
        RelativeLayout bt_verify = (RelativeLayout) dialog.findViewById(R.id.bt_verify);
        tv_detail.setText("Thank you " + USER_FULLNAME + " you are registered successfully. Please Login to your account");
        bt_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent i = new Intent(PhoneAuthActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();

            }
        });
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.show();
    }

}
