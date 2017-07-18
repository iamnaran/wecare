package com.naran.wecare.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
import com.naran.wecare.CustomViews.OrganizationAdapter;
import com.naran.wecare.Models.Organization;
import com.naran.wecare.Models.SharedPrefManager;
import com.naran.wecare.R;
import com.naran.wecare.URLConstants.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;


public class BloodBankFragment extends WeCareFragment {

    private RecyclerView recyclerView;
    SwipeRefreshLayout swipeView;
    OrganizationAdapter organizationAdapter;
    List<Organization> organizationList;
    private Animation myAni;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_blood_bank, null);
        initiliseView(view);
        initialiseListener();
        setUpRecyclerView();
        getBloodBankData();
        return view;
    }

    private void setUpRecyclerView() {


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

        if (SharedPrefManager.getInstance(getContext()).isOrgLoggedIn()) {

            myFab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (SharedPrefManager.getInstance(getContext()).isOrgLoggedIn()) {

                        showPostBloodBankData();

                    } else {
                        Toast.makeText(getContext(), "Sorry!! This feature is for Blood Bank Organization ", Toast.LENGTH_SHORT).show();
                    }


                }
            });

        }

        myFab.setVisibility(View.GONE);


        if (SharedPrefManager.getInstance(getContext()).isOrgLoggedIn()) {

            myFab.setVisibility(View.VISIBLE);
            myFab.startAnimation(myAni);


        }


    }


    @Override
    protected void initialiseListener() {



    }

    private void getBloodBankData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConstants.GET_BLOOD_DATABASE_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", response);


                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String org_name = jsonObject.getString(UrlConstants.KEY_ORG_NAME);
                        String update_date = jsonObject.getString(UrlConstants.KEY_UPDATE_DATE);
                        String contact_number = jsonObject.getString(UrlConstants.KEY_ORG_CONTACT_NUMBER);
                        String aP = jsonObject.getString(UrlConstants.KEY_AP);
                        String aN = jsonObject.getString(UrlConstants.KEY_AN);
                        String bP = jsonObject.getString(UrlConstants.KEY_BP);
                        String bN = jsonObject.getString(UrlConstants.KEY_BN);
                        String abP = jsonObject.getString(UrlConstants.KEY_ABP);
                        String abN = jsonObject.getString(UrlConstants.KEY_ABN);
                        String oP = jsonObject.getString(UrlConstants.KEY_OP);
                        String oN = jsonObject.getString(UrlConstants.KEY_ON);

                        Organization org = new Organization();

                        org.setOrg_name(org_name);
                        org.setUpdate_date(update_date);
                        org.setContact_number(contact_number);
                        org.setaP(aP);
                        org.setaN(aN);
                        org.setbP(bP);
                        org.setbN(bN);
                        org.setAbP(abP);
                        org.setAbN(abN);
                        org.setoP(oP);
                        org.setoN(oN);
                        organizationList.add(org);


                    }
                    organizationAdapter.notifyDataSetChanged();


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

    private void showPostBloodBankData() {

        final Dialog dialog = new Dialog(getActivity(), R.style.AppTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.post_blood_bank_data);

        ImageView cross = (ImageView) dialog.findViewById(R.id.cross);
        ImageView done = (ImageView) dialog.findViewById(R.id.done);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        if (dialog.getWindow() != null)
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();



    }
}
