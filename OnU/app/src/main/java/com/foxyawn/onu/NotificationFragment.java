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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {
    GridView gridView;
    ArrayList<String> provider;
    GridAdapter adapter;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    class GridAdapter extends BaseAdapter {
        ArrayList<GridItem> items = new ArrayList<GridItem>();
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
            view.setPlaceType(item.getPlaceType());
            view.setDistrict(item.getDistrict());
            view.setPerson(item.getPerson());
            view.setaddress(item.getAddress());
            view.setImage(item.getResId());
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

        String company = data.getStringExtra("company");
        String name = data.getStringExtra("name");
        String price = data.getStringExtra("price");
        int p = Integer.parseInt(price);
        DecimalFormat commas = new DecimalFormat("#,###");
        price = (String)commas.format(p);
        String comment = data.getStringExtra("comment");
        adapter.addIem(new GridItem(company, name, price, comment, R.drawable.aoa));
        adapter.notifyDataSetChanged();
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NotificationFragment() {
        // Required empty public constructor
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mContext = this.getActivity();
    }
    class Stringvalue{
        String yet;
        public Stringvalue(String yet){
            this.yet = yet;
        }

        public String getYet() {
            return yet;
        }

        public void setYet(String yet) {
            this.yet = yet;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        databaseReference.child("contract").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Estimation estimation = dataSnapshot.getValue(Estimation.class);
//                Log.d("address", estimation.getAddress());
//                Log.d("data", estimation.getDate());
//                Log.d("district", estimation.getDistrict());
//                Log.d("person", estimation.getPerson());
//                Log.d("pro", estimation.getProvider().get(0));
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

            public void next() {
                for (int i = 0; i < provider.size(); i++) {
                    databaseReference.child("users").child("provider").child(provider.get(i)).child("info").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Info info = dataSnapshot.getValue(Info.class);
                            adapter.addIem(new GridItem(info.getFacilities(), info.getName(), "7", info.getAddress(), R.drawable.hall2));
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
//        adapter.addIem(new GridItem("공연장","동구","3","상세주소1",R.drawable.hall2));
//        adapter.addIem(new GridItem("숙소","중구","7","상세주소2",R.drawable.home));
//        adapter.addIem(new GridItem("스터디룸","서구","5","상세주소3",R.drawable.study));
//        adapter.addIem(new GridItem("연습실","유성구","3","상세주소4",R.drawable.practice));

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GridItem item = (GridItem) adapter.getItem(position);
                Toast.makeText(mContext,
                        "선택 : "+item.getPlaceType()+
                                "\n장소 : "+item.getDistrict()+
                                "\n수용인원 : "+ item.getPerson()+
                                "\n상세주소 : "+ item.getAddress()
                        ,Toast.LENGTH_LONG).show();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
