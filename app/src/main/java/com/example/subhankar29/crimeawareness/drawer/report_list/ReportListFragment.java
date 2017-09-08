package com.example.subhankar29.crimeawareness.drawer.report_list;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.subhankar29.crimeawareness.PostDetails;
import com.example.subhankar29.crimeawareness.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReportListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReportListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportListFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private RecyclerView reportListRecyclerView;
    private myCrimeAdapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;

    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;

    private List<PostDetails> postDetailsList;

    private File local;
    List<Bitmap> bitmaps;

    public ReportListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportListFragment newInstance(String param1, String param2) {
        ReportListFragment fragment = new ReportListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_list, container, false);
        //Add views here using view.findViewByID()
        reportListRecyclerView = (RecyclerView) view.findViewById(R.id.report_list_recycler_view);

        rLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        reportListRecyclerView.setLayoutManager(rLayoutManager);


        postDetailsList = new ArrayList<>();
        rAdapter = new myCrimeAdapter(postDetailsList);




        reportListRecyclerView.setAdapter(rAdapter);


        preparePostDetails();





        return view;
    }

    private void preparePostDetails() {

        //PostDetails e = new PostDetails();
        //e.setSubject("Hello");
        //postDetailsList.add(e);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        bitmaps = new ArrayList<>();


        firebaseDatabase.getReference().getRoot().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot d:dataSnapshot.getChildren()){
                    //Log.d("CDATABASE",d.getKey()+"\n"+d.getValue());
                    PostDetails p = d.getValue(PostDetails.class);
                    postDetailsList.add(p);
                    try {
                        local = File.createTempFile("thumb",".jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //local.delete();
                    Log.d("PATH",firebaseStorage.getReference().getRoot().child("images/"+d.getKey()+"/picture.jpg").getPath());
                    firebaseStorage.getReference().getRoot().child("images/"+d.getKey()+"/picture.jpg").getFile(local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap img = BitmapFactory.decodeFile(local.getPath());

                            if(img==null)Log.d("IMAGE","It's not there."+local.length()+local.exists());
                            bitmaps.add(img);
                            rAdapter.notifyDataSetChanged();
                            Log.d("IMAGE","Hello\nHello\nHello\nThe list size is:"+bitmaps.size());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("IMAGE_DOWNLOAD","fail");
                            Log.d("IMAGE","Hello\nHello\nHello\nThe list size is:"+bitmaps.size());
                        }
                    });
                }
                rAdapter.setImageViewList(bitmaps);
                Log.d("IMAGE","The list size is:"+bitmaps.size());
                rAdapter.lStats();
                rAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //rAdapter.notifyDataSetChanged();
        Log.d("CARDVIEW","DONE.");
        Log.d("CARDVIEW","DONE.");
        Log.d("CARDVIEW","DONE.");
        Log.d("CARDVIEW","DONE.");
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
