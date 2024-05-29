package com.example.hmsstudent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
//    EditText email, pass;
    Button loginbtn;
    public static final String MY_Prefence = "myprefence";
    ProgressBar pb;
    RelativeLayout layout;
    TextInputEditText email,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (TextInputEditText) findViewById(R.id.email_edt);
        pass = (TextInputEditText) findViewById(R.id.pass_edt);
        loginbtn = (Button) findViewById(R.id.loginbtn);
        pb = (ProgressBar) findViewById(R.id.pblogin);
        layout = (RelativeLayout) findViewById(R.id.loginlayout);

        getSupportActionBar().setTitle(" Student Login");

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    pb.setVisibility(View.VISIBLE);
                    new Studentlogin().execute(email.getText().toString(), pass.getText().toString());
                }
            }
        });

    }

    private boolean isValidate() {
        if (email.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Email", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }

        if (pass.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Password", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        } else {
            return true;
        }
    }

    public class Studentlogin extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.SLogin(strings[0], strings[1]);
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
                String response = jsonObject.getString("status");
                if (response.compareTo("ok") == 0) {
                    JSONArray Arr = jsonObject.getJSONArray("Data");
                    String res = Arr.getString(0);
                    JSONObject jsonObject1 = new JSONObject(res);
                    String sid = jsonObject1.getString("data0");
                    String sname = jsonObject1.getString("data1");


                    SharedPreferences sharedPreferences = getSharedPreferences(MY_Prefence, MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("name", sname);
                    myEdit.putString("sid", sid);
                    myEdit.putBoolean("islogin", true);
                    myEdit.commit();
                    myEdit.apply();
                    pb.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();


                } else {
                    pb.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar
                            .make(layout, "Invalid Email or Password", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}