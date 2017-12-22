package com.sxm.example.smoothtransindicator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewpager;
    private ViewPagerAdapter adapter;
    private SmoothTransIndicator mIndicatorCircleLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("blue");
        strings.add("red");
        strings.add("yellow");
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i=0;i<strings.size();i++){
            Fragment fragment = new ViewPagerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("key",strings.get(i));
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }

        ViewPagerAdapter adapter =  new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        mViewpager.setAdapter(adapter);

        mIndicatorCircleLine = (SmoothTransIndicator) findViewById(R.id.indicator_circle_line);

        mIndicatorCircleLine.setViewPager(mViewpager);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private FragmentManager mSupportFragmentManager;
        private ArrayList<Fragment> mFragments;


        public ViewPagerAdapter(FragmentManager supportFragmentManager, ArrayList<Fragment> fragments) {
            super(supportFragmentManager);
            mSupportFragmentManager = supportFragmentManager;
            mFragments = fragments;

        }

        @Override
        public Fragment getItem(int position) {
            return   mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
