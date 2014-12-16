/*
* Copyright (C) 2014 Dirty Unicorns
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

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