package com.naran.wecare.CustomViews;

import android.support.v7.widget.RecyclerView;

/**
 * Created by NaRan on 6/3/17.
 */

public abstract class WeCareRecyclerViewAdapter extends RecyclerView.Adapter {
    abstract protected void add(Object object);
    abstract protected void clear();
}
