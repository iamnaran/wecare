package com.naran.wecare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naran.wecare.Activity.DeleteActivity;
import com.naran.wecare.Activity.WeCareActivity;
import com.naran.wecare.Fragments.BloodBankFragment;
import com.naran.wecare.Fragments.BloodDatabaseFragment;
import com.naran.wecare.Fragments.DonationEventFragment;
import com.naran.wecare.Fragments.HomeFragment;
import com.naran.wecare.Models.RequestHandler;
import com.naran.wecare.Models.SharedPrefManager;
import com.naran.wecare.URLConstants.UrlConstants;
import com.naran.wecare.fcm.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MainActivity extends WeCareActivity implements View.OnClickListener {

    int sYear;
    int sMonth;
    int sDay;
    private Date date;
    String datePicked = "";
    private Toolbar toolbar;
    private LinearLayout bottomNavigation;
    private ImageView home, location, search, user;
    private ImageView lastSelectedImageView;
    AlertDialog progressDialog;
    int buttonStatus = 0;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

    private Date currentDate = new Date();
    private Date todayDate = new Date(currentDate.getTime());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendToken();
        initiliseView();
        initialiseListener();


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        setUpFragment(new HomeFragment());


//        if (CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
//        {
//
//        } else {
//            Toast.makeText(getApplicationContext(), "No internet Connection", Toast.LENGTH_SHORT).show();
//        }


    }

    @Override
    protected void initiliseView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bottomNavigation = (LinearLayout) findViewById(R.id.bottom_navigation);
        home = (ImageView) findViewById(R.id.home);
        lastSelectedImageView = home;
        search = (ImageView) findViewById(R.id.search);
        location = (ImageView) findViewById(R.id.location);
        user = (ImageView) findViewById(R.id.user);

    }

    @Override
    protected void initialiseListener() {

        bottomNavigation.setOnClickListener(this);
        home.setOnClickListener(this);
        location.setOnClickListener(this);
        search.setOnClickListener(this);
        user.setOnClickListener(this);
    }

    private void setUpFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        fragmentTransaction.commit();
    }

    private void setUpBottomNavigation(ImageView view) {

        view.setColorFilter(R.color.toolbar_background);

        if (lastSelectedImageView != null) {
            lastSelectedImageView.setColorFilter(Color.parseColor("#000000"));
        }
        view.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        lastSelectedImageView = view;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.home:
                setUpBottomNavigation(home);
                setUpFragment(new HomeFragment());
                break;
            case R.id.search:
                setUpBottomNavigation(search);
                setUpFragment(new BloodDatabaseFragment());
                break;

            case R.id.location:
                setUpBottomNavigation(location);
                setUpFragment(new BloodBankFragment());
                break;

            case R.id.user:
                setUpBottomNavigation(user);
                setUpFragment(new DonationEventFragment());
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_homepage, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_add_request) {

            final EditText full_name;
            final EditText blood_type;
            final EditText blood_amount;
            final EditText contact_number;
            final EditText donation_date;
            final EditText donation_place;
            final EditText donation_type;
            Button buttonPost;
            Spinner spinner1, spinner2;

            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater layoutInflater = getLayoutInflater();
            final View customView = layoutInflater.inflate(R.layout.post_blood_request, null);
            builder.setView(customView);
            builder.create();

            final AlertDialog alertDialog1 = builder.show();

//            builder.show();

            full_name = (EditText) customView.findViewById(R.id.post_full_name);
            blood_type = (EditText) customView.findViewById(R.id.post_blood_type);
            blood_amount = (EditText) customView.findViewById(R.id.post_blood_amount);
            contact_number = (EditText) customView.findViewById(R.id.post_donation_contact_number);
            donation_date = (EditText) customView.findViewById(R.id.post_donation_date);
            donation_place = (EditText) customView.findViewById(R.id.post_donation_place);
            donation_type = (EditText) customView.findViewById(R.id.post_donation_type); // changed to date
            buttonPost = (Button) customView.findViewById(R.id.button_post_request);

            spinner1 = (Spinner) customView.findViewById(R.id.spinner1);
            spinner2 = (Spinner) customView.findViewById(R.id.spinner2);

            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    blood_type.setText(parent.getItemAtPosition(position).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    blood_amount.setText(parent.getItemAtPosition(position).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            donation_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            donation_type.setText( selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();

                }
            });


            donation_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    sYear = c.get(Calendar.YEAR);
                    sMonth = c.get(Calendar.MONTH);
                    sDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(builder.getContext(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                    datePicked = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                                    donation_date.setText(datePicked);




                                }
                            }, sYear, sMonth, sDay);
                    datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                    datePickerDialog.show();


                }
            });

            buttonPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = new SpotsDialog(builder.getContext(), R.style.Custom);

