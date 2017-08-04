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

import com.naran.wecare.Models.Organization;
import com.naran.wecare.R;

import java.util.List;

/**
 * Created by NaRan on 6/8/17.
 */

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.ViewHolders> {

    private Context context;
    private List<Organization> organizationList;
    private int lastPosition = -1;


    public OrganizationAdapter(Context context, List<Organization> organizationList) {
        this.context = context;
        this.organizationList = organizationList;
    }


    @Override
    public OrganizationAdapter.ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_banks_adapter, parent,false);
        return new OrganizationAdapter.ViewHolders(view);
    }

    @Override
    public void onBindViewHolder(OrganizationAdapter.ViewHolders holder, int position) {

        final Organization org = organizationList.get(position);

        holder.aP.setText(org.getaP());
        holder.aN.setText(org.getaN());
        holder.bP.setText(org.getbP());
        holder.bN.setText(org.getbN());
        holder.abP.setText(org.getAbP());
        holder.abN.setText(org.getAbN());
        holder.oP.setText(org.getoP());
        holder.oN.setText(org.getoN());
        holder.org_name.setText(org.getUserName());

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String phoneNumber = "tel:" + org.getContactNumber();
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
        return organizationList.size();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolders holder) {

            super.onViewDetachedFromWindow(holder);
            holder.itemView.clearAnimation();

    }

    public class ViewHolders extends RecyclerView.ViewHolder {

        private TextView org_name;
        private ImageView callButton;
        private TextView aP;
        private TextView aN;
        private TextView bP;
        private TextView bN;
        private TextView abP;
        private TextView abN;
        private TextView oP;
        private TextView oN;

        public ViewHolders(View itemView) {
            super(itemView);

            org_name = (TextView) itemView.findViewById(R.id.org_name);
            callButton = (ImageView) itemView.findViewById(R.id.call_button);
            aP = (TextView) itemView.findViewById(R.id.aP);
            aN = (TextView) itemView.findViewById(R.id.aN);
            bP = (TextView) itemView.findViewById(R.id.bP);
            bN = (TextView) itemView.findViewById(R.id.bN);
            abP = (TextView) itemView.findViewById(R.id.abP);
            abN = (TextView) itemView.findViewById(R.id.abN);
            oP = (TextView) itemView.findViewById(R.id.oP);
            oN = (TextView) itemView.findViewById(R.id.oN);
;
        }
    }
}
