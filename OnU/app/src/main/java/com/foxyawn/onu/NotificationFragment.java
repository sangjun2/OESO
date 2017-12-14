package com.foxyawn.onu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    GridView gridView;
    ArrayList<String> provider;
    GridAdapter adapter;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    class GridAdapter extends BaseAdapter {
        ArrayList<GridItem> items = new ArrayList<GridItem>();
        GridItemView view;
        @Override
        public int getCount() {return items.size();}
        public void addIem(GridItem item){items.add(item);}
        @Override
        public Object getItem(int position) {return items.get(position);}
        @Override
        public long getItemId(int position) {return position;}
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            GridItemView view = new GridItemView(mContext);
            GridItem item = items.get(position);
<<<<<<< HEAD
=======
            item.setSonImageView(view.getImageView());
>>>>>>> 915cd8401a8a61cdf5cb871bcdcb54bfbc8c1011
            view.setName(item.getName());
            view.setAddress(item.getAddress());
            view.setIntroduce(item.getIntroduce());
            view.setPrice(item.getPrice());
            int numColumns = gridView.getNumColumns();
            int rowIndex = position/numColumns;
            int columnIndex = position%numColumns;
            Log.d("GridAdapter","index : " + rowIndex+", "+columnIndex);
            return view;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private OnFragmentInteractionListener mListener;

    public NotificationFragment() {
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this.getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView);

        adapter = new GridAdapter();
        gridView.setAdapter(adapter);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.child("contract").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Estimation estimation = dataSnapshot.getValue(Estimation.class);
                provider = estimation.getProvider();
                if (provider.get(0).equals("")) {
                    Toast.makeText(getContext(),"신청서입력이 안되있거나\n신청자가 없습니다..",Toast.LENGTH_LONG).show();
                }else{
                    next();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
<<<<<<< HEAD
            int index = 0;
=======
            public void next() {
                for (int i = 0; i < provider.size(); i++) {
                    final int index = i;
                    databaseReference.child("users").child("provider").child(provider.get(i)).child("info").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final Info info = dataSnapshot.getValue(Info.class);
                            StorageReference mStorageRef =  FirebaseStorage.getInstance().getReference();
                            StorageReference uidfolder = mStorageRef.child(provider.get(index));
                            final StorageReference imagefile1 = uidfolder.child("image1");
                            imagefile1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ImageView imgv = ((GridItem)adapter.getItem(index)).getSonImageView();
                                    Glide.with(getContext()).using(new FirebaseImageLoader()).load(imagefile1).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imgv);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                            adapter.addIem(new GridItem(info.getName(), info.getAddress(), info.getIntroduce(),info.getPrice(),R.drawable.noimage , provider.get(index),info));
                            adapter.notifyDataSetChanged();
>>>>>>> 915cd8401a8a61cdf5cb871bcdcb54bfbc8c1011

            public void next() {
                databaseReference.child("users").child("provider").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        for (int i=0;i<provider.size();i++){
                            String a = dataSnapshot.getKey();
                            String b = provider.get(i);
                            if (provider.get(i).equals(dataSnapshot.getKey())){
                                Info info = dataSnapshot.getValue(Info.class);
                                adapter.addIem(new GridItem(info.getName(), info.getAddress(), info.getIntroduce(),info.getPrice(),R.drawable.noimage , provider.get(i), info));
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

<<<<<<< HEAD
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView);

        adapter = new GridAdapter();
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
=======

>>>>>>> 915cd8401a8a61cdf5cb871bcdcb54bfbc8c1011

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GridItem item = (GridItem) adapter.getItem(position);
                Toast.makeText(mContext,
<<<<<<< HEAD
                        "공간유형 : " + item.getInfo().getPurpose() +
=======
                                "공간유형 : " + item.getInfo().getPurpose() +
>>>>>>> 915cd8401a8a61cdf5cb871bcdcb54bfbc8c1011
                                "\n이용가능 시간 : " + item.getInfo().getTime() +
                                "\n내부 시설 : " + item.getInfo().getFacilities() +
                                "\n주변 시설 : " + item.getInfo().getAround() +
                                "\n주의 사항 : " + item.getInfo().getNotice() +
                                "\n기타 : " + item.getInfo().getEtc()
<<<<<<< HEAD
                        , Toast.LENGTH_SHORT).show();
=======
                                , Toast.LENGTH_SHORT).show();
>>>>>>> 915cd8401a8a61cdf5cb871bcdcb54bfbc8c1011
                Intent intent = new Intent(mContext, PictureView.class);
                intent.putExtra("providerUid",item.getProviderUid());
                startActivity(intent);
            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
        mListener.onFragmentInteraction(uri);
    }
}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}