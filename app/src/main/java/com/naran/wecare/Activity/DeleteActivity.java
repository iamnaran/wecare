package com.naran.wecare.Activity;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naran.wecare.R;
import com.naran.wecare.URLConstants.UrlConstants;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class DeleteActivity extends AppCompatActivity {

    private Button btnDltEvent, btnDltRequest;
    private EditText contactNumber;
    AlertDialog progressDialog;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        contactNumber = (EditText) findViewById(R.id.phone_number);

        btnDltRequest = (Button) findViewById(R.id.dlt_request);
        btnDltEvent = (Button) findViewById(R.id.dlt_event);


        btnDltRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteRequest();

            }
        });

        btnDltEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteEvent();


            }
        });

    }


    private void deleteRequest() {
        progressDialog = new SpotsDialog(this, R.style.Custom);


        final String contactNo = contactNumber.getText().toString();

        if (contactNo.isEmpty()) {
            Toast.makeText(this, " Contact number is mandatory ", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.DELETE_REQUEST, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(getApplicationContext(), " " + response, Toast.LENGTH_SHORT).show();
                    progressDialog.hide();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(), " "+error, Toast.LENGTH_SHORT).show();

                    progressDialog.hide();

                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("contact_number", contactNo);
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);


        }
    }

    private void deleteEvent() {
        progressDialog = new SpotsDialog(this, R.style.Custom);


        final String contactNo = contactNumber.getText().toString();

        if (contactNo.isEmpty()) {
            Toast.makeText(this, " Contact number is mandatory ", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.DELETE_EVENT, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(getApplicationContext(), " " + response, Toast.LENGTH_SHORT).show();
                    progressDialog.hide();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(), " "+error, Toast.LENGTH_SHORT).show();

                    progressDialog.hide();

                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("contact_number", contactNo);
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);


        }


    }

}
