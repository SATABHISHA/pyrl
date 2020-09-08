package org.wrkplan.payroll.Timesheet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Model.OutDoorLogListModel;
import org.wrkplan.payroll.Model.TimesheetMyAttendanceModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.OutDoorDutyLog.CustomOdDutyLogListAdapter;
import org.wrkplan.payroll.OutDoorDutyLog.OdDutyLogDetailActivity;
import org.wrkplan.payroll.OutDoorDutyLog.OdDutyLogEmployeeTaskActivity;
import org.wrkplan.payroll.OutDoorDutyLog.OdDutyLogListActivity;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAttendanceListAdapter extends RecyclerView.Adapter<MyAttendanceListAdapter.MyViewHolder> {
    public LayoutInflater inflater;
    public static ArrayList<TimesheetMyAttendanceModel> timesheetMyAttendanceModelArrayList;
    private Context context;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public MyAttendanceListAdapter(Context ctx, ArrayList<TimesheetMyAttendanceModel> timesheetMyAttendanceModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.timesheetMyAttendanceModelArrayList = timesheetMyAttendanceModelArrayList;
    }
    @Override
    public MyAttendanceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_my_attendance_row, parent, false);
//        View view = inflater.inflate(R.layout.activity_my_attendance_row1, parent, false);
        MyAttendanceListAdapter.MyViewHolder holder = new MyAttendanceListAdapter.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(MyAttendanceListAdapter.MyViewHolder holder, int position) {
        holder.itemView.setTag(timesheetMyAttendanceModelArrayList.get(position));

//        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");

        String inputText = timesheetMyAttendanceModelArrayList.get(position).getTs_date();

        Date date_log = null;
        try {
            date_log = inputFormat.parse(inputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_date.setText(outputFormat.format(date_log));
        holder.tv_in_time.setText(timesheetMyAttendanceModelArrayList.get(position).getTime_in());
        holder.tv_out_time.setText(timesheetMyAttendanceModelArrayList.get(position).getTime_out());
        if(timesheetMyAttendanceModelArrayList.get(position).getWork_from_home().contentEquals("1")){
            holder.img_view_home.setVisibility(View.VISIBLE);
        }else {
            holder.img_view_home.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return timesheetMyAttendanceModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_date, tv_in_time, tv_out_time;
        ImageView img_view_home;

        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_in_time = itemView.findViewById(R.id.tv_in_time);
            tv_out_time = itemView.findViewById(R.id.tv_out_time);
            img_view_home = itemView.findViewById(R.id.img_view_home);


        }

    }
}
