package com.example.subhankar29.crimeawareness.drawer.map;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.subhankar29.crimeawareness.LocationService;
import com.example.subhankar29.crimeawareness.PostDetails;
import com.example.subhankar29.crimeawareness.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private GoogleMap mMap;
    private ArrayList<AreaList> alist;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
        alist = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        initializeMap(view);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void initializeMap(View v) {
        if (mMap == null) {
            SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(this);
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
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    String wordToDisplay;

    String wordList[] = new String[3];
    Random r = new Random();

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        wordList[0]="ROBBERY";
        wordList[1]="EVE TEASING";
        wordList[2]="ASSAULT";
        int rnd = new Random().nextInt(wordList.length);
        wordToDisplay = wordList[rnd];



        // Add a marker in your location and move the camera
        LatLng bangalore = new LatLng(LocationService.getUserLocation().getLatitude(),LocationService.getUserLocation().getLongitude());
        mMap.addMarker(new MarkerOptions().position(bangalore).title("Crime Reported:" + wordToDisplay));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bangalore));


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    PostDetails det =d.getValue(PostDetails.class);
                    Log.d("POST_DETAILS",det.getLocation().getLatitude()+" "+det.getLocation().getLongitude()+"\n");
                    Log.d("POST_DETAILS",d.getKey());

                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(det.getLocation().getLatitude()),Double.parseDouble(det.getLocation().getLongitude()))).title("Crime Reported:" + wordList[r.nextInt(3)]));

                    //Diagnose how classifier works
                    Location l = new Location("gps");
                    l.setLatitude(Double.parseDouble(det.getLocation().getLatitude()));
                    l.setLongitude(Double.parseDouble(det.getLocation().getLongitude()));
                    classifyLocation(l);


                }
                printclassifier();
                markArea();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //This algorithm has not been tested for robustness. Please check for potential problems.
    public void classifyLocation(Location l){
        for(AreaList area:alist){
            for(Location location:area.getMembers()){
                //Don't add location to this area if distance is greater than 500m.
                if(l.distanceTo(location)>500)continue;
                //Add location to this area and return
                area.getMembers().add(l);
                return;
            }
        }
        //At this point this location couldn't be classified in existing areas, so add new area and add location to this area.
        AreaList e = new AreaList();
        e.getMembers().add(l);
        alist.add(e);
    }

    public void printclassifier(){
        int i=0;
        for(AreaList area:alist){
            Log.d("CLASSIFIER","Area "+(++i));
            for(Location location:area.getMembers()){
                Log.d("CLASSIFIER","Location:"+location.getLatitude()+","+location.getLongitude());
            }
        }
    }

    public void markArea(){
        for(AreaList area:alist){
            float lat=0,lo=0;
            int n=0;
            for(Location location:area.getMembers()){
                lat+=location.getLatitude();
                lo+=location.getLongitude();
                n++;
            }

            if(n<5)continue;
            LatLng l = new LatLng(lat/n,lo/n);

            CircleOptions circleOptions = new CircleOptions()
                    .center(l)   //set center
                    .radius(500)   //set radius in meters
                    .fillColor(0x40ff0000)  //default
                    .strokeColor(Color.BLUE)
                    .strokeWidth(5);
            mMap.addCircle(circleOptions);
        }


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
