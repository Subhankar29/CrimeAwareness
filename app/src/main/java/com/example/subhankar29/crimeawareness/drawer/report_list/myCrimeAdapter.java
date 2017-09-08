package com.example.subhankar29.crimeawareness.drawer.report_list;

import android.graphics.Bitmap;
import android.location.Location;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.subhankar29.crimeawareness.PostDetails;
import com.example.subhankar29.crimeawareness.R;

import java.util.List;

/**
 * Created by Subhankar29 on 11/03/17.
 */

public class myCrimeAdapter extends RecyclerView.Adapter<myCrimeAdapter.MyOwnHolder> {
    private  List<PostDetails> postDetailsList;
    private  List<Bitmap> bitmaps;


    @Override
    public MyOwnHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_list_layout_row,parent,false);

        return new MyOwnHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyOwnHolder holder, int position) {
        PostDetails p = postDetailsList.get(position);
        lStats();
        try{
            Bitmap b = bitmaps.get(position);
            holder.imageView.setImageBitmap(b);
        }catch (Exception e){
            Log.d("IMAGE","Didn't/Couldn't download.");
        }

        holder.subject.setText(p.getSubject());
        holder.desc.setText(p.getDesc());

    }

    public myCrimeAdapter(List<PostDetails> listPostDetails){
        postDetailsList  = listPostDetails;
        if(listPostDetails == null) Log.d("CARDVIEW","NULL");
    }

    @Override
    public int getItemCount() {
        return postDetailsList.size();
    }

    public void setImageViewList(List<Bitmap> bitmaps){
        this.bitmaps = bitmaps;
    }

    public void lStats(){
        Log.d("IMAGELIST",""+(bitmaps==null));
        Log.d("IMAGELIST",""+(bitmaps.size()));
    }

    public class MyOwnHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView subject;
        ImageView imageView;
        TextView desc;

        public MyOwnHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            subject = (TextView) cardView.findViewById(R.id.subject);
            imageView = (ImageView) cardView.findViewById(R.id.cardImage);
            desc = (TextView) cardView.findViewById(R.id.description);
        }
    }
}