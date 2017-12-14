package com.foxyawn.onu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PictureView extends AppCompatActivity {
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    public Context mContext;
    ImageView img1, img2,img3,img4;
    StorageReference uidfolder;
    StorageReference imagefile1;
    StorageReference imagefile2;
    StorageReference imagefile3;
    StorageReference imagefile4;

    public PictureView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view);
        mContext = getApplicationContext();
        final Button btn = (Button) findViewById(R.id.btn);

        Intent intent = getIntent();
        final String providerUid = intent.getStringExtra("providerUid");

        databaseReference.child("users").child("provider").addChildEventListener(new ChildEventListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(providerUid)) {
                    User user = dataSnapshot.getValue(User.class);
                    final String tel = "tel:" + user.getTel();
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(tel));
                            startActivity(intent);

                        }
                    });
                }

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

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        uidfolder = mStorageRef.child(providerUid);

        img1 = (ImageView) findViewById(R.id.imageView1);
        img2 = (ImageView) findViewById(R.id.imageView2);
        img3 = (ImageView) findViewById(R.id.imageView3);
        img4 = (ImageView) findViewById(R.id.imageView4);

        imagefile1 = uidfolder.child("image1");
        imagefile2 = uidfolder.child("image2");
        imagefile3 = uidfolder.child("image3");
        imagefile4 = uidfolder.child("image4");
        imagefile1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(imagefile1).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(img1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        imagefile2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(imagefile2).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(img2);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        imagefile3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(imagefile3).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(img3);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        imagefile4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(imagefile4).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(img4);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);


    }
}
