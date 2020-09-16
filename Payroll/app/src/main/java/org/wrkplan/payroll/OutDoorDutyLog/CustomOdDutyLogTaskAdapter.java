package org.wrkplan.payroll.OutDoorDutyLog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Model.OutDoorTaskModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

import static org.wrkplan.payroll.OutDoorDutyLog.OdDutyLogEmployeeTaskActivity.customOdDutyLogTaskAdapter;

public class CustomOdDutyLogTaskAdapter extends RecyclerView.Adapter<CustomOdDutyLogTaskAdapter.MyViewHolder> {
    public LayoutInflater inflater;
    public static ArrayList<OutDoorTaskModel> outDoorTaskModelArrayList;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    private Context context;

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public CustomOdDutyLogTaskAdapter(Context ctx, ArrayList<OutDoorTaskModel> outDoorTaskModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.outDoorTaskModelArrayList = outDoorTaskModelArrayList;
    }
    @Override
    public CustomOdDutyLogTaskAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_od_duty_log_task_row, parent, false);
        CustomOdDutyLogTaskAdapter.MyViewHolder holder = new CustomOdDutyLogTaskAdapter.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomOdDutyLogTaskAdapter.MyViewHolder holder, int position) {
        holder.itemView.setTag(outDoorTaskModelArrayList.get(position));
        holder.tv_task_name.setText(outDoorTaskModelArrayList.get(position).getTask_name());
        holder.tv_description.setText(outDoorTaskModelArrayList.get(position).getTask_description());
    }

    @Override
    public int getItemCount() {
        return outDoorTaskModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_task_name, tv_description;
        Button btn_edit_task, btn_delete_task;

        public MyViewHolder(final View itemView) {
            super(itemView);
            final int position = getAdapterPosition();
            tv_task_name = itemView.findViewById(R.id.tv_task_name);
            tv_description = itemView.findViewById(R.id.tv_description);
            btn_edit_task = itemView.findViewById(R.id.btn_edit_task);
            btn_delete_task = itemView.findViewById(R.id.btn_delete_task);

            /*if(HomeActivity.log_task_status == 1){
                btn_edit_task.setVisibility(View.GONE);
            }else if(HomeActivity.log_task_status == 0){
                if(OdDutyLogEmployeeTaskActivity.status_user.contentEquals("Approved") ||
                        OdDutyLogEmployeeTaskActivity.status_user.contentEquals("Cancelled")){
                    btn_edit_task.setVisibility(View.GONE);
                }else{
                    btn_edit_task.setVisibility(View.VISIBLE);
                }
            }else{
                btn_edit_task.setVisibility(View.VISIBLE);
            }*/
            if(OdDutyLogListActivity.log_task_status == 1){
                btn_edit_task.setVisibility(View.GONE);
                btn_delete_task.setVisibility(View.GONE);
            }else{
                /*btn_edit_task.setVisibility(View.VISIBLE);
                btn_delete_task.setVisibility(View.VISIBLE);*/

                if(OdDutyLogEmployeeTaskActivity.task_status.contentEquals("Saved")){
                    btn_edit_task.setVisibility(View.VISIBLE);
                    btn_delete_task.setVisibility(View.VISIBLE);
                }else if(OdDutyLogEmployeeTaskActivity.task_status.contentEquals("Returned")){
                    btn_edit_task.setVisibility(View.VISIBLE);
                    btn_delete_task.setVisibility(View.GONE);
                }else if(OdDutyLogEmployeeTaskActivity.task_status.contentEquals("")){
                    btn_edit_task.setVisibility(View.VISIBLE);
                    btn_delete_task.setVisibility(View.VISIBLE);
                }else{
                    btn_edit_task.setVisibility(View.GONE);
                    btn_delete_task.setVisibility(View.GONE);
                }
            }

            btn_edit_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();

                    LayoutInflater li = LayoutInflater.from(context);
                    final View dialog = li.inflate(R.layout.od_duty_log_employee_task_popup, null);
                    final EditText ed_task_name = (EditText) dialog.findViewById(R.id.ed_task_name);
                    final EditText ed_description = (EditText) dialog.findViewById(R.id.ed_description);
                    TextView tv_button_cancel = dialog.findViewById(R.id.tv_button_cancel);
                    final TextView tv_dialog_button_save = dialog.findViewById(R.id.tv_button_save);
                    tv_dialog_button_save.setText("Modify");



                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setView(dialog);
                    alert.setCancelable(false);
                    //Creating an alert dialog
                    final AlertDialog alertDialog = alert.create();
                    alertDialog.show();

                    ed_task_name.setText(outDoorTaskModelArrayList.get(position).getTask_name());
                    ed_description.setText(outDoorTaskModelArrayList.get(position).getTask_description());

                    ed_task_name.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(ed_task_name.getText().toString().trim().equals("")){

                                tv_dialog_button_save.setClickable(false);
                                tv_dialog_button_save.setAlpha(0.6f);

                                ed_description.setClickable(false);
                                ed_description.setEnabled(false);
                                ed_description.setAlpha(0.6f);
                            }else if(!ed_task_name.getText().toString().trim().equals("")){

                                ed_description.setClickable(true);
                                ed_description.setEnabled(true);
                                ed_description.setAlpha(1.0f);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    ed_description.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(ed_description.getText().toString().trim().equals("")){
                                //write your code here
                                tv_dialog_button_save.setClickable(false);
                                tv_dialog_button_save.setAlpha(0.6f);
                            }else{
                                tv_dialog_button_save.setClickable(true);
                                tv_dialog_button_save.setAlpha(1.0f);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    tv_button_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    tv_dialog_button_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            outDoorTaskModelArrayList.get(position).setTask_name(ed_task_name.getText().toString());
                            outDoorTaskModelArrayList.get(position).setTask_description(ed_description.getText().toString());
                            customOdDutyLogTaskAdapter.notifyDataSetChanged();


                            OdDutyLogEmployeeTaskActivity.back_btn_save_unsave_check = 1;
                            alertDialog.dismiss();
                        }
                    });
                }
            });

            btn_delete_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    builder.setMessage("Are you sure, you want to delete task ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    if(outDoorTaskModelArrayList.get(position).getTask_delete_api_call() == 1){
                                        /*outDoorTaskModelArrayList.remove(position);
                                        customOdDutyLogTaskAdapter.notifyItemRemoved(position);*/
//                                        Toast.makeText(context,"Call Api",Toast.LENGTH_LONG).show();
                                        delete_api_call(position);
                                    }else{
                                        outDoorTaskModelArrayList.remove(position);
                                        customOdDutyLogTaskAdapter.notifyItemRemoved(position);
                                        Toast.makeText(context,"OD task deleted successfully.",Toast.LENGTH_LONG).show();
                                    }
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


//                    customOdDutyLogTaskAdapter.notifyDataSetChanged();
                }
            });

        }

        public void delete_api_call(final int position){
            String url = Url.BASEURL()+"od/task/delete/"+userSingletonModel.getCorporate_id()+"/"+outDoorTaskModelArrayList.get(position).getOd_duty_task_detail_id();
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
                                    outDoorTaskModelArrayList.remove(position);
                                    customOdDutyLogTaskAdapter.notifyItemRemoved(position);
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
}
