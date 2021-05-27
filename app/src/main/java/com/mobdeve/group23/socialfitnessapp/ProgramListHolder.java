package com.mobdeve.group23.socialfitnessapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProgramListHolder extends RecyclerView.ViewHolder{


    private ImageView programPhotoIv;
    private TextView programNameTv;
    private TextView programTypeTv;
    private TextView programDateTimeTv;



    public ProgramListHolder(@NonNull View itemView) {
        super(itemView);

        this.programPhotoIv = itemView.findViewById(R.id.programPhotoIv);
        this.programNameTv = itemView.findViewById(R.id.programNameTv);
        this.programTypeTv = itemView.findViewById(R.id.programTypeTv);
        this.programDateTimeTv = itemView.findViewById(R.id.programDateTimeTv);

    }

    public void setProgramPhotoIv(int programPhotoIv) {
        this.programPhotoIv.setImageResource(programPhotoIv);
    }

    public void setProgramNameTv(String programNameTv) {
        this.programNameTv.setText(programNameTv);
    }

    public void setProgramTypeTv(String programTypeTv) {
        this.programTypeTv.setText(programTypeTv);
    }

    public void setProgramDateTimeTv(String programDateTimeTv) {
        this.programDateTimeTv.setText(programDateTimeTv);
    }

}
