package com.foxyawn.onu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingListViewAdapter extends BaseAdapter {

    private String[] list;
    private SharedPreferences preferences;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    public SettingListViewAdapter(SharedPreferences preferences, String[] list) {
        this.preferences = preferences;
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.length + 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return this.list[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if(convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(position == 0) {
                convertView = inflater.inflate(R.layout.setting_profile, parent, false);
                TextView profileName = (TextView) convertView.findViewById(R.id.profile_email);
                TextView profileEmail = (TextView) convertView.findViewById(R.id.profile_name);

                profileName.setText(preferences.getString("name", "이름") + "님 환영합니다.");
                profileEmail.setText(preferences.getString("email", "이메일"));

            } else {
                convertView = inflater.inflate(R.layout.setting_list, parent, false);

                TextView settingText = (TextView) convertView.findViewById(R.id.setting_text);
                settingText.setText(this.list[position - 1]);
                if(this.list[position-1].equals("푸시알림 설정")){
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
                            alert_confirm.setMessage("푸시알림을 받으시겠습니까?").setCancelable(false).setPositiveButton("설정", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context,"설정되었습니다.",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(context, MyService.class);
                                    context.startService(intent);
                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context,"취소되었습니다.",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(context,MyService.class);
                                    context.stopService(intent);
                                }
                            });
                            AlertDialog alert = alert_confirm.create();
                            alert.show();
                        }
                    });

                }
                else if(this.list[position-1].equals("개인정보 변경")) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context,"개인정보 변경",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context,ChangeSign.class);
                            intent.putExtra("type", preferences.getString("type", ""));
                            context.startActivity(intent);
                        }
                    });
                }
                else if(this.list[position - 1].equals("로그아웃")) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences preferences = context.getSharedPreferences("Account", context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.commit();
                            Intent intent = new Intent(context,MyService.class);
                            context.stopService(intent);
                            intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        }
                    });
                }
            }
        }

        return convertView;
    }
}

