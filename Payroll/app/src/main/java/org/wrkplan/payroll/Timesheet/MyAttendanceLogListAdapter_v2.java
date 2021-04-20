package org.wrkplan.payroll.Timesheet;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Model.TimesheetMyAttendanceModel_v2;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAttendanceLogListAdapter_v2 extends RecyclerView.Adapter<MyAttendanceLogListAdapter_v2.MyViewHolder> {
    public LayoutInflater inflater;
    public static ArrayList<TimesheetMyAttendanceModel_v2> timesheetMyAttendanceModel_v2ArrayList;
    private Context context;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public MyAttendanceLogListAdapter_v2(Context ctx, ArrayList<TimesheetMyAttendanceModel_v2> timesheetMyAttendanceModel_v2ArrayList){

        inflater = LayoutInflater.from(ctx);
        this.timesheetMyAttendanceModel_v2ArrayList = timesheetMyAttendanceModel_v2ArrayList;
    }
    @Override
    public MyAttendanceLogListAdapter_v2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_my_attendance_log_list_v2, parent, false);
//        View view = inflater.inflate(R.layout.activity_my_attendance_row1, parent, false);
        MyAttendanceLogListAdapter_v2.MyViewHolder holder = new MyAttendanceLogListAdapter_v2.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(MyAttendanceLogListAdapter_v2.MyViewHolder holder, int position) {
        holder.itemView.setTag(timesheetMyAttendanceModel_v2ArrayList.get(position));

//        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
//        DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a"); //--for live url
//        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss"); //--for local
        DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy"); //--again changed on 21st jan
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");

        String inputText = timesheetMyAttendanceModel_v2ArrayList.get(position).getDate();

        Date date_log = null;
        try {
            date_log = inputFormat.parse(inputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_date.setText(outputFormat.format(date_log));
        holder.tv_in_time.setText(timesheetMyAttendanceModel_v2ArrayList.get(position).getTime_in());
        holder.tv_out_time.setText(timesheetMyAttendanceModel_v2ArrayList.get(position).getTime_out());
        if(timesheetMyAttendanceModel_v2ArrayList.get(position).getAttendance_status().trim().contentEquals("Absent")){
            holder.tv_present_absent.setText("Absent");
            holder.tv_present_absent.setTextColor(Color.parseColor("#ffffff"));
            holder.ll_label.setVisibility(View.VISIBLE);
//            holder.ll_label.setBackgroundColor(Color.parseColor(timesheetMyAttendanceModel_v2ArrayList.get(position).getAttendance_color()));
//            holder.ll_label.setBackgroundColor(R.drawable.loglist_corner_radius_absent);
            holder.ll_label.setBackgroundResource(R.drawable.loglist_corner_radius_absent);
//            Log.d("Absent color-=>",fb4e4e);
//            holder.ll_label.setBackgroundColor(R.drawable.loglist_corner_radius);

        }else if(timesheetMyAttendanceModel_v2ArrayList.get(position).getAttendance_status().trim().contentEquals("Present")){
            holder.tv_present_absent.setText("Present");
            holder.tv_present_absent.setTextColor(Color.parseColor("#494949"));
            holder.ll_label.setVisibility(View.VISIBLE);
//            holder.ll_label.setBackgroundColor(Color.parseColor("#00FF00"));
//            holder.ll_label.setBackgroundColor(R.drawable.loglist_corner_radius_present);
            holder.ll_label.setBackgroundResource(R.drawable.loglist_corner_radius_present);
//            Log.d("Present color-=>",9fdd55);
        }else if(timesheetMyAttendanceModel_v2ArrayList.get(position).getAttendance_status().trim().contentEquals("WFH")){
            holder.tv_present_absent.setText("WFH");
            holder.tv_present_absent.setTextColor(Color.parseColor("#ffffff"));
            holder.ll_label.setVisibility(View.VISIBLE);
//            holder.ll_label.setBackgroundColor(Color.parseColor("#00FF00"));
//            holder.ll_label.setBackgroundColor(R.drawable.loglist_corner_radius_present);
            holder.ll_label.setBackgroundResource(R.drawable.loglist_corner_radius_wfh);
//            Log.d("Present color-=>",9fdd55);
            Log.d("WFH color-=>",timesheetMyAttendanceModel_v2ArrayList.get(position).getAttendance_color());
        }else if(timesheetMyAttendanceModel_v2ArrayList.get(position).getAttendance_status().trim().contentEquals("")){
            holder.ll_label.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return timesheetMyAttendanceModel_v2ArrayList.size();
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
