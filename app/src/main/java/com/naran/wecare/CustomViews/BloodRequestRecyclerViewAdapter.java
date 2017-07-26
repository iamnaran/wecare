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

import com.naran.wecare.Models.Notification;
import com.naran.wecare.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by NaRan on 5/21/17.
 */


public class BloodRequestRecyclerViewAdapter extends RecyclerView.Adapter<BloodRequestRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Notification> notificationList;
    private int lastPosition = -1;

    private Date date;
    private Date currentDate = new Date();
    private Date tomDate = new Date(currentDate.getTime() - (1000 * 60 * 60 * 24));

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

    public BloodRequestRecyclerViewAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @Override
    public BloodRequestRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_request_adapter, parent, false);

        return new BloodRequestRecyclerViewAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final BloodRequestRecyclerViewAdapter.ViewHolder holder, int position) {

        final Notification notification = notificationList.get(position);
        holder.full_name.setText(notification.getFull_name());
        holder.blood_type.setText(notification.getBlood_type());
        holder.donation_date.setText(notification.getDonation_date());
        holder.donation_place.setText(notification.getDonation_place());
        holder.donation_type.setText(notification.getDonation_type());
        holder.donation_amount.setText(notification.getBlood_amount());

        String dateCheck = notification.getDonation_date();


        int status = 0;
        try {
            date = dateFormatter.parse(dateCheck);

            if (date.before(tomDate)){

                status = 1;

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (status == 1){

//            holder.donation_date.setText(R.string.not_available);
//            holder.call_button.setVisibility(View.INVISIBLE);
//            holder.donation_type.setVisibility(View.INVISIBLE);

        }


        holder.call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String phoneNumber = "tel:" + notification.getContact_number();
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
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {

        return notificationList.size();
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView full_name;
        private TextView blood_type;
        private TextView donation_date;
        private TextView donation_place;
        private TextView donation_type;
        private TextView donation_amount;
        private ImageView call_button;

        private ViewHolder(View itemView) {
            super(itemView);

            full_name = (TextView) itemView.findViewById(R.id.adapter_full_name);
            blood_type = (TextView) itemView.findViewById(R.id.adapter_blood_type);
            donation_date = (TextView) itemView.findViewById(R.id.adapter_donation_date);
            donation_place = (TextView) itemView.findViewById(R.id.adapter_donation_place);
            call_button = (ImageView) itemView.findViewById(R.id.call_button_request);
            donation_type = (TextView) itemView.findViewById(R.id.adapter_donation_type);
            donation_amount = (TextView) itemView.findViewById(R.id.adapter_blood_amount);

        }
    }
}
