package com.example.hmsstudent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QRCodeActivity extends AppCompatActivity {
    String sid, rid;
    public static final String MY_Prefence = "myprefence";
    ImageView imageView;
    Button scanbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
//        imageView = (ImageView) findViewById(R.id.imageView);
//        scanbtn = (Button) findViewById(R.id.btnscan);

        getSupportActionBar().setTitle("Scanner");

        IntentIntegrator intentIntegrator = new IntentIntegrator(QRCodeActivity.this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();


//scanner


        SharedPreferences sh = getSharedPreferences(MY_Prefence, MODE_PRIVATE);
        sid = sh.getString("sid", "id is null");
//        new getProfile().execute(sid);


    }

//    private void QRCodeButton() {
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        try {
//            BitMatrix bitMatrix = qrCodeWriter.encode(rid, BarcodeFormat.QR_CODE, 200, 200);
//            Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
//            for (int x = 0; x < 200; x++) {
//                for (int y = 0; y < 200; y++) {
//                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
//                }
//            }
//            imageView.setImageBitmap(bitmap);
//            Toast.makeText(getApplicationContext(), "sucess", Toast.LENGTH_SHORT).show();
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//     scanner


        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                Toast.makeText(this, intentResult.getContents(), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    }

//public class getProfile extends AsyncTask<String, String, String> {
//
//    @Override
//    protected String doInBackground(String... strings) {
//        String data = null;
//        RestAPI restAPI = new RestAPI();
//
//        try {
//            JSONParse jp = new JSONParse();
//            JSONObject json = restAPI.getProfile(strings[0]);
//            data = jp.parse(json);
//        } catch (Exception e) {
//            data = e.getMessage();
//        }
//        return data;
//
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//        Log.d("res", s);
//        try {
//            JSONObject object = new JSONObject(s);
//            String res = object.getString("status");
//            if (res.compareTo("ok") == 0) {
//                JSONArray array = object.getJSONArray("Data");
//                String data = array.getString(0);
//                JSONObject jsonObject1 = new JSONObject(data);
//                rid = jsonObject1.getString("data6");
//                Log.d("check", rid);
//                QRCodeButton();
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//}


