package com.naran.wecare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.naran.wecare.Activity.WeCareActivity;
import com.naran.wecare.Fragments.BloodBankFragment;
import com.naran.wecare.Fragments.BloodDatabaseFragment;
import com.naran.wecare.Fragments.DonationEventFragment;
import com.naran.wecare.Fragments.HomeFragment;
import com.naran.wecare.Models.RequestHandler;
import com.naran.wecare.Models.SharedPrefManager;
import com.naran.wecare.URLConstants.UrlConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MainActivity extends WeCareActivity implements View.OnClickListener {

    int sYear;
    int sMonth;
    int sDay;
    String date = "";
    private Toolbar toolbar;
    private LinearLayout bottomNavigation;
    private ImageView home, location, search, user;
    private ImageView lastSelectedImageView;
    AlertDialog progressDialog;
    int buttonStatus = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        fragmentTransaction.commit();
    }

    private void setUpBottomNavigation(ImageView view) {
        if (lastSelectedImageView != null) {
            lastSelectedImageView.setColorFilter(Color.parseColor("#FF03071E"));
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
            Spinner spinner1, spinner2, spinner3;

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
            donation_type = (EditText) customView.findViewById(R.id.post_donation_type);
            buttonPost = (Button) customView.findViewById(R.id.button_post_request);

            spinner1 = (Spinner) customView.findViewById(R.id.spinner1);
            spinner2 = (Spinner) customView.findViewById(R.id.spinner2);
            spinner3 = (Spinner) customView.findViewById(R.id.spinner3);

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
            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    donation_type.setText(parent.getItemAtPosition(position).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

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

                                    date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                    donation_date.setText(date);

                                }
                            }, sYear, sMonth, sDay);
                    datePickerDialog.show();


                }
            });

            buttonPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = new SpotsDialog(builder.getContext(), R.style.Custom);
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

                            params.put(UrlConstants.KEY_FULLNAME, full_name.getText().toString().trim());
                            params.put(UrlConstants.KEY_BLOOD_TYPE, blood_type.getText().toString().trim());
                            params.put(UrlConstants.KEY_BLOOD_AMOUNT, blood_amount.getText().toString().trim());
                            params.put(UrlConstants.KEY_CONTACT_NUMBER, contact_number.getText().toString().trim());
                            params.put(UrlConstants.KEY_DONATION_DATE, donation_date.getText().toString().trim());
                            params.put(UrlConstants.KEY_DONATION_PLACE, donation_place.getText().toString().trim());
                            params.put(UrlConstants.KEY_DONATION_TYPE, donation_type.getText().toString().trim());
                            return params;

                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                }
            });

            if (alertDialog1.getWindow() != null)
                alertDialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            alertDialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            alertDialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            alertDialog1.show();



        }
        if (id == R.id.action_profile) {

            if (SharedPrefManager.getInstance(this).isLoggedIn() || SharedPrefManager.getInstance(this).isOrgLoggedIn()) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater layoutInflater = getLayoutInflater();
                final View customView = layoutInflater.inflate(R.layout.user_details_popup, null);
                builder.setView(customView);
                builder.create();
                final AlertDialog alertDialog2 = builder.show();
                TextView textViewUsername, textViewUserEmail;
                final Button buttonLogout;

                textViewUsername = (TextView) customView.findViewById(R.id.textViewUsername);
                textViewUserEmail = (TextView) customView.findViewById(R.id.textViewUseremail);
                buttonLogout = (Button) customView.findViewById(R.id.buttonLogout);

                textViewUserEmail.setText(SharedPrefManager.getInstance(this).getUserEmail());
                textViewUsername.setText(SharedPrefManager.getInstance(this).getUsername());
                textViewUserEmail.setText(SharedPrefManager.getInstance(this).getOrgEmail());
                textViewUsername.setText(SharedPrefManager.getInstance(this).getOrgUsername());

                buttonLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                        SharedPrefManager.getInstance(getApplicationContext()).orgLogout();
                        alertDialog2.dismiss();
                        Toast.makeText(MainActivity.this, "Logged out :( ", Toast.LENGTH_SHORT).show();

                    }
                });


            } else {

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

                            final String username = editTextUser.getText().toString().trim();
                            final String password = editTextPass.getText().toString().trim();
                            if (username.isEmpty() || password.isEmpty()) {
                                editTextUser.setError("");
                                editTextPass.setError("");
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
                                editTextUser.setError("Field shouldn't be empty");
                                editTextPass.setError("Field shouldn't be empty");
                                editTextEmail.setError("Field shouldn't be empty");

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
                                editTextUser.setError("");
                                editTextPass.setError("");
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


}
