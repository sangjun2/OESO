package com.foxyawn.onu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.AutoTransition;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequireLoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText email;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_require_login);

        final TextView loginText = (TextView) findViewById(R.id.login_text);
        Button loginButton = (Button) findViewById(R.id.form_login);
        Button signupButton = (Button) findViewById(R.id.form_signup);

        email = (EditText) findViewById(R.id.input_id);
        password = (EditText) findViewById(R.id.input_password);

        View fullView = (View) findViewById(R.id.fullView);

        final ConstraintLayout mConstraintLayout = (ConstraintLayout) findViewById(R.id.login_layout);
        ConstraintSet constraintSetOriginal = new ConstraintSet();
        final ConstraintSet constraintSetAfter = new ConstraintSet();

        constraintSetOriginal.clone(mConstraintLayout);
        constraintSetAfter.clone(getApplicationContext(), R.layout.activity_require_login_after);

        final Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        loginText.startAnimation(anim);
        fullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginText.clearAnimation();

                Transition transition = new AutoTransition();
                transition.setDuration(500);
                TransitionManager.beginDelayedTransition(mConstraintLayout, transition);
                constraintSetAfter.applyTo(mConstraintLayout);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                if(!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(RequireLoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final String uid = mAuth.getCurrentUser().getUid();
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                mDatabase.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        User user = null;

                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            if(snapshot.getKey().equals(uid)) {
                                                user = snapshot.getValue(User.class);

                                                SharedPreferences preferences = getSharedPreferences("Account", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("email", user.email);
                                                editor.putString("name", user.name);
                                                editor.putString("tel", user.tel);

                                                if(dataSnapshot.getKey().equals("consumer")) {
                                                    editor.putString("type", "consumer");
                                                } else {
                                                    editor.putString("type", "provider");
                                                }

                                                editor.commit();

                                                Intent intent = new Intent(RequireLoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }


                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            } else {
                                Toast.makeText(getApplicationContext(), "로그인 정보가 틀립니다.", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_right, R.anim.anim_slide_left);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
