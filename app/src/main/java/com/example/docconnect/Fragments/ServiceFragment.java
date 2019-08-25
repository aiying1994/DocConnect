package com.example.docconnect.Fragments;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.docconnect.Adapter.MyViewPagerAdapter;
import com.example.docconnect.Common.Common;
import com.example.docconnect.Common.NonSwipeViewPager;
import com.example.docconnect.Interface.IAllPremisesLoadListener;
import com.example.docconnect.Model.Premise;
import com.example.docconnect.Model.Service;
import com.example.docconnect.Model.User;
import com.example.docconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.common.eventbus.EventBus;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends Fragment implements IAllPremisesLoadListener {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;
    AlertDialog dialog;
    CollectionReference premiseServiceRef, allPremisesRef;
    DocumentReference premiseInfoRef;

    IAllPremisesLoadListener iAllPremisesLoadListener;

    @BindView(R.id.step_view)
    StepView stepView;
    @BindView(R.id.view_pager)
    NonSwipeViewPager viewPager;
    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;
    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    // Event
    @OnClick(R.id.btn_previous_step)
    void previousClick(){
//        Toast.makeText(getActivity(), ""+Common.currentPremise.getPremiseId(), Toast.LENGTH_SHORT).show();
        if (Common.step == 3 || Common.step > 0){
            Common.step--;
            viewPager.setCurrentItem(Common.step);
//            Toast.makeText(getActivity(), ""+Common.step, Toast.LENGTH_SHORT).show();

            if (Common.step < 3) { // Always enable NEXT when step <3
                btn_next_step.setEnabled(true);
                setButtonColor();
            }
        }
    }

    @OnClick(R.id.btn_next_step)
    void nextClick(){
//        Toast.makeText(getActivity(), ""+Common.currentPremise.getPremiseId(), Toast.LENGTH_SHORT).show();
        if(Common.step < 3 || Common.step == 0){

            Common.step++; //Increment
            if(Common.step == 1){ // After premise is selected
                if(Common.currentPremise != null)
                    loadSelectedPremiseServices(Common.currentPremise.getPremiseId());
                    loadSelectedPremiseInfo(Common.currentPremise.getPremiseId());

            }
            else if(Common.step == 2) { // Pick time slot
//                if(Common.currentPremise != null)
//                    loadTimeSlotOfBarber(Common.currentPremise.getPremiseId());

            }
            else if(Common.step == 3) { // Confirm
//                if(Common.currentPremise != -1)
//                    confirmBooking();
            }
            viewPager.setCurrentItem(Common.step);
        }
    }

    //This will be called in Step2
    private void loadSelectedPremiseServices(String premiseId) {
        dialog.show();

        // If the a premise is selected in step1, load all the info of that particular premise.
        if(!TextUtils.isEmpty(Common.premise)){

            premiseServiceRef = FirebaseFirestore.getInstance()
                    .collection("AllPremises")
                    .document(premiseId)
                    .collection("AllServices");

            // Get all the services available in premiseId
            premiseServiceRef
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<Service> services = new ArrayList<>();
                            for(QueryDocumentSnapshot serviceSnapShot:task.getResult()) {
                                Service service = serviceSnapShot.toObject(Service.class);
                                service.setServiceId(serviceSnapShot.getId()); // Get serviceId
                                services.add(service);
                            }

                            // Send Broadcast to Bookingstep2Fragment to load Recycler
                            Intent intent = new Intent(Common.KEY_SERVICE_LOAD_DONE);
                            intent.putParcelableArrayListExtra(Common.KEY_SERVICE_LOAD_DONE, (ArrayList<? extends Parcelable>) services);
                            localBroadcastManager.sendBroadcast(intent);
                            dialog.dismiss();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                        }
                    });
        }
    }

    private void loadSelectedPremiseInfo(String premiseId) {
        dialog.show();

        // If the a premise is selected in step1, load all the info of that particular premise.
        if(!TextUtils.isEmpty(Common.premise)){
            premiseInfoRef = FirebaseFirestore.getInstance()
                    .collection("AllPremises")
                    .document(premiseId);

            // Get all the services available in premiseId
            premiseInfoRef
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task)
                        {
                            DocumentSnapshot infoSnapshot = task.getResult();
                            Premise premiseInfo = infoSnapshot.toObject(Premise.class);
                            premiseInfo.setName(infoSnapshot.getString("name"));
                            premiseInfo.setDescription(infoSnapshot.getString("description"));
                            premiseInfo.setLocation(infoSnapshot.getString("location"));
                            premiseInfo.setRating(infoSnapshot.getLong("rating"));
                            premiseInfo.setRatingTimes(infoSnapshot.getLong("ratingTimes"));
                            Intent intent = new Intent(Common.KEY_INFO_LOAD_DONE);
                            intent.putExtra(Common.KEY_INFO_LOAD_DONE, premiseInfo);
                            localBroadcastManager.sendBroadcast(intent);

                            dialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Info load failed", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

        }
    }

    //Broadcast Receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Common.currentPremise = intent.getParcelableExtra(Common.KEY_PREMISE_SAVE);
            btn_next_step.setEnabled(true);
            setButtonColor();
        }
    };

    public ServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = (LocalBroadcastManager) LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver((buttonNextReceiver), new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));
        allPremisesRef = FirebaseFirestore.getInstance().collection("AllPremises");
        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(getActivity()).build();
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View itemView = inflater.inflate(R.layout.fragment_service, container, false);
        unbinder =  ButterKnife.bind(this, itemView);

        setupStepView();
