package com.example.kshitij.patentlite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private String tabtitles[]= new String[]{"Applications","Status"};
    private int role = 1;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle=new Bundle();
        bundle.putInt("role", role);
        if (position == 0) {
            ApplicationFragment fragment =  new ApplicationFragment();
            fragment.setArguments(bundle);
            return fragment;
        } else{
            StatusFragment fragment =  new StatusFragment();
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
