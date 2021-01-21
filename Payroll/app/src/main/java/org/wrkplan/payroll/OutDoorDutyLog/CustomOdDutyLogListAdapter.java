package org.wrkplan.payroll.OutDoorDutyLog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Model.OutDoorLogListModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomOdDutyLogListAdapter extends RecyclerView.Adapter<CustomOdDutyLogListAdapter.MyViewHolder> {
    public LayoutInflater inflater;
    public static ArrayList<OutDoorLogListModel> outDoorLogListModelArrayList;
    private Context context;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public CustomOdDutyLogListAdapter(Context ctx, ArrayList<OutDoorLogListModel> outDoorLogListModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.outDoorLogListModelArrayList = outDoorLogListModelArrayList;
    }
    @Override
    public CustomOdDutyLogListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_od_duty_log_list_row, parent, false);
        CustomOdDutyLogListAdapter.MyViewHolder holder = new CustomOdDutyLogListAdapter.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomOdDutyLogListAdapter.MyViewHolder holder, int position) {
        holder.itemView.setTag(outDoorLogListModelArrayList.get(position));

//        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy"); //again changed on 21st jan
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");

        String inputText = outDoorLogListModelArrayList.get(position).getOd_duty_log_date();

        Date date_log = null;
        try {
            date_log = inputFormat.parse(inputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_date.setText(outputFormat.format(date_log));
    }

    @Override
    public int getItemCount() {
        return outDoorLogListModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_date;
        RelativeLayout relative_layout;
        Button btn_view_time_log, btn_view_task;



        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            relative_layout = itemView.findViewById(R.id.relative_layout);
            btn_view_time_log = itemView.findViewById(R.id.btn_view_time_log);
            btn_view_task = itemView.findViewById(R.id.btn_view_task);
            btn_view_time_log.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();

                    OdDutyLogListActivity.od_request_id = outDoorLogListModelArrayList.get(position).getOd_request_id();
//                    DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                    String inputText = outDoorLogListModelArrayList.get(position).getOd_duty_log_date();

                    Date date_log = null;
                    try {
                        date_log = inputFormat.parse(inputText);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    OdDutyLogListActivity.od_log_date = outputFormat.format(date_log);

                    Intent i = new Intent(context, OdDutyLogDetailActivity.class);
                    context.startActivity(i);
                }
            });

            btn_view_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();

                    OdDutyLogListActivity.od_request_id = outDoorLogListModelArrayList.get(position).getOd_request_id();
//                    DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy"); //-again changed on 21st jan
                    DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                    String inputText = outDoorLogListModelArrayList.get(position).getOd_duty_log_date();

                    Date date_log = null;
                    try {
                        date_log = inputFormat.parse(inputText);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    OdDutyLogListActivity.od_log_date = outputFormat.format(date_log);

                    userSingletonModel.setLog_employee_id(outDoorLogListModelArrayList.get(position).getEmployee_id());
                    userSingletonModel.setLog_task_employee_name(outDoorLogListModelArrayList.get(position).getEmployee_name());
                    userSingletonModel.setLog_task_date(outputFormat.format(date_log));
                    OdDutyLogListActivity.log_task_status = 0;

                    Intent i = new Intent(context, OdDutyLogEmployeeTaskActivity.class);
                    context.startActivity(i);
                }
            });

        }

    }
}
