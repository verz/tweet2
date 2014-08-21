package com.example.myTestApp;

import android.support.v4.app.FragmentActivity;
import com.viewpagerindicator.PageIndicator;

import java.util.Random;


/**
 * Created by verzatran on 15.08.14.
 */
public abstract class BaseSampleActivity extends FragmentActivity {
    private static final Random RANDOM = new Random();

    MyFragmentAdapter mAdapter;
    android.support.v4.view.ViewPager mPager;
    PageIndicator mIndicator;
}
