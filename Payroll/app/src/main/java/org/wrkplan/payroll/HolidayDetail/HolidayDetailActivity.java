package org.wrkplan.payroll.HolidayDetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
import org.wrkplan.payroll.CompanyDocuments.CompanyDocumentsActivity;
import org.wrkplan.payroll.Config.Url;
import org.wrkplan.payroll.EmployeeFacilitisModel.Facilitis;
import org.wrkplan.payroll.HolidayModel.Holiday;
import org.wrkplan.payroll.Home.HomeActivity;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.System.exit;

public class HolidayDetailActivity extends AppCompatActivity implements View.OnClickListener {


    //---------------Caldroid function name-----------------------//
    public final static String
            DIALOG_TITLE = "dialogTitle",
            MONTH = "month",
            YEAR = "year",
            SHOW_NAVIGATION_ARROWS = "showNavigationArrows",
            DISABLE_DATES = "disableDates",
            SELECTED_DATES = "selectedDates",
            MIN_DATE = "minDate",
            MAX_DATE = "maxDate",
            ENABLE_SWIPE = "enableSwipe",
            START_DAY_OF_WEEK = "startDayOfWeek",
            SIX_WEEKS_IN_CALENDAR = "sixWeeksInCalendar",
            ENABLE_CLICK_ON_DISABLED_DATES = "enableClickOnDisabledDates",
            SQUARE_TEXT_VIEW_CELL = "squareTextViewCell",
            THEME_RESOURCE = "themeResource";
    //-------------end caldroid function name---------------------//

    ListView lv1;
    boolean flag = false;
    String dateString, holiday_name1;
    Date selectDate;
    CaldroidFragment caldroidFragment;
    ArrayList<Holiday> arrayList = new ArrayList<>();
    ArrayList<Holiday> arrayList1 = new ArrayList<>();
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    Button btn_ok;
    ImageView img_calendar, img_list;
    LinearLayout ll_cal, ll_date;
    RelativeLayout rl11;
    TextView txt_dt, txt_holiname;
    FragmentTransaction t;
    Date date = new Date();
    String currntdate;
    SimpleDateFormat currentDte = new SimpleDateFormat("dd/MM/yyyy");
    ImageView img_back;


//    int flag=0;


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HolidayDetailActivity.this, HomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_detail);
        img_back=findViewById(R.id.img_back);
        lv1 = findViewById(R.id.lv1);
        btn_ok = findViewById(R.id.btn_ok);
        rl11 = findViewById(R.id.rl11);
        ll_cal = findViewById(R.id.ll_cal);
        ll_date = findViewById(R.id.ll_date);
        txt_dt = findViewById(R.id.txt_dt);
        txt_holiname = findViewById(R.id.txt_holiname);
        img_calendar = findViewById(R.id.img_calendar);
        img_list = findViewById(R.id.img_list);
        ll_date.setVisibility(View.GONE);
        lv1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        currntdate = (currentDte.format(date));
        //  Toast.makeText(this, currntdate, Toast.LENGTH_SHORT).show();

        img_back.setOnClickListener(this);


        img_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_date.setVisibility(View.GONE);
                Intent refresh = new Intent(HolidayDetailActivity.this, HolidayDetailActivity.class);
                overridePendingTransition(0, 0);
                startActivity(refresh);
                overridePendingTransition(0, 0);
                //finish();
                Getholidaylist("1");


            }
        });


        Getholidaylist("1");
        btn_ok.setVisibility(View.GONE);


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HolidayDetailActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            }
        });

        img_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* finish();
                startActivity(getIntent());*/

                //   getholi();

                rl11.setVisibility(View.GONE);
                ll_date.setVisibility(View.VISIBLE);
                caldroidFragment = new CaldroidFragment();
                // flag=1;
                Bundle args = new Bundle();
                Calendar cal = Calendar.getInstance();
                args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
                args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
                caldroidFragment.setArguments(args);
                args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, true);
                caldroidFragment.setArguments(args);
                getholiday("1");

                //arrayList.get(position).getFrom_date();

                final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                final CaldroidListener listener = new CaldroidListener() {

                    @Override
                    public void onSelectDate(Date date, View view) {
                        // Toast.makeText(getApplicationContext(), formatter.format(date), Toast.LENGTH_SHORT).show();
                        Log.d("date==", date.toString());
                        SimpleDateFormat inputformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
                        //SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
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
                                    txt_dt.setText(sMyDate);
                                    txt_holiname.setText(arrayList1.get(j).getHoliday_name());
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


                try {


                    t = getSupportFragmentManager().beginTransaction();
                    t.addToBackStack(null);
                    t.replace(R.id.ll_cal, caldroidFragment, null);
                    t.commit();


                } catch (NullPointerException e) {
                    e.getMessage();
                }


            }
        });


    }
