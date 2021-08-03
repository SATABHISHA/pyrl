package org.wrkplan.payroll.Model.Mediclaim;

public class Delete_Model {
    String mediclaim_id,file_name;

    public String getMediclaim_id() {
        return mediclaim_id;
    }

    public void setMediclaim_id(String mediclaim_id) {
        this.mediclaim_id = mediclaim_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public Delete_Model(String mediclaim_id, String file_name) {
        this.mediclaim_id = mediclaim_id;
        this.file_name = file_name;
    }
}
