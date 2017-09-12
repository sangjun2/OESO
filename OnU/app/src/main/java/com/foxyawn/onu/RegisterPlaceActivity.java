package com.foxyawn.onu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterPlaceActivity extends AppCompatActivity {
    Context mContext;
    LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_place);

        mContext = this;
        mLayoutInflater = getLayoutInflater();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_text);
        toolbarTitle.setText("장소 등록하기");

        Button toolbarButton = (Button) findViewById(R.id.toolbar_button);
        toolbarButton.setText("다음에 하기");
        toolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomDialog dialog = new CustomDialog();
                dialog.getInstance(mContext, mLayoutInflater, R.layout.twobutton_dialog);
                dialog.show("장소를 등록하지 않으면 신청을 할 수 없습니다.", "취소", "확인");
                dialog.dialogButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.dialogButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences preferences = getSharedPreferences("temp", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("place", true);
                        editor.commit();

                        Intent intent = new Intent(RegisterPlaceActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}
