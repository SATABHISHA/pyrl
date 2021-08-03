package org.wrkplan.payroll.Mediclaim;

import android.content.Context;
import android.content.DialogInterface;
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
import org.wrkplan.payroll.Model.Mediclaim.Delete_Model;
import org.wrkplan.payroll.Model.Mediclaim.Upload_PDF_Model;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class CustomUploadPDFlistAdapter extends RecyclerView.Adapter<CustomUploadPDFlistAdapter.ViewHolder> {

    ArrayList<Upload_PDF_Model> pdf_modelArrayList = new ArrayList<>();
    public static ArrayList<Delete_Model> deleteModelArrayList=new ArrayList<>();
    Context context;

    public CustomUploadPDFlistAdapter(ArrayList<Upload_PDF_Model> pdf_modelArrayList, Context context) {
        this.pdf_modelArrayList = pdf_modelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomUploadPDFlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_custom_pdf_layout_row, parent, false);
        return new CustomUploadPDFlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomUploadPDFlistAdapter.ViewHolder holder, int position) {
        Upload_PDF_Model model = pdf_modelArrayList.get(position);
        holder.tv_index.setText(position + 1 + "");
        holder.tv_pdf_file.setText(pdf_modelArrayList.get(position).getFile_name());


        holder.img_pdf_icon_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setMessage("Do you really want to delete this document ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(pdf_modelArrayList.get(position).isFromapi_yn())
                                {
                                    Delete_Model model1=new Delete_Model(pdf_modelArrayList.get(position).getMediclaim_id(),pdf_modelArrayList.get(position).getFile_name());
                                    deleteModelArrayList.add(model1);
                                }
                                delete_record(holder.getAdapterPosition());
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
        if (MediclaimDocumentsActivity.flag == true) {
            holder.img_pdf_icon_delete.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPdf(pdf_modelArrayList.get(position).getUri(), position);
            }
        });

    }

    private void ViewPdf(Uri uri, int position) {
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_VIEW);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        shareIntent.setType("application/pdf");
//        context.startActivity(Intent.createChooser(shareIntent, "Open With"));

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(uri, "application/pdf");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        context.startActivity(Intent.createChooser(intent, "Open With"));

//        if (MediclaimDocumentsActivity.flag == true || MediclaimDocumentsActivity.med_status.equalsIgnoreCase("Saved")) {

        if(Url.isNewEntryMediclaim == true){
            Intent intent = new Intent(context, PdfViewActivity.class);
//            intent.putExtra("base64", pdf_modelArrayList.get(position).getFile_base64());
//            intent.putExtra("uri", "");
            Url.base64=pdf_modelArrayList.get(position).getFile_base64();
            Url.uri="";

            try {
                context.startActivity(intent);
            } catch (Exception exception)
            {
                Toast.makeText(context.getApplicationContext(), "Erre", Toast.LENGTH_LONG).show();
            }
        }
        else if (MediclaimDocumentsActivity.med_status.equalsIgnoreCase("Saved"))
        {
            Intent intent = new Intent(context, PdfViewActivity.class);
//            intent.putExtra("base64", pdf_modelArrayList.get(position).getFile_base64());
//            intent.putExtra("uri", "");
            Url.base64=pdf_modelArrayList.get(position).getFile_base64();
            Url.uri="";

            try {
                context.startActivity(intent);
            } catch (Exception exception)
            {
                Toast.makeText(context.getApplicationContext(), "Erre", Toast.LENGTH_LONG).show();
            }
        }
        else if (MediclaimDocumentsActivity.med_status.equalsIgnoreCase("Returned"))
        {
            Intent intent = new Intent(context, PdfViewActivity.class);
//            intent.putExtra("base64", pdf_modelArrayList.get(position).getFile_base64());
//            intent.putExtra("uri", "");
            Url.base64=pdf_modelArrayList.get(position).getFile_base64();
            Url.uri="";

            try {
                context.startActivity(intent);
            } catch (Exception exception)
            {
                Toast.makeText(context.getApplicationContext(), "Erre", Toast.LENGTH_LONG).show();
            }
        }
        else if (MediclaimDocumentsActivity.flag)
        {
            Intent intent = new Intent(context, PdfViewActivity.class);
//            intent.putExtra("base64", pdf_modelArrayList.get(position).getFile_base64());
//            intent.putExtra("uri", "");
            Url.base64=pdf_modelArrayList.get(position).getFile_base64();
            Url.uri="";

            try {
                context.startActivity(intent);
            } catch (Exception exception)
            {
                Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }
        else {

            Intent intent = new Intent(context, PdfViewActivity.class);
//            intent.putExtra("base64", "");
//            intent.putExtra("uri", uri.toString());
            Url.base64="";
            Url.uri=uri.toString();
            context.startActivity(intent);
        }




    }

    private void delete_record(int position) {
        pdf_modelArrayList.remove(position);
        MediclaimDocumentsActivity.adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return pdf_modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_index;
        ImageView img_pdf_icon, img_pdf_icon_delete;
        TextView tv_pdf_file;
        TextView tv_pdf_file_size;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_index = itemView.findViewById(R.id.tv_index);
            img_pdf_icon = itemView.findViewById(R.id.img_pdf_icon);
            img_pdf_icon_delete = itemView.findViewById(R.id.img_pdf_icon_delete);
            tv_pdf_file = itemView.findViewById(R.id.tv_pdf_file);
            tv_pdf_file_size = itemView.findViewById(R.id.tv_pdf_file_size);
        }
    }
}
