package com.example.subhankar29.crimeawareness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TakePhoto extends AppCompatActivity {
    Button time;

    FirebaseDatabase ref;
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
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                //get current date time with Date()
                Date date = new Date();
                System.out.println(dateFormat.format(date));

                //get current date time with Calendar()
                Calendar cal = Calendar.getInstance();
                System.out.println(dateFormat.format(cal.getTime()));
                time.setText((CharSequence) cal.getTime());
            }
        });

    }


}
