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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    DateFormat format2=new SimpleDateFormat("dd");

    private Calendar c = Calendar.getInstance();

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
        holder.donation_place.setText(notification.getDonation_place());
        holder.donation_date.setText(notification.getDonation_date());
        holder.donation_amount.setText(notification.getBlood_amount());

        String dateCheck = notification.getDonation_date();




//        try {
//            date = dateFormatter.parse(dateCheck);
//            if (date.before(tomDate)) {
//                holder.donation_date.setText(R.string.donation_expired);
//                holder.call_button.setVisibility(View.INVISIBLE);
//                holder.donation_type.setVisibility(View.INVISIBLE);
//
//            }else{
//
//                holder.donation_date.setText(notification.getDonation_date());
//                holder.call_button.setVisibility(View.VISIBLE);
//                holder.donation_type.setVisibility(View.VISIBLE);
//
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        try {
            Date date1 =dateFormatter.parse(dateCheck);
            String finalDay=format2.format(date1);

            int dayOfWeek = c.get(Calendar.DAY_OF_MONTH);

            int day = Integer.parseInt(finalDay);

            int checkDaysRemaining = day - dayOfWeek ;

            System.out.println(checkDaysRemaining);

//            if (checkDaysRemaining > 1){
//
//                holder.iconTime.setVisibility(View.VISIBLE);
//                holder.donation_date.setText(" "+checkDaysRemaining +"  days left");
//                holder.donation_type.setText("Time : "+notification.getDonation_type());
//
//            }
            if(checkDaysRemaining == 0){
                holder.donation_type.setVisibility(View.VISIBLE);
                holder.donation_date.setText(" TODAY ");
                holder.donation_type.setText(notification.getDonation_type());
                holder.iconTime.setVisibility(View.VISIBLE);

            }
            else {

                if (checkDaysRemaining < 0){
                    holder.call_button.setVisibility(View.INVISIBLE);
                    holder.iconTime.setVisibility(View.INVISIBLE);
                    holder.donation_date.setText("CLOSED");
                    holder.donation_type.setVisibility(View.INVISIBLE);


                }else {
                    holder.donation_type.setText(checkDaysRemaining+" days left");

                    holder.call_button.setVisibility(View.VISIBLE);
                    holder.iconTime.setVisibility(View.VISIBLE);
                    holder.donation_date.setVisibility(View.VISIBLE);


                }


            }


        } catch (ParseException | IndexOutOfBoundsException e) {
            e.printStackTrace();
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
        private ImageView iconTime;

        private ViewHolder(View itemView) {
            super(itemView);

            full_name = (TextView) itemView.findViewById(R.id.adapter_full_name);
            blood_type = (TextView) itemView.findViewById(R.id.adapter_blood_type);
            donation_date = (TextView) itemView.findViewById(R.id.adapter_donation_date);
            donation_place = (TextView) itemView.findViewById(R.id.adapter_donation_place);
            call_button = (ImageView) itemView.findViewById(R.id.call_button_request);
            iconTime = (ImageView) itemView.findViewById(R.id.iconTime);
            donation_type = (TextView) itemView.findViewById(R.id.adapter_donation_type);
            donation_amount = (TextView) itemView.findViewById(R.id.adapter_blood_amount);

        }
    }


}
