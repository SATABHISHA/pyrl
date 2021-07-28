package org.wrkplan.payroll.AdvanceRequisition;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Model.Subordinate_Requsition_Model;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class CustomSubordinateRequisitionListAdapter extends RecyclerView.Adapter<CustomSubordinateRequisitionListAdapter.ViewHolder> {

    Context context;
    ArrayList<Subordinate_Requsition_Model> subrequsitionList=new ArrayList<>();
    public static String requisition_id="";
    public static String requisition_no="";
    public static String requisition_amount="";
    public static String description="";
    public static String approved_requisition_amount="";
    public static String requisition_status="";
    public static String supervisor_remark="";
    public static String reason="";
    public static String return_period_in_months="";
    public static String employee_name="";


    public CustomSubordinateRequisitionListAdapter(Context context, ArrayList<Subordinate_Requsition_Model> subrequsitionList) {
        this.context = context;
        this.subrequsitionList = subrequsitionList;
    }

    @NonNull
    @Override
    public CustomSubordinateRequisitionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_subordinate_requsition_list_custom_row,parent,false);
        return new CustomSubordinateRequisitionListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomSubordinateRequisitionListAdapter.ViewHolder holder, int position) {

        Subordinate_Requsition_Model subordinateRequsitionModel=subrequsitionList.get(position);
        holder.tv_sub_employee_name.setText(subrequsitionList.get(position).getEmployee_name());
        holder.tv_sub_requisition_no.setText(subrequsitionList.get(position).getRequisition_no());
        holder.tv_sub_requisition_date.setText(subrequsitionList.get(position).getRequisition_date());
        holder.tv_sub_requisition_amount.setText(subrequsitionList.get(position).getRequisition_amount());
        if(subrequsitionList.get(position).getRequisition_status().contentEquals("Approved"))
        {
            holder.tv_sub_requisition_status.setText(subrequsitionList.get(position).getRequisition_status());
            holder.tv_sub_requisition_status.setTextColor(Color.parseColor("#1e9547"));
        }
        else if(subrequsitionList.get(position).getRequisition_status().contentEquals("Canceled"))
        {
            holder.tv_sub_requisition_status.setText(subrequsitionList.get(position).getRequisition_status());
            holder.tv_sub_requisition_status.setTextColor(Color.parseColor("#ed1c24"));
        }
        else if(subrequsitionList.get(position).getRequisition_status().contentEquals("Returned"))
        {
            holder.tv_sub_requisition_status.setText(subrequsitionList.get(position).getRequisition_status());
            holder.tv_sub_requisition_status.setTextColor(Color.parseColor("#b04d0b"));
        }
        else if(subrequsitionList.get(position).getRequisition_status().contentEquals("Submitted"))
        {
            holder.tv_sub_requisition_status.setText(subrequsitionList.get(position).getRequisition_status());
            holder.tv_sub_requisition_status.setTextColor(Color.parseColor("#fe52ce"));
        }
        else if(subrequsitionList.get(position).getRequisition_status().contentEquals("Saved"))
        {
            holder.tv_sub_requisition_status.setText(subrequsitionList.get(position).getRequisition_status());
            holder.tv_sub_requisition_status.setTextColor(Color.parseColor("#2196ed"));
        }
        else if(subrequsitionList.get(position).getRequisition_status().contentEquals("Payment done"))
        {
            holder.tv_sub_requisition_status.setText(subrequsitionList.get(position).getRequisition_status());
            holder.tv_sub_requisition_status.setTextColor(Color.parseColor("#4A47F2"));
        }

        holder.relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Url.islistclicked=true;
                Url.isNewEntry=false;
                Url.isSubordinateRequisition=true;
                Url.isMyRequisition=false;

                requisition_id=subrequsitionList.get(position).getRequisition_id();
                requisition_no=subrequsitionList.get(position).getRequisition_no();
                requisition_amount=subrequsitionList.get(position).getRequisition_amount();
                description=subrequsitionList.get(position).getDescription();
                approved_requisition_amount=subrequsitionList.get(position).getApproved_requisition_amount();
                requisition_status=subrequsitionList.get(position).getRequisition_status();
                supervisor_remark=subrequsitionList.get(position).getSupervisor_remark();
                reason=subrequsitionList.get(position).getReason();
                return_period_in_months=subrequsitionList.get(position).getReturn_period_in_months().replace("."," ").split(" ")[0];
                employee_name=subrequsitionList.get(position).getEmployee_name();

                Intent intent=new Intent(context,AdvanceRequisitionEntryActivity.class);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return subrequsitionList.size();
    }

    public void filltered(ArrayList<Subordinate_Requsition_Model> filterlist) {
        subrequsitionList=filterlist;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sub_employee_name;
        TextView tv_sub_requisition_no;
        TextView tv_sub_requisition_date;
        TextView tv_sub_requisition_status;
        TextView tv_sub_requisition_amount;
        RelativeLayout relative_layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sub_employee_name=itemView.findViewById(R.id.tv_sub_employee_name);
            tv_sub_requisition_no=itemView.findViewById(R.id.tv_sub_requisition_no);
            tv_sub_requisition_date=itemView.findViewById(R.id.tv_sub_requisition_date);
            tv_sub_requisition_status=itemView.findViewById(R.id.tv_sub_requisition_status);
            tv_sub_requisition_amount=itemView.findViewById(R.id.tv_sub_requisition_amount);
            relative_layout=itemView.findViewById(R.id.relative_layout);
        }
    }
}
