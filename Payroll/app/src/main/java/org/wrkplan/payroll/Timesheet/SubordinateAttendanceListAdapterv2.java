package org.wrkplan.payroll.Timesheet;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.wrkplan.payroll.Model.TimesheetSubordinateModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SubordinateAttendanceListAdapterv2 extends RecyclerView.Adapter<SubordinateAttendanceListAdapterv2.MyViewHolder> {
    public LayoutInflater inflater;
    public static ArrayList<TimesheetSubordinateModel> timesheetSubordinateModelArrayList;
    private Context context;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public SubordinateAttendanceListAdapterv2(Context ctx, ArrayList<TimesheetSubordinateModel> timesheetSubordinateModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.timesheetSubordinateModelArrayList = timesheetSubordinateModelArrayList;
    }
    @Override
    public SubordinateAttendanceListAdapterv2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_subordinate_attendance_row_v2, parent, false);
//        View view = inflater.inflate(R.layout.activity_my_attendance_row1, parent, false);
        SubordinateAttendanceListAdapterv2.MyViewHolder holder = new SubordinateAttendanceListAdapterv2.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(SubordinateAttendanceListAdapterv2.MyViewHolder holder, int position) {
        holder.itemView.setTag(timesheetSubordinateModelArrayList.get(position));

        holder.tv_emp_name.setText(timesheetSubordinateModelArrayList.get(position).getEmployee_name());
        holder.tv_in_time.setText(timesheetSubordinateModelArrayList.get(position).getTime_in());
//        holder.tv_in_time.setText("10:00AM");
        holder.tv_out_time.setText(timesheetSubordinateModelArrayList.get(position).getTime_out());
//        holder.tv_out_time.setText("04:00PM");
        holder.img_sublog_arrow.setVisibility(View.INVISIBLE);
        if(timesheetSubordinateModelArrayList.get(position).getAttendance_status().trim().contentEquals("Absent")){
            holder.tv_present_absent.setText("Absent");
            holder.tv_present_absent.setTextColor(Color.parseColor("#ffffff"));
            holder.ll_label.setVisibility(View.VISIBLE);
//            holder.ll_label.setBackgroundColor(Color.parseColor(timesheetSubordinateModelArrayList.get(position).getAttendance_color()));
//            holder.ll_label.setBackgroundColor(Color.parseColor("#FF0000"));
            holder.ll_label.setBackgroundResource(R.drawable.loglist_corner_radius_absent);
        }else if(timesheetSubordinateModelArrayList.get(position).getAttendance_status().trim().contentEquals("Present")){
            holder.tv_present_absent.setText("Present");
            holder.tv_present_absent.setTextColor(Color.parseColor("#494949"));
            holder.ll_label.setVisibility(View.VISIBLE);
//            holder.ll_label.setBackgroundColor(Color.parseColor("#00FF00"));
            holder.ll_label.setBackgroundResource(R.drawable.loglist_corner_radius_present);
            if(userSingletonModel.getAttendance_with_selfie_yn().contentEquals("1")){
                holder.img_sublog_arrow.setVisibility(View.VISIBLE);
            }else{
                holder.img_sublog_arrow.setVisibility(View.INVISIBLE);
            }
        }else if(timesheetSubordinateModelArrayList.get(position).getAttendance_status().trim().contentEquals("WFH")){
            holder.tv_present_absent.setText("WFH");
            holder.tv_present_absent.setTextColor(Color.parseColor("#ffffff"));
            holder.ll_label.setVisibility(View.VISIBLE);
//            holder.ll_label.setBackgroundColor(Color.parseColor("#00FF00"));
            holder.ll_label.setBackgroundResource(R.drawable.loglist_corner_radius_wfh);
            if(userSingletonModel.getAttendance_with_selfie_yn().contentEquals("1")){
                holder.img_sublog_arrow.setVisibility(View.VISIBLE);
            }else{
                holder.img_sublog_arrow.setVisibility(View.INVISIBLE);
            }
        }else if(timesheetSubordinateModelArrayList.get(position).getAttendance_status().trim().contentEquals("")){
            holder.ll_label.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return timesheetSubordinateModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_emp_name, tv_in_time, tv_out_time, tv_present_absent;
        ImageView img_sublog_arrow;
        LinearLayout ll_label;

        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_emp_name = itemView.findViewById(R.id.tv_emp_name);
            tv_in_time = itemView.findViewById(R.id.tv_in_time);
            tv_out_time = itemView.findViewById(R.id.tv_out_time);
            ll_label = itemView.findViewById(R.id.ll_label);
            tv_present_absent = itemView.findViewById(R.id.tv_present_absent);
            img_sublog_arrow = itemView.findViewById(R.id.img_sublog_arrow);

            img_sublog_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    loadLocationData(timesheetSubordinateModelArrayList.get(position).getEmployee_id());
                }
            });
        }

    }

    public void openLocationDetailsPopup(String employee_name,String employee_code, String attendance_date, String in_time, String in_latitude,String in_longitude, String in_address, String in_image_base_64, String out_time, String out_latitude, String out_longitude, String out_address, String out_image_base_64){
        //-------custom dialog code starts=========
        LayoutInflater li2 = LayoutInflater.from(context);
        View dialog = li2.inflate(R.layout.activity_subordinate_attendance_location_details_popup, null);
        TextView tv_employee_name = dialog.findViewById(R.id.tv_employee_name);
        TextView tv_employee_id = dialog.findViewById(R.id.tv_employee_id);
        TextView tv_attendance_date = dialog.findViewById(R.id.tv_attendance_date);
        TextView tv_in_time = dialog.findViewById(R.id.tv_in_time);
        TextView tv_latitude_in = dialog.findViewById(R.id.tv_latitude_in);
        TextView tv_longitude_in = dialog.findViewById(R.id.tv_longitude_in);
        TextView tv_in_address = dialog.findViewById(R.id.tv_in_address);
        ImageView img_view_in = dialog.findViewById(R.id.img_view_in);

        TextView tv_out_time = dialog.findViewById(R.id.tv_out_time);
        TextView tv_latitude_out = dialog.findViewById(R.id.tv_latitude_out);
        TextView tv_longitude_out = dialog.findViewById(R.id.tv_longitude_out);
        TextView tv_address_out = dialog.findViewById(R.id.tv_address_out);
        TextView tv_out = dialog.findViewById(R.id.tv_out);
        TextView tv_title_out_latitude = dialog.findViewById(R.id.tv_title_out_latitude);
        TextView tv_title_out_longitude = dialog.findViewById(R.id.tv_title_out_longitude);
        ImageView img_view_out = dialog.findViewById(R.id.img_view_out);

        TextView tv_button_ok = dialog.findViewById(R.id.tv_button_ok);


        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(context);
        alert.setView(dialog);
        //Creating an alert dialog
        final androidx.appcompat.app.AlertDialog alertDialog = alert.create();
        alertDialog.show();

        tv_employee_name.setText(employee_name);
        tv_employee_id.setText(employee_code);
        tv_attendance_date.setText(attendance_date);

        tv_in_time.setText(in_time);
        tv_latitude_in.setText(in_latitude);
        tv_longitude_in.setText(in_longitude);
        tv_in_address.setText(in_address.replace("null",""));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if(!in_image_base_64.trim().contentEquals("")) {
            byte[] imageBytes_in = byteArrayOutputStream.toByteArray();
            imageBytes_in = Base64.decode(in_image_base_64, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes_in, 0, imageBytes_in.length);
            img_view_in.setImageBitmap(decodedImage);
            img_view_in.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        if(out_latitude.trim().contentEquals("") ||
                out_longitude.trim().contentEquals("")){
            tv_out.setVisibility(View.INVISIBLE);
            tv_title_out_latitude.setVisibility(View.INVISIBLE);
            tv_title_out_longitude.setVisibility(View.INVISIBLE);
            tv_out_time.setVisibility(View.INVISIBLE);
            tv_latitude_out.setVisibility(View.INVISIBLE);
            tv_longitude_out.setVisibility(View.INVISIBLE);
            tv_address_out.setVisibility(View.INVISIBLE);
            img_view_out.setVisibility(View.INVISIBLE);
        }
        tv_out_time.setText(out_time);
        tv_latitude_out.setText(out_latitude);
        tv_longitude_out.setText(out_longitude);
        tv_address_out.setText(out_address.replace("null",""));

        if(!out_image_base_64.trim().contentEquals("")) {
            byte[] imageBytes_out = byteArrayOutputStream.toByteArray();
            imageBytes_out = Base64.decode(out_image_base_64, Base64.DEFAULT);
            Bitmap decodedImageOut = BitmapFactory.decodeByteArray(imageBytes_out, 0, imageBytes_out.length);
            img_view_out.setImageBitmap(decodedImageOut);
            img_view_out.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        tv_button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //-------custom dialog code ends=========
    }

    //===========Code to get location details data from api using volley and load data to popup, starts==========
    public void loadLocationData(String log_employee_id){
//        String url = Url.BASEURL+"od/log/list/"+userSingletonModel.getCorporate_id()+"/1/"+userSingletonModel.getEmployee_id();
        String url = Url.BASEURL()+"timesheet/log/subordinate/detail/"+userSingletonModel.getCorporate_id()+"/"+log_employee_id;
        Log.d("locationDataUrl-=>",url);
        final ProgressDialog loading = ProgressDialog.show(context, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getResponseData(response);
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
    public void getResponseData(String response){
        try {

            JSONObject jsonObject = new JSONObject(response);
            Log.d("jsonLocationData-=>",jsonObject.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            JSONObject jsonObjectDetail = jsonObject.getJSONObject("detail");
            if(jsonObject1.getString("status").contentEquals("true")){

                openLocationDetailsPopup(jsonObject.getString("employee_name"),jsonObject.getString("employee_code"),jsonObject.getString("attendance_date"),jsonObjectDetail.getString("in_time"),jsonObjectDetail.getString("in_latitude"),jsonObjectDetail.getString("in_longitude"),jsonObjectDetail.getString("in_address"),jsonObjectDetail.getString("in_image_base_64"),jsonObjectDetail.getString("out_time"),jsonObjectDetail.getString("out_latitude"),jsonObjectDetail.getString("out_longitude"),jsonObjectDetail.getString("out_address"),jsonObjectDetail.getString("out_image_base_64"));

            }else if(jsonObject1.getString("status").contentEquals("false")){
                Toast.makeText(context.getApplicationContext(),jsonObject1.getString("message"), Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //===========Code to get location details data from api using volley and load data to popup, ends==========
}
