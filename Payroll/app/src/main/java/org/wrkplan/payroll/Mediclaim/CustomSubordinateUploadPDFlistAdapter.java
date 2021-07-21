package org.wrkplan.payroll.Mediclaim;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.Model.Mediclaim.Subordinate_Upload_PDF_Model;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class CustomSubordinateUploadPDFlistAdapter extends RecyclerView.Adapter<CustomSubordinateUploadPDFlistAdapter.ViewHolder> {

    ArrayList<Subordinate_Upload_PDF_Model> subordinate_upload_pdf_list=new ArrayList<>();
    Context context;

    public CustomSubordinateUploadPDFlistAdapter(ArrayList<Subordinate_Upload_PDF_Model> subordinate_upload_pdf_list, Context context) {
        this.subordinate_upload_pdf_list = subordinate_upload_pdf_list;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomSubordinateUploadPDFlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.subordinate_custom_pdf_layout_row,parent,false);
        return new CustomSubordinateUploadPDFlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomSubordinateUploadPDFlistAdapter.ViewHolder holder, int position) {
        Subordinate_Upload_PDF_Model model=subordinate_upload_pdf_list.get(position);
        holder.tv_pdf_file.setText(subordinate_upload_pdf_list.get(position).getFile_name());
        holder.tv_index.setText(position+1+ "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPdf(subordinate_upload_pdf_list.get(position).getUri(), position);
            }
        });


    }

    private void ViewPdf(Uri uri, int position) {

        if (MediclaimDocumentsActivity.sub_flag)
        {
            Intent intent = new Intent(context, SubordinatePdfViewActivity.class);
//            intent.putExtra("base64", subordinate_upload_pdf_list.get(position).getFile_base64());
//            intent.putExtra("uri", "");
            Url.base64=subordinate_upload_pdf_list.get(position).getFile_base64();
            Url.uri="";

            try {
                context.startActivity(intent);
            } catch (Exception exception)
            {
                Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public int getItemCount() {
        return subordinate_upload_pdf_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_index;
        ImageView img_pdf_icon,img_pdf_icon_delete;
        TextView tv_pdf_file;
        TextView tv_pdf_file_size;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_index=itemView.findViewById(R.id.tv_index);
            img_pdf_icon=itemView.findViewById(R.id.img_pdf_icon);
            img_pdf_icon_delete=itemView.findViewById(R.id.img_pdf_icon_delete);
            tv_pdf_file=itemView.findViewById(R.id.tv_pdf_file);
            tv_pdf_file_size=itemView.findViewById(R.id.tv_pdf_file_size);
        }
    }
}
