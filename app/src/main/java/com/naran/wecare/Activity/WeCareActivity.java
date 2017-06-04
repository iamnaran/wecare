package com.naran.wecare.Activity;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by NaRan on 6/3/17.
 */

public abstract class WeCareActivity  extends AppCompatActivity {

    abstract protected void initiliseView();

    abstract protected void initialiseListener();

}