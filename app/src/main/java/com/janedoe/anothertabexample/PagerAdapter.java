package com.janedoe.anothertabexample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.janedoe.anothertabexample.fragments.TabFragment1;
import com.janedoe.anothertabexample.fragments.TabFragment2;
import com.janedoe.anothertabexample.fragments.TabFragment3;

/**
 * Created by janedoe on 12/5/2015.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int tabs;

    public PagerAdapter(FragmentManager fm, int tabs){
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                TabFragment1 tab1 = new TabFragment1();
                return tab1;
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                return tab2;
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                return  tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabs;
    }
}
