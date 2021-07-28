package org.wrkplan.payroll.AdvanceRequisition;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Model.Requisition_Model;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class CustomRequisitionListAdapter extends RecyclerView.Adapter<CustomRequisitionListAdapter.ViewHolder> {
        Context context;
        ArrayList<Requisition_Model> requisitionlist=new ArrayList<>();
        UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
        public static String requisition_id="";
        public static String requisition_no="";
        public static String requisition_amount="";
        public static String description="";
        public static String approved_requisition_amount="";
        public static String requisition_status="";
        public static String supervisor_remark="";
        public static String reason="";
        public static String return_period_in_months="";







        public CustomRequisitionListAdapter(Context context, ArrayList<Requisition_Model> requisitionlist) {
                this.context = context;
                this.requisitionlist = requisitionlist;
        }

        @NonNull
        @Override
        public CustomRequisitionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_advance_requsition_list_custom_row,parent,false);
                return new CustomRequisitionListAdapter.ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull CustomRequisitionListAdapter.ViewHolder holder, int position) {

                Requisition_Model requisition_model=requisitionlist.get(position);
                holder.tv_requisition_no.setText(requisitionlist.get(position).getRequisition_no());
                holder.tv_requisition_date.setText(requisitionlist.get(position).getRequisition_date());
                holder.tv_requisition_amount.setText(requisitionlist.get(position).getRequisition_amount());
                if(requisitionlist.get(position).getRequisition_status().contentEquals("Approved"))
                {
                        holder.tv_requisition_status.setText(requisitionlist.get(position).getRequisition_status());
                        holder.tv_requisition_status.setTextColor(Color.parseColor("#1e9547"));
                }
                else if(requisitionlist.get(position).getRequisition_status().contentEquals("Canceled"))
                {
                        holder.tv_requisition_status.setText(requisitionlist.get(position).getRequisition_status());
                        holder.tv_requisition_status.setTextColor(Color.parseColor("#ed1c24"));
                }
                else if(requisitionlist.get(position).getRequisition_status().contentEquals("Returned"))
                {
                        holder.tv_requisition_status.setText(requisitionlist.get(position).getRequisition_status());
                        holder.tv_requisition_status.setTextColor(Color.parseColor("#b04d0b"));
                }
                else if(requisitionlist.get(position).getRequisition_status().contentEquals("Submitted"))
                {
                        holder.tv_requisition_status.setText(requisitionlist.get(position).getRequisition_status());
                        holder.tv_requisition_status.setTextColor(Color.parseColor("#fe52ce"));
                }
                else if(requisitionlist.get(position).getRequisition_status().contentEquals("Saved"))
                {
                        holder.tv_requisition_status.setText(requisitionlist.get(position).getRequisition_status());
                        holder.tv_requisition_status.setTextColor(Color.parseColor("#2196ed"));
                        holder.imgbtn_delete.setVisibility(View.VISIBLE);
                }
                else if(requisitionlist.get(position).getRequisition_status().contentEquals("Payment done"))
                {
                        holder.tv_requisition_status.setText(requisitionlist.get(position).getRequisition_status());
                        holder.tv_requisition_status.setTextColor(Color.parseColor("#4A47F2"));
                }

                holder.relative_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                Url.islistclicked=true;
                                Url.isNewEntry=false;
                                Url.isMyRequisition=true;
                                Url.isSubordinateRequisition=false;

                                requisition_id=requisitionlist.get(position).getRequisition_id();
                                requisition_no=requisitionlist.get(position).getRequisition_no();
                                requisition_amount=requisitionlist.get(position).getRequisition_amount();
                                description=requisitionlist.get(position).getDescription();
                                approved_requisition_amount=requisitionlist.get(position).getApproved_requisition_amount();
                                requisition_status=requisitionlist.get(position).getRequisition_status();
                                supervisor_remark=requisitionlist.get(position).getSupervisor_remark();
                                reason=requisitionlist.get(position).getReason();
                                return_period_in_months=requisitionlist.get(position).getReturn_period_in_months().replace("."," ").split(" ")[0];

                                Intent intent=new Intent(context,AdvanceRequisitionEntryActivity.class);
                                context.startActivity(intent);

                        }
                });

                holder.imgbtn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                                builder.setMessage("Do you really want to delete this record "+requisitionlist.get(position).getRequisition_no()+ " ?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                        delete_api_record(position);
                                                        dialog.cancel();
                                                        //Toast.makeText(context, "Delet item successfully of Position => "+position, Toast.LENGTH_SHORT).show();
                                                }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.cancel();
                                                }
                                        });
                                androidx.appcompat.app.AlertDialog alert = builder.create();
                                alert.show();
                        }
                });
        }

        private void delete_api_record(int position) {
                String url = Url.BASEURL()+"advance-requisition/delete/"+userSingletonModel.getCorporate_id()+"/"+requisitionlist.get(position).getRequisition_id();

                Log.d("delet_url=>",url);
                final ProgressDialog loading = ProgressDialog.show(context, "Loading", "Please wait...", true, false);
                StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                                try {
                                        JSONObject jsonObject=new JSONObject(response);
                                        JSONObject jb1=jsonObject.getJSONObject("response");
                                        if(jb1.getString("status").contentEquals("true"))
                                        {
                                                String message=jb1.getString("message");
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                                requisitionlist.remove(position);
                                                AdvanceRequisitionActivity.adapter.notifyItemRemoved(position);
                                        }
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                        loading.dismiss();
                                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                loading.dismiss();

                        }
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                                loading.dismiss();
                                Toast.makeText(context, "Could't connect to the server", Toast.LENGTH_SHORT).show();
                        }
                });
                Volley.newRequestQueue(context).add(stringRequest);
        }

        @Override
        public int getItemCount() {
                return requisitionlist.size();
        }



        public class ViewHolder extends RecyclerView.ViewHolder {

                TextView tv_requisition_no;
                TextView tv_requisition_date;
                TextView tv_requisition_status;
                TextView tv_requisition_amount;
                ImageView imgbtn_delete;
                RelativeLayout relative_layout;

                public ViewHolder(@NonNull View itemView) {
                        super(itemView);

                        tv_requisition_no=itemView.findViewById(R.id.tv_requisition_no);
                        tv_requisition_date=itemView.findViewById(R.id.tv_requisition_date);
                        tv_requisition_status=itemView.findViewById(R.id.tv_requisition_status);
                        tv_requisition_amount=itemView.findViewById(R.id.tv_requisition_amount);
                        imgbtn_delete=itemView.findViewById(R.id.imgbtn_delete);
                        relative_layout=itemView.findViewById(R.id.relative_layout);

                }
        }
}
