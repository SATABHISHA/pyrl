package org.wrkplan.payroll.Mediclaim;

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
import org.wrkplan.payroll.Model.Mediclaim.Subordinate_Mediclaim_Model;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class CustomSubordinateMediclaimListAdapter extends RecyclerView.Adapter<CustomSubordinateMediclaimListAdapter.ViewHolder> {

    ArrayList<Subordinate_Mediclaim_Model> modelArrayList=new ArrayList<>();
    Context context;
    public static String mediclaim_id="";
    public static String mediclaim_status="";
    public static  String mediclaim_no="";
    public static String employee_name="";
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();


    public CustomSubordinateMediclaimListAdapter(ArrayList<Subordinate_Mediclaim_Model> modelArrayList, Context context) {
        this.modelArrayList = modelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomSubordinateMediclaimListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_subordinate_mediclaim_list_custom_row,parent,false);
        return new CustomSubordinateMediclaimListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomSubordinateMediclaimListAdapter.ViewHolder holder, int position) {
        Subordinate_Mediclaim_Model mediclaim_model=modelArrayList.get(position);

        holder.tv_sub_employee_name.setText(modelArrayList.get(position).getEmployee_name());
        holder.tv_sub_mediclaim_no.setText(modelArrayList.get(position).getMediclaim_no());
        holder.tv_sub_mediclaim_date.setText(modelArrayList.get(position).getMediclaim_date());
        holder.tv_sub_mediclaim_amount.setText(modelArrayList.get(position).getMediclaim_amount());

        if(modelArrayList.get(position).getMediclaim_status().contentEquals("Approved"))
        {
            holder.tv_sub_mediclaim_status.setText(modelArrayList.get(position).getMediclaim_status());
            holder.tv_sub_mediclaim_status.setTextColor(Color.parseColor("#1e9547"));
        }
        else if(modelArrayList.get(position).getMediclaim_status().contentEquals("Canceled"))
        {
            holder.tv_sub_mediclaim_status.setText(modelArrayList.get(position).getMediclaim_status());
            holder.tv_sub_mediclaim_status.setTextColor(Color.parseColor("#ed1c24"));
        }
        else if(modelArrayList.get(position).getMediclaim_status().contentEquals("Returned"))
        {
            holder.tv_sub_mediclaim_status.setText(modelArrayList.get(position).getMediclaim_status());
            holder.tv_sub_mediclaim_status.setTextColor(Color.parseColor("#b04d0b"));
        }
        else if(modelArrayList.get(position).getMediclaim_status().contentEquals("Submitted"))
        {
            holder.tv_sub_mediclaim_status.setText(modelArrayList.get(position).getMediclaim_status());
            holder.tv_sub_mediclaim_status.setTextColor(Color.parseColor("#fe52ce"));
        }

        holder.relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Url.isMyMediclaim=false;
                Url.isNewEntryMediclaim=false;
                Url.isSubordinateMediclaim=true;
                mediclaim_id=modelArrayList.get(position).getMediclaim_id();
                mediclaim_status=modelArrayList.get(position).getMediclaim_status();
                mediclaim_no=modelArrayList.get(position).getMediclaim_no();
                employee_name=modelArrayList.get(position).getEmployee_name();
                Intent intent=new Intent(context,MediclaimEntryActivity.class);
                // Toast.makeText(context, "employee form", Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public void filltered(ArrayList<Subordinate_Mediclaim_Model> filterlist) {
        modelArrayList=filterlist;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_sub_mediclaim_no;
        TextView tv_sub_mediclaim_date;
        TextView tv_sub_mediclaim_status;
        TextView tv_sub_mediclaim_amount;
        TextView tv_sub_employee_name;
        RelativeLayout relative_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_sub_mediclaim_no=itemView.findViewById(R.id.tv_sub_mediclaim_no);
            tv_sub_mediclaim_date=itemView.findViewById(R.id.tv_sub_mediclaim_date);
            tv_sub_mediclaim_status=itemView.findViewById(R.id.tv_sub_mediclaim_status);
            tv_sub_mediclaim_amount=itemView.findViewById(R.id.tv_sub_mediclaim_amount);
            tv_sub_employee_name=itemView.findViewById(R.id.tv_sub_employee_name);
            relative_layout=itemView.findViewById(R.id.relative_layout);
        }
    }
}
