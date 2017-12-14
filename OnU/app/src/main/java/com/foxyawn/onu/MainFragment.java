package com.foxyawn.onu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MainFragment extends Fragment {
    public Context mContext;
    Estimation estimation;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    public MainFragment() {
        // Required empty public constructor
         estimation = new Estimation();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        final ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.expanded_menu);
        Button applyButton = (Button) view.findViewById(R.id.apply_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "공간유형 : "+estimation.getPlacetype()+"\n"+"구 : "+estimation.getDistrict()+"\n"+"인원 : "+estimation.getPerson()+"\n"+"날짜 : "+estimation.getDate(), Toast.LENGTH_LONG).show();
                databaseReference = FirebaseDatabase.getInstance().getReference();
                user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(mContext, user.getUid(), Toast.LENGTH_LONG).show();
                estimation.setEmail(user.getEmail());

                databaseReference.child("contract").child(user.getUid()).setValue(estimation);
            }
        });


        ArrayList<Item> groupList = new ArrayList<>();
        final ArrayList<String> typeList = new ArrayList<>();
        databaseReference.child("type").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                 String typeString = dataSnapshot.getKey();
                 typeList.add(typeString);
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
        Item item = new Item("공간유형", typeList);
        groupList.add(item);
        final ArrayList<String> districtList = new ArrayList<>();
        databaseReference.child("place").child("대전광역시").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String typeString = (String) dataSnapshot.getValue();
                districtList.add(typeString);
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
        groupList.add(new Item("구", districtList));

        groupList.add(new Item("인원", null));
        groupList.add(new Item("날짜", null));

        final ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(groupList);
        listView.setAdapter(adapter);

        // 그룹 클릭
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        // 차일드 클릭
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return true;
            }
        });

        // 그룹 닫힘
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // 그룹 열림
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0 ; i < adapter.getGroupCount(); i++){
                }
            }
        });

        return view;
    }

    public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
        public ArrayList<Item> list;

        public ExpandableListViewAdapter(ArrayList<Item> list) {
            this.list = list;
        }

        @Override
        public int getGroupCount() {
            return list.size();
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return list.get(groupPosition).getChildList().size() == 0 ? 1 : list.get(groupPosition).getChildList().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return list.get(groupPosition).getTitle();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return list.get(groupPosition).getChildList().get(childPosition).toString();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_item, null);

            TextView title = (TextView) view.findViewById(R.id.group_title);
            title.setText(list.get(groupPosition).getTitle());
            return view;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, final View convertView, final ViewGroup parent) {
            View view = null;

            if(groupPosition == 0 || groupPosition == 1) { //공간유형 , 구
                view = LayoutInflater.from(mContext).inflate(R.layout.normal_child_item, null);
                final Button button = (Button) view.findViewById(R.id.normal_child_item);

                button.setText(list.get(groupPosition).getChildList().get(childPosition));
                button.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View v) {
                        if (groupPosition == 0){
                            estimation.setPlacetype(button.getText().toString());
                            button.setBackgroundColor(R.color.selected);
                        }
                        else{
                            estimation.setDistrict(button.getText().toString());
                            button.setBackgroundColor(R.color.selected);
                        }
                    }
                });

            } else if(groupPosition == 2) { // 인원
                view = LayoutInflater.from(mContext).inflate(R.layout.number_child_item, null);
                final EditText editText = (EditText) view.findViewById(R.id.number_child_item);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                final Button button = (Button) view.findViewById(R.id.number_child_bt);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        estimation.setPerson(editText.getText().toString());
                    }
                });
            } else if(groupPosition == 3) { // 날짜
                view = LayoutInflater.from(mContext).inflate(R.layout.date_child_item, null);
                final EditText editText = (EditText) view.findViewById(R.id.number_child_item);
                editText.setInputType(InputType.TYPE_CLASS_DATETIME);

                final Button button = (Button) view.findViewById(R.id.number_child_bt);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        estimation.setDate(editText.getText().toString());
                    }
                });
            }
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
