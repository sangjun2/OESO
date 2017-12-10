package com.foxyawn.onu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class PictureView extends AppCompatActivity {
    private StorageReference mStorageRef;
    private ImageView img,img1, img2,img3,img4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view);
        Intent intent = getIntent();
        String providerUid = intent.getStringExtra("providerUid");
        Toast.makeText(this,providerUid,Toast.LENGTH_LONG).show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.getUid();
        img1.setDrawingCacheEnabled(true);
        img1.buildDrawingCache();
        img2.setDrawingCacheEnabled(true);
        img2.buildDrawingCache();
        img3.setDrawingCacheEnabled(true);
        img3.buildDrawingCache();
        img4.setDrawingCacheEnabled(true);
        img4.buildDrawingCache();

        Drawable temp = this.getResources().getDrawable(R.drawable.noimage);
        Drawable temp1 = img1.getDrawable();
        Drawable temp2 = img2.getDrawable();
        Drawable temp3 = img3.getDrawable();
        Drawable temp4 = img4.getDrawable();
        Bitmap noBitmap = ((BitmapDrawable)temp).getBitmap();
        Bitmap Bitmap1 = ((BitmapDrawable)temp1).getBitmap();
        Bitmap Bitmap2 = ((BitmapDrawable)temp2).getBitmap();
        Bitmap Bitmap3 = ((BitmapDrawable)temp3).getBitmap();
        Bitmap Bitmap4 = ((BitmapDrawable)temp4).getBitmap();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference uidfolder = mStorageRef.child(user.getUid());
        StorageReference imagefile1 = uidfolder.child("image1");
        StorageReference imagefile2 = uidfolder.child("image2");
        StorageReference imagefile3 = uidfolder.child("image3");
        StorageReference imagefile4 = uidfolder.child("image4");
        ByteArrayOutputStream baos;
        UploadTask uploadTask;
        byte[] photodata;
        if(noBitmap != Bitmap1){
            baos = new ByteArrayOutputStream();
            Bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            photodata = baos.toByteArray();
            uploadTask = imagefile1.putBytes(photodata);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(),"실패",Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Toast.makeText(getContext(),"성공",Toast.LENGTH_LONG).show();
                }
            });
        }
        if(noBitmap != Bitmap2){
            baos = new ByteArrayOutputStream();
            Bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            photodata = baos.toByteArray();
            uploadTask = imagefile2.putBytes(photodata);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(),"실패",Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Toast.makeText(getContext(),"성공",Toast.LENGTH_LONG).show();
                }
            });
        }
        if(noBitmap != Bitmap3){
            baos = new ByteArrayOutputStream();
            Bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            photodata = baos.toByteArray();
            uploadTask = imagefile3.putBytes(photodata);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(),"실패",Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Toast.makeText(getContext(),"성공",Toast.LENGTH_LONG).show();
                }
            });
        }
        if(noBitmap != Bitmap4){
            baos = new ByteArrayOutputStream();
            Bitmap4.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            photodata = baos.toByteArray();
            uploadTask = imagefile4.putBytes(photodata);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(),"실패",Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Toast.makeText(getContext(),"성공",Toast.LENGTH_LONG).show();
                }
            });
        }
        Toast.makeText(this,"등록되었습니다!",Toast.LENGTH_SHORT).show();
    }
}
