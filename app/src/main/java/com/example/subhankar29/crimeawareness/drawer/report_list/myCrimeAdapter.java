package com.example.subhankar29.crimeawareness.drawer.report_list;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.subhankar29.crimeawareness.PostDetails;
import com.example.subhankar29.crimeawareness.R;

import java.util.List;

/**
 * Created by Subhankar29 on 11/03/17.
 */

public class myCrimeAdapter extends RecyclerView.Adapter<myCrimeAdapter.MyOwnHolder> {
    private  List<PostDetails> postDetailsList;

    @Override
    public MyOwnHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_list_layout_row,parent,false);

        return new MyOwnHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyOwnHolder holder, int position) {
        PostDetails p = postDetailsList.get(position);
        holder.subject.setText("Hello");

    }

    public myCrimeAdapter(List<PostDetails> listPostDetails){
        postDetailsList  = listPostDetails;
        if(listPostDetails == null) Log.d("CARDVIEW","NULL");
    }

    @Override
    public int getItemCount() {
        return postDetailsList.size();
    }

    public class MyOwnHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView subject;
        public MyOwnHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            subject = (TextView) cardView.findViewById(R.id.info_text);
        }
    }
}
