package org.wrkplan.payroll.Timesheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Model.TimesheetMyAttendanceModel;
import org.wrkplan.payroll.Model.TimesheetMyAttendanceModel_v3;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAttendanceActivity_LogList_Adapter_v3 extends RecyclerView.Adapter<MyAttendanceActivity_LogList_Adapter_v3.MyViewHolder> {
    public LayoutInflater inflater;
    public static ArrayList<TimesheetMyAttendanceModel_v3> timesheetMyAttendanceModel_v3ArrayList;
    private Context context;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public MyAttendanceActivity_LogList_Adapter_v3(Context ctx, ArrayList<TimesheetMyAttendanceModel_v3> timesheetMyAttendanceModel_v3ArrayList){

        inflater = LayoutInflater.from(ctx);
        this.timesheetMyAttendanceModel_v3ArrayList = timesheetMyAttendanceModel_v3ArrayList;
    }
    @Override
    public MyAttendanceActivity_LogList_Adapter_v3.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_my_attendance_row_v3, parent, false);
//        View view = inflater.inflate(R.layout.activity_my_attendance_row1, parent, false);
        MyAttendanceActivity_LogList_Adapter_v3.MyViewHolder holder = new MyAttendanceActivity_LogList_Adapter_v3.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(MyAttendanceActivity_LogList_Adapter_v3.MyViewHolder holder, int position) {
        holder.itemView.setTag(timesheetMyAttendanceModel_v3ArrayList.get(position));

        if(!timesheetMyAttendanceModel_v3ArrayList.get(position).getDate().isEmpty()) {
//            DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy"); //changed on 21st jan
//        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");

            String inputText = timesheetMyAttendanceModel_v3ArrayList.get(position).getDate();

            Date date_log = null;
            try {
                date_log = inputFormat.parse(inputText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.tv_date.setText(outputFormat.format(date_log));
        }else if(timesheetMyAttendanceModel_v3ArrayList.get(position).getDate().isEmpty()){
            holder.tv_date.setText(timesheetMyAttendanceModel_v3ArrayList.get(position).getDate());
        }
        holder.tv_in_time.setText(timesheetMyAttendanceModel_v3ArrayList.get(position).getTime());
        holder.tv_out_time.setText(timesheetMyAttendanceModel_v3ArrayList.get(position).getStatus());
       /* if(timesheetMyAttendanceModelArrayList.get(position).getWork_from_home().contentEquals("1")){
            holder.img_view_home.setVisibility(View.VISIBLE);
        }else {
            holder.img_view_home.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return timesheetMyAttendanceModel_v3ArrayList.size();
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