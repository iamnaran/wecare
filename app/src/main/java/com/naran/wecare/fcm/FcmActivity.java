package com.naran.wecare.fcm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.naran.wecare.MainActivity;
import com.naran.wecare.R;
import com.naran.wecare.URLConstants.UrlConstants;

import java.util.HashMap;
import java.util.Map;

public class FcmActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        button = (Button) findViewById(R.id.startBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN),"");
                Log.e("Token", token);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.URL_FCM, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), response , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FcmActivity.this, MainActivity.class);
                        startActivity(intent);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(FcmActivity.this, "Please try after some second ", Toast.LENGTH_SHORT).show();

                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("app_id",token);
                        return params;
                    }
                };
                MySingleton.getmInstance(FcmActivity.this).addToRequestQueue(stringRequest);


            }



        });
    }
}
