package com.example.docconnect.Adapter;

import com.example.docconnect.Fragments.ServiceFragment;
import com.example.docconnect.Fragments.ServiceStep1Fragment;
import com.example.docconnect.Fragments.ServiceStep2Fragment;
import com.example.docconnect.Fragments.ServiceStep3Fragment;
import com.example.docconnect.Fragments.ServiceStep4Fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return ServiceStep1Fragment.getInstance();
            case 1:
                return ServiceStep2Fragment.getInstance();
            case 2:
                return ServiceStep3Fragment.getInstance();
            case 3:
                return ServiceStep4Fragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