//        loadAllPremises();
        setButtonColor();

        //View
        viewPager.setAdapter(new MyViewPagerAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(4); // We have 4 fragment so we need to keep the state these pages
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                // Show step at top
                stepView.go(i, true);

                if(i==0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

                //Disable button next here
                btn_next_step.setEnabled(false);

                setButtonColor();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        return itemView;
    }

    private void loadAllPremises() {
        allPremisesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<String> allPremiseList = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot:task.getResult())
                                allPremiseList.add(documentSnapshot.getId());
                            iAllPremisesLoadListener.onSelectedPremisesLoadSuccess(allPremiseList);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllPremisesLoadListener.onAllPremisesLoadFailed(e.getMessage());
            }
        });
    }

    private void setButtonColor() {
        if (btn_next_step.isEnabled())
        {
            btn_next_step.setBackgroundResource(R.color.colorPrimary);
        }
        else
         {
            btn_next_step.setBackgroundResource(android.R.color.darker_gray);
         }
        if (btn_previous_step.isEnabled())
        {
            btn_previous_step.setBackgroundResource(R.color.colorPrimary);
        }
        else
        {
            btn_previous_step.setBackgroundResource(android.R.color.darker_gray);
        }
    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Search"); // Pick a service / premise
        stepList.add("Profile"); // Select more service
        stepList.add("Appointment"); // Choose ur time
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }

    @Override
    public void onAllPremisesLoadSuccess(List<Premise> premiseList) {

    }

    @Override
    public void onAllPremisesLoadFailed(String message) {

    }

    @Override
    public void onSelectedPremisesLoadSuccess(List<String> premiseIdList) {
        // Maks sure we have similar list
//        for (int pos = 0; pos <= premiseIdList.size()-1; pos++) {
//            if (Common.premise == premiseIdList.get(pos)) {
//                Toast.makeText(getActivity(), ""+Common.premise +" xx "+premiseIdList.get(pos).toString(), Toast.LENGTH_SHORT).show();;
//                break;
//            }
//            else Toast.makeText(getActivity(), "No thing similar", Toast.LENGTH_SHORT).show();;
//        }
        Toast.makeText(getActivity(), ""+premiseIdList.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSelectedPremisesLoadFailed(String message) {

    }
}
