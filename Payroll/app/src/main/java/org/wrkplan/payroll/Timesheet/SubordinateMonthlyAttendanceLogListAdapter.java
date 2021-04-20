package org.wrkplan.payroll.Timesheet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Model.TimesheetMyAttendanceModel_v2;
import org.wrkplan.payroll.Model.TimesheetSubordinateMonthlyAttendanceModel1;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SubordinateMonthlyAttendanceLogListAdapter extends RecyclerView.Adapter<SubordinateMonthlyAttendanceLogListAdapter.MyViewHolder> {
    public LayoutInflater inflater;
    public static ArrayList<TimesheetSubordinateMonthlyAttendanceModel1> timesheetSubordinateMonthlyAttendanceModel1ArrayList;
    private Context context;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public SubordinateMonthlyAttendanceLogListAdapter(Context ctx, ArrayList<TimesheetSubordinateMonthlyAttendanceModel1> timesheetSubordinateMonthlyAttendanceModel1ArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.timesheetSubordinateMonthlyAttendanceModel1ArrayList = timesheetSubordinateMonthlyAttendanceModel1ArrayList;
    }

    @Override
    public SubordinateMonthlyAttendanceLogListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_my_attendance_log_list_v2, parent, false);
//        View view = inflater.inflate(R.layout.activity_my_attendance_row1, parent, false);
        SubordinateMonthlyAttendanceLogListAdapter.MyViewHolder holder = new SubordinateMonthlyAttendanceLogListAdapter.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(SubordinateMonthlyAttendanceLogListAdapter.MyViewHolder holder, int position) {
        holder.itemView.setTag(timesheetSubordinateMonthlyAttendanceModel1ArrayList.get(position));

//        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
//        DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
//        DateFormat inputFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss"); //again date format changed, noted on 7th dec
        DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy"); //again date format changed, noted on 21st jan
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");

        String inputText = timesheetSubordinateMonthlyAttendanceModel1ArrayList.get(position).getDate();

        Date date_log = null;
        try {
            date_log = inputFormat.parse(inputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_date.setText(outputFormat.format(date_log));
        holder.tv_in_time.setText(timesheetSubordinateMonthlyAttendanceModel1ArrayList.get(position).getTime_in());
        holder.tv_out_time.setText(timesheetSubordinateMonthlyAttendanceModel1ArrayList.get(position).getTime_out());
        if (timesheetSubordinateMonthlyAttendanceModel1ArrayList.get(position).getAttendance_status().trim().contentEquals("Absent")) {
            holder.tv_present_absent.setText("Absent");
            holder.tv_present_absent.setTextColor(Color.parseColor("#ffffff"));
            holder.ll_label.setVisibility(View.VISIBLE);
//            holder.ll_label.setBackgroundColor(Color.parseColor(timesheetSubordinateMonthlyAttendanceModel1ArrayList.get(position).getAttendance_color()));
            holder.ll_label.setBackgroundResource(R.drawable.loglist_corner_radius_absent);
        } else if (timesheetSubordinateMonthlyAttendanceModel1ArrayList.get(position).getAttendance_status().trim().contentEquals("Present")) {
            holder.tv_present_absent.setText("Present");
            holder.tv_present_absent.setTextColor(Color.parseColor("#494949"));
            holder.ll_label.setVisibility(View.VISIBLE);
//            holder.ll_label.setBackgroundColor(Color.parseColor("#00FF00"));
//            holder.ll_label.setBackgroundColor(Color.parseColor(timesheetSubordinateMonthlyAttendanceModel1ArrayList.get(position).getAttendance_color()));
            holder.ll_label.setBackgroundResource(R.drawable.loglist_corner_radius_present);
        }else if (timesheetSubordinateMonthlyAttendanceModel1ArrayList.get(position).getAttendance_status().trim().contentEquals("WFH")) {
            holder.tv_present_absent.setText("WFH");
            holder.tv_present_absent.setTextColor(Color.parseColor("#ffffff"));
            holder.ll_label.setVisibility(View.VISIBLE);
//            holder.ll_label.setBackgroundColor(Color.parseColor("#00FF00"));
//            holder.ll_label.setBackgroundColor(Color.parseColor(timesheetSubordinateMonthlyAttendanceModel1ArrayList.get(position).getAttendance_color()));
            holder.ll_label.setBackgroundResource(R.drawable.loglist_corner_radius_wfh);
        } else if (timesheetSubordinateMonthlyAttendanceModel1ArrayList.get(position).getAttendance_status().trim().contentEquals("")) {
            holder.ll_label.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return timesheetSubordinateMonthlyAttendanceModel1ArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_date, tv_in_time, tv_out_time, tv_present_absent;
        LinearLayout ll_label;

        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_in_time = itemView.findViewById(R.id.tv_in_time);
            tv_out_time = itemView.findViewById(R.id.tv_out_time);
            tv_out_time = itemView.findViewById(R.id.tv_out_time);
            ll_label = itemView.findViewById(R.id.ll_label);
            tv_present_absent = itemView.findViewById(R.id.tv_present_absent);


        }

    }
}
