package org.wrkplan.payroll.Config;

import org.wrkplan.payroll.Login.LoginActivity;
import org.wrkplan.payroll.Model.UserSingletonModel;

import java.util.ArrayList;

public class Url{
//  public static String BASEURL = "";
  UserSingletonModel userSingletonModel = UserSingletonModel.getUserSingletonModel();
//  public static String BASEURL = "http://220.225.40.151:9018/api/";
//  public static String BASEURL = "https://arb-erp.com/mergepr/mobile/api/";
public static boolean isMyLeaveApplication=false;
  public static boolean isSubordinateLeaveApplication=false;

  public static boolean islistclicked=false;
  public static ArrayList<String> application_id=new ArrayList<>();
  public  static  String currtent_application_id;
  public  static  String supervisor2_id,supervisor1_id;
  public  static String LeaveType="";
  public  static  boolean isNew=false;

  public  static  boolean isNewEntry=false;
  public static boolean isSubordinateRequisition=false;
  public static boolean isMyRequisition=false;

  public  static  boolean isNewEntryMediclaim=false;
  public static boolean isSubordinateMediclaim=false;
  public static boolean isMyMediclaim=false;
  public static String base64="";
  public static String uri="";

  //----as per discussion on test and live url on 12th sept this code has been done
  public static String BASEURL(){
    String url = "";
    if(LoginActivity.url_check.contentEquals("test")){
//      url = "http://220.225.40.151:9018/api/";
//      url = "http://203.163.248.154:9018/api/";
      url = "http://14.99.211.60:9018/api/";
    }else if(LoginActivity.url_check.contentEquals("live")){
      url = "https://arb-erp.com/mergepr/mobile/api/";
    }

    return url;
  }



}
