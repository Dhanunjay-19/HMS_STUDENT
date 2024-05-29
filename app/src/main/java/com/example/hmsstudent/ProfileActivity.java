package com.example.hmsstudent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    String sid, sname;
    public static final String MY_Prefence = "myprefence";
    //    String name, email, add, contact, econtact, roomno;
//    EditText namedt;
//            EditText emailedt, addedt, contactedt, econtactedt, roomnoedt,bednoedt;
    Button updatebtn;
    ProgressBar pb;
    ScrollView layout;
    TextInputEditText namedt,emailedt, addedt, contactedt, econtactedt, roomnoedt,bednoedt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        namedt = (TextInputEditText) findViewById(R.id.name_edt);
        emailedt = (TextInputEditText) findViewById(R.id.email_edt);
        contactedt = (TextInputEditText) findViewById(R.id.contact_edt);
        econtactedt = (TextInputEditText) findViewById(R.id.econtact_edt);
        addedt = (TextInputEditText) findViewById(R.id.add_edt);
        roomnoedt = (TextInputEditText) findViewById(R.id.roomno_edt);
        updatebtn = (Button) findViewById(R.id.updateprofile_btn);
        bednoedt= (TextInputEditText) findViewById(R.id.bedno_edt);
        pb = (ProgressBar) findViewById(R.id.pbprofile);
        layout = (ScrollView) findViewById(R.id.proflayout);

        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setHomeButtonEnabled(true);

        SharedPreferences sh = getSharedPreferences(MY_Prefence, MODE_PRIVATE);
        sid = sh.getString("sid", "id is null");
        sname = sh.getString("name", "name is null");

        new GetProfile().execute(sid);

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    pb.setVisibility(View.VISIBLE);
                    new UpdateProfile().execute(sid, namedt.getText().toString(), emailedt.getText().toString(),
                            contactedt.getText().toString(), addedt.getText().toString(), econtactedt.getText().toString());
                }

            }
        });

    }

    public class GetProfile extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.getProfile(strings[0]);
                data = jp.parse(json);
            } catch (Exception e) {
                data = e.getMessage();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("respp", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String response = jsonObject.getString("status");
                if (response.compareTo("ok") == 0) {
                    JSONArray Arr = jsonObject.getJSONArray("Data");
                    String res = Arr.getString(0);
                    JSONObject jsonObject1 = new JSONObject(res);


                    namedt.setText(jsonObject1.getString("data1"));
                    emailedt.setText(jsonObject1.getString("data2"));
                    contactedt.setText(jsonObject1.getString("data3"));
                    econtactedt.setText(jsonObject1.getString("data5"));
                    addedt.setText(jsonObject1.getString("data4"));
                    roomnoedt.setText(jsonObject1.getString("data7"));
                    bednoedt.setText(jsonObject1.getString("data9"));


                } else {
                    Snackbar snackbar = Snackbar
                            .make(layout, "Enable to fetch Profile", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class UpdateProfile extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.UpdateProfile(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5]);
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
                JSONObject jsonObject = new JSONObject(s);
                String res = jsonObject.getString("status");
                if (res.compareTo("true") == 0) {
                    pb.setVisibility(View.GONE);

                    Snackbar snackbar = Snackbar
                            .make(layout, "Profile Update Sucessful", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    finish();

                } else if (res.compareTo("Email") == 0) {
                    pb.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar
                            .make(layout, "Email Already Exits", Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else if (res.compareTo("Contact") == 0) {
                    pb.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar
                            .make(layout, "Contact Already Exits", Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else {
                    pb.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar
                            .make(layout, "Profile Not Updated", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean isValidate() {
        if (namedt.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Name", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (emailedt.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Email", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }

        if (contactedt.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Contact", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (addedt.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Address", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (econtactedt.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Emergency Contact", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;

        }
        if (econtactedt.getText().toString().length() != 10) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Invalid Emergency Contact", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        if (contactedt.getText().toString().length() != 10) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Invalid Contact Number", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;

        } else {
            return true;
        }
    }

}