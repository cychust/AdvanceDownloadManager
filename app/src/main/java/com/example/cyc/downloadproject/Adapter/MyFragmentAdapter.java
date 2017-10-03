package com.example.cyc.downloadproject.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by cyc on 17-10-2.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Fragment>fragments;
    private List<String> titles;
    private int PAG_COUNT=2;
    public MyFragmentAdapter(List<Fragment> fragments, List<String>titles, FragmentManager fm, Context context){
        super(fm);
        this.context=context;
        this.fragments=fragments;
        this.titles=titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
