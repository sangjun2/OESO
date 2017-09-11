package com.foxyawn.onu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SignupActivity extends AppCompatActivity {
    ProgressBar progressBar;
    ArrayList<City> cities;
    boolean isProviderChecked;

    Spinner citySpinner;
    ArrayAdapter<String> cityAdapter;

    Spinner districtSpinner;
    ArrayAdapter<String> districtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        isProviderChecked = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button cancelButton = (Button) findViewById(R.id.toolbar_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button submitButton = (Button) findViewById(R.id.form_submit);

        final Button consumerButton = (Button) findViewById(R.id.consumer);
        final Button providerButton = (Button) findViewById(R.id.provider);
        final LinearLayout regionLayout = (LinearLayout) findViewById(R.id.regionLayout);

        final EditText emailText = (EditText) findViewById(R.id.signup_email);
        final EditText passwordText = (EditText) findViewById(R.id.signup_password);
        final EditText passwordRetryText = (EditText) findViewById(R.id.signup_retry_password);
        final EditText nameText = (EditText) findViewById(R.id.signup_name);
        final EditText phoneText = (EditText) findViewById(R.id.signup_phone);

        citySpinner = (Spinner) findViewById(R.id.form_city);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        districtSpinner = (Spinner) findViewById(R.id.form_district);

        consumerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProviderChecked = false;

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
                isProviderChecked = true;
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("place");
                cities = new ArrayList<City>();

                consumerButton.setBackgroundResource(R.drawable.border);
                consumerButton.setTextColor(Color.parseColor("#000000"));

                providerButton.setBackgroundResource(R.drawable.type_button_click);
                providerButton.setTextColor(Color.parseColor("#ffffff"));

                progressBar.setVisibility(View.VISIBLE);

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> cityList = new ArrayList<String>();

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.d("snapshot = ", snapshot.getKey());
                            City city = new City();
                            city.setName(snapshot.getKey());
                            cityList.add(snapshot.getKey());
                            District[] districts = new District[(int) snapshot.getChildrenCount()];
                            Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                            int i = 0;
                            while(iterator.hasNext()) {
                                districts[i++] = new District(iterator.next().getValue().toString());
                            }
                            city.setDistricts(districts);
                            cities.add(city);
                        }

                        cityAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
                        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        citySpinner.setAdapter(cityAdapter);
                        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Log.d("position = ", String.valueOf(position));
                                ArrayList<String> districtList = new ArrayList<String>();
                                District[] districts = cities.get(position).getDistricts();
                                for(int i = 0; i < districts.length; i++) {
                                    districtList.add(districts[i].getName());
                                }

                                districtAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, districtList);
                                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                districtSpinner.setAdapter(districtAdapter);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                regionLayout.setVisibility(View.VISIBLE);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkText(emailText) && checkText(passwordText) && checkText(passwordRetryText) && checkText(nameText) && checkText(phoneText)) {
                    String pw = passwordText.getText().toString();
                    String retryPw = passwordRetryText.getText().toString();

                    if(pw.equals(retryPw)) {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                        if(isProviderChecked) { // provider

                        } else { // consumer

                        }
                    } else { // password incorrect
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.anim_slide_right, R.anim.anim_slide_left);
    }

    private boolean checkText(EditText input) {
        String str = input.getText().toString();

        return !str.equals("") && str.length() > 0;
    }
}
