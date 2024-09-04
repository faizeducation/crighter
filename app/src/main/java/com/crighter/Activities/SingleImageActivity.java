package com.crighter.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crighter.Adapter.SingleImageAdapter;
import com.crighter.R;

import java.util.ArrayList;

public class SingleImageActivity extends AppCompatActivity {

    RecyclerView rc_view;
    LinearLayoutManager linearLayoutManager;
    ArrayList<String> dataList;
    SingleImageAdapter mAdapter;
    TextView tv_title;
    ImageView tv_back_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);
        init();
        gettingIntentValues();
        imageBackClickHandler();
    }

    private void init() {
        rc_view = (RecyclerView) findViewById(R.id.rc_view);
        linearLayoutManager = new LinearLayoutManager(SingleImageActivity.this, LinearLayoutManager.VERTICAL, false);
        rc_view.setLayoutManager(linearLayoutManager);
        dataList = new ArrayList<>();

        Typeface  custom_font = Typeface.createFromAsset(getAssets(),  "fonts/office_square.otf");

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_back_image = (ImageView) findViewById(R.id.tv_back_image);
        tv_title.setTypeface(custom_font);

    }
    private void gettingIntentValues()
    {
        Intent i = getIntent();
        String img1 = i.getStringExtra("img1");
        String img2 = i.getStringExtra("img2");
        String img3 = i.getStringExtra("img3");
        String img4 = i.getStringExtra("img4");
        String img5 = i.getStringExtra("img5");

        dataList.add(img1);
        dataList.add(img2);
        dataList.add(img3);
        dataList.add(img4);
        dataList.add(img5);

        mAdapter = new SingleImageAdapter(SingleImageActivity.this, dataList);
        rc_view.setAdapter(mAdapter);

    }

    private void imageBackClickHandler(){
        tv_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}