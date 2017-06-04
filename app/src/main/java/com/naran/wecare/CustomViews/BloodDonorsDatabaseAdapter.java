package com.naran.wecare.CustomViews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.naran.wecare.Models.BloodDatabase;
import com.naran.wecare.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NaRan on 5/24/17.
 */

public class BloodDonorsDatabaseAdapter extends RecyclerView.Adapter<BloodDonorsDatabaseAdapter.ViewHolders>{
    private Context context;
    private List<BloodDatabase> bloodDatabaseList;
    private int lastPosition = -1;
    public BloodDonorsDatabaseAdapter(Context context, List<BloodDatabase> bloodDatabaseList) {
        this.context = context;
        this.bloodDatabaseList = bloodDatabaseList;
    }

    @Override
    public ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_donors_db_adapter, parent, false);

        return new BloodDonorsDatabaseAdapter.ViewHolders(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolders holder, int position) {
        final BloodDatabase bloodDatabase = bloodDatabaseList.get(position);
        holder.full_name.setText(bloodDatabase.getFull_name());
        holder.blood_group.setText(bloodDatabase.getBlood_group());
        holder.contact_number.setText(bloodDatabase.getContact_number());
        holder.age.setText(bloodDatabase.getAge());
        holder.sex.setText(bloodDatabase.getSex());
        holder.local_address.setText(bloodDatabase.getLocal_address());
        holder.email.setText(bloodDatabase.getEmail());
        holder.district.setText(bloodDatabase.getDistrict());

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String phoneNumber = "tel:" + bloodDatabase.getContact_number();
                intent.setData(Uri.parse(phoneNumber));
                context.startActivity(intent);
            }
        });



        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;


    }


    @Override
    public int getItemCount() {
        return bloodDatabaseList.size();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolders holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    class ViewHolders extends RecyclerView.ViewHolder {


        private TextView full_name;
        private TextView blood_group;
        private TextView contact_number;
        private TextView age;
        private TextView sex;
        private TextView local_address;
        private TextView district;
        private TextView email;
        private ImageView callButton;

        private ViewHolders(View itemView) {
            super(itemView);
            full_name = (TextView) itemView.findViewById(R.id.full_name);
            blood_group = (TextView) itemView.findViewById(R.id.blood_group);
            contact_number = (TextView) itemView.findViewById(R.id.contact_number);
            district = (TextView) itemView.findViewById(R.id.district);
            age = (TextView) itemView.findViewById(R.id.age);
            sex = (TextView) itemView.findViewById(R.id.sex);
            local_address = (TextView) itemView.findViewById(R.id.local_address);
            email = (TextView) itemView.findViewById(R.id.email);
            callButton = (ImageView) itemView.findViewById(R.id.call_button);


        }

    }

    public  void setFilter(List<BloodDatabase> bloodDatabases){

        bloodDatabaseList = new ArrayList<>();
        bloodDatabaseList.addAll(bloodDatabases);
        notifyDataSetChanged();
    }


}
