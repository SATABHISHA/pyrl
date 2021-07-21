package org.wrkplan.payroll.Model.Mediclaim;

import android.net.Uri;

public class Subordinate_Upload_PDF_Model {
    String file_base64,file_name,file_path,mediclaim_id;
    Uri uri;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getFile_base64() {
        return file_base64;
    }

    public void setFile_base64(String file_base64) {
        this.file_base64 = file_base64;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getMediclaim_id() {
        return mediclaim_id;
    }

    public void setMediclaim_id(String mediclaim_id) {
        this.mediclaim_id = mediclaim_id;
    }

    public Subordinate_Upload_PDF_Model(String file_base64, String file_name, String file_path, String mediclaim_id,Uri uri) {
        this.file_base64 = file_base64;
        this.file_name = file_name;
        this.file_path = file_path;
        this.mediclaim_id = mediclaim_id;
        this.uri=uri;
    }

    public Subordinate_Upload_PDF_Model() {
    }
}
