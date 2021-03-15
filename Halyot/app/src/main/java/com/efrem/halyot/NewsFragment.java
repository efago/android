package com.efrem.halyot;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


public class NewsFragment extends Fragment {

    public NewsFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_news, container, false);
        ViewPager viewPager= view.findViewById(R.id.viewPager);
        NewsViewPager messagingViewPager= new NewsViewPager(getChildFragmentManager());
        viewPager.setAdapter( messagingViewPager);
        TabLayout tabLayout= view.findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

}
class NewsViewPager extends FragmentPagerAdapter {

    public NewsViewPager(@NonNull FragmentManager fm) {
        super(fm);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ForYouFragment();
            case 1:
                return new TopNewsFragment();
            case 2:
                return new FunFragment();
            case 3:
                return new SportsFragment();
            case 4:
                return new SportsFragment();
            case 5:
                return new SportsFragment();
            case 6:
                return new SportsFragment();
            case 7:
                return new SportsFragment();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 8;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "For you";
            case 1:
                return "Top news";
            case 2:
                return "Fun";
            case 3:
                return "Sports";
            case 4:
                return "Business";
            case 5:
                return "Tech";
            case 6:
                return "Health";
            case 7:
                return "Science";
            default:
                return null;
        }
    }
}
