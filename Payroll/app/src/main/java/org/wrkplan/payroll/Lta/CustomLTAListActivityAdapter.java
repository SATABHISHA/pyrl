package org.wrkplan.payroll.Lta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Model.LTAModel;
import org.wrkplan.payroll.Model.OutDoorListModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.OutDoorDuty.CustomOutdoorListAdapter;
import org.wrkplan.payroll.OutDoorDuty.OutDoorRequestActivity;
import org.wrkplan.payroll.OutDoorDuty.OutdoorListActivity;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.wrkplan.payroll.Lta.LtaListActivity.customLTAListActivityAdapter;

public class CustomLTAListActivityAdapter extends RecyclerView.Adapter<CustomLTAListActivityAdapter.MyViewHolder> {
    public LayoutInflater inflater;
    public static ArrayList<LTAModel> ltaModelArrayList;
    private Context context;
    public static String od_request_id = "";
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public CustomLTAListActivityAdapter(Context ctx, ArrayList<LTAModel> ltaModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.ltaModelArrayList = ltaModelArrayList;
    }
    @Override
    public CustomLTAListActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_lta_list_custom_row, parent, false);
        CustomLTAListActivityAdapter.MyViewHolder holder = new CustomLTAListActivityAdapter.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomLTAListActivityAdapter.MyViewHolder holder, int position) {
        holder.itemView.setTag(ltaModelArrayList.get(position));
        holder.tv_lta_no.setText(ltaModelArrayList.get(position).getLta_application_no());
        holder.tv_lta_status.setText(ltaModelArrayList.get(position).getLta_application_status());
        if(ltaModelArrayList.get(position).getLta_application_status().contentEquals("Saved")){
            holder.imgbtn_delete.setVisibility(View.VISIBLE);
        }else {
            holder.imgbtn_delete.setVisibility(View.INVISIBLE);
        }


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



//        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
      /*  DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy"); //agein changed on 21st jan
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String inputText = ltaModelArrayList.get(position).getDate_to();
        Date date1 = null;
        try {
            date1 = inputFormat.parse(inputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputText = outputFormat.format(date1);
        holder.tv_od_date.setText(outputText);*/
       /* String inputTextFromDate = outDoorListModelArrayList.get(position).getFrom_date();
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
        }*/
    }

    @Override
    public int getItemCount() {
        return ltaModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_lta_no, tv_lta_amount, tv_lta_date, tv_lta_status;
        ImageButton imgbtn_delete;
        RelativeLayout relative_layout;



        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_lta_no = itemView.findViewById(R.id.tv_lta_no);
            tv_lta_amount = itemView.findViewById(R.id.tv_lta_amount);
            tv_lta_date = itemView.findViewById(R.id.tv_lta_date);
            tv_lta_status = itemView.findViewById(R.id.tv_lta_status);
            relative_layout = itemView.findViewById(R.id.relative_layout);
            imgbtn_delete = itemView.findViewById(R.id.imgbtn_delete);

            relative_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    LtaListActivity.EmployeeType = "Employee";
                    LtaListActivity.mediclaim_status = ltaModelArrayList.get(position).getLta_application_status();
                    LtaListActivity.new_create_yn = 0;
                    LtaListActivity.lta_application_id = ltaModelArrayList.get(position).getLta_application_id();
                    LtaListActivity.employee_id = ltaModelArrayList.get(position).getEmployee_id();
//                    od_request_id = outDoorListModelArrayList.get(position).getOd_request_id();
                    Intent i = new Intent(context, LtaRequestActivity.class);
                    context.startActivity(i);
                }
            });

            imgbtn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final int position = getAdapterPosition();
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    builder.setMessage("Do you really want to delete this LTA Requsition Request ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    delete_api_call(position);
                                    dialog.cancel();
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


    }


    public void delete_api_call(final int position){
        String url = Url.BASEURL()+"lta/delete/"+userSingletonModel.getCorporate_id()+"/"+ltaModelArrayList.get(position).getLta_application_id();
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
                                ltaModelArrayList.remove(position);
                                customLTAListActivityAdapter.notifyItemRemoved(position);
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
    }
}
