package com.naran.wecare.CustomViews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


    @Override
    public OrganizationAdapter.ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_banks_adapter, parent,false);
        return new OrganizationAdapter.ViewHolders(view);
    }

    @Override
    public void onBindViewHolder(OrganizationAdapter.ViewHolders holder, int position) {
        final Organization org = organizationList.get(position);
        holder.org_name.setText(org.getOrg_name());
        holder.updateDate.setText(org.getUpdate_date());
        holder.aP.setText(org.getaP());
        holder.aN.setText(org.getaN());
        holder.bP.setText(org.getbP());
        holder.bN.setText(org.getbN());
        holder.abP.setText(org.getAbP());
        holder.abN.setText(org.getAbN());
        holder.oP.setText(org.getoP());
        holder.oN.setText(org.getoN());

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String phoneNumber = "tel:" + org.getContact_number();
                intent.setData(Uri.parse(phoneNumber));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return organizationList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {

        private TextView org_name;
        private ImageView callButton;
        private TextView updateDate;
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
            updateDate = (TextView) itemView.findViewById(R.id.update_date);
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
