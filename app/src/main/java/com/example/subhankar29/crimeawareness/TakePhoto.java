package com.example.subhankar29.crimeawareness;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;

import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;



public class TakePhoto extends AppCompatActivity {
    Button mcamera;
    ImageView mImageView;
    private Object Bitmap;
    private Button postButton;
    private TextView subjectText;
    private TextView descText;

    private static final int APP_PERMS = 1097;


    FirebaseDatabase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        //Request Permissions (for Marshmallow onwards)
        requestPermissions();

        final TextView time  = (TextView) findViewById(R.id.time);
        ref = FirebaseDatabase.getInstance();
        subjectText = (EditText) findViewById(R.id.subject);
        descText = (EditText) findViewById(R.id.description);
        postButton = (Button) findViewById(R.id.postButton);



        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

// textView is the TextView view that should display it
                time.setText(currentDateTimeString);
            }


        });


        mcamera = (Button) findViewById(R.id.ButtonCamera);
        mImageView = (ImageView) findViewById(R.id.imageview);
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

    @Override
    protected void onStart() {
        super.onStart();

        //Check if Location Service is running. If not, starts it.
        if(!LocationService.isRunning){
            Log.d("LOCATIONSERVICE","Started");
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    startService(new Intent(TakePhoto.this,LocationService.class)/*.putExtra("com.goodsamaritan.myphone",getIntent().getStringExtra("com.goodsamaritan.myphone"))*/);
                }
            });
            t1.start();
        } else Log.d("LOCATIONSERVICE","Running");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bp = (Bitmap) data.getExtras().get("data");
        mImageView.setImageBitmap(bp);
    }

    public void requestPermissions(){
        if ((ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE},APP_PERMS);
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





}
