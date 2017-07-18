package com.naran.wecare.CustomViews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.AlarmClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.naran.wecare.Models.Event;
import com.naran.wecare.R;

import java.util.List;

/**
 * Created by NaRan on 6/8/17.
 */

public class DonationEventAdapter extends RecyclerView.Adapter<DonationEventAdapter.ViewHolders> {

    private Context context;
    private List<Event> eventList;
    private int lastPosition = -1;
    public DonationEventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public DonationEventAdapter.ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_details_adapter,parent,false);

        return new DonationEventAdapter.ViewHolders(view);
    }

    @Override
    public void onBindViewHolder(DonationEventAdapter.ViewHolders holder, int position) {

        final Event event = eventList.get(position);
        holder.event_name.setText(event.getEvent_name());
        holder.location.setText(event.getLocation());
        holder.time_start.setText(event.getTime_start());
        holder.time_end.setText(event.getTime_end());
        holder.event_date.setText(event.getEvent_date());



        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                String phoneNumber = "tel:" + event.getContact_number();
                intent.setData(Uri.parse(phoneNumber));
                context.startActivity(intent);

            }
        });
        holder.addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openClockIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
                openClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                openClockIntent.putExtra(AlarmClock.EXTRA_HOUR, 11);
                context.startActivity(openClockIntent);

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
        return eventList.size();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolders holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {

        private TextView event_name;
        private TextView location;
        private TextView time_start;
        private TextView time_end;
        private TextView event_date;
        private ImageView callButton;
        private ImageView addAlarm;

        public ViewHolders(View itemView) {
            super(itemView);

            event_name = (TextView) itemView.findViewById(R.id.event_name);
            location = (TextView) itemView.findViewById(R.id.event_location);
            time_start = (TextView) itemView.findViewById(R.id.time_start);
            time_end = (TextView) itemView.findViewById(R.id.time_end);
            event_date = (TextView) itemView.findViewById(R.id.event_date);
            callButton = (ImageView) itemView.findViewById(R.id.call_button);
            addAlarm = (ImageView) itemView.findViewById(R.id.addAlarm);


        }
    }
}
