package org.wrkplan.payroll.Model;

public class LtaDocumentsModel {
    String lta_id, lta_filename, lta_file_base64, lta_file_size, lta_file_from_api_yn, file_path;

    //=========Getter method starts=======

    public String getLta_id() {
        return lta_id;
    }

    public String getLta_filename() {
        return lta_filename;
    }

    public String getLta_file_base64() {
        return lta_file_base64;
    }

    public String getLta_file_size() {
        return lta_file_size;
    }

    public String getLta_file_from_api_yn() {
        return lta_file_from_api_yn;
    }

    public String getFile_path() {
        return file_path;
    }

    //=========Getter method ends=======

    //=========Setter method starts=======

    public void setLta_id(String lta_id) {
        this.lta_id = lta_id;
    }

    public void setLta_filename(String lta_filename) {
        this.lta_filename = lta_filename;
    }

    public void setLta_file_base64(String lta_file_base64) {
        this.lta_file_base64 = lta_file_base64;
    }

    public void setLta_file_size(String lta_file_size) {
        this.lta_file_size = lta_file_size;
    }

    public void setLta_file_from_api_yn(String lta_file_from_api_yn) {
        this.lta_file_from_api_yn = lta_file_from_api_yn;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    //=========Setter method ends=======
}
