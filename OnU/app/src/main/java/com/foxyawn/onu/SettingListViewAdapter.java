package com.foxyawn.onu;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sangjun on 2017-09-18.
 */

public class SettingListViewAdapter extends BaseAdapter {

    private String[] list;
    private SharedPreferences preferences;

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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
            }
        }

        return convertView;
    }
}

