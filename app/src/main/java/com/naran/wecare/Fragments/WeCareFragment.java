package com.naran.wecare.Fragments;

import android.support.v4.app.Fragment;
import android.view.View;


abstract public class WeCareFragment extends Fragment {

    abstract protected void initiliseView(View view);

    abstract protected void initialiseListener();

}
