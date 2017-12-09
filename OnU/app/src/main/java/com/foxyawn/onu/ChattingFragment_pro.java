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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class ChattingFragment_pro extends Fragment {


    private StorageReference mStorageRef;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;
    private static Uri mlmageCaptureUri;
    private static ImageView img;
    public Context mContext;
    Estimation estimation;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    public ChattingFragment_pro() {
        // Required empty public constructor
        estimation = new Estimation();
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

            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chatting_pro, container, false);
        img = (ImageView) view.findViewById(R.id.imageView);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakePhotoAction();
                    }
                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction();
                    }
                };
                DialogInterface.OnClickListener cancleListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };

                new AlertDialog.Builder(getContext()).setTitle("업로드할 이미지 선택").setPositiveButton("앨범선택",albumListener).setNeutralButton("취소",cancleListener).setNegativeButton("사진촬영",cameraListener).show();

            }
            public void doTakePhotoAction(){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";

                mlmageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),url));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mlmageCaptureUri);
                startActivityForResult(intent,PICK_FROM_CAMERA);

            }
            public void doTakeAlbumAction(){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }


        });


        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.expanded_menu);
        Button applyButton = (Button) view.findViewById(R.id.apply_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "공간유형 : "+estimation.getPlacetype()+"\n"+"구 : "+estimation.getDistrict()+"\n"+"상세주소 : "+estimation.getAddress()+"\n인원 : "+estimation.getPerson()+"\n가격 : "+estimation.getPrice(), Toast.LENGTH_LONG).show();
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

            } else if(groupPosition == 2) { // 상세주소
                view = LayoutInflater.from(mContext).inflate(R.layout.number_child_item, null);
                final EditText editText = (EditText) view.findViewById(R.id.number_child_item);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);

                final Button button = (Button) view.findViewById(R.id.number_child_bt);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        estimation.setAddress(editText.getText().toString());
//                        Log.d("third",estimation.getNumber());
                    }
                });
            } else if(groupPosition == 3) { // 인원
                view = LayoutInflater.from(mContext).inflate(R.layout.number_child_item, null);
                final EditText editText = (EditText) view.findViewById(R.id.number_child_item);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                final Button button = (Button) view.findViewById(R.id.number_child_bt);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        estimation.setPerson(editText.getText().toString());
//                        Log.d("fourth",estimation.getDate());
                    }
                });
            }else if(groupPosition == 4) { // 날짜
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
            }else if(groupPosition == 5) { // 가격
                view = LayoutInflater.from(mContext).inflate(R.layout.number_child_item, null);
                final EditText editText = (EditText) view.findViewById(R.id.number_child_item);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                final Button button = (Button) view.findViewById(R.id.number_child_bt);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        estimation.setPrice(editText.getText().toString());
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
