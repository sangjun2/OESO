package com.foxyawn.onu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;


public class MainActivity extends AppCompatActivity implements MainFragment_pro.OnFragmentInteractionListener,ChattingFragment.OnFragmentInteractionListener, NotificationFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    FirebaseUser user;
    String type;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (type.equals("consumer")) {
                switch (item.getItemId()) {
                    case R.id.navigation_menu1:
                        fragmentTransaction.replace(R.id.content, new MainFragment());
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_menu2:
                        fragmentTransaction.replace(R.id.content, new ChattingFragment());
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_menu3:
                        fragmentTransaction.replace(R.id.content, new NotificationFragment());
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_menu4:
                        fragmentTransaction.replace(R.id.content, new SettingFragment());
                        fragmentTransaction.commit();
                        return true;
                }
            }else if(type.equals("provider")){
            switch (item.getItemId()) {
                case R.id.navigation_menu1:
                    fragmentTransaction.replace(R.id.content, new MainFragment_pro());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_menu2:
                    fragmentTransaction.replace(R.id.content, new ChattingFragment_pro());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_menu3:
                    fragmentTransaction.replace(R.id.content, new NotificationFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_menu4:
                    fragmentTransaction.replace(R.id.content, new SettingFragment());
                    fragmentTransaction.commit();
                    return true;
            }
}
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("Account", MODE_PRIVATE);
        String email = preferences.getString("email", null);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(email == null) {
            Intent intent = new Intent(MainActivity.this, RequireLoginActivity.class);
            startActivity(intent);
            finish();
        }

        type = preferences.getString("type", "");
        Log.d("type==", type);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(type.equals("consumer")) {
            fragmentTransaction.replace(R.id.content, new MainFragment());
        } else {
            fragmentTransaction.replace(R.id.content, new MainFragment_pro());
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentTransaction.commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
