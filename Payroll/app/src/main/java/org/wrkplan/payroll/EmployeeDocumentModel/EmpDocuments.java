package org.wrkplan.payroll.EmployeeDocumentModel;

public class EmpDocuments {
    String name,id,file_path,upload_user_name,view_yn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getUpload_user_name() {
        return upload_user_name;
    }

    public void setUpload_user_name(String upload_user_name) {
        this.upload_user_name = upload_user_name;
    }

    public String getView_yn() {
        return view_yn;
    }

    public void setView_yn(String view_yn) {
        this.view_yn = view_yn;
    }
}
