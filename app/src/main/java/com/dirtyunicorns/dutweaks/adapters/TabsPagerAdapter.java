package com.dirtyunicorns.dutweaks.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dirtyunicorns.dutweaks.fragments.MiscFragment;
import com.dirtyunicorns.dutweaks.fragments.NavigationFragment;
import com.dirtyunicorns.dutweaks.fragments.StatusBarFragment;
import com.dirtyunicorns.dutweaks.fragments.TaskingFragment;
import com.dirtyunicorns.dutweaks.fragments.UIFragment;
import com.dirtyunicorns.dutweaks.fragments.WelcomeFragment;

/**
 * Created by mazwoz on 12/16/14.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new WelcomeFragment();
            case 1:
                return new UIFragment();
            case 2:
                return new StatusBarFragment();
            case 3:
                return new TaskingFragment();
            case 4:
                return new NavigationFragment();
            case 5:
                return new MiscFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 6;
    }
}