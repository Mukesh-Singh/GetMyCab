package gmc.com.getmycab.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Baba on 9/5/2015.
 */
public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    Fragment [] fragments;
    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public FragmentPagerAdapter (FragmentManager fm , Fragment [] fragments){
        this(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
