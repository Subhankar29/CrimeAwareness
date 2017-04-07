package com.example.subhankar29.crimeawareness.drawer.report_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Subhankar29 on 11/03/17.
 */

public class myCrimeAdapter extends RecyclerView.Adapter<myCrimeAdapter.MyOwnHolder> {

    @Override
    public MyOwnHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyOwnHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyOwnHolder extends RecyclerView.ViewHolder {
        public MyOwnHolder(View itemView) {
            super(itemView);
        }
    }
}
