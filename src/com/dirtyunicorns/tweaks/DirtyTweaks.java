/*
 * Copyright (C) 2017-2020 The Dirty Unicorns Project
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

package com.dirtyunicorns.tweaks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.dirtyunicorns.tweaks.fragments.team.TeamActivity;
import com.dirtyunicorns.tweaks.navigation.BottomNavigationViewCustom;
import com.dirtyunicorns.tweaks.tabs.Multitasking;
import com.dirtyunicorns.tweaks.tabs.Navigation;
import com.dirtyunicorns.tweaks.tabs.Statusbar;
import com.dirtyunicorns.tweaks.tabs.System;

public class DirtyTweaks extends SettingsPreferenceFragment {

    private Context mContext;
    private MenuItem mMenuItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getActivity();

        getActivity().setTitle(R.string.dirtytweaks_title);

        View view = inflater.inflate(R.layout.dirtytweaks, container, false);

        final BottomNavigationViewCustom navigation = view.findViewById(R.id.navigation);

        final ViewPager viewPager = view.findViewById(R.id.viewpager);
        PagerAdapter mPagerAdapter = new PagerAdapter(getFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationViewCustom.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.system:
                                viewPager.setCurrentItem(0);
                                return true;
                            case R.id.statusbar:
                                viewPager.setCurrentItem(1);
                                return true;
                            case R.id.navigation:
                                viewPager.setCurrentItem(2);
                                return true;
                            case R.id.multitasking:
                                if (mContext.getResources().getBoolean(R.bool.has_active_edge)) {
                                    viewPager.setCurrentItem(3);
                                }
                                return true;
                        }
                        return false;
                    }
                });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mMenuItem != null) {
                    mMenuItem.setChecked(false);
                }

                navigation.getMenu().getItem(position).setChecked(true);

                mMenuItem = navigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setHasOptionsMenu(true);

        if (!mContext.getResources().getBoolean(R.bool.has_active_edge)) {
            navigation.getMenu().removeItem(R.id.multitasking);
        }

        return view;
    }

    class PagerAdapter extends FragmentPagerAdapter {

        String titles[] = getTitles();
        private Fragment frags[] = new Fragment[titles.length];

        PagerAdapter(FragmentManager fm) {
            super(fm);
            frags[0] = new System();
            frags[1] = new Statusbar();
            frags[2] = new Navigation();
            if (mContext.getResources().getBoolean(R.bool.has_active_edge)) {
                frags[3] = new Multitasking();
            }
        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    private String[] getTitles() {
        String titleString[];

        if (mContext.getResources().getBoolean(R.bool.has_active_edge)) {
            titleString = new String[]{
                    mContext.getString(R.string.bottom_nav_system_title),
                    mContext.getString(R.string.bottom_nav_statusbar_title),
                    mContext.getString(R.string.bottom_nav_navigation_title),
                    mContext.getString(R.string.bottom_nav_multitasking_title)};
        } else {
            titleString = new String[]{
                    mContext.getString(R.string.bottom_nav_system_title),
                    mContext.getString(R.string.bottom_nav_statusbar_title),
                    mContext.getString(R.string.bottom_nav_navigation_title)};
        }

        return titleString;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DIRTYTWEAKS;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, R.string.dialog_team_title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(mContext, TeamActivity.class);
                mContext.startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}
