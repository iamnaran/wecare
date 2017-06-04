package com.naran.wecare.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.naran.wecare.Models.RequestHandler;
import com.naran.wecare.Models.SharedPrefManager;
import com.naran.wecare.R;
import com.naran.wecare.URLConstants.UrlConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UserFragment extends WeCareFragment {

    EditText editTextUsername,editTextPassword,editTextEmail;
    Button buttonRegister, buttonLogin;
    private ProgressDialog progressDialog;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user,container,false);
        initiliseView(view);
        initialiseListener();

        if(SharedPrefManager.getInstance(getContext()).isLoggedIn()){


        }
        return view;
    }

    @Override
    protected void initiliseView(View view) {

        editTextUsername = (EditText) view.findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) view.findViewById(R.id.editTexTEmail);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);
        buttonRegister = (Button) view.findViewById(R.id.buttonRegister);
        buttonLogin = (Button) view.findViewById(R.id.buttonLogin);

    }

    @Override
    protected void initialiseListener() {

        progressDialog = new ProgressDialog(getContext());

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater layoutInflater =  getActivity().getLayoutInflater();
                final View customView = layoutInflater.inflate(R.layout.login_pop__layout, null);

//                post_request = (Button) customView.findViewById(R.id.post_request);
//                cancel_request = (Button) customView.findViewById(R.id.cancel_request);

//                user_full_name = (EditText) customView.findViewById(R.id.user_full_name);
//                user_blood_type = (EditText) customView.findViewById(R.id.user_blood_type);
//                user_local_address = (EditText) customView.findViewById(R.id.user_local_address);
//                user_district = (EditText) customView.findViewById(R.id.user_district);
//                user_age = (EditText) customView.findViewById(R.id.user_age);
//                user_sex = (EditText) customView.findViewById(R.id.user_sex);
//                user_contact_number = (EditText) customView.findViewById(R.id.user_contact_number);
//                user_email = (EditText) customView.findViewById(R.id.user_email);
//                user_email.setText(SharedPrefManager.getInstance(this).getUserEmail());



                builder.setView(customView);
                builder.create();
                builder.show();

            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();

            }
        });

    }

    private void registerUser() {

        final String email = editTextEmail.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();


        progressDialog.setMessage("Register in Progress");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                        }catch (JSONException e){
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                return params;

            }
        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}
