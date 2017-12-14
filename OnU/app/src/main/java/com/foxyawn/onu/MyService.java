package com.foxyawn.onu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyService extends Service {
    NotificationManager Notifi_M;
    //ServiceThread thread;
    Notification Notifi ;
    String mydistrict = null;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<String> array = new ArrayList<String>();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        SharedPreferences preferences = getSharedPreferences("Account", MODE_PRIVATE);
        String type = preferences.getString("type", "");

        if(type.equals("consumer")){
            databaseReference.child("contract").child(user.getUid()).child("provider").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                        Intent intent = new Intent(MyService.this, MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        Notifi = new Notification.Builder(getApplicationContext())
                                .setContentTitle("공급 요청")
                                .setContentText("신청한 요청의 새로운 공급자가 생겼습니다.")
                                .setSmallIcon(R.drawable.icon)
                                .setTicker("알림!!!")
                                .setContentIntent(pendingIntent)
                                .build();

                        //소리추가
                        Notifi.defaults = Notification.DEFAULT_SOUND;
                        //알림 소리를 한번만 내도록
                        Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;
                        //확인하면 자동으로 알림이 제거 되도록
                        Notifi.flags = Notification.FLAG_AUTO_CANCEL;
                        Notifi_M.notify(777, Notifi);
                    }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String st = user.getUid();
            databaseReference.child("users").child("provider").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User us = dataSnapshot.getValue(User.class);
                    mydistrict = us.getPlace();
                    next();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
                public void next(){
                    databaseReference.child("contract").addChildEventListener(new ChildEventListener() {

                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            Estimation es = dataSnapshot.getValue(Estimation.class);
                            if (("대전광역시 "+es.getDistrict()).equals(mydistrict)) {
                                Intent intent = new Intent(MyService.this, MainActivity.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                Notifi = new Notification.Builder(getApplicationContext())
                                        .setContentTitle("새로운 견적신청")
                                        .setContentText("주변에 새로운 견적이 신청되었습니다.")
                                        .setSmallIcon(R.drawable.icon)
                                        .setTicker("알림!!!")
                                        .setContentIntent(pendingIntent)
                                        .build();

                                //소리추가
                                Notifi.defaults = Notification.DEFAULT_SOUND;
                                //알림 소리를 한번만 내도록
                                Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;
                                //확인하면 자동으로 알림이 제거 되도록
                                Notifi.flags = Notification.FLAG_AUTO_CANCEL;
                                Notifi_M.notify(777, Notifi);
                            }
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
                }
            });

        }

        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업

    public void onDestroy() {

    }

}