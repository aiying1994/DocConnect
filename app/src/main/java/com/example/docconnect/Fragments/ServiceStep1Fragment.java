package com.example.docconnect.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.docconnect.Adapter.MyPremiseAdapter;
import com.example.docconnect.Common.Common;
import com.example.docconnect.Common.SpacesItemDecoration;
import com.example.docconnect.Interface.IAllPremisesLoadListener;
import com.example.docconnect.Interface.IAllServicesLoadListener;
import com.example.docconnect.Model.Premise;
import com.example.docconnect.Model.Service;
import com.example.docconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class ServiceStep1Fragment extends Fragment implements IAllPremisesLoadListener, IAllServicesLoadListener {

    static ServiceStep1Fragment instance;
    public static ServiceStep1Fragment getInstance(){
        if (instance == null)
            instance = new ServiceStep1Fragment();
        return instance;
    }

    // Variable
    CollectionReference premisesRef;
    CollectionReference allServicesRef;

    IAllPremisesLoadListener iAllPremisesLoadListener;
    IAllServicesLoadListener iAllServicesLoadListener;

    Unbinder unbinder;

    AlertDialog dialog; // This shows the loading ...

    LayoutInflater inflater;

    @BindView(R.id.spinner_service)
    MaterialSpinner spinner_service;
    @BindView(R.id.recycler_premise)
    RecyclerView recycler_premise;
    @BindView(R.id.chip_group_all_services)
    ChipGroup chip_group_all_services;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        allPremisesRef = FirebaseFirestore.getInstance().collection("AllPremises");
        allServicesRef = FirebaseFirestore.getInstance()
                .collection("AllServices");
        iAllServicesLoadListener = this;
        iAllPremisesLoadListener = this;
        inflater = LayoutInflater.from(getContext());
        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(getActivity()).build();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_service_step1,container,false);
        unbinder = ButterKnife.bind(this,itemView);

        initView(); 
        
        loadAllServices();
//        loadAllPremises();

        return itemView;
    }

    private void initView() {
        //Initialize the chipgroup view
        chip_group_all_services.removeAllViews();

        recycler_premise.setHasFixedSize(true);
        recycler_premise.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_premise.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void loadAllServices() {
        allServicesRef.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        List<String> list = new ArrayList<>();
                        list.add("Choose a service");
                        for (QueryDocumentSnapshot documentSnapshot:task.getResult())
                            list.add(documentSnapshot.getId());
                        iAllServicesLoadListener.onAllServicesLoadSuccess(list);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    iAllServicesLoadListener.onAllServicesLoadFailed(e.getMessage());
                }
            });
    }

//    private void loadAllPremises()
//    {
//        allPremisesRef.get()
//            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if(task.isSuccessful()){
//                        List<String> list = new ArrayList<>();
//                        list.add("Choose premise");
//                        for (QueryDocumentSnapshot documentSnapshot:task.getResult())
//                            list.add(documentSnapshot.getId());
//                        iAllPremisesLoadListener.onAllPremisesLoadSuccess(list);
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    iAllPremisesLoadListener.onAllPremisesLoadFailed(e.getMessage());
//                }
//            });
//
//    }



    @Override
    public void onAllServicesLoadSuccess(List<String> serviceIdList) {
        //Load all the item in serviceList
        spinner_service.setItems(serviceIdList);

        spinner_service.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position > 0){
                    loadPremiseBaseOnSelectedService(item.toString());
                }
                else // If "Please Choose City" is selected, all saloon gone
                    recycler_premise.setVisibility(View.GONE);
            }
        });


        int pos=0;
        // Pos = 0 is "Choose a service

        for(pos=1; pos<serviceIdList.size(); pos++) {
//            tv_services.setText(serviceIdList.get(pos).getServiceId(""));

            final Chip item = (Chip) inflater.inflate(R.layout.chip_item, null);
            item.setText(serviceIdList.get(pos));
            item.setTag(pos);
//            item.setClickable(true);
            item.setCloseIconVisible(false);
            item.setCheckable(true);
            item.setSingleLine(true);

            item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(item.isChecked()){
                        Toast.makeText(getContext(), "Checked "+serviceIdList, Toast.LENGTH_SHORT).show();
                        item.setChipBackgroundColorResource(R.color.colorPrimary);
                        item.setTextColor(getActivity().getResources().getColor(android.R.color.white));
                    }
                    else {
                        item.setChipBackgroundColorResource(R.color.divider);
                        item.setTextColor(getActivity().getResources().getColor(R.color.colorTextHeavy));
                    }
                }
            });


            chip_group_all_services.addView(item);
        }
        // TODO: Implement chipgroup check id.
    }

    private void loadPremiseBaseOnSelectedService(String serviceId) {
        dialog.show();

        Common.service = serviceId; //Selected serviceId

        premisesRef = FirebaseFirestore.getInstance()
                .collection("AllServices")
                .document(serviceId)
                .collection("AllPremises");
        premisesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Premise> list = new ArrayList<>();
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                    {
                        Premise premise = documentSnapshot.toObject(Premise.class);
                        premise.setPremiseId(documentSnapshot.getId());
                        list.add(premise);
                    }

                    iAllPremisesLoadListener.onAllPremisesLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllPremisesLoadListener.onAllPremisesLoadFailed(e.getMessage());
            }
        });
    }


    @Override
    public void onAllServicesLoadFailed(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAllServicesOfSelectedPremiseLoadSuccess(List<Service> serviceIdList) {

    }

    @Override
    public void onAllServicesOfSelectedPremiseLoadFailed(String message) {

    }

    @Override
    public void onAllPremisesLoadSuccess(List<Premise> premiseList) {
        MyPremiseAdapter adapter = new MyPremiseAdapter(getActivity(),premiseList);
        recycler_premise.setAdapter(adapter);
        recycler_premise.setVisibility(View.VISIBLE);
        dialog.dismiss();

    }

    @Override
    public void onAllPremisesLoadFailed(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSelectedPremisesLoadSuccess(List<String> premiseIdList) {

    }

    @Override
    public void onSelectedPremisesLoadFailed(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
}
