package com.foxyawn.onu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class ChattingFragment_pro extends Fragment {

    public Context mContext;
    Info info;
    Estimation estimation;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    public ChattingFragment_pro() {
        // Required empty public constructor
        info = new Info();
    }

    public static ChattingFragment_pro newInstance() {
        ChattingFragment_pro fragment = new ChattingFragment_pro();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode != RESULT_OK)
            return;

        if(resultCode != RESULT_OK) return;
        switch(requestCode)
        {
            case PICK_FROM_ALBUM:
                mlmageCaptureUri = data.getData();
                Log.d("SmartWheel",mlmageCaptureUri.getPath().toString());
            case PICK_FROM_CAMERA:
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
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.getUid();
                    Bitmap photo = extras.getParcelable("data");
                    mStorageRef = FirebaseStorage.getInstance().getReference();

                    StorageReference imagefolder = mStorageRef.child("image");
                    StorageReference imagefile = imagefolder.child("hihi");
                    img.setImageBitmap(photo);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] photodata = baos.toByteArray();
                    UploadTask uploadTask = imagefile.putBytes(photodata);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"실패",Toast.LENGTH_LONG).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(),"성공",Toast.LENGTH_LONG).show();

                        }
                    });

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chatting_pro, container, false);



        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.expanded_menu);
        Button applyButton = (Button) view.findViewById(R.id.apply_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                databaseReference.child("users").child("provider").child(user.getUid()).child("info").setValue(info);

                Toast.makeText(mContext, "등록되었습니다!", Toast.LENGTH_LONG).show();
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

        groupList.add(new Item("공간유형", typeList));
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
        groupList.add(new Item("상세주소", null));
        groupList.add(new Item("최대수용인원", null));
        groupList.add(new Item("가격",null));
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

            if(groupPosition == 0) { // 이름
                view = LayoutInflater.from(mContext).inflate(R.layout.number_child_item, null);
                final EditText editText = (EditText) view.findViewById(R.id.number_child_item);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);

                final Button button = (Button) view.findViewById(R.id.number_child_bt);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        info.setName(editText.getText().toString());
//                        Log.d("third",estimation.getNumber());
                    }
                });
            }else if(groupPosition == 1) { // 공간유형
                view = LayoutInflater.from(mContext).inflate(R.layout.number_child_item, null);
                final EditText editText = (EditText) view.findViewById(R.id.number_child_item);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);

                final Button button = (Button) view.findViewById(R.id.number_child_bt);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       info.setPurpose(editText.getText().toString());
//                        Log.d("third",estimation.getNumber());
                    }
                });
            }else if(groupPosition == 2) { // 소개
                view = LayoutInflater.from(mContext).inflate(R.layout.number_child_item, null);
                final EditText editText = (EditText) view.findViewById(R.id.number_child_item);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);

                final Button button = (Button) view.findViewById(R.id.number_child_bt);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        info.setIntroduce(editText.getText().toString());
//                        Log.d("third",estimation.getNumber());
                    }
                });
            } else if(groupPosition == 3) { // 주소
                view = LayoutInflater.from(mContext).inflate(R.layout.number_child_item, null);
                final EditText editText = (EditText) view.findViewById(R.id.number_child_item);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);

                final Button button = (Button) view.findViewById(R.id.number_child_bt);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        info.setAddress(editText.getText().toString());
//                        Log.d("fourth",estimation.getDate());
                    }
                });
            }else if(groupPosition == 4) { // 이용가능시간
                view = LayoutInflater.from(mContext).inflate(R.layout.number_child_item, null);
                final EditText editText = (EditText) view.findViewById(R.id.number_child_item);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);

                final Button button = (Button) view.findViewById(R.id.number_child_bt);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        info.setTime(editText.getText().toString());
//                        Log.d("fourth",estimation.getDate());
                    }
                });
            }else if(groupPosition == 5) { // 내부시설
                view = LayoutInflater.from(mContext).inflate(R.layout.number_child_item, null);
                final EditText editText = (EditText) view.findViewById(R.id.number_child_item);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);

                final Button button = (Button) view.findViewById(R.id.number_child_bt);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        info.setFacilities(editText.getText().toString());
//                        Log.d("five",estimation.getDate());
                    }
                });
            }else if(groupPosition == 6) { // 주변시설
                view = LayoutInflater.from(mContext).inflate(R.layout.number_child_item, null);
                final EditText editText = (EditText) view.findViewById(R.id.number_child_item);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);

                final Button button = (Button) view.findViewById(R.id.number_child_bt);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        info.setAround(editText.getText().toString());
//                        Log.d("five",estimation.getDate());
                    }
                });
            }else if(groupPosition == 7) { // 주의사항
                view = LayoutInflater.from(mContext).inflate(R.layout.number_child_item, null);
                final EditText editText = (EditText) view.findViewById(R.id.number_child_item);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);

                final Button button = (Button) view.findViewById(R.id.number_child_bt);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        info.setNotice(editText.getText().toString());
//                        Log.d("five",estimation.getDate());
                    }
                });
            }else if(groupPosition == 8) { // 기타
                view = LayoutInflater.from(mContext).inflate(R.layout.number_child_item, null);
                final EditText editText = (EditText) view.findViewById(R.id.number_child_item);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);

                final Button button = (Button) view.findViewById(R.id.number_child_bt);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        info.setEtc(editText.getText().toString());
//                        Log.d("five",estimation.getDate());
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
