package com.example.hmsstudent;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity {
    EditText dateedt;
    ImageView img;
    String todaydate;
    String sid;
    public static final String MY_Prefence = "myprefence";
    RecyclerView recyclerView;
    AttendanceAdapter adapter;
    RelativeLayout layout;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        dateedt = (EditText) findViewById(R.id.date_editext);
        img = (ImageView) findViewById(R.id.datepickerimg);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_attendance);
        layout = (RelativeLayout) findViewById(R.id.attendancelayout);
        pb = (ProgressBar) findViewById(R.id.pb);

        getSupportActionBar().setTitle("My Attendace");

        SharedPreferences sh = getSharedPreferences(MY_Prefence, MODE_PRIVATE);
        sid = sh.getString("sid", "id is null");

        pb.setVisibility(View.VISIBLE);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        todaydate = df.format(c);
        Log.d("date", todaydate);

        dateedt.setText(todaydate);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(AttendanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int mYear, int mMonth, int selectedday) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */

                        Date d = new Date(mYear, mMonth, selectedday);
                        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                                "20YY/MM/dd");
                        String strDate = dateFormatter.format(d);
                        Log.d("format", strDate);
                        dateedt.setText(strDate);
                        pb.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(null);
                        new GETAttendance().execute(dateedt.getText().toString(), sid);

                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_attendance);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        new GETAttendance().execute(dateedt.getText().toString(), sid);

    }

    public class GETAttendance extends AsyncTask<String, String, String> {
        List<Attendance> attendanceList = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.SgetAttedance(strings[0], strings[1]);
                data = jp.parse(json);
            } catch (Exception e) {
                data = e.getMessage();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("res", s);
            try {
                recyclerView.setAdapter(null);
                JSONObject jsonObject = new JSONObject(s);
                String res = jsonObject.getString("status");
                if (res.compareTo("ok") == 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Attendance attendance = new Attendance(
                                object.getString("data0"),
                                object.getString("data1"),
                                object.getString("data2"),
                                object.getString("data3"),
                                object.getString("data4"),
                                object.getString("data5"));
                        attendanceList.add(attendance);
                    }
                    adapter = new AttendanceAdapter(getApplicationContext(), attendanceList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pb.setVisibility(View.GONE);


                } else {
                    pb.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar
                            .make(layout, "No Attendance Found!", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}