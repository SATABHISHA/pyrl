package org.wrkplan.payroll.Notifications;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.wrkplan.payroll.Data.SqliteDb;
import org.wrkplan.payroll.Home.CustomDashboardPendingItemsListAdapter;
import org.wrkplan.payroll.Home.DashboardActivity;
import org.wrkplan.payroll.Model.NotificationModel;
import org.wrkplan.payroll.Model.UserSingletonModel;
import org.wrkplan.payroll.R;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    LinearLayout ll_recycler;
    RecyclerView recycler_view;
    TextView tv_nodata;
    ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
    SqliteDb sqliteDb=new SqliteDb(this);
    SQLiteDatabase db;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ImageView img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        LoadNotificationData();
    }

    public void LoadNotificationData(){
        ll_recycler = findViewById(R.id.ll_recycler);
        img_back = findViewById(R.id.img_back);
        tv_nodata = findViewById(R.id.tv_nodata);
        recycler_view = findViewById(R.id.recycler_view);

        //==========Recycler code initializing and setting layoutManager starts======
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends=====

        try {
            db = openOrCreateDatabase("Payroll", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS NOTIFICATIONDETAILS(employee_id text,insertYN text, title text, notification_id text, event_name text,event_id text, event_owner_id text, event_owner text, message text)");
        }catch (Exception e){
            e.printStackTrace();
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotificationActivity.this, DashboardActivity.class));
            }
        });
        LoadDataFromSqlite();
    }

    public void LoadDataFromSqlite(){

        if(!notificationModelArrayList.isEmpty()){
            notificationModelArrayList.clear();
        }
        notificationModelArrayList = sqliteDb.getNotificationDataFromSqlite(userSingletonModel.getEmployee_id());
        recycler_view.setAdapter(new CustomNotificationAdapter(this, notificationModelArrayList));
    }
}
