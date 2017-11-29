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

import java.util.ArrayList;


public class MainFragment extends Fragment {
    public Context mContext;
    Estimation estimation;

    public MainFragment() {
        // Required empty public constructor
         estimation = new Estimation();
    }


    // TODO: Rename and change types and number of parameters
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

        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.expanded_menu);
        Button applyButton = (Button) view.findViewById(R.id.apply_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "공간유형 : "+estimation.getPlacetype()+"\n"+"구 : "+estimation.getDistrict()+"\n"+"인원 : "+estimation.getNumber()+"\n"+"날짜 : "+estimation.getDate(), Toast.LENGTH_LONG).show();
            }
        });
        ArrayList<Item> groupList = new ArrayList<>();
        ArrayList<String> typeList = new ArrayList<>();
        typeList.add("공연장");
        typeList.add("숙소");
        typeList.add("스터디룸");
        typeList.add("연습실");
        typeList.add("카페");
        typeList.add("파티룸");
        typeList.add("회의실");
        Item item = new Item("공간유형", typeList);

        groupList.add(item);

        ArrayList<String> districtList = new ArrayList<>();
        districtList.add("동구");
        districtList.add("중구");
        districtList.add("서구");
        districtList.add("유성구");
        districtList.add("대덕구");
        groupList.add(new Item("구", districtList));

        groupList.add(new Item("인원", null));
        groupList.add(new Item("날짜", null));

        ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(groupList);
        listView.setAdapter(adapter);

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
        public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
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
//                            Log.d("first",estimation.getPlacetype());
                        }
                        else{
                            estimation.setDistrict(button.getText().toString());
                            button.setBackgroundColor(R.color.selected);
//                            Log.d("second",estimation.getDistrict());
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
                        estimation.setNumber(editText.getText().toString());
//                        Log.d("third",estimation.getNumber());
                    }
                });
            } else if(groupPosition == 3) { // 날짜
                view = LayoutInflater.from(mContext).inflate(R.layout.number_child_item, null);
                final EditText editText = (EditText) view.findViewById(R.id.number_child_item);
                editText.setInputType(InputType.TYPE_CLASS_DATETIME);

                final Button button = (Button) view.findViewById(R.id.number_child_bt);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        estimation.setDate(editText.getText().toString());
//                        Log.d("fourth",estimation.getDate());
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
