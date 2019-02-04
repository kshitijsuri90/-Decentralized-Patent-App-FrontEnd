package com.example.kshitij.patentlite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabtitles[]= new String[]{"Applications","Status","New Patent"};
    private int role = 1;

    public SimpleFragmentPagerAdapter(FragmentManager fm) {
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
        } else if (position == 1){
            StatusFragment fragment =  new StatusFragment();
            fragment.setArguments(bundle);
            return fragment;
        } else {
            NewFormFragment fragment =  new NewFormFragment();
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
