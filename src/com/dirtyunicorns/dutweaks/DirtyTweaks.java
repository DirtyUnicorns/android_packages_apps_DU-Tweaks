/*
 * Copyright (C) 2014 The Dirty Unicorns project
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

package com.dirtyunicorns.dutweaks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.provider.Settings;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dirtyunicorns.dutweaks.tabs.GeneralUI;
import com.dirtyunicorns.dutweaks.tabs.StatusBar;
import com.dirtyunicorns.dutweaks.tabs.Lockscreen;
import com.dirtyunicorns.dutweaks.tabs.Navigation;
import com.dirtyunicorns.dutweaks.tabs.MultiTasking;
import com.dirtyunicorns.dutweaks.tabs.System;
import com.dirtyunicorns.dutweaks.tabs.Misc;
import com.dirtyunicorns.dutweaks.PagerSlidingTabStrip;
import com.android.internal.util.slim.DeviceUtils;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import java.util.ArrayList;
import java.util.List;

public class DirtyTweaks extends SettingsPreferenceFragment {

    private static final int MENU_HELP  = 0;

    ViewPager mViewPager;
    String titleString[];
    ViewGroup mContainer;
    PagerSlidingTabStrip mTabs;

    static Bundle mSavedState;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContainer = container;

        View view = inflater.inflate(R.layout.preference_generalui, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        StatusBarAdapter StatusBarAdapter = new StatusBarAdapter(getFragmentManager());
        mViewPager.setAdapter(StatusBarAdapter);
        mTabs.setViewPager(mViewPager);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!DeviceUtils.isTablet(getActivity())) {
            mContainer.setPadding(30, 30, 30, 30);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_HELP, 0, R.string.dirtytweaks_dialog_title)
                .setIcon(R.drawable.ic_dirtytweaks_info)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_HELP:
                showDialogInner(MENU_HELP);
                Toast.makeText(getActivity(),
                (R.string.dirtytweaks_dialog_toast),
                Toast.LENGTH_LONG).show();
                return true;
            default:
                return false;
        }
    }

    private void showDialogInner(int id) {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(id);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "dialog " + id);
    }

    public static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(int id) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("id", id);
            frag.setArguments(args);
            return frag;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int id = getArguments().getInt("id");
            switch (id) {
                case MENU_HELP:
                    return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.dirtytweaks_dialog_title)
                    .setMessage(R.string.dirtytweaks_dialog_message)
                    .setCancelable(false)
                    .setNegativeButton(R.string.dlg_ok,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create();
            }
            throw new IllegalArgumentException("unknown id " + id);
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    class StatusBarAdapter extends FragmentPagerAdapter {
        String titles[] = getTitles();
        private Fragment frags[] = new Fragment[titles.length];

        public StatusBarAdapter(FragmentManager fm) {
            super(fm);
            frags[0] = new GeneralUI();
            frags[1] = new StatusBar();
            frags[2] = new Lockscreen();
            frags[3] = new Navigation();
            frags[4] = new MultiTasking();
            frags[5] = new System();
            frags[6] = new Misc();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }
    }

    private String[] getTitles() {
        String titleString[];
        if (!DeviceUtils.isPhone(getActivity())) {
        titleString = new String[]{
                    getString(R.string.generalui_category),
                    getString(R.string.statusbar_category),
                    getString(R.string.lockscreen_category),
                    getString(R.string.navigation_category),
                    getString(R.string.multitasking_category),
                    getString(R.string.system_category),
	            getString(R.string.misc_category)};
        } else {
        titleString = new String[]{
                    getString(R.string.generalui_category),
                    getString(R.string.statusbar_category),
                    getString(R.string.lockscreen_category),
                    getString(R.string.navigation_category),
                    getString(R.string.multitasking_category),
                    getString(R.string.system_category),
	            getString(R.string.misc_category)};
        }
        return titleString;
    }
}

