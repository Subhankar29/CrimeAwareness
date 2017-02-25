package com.example.subhankar29.crimeawareness;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class TakePhoto extends AppCompatActivity {
    Button mcamera;
    ImageView mImageView;
    private Object Bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        final TextView time  = (TextView) findViewById(R.id.time);
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

    }

    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bp = (Bitmap) data.getExtras().get("data");
        mImageView.setImageBitmap(bp);
    }





}
