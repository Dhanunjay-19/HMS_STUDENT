package com.example.hmsstudent;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {
    public static final String MY_Prefence = "myprefence";
    CardView card1, card2;
    String todaydate, time;
    String sid;
    Boolean ty;
    RelativeLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        card1 = (CardView) findViewById(R.id.card1);
        card2 = (CardView) findViewById(R.id.card2);
        layout = (RelativeLayout) findViewById(R.id.mainlayout);

        getSupportActionBar().setTitle("Home");

        SharedPreferences sh = getSharedPreferences(MY_Prefence, MODE_PRIVATE);
        sid = sh.getString("sid", "id is null");


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        todaydate = df.format(c);
        Log.d("date", todaydate);

//        time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
//        Log.d("time", time);


        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.initiateScan();

            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), AttendanceActivity.class));

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences(MY_Prefence, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("islogin", false);
            editor.clear();
            editor.apply();

            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
         else if (id==R.id.changepass) {
            startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
        }
         else {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//     scanner
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Snackbar snackbar = Snackbar
                        .make(layout, "Scan Cancelled!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else {


                String content = intentResult.getContents();

                if (content.contains("*")) {
                    String[] separated = content.split("\\*");


                    String roomid = separated[0];
                    String type = separated[1];

                    Log.d("type", type);
                    if (type.compareTo("In") == 0) {
                        time = new SimpleDateFormat("HH:mm", Locale.US).format(new Date());
//                    time = new SimpleDateFormat("HH:mm").format(new Date(String.valueOf(Locale.US)));
                        new Verifyroom().execute(roomid, type, sid, todaydate, time);

                    } else if (type.compareTo("Out") == 0) {
                        time = new SimpleDateFormat("HH:mm", Locale.US).format(new Date());
                        new Verifyroom().execute(roomid, type, sid, todaydate, time);

                    } else {
                        Snackbar snackbar = Snackbar
                                .make(layout, "Invalid QRCode", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }


                } else {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Invalid QRCode", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }


        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public class Verifyroom extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.VerifyRoom(strings[0], strings[1], strings[2], strings[3], strings[4]);
                data = jp.parse(json);
            } catch (Exception e) {
                data = e.getMessage();
            }
            return data;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("response", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String response = jsonObject.getString("status");
                if (response.compareTo("ok") == 0) {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            MainActivity.this);


                    new SweetAlertDialog(MainActivity.this)
                            .setTitleText("You Scanned at " + time + " on " + todaydate)
                            .show();

                } else if (response.compareTo("no") == 0) {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Invalid QRCode", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                } else {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Scanned Failed!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}