package org.wrkplan.payroll.Timesheet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Model.TimesheetSubordinateModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class SubordinateAttendanceListAdapterv2 extends RecyclerView.Adapter<SubordinateAttendanceListAdapterv2.MyViewHolder> {
    public LayoutInflater inflater;
    public static ArrayList<TimesheetSubordinateModel> timesheetSubordinateModelArrayList;
    private Context context;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public SubordinateAttendanceListAdapterv2(Context ctx, ArrayList<TimesheetSubordinateModel> timesheetSubordinateModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.timesheetSubordinateModelArrayList = timesheetSubordinateModelArrayList;
    }
    @Override
    public SubordinateAttendanceListAdapterv2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_subordinate_attendance_row_v2, parent, false);
//        View view = inflater.inflate(R.layout.activity_my_attendance_row1, parent, false);
        SubordinateAttendanceListAdapterv2.MyViewHolder holder = new SubordinateAttendanceListAdapterv2.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(SubordinateAttendanceListAdapterv2.MyViewHolder holder, int position) {
        holder.itemView.setTag(timesheetSubordinateModelArrayList.get(position));

        holder.tv_emp_name.setText(timesheetSubordinateModelArrayList.get(position).getEmployee_name());
        holder.tv_in_time.setText(timesheetSubordinateModelArrayList.get(position).getTime_in());
//        holder.tv_in_time.setText("10:00AM");
        holder.tv_out_time.setText(timesheetSubordinateModelArrayList.get(position).getTime_out());
//        holder.tv_out_time.setText("04:00PM");
        if(timesheetSubordinateModelArrayList.get(position).getAttendance_status().trim().contentEquals("Absent")){
            holder.tv_present_absent.setText("Absent");
            holder.tv_present_absent.setTextColor(Color.parseColor("#ffffff"));
            holder.ll_label.setVisibility(View.VISIBLE);
//            holder.ll_label.setBackgroundColor(Color.parseColor(timesheetSubordinateModelArrayList.get(position).getAttendance_color()));
//            holder.ll_label.setBackgroundColor(Color.parseColor("#FF0000"));
            holder.ll_label.setBackgroundResource(R.drawable.loglist_corner_radius_absent);
        }else if(timesheetSubordinateModelArrayList.get(position).getAttendance_status().trim().contentEquals("Present")){
            holder.tv_present_absent.setText("Present");
            holder.tv_present_absent.setTextColor(Color.parseColor("#494949"));
            holder.ll_label.setVisibility(View.VISIBLE);
//            holder.ll_label.setBackgroundColor(Color.parseColor("#00FF00"));
            holder.ll_label.setBackgroundResource(R.drawable.loglist_corner_radius_present);
        }else if(timesheetSubordinateModelArrayList.get(position).getAttendance_status().trim().contentEquals("")){
            holder.ll_label.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return timesheetSubordinateModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_emp_name, tv_in_time, tv_out_time, tv_present_absent;
        LinearLayout ll_label;

        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_emp_name = itemView.findViewById(R.id.tv_emp_name);
            tv_in_time = itemView.findViewById(R.id.tv_in_time);
            tv_out_time = itemView.findViewById(R.id.tv_out_time);
            ll_label = itemView.findViewById(R.id.ll_label);
            tv_present_absent = itemView.findViewById(R.id.tv_present_absent);


        }

    }
}
