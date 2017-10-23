package com.michaelvelez.travelcol;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.michaelvelez.travelcol.fragments.NotificationFragment;
import com.michaelvelez.travelcol.fragments.ToursFragment;
import com.michaelvelez.travelcol.fragments.ValorarFragment;

import static com.michaelvelez.travelcol.fragments.TabFragment.int_items;

/**
 * Created by Admin on 3/1/2017.
 */

public class MyAdapter  extends FragmentPagerAdapter {


    public MyAdapter(FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new NotificationFragment();
            case 1:
                return new ToursFragment();



        }
        return null;
    }

    @Override
    public int getCount() {


        return int_items;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "About";
            case 1:
                return "Tours";



        }

        return null;
    }
}
