package com.example.cyc.downloadproject;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.example.cyc.downloadproject.Adapter.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expandableListView;
    private List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private TabLayout mTabLayout;
    private ViewPager viewPager;
    private ActionBarDrawerToggle mDrawerToggle;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initView();
        initViewPager();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);

   /*     expandableListView = (ExpandableListView) findViewById(R.id.expandableView);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(listAdapter);*/


        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.app_name);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                final AlertDialog alertDialog = builder.create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                alertDialog.show();
                View view1 = getLayoutInflater().inflate(R.layout.dialog, null);
                Window window = alertDialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable());
                WindowManager.LayoutParams lm = window.getAttributes();
                lm.gravity = Gravity.CENTER;
                int width = getWindowManager().getDefaultDisplay().getWidth();
                lm.width = width;
                lm.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lm);

                window.setContentView(view1);
                final EditText editText = (EditText) window.findViewById(R.id.url_edit);
                editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            alertDialog.getWindow()
                                    .clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                        }
                    }
                });
                final EditText nameEdit = (EditText) window.findViewById(R.id.name_edit);
                nameEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
                final Button cancelButton = (Button) window.findViewById(R.id.cancel);
                final Button categoryAdd = (Button) window.findViewById(R.id.category_add);
                break;
        }
    }


    private void initViewPager() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        final List<String> titles = new ArrayList<String>();
        titles.add("任务");
        titles.add("已下载");


        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        final List<Fragment> fragments = new ArrayList<Fragment>();
        for (int i = 0; i < titles.size(); i++) {
            fragments.add(PagerFragment.newInstance(titles.get(i)));
        }
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
        };
        //给ViewPager设置适配器
        viewPager.setAdapter(fragmentPagerAdapter);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(viewPager);
        //给TabLayout设置适配器
        mTabLayout.setTabsFromPagerAdapter(fragmentPagerAdapter);
    }

}
