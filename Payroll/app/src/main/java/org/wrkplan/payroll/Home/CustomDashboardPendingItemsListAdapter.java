package org.wrkplan.payroll.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Model.DashboardPendingItemModel;
import org.wrkplan.payroll.Model.OutDoorLogListModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.MyLeaveApplication2.MyLeaveApplication2Activity;
import org.wrkplan.payroll.OutDoorDuty.OutDoorRequestActivity;
import org.wrkplan.payroll.OutDoorDuty.SubordinateOutDoorRequestActivity;
import org.wrkplan.payroll.OutDoorDutyLog.CustomSubordinateOdDutyLogListAdapter;
import org.wrkplan.payroll.OutDoorDutyLog.OdDutyLogDetailActivity;
import org.wrkplan.payroll.OutDoorDutyLog.OdDutyLogEmployeeTaskActivity;
import org.wrkplan.payroll.OutDoorDutyLog.OdDutyLogListActivity;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomDashboardPendingItemsListAdapter extends RecyclerView.Adapter<CustomDashboardPendingItemsListAdapter.MyViewHolder> {
    public LayoutInflater inflater;
    public static ArrayList<DashboardPendingItemModel> dashboardPendingItemModelArrayList;
    private Context context;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public CustomDashboardPendingItemsListAdapter(Context ctx, ArrayList<DashboardPendingItemModel> dashboardPendingItemModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.dashboardPendingItemModelArrayList = dashboardPendingItemModelArrayList;
    }
    @Override
    public CustomDashboardPendingItemsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_pending_items_row, parent, false);
        CustomDashboardPendingItemsListAdapter.MyViewHolder holder = new CustomDashboardPendingItemsListAdapter.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomDashboardPendingItemsListAdapter.MyViewHolder holder, int position) {
        holder.itemView.setTag(dashboardPendingItemModelArrayList.get(position));
        holder.tv_event_status.setText(dashboardPendingItemModelArrayList.get(position).getEvent_status());
        holder.tv_event_type.setText(dashboardPendingItemModelArrayList.get(position).getEvent_type());
        holder.tv_event_owner_name.setText(dashboardPendingItemModelArrayList.get(position).getEvent_owner_name());

        if (dashboardPendingItemModelArrayList.get(position).getEvent_status().contentEquals("Submitted")){
            holder.tv_event_status.setTextColor(Color.parseColor("#aa1667"));
        }else if (dashboardPendingItemModelArrayList.get(position).getEvent_status().contentEquals("Returned")){
            holder.tv_event_status.setTextColor(Color.parseColor("#fd3d3d"));
        }

        if (dashboardPendingItemModelArrayList.get(position).getEvent_name().contentEquals("Leave Application")){
            holder.tv_event_name_abbrebiation.setText("LA");
//            holder.rl_event_name_abbrebiation.setBackgroundColor(Color.parseColor("#9cc1e4"));
            holder.rl_event_name_abbrebiation.setBackgroundResource(R.drawable.activity_dashboard_pending_items_la_shape);
        }else if (dashboardPendingItemModelArrayList.get(position).getEvent_name().contentEquals("OD Application")){
            holder.tv_event_name_abbrebiation.setText("OD");
//            holder.rl_event_name_abbrebiation.setBackgroundColor(Color.parseColor("#febf83"));
            holder.rl_event_name_abbrebiation.setBackgroundResource(R.drawable.activity_dashboard_pending_items_od_shape);
        }

    }

    @Override
    public int getItemCount() {
        return dashboardPendingItemModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_event_status, tv_event_type, tv_event_owner_name, tv_event_name_abbrebiation;
        RelativeLayout rl_event_name_abbrebiation, relative_layout;




        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_event_status = itemView.findViewById(R.id.tv_event_status);
            tv_event_type = itemView.findViewById(R.id.tv_event_type);
            tv_event_owner_name = itemView.findViewById(R.id.tv_event_owner_name);
            tv_event_name_abbrebiation = itemView.findViewById(R.id.tv_event_name_abbrebiation);
            rl_event_name_abbrebiation = itemView.findViewById(R.id.rl_event_name_abbrebiation);
            relative_layout = itemView.findViewById(R.id.relative_layout);

            relative_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();

                    if (dashboardPendingItemModelArrayList.get(position).getEvent_name().contentEquals("Leave Application")){
                        DashboardActivity.event_id = dashboardPendingItemModelArrayList.get(position).getEvent_id();
                        DashboardActivity.event_owner_id = dashboardPendingItemModelArrayList.get(position).getEvent_owner_id();
                        DashboardActivity.LeaveType = dashboardPendingItemModelArrayList.get(position).getEvent_type();
                        DashboardActivity.NotificationPendingItemsYN = true;
                        Url.isSubordinateLeaveApplication=true;
                        Intent i = new Intent(context, MyLeaveApplication2Activity.class);
                        context.startActivity(i);

                    }else if (dashboardPendingItemModelArrayList.get(position).getEvent_name().contentEquals("OD Application")){
                        DashboardActivity.event_id = dashboardPendingItemModelArrayList.get(position).getEvent_id();
                        DashboardActivity.event_owner_id = dashboardPendingItemModelArrayList.get(position).getEvent_owner_id();
                        DashboardActivity.LeaveType = dashboardPendingItemModelArrayList.get(position).getEvent_type();
                        DashboardActivity.NotificationPendingItemsYN = true;

                        Intent i = new Intent(context, SubordinateOutDoorRequestActivity.class);
                        context.startActivity(i);
                    }

                }
            });

        }

    }
}
