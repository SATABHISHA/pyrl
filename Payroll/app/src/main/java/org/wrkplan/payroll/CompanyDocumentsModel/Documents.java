package org.wrkplan.payroll.CompanyDocumentsModel;

public class Documents {
    String id,name,file_name,upload_datetime,upload_user_name,deleted_yn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getUpload_datetime() {
        return upload_datetime;
    }

    public void setUpload_datetime(String upload_datetime) {
        this.upload_datetime = upload_datetime;
    }

    public String getUpload_user_name() {
        return upload_user_name;
    }

    public void setUpload_user_name(String upload_user_name) {
        this.upload_user_name = upload_user_name;
    }

    public String getDeleted_yn() {
        return deleted_yn;
    }

    public void setDeleted_yn(String deleted_yn) {
        this.deleted_yn = deleted_yn;
    }
}
