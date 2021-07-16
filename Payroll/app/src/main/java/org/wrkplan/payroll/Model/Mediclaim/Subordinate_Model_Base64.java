package org.wrkplan.payroll.Model.Mediclaim;

public class Subordinate_Model_Base64 {
    String file_name,file_base64;

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_base64() {
        return file_base64;
    }

    public void setFile_base64(String file_base64) {
        this.file_base64 = file_base64;
    }

    public Subordinate_Model_Base64(String file_name, String file_base64) {
        this.file_name = file_name;
        this.file_base64 = file_base64;
    }
}
