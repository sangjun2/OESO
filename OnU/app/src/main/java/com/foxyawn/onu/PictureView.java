package com.foxyawn.onu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class PictureView extends AppCompatActivity {
    private StorageReference mStorageRef;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;
    private static Uri mlmageCaptureUri;
    private static ImageView img,img1, img2,img3,img4;
    public Context mContext;

    public PictureView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view);
        Intent intent = getIntent();
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_notification_pro, container, false);
        img1 = (ImageView) findViewById(R.id.imageView1);
        img2 = (ImageView) findViewById(R.id.imageView2);
        img3 = (ImageView) findViewById(R.id.imageView3);
        img4 = (ImageView) findViewById(R.id.imageView4);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                Drawable temp = getResources().getDrawable(R.drawable.noimage);
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
                Toast.makeText(mContext,"등록되었습니다!",Toast.LENGTH_SHORT).show();
            }
        });

        View.OnClickListener imgClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img = (ImageView)v;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        };
        img1.setOnClickListener(imgClick);
        img2.setOnClickListener(imgClick);
        img3.setOnClickListener(imgClick);
        img4.setOnClickListener(imgClick);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode != RESULT_OK)
            return;

        switch(requestCode)
        {
            case PICK_FROM_ALBUM:
                mlmageCaptureUri = data.getData();
                Log.d("SmartWheel",mlmageCaptureUri.getPath().toString());
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mlmageCaptureUri,"image/*");

                intent.putExtra("outputX",200);
                intent.putExtra("outputY",200);
                intent.putExtra("aspectX",1);
                intent.putExtra("aspectY",1);
                intent.putExtra("scale",true);
                intent.putExtra("return-data",true);
                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;

            case CROP_FROM_IMAGE:
                if(resultCode != RESULT_OK){
                    return ;
                }
                final Bundle extras = data.getExtras();

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmartWheel/"+System.currentTimeMillis()+".jpg";

                if(extras != null)
                {
                    Bitmap photo = extras.getParcelable("data");
                    img.setImageBitmap(photo);
                    storeCropImage(photo,filePath);

                    break;
                }
                File f = new File(mlmageCaptureUri.getPath());
                if(f.exists()){
                    f.delete();
                }
        }
    }
    private void storeCropImage(Bitmap bitmap, String filePath){
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel";
        File directory_SmartWheel = new File(dirPath);
        if(!directory_SmartWheel.exists()){
            directory_SmartWheel.mkdir();
        }
        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try{
            copyFile.createNewFile();
            out=new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);

            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
