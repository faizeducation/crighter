package com.crighter.Services;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

/**
 * Created by gold on 9/25/2018.
 */

public class AsyntaskService extends AsyncTask<String, Void, String> {

    private int timer = 30;
    Handler mHandler;

    @Override
    protected String doInBackground(String... params) {

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("TAG", "the value of time is " + timer);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        useHandler();

    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }
    //Thread for starting mainActivity
    private Runnable mRunnableStartMainActivity = new Runnable() {
        @Override
        public void run() {
            Log.d("Handler", " Calls " + timer);
            timer--;
            mHandler = new Handler();
            mHandler.postDelayed(this, 1000);
            if (timer==0) {
                mHandler.removeCallbacks(mRunnableStartMainActivity);
                Log.e("TAG", "the value of time is 123 " + timer);
                timer = 30;
            }

        }
    };
    //handler for the starign activity
    Handler newHandler;
    public void useHandler(){

        newHandler = new Handler();
        newHandler.postDelayed(mRunnableStartMainActivity, 1000);

    }

}