package com.crighter.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.crighter.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EmergencyActivity extends AppCompatActivity {

    Button btnPolice, btnEdhi,btnRailway,btnAid;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        btnPolice = findViewById(R.id.btn_police);
        btnEdhi = findViewById(R.id.btn_ambulance);
        btnRailway = findViewById(R.id.btn_railway);
        btnAid = findViewById(R.id.btn_aid);
        imageView = findViewById(R.id.tv_back);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialerWithNumber("15");
            }
        });
        btnEdhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialerWithNumber("+922132413232");
            }
        });
        btnRailway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialerWithNumber("117");
            }
        });
        btnAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(EmergencyActivity.this,FirstAidActivity.class));
            }
        });
    }
    private File copyFileFromAssets(String fileName) throws IOException {
        InputStream assetInputStream = getAssets().open(fileName);
        File outFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
        OutputStream outputStream = new FileOutputStream(outFile);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = assetInputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        assetInputStream.close();
        outputStream.close();

        return outFile;
    }

    private void openDocFile(File file) {
        Uri fileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/msword");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // No application found to open .doc files
            // You can show a message to the user
        }
    }
    public void openDialerWithNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}