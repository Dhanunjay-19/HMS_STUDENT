
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

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {
//    EditText oldpassedt, newpassedt;
    Button cp_btn;
    public static final String MY_Prefence = "myprefence";
    String sid, sname;
    RelativeLayout layout;
    ProgressBar pb;
    TextInputEditText oldpassedt,newpassedt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldpassedt = (TextInputEditText) findViewById(R.id.cp_oldemailedt);
        newpassedt = (TextInputEditText) findViewById(R.id.cp_newemailedt);
        layout = (RelativeLayout) findViewById(R.id.changepasslayout);
        cp_btn = (Button) findViewById(R.id.cp_btn);
        pb = (ProgressBar) findViewById(R.id.pbchange);


        getSupportActionBar().setTitle("Change Password");

        SharedPreferences sh = getSharedPreferences(MY_Prefence, MODE_PRIVATE);
        sid = sh.getString("sid", "id is null");
        sname = sh.getString("name", "name is null");

        cp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    pb.setVisibility(View.VISIBLE);
                    new ChangePass().execute(sid.toString(), oldpassedt.getText().toString(), newpassedt.getText().toString());
                }
            }
        });

    }

    private boolean isValidate() {
        if (oldpassedt.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Old Password!", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }

        if (newpassedt.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter New Password", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        } else {
            return true;
        }
    }

    public class ChangePass extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            RestAPI restAPI = new RestAPI();

            try {
                JSONParse jp = new JSONParse();
                JSONObject json = restAPI.ChangePassword(strings[0], strings[1], strings[2]);
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
                if (response.compareTo("true") == 0) {

                    pb.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar
                            .make(layout, "Password Changed", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    SharedPreferences sharedPreferences = getSharedPreferences(MY_Prefence, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("islogin", false);
                    editor.clear();
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    pb.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar
                            .make(layout, "Password Not Changed!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}