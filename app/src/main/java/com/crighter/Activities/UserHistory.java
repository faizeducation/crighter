package com.crighter.Activities;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crighter.Adapter.HistoryAdapter;
import com.crighter.Preferences.SPref;
import com.crighter.R;
import com.crighter.URLS.APIURLS;
import com.crighter.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserHistory extends AppCompatActivity {

    RecyclerView rc_view;
    LinearLayoutManager linearLayoutManager;
    ArrayList<HashMap<String, String>> dataList;
    HistoryAdapter mAdapter;
    ProgressBar progressbar;
    TextView tv_history;
    Typeface custom_font;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);

        init();
        onBackImageClickHandler();
    }
    private void init()
    {
        custom_font = Typeface.createFromAsset(getAssets(),  "fonts/neuropol.ttf");

        rc_view = (RecyclerView) findViewById(R.id.rc_view);
        linearLayoutManager = new LinearLayoutManager(UserHistory.this, LinearLayoutManager.VERTICAL, false);
        rc_view.setLayoutManager(linearLayoutManager);
        dataList = new ArrayList<>();
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        tv_history = (TextView) findViewById(R.id.tv_history);

        tv_history.setTypeface(custom_font);

        SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_USER_CRED, 0);
        String userID = SPref.getStringPref(sharedPreferences, SPref.USER_ID);

        callingHistoryAPI(userID);

    }

    private void callingHistoryAPI(String userID)
    {
        // Tag used to cancel the request
        String cancel_req_tag = "history";
        progressbar.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST, APIURLS.USER_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "history Response: " + response.toString());
                //hideDialog();
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e("TAG", "json objec is " + jObj);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String mesage = jObj.getString("msg");
                       JSONArray jsonArray = jObj.getJSONArray("danger_data");
                       if (jsonArray.length()>0)
                       {
                           for (int i=0; i<jsonArray.length() ; i++)
                           {
                               String danger_id = jsonArray.getJSONObject(i).get("danger_id").toString();
                               String img1 = jsonArray.getJSONObject(i).get("img1").toString();
                               String img2 = jsonArray.getJSONObject(i).get("img2").toString();
                               String img3 = jsonArray.getJSONObject(i).get("img3").toString();
                               String img4 = jsonArray.getJSONObject(i).get("img4").toString();
                               String img5 = jsonArray.getJSONObject(i).get("img5").toString();
                               String audio = jsonArray.getJSONObject(i).get("audio").toString();
                               String post_lat = jsonArray.getJSONObject(i).get("post_lat").toString();
                               String post_lng = jsonArray.getJSONObject(i).get("post_lng").toString();
                               String post_created = jsonArray.getJSONObject(i).get("post_created").toString();
                               String user_id = jsonArray.getJSONObject(i).get("user_id").toString();

                               if (!img1.equals("null") && !post_lat.equals("null") && !post_lng.equals("null")) {
                                   HashMap<String, String> map = new HashMap<>();
                                   map.put("danger_id", danger_id);
                                   map.put("img1", img1);
                                   map.put("img2", img2);
                                   map.put("img3", img3);
                                   map.put("img4", img4);
                                   map.put("img5", img5);
                                   map.put("audio", audio);
                                   map.put("post_lat", post_lat);
                                   map.put("post_lng", post_lng);
                                   map.put("post_created", post_created);
                                   map.put("user_id", user_id);

                                   dataList.add(map);
                               }
                           }

                           Collections.reverse(dataList);
                           mAdapter = new HistoryAdapter(UserHistory.this, dataList);
                           rc_view.setAdapter(mAdapter);
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
                Log.e("TAG", "Hisotry fatcching Error: " + error.getMessage());
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
                params.put("user_id", userID);

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

    private void onBackImageClickHandler()
    {
        ImageView tv_back = (ImageView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
