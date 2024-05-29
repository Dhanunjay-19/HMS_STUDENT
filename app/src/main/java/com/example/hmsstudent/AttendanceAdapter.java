package com.example.hmsstudent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.myview> {
    Context context;
    List<Attendance> attendanceList = new ArrayList<>();

    public AttendanceAdapter(Context context, List<Attendance> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_attendance, parent, false);
        // set the view's size, margins, paddings and layout parameters
        myview vh = new myview(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {
        Attendance attendance = attendanceList.get(position);
//        holder.name.setText("Name : " + attendance.getData2());
        holder.type.setText("Type : " + attendance.getData5());
        holder.time.setText("Time : " + attendance.getData4());
        holder.roomno.setText("Room No : " + attendance.getData1());


    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public class myview extends RecyclerView.ViewHolder {
        TextView name, type, roomno, time;

        public myview(@NonNull View itemView) {
            super(itemView);
//            name = itemView.findViewById(R.id.Astudentname_tv);
            type = itemView.findViewById(R.id.Atype_tv);
            roomno = itemView.findViewById(R.id.Aroomno_tv);
            time = itemView.findViewById(R.id.Atime_tv);
        }
    }
}
