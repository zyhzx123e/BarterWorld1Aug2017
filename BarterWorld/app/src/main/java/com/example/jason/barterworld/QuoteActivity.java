package com.example.jason.barterworld;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jason.barterworld.quotes.IncomingQuoteFragment;
import com.example.jason.barterworld.quotes.OutgoingQuoteFragment;


import java.util.ArrayList;
import java.util.List;

public class QuoteActivity extends AppCompatActivity {

    OutgoingQuoteFragment tab0;
    IncomingQuoteFragment tab1;
    ViewPager viewPager;
    TabLayout tabLayout;

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager manager) {super(manager);}

        @Override public int getCount() {return 2;}

        @Override
        public Fragment getItem(int position) {
            if (position == 0) return new OutgoingQuoteFragment();
            if (position == 1) return new IncomingQuoteFragment();
            return null;  // or throw some exception
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "My Interested Barter";
            if (position == 1) return "Quotes to Me";
            return null;  // or throw some exception
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_backbtn);

        viewPager = (ViewPager) findViewById(R.id.pager);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        adapter.startUpdate(viewPager);
        tab0 = (OutgoingQuoteFragment) adapter.instantiateItem(viewPager, 0);
        tab1 = (IncomingQuoteFragment) adapter.instantiateItem(viewPager, 1);
        adapter.finishUpdate(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



        //----------------------

      /*  ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OutgoingQuoteFragment(), "My Interested Barter");
        adapter.addFragment(new IncomingQuoteFragment(), "Quotes to Me");

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);*/


    }



    // Adapter for the viewpager using FragmentPagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
