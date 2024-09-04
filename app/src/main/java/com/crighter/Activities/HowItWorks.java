package com.crighter.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.crighter.R;

public class HowItWorks extends AppCompatActivity {

    ImageView iv_crooss;
    WebView video_view;
    ImageView iv_play_video;
    VideoView video_view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_it_works);
        init();
        onCrossClickHandler();
        playVideo();
    }

    private void init()
    {
        iv_crooss = (ImageView) findViewById(R.id.iv_crooss);
        video_view = (WebView) findViewById(R.id.video_view);
        iv_play_video = (ImageView) findViewById(R.id.iv_play_video);
        video_view1 = (VideoView) findViewById(R.id.video_view1);



    }
    private void onCrossClickHandler()
    {
        iv_crooss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void playigYoutubeVideo() {

        final ProgressDialog progressBar = new ProgressDialog(HowItWorks.this);
        progressBar.setMessage("Loading Video...");

        //String frameVideo = "<html><body><br><iframe width=\"385\" height=\"315\" src=\"https://www.youtube.com/embed/AS8FmmQlSOA\" frameborder=\"0\" autoplay=1 \" allowfullscreen></iframe></body></html>";
        String frameVideo = "<div style=\"position:relative;padding-bottom:56.25%;padding-top:30px;height:0;overflow:hidden;\"><iframe style=\"width:100%;height:90%;position:absolute;left:0px;top:0px;overflow:hidden\" frameborder=\"0\" type=\"text/html\" src=\"https://www.dailymotion.com/embed/video/k6jWHJufn3CkuSskjea?autoplay=1\" width=\"100%\" height=\"100%\"   allow=\"autoplay\" allowfullscreen></iframe></div>";
        //String frameVideo = "android.resource://"+getPackageName()+"/"+R.raw.video;
       // video_view1.setVideoURI(Uri.parse(frameVideo));
        //video_view1.start();
        video_view1.setVisibility(View.GONE);


        video_view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = video_view.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //video_view.loadUrl(frameVideo);
        video_view.loadData(frameVideo, "text/html", "utf-8");
        video_view.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!progressBar.isShowing()) {
                    progressBar.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }
        });
    }

    private void playVideo()
    {
        iv_play_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    iv_play_video.setVisibility(View.GONE);
                    playigYoutubeVideo();
                }else
                    {
                        Toast.makeText(HowItWorks.this, "Please connect to internet", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onResume()
    {
        super.onResume();
        video_view.onResume();

    }
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPause()
    {
        super.onPause();
        video_view.onPause();
        video_view1.stopPlayback();
    }
}
