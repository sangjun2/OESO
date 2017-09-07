package com.foxyawn.onu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignupActivity extends AppCompatActivity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button cancelButton = (Button) findViewById(R.id.toolbar_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final Button consumerButton = (Button) findViewById(R.id.consumer);
        final Button providerButton = (Button) findViewById(R.id.provider);
        final LinearLayout regionLayout = (LinearLayout) findViewById(R.id.regionLayout);

        consumerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumerButton.setBackgroundResource(R.drawable.type_button_click);
                consumerButton.setTextColor(Color.parseColor("#ffffff"));

                providerButton.setBackgroundResource(R.drawable.border);
                providerButton.setTextColor(Color.parseColor("#000000"));

                regionLayout.setVisibility(View.GONE);
            }
        });

        providerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumerButton.setBackgroundResource(R.drawable.border);
                consumerButton.setTextColor(Color.parseColor("#000000"));

                providerButton.setBackgroundResource(R.drawable.type_button_click);
                providerButton.setTextColor(Color.parseColor("#ffffff"));

                regionLayout.setVisibility(View.VISIBLE);
            }
        });

        Spinner citySpinner = (Spinner) findViewById(R.id.form_city);
        Spinner districtSpinner = (Spinner) findViewById(R.id.form_district);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        LoadAdressTask task = new LoadAdressTask();
        task.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.anim_slide_right, R.anim.anim_slide_left);
    }

    private class LoadAdressTask extends AsyncTask {
        public URL url;
        public HttpURLConnection connection;
        private final String authKey = "64ba561a79a2fff0501ca7";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                url = new URL("http://openapi.nsdi.go.kr/nsdi/eios/service/rest/AdmService/admCodeList.json?&authkey=" + authKey);
                connection = (HttpURLConnection) url.openConnection();

                if(connection.getResponseCode() == 200) {
                    InputStream response = connection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(response, "UTF-8");
                } else {

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
