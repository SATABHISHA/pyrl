package org.wrkplan.payroll.Notifications;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Home.CustomDashboardPendingItemsListAdapter;
import org.wrkplan.payroll.Home.DashboardActivity;
import org.wrkplan.payroll.Model.DashboardPendingItemModel;
import org.wrkplan.payroll.Model.NotificationModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.MyLeaveApplication2.MyLeaveApplication2Activity;
import org.wrkplan.payroll.OutDoorDuty.OutDoorRequestActivity;
import org.wrkplan.payroll.OutDoorDuty.SubordinateOutDoorRequestActivity;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class CustomNotificationAdapter extends RecyclerView.Adapter<CustomNotificationAdapter.MyViewHolder> {
    public LayoutInflater inflater;
    public static ArrayList<NotificationModel> notificationModelArrayList;
    private Context context;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    RelativeLayout relative_layout;

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public CustomNotificationAdapter(Context ctx, ArrayList<NotificationModel> notificationModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.notificationModelArrayList = notificationModelArrayList;
    }
    @Override
    public CustomNotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_notification_row, parent, false);
        CustomNotificationAdapter.MyViewHolder holder = new CustomNotificationAdapter.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomNotificationAdapter.MyViewHolder holder, int position) {
        holder.itemView.setTag(notificationModelArrayList.get(position));
        holder.tv_message.setText(notificationModelArrayList.get(position).getMessage());
        if (notificationModelArrayList.get(position).getEvent_name().contentEquals("Leave Application")){
            holder.tv_event_name_abbrebiation.setText("LA");
            holder.rl_event_name_abbrebiation.setBackgroundColor(Color.parseColor("#9cc1e4"));
        }else if (notificationModelArrayList.get(position).getEvent_name().contentEquals("OD Application")){
            holder.tv_event_name_abbrebiation.setText("OD");
            holder.rl_event_name_abbrebiation.setBackgroundColor(Color.parseColor("#febf83"));
        }

    }

    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_event_name_abbrebiation, tv_message;
        RelativeLayout rl_event_name_abbrebiation, relative_layout;




        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_event_name_abbrebiation = itemView.findViewById(R.id.tv_event_name_abbrebiation);
            tv_message = itemView.findViewById(R.id.tv_message);

            rl_event_name_abbrebiation = itemView.findViewById(R.id.rl_event_name_abbrebiation);
            relative_layout = itemView.findViewById(R.id.relative_layout);

            relative_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();

                    if (notificationModelArrayList.get(position).getEvent_name().contentEquals("Leave Application")){
                        DashboardActivity.event_id = notificationModelArrayList.get(position).getEvent_id();
                        DashboardActivity.NotificationPendingItemsYN = true;
                        Url.isSubordinateLeaveApplication=true;
                        Intent i = new Intent(context, MyLeaveApplication2Activity.class);
                        context.startActivity(i);

                    }else if (notificationModelArrayList.get(position).getEvent_name().contentEquals("OD Application")){
                        DashboardActivity.event_id = notificationModelArrayList.get(position).getEvent_id();
                        DashboardActivity.NotificationPendingItemsYN = true;

                        Intent i = new Intent(context, SubordinateOutDoorRequestActivity.class);
                        context.startActivity(i);
                    }
                }
            });

        }

    }


}
