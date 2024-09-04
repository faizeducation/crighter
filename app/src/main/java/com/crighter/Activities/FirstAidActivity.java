package com.crighter.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.crighter.R;
import com.github.barteksc.pdfviewer.PDFView;

public class FirstAidActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid);

        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset("first_aid_report.pdf")
                .load();
    }
}