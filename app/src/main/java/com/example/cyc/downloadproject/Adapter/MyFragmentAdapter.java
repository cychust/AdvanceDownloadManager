package com.example.cyc.downloadproject.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by cyc on 17-10-2.
 */

public class MyFragmentAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private List<Fragment>fragments;
    private List<String> titles;
    private int PAG_COUNT=2;
    private Fragment currentFragment;
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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentFragment=(Fragment)object;
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getCurrentFragment(){
        return currentFragment;
    }
}
