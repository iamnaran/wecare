package com.naran.wecare;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.naran.wecare.Activity.WeCareActivity;
import com.naran.wecare.Fragments.BloodDatabaseFragment;
import com.naran.wecare.Fragments.HomeFragment;
import com.naran.wecare.Fragments.LocationFragment;
import com.naran.wecare.Fragments.UserFragment;

public class MainActivity extends WeCareActivity implements View.OnClickListener{


    private Toolbar toolbar;
    private LinearLayout bottomNavigation;
    private ImageView home, location, search, user;
    private ImageView lastSelectedImageView;


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
                setUpFragment(new LocationFragment());
                break;

            case R.id.user:
                setUpBottomNavigation(user);
                setUpFragment(new UserFragment());
                break;
        }

    }
    
}
