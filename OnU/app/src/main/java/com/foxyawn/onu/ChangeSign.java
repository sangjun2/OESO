package com.foxyawn.onu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChangeSign extends AppCompatActivity {
    ProgressBar progressBar;
    ArrayList<City> cities;
    boolean isProviderChecked;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    User currentUser;
    Spinner citySpinner;
    ArrayAdapter<String> cityAdapter;

    Spinner districtSpinner;
    ArrayAdapter<String> districtAdapter;

    Context mContext;
    LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mContext = this;
        mLayoutInflater = getLayoutInflater();
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if (type.equals("consumer")){
            isProviderChecked = false;
        } else {
            isProviderChecked = true;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_text);
        toolbarTitle.setText("회원가입");

        Button toolbarButton = (Button) findViewById(R.id.toolbar_button);
        toolbarButton.setText("취소");
        toolbarButton.setOnClickListener(new View.OnClickListener() {
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
        DatabaseReference updateRef;

        if (isProviderChecked){
            updateRef = databaseReference.child("users").child("provider");
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
        } else {
            updateRef = databaseReference.child("users").child("consumer");
            consumerButton.setBackgroundResource(R.drawable.type_button_click);
            consumerButton.setTextColor(Color.parseColor("#ffffff"));
            providerButton.setBackgroundResource(R.drawable.border);
            providerButton.setTextColor(Color.parseColor("#000000"));
            regionLayout.setVisibility(View.GONE);
        }

        updateRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User tempUser = dataSnapshot.getValue(User.class);
                if (user.getEmail().equals(tempUser.email)) {
                    currentUser = tempUser;
                    update();
                }
            }

            private void update() {
                emailText.setText(currentUser.getEmail());
                passwordText.setText(currentUser.getPassword());
                passwordRetryText.setText(currentUser.getPassword());
                phoneText.setText(currentUser.getTel());
                nameText.setText(currentUser.getName());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        consumerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "consumer",Toast.LENGTH_LONG).show();
            }
        });

        providerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "provider",Toast.LENGTH_LONG).show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkText(emailText) && checkText(passwordText) && checkText(passwordRetryText) && checkText(nameText) && checkText(phoneText)) {
                    String pw = passwordText.getText().toString();
                    String retryPw = passwordRetryText.getText().toString();
                    if(pw.equals(retryPw)) {
                        FirebaseAuth mAuth;
                        final String email = emailText.getText().toString();
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(ChangeSign.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                User newUser;
                                String userEmail = emailText.getText().toString();
                                String userPassword = passwordText.getText().toString();
                                String userTel = phoneText.getText().toString();
                                String userName = nameText.getText().toString();
                                Info info = new Info();
                                Map<String, Object> userValues;
                                Map<String, Object> childUpdates = new HashMap<String, Object>();

                                if(isProviderChecked) { // provider
                                    String userPlace = citySpinner.getSelectedItem().toString() + " " + districtSpinner.getSelectedItem().toString();
                                    newUser = new User(userEmail, userPassword, userName, userTel, userPlace, info);
                                    userValues = newUser.toMap();
                                    childUpdates.put("/provider/" + user.getUid(), userValues);
                                } else { // consumer
                                    newUser = new User(userEmail, userPassword, userName, userTel, null, info);
                                    userValues = newUser.toMap();
                                    childUpdates.put("/consumer/" + user.getUid(), userValues);
                                }
                                mDatabase.updateChildren(childUpdates);

                                SharedPreferences preferences = getSharedPreferences("Account", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("email", email);
                                if (isProviderChecked) {
                                    editor.putBoolean("place", true);
                                }else{
                                    databaseReference.child("contract").child(user.getUid()).setValue(new Estimation());
                                }
                                editor.commit();

                                CustomDialog customDialog = new CustomDialog();
                                customDialog.getInstance(mContext, mLayoutInflater, R.layout.submit_dialog);
                                customDialog.show("회원가입이 완료되었습니다.", "확인");

                                customDialog.dialogButton1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });
                            }
                        });
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
