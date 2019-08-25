package com.example.docconnect.Interface;

import com.example.docconnect.Model.Premise;

import java.util.List;

public interface IAllPremisesInfoLoadListener {
    void onAllPremisesInfoLoadSuccess(Premise premiseInfo);
    void onAllPremisesInfoLoadFailed(String message);
}
