package com.example.docconnect.Interface;

import com.example.docconnect.Model.Service;

import java.util.List;

public interface IAllServicesLoadListener {

    void onAllServicesLoadSuccess(List<String> serviceList);
    void onAllServicesLoadFailed(String message);

    // Used in Step2
    void onAllServicesOfSelectedPremiseLoadSuccess(List<Service> serviceIdList);
    void onAllServicesOfSelectedPremiseLoadFailed(String message);

}
