package org.wrkplan.payroll.Mediclaim;

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
import org.wrkplan.payroll.Model.Mediclaim.My_Mediclaim_Model;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class CustomMediclaimListAdapter extends RecyclerView.Adapter<CustomMediclaimListAdapter.ViewHolder> {

    ArrayList<My_Mediclaim_Model> mediclaimList=new ArrayList<>();
    Context context;
    public static String mediclaim_id="";
    public static String mediclaim_status="";
    public static  String mediclaim_no="";
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

    public CustomMediclaimListAdapter(ArrayList<My_Mediclaim_Model> mediclaimList, Context context) {
        this.mediclaimList = mediclaimList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomMediclaimListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_my_mediclaim_list_custom_row,parent,false);
        return new CustomMediclaimListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomMediclaimListAdapter.ViewHolder holder, int position) {

        My_Mediclaim_Model model=mediclaimList.get(position);
        holder.tv_mediclaim_no.setText(mediclaimList.get(position).getMediclaim_no());
        holder.tv_mediclaim_date.setText(mediclaimList.get(position).getMediclaim_date());
        holder.tv_mediclaim_amount.setText(mediclaimList.get(position).getMediclaim_amount());
        if(mediclaimList.get(position).getMediclaim_status().contentEquals("Approved"))
        {
            holder.tv_mediclaim_status.setText(mediclaimList.get(position).getMediclaim_status());
            holder.tv_mediclaim_status.setTextColor(Color.parseColor("#1e9547"));
        }
        else if(mediclaimList.get(position).getMediclaim_status().contentEquals("Canceled"))
        {
            holder.tv_mediclaim_status.setText(mediclaimList.get(position).getMediclaim_status());
            holder.tv_mediclaim_status.setTextColor(Color.parseColor("#ed1c24"));
        }
        else if(mediclaimList.get(position).getMediclaim_status().contentEquals("Returned"))
        {
            holder.tv_mediclaim_status.setText(mediclaimList.get(position).getMediclaim_status());
            holder.tv_mediclaim_status.setTextColor(Color.parseColor("#b04d0b"));
        }
        else if(mediclaimList.get(position).getMediclaim_status().contentEquals("Submitted"))
        {
            holder.tv_mediclaim_status.setText(mediclaimList.get(position).getMediclaim_status());
            holder.tv_mediclaim_status.setTextColor(Color.parseColor("#fe52ce"));
        }
        else if(mediclaimList.get(position).getMediclaim_status().contentEquals("Payment done"))
        {
            holder.tv_mediclaim_status.setText(mediclaimList.get(position).getMediclaim_status());
            holder.tv_mediclaim_status.setTextColor(Color.parseColor("#98760E"));
        }
        else if(mediclaimList.get(position).getMediclaim_status().contentEquals("Saved"))
        {
            holder.tv_mediclaim_status.setText(mediclaimList.get(position).getMediclaim_status());
            holder.tv_mediclaim_status.setTextColor(Color.parseColor("#2196ed"));
            holder.imgbtn_delete.setVisibility(View.VISIBLE);
        }

        holder.tv_mediclaim_status.setText(mediclaimList.get(position).getMediclaim_status()); //---added by SR

        holder.relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Url.isMyMediclaim=true;
                Url.isNewEntryMediclaim=false;
                Url.isSubordinateMediclaim=false;
                mediclaim_id=mediclaimList.get(position).getMediclaim_id();
                mediclaim_status=mediclaimList.get(position).getMediclaim_status();
                mediclaim_no=mediclaimList.get(position).getMediclaim_no();
                Intent intent=new Intent(context,MediclaimEntryActivity.class);
                // Toast.makeText(context, "employee form", Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });
        holder.imgbtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setMessage("Do you really want to delete this record "+mediclaimList.get(position).getMediclaim_no()+ " ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                delete_api_record(holder.getAdapterPosition());
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
        String url = Url.BASEURL()+"mediclaim/delete/"+userSingletonModel.getCorporate_id()+"/"+mediclaimList.get(position).getMediclaim_id();

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
                        mediclaimList.remove(position);
                        MediclaimActivity.customMediclaimListAdapter.notifyDataSetChanged();
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
        return mediclaimList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_mediclaim_no;
        TextView tv_mediclaim_date;
        TextView tv_mediclaim_status;
        TextView tv_mediclaim_amount;
        ImageView imgbtn_delete;
        RelativeLayout relative_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_mediclaim_amount=itemView.findViewById(R.id.tv_mediclaim_amount);
            tv_mediclaim_no=itemView.findViewById(R.id.tv_mediclaim_no);
            tv_mediclaim_date=itemView.findViewById(R.id.tv_mediclaim_date);
            tv_mediclaim_status=itemView.findViewById(R.id.tv_mediclaim_status);
            imgbtn_delete=itemView.findViewById(R.id.imgbtn_delete);
            relative_layout=itemView.findViewById(R.id.relative_layout);


        }
    }
}
