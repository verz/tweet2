package com.example.myTestApp;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.viewpagerindicator.TitlePageIndicator;

/**
 * Created by verzatran on 15.08.14.
 */
public class myViewPager extends BaseSampleActivity {
    public static myViewPager pagerActivity;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_titles);
        pagerActivity = this;
        if (Auth.Auth != null)
            Auth.Auth.finish();

        mAdapter = new MyFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mIndicator = (TitlePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }

}