// p indicates post

                    final String p_full_name = full_name.getText().toString().trim();
                    final String p_blood_type = blood_type.getText().toString().trim();
                    final String p_blood_amount = blood_amount.getText().toString().trim();
                    final String p_contact_number = contact_number.getText().toString().trim();
                    final String p_donation_date = donation_date.getText().toString().trim();
                    final String p_donation_place = donation_place.getText().toString().trim();
                    final String p_donation_type = donation_type.getText().toString().trim();

                    if (p_full_name.isEmpty() || p_blood_type.isEmpty() || p_blood_amount.isEmpty() || p_contact_number.isEmpty()
                            || p_donation_date.isEmpty() || p_donation_place.isEmpty() || p_donation_type.isEmpty()
                            ) {
                        Toast.makeText(MainActivity.this, " All fields are mandatory ", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.POST_BLOOD_REQUEST_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Toast.makeText(getApplicationContext(), " Request " + response, Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                                alertDialog1.dismiss();

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(getApplicationContext(), " Request " + error, Toast.LENGTH_SHORT).show();

                                progressDialog.hide();

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {

                                Map<String, String> params = new HashMap<String, String>();

                                params.put(UrlConstants.KEY_FULLNAME, p_full_name);
                                params.put(UrlConstants.KEY_BLOOD_TYPE, p_blood_type);
                                params.put(UrlConstants.KEY_BLOOD_AMOUNT, p_blood_amount);
                                params.put(UrlConstants.KEY_CONTACT_NUMBER, p_contact_number);
                                params.put(UrlConstants.KEY_DONATION_DATE, p_donation_date);
                                params.put(UrlConstants.KEY_DONATION_PLACE, p_donation_place);
                                params.put(UrlConstants.KEY_DONATION_TYPE, p_donation_type);
                                return params;

                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);


                    }


                }
            });

            if (alertDialog1.getWindow() != null)
                alertDialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            alertDialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            alertDialog1.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
            alertDialog1.show();


        }
        if (id == R.id.action_profile) {

            if (SharedPrefManager.getInstance(this).isLoggedIn() || SharedPrefManager.getInstance(this).isOrgLoggedIn()) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_Design_Light_BottomSheetDialog);
                LayoutInflater layoutInflater = getLayoutInflater();
                final View customView = layoutInflater.inflate(R.layout.user_details_popup, null);
                builder.setView(customView);
                builder.create();
                final AlertDialog alertDialog2 = builder.show();
                TextView textViewUsername, textViewUserEmail;
                final Button buttonLogout , buttonDelete;

                textViewUsername = (TextView) customView.findViewById(R.id.textViewUsername);
                textViewUserEmail = (TextView) customView.findViewById(R.id.textViewUseremail);
                buttonDelete = (Button) customView.findViewById(R.id.buttonDelete);
                buttonLogout = (Button) customView.findViewById(R.id.buttonLogout);

                textViewUserEmail.setText(SharedPrefManager.getInstance(this).getUserEmail());
                textViewUsername.setText(SharedPrefManager.getInstance(this).getUsername());

                if (SharedPrefManager.getInstance(this).isOrgLoggedIn()) {
                    textViewUserEmail.setText(SharedPrefManager.getInstance(this).getOrgEmail());
                    textViewUsername.setText(SharedPrefManager.getInstance(this).getOrgUsername());
                }


                buttonLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                        SharedPrefManager.getInstance(getApplicationContext()).orgLogout();
                        alertDialog2.dismiss();
                        Toast.makeText(MainActivity.this, "Logged out :( ", Toast.LENGTH_SHORT).show();

                    }
                });

                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog2.dismiss();

                        Intent intent = new Intent(MainActivity.this, DeleteActivity.class);
                        startActivity(intent);

                    }
                });


            } else {


//                ##########################################################################################################
//                LOGIN register organization


                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_Design_Light_BottomSheetDialog);
                LayoutInflater layoutInflater = getLayoutInflater();
                final View customView = layoutInflater.inflate(R.layout.login_pop__layout, null);
                builder.setView(customView);
                builder.create();
                final AlertDialog alertDialog = builder.show();
                final Button buttonLogin = (Button) customView.findViewById(R.id.buttonLogin);
                final Button buttonRegister = (Button) customView.findViewById(R.id.buttonRegister);
                final Button buttonOrg = (Button) customView.findViewById(R.id.buttonOrg);
                final Button buttonGo = (Button) customView.findViewById(R.id.buttonGo);

                final EditText editTextUser = (EditText) customView.findViewById(R.id.editTextUsername);
                final EditText editTextPass = (EditText) customView.findViewById(R.id.editTextPassword);
                final EditText editTextEmail = (EditText) customView.findViewById(R.id.editTexTEmail);
                final LinearLayout emailLayout = (LinearLayout) customView.findViewById(R.id.emailLayout);
                emailLayout.setVisibility(View.GONE);
                buttonLogin.setPressed(true);
                buttonStatus = 1;

                buttonLogin.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        buttonLogin.setPressed(true);
                        buttonRegister.setPressed(false);
                        buttonOrg.setPressed(false);
                        emailLayout.setVisibility(View.GONE);
                        buttonStatus = 1;
                        return true;
                    }
                });

                buttonRegister.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        buttonRegister.setPressed(true);
                        buttonLogin.setPressed(false);
                        buttonOrg.setPressed(false);
                        emailLayout.setVisibility(View.VISIBLE);
                        buttonStatus = 2;
                        return true;
                    }
                });

                buttonOrg.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        buttonOrg.setPressed(true);
                        buttonRegister.setPressed(false);
                        buttonLogin.setPressed(false);
                        emailLayout.setVisibility(View.GONE);
                        buttonStatus = 3;
                        return true;
                    }
                });
                buttonGo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog = new SpotsDialog(builder.getContext(), R.style.Custom);

                        if (buttonStatus == 1) {
// Login
                            // FIX:

                            final String username = editTextUser.getText().toString().trim();
                            final String password = editTextPass.getText().toString().trim();

                            if (username.isEmpty() || password.isEmpty()) {
                                editTextUser.setError(" All fields are mandatory");
                                editTextPass.setError("All fields are mandatory");
                            } else {

                                progressDialog.show();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.URL_LOGIN, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog.dismiss();

                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            if (!obj.getBoolean("error")) {
                                                SharedPrefManager.getInstance(getApplicationContext())
                                                        .userLogin(
                                                                obj.getInt("id"),
                                                                obj.getString("username"),
                                                                obj.getString("email")
                                                        );
                                                Toast.makeText(MainActivity.this, "Woah ! You are logged in..", Toast.LENGTH_SHORT).show();
                                                alertDialog.dismiss();
                                            } else {
                                                Toast.makeText(
                                                        getApplicationContext(),
                                                        obj.getString("message"),
                                                        Toast.LENGTH_LONG
                                                ).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                progressDialog.dismiss();

                                                Toast.makeText(getApplicationContext(), " Something went wrong ", Toast.LENGTH_SHORT).show();


                                            }
                                        }
                                ) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("username", username);
                                        params.put("password", password);
                                        return params;
                                    }

                                };

                                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                            }

                        }
                        if (buttonStatus == 2) {
                            //Register
                            final String email = editTextEmail.getText().toString().trim();
                            final String username = editTextUser.getText().toString().trim();
                            final String password = editTextPass.getText().toString().trim();

                            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                            if (email.matches(emailPattern)) {

                                progressDialog.show();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.URL_REGISTER,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                progressDialog.hide();

                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                    alertDialog.dismiss();

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.hide();
                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("username", username);
                                        params.put("email", email);
                                        params.put("password", password);
                                        return params;
                                    }
                                };

                                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                            }
                            if (email.isEmpty() && username.isEmpty() || password.isEmpty()) {
                                editTextUser.setError("All fields are mandatory");
                                editTextPass.setError("All fields are mandatory");
                                editTextEmail.setError("All fields are mandatory");

                            }
                            if (!email.matches(emailPattern)) {
                                editTextEmail.setError("Invalid email");

                            }
                        }
                        if (buttonStatus == 3) {
//org login
                            final String username = editTextUser.getText().toString().trim();
                            final String password = editTextPass.getText().toString().trim();
                            if (username.isEmpty() || password.isEmpty()) {
                                editTextUser.setError("All fields are mandatory");
                                editTextPass.setError("All fields are mandatory");
                            } else {

                                progressDialog.show();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.URL_ORG_LOGIN, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog.dismiss();

                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            if (!obj.getBoolean("error")) {
                                                SharedPrefManager.getInstance(getApplicationContext())
                                                        .userOrganizationLogin(
                                                                obj.getInt("id"),
                                                                obj.getString("username"),
                                                                obj.getString("email")
                                                        );
                                                Toast.makeText(MainActivity.this, "Woah ! You are logged in..", Toast.LENGTH_SHORT).show();
                                                alertDialog.dismiss();
                                            } else {
                                                Toast.makeText(
                                                        getApplicationContext(),
                                                        obj.getString("message"),
                                                        Toast.LENGTH_LONG
                                                ).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                progressDialog.dismiss();
                                                if(error instanceof ServerError){

                                                }

                                                Toast.makeText(getApplicationContext(), " Something went wrong.", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                ) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("username", username);
                                        params.put("password", password);
                                        return params;
                                    }

                                };

                                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                            }
                        }
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendToken() {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), "");
        Log.e("Token", token);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.URL_FCM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("app_id", token);
                return params;
            }
        };
        MySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


}