//    public void clearStack() {
//        //Here we are clearing back stack fragment entries
//        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
//        if (backStackEntry > 0) {
//            for (int i = 0; i < backStackEntry; i++) {
//                getSupportFragmentManager().popBackStackImmediate();
//            }
//        }
//
//        //Here we are removing all the fragment that are shown here
//        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
//            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
//                Fragment mFragment = getSupportFragmentManager().getFragments().get(i);
//                if (mFragment != null) {
//                    getSupportFragmentManager().beginTransaction().remove(mFragment).commit();
//                }
//            }
//        }
//    }


    private void getholiday(String year_code) {
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


                    lv1.setAdapter(new Nr());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HolidayDetailActivity.this, "Could not connect to the server", Toast.LENGTH_SHORT).show();

            }
        });
        Volley.newRequestQueue(HolidayDetailActivity.this).add(stringRequest);


    }


    private void Getholidaylist(String year_code) {
        final int[] position = {0};
        String url = Url.BASEURL() + "holidays/" + userSingletonModel.corporate_id + "/" + year_code;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("holidays");
                    if(!arrayList.isEmpty()){
                        arrayList.clear();
                    }
                    Holiday holiday = new Holiday();


                    for (int i = 0; i < jsonArray.length(); i++) {
                        //flag=false;
                        JSONObject jb1 = jsonArray.getJSONObject(i);
                        holiday = new Holiday();
                        String holiday_name = jb1.getString("holiday_name");
                        String from_date = jb1.getString("from_date");
                        String total_days = jb1.getString("total_days");
                        String id = jb1.getString("id");
                        String to_date=jb1.getString("to_date");


//                        Holiday holiday = new Holiday();
                        holiday.setHoliday_name(holiday_name);
                        holiday.setFrom_date(from_date);
                        holiday.setTotal_days(total_days);
                        holiday.setId(id);
                        holiday.setTo_date(to_date);
//                        Date currentDate = new SimpleDateFormat("dd/MM/yyyy").parse(currntdate);
//                        Date Fromdate = new SimpleDateFormat("dd/MM/yyyy").parse(from_date);

//                        if(date1.equals(date2) || date1.before(date2))
                     /*   if (flag == false) {
                            if (currentDate.equals(Fromdate) || Fromdate.after(currentDate)) {
                                flag = true;


                            }

                        }*/


                        arrayList.add(holiday);

                    }
                    for(int i=0; i< arrayList.size(); i++) {
                        Date currentDate = new SimpleDateFormat("dd/MM/yyyy").parse(currntdate);
                        Date Fromdate = new SimpleDateFormat("dd/MM/yyyy").parse(holiday.getFrom_date());
                        if (currentDate.equals(Fromdate) || Fromdate.after(currentDate)) {
                            position[0] = i;

                        }
                    }

                    Log.d("getPostion-=>",String.valueOf(position[0]));
                    lv1.setAdapter(new Nr());
                    lv1.setSelection(position[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HolidayDetailActivity.this, "Could not connect to the server", Toast.LENGTH_SHORT).show();

            }
        });
        Volley.newRequestQueue(HolidayDetailActivity.this).add(stringRequest);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
            onBackPressed();
            break;
        }
    }


    class Nr extends BaseAdapter {
        @Override
        public int getCount() {
            return arrayList.size();

        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.row, null);
            TextView txt_count = v.findViewById(R.id.txt_count);
            TextView txt_holiday_name = v.findViewById(R.id.txt_holiday_name);
            TextView txt_date = v.findViewById(R.id.txt_date);
            TextView txt_total_holiday=v.findViewById(R.id.txt_total_holiday);
            RelativeLayout rl11 = findViewById(R.id.rl11);

            txt_holiday_name.setText(arrayList.get(position).getHoliday_name());
            // txt_date.setText(arrayList.get(position).getFrom_date());
            // txt_list.setText(arrayList.get(position).getTotal_days());
            //  txt_count.setText(arrayList.get(position).getId());
            txt_count.setText(Integer.valueOf(arrayList.get(position).getId()) + 1 + "");
//            SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
//            Date dt1= null;
//            try {
//                dt1 = format1.parse(arrayList.get(position).getFrom_date());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            DateFormat format2=new SimpleDateFormat("EEEE");
//            String finalDay=format2.format(dt1);
//            txt_day_name.setText(finalDay);

//            SimpleDateFormat format=new SimpleDateFormat("dd/MMM/yyyy");
//            Date dt4= null;
//            try {
//                dt1 = format.parse(arrayList.get(position).getFrom_date());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            DateFormat format3=new SimpleDateFormat("MMMM");
//            String finalDay2=format2.format(dt1);
//            txt_date.setText(finalDay2);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH);
            Date myFromDate = null;
            Date myToDate=null;
            try {
                myFromDate = sdf.parse(arrayList.get(position).getFrom_date());
                myToDate = sdf.parse(arrayList.get(position).getTo_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sdf.applyPattern("EEE, d MMM yyyy");
            //sdf.applyPattern("d MMM YYYY");
            String sMyFromDate = sdf.format(myFromDate);
            String sMyToDate=sdf.format(myToDate);

            txt_date.setText(sMyFromDate + " To "+sMyToDate);

            txt_total_holiday.setText(arrayList.get(position).getTotal_days());


            return v;
        }
    }


}
