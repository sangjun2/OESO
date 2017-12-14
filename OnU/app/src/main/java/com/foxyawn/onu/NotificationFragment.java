package com.foxyawn.onu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        public void addIem(GridItem item){
            items.add(item);
            view = new GridItemView(mContext);
            item.setSonImageView(view.getImageView());
        }
        @Override
        public Object getItem(int position) {return items.get(position);}
        @Override
        public long getItemId(int position) {return position;}
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            GridItem item = items.get(position);
            view.setPlaceType(item.getPlaceType());
            view.setDistrict(item.getDistrict());
            view.setPerson(item.getPerson());
            view.setaddress(item.getAddress());
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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            int index = 0;
            public void next() {
                for (int i = 0; i < provider.size(); i++) {
                    index = i;
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
//                                    ((GridItem)gridView.getItemAtPosition(index)).setResId(uri);
                                    ImageView imgv = ((GridItem)adapter.getItem(index)).getSonImageView();
                                    Glide.with(getContext()).using(new FirebaseImageLoader()).load(imagefile1).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imgv);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                            adapter.addIem(new GridItem(info.getFacilities(), info.getName(), "7", info.getAddress(),R.drawable.noimage , provider.get(index)));
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView);

        adapter = new GridAdapter();

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GridItem item = (GridItem) adapter.getItem(position);
                Intent intent = new Intent(mContext, PictureView.class);
                intent.putExtra("providerUid",item.getProviderUid());
                startActivity(intent);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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