package com.mobdeve.group23.socialfitnessapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProgramListHolder extends RecyclerView.ViewHolder{


    private ImageView programThumbnailIv;
    private TextView programNameTv;
    private TextView programTypeTv;
    private TextView programDateTv;
    private TextView programTimeTv;



    public ProgramListHolder(@NonNull View itemView) {
        super(itemView);

        this.programThumbnailIv = itemView.findViewById(R.id.programThumbnailIv);
        this.programNameTv = itemView.findViewById(R.id.programNameTv);
        this.programTypeTv = itemView.findViewById(R.id.programTypeTv);
        this.programDateTv = itemView.findViewById(R.id.programDateTv);
        this.programTimeTv = itemView.findViewById(R.id.programTimeTv);

    }

    public void setProgramThumbnailIv(int programThumbnailIv) {
        this.programThumbnailIv.setImageResource(programThumbnailIv);
    }

    public void setProgramNameTv(String programNameTv) {
        this.programNameTv.setText(programNameTv);
    }

    public void setProgramTypeTv(String programTypeTv) {
        this.programTypeTv.setText(programTypeTv);
    }

    public void setProgramDateTv(String programDateTv) {
        this.programDateTv.setText(programDateTv);
    }

    public void setProgramTimeTv(String programTimeTv) {
        this.programTimeTv.setText(programTimeTv);
    }
}
