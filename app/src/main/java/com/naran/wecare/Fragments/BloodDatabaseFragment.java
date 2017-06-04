package com.naran.wecare.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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
import com.naran.wecare.R;
import com.naran.wecare.URLConstants.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class BloodDatabaseFragment extends WeCareFragment {

    private RecyclerView recyclerViewBloodDB;
    SwipeRefreshLayout swipeView1;
    BloodDonorsDatabaseAdapter bloodDonorsDatabaseAdapter;
    List<BloodDatabase> bloodDatabaseList;
    SearchView searchView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_blood_database,container,false);
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


    }

    @Override
    protected void initialiseListener() {

        swipeView1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView1.setRefreshing(true);
                bloodDatabaseList.clear();
                getBloodDonorsDatabase();
                swipeView1.setRefreshing(false);

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
                final List<BloodDatabase> filterModeList = filter(bloodDatabaseList , newText);
                bloodDonorsDatabaseAdapter.setFilter(filterModeList);
                return true;
            }
        });

    }

    private List<BloodDatabase> filter(List<BloodDatabase> bloodDatabaseList, String query) {

        query = query.toUpperCase();
        final  List<BloodDatabase> filteredModeList = new ArrayList<>();
        for (BloodDatabase bloodDatabases : bloodDatabaseList){
            final String text = bloodDatabases.getBlood_group().toUpperCase();
            final String text1 = bloodDatabases.getDistrict().toUpperCase();
            final String text2 = bloodDatabases.getLocal_address().toUpperCase();
            if (text.contains(query) || text1.contains(query) || text2.contains(query)){
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

}
