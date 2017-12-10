package com.foxyawn.onu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class PictureView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view);
        Intent intent = getIntent();
        String providerUid = intent.getStringExtra("providerUid");
        Toast.makeText(this,providerUid,Toast.LENGTH_LONG).show();
    }
}
