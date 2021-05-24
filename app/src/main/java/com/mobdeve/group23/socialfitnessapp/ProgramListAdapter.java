package com.mobdeve.group23.socialfitnessapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProgramListAdapter extends RecyclerView.Adapter<ProgramListHolder> {

    private ArrayList<Program> programList;

    public ProgramListAdapter(ArrayList<Program> data) {
        this.programList = data;
    }

    @NonNull
    @Override
    public ProgramListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate layout to a view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_layout, parent, false);

        //Assign layout or view to OrderListHolder
        ProgramListHolder programListHolder = new ProgramListHolder(v);
        return programListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramListHolder holder, int position) {

        //To set the information
        holder.setProgramPhotoIv(this.programList.get(position).getPhoto());
        holder.setProgramNameTv(this.programList.get(position).getName());
        holder.setProgramTypeTv(this.programList.get(position).getType());
        holder.setProgramDateTv(this.programList.get(position).getDate());
        holder.setProgramTimeTv(this.programList.get(position).getTime());

        //start new activity
        /*
        holder.itemView.findViewById(R.id.orderlistViewTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntent(v, position);
            }
        });

         */

        holder.itemView.findViewById(R.id.programLl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntent(v, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return programList.size();
    }

    public void setIntent(View v, int position) {
        Intent i = new Intent(v.getContext(), ProgramActivity.class);
        i.putExtra("name", this.programList.get(position).getName());
        i.putExtra("date", this.programList.get(position).getDate());
        i.putExtra("time", this.programList.get(position).getTime());
        i.putExtra("description", this.programList.get(position).getDescription());
        i.putExtra("link", this.programList.get(position).getLink());
        i.putExtra("type", this.programList.get(position).getType());
        i.putExtra("photo", this.programList.get(position).getPhoto());

        v.getContext().startActivity(i);

    }
}
