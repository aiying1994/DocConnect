package com.example.docconnect.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.docconnect.Common.Common;
import com.example.docconnect.Interface.IRecyclerItemSelectedListener;
import com.example.docconnect.Model.Premise;
import com.example.docconnect.R;
import com.google.common.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyPremiseAdapter extends RecyclerView.Adapter<MyPremiseAdapter.MyViewHolder> {
    Context context;

    List<Premise> premiseList;
    List<CardView> cardViewList;

    LocalBroadcastManager localBroadcastManager;

    public MyPremiseAdapter(Context context, List<Premise> premiseList) {
        this.context = context;
        this.premiseList = premiseList;

        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_premise,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        // getName() and getAddress() came from Model.Saloon
        myViewHolder.txt_premise_name.setText(premiseList.get(position).getName());
        myViewHolder.txt_premise_description.setText(premiseList.get(position).getDescription());

        if(!cardViewList.contains(myViewHolder.card_premise))
            cardViewList.add(myViewHolder.card_premise);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Set white background for all card not been selected
                for (CardView cardView:cardViewList)
                    cardView.setBackgroundColor(context.getResources().getColor(android.R.color.white));

                //Set selected BG for only selected item
                myViewHolder.card_premise.setBackgroundColor(context.getResources()
                        .getColor(R.color.colorPrimary));

                //Send Broadcast to tell BookingActivity to enable Button NEXT
                // Part 27: Removed
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_PREMISE_SAVE, premiseList.get(pos));
//                intent.putExtra(Common.KEY_STEP, 1);
                // IMPORTANT! This tell the ServiceFragment that this premise is selected!
                Common.premise = premiseList.get(pos).getPremiseId();

                localBroadcastManager.sendBroadcast(intent);

                // Part 27:
//                EventBus.getDefault().postSticky(new EnableNextButton(1, saloonList.get(i)));

            }
        });

    }

    @Override
    public int getItemCount() {
        return premiseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_premise_name, txt_premise_description;
        CardView card_premise;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_premise_name = (TextView)itemView.findViewById(R.id.txt_premise_name);
            txt_premise_description = (TextView) itemView.findViewById(R.id.txt_premise_address);

            card_premise = (CardView)itemView.findViewById(R.id.card_premise);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view, getAdapterPosition());
        }
    }

}
