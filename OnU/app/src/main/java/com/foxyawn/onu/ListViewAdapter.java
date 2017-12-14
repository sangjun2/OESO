package com.foxyawn.onu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private ArrayList<Estimation> listViewItemList = new ArrayList<Estimation>() ;

    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final Estimation listViewItem = listViewItemList.get(position);

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView type = (TextView) convertView.findViewById(R.id.typetext) ;
        TextView place = (TextView) convertView.findViewById(R.id.placetext) ;
        TextView personal = (TextView) convertView.findViewById(R.id.personaltext) ;
        TextView day = (TextView)  convertView.findViewById(R.id.daytext) ;
        Button regist = (Button) convertView.findViewById(R.id.regist);
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("contract").addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Estimation estimation = dataSnapshot.getValue(Estimation.class);
                        if (estimation.getEmail() == listViewItem.getEmail()) {
                            ArrayList<String> a = estimation.getProvider();
                            if (a.get(0).toString().equals("")){
                                a.set(0, user.getUid());
                            }else if (!a.contains((String) user.getUid())){
                                a.add(user.getUid());
                            }else{
                                for (int i=0; i<a.size(); i++){
                                    if (a.get(i).toString().equals(user.getUid())){
                                        a.set(i, user.getUid());
                                        break;
                                    }
                                }
                            }

                            databaseReference.child("contract").child(dataSnapshot.getKey()).child("provider").setValue(a);
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
            }
        });

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득

        // 아이템 내 각 위젯에 데이터 반영
        type.setText(listViewItem.getPlacetype());
        place.setText(listViewItem.getDistrict());
        personal.setText(listViewItem.getPerson());
        day.setText(listViewItem.getDate());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Estimation estimation) {

        listViewItemList.add(estimation);
    }

}
