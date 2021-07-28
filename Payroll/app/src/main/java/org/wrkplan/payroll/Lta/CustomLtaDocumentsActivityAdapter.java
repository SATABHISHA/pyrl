package org.wrkplan.payroll.Lta;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Model.LTAModel;
import org.wrkplan.payroll.Model.LtaDocumentsModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static org.wrkplan.payroll.Lta.LtaDocumentsActivity.customLtaDocumentsActivityAdapter;
import static org.wrkplan.payroll.Lta.LtaDocumentsActivity.load_data;
import static org.wrkplan.payroll.OutDoorDutyLog.OdDutyLogEmployeeTaskActivity.customOdDutyLogTaskAdapter;

public class CustomLtaDocumentsActivityAdapter extends RecyclerView.Adapter<CustomLtaDocumentsActivityAdapter.MyViewHolder> {
    public LayoutInflater inflater;
    public static ArrayList<LtaDocumentsModel> ltaDocumentsModelArrayList;
    private Context context;
    public  static String base64String = "", filename = "";
    public static ImageView img_view_delete;

    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

//    public static ProgressDialog loading;
//    public static TextView tv_download;


    public CustomLtaDocumentsActivityAdapter(Context ctx, ArrayList<LtaDocumentsModel> ltaDocumentsModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.ltaDocumentsModelArrayList = ltaDocumentsModelArrayList;
    }
    @Override
    public CustomLtaDocumentsActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_lta_documents_custom_row, parent, false);
        CustomLtaDocumentsActivityAdapter.MyViewHolder holder = new CustomLtaDocumentsActivityAdapter.MyViewHolder(view);
        context = parent.getContext();
        LoadButtons();
        return holder;
    }

    //----function to load buttons acc to the logic, code starts
    public void LoadButtons(){


        if (LtaListActivity.EmployeeType == "Employee"){

            if (LtaListActivity.mediclaim_status.contentEquals("")){
                CustomLtaDocumentsActivityAdapter.img_view_delete.setVisibility(View.VISIBLE);


            }
            if (LtaListActivity.mediclaim_status.contentEquals("Saved")){
                CustomLtaDocumentsActivityAdapter.img_view_delete.setVisibility(View.VISIBLE);

            }
            if ((LtaListActivity.mediclaim_status.contentEquals("Submitted")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Approved")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Payment done")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Canceled"))){

                CustomLtaDocumentsActivityAdapter.img_view_delete.setVisibility(View.INVISIBLE);

            }
            if (LtaListActivity.mediclaim_status.contentEquals("Returned")){

                CustomLtaDocumentsActivityAdapter.img_view_delete.setVisibility(View.INVISIBLE);

            }
        }
        if (LtaListActivity.EmployeeType == "Supervisor"){
            if (LtaListActivity.mediclaim_status.contentEquals("Submitted")){

                CustomLtaDocumentsActivityAdapter.img_view_delete.setVisibility(View.INVISIBLE);
            }
            if ((LtaListActivity.mediclaim_status.contentEquals("Returned")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Approved")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Payment done")) ||
                    (LtaListActivity.mediclaim_status.contentEquals("Cancelled"))){

                CustomLtaDocumentsActivityAdapter.img_view_delete.setVisibility(View.INVISIBLE);

            }
        }


    }

    //----function to load buttons acc to the logic, code ends
    @Override
    public void onBindViewHolder(CustomLtaDocumentsActivityAdapter.MyViewHolder holder, int position) {
        holder.itemView.setTag(ltaDocumentsModelArrayList.get(position));
        holder.tv_index.setText(String.valueOf(position+1));
        holder.tv_pdf_file_name.setText(ltaDocumentsModelArrayList.get(position).getLta_filename());
        holder.tv_pdf_file_size.setText(ltaDocumentsModelArrayList.get(position).getLta_file_size());

    }

    @Override
    public int getItemCount() {
        return ltaDocumentsModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_index, tv_pdf_file_name, tv_pdf_file_size;

        RelativeLayout relative_layout;



        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_index = itemView.findViewById(R.id.tv_index);
            tv_pdf_file_name = itemView.findViewById(R.id.tv_pdf_file_name);
            tv_pdf_file_size = itemView.findViewById(R.id.tv_pdf_file_size);
            relative_layout = itemView.findViewById(R.id.relative_layout);
            img_view_delete = itemView.findViewById(R.id.img_view_delete);

            relative_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    base64String = ltaDocumentsModelArrayList.get(position).getLta_file_base64();
                    filename = ltaDocumentsModelArrayList.get(position).getLta_filename();
                    Intent i = new Intent(context, LtaDocumentPdfViewer.class);
                    context.startActivity(i);
                }
            });

            img_view_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final int position = getAdapterPosition();
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    builder.setMessage("Do you really want to delete this document ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
//                                    delete_api_call(position);
                                    if(ltaDocumentsModelArrayList.get(position).getLta_file_from_api_yn().contains("Yes")){
                                        LtaDocumentsModel ltaDocumentsModel = new LtaDocumentsModel();
                                        ltaDocumentsModel.setLta_id(ltaDocumentsModelArrayList.get(position).getLta_id());
                                        ltaDocumentsModel.setLta_filename(ltaDocumentsModelArrayList.get(position).getLta_filename());
                                        ltaDocumentsModel.setFile_path(ltaDocumentsModelArrayList.get(position).getFile_path());
                                        LtaRequestActivity.delete_documents_id_arraylist.add(ltaDocumentsModel);
                                    }
                                    ltaDocumentsModelArrayList.remove(position);
//                                    customLtaDocumentsActivityAdapter.notifyItemRemoved(position);

                                    load_data();
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

}
