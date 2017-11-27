package com.foxyawn.onu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

<<<<<<< HEAD
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, ApplyFragment.OnFragmentInteractionListener, NotificationFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener {
=======
public class MainActivity extends AppCompatActivity implements ChattingFragment.OnFragmentInteractionListener, NotificationFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener {
>>>>>>> bd0831bb88bc6ac1e1af86fff02e25d020a24263
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

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

        if(email == null) {
            Intent intent = new Intent(MainActivity.this, RequireLoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            boolean requirePlace = preferences.getBoolean("place", false);
            if(requirePlace) {
                SharedPreferences tempPreferences = getSharedPreferences("temp", MODE_PRIVATE);
                boolean temp = tempPreferences.getBoolean("place", false);
                if(!temp) {
                    Intent intent = new Intent(MainActivity.this, RegisterPlaceActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                }
            }
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, new MainFragment());
        fragmentTransaction.commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences tempPreferences = getSharedPreferences("temp", MODE_PRIVATE);

        SharedPreferences.Editor editor = tempPreferences.edit();
        editor.putBoolean("place", false);

        editor.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
