package org.wrkplan.payroll.Home;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.HolidayDetail.HolidayDetailActivity;
import org.wrkplan.payroll.HolidayModel.Holiday;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    //----Calendar variable, code starts---
    CaldroidFragment caldroidFragment;
    ArrayList<Holiday> arrayList = new ArrayList<>();
    ArrayList<Holiday> arrayList1 = new ArrayList<>();
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    String dateString, holiday_name1;
    TextView txt_date, txt_day_name, txt_holiday_name;
    public static Bundle savedInstanceState;
    SimpleDateFormat myFormat = new SimpleDateFormat("MM-dd-yyyy");
    List<Date> selectedDateRangeList = new ArrayList<>();
    public static int count = 0;
    //----Calendar variable, code ends---
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        LoadCalendarData(savedInstanceState);

    }

    //-------Calendar code, starts-----
    public void LoadCalendarData(Bundle savedInstanceState){
        txt_date = findViewById(R.id.txt_date);
        txt_day_name = findViewById(R.id.txt_day_name);
        txt_holiday_name = findViewById(R.id.txt_holiday_name);

        caldroidFragment = new CaldroidFragment();
        // If Activity is created after rotation
        this.savedInstanceState = savedInstanceState;
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            // flag=1;
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            caldroidFragment.setArguments(args);
            args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, true);
            caldroidFragment.setArguments(args);

            // Attach to the activity
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.calendar_date_list, caldroidFragment);
            t.commit();
        }

            getholiday("1");


        //arrayList.get(position).getFrom_date();

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        final CaldroidListener listener = new CaldroidListener() {


            @Override
            public void onSelectDate(Date date, View view) {
               count = count + 1;


                // Toast.makeText(getApplicationContext(), formatter.format(date), Toast.LENGTH_SHORT).show();
                Log.d("date==", date.toString());
                SimpleDateFormat inputformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
                //SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

                //---get current day date, code starts---

                if (count <= 2) {
                    ColorDrawable color = new ColorDrawable(Color.parseColor("#E4FCAD"));
                    caldroidFragment.setBackgroundDrawableForDate(color, date);
                    caldroidFragment.refreshView();
                    selectedDateRangeList.add(date);
                }else if(count>2){
                    for(int i=0; i<selectedDateRangeList.size(); i++){

                        caldroidFragment.clearBackgroundDrawableForDate(selectedDateRangeList.get(i));
                        caldroidFragment.refreshView();
                    }
                    selectedDateRangeList.clear();
                    count = 0;

                }
                //---get current day date, code ends---


                try {
                    Date d1 = inputformat.parse(date.toString());
                    Log.d("niladri=>", d1.toString());
                    String formateDate = new SimpleDateFormat("dd/MM/yyyy").format(d1);
                    Log.d("DraftDate1-=>", formateDate);


                    for (int j = 0; j < arrayList1.size(); j++) {
                        if (formateDate.equals(arrayList1.get(j).getFrom_date())) {

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH);
                            Date myDate = null;
                            try {
                                myDate = sdf.parse(arrayList1.get(j).getFrom_date());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            sdf.applyPattern("EEE, d MMM yyyy");
                            //sdf.applyPattern("d MMM YYYY");
                            String sMyDate = sdf.format(myDate);
                            txt_date.setText(sMyDate);
                            txt_holiday_name.setText(arrayList1.get(j).getHoliday_name());
                        }
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }


                //

                //}

            }

        };
        caldroidFragment.setCaldroidListener(listener);


      /*  try {


            t = getSupportFragmentManager().beginTransaction();
            t.addToBackStack(null);
            t.replace(R.id.ll_cal, caldroidFragment, null);
            t.commit();


        } catch (NullPointerException e) {
            e.getMessage();
        }*/
    }

    public void getholiday(String year_code) {
        String url = Url.BASEURL() + "holidays/" + userSingletonModel.corporate_id + "/" + year_code;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("holidays");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jb1 = jsonArray.getJSONObject(i);
                        String holiday_name = jb1.getString("holiday_name");
                        String from_date = jb1.getString("from_date");
                        String total_days = jb1.getString("total_days");
                        String id = jb1.getString("id");
                        String to_date=jb1.getString("to_date");
                        Holiday holiday = new Holiday();
                        holiday.setHoliday_name(holiday_name);
                        holiday.setFrom_date(from_date);
                        holiday.setTotal_days(total_days);
                        holiday.setId(id);
                        holiday.setTo_date(to_date);
                        arrayList1.add(holiday);

                        for (int j = 0; j < arrayList1.size(); j++) {
                            dateString = arrayList1.get(j).getFrom_date();

                            holiday_name1 = arrayList1.get(j).getHoliday_name();


                        }
                        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date draft_date_current_format = inputFormat.parse(dateString);
                        Log.d("datenew==", dateString);
                        String draft_date_otput_format = outputFormat.format(draft_date_current_format);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        ColorDrawable color = new ColorDrawable(Color.parseColor("#c2c2c2"));
                        Log.d("DraftDate-=>", draft_date_otput_format.toString());
                        caldroidFragment.setBackgroundDrawableForDate(color, dateFormat.parse(draft_date_otput_format));


                        Log.d("dateString-=>", dateString);
                        /*SimpleDateFormat myFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                        selectDate = myFormat1.parse(dateString);

                        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date_current_format = inputFormat.parse(dateString);
                        String draft_date_otput_format = outputFormat.format(date_current_format);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Log.d("dateString-=>",dateFormat.parse(draft_date_otput_format).toString());
                        ColorDrawable color = new ColorDrawable(Color.parseColor("#c2c2c2"));
                        caldroidFragment.setBackgroundDrawableForDate(color, dateFormat.parse(draft_date_otput_format));*/
                        // caldroidFragment.setTextColorForDate(color, selectDate);


                    }

                    //---get current day date, code starts---
                    Date cDate = new Date();
                    String fDate = new SimpleDateFormat("MM-dd-yyyy").format(cDate);
                    Date today = (Date) myFormat.parse(fDate);
                    //---get current day date, code ends---
                    ColorDrawable color = new ColorDrawable(Color.parseColor("#E4FCAD"));
                    caldroidFragment.setBackgroundDrawableForDate(color, today);


//                    lv1.setAdapter(new HolidayDetailActivity.Nr());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DashboardActivity.this, "Could not connect to the server", Toast.LENGTH_SHORT).show();

            }
        });
        Volley.newRequestQueue(DashboardActivity.this).add(stringRequest);


    }

    //-------Calendar code, ends-----
}
