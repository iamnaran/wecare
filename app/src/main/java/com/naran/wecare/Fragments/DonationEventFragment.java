package com.naran.wecare.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naran.wecare.CustomViews.DonationEventAdapter;
import com.naran.wecare.Models.Event;
import com.naran.wecare.Models.SharedPrefManager;
import com.naran.wecare.R;
import com.naran.wecare.URLConstants.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class DonationEventFragment extends WeCareFragment {

    private RecyclerView recyclerView;
    SwipeRefreshLayout swipeView;
    DonationEventAdapter donationEventAdapter;
    List<Event> eventList;
    private Animation myAni;
    AlertDialog progressDialog;
    int sYear;
    int sMonth;
    int sDay;
    String date = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_donation_event, null);
        initiliseView(view);
        initialiseListener();
        setUpRecyclerView();
        getDonationEvent();
        return view;

    }

    private void setUpRecyclerView() {

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager1);
        eventList = new ArrayList<>();
        donationEventAdapter = new DonationEventAdapter(getContext(), eventList);
        recyclerView.setAdapter(donationEventAdapter);

    }

    @Override
    protected void initiliseView(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewDonationEvent);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        myAni = AnimationUtils.loadAnimation(getContext(), R.anim.down_from_top);
        swipeView.setColorSchemeColors(

                Color.RED
        );

        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.addEvent);
        myFab.startAnimation(myAni);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (SharedPrefManager.getInstance(getContext()).isLoggedIn() || SharedPrefManager.getInstance(getContext()).isOrgLoggedIn()) {

                    showPostEventDialog();

                } else {
                    Toast.makeText(getContext(), "Please Login ", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    protected void initialiseListener() {

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeView.setRefreshing(true);
                getDonationEvent();
                eventList.clear();
                donationEventAdapter.notifyDataSetChanged();
                swipeView.setRefreshing(false);
            }
        });

    }



    private void getDonationEvent() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConstants.GET_EVENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("HELLO", "Response: " + response);


                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {


                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String event_name = jsonObject.getString(UrlConstants.KEY_EVENT_NAME);
                        String location = jsonObject.getString(UrlConstants.KEY_EVENT_LOCATION);
                        String contact_number = jsonObject.getString(UrlConstants.KEY_EVENT_CONTACT);
                        String time_start = jsonObject.getString(UrlConstants.KEY_EVENT_START_TIME);
                        String time_end = jsonObject.getString(UrlConstants.KEY_EVENT_END_TIME);
                        String event_date = jsonObject.getString(UrlConstants.KEY_EVENT_DATE);

                        Event event = new Event();

                        event.setEvent_name(event_name);
                        event.setLocation(location);
                        event.setContact_number(contact_number);
                        event.setTime_start(time_start);
                        event.setTime_end(time_end);
                        event.setEvent_date(event_date);
                        eventList.add(event);

                    }
                    donationEventAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
//        {
//
//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                try {
//                    notificationList.clear();
//                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
//                    if (cacheEntry == null) {
//                        cacheEntry = new Cache.Entry();
//                    }
//                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
//                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
//                    long now = System.currentTimeMillis();
//                    final long softExpire = now + cacheHitButRefreshed;
//                    final long ttl = now + cacheExpired;
//                    cacheEntry.data = response.data;
//                    cacheEntry.softTtl = softExpire;
//                    cacheEntry.ttl = ttl;
//                    String headerValue;
//                    headerValue = response.headers.get("Date");
//                    if (headerValue != null) {
//                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                    }
//                    headerValue = response.headers.get("Last-Modified");
//                    if (headerValue != null) {
//                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                    }
//                    cacheEntry.responseHeaders = response.headers;
//                    final String jsonString = new String(response.data,
//                            HttpHeaderParser.parseCharset(response.headers));
//                    return Response.success(new String(jsonString), cacheEntry);
//                } catch (UnsupportedEncodingException e) {
//                    return Response.error(new ParseError(e));
//                }
//            }
//
//            @Override
//            protected void deliverResponse(String response) {
//                super.deliverResponse(response);
//            }
//
//            @Override
//            public void deliverError(VolleyError error) {
//                super.deliverError(error);
//            }
//
//            @Override
//            protected VolleyError parseNetworkError(VolleyError volleyError) {
//                return super.parseNetworkError(volleyError);
//            }
//        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);


    }

    private void showPostEventDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.AppTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.post_event_layout);

        ImageView cross = (ImageView) dialog.findViewById(R.id.cross);

        final EditText eventName = (EditText) dialog.findViewById(R.id.event_name);
        final EditText eventLocation = (EditText) dialog.findViewById(R.id.event_location);
        final EditText eventContactNumber = (EditText) dialog.findViewById(R.id.event_contact_number);
        final EditText startTime = (EditText) dialog.findViewById(R.id.time_start);
        final EditText endTime = (EditText) dialog.findViewById(R.id.time_end);
        final EditText eventDate = (EditText) dialog.findViewById(R.id.event_date);


        Button buttonPost = (Button) dialog.findViewById(R.id.button_post);

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        eventDate.setOnClickListener(new View.OnClickListener() {
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

                                date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                eventDate.setText(date);

                            }
                        }, sYear, sMonth, sDay);
                datePickerDialog.setTitle(" Select Event Date");
                datePickerDialog.show();

            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentDate = Calendar.getInstance();
                int hour = mCurrentDate.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentDate.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle(" Select Event Start Time");
                mTimePicker.show();

            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentDate = Calendar.getInstance();
                int hour = mCurrentDate.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentDate.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle(" Select Event End Time");
                mTimePicker.show();

            }
        });



        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new SpotsDialog(getContext(), R.style.Custom);
                progressDialog.show();


                StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.POST_EVENT, new Response.Listener<String>() {
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

                        params.put(UrlConstants.KEY_EVENT_NAME, eventName.getText().toString().trim());
                        params.put(UrlConstants.KEY_EVENT_LOCATION, eventLocation.getText().toString().trim());
                        params.put(UrlConstants.KEY_CONTACT_NUMBER, eventContactNumber.getText().toString().trim());
                        params.put(UrlConstants.KEY_EVENT_START_TIME, startTime.getText().toString().trim());
                        params.put(UrlConstants.KEY_EVENT_END_TIME, endTime.getText().toString().trim());
                        params.put(UrlConstants.KEY_EVENT_DATE, eventDate.getText().toString().trim());
                        return params;

                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);

            }
        });

        if (dialog.getWindow() != null)
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();


    }


}
