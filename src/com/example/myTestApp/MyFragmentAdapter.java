package com.example.myTestApp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by verzatran on 15.08.14.
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {
    protected static final String[] CONTENT = new String[] { "List","Tweeet",  };
    private int mCount = 2;

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        return TestFragment.newInstance(CONTENT[position % mCount]);
    }

    @Override
    public int getCount() {
        return mCount;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return MyFragmentAdapter.CONTENT[position % CONTENT.length];
    }
    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}
