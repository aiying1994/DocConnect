package com.example.docconnect.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docconnect.Common.Common;
import com.example.docconnect.Common.SpacesItemDecoration;
import com.example.docconnect.Interface.IAllPremisesInfoLoadListener;
import com.example.docconnect.Interface.IAllServicesLoadListener;
import com.example.docconnect.Model.Premise;
import com.example.docconnect.Model.Service;
import com.example.docconnect.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ServiceStep2Fragment extends Fragment implements IAllServicesLoadListener, IAllPremisesInfoLoadListener {

    static ServiceStep2Fragment instance;
    public static ServiceStep2Fragment getInstance(){
        if (instance == null)
            instance = new ServiceStep2Fragment();
        return instance;
    }

    Unbinder unbinder;

    LocalBroadcastManager localBroadcastManager;
    IAllServicesLoadListener iAllServicesLoadListener;
    IAllPremisesInfoLoadListener iAllPremisesInfoLoadListener;

    LayoutInflater inflater;

    @BindView(R.id.recycler_review)
    RecyclerView recycler_review;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_description)
    TextView tv_description;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_rating)
    TextView tv_rating;
    @BindView(R.id.tv_ratingTimes)
    TextView tv_ratingTimes;
    @BindView(R.id.chip_group_services)
    ChipGroup chip_group_services;
    @BindView(R.id.rating_premise)
    RatingBar rating_premise;



    // Part 27:
    private BroadcastReceiver serviceLoadDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Common.KEY_SERVICE_LOAD_DONE.equals(intent.getAction()))
            {
                ArrayList<Service> serviceArrayList = intent.getParcelableArrayListExtra(Common.KEY_SERVICE_LOAD_DONE);
//                Toast.makeText(context, ""+serviceArrayList.get(0).getServiceId(""), Toast.LENGTH_SHORT).show();

                iAllServicesLoadListener.onAllServicesOfSelectedPremiseLoadSuccess(serviceArrayList);
            }
            else if (Common.KEY_INFO_LOAD_DONE.equals(intent.getAction()))
            {
                Premise permiseInfo = intent.getParcelableExtra(Common.KEY_INFO_LOAD_DONE);
//                Toast.makeText(context, ""+permiseInfo.getName(), Toast.LENGTH_SHORT).show();

                iAllPremisesInfoLoadListener.onAllPremisesInfoLoadSuccess((Premise) permiseInfo);
            }

            // Create review adapter here
//            MyBarberAdapter adapter = new MyBarberAdapter(getContext(),barberArrayList);
//            recycler_review.setAdapter(adapter);
//            tv_title.setText(permiseInfo.getName());



        }
    };

    @Override
    public void onDestroy() {
    localBroadcastManager.unregisterReceiver(serviceLoadDoneReceiver);
       super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(serviceLoadDoneReceiver, new IntentFilter(Common.KEY_SERVICE_LOAD_DONE));
        localBroadcastManager.registerReceiver(serviceLoadDoneReceiver, new IntentFilter(Common.KEY_INFO_LOAD_DONE));

        iAllServicesLoadListener = this;
        iAllPremisesInfoLoadListener = this;
        inflater = LayoutInflater.from(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_service_step2,container,false);
        unbinder = ButterKnife.bind(this, itemView);
        initView();
        return itemView;
    }


    private void initView()
    {

        recycler_review.setHasFixedSize(true);
        recycler_review.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_review.addItemDecoration(new SpacesItemDecoration(4));

    }


    @Override
    public void onAllServicesLoadSuccess(List<String> serviceList) {

    }

    @Override
    public void onAllServicesLoadFailed(String message) {

    }

    @Override
    public void onAllServicesOfSelectedPremiseLoadSuccess(List<Service> serviceIdList) {
        int pos=0;
        //Initialize the chipgroup view
        chip_group_services.removeAllViews();
        for(pos=0; pos<serviceIdList.size(); pos++) {
//            tv_services.setText(serviceIdList.get(pos).getServiceId(""));

            final Chip item = (Chip) inflater.inflate(R.layout.chip_item, null);
            item.setText(serviceIdList.get(pos).getServiceId(""));
            item.setTag(pos);
            item.setClickable(false);
            item.setCloseIconVisible(false);
            item.setCheckable(false);

            if (Common.service.equals(serviceIdList.get(pos).getServiceId("")))
            {
                item.setChipBackgroundColorResource(R.color.colorPrimary);
                item.setTextColor(getActivity().getResources().getColor(android.R.color.white));
            }
            else {
                item.setChipBackgroundColorResource(R.color.colorDefault);
                item.setTextColor(getActivity().getResources().getColor(R.color.colorTextHeavy));
            }

            chip_group_services.addView(item);
        }
    }

    @Override
    public void onAllServicesOfSelectedPremiseLoadFailed(String message) {

    }

    @Override
    public void onAllPremisesInfoLoadSuccess(Premise premiseInfo) {
        tv_title.setText(premiseInfo.getName());
        tv_address.setText(premiseInfo.getLocation());
        tv_description.setText(premiseInfo.getDescription());
        tv_rating.setText(premiseInfo.getRating().toString());
        tv_ratingTimes.setText("("+premiseInfo.getRatingTimes().toString()+")");
        rating_premise.setRating((float)premiseInfo.getRating() / premiseInfo.getRatingTimes() );

        Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
        localBroadcastManager.sendBroadcast(intent);

    }

    @Override
    public void onAllPremisesInfoLoadFailed(String message) {

    }
}
