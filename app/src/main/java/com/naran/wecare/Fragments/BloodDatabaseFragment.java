package com.naran.wecare.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naran.wecare.CustomViews.BloodDonorsDatabaseAdapter;
import com.naran.wecare.Models.BloodDatabase;
import com.naran.wecare.Models.SharedPrefManager;
import com.naran.wecare.R;
import com.naran.wecare.URLConstants.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class BloodDatabaseFragment extends WeCareFragment {

    private RecyclerView recyclerViewBloodDB;
    SwipeRefreshLayout swipeView1;
    BloodDonorsDatabaseAdapter bloodDonorsDatabaseAdapter;
    List<BloodDatabase> bloodDatabaseList;

    SearchView searchView;
    private boolean activityStartup = true;
    AlertDialog progressDialog;
    int sYear;
    int sMonth;
    int sDay;
    String date = "";
    public int ageYear = 0;
    Dialog dialog;

    Calendar calendar = Calendar.getInstance();
    int cYear = calendar.get(Calendar.YEAR);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blood_database, container, false);
        initiliseView(view);
        initialiseListener();
        setUpRecyclerView();
        getBloodDonorsDatabase();

        return view;
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        recyclerViewBloodDB.setLayoutManager(layoutManager1);
        bloodDatabaseList = new ArrayList<>();
        bloodDonorsDatabaseAdapter = new BloodDonorsDatabaseAdapter(getContext(), bloodDatabaseList);
        recyclerViewBloodDB.setAdapter(bloodDonorsDatabaseAdapter);

    }

    @Override
    protected void initiliseView(View view) {

        recyclerViewBloodDB = (RecyclerView) view.findViewById(R.id.recyclerViewBloodDB);
        swipeView1 = (SwipeRefreshLayout) view.findViewById(R.id.swipe1);
        searchView = (SearchView) view.findViewById(R.id.searchView);
        swipeView1.setColorSchemeColors(
                Color.RED
        );

        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.addUser);

        myFab.setVisibility(View.GONE);

        if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {

            myFab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {

                        postUserData();


                    } else {
                        Toast.makeText(getContext(), " Please login First ", Toast.LENGTH_SHORT).show();
                    }


                }
            });

        }


        if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {

            myFab.setVisibility(View.VISIBLE);

        }


    }


    @Override
    protected void initialiseListener() {

        swipeView1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBloodDonorsDatabase();

            }
        });
        searchView.setQueryHint("search blood donors");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<BloodDatabase> filterModeList = filter(bloodDatabaseList, newText);
                bloodDonorsDatabaseAdapter.setFilter(filterModeList);
                return true;
            }
        });

    }

    private List<BloodDatabase> filter(List<BloodDatabase> bloodDatabaseList, String query) {

        query = query.toUpperCase();
        final List<BloodDatabase> filteredModeList = new ArrayList<>();
        for (BloodDatabase bloodDatabases : bloodDatabaseList) {
            final String text = bloodDatabases.getBlood_group().toUpperCase();
            final String text1 = bloodDatabases.getDistrict().toUpperCase();
            final String text2 = bloodDatabases.getLocal_address().toUpperCase();
            final String text3 = bloodDatabases.getFull_name().toUpperCase();
            final String text4 = bloodDatabases.getEmail().toUpperCase();

            if (text.contains(query) || text1.contains(query) || text2.contains(query) || text3.contains(query) || text4.contains(query)) {
                filteredModeList.add(bloodDatabases);
            }
        }
        return filteredModeList;
    }

    private void getBloodDonorsDatabase() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConstants.GET_BLOOD_DATABASE_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", response);


                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String full_name = jsonObject.getString(UrlConstants.KEY_FULLNAME);
                        String age = jsonObject.getString(UrlConstants.KEY_AGE);
                        String sex = jsonObject.getString(UrlConstants.KEY_SEX);
                        String district = jsonObject.getString(UrlConstants.KEY_DISTRICT);
                        String local_address = jsonObject.getString(UrlConstants.KEY_LOCAL_ADDRESS);
                        String email = jsonObject.getString(UrlConstants.KEY_EMAIL);
                        String contact_number = jsonObject.getString(UrlConstants.KEY_CONTACT_NUMBER);
                        String blood_group = jsonObject.getString(UrlConstants.KEY_BLOOD_GROUP);

                        BloodDatabase bloodDb = new BloodDatabase();

                        bloodDb.setFull_name(full_name);
                        bloodDb.setAge(age);
                        bloodDb.setSex(sex);
                        bloodDb.setDistrict(district);
                        bloodDb.setLocal_address(local_address);
                        bloodDb.setEmail(email);
                        bloodDb.setContact_number(contact_number);
                        bloodDb.setBlood_group(blood_group);
                        bloodDatabaseList.add(bloodDb);


                    }
                    bloodDonorsDatabaseAdapter.notifyDataSetChanged();
                    swipeView1.setRefreshing(false);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 4 * 60 * 60 * 1000; // in 4 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new String(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }

            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);


    }

    private void postUserData() {


        dialog = new Dialog(getActivity(), R.style.AppTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.post_user_to_database);

        ImageView cross = (ImageView) dialog.findViewById(R.id.cross);

        // u = user

        final EditText uFullName = (EditText) dialog.findViewById(R.id.user_full_name);
        final EditText uBloodType = (EditText) dialog.findViewById(R.id.user_blood_type);
        final EditText uDistrict = (EditText) dialog.findViewById(R.id.user_district);
        final EditText uLocalAddress = (EditText) dialog.findViewById(R.id.user_local_address);
        final EditText uContactNumber = (EditText) dialog.findViewById(R.id.user_contact_number);
        final EditText uSex = (EditText) dialog.findViewById(R.id.user_sex);
        final EditText uDob = (EditText) dialog.findViewById(R.id.user_age);
        final EditText uEmail = (EditText) dialog.findViewById(R.id.user_email);
        final Spinner spinner1 = (Spinner) dialog.findViewById(R.id.spinner1);
        final Spinner spinner2 = (Spinner) dialog.findViewById(R.id.spinner2);

        Button buttonPost = (Button) dialog.findViewById(R.id.btn_send);


        //p = post

        uEmail.setText(SharedPrefManager.getInstance(getContext()).getUserEmail());


        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        uDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                sYear = c.get(Calendar.YEAR);
                sMonth = c.get(Calendar.MONTH);
                sDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                date = ""+year;

                                // FIX : change to year
                                uDob.setText(date);


                            }
                        }, sYear, sMonth, sDay);


                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.setTitle(" Select Birth Date");
                datePickerDialog.show();

            }
        });


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                uBloodType.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                uSex.setText(parent.getItemAtPosition(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String pFullName = uFullName.getText().toString().trim();
                final String pAge = uDob.getText().toString().trim();
                final String pBloodType = uBloodType.getText().toString().trim();
                final String pDistrict = uDistrict.getText().toString().trim();
                final String pLocalAddress = uLocalAddress.getText().toString().trim();
                final String pContactNumber = uContactNumber.getText().toString().trim();
                final String pSex = uSex.getText().toString().trim();
                final String pEmail = uEmail.getText().toString().trim();


                if (pFullName.isEmpty() && pBloodType.isEmpty() || pDistrict.isEmpty() || pLocalAddress.isEmpty()
                        || pContactNumber.isEmpty() || pSex.isEmpty() || pEmail.isEmpty()) {

                    Toast.makeText(getContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();

                } else {

                    final int Age = Integer.parseInt(pAge);

                    final int checkYear = cYear - Age;

                    if (checkYear < 18 || checkYear > 60 ){
                        Toast.makeText(getContext(), "Sorry , you are not eligible for donating blood !", Toast.LENGTH_SHORT).show();
                    }else {

                        progressDialog = new SpotsDialog(getContext(), R.style.Custom);
                        progressDialog.show();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.POST_BLOOD_DATABASE_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                                progressDialog.hide();
                                dialog.dismiss();
                                progressDialog.cancel();

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(getContext(), "Oops ! Some error occur :( " + error, Toast.LENGTH_SHORT).show();

                                progressDialog.hide();

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {

                                Map<String, String> params = new HashMap<String, String>();

                                params.put(UrlConstants.KEY_FULLNAME, pFullName);
                                params.put(UrlConstants.KEY_AGE, String.valueOf(checkYear));
                                params.put(UrlConstants.KEY_SEX, pSex);
                                params.put(UrlConstants.KEY_DISTRICT, pDistrict);
                                params.put(UrlConstants.KEY_LOCAL_ADDRESS, pLocalAddress);
                                params.put(UrlConstants.KEY_EMAIL, pEmail);
                                params.put(UrlConstants.KEY_CONTACT_NUMBER, pContactNumber);
                                params.put(UrlConstants.KEY_BLOOD_GROUP, pBloodType);
                                return params;

                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(stringRequest);


                    }


                }


            }


    });

        if(dialog.getWindow()!=null)
                dialog.getWindow().

    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().

    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().

    getAttributes().windowAnimations =R.style.DialogAnimation;
        dialog.show();


}

//    private void getAge(int year, int month, int day) {
//        Calendar dob = Calendar.getInstance();
//        Calendar today = Calendar.getInstance();
//
//        dob.set(year, month, day);
//
//        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
//
//        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
//            age--;
//        }
//
//        Integer ageInt = new Integer(age);
//        String ageS = ageInt.toString();
//        ageYear = Integer.parseInt(ageS);
//
//
//    }


//

}
