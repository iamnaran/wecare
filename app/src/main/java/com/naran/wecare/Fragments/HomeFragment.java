package com.naran.wecare.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.naran.wecare.CustomViews.BloodRequestRecyclerViewAdapter;
import com.naran.wecare.Models.Notification;
import com.naran.wecare.R;
import com.naran.wecare.URLConstants.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends WeCareFragment {
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipeView;
    BloodRequestRecyclerViewAdapter bloodRequestRecyclerViewAdapter;
    List<Notification> notificationList;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        initiliseView(view);
        initialiseListener();
        setUpRecyclerView();
        return view;
    }

    @Override
    protected void initiliseView(View view) {


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        swipeView.setColorSchemeColors(

                Color.RED
        );



    }

    @Override
    protected void initialiseListener() {

        getNotification();
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeView.setRefreshing(false);
                        notificationList.clear();
                        getNotification();
                        bloodRequestRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });

    }


    private void setUpRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        notificationList = new ArrayList<>();
        bloodRequestRecyclerViewAdapter = new BloodRequestRecyclerViewAdapter(getContext(), notificationList);
        recyclerView.setAdapter(bloodRequestRecyclerViewAdapter);

    }

    private void getNotification() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConstants.GET_BLOOD_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("HELLO", "Response: " + response);


                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String full_name = jsonObject.getString(UrlConstants.KEY_FULLNAME);
                        String blood_type = jsonObject.getString(UrlConstants.KEY_BLOOD_TYPE);
                        String blood_amount = jsonObject.getString(UrlConstants.KEY_BLOOD_AMOUNT);
                        String contact_number = jsonObject.getString(UrlConstants.KEY_CONTACT_NUMBER);
                        String donation_date = jsonObject.getString(UrlConstants.KEY_DONATION_DATE);
                        String donation_place = jsonObject.getString(UrlConstants.KEY_DONATION_PLACE);
                        String donation_type = jsonObject.getString(UrlConstants.KEY_DONATION_TYPE);

                        Notification notification = new Notification();

                        notification.setFull_name(full_name);
                        notification.setBlood_type(blood_type);
                        notification.setBlood_amount(blood_amount);
                        notification.setContact_number(contact_number);
                        notification.setDonation_date(donation_date);
                        notification.setDonation_place(donation_place);
                        notification.setDonation_type(donation_type);
                        notificationList.add(notification);

                    }
                    bloodRequestRecyclerViewAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    notificationList.clear();
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {

                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
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

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);

    }


}
