package com.example.docconnect.Interface;

import com.example.docconnect.Model.Premise;

import java.util.List;

public interface IAllPremisesLoadListener {
    void onAllPremisesLoadSuccess(List<Premise> premiseList);
    void onAllPremisesLoadFailed(String message);

    void onSelectedPremisesLoadSuccess(List<String> premiseIdList);
    void onSelectedPremisesLoadFailed(String message);

}
