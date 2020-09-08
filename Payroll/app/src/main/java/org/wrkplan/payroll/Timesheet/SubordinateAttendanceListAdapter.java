package org.wrkplan.payroll.Timesheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Model.TimesheetMyAttendanceModel;
import org.wrkplan.payroll.Model.TimesheetSubordinateModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SubordinateAttendanceListAdapter extends RecyclerView.Adapter<SubordinateAttendanceListAdapter.MyViewHolder> {
    public LayoutInflater inflater;
    public static ArrayList<TimesheetSubordinateModel> timesheetSubordinateModelArrayList;
    private Context context;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public SubordinateAttendanceListAdapter(Context ctx, ArrayList<TimesheetSubordinateModel> timesheetSubordinateModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.timesheetSubordinateModelArrayList = timesheetSubordinateModelArrayList;
    }
    @Override
    public SubordinateAttendanceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_subordinate_attendance_row, parent, false);
//        View view = inflater.inflate(R.layout.activity_my_attendance_row1, parent, false);
        SubordinateAttendanceListAdapter.MyViewHolder holder = new SubordinateAttendanceListAdapter.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(SubordinateAttendanceListAdapter.MyViewHolder holder, int position) {
        holder.itemView.setTag(timesheetSubordinateModelArrayList.get(position));

        holder.tv_emp_name.setText(timesheetSubordinateModelArrayList.get(position).getEmployee_name());
        holder.tv_in_time.setText(timesheetSubordinateModelArrayList.get(position).getTime_in());
        holder.tv_out_time.setText(timesheetSubordinateModelArrayList.get(position).getTime_out());
       /* if(timesheetMyAttendanceModelArrayList.get(position).getWork_from_home().contentEquals("1")){
            holder.img_view_home.setVisibility(View.VISIBLE);
        }else {
            holder.img_view_home.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return timesheetSubordinateModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_emp_name, tv_in_time, tv_out_time;
        ImageView img_view_home;

        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_emp_name = itemView.findViewById(R.id.tv_emp_name);
            tv_in_time = itemView.findViewById(R.id.tv_in_time);
            tv_out_time = itemView.findViewById(R.id.tv_out_time);
            img_view_home = itemView.findViewById(R.id.img_view_home);


        }

    }
}
