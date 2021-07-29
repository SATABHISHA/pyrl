package org.wrkplan.payroll.Lta;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Model.LTAModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class CustomSubordinateLTAListAdapter extends RecyclerView.Adapter<CustomSubordinateLTAListAdapter.MyViewHolder> {
    public LayoutInflater inflater;
    public ArrayList<LTAModel> ltaModelArrayList;
    private Context context;
    public static String od_request_id = "";
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public CustomSubordinateLTAListAdapter(Context ctx, ArrayList<LTAModel> ltaModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.ltaModelArrayList = ltaModelArrayList;
    }
    @Override
    public CustomSubordinateLTAListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_subordinate_lta_list_custom_row, parent, false);
        CustomSubordinateLTAListAdapter.MyViewHolder holder = new CustomSubordinateLTAListAdapter.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomSubordinateLTAListAdapter.MyViewHolder holder, int position) {
        holder.itemView.setTag(ltaModelArrayList.get(position));
        holder.tv_sub_employee_name.setText(ltaModelArrayList.get(position).getEmployee_name());
        holder.tv_lta_no.setText(ltaModelArrayList.get(position).getLta_application_no());
        holder.tv_lta_status.setText(ltaModelArrayList.get(position).getLta_application_status());



        if(ltaModelArrayList.get(position).getLta_application_status().contentEquals("Approved")){
            holder.tv_lta_status.setTextColor(Color.parseColor("#1e9547"));
        }else if(ltaModelArrayList.get(position).getLta_application_status().contentEquals("Cancelled")){
            holder.tv_lta_status.setTextColor(Color.parseColor("#ed1c24"));
        }else if(ltaModelArrayList.get(position).getLta_application_status().contentEquals("Returned")){
            holder.tv_lta_status.setTextColor(Color.parseColor("#b04d0b"));
        }else if(ltaModelArrayList.get(position).getLta_application_status().contentEquals("Submitted")){
            holder.tv_lta_status.setTextColor(Color.parseColor("#fe52ce"));
        }else if(ltaModelArrayList.get(position).getLta_application_status().contentEquals("Saved")){
            holder.tv_lta_status.setTextColor(Color.parseColor("#2196ed"));
        }
//        holder.tv_odr_request_id.setText(outDoorListModelArrayList.get(position).getOd_request_id());
        holder.tv_lta_amount.setText(ltaModelArrayList.get(position).getLta_amount());
        holder.tv_lta_date.setText(ltaModelArrayList.get(position).getDate_from()+" to "+ltaModelArrayList.get(position).getDate_to());

    }

    @Override
    public int getItemCount() {
        return ltaModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_lta_no, tv_lta_amount, tv_lta_date, tv_lta_status, tv_sub_employee_name;
        RelativeLayout relative_layout;



        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_lta_no = itemView.findViewById(R.id.tv_lta_no);
            tv_sub_employee_name = itemView.findViewById(R.id.tv_sub_employee_name);
            tv_lta_amount = itemView.findViewById(R.id.tv_lta_amount);
            tv_lta_date = itemView.findViewById(R.id.tv_lta_date);
            tv_lta_status = itemView.findViewById(R.id.tv_lta_status);
            relative_layout = itemView.findViewById(R.id.relative_layout);

            relative_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    LtaListActivity.EmployeeType = "Supervisor";
                    LtaListActivity.mediclaim_status = ltaModelArrayList.get(position).getLta_application_status();
                    LtaListActivity.new_create_yn = 0;
                    LtaListActivity.lta_application_id = ltaModelArrayList.get(position).getLta_application_id();
                    LtaListActivity.employee_id = ltaModelArrayList.get(position).getEmployee_id();
//                    od_request_id = outDoorListModelArrayList.get(position).getOd_request_id();
                    Intent i = new Intent(context, LtaRequestActivity.class);
                    context.startActivity(i);
                }
            });


        }


    }


    /*public void delete_api_call(final int position){
        String url = Url.BASEURL()+"od/request/delete/"+userSingletonModel.getCorporate_id()+"/"+outDoorListModelArrayList.get(position).getOd_request_id();
        Log.d("url-=>",url);
        final ProgressDialog loading = ProgressDialog.show(context, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                            if (jsonObject1.getString("status").contentEquals("true")) {
                                Toast.makeText(context,jsonObject1.getString("message"),Toast.LENGTH_LONG).show();
                                outDoorListModelArrayList.remove(position);
                                customOutdoorListAdapter.notifyItemRemoved(position);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loading.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }*/
}
