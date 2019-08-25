package com.example.docconnect.Interface;


import com.example.docconnect.Model.SmallBanner;

import java.util.List;

public interface ISmallBannerLoadListener {
    void onPremisesLoadSuccess(List<SmallBanner> premises);
    void onPremisesLoadFailed(String message);
}
