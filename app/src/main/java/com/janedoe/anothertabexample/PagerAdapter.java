package com.janedoe.anothertabexample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.janedoe.anothertabexample.Fragments.PreviousWalksTab;
import com.janedoe.anothertabexample.Fragments.GoogleMapsFragment;
import com.janedoe.anothertabexample.Fragments.TabFragment3;

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
                PreviousWalksTab previousWalksTab = new PreviousWalksTab();
                return previousWalksTab;
            case 1:
                GoogleMapsFragment mapTab = new GoogleMapsFragment();
                return mapTab;
            case 2:
                TabFragment3 statisticsTab = new TabFragment3();
                return  statisticsTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabs;
    }
}
