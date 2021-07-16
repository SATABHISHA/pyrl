package org.wrkplan.payroll.Model.Mediclaim;

public class Upload_PDF_Model {
   String file_base64,file_name,file_path,mediclaim_id;

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

    public Upload_PDF_Model(String file_base64, String file_name, String file_path, String mediclaim_id) {
        this.file_base64 = file_base64;
        this.file_name = file_name;
        this.file_path = file_path;
        this.mediclaim_id = mediclaim_id;
    }
}
