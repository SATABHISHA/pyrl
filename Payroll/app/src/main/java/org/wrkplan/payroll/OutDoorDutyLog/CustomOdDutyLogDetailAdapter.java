package org.wrkplan.payroll.OutDoorDutyLog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Model.OutDoorDetailModel;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomOdDutyLogDetailAdapter extends RecyclerView.Adapter<CustomOdDutyLogDetailAdapter.MyViewHolder> {
    public LayoutInflater inflater;
    public static ArrayList<OutDoorDetailModel> outDoorDetailModelArrayList;
    private Context context;

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public CustomOdDutyLogDetailAdapter(Context ctx, ArrayList<OutDoorDetailModel> outDoorDetailModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.outDoorDetailModelArrayList = outDoorDetailModelArrayList;
    }

    @Override
    public CustomOdDutyLogDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_od_duty_log_detail_row, parent, false);
        CustomOdDutyLogDetailAdapter.MyViewHolder holder = new CustomOdDutyLogDetailAdapter.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomOdDutyLogDetailAdapter.MyViewHolder holder, int position) {
        holder.itemView.setTag(outDoorDetailModelArrayList.get(position));

        holder.tv_log_action.setText(outDoorDetailModelArrayList.get(position).getLog_action());
        holder.tv_latitude.setText(outDoorDetailModelArrayList.get(position).getLatitude());
        holder.tv_longitude.setText(outDoorDetailModelArrayList.get(position).getLongitude());

        String address = outDoorDetailModelArrayList.get(position).getLocation_address().replaceAll("null","");
//        holder.tv_address.setText(outDoorDetailModelArrayList.get(position).getLocation_address());
        holder.tv_address.setText(address);

//        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a"); //again changed on 21st jan
        DateFormat outputFormat = new SimpleDateFormat("hh:mm a");

        String inputText = outDoorDetailModelArrayList.get(position).getLog_datetime();

        Date time_log = null;
        try {
            time_log = inputFormat.parse(inputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_log_time.setText(outputFormat.format(time_log));
    }

    @Override
    public int getItemCount() {
        return outDoorDetailModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_log_action, tv_log_time, tv_latitude, tv_longitude, tv_address;


        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_log_action = itemView.findViewById(R.id.tv_log_action);
            tv_log_time = itemView.findViewById(R.id.tv_log_time);
            tv_latitude = itemView.findViewById(R.id.tv_latitude);
            tv_longitude = itemView.findViewById(R.id.tv_longitude);
            tv_address = itemView.findViewById(R.id.tv_address);



        }

    }
}
