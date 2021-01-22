package org.wrkplan.payroll.OutDoorDuty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Model.OutDoorListModel;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomSubordinateOutdoorListAdapter extends RecyclerView.Adapter<CustomSubordinateOutdoorListAdapter.MyViewHolder> {
    public LayoutInflater inflater;
    public static ArrayList<OutDoorListModel> outDoorListModelArrayList;
    private Context context;
    public static String od_request_id = "";
    public static int subordinate_employee_id = 0;

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public CustomSubordinateOutdoorListAdapter(Context ctx, ArrayList<OutDoorListModel> outDoorListModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.outDoorListModelArrayList = outDoorListModelArrayList;
    }
    @Override
    public CustomSubordinateOutdoorListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_subordinate_outdoor_list_custom_row, parent, false);
        CustomSubordinateOutdoorListAdapter.MyViewHolder holder = new CustomSubordinateOutdoorListAdapter.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomSubordinateOutdoorListAdapter.MyViewHolder holder, int position) {
        holder.itemView.setTag(outDoorListModelArrayList.get(position));
        holder.tv_od_status.setText(outDoorListModelArrayList.get(position).getOd_status());
        if(outDoorListModelArrayList.get(position).getOd_status().contentEquals("Approved")){
            holder.tv_od_status.setTextColor(Color.parseColor("#1e9547"));
        }else if(outDoorListModelArrayList.get(position).getOd_status().contentEquals("Canceled")){
            holder.tv_od_status.setTextColor(Color.parseColor("#ed1c24"));
        }else if(outDoorListModelArrayList.get(position).getOd_status().contentEquals("Returned")){
            holder.tv_od_status.setTextColor(Color.parseColor("#b04d0b"));
        }else if(outDoorListModelArrayList.get(position).getOd_status().contentEquals("Returned")){
            holder.tv_od_status.setTextColor(Color.parseColor("#fe52ce"));
        }else if(outDoorListModelArrayList.get(position).getOd_status().contentEquals("Returned")){
            holder.tv_od_status.setTextColor(Color.parseColor("#2196ed"));
        }else if(outDoorListModelArrayList.get(position).getOd_status().contentEquals("Submit")){
            holder.tv_od_status.setTextColor(Color.parseColor("#fe52ce"));
        }

        holder.tv_od_no.setText(outDoorListModelArrayList.get(position).getOd_request_no());
        holder.tv_day_no.setText(outDoorListModelArrayList.get(position).getTotal_days());
        holder.tv_od_name.setText(outDoorListModelArrayList.get(position).getEmployee_name());



//        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy"); //--again changed on 21st jan
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
        /*String inputText = outDoorListModelArrayList.get(position).getOd_request_date();
        Date date1 = null;
        try {
            date1 = inputFormat.parse(inputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputText = outputFormat.format(date1);
        holder.tv_od_date.setText(outputText);*/

        String inputTextFromDate = outDoorListModelArrayList.get(position).getFrom_date();
        String inputTextToDate = outDoorListModelArrayList.get(position).getTo_date();

        Date dateFromDate = null, dateToate = null;
        try {
            dateFromDate = inputFormat.parse(inputTextFromDate);
            dateToate = inputFormat.parse(inputTextToDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(Double.parseDouble(outDoorListModelArrayList.get(position).getTotal_days())>1) {
            holder.tv_od_date.setText(outputFormat.format(dateFromDate) + " To  " + outputFormat.format(dateToate));
        }else{
            holder.tv_od_date.setText(outputFormat.format(dateFromDate));
        }

    }

    @Override
    public int getItemCount() {
        return outDoorListModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_od_no, tv_od_date, tv_od_status, tv_day_no, tv_od_name;
        RelativeLayout relative_layout;



        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_od_no = itemView.findViewById(R.id.tv_od_no);
            tv_od_date = itemView.findViewById(R.id.tv_od_date);
            tv_od_status = itemView.findViewById(R.id.tv_od_status);
            tv_day_no = itemView.findViewById(R.id.tv_day_no);
            tv_od_name = itemView.findViewById(R.id.tv_od_name);
            relative_layout = itemView.findViewById(R.id.relative_layout);

            relative_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    od_request_id = outDoorListModelArrayList.get(position).getOd_request_id();
                    subordinate_employee_id = Integer.parseInt(outDoorListModelArrayList.get(position).getEmployee_id());
                    Intent i = new Intent(context, SubordinateOutDoorRequestActivity.class);
                    context.startActivity(i);
                }
            });
        }


    }
}