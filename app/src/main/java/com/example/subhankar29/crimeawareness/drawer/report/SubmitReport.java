package com.example.subhankar29.crimeawareness.drawer.report;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.subhankar29.crimeawareness.LocationService;
import com.example.subhankar29.crimeawareness.PostDetails;
import com.example.subhankar29.crimeawareness.R;
import com.example.subhankar29.crimeawareness.ULocation;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubmitReport.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubmitReport#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubmitReport extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button mcamera;
    ImageView mImageView;
    private Object Bitmap;
    private Button postButton;
    private TextView subjectText;
    private TextView descText;
    Button location;

    private static final int APP_PERMS = 1097;


    FirebaseDatabase ref;

    private OnFragmentInteractionListener mListener;

    public SubmitReport() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubmitReport.
     */
    // TODO: Rename and change types and number of parameters
    public static SubmitReport newInstance(String param1, String param2) {
        SubmitReport fragment = new SubmitReport();
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
        return inflater.inflate(R.layout.fragment_submit_report, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        //Request Permissions (for Marshmallow onwards)
        requestPermissions();

        final TextView time  = (TextView) getView().findViewById(R.id.time);
        ref = FirebaseDatabase.getInstance();
        subjectText = (EditText) getView().findViewById(R.id.subject);
        descText = (EditText) getView().findViewById(R.id.description);
        postButton = (Button) getView().findViewById(R.id.postButton);



        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

// textView is the TextView view that should display it
                time.setText(currentDateTimeString);
            }


        });


        mcamera = (Button) getView().findViewById(R.id.ButtonCamera);
        mImageView = (ImageView) getView().findViewById(R.id.imageview);
        mcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

        //Sends data to Firebase
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostDetails details = new PostDetails();
                ULocation location = new ULocation();
                location.setLatitude(Double.toString(LocationService.getUserLocation().getLatitude()));
                location.setLongitude(Double.toString(LocationService.getUserLocation().getLongitude()));
                details.setDesc(descText.getText().toString());
                details.setSubject(subjectText.getText().toString());
                details.setLocation(location);
                ref.getReference().getRoot().child("Posts").push().setValue(details);

            }
        });
    }

    public void requestPermissions(){
        if ((ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this.getActivity(),Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE},APP_PERMS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case APP_PERMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        android.graphics.Bitmap bp = (Bitmap) data.getExtras().get("data");
        mImageView.setImageBitmap(bp);
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
