/*
 * Copyright (C) 2014-2015 The Dirty Unicorns Project
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
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.support.v13.app.FragmentPagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.internal.logging.MetricsLogger;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.dirtyunicorns.dutweaks.PagerSlidingTabStrip;
import com.dirtyunicorns.dutweaks.tabs.Lockscreen;
import com.dirtyunicorns.dutweaks.tabs.MultiTasking;
import com.dirtyunicorns.dutweaks.tabs.Navigation;
import com.dirtyunicorns.dutweaks.tabs.StatusBar;
import com.dirtyunicorns.dutweaks.tabs.System;
import com.dirtyunicorns.dutweaks.viewpager.transforms.*;

import java.util.ArrayList;
import java.util.List;

public class DirtyTweaks extends SettingsPreferenceFragment {

    private static final int MENU_HELP  = 0;

    ViewPager mViewPager;
    String titleString[];
    ViewGroup mContainer;
    PagerSlidingTabStrip mTabs;

    private SettingsObserver mSettingsObserver;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContainer = container;

        View view = inflater.inflate(R.layout.dirtytweaks, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        mSettingsObserver = new SettingsObserver(new Handler());
        StatusBarAdapter StatusBarAdapter = new StatusBarAdapter(getFragmentManager());
        mViewPager.setAdapter(StatusBarAdapter);
        mTabs.setViewPager(mViewPager);
        mSettingsObserver.observe();

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
    protected int getMetricsCategory() {
        return MetricsLogger.DIRTYTWEAKS;
    }

    @Override
    public void onResume() {
        super.onResume();
        mContainer.setPadding(30, 30, 30, 30);
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
            frags[0] = new System();
            frags[1] = new Lockscreen();
            frags[2] = new StatusBar();
            frags[3] = new Navigation();
            frags[4] = new MultiTasking();
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
        titleString = new String[]{
                    getString(R.string.system_category),
                    getString(R.string.lockscreen_category),
                    getString(R.string.statusbar_category),
                    getString(R.string.navigation_category),
                    getString(R.string.multitasking_category)};
        return titleString;
    }

    private class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            ContentResolver resolver = mContext.getContentResolver();
            resolver.registerContentObserver(Settings.System.getUriFor(
                    Settings.System.DIRTY_TWEAKS_TABS_EFFECT),
                    false, this, UserHandle.USER_ALL);
            update();
        }

        void unobserve() {
            ContentResolver resolver = mContext.getContentResolver();
            resolver.unregisterContentObserver(this);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            update();
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            update();
        }

        public void update() {
            ContentResolver resolver = mContext.getContentResolver();
            int effect = Settings.System.getIntForUser(resolver,
                Settings.System.DIRTY_TWEAKS_TABS_EFFECT, 0,
                UserHandle.USER_CURRENT);
            switch (effect) {
                case 0:
                    mViewPager.setPageTransformer(true, new DefaultTransformer());
                    break;
                case 1:
                    mViewPager.setPageTransformer(true, new AccordionTransformer());
                    break;
                case 2:
                    mViewPager.setPageTransformer(true, new BackgroundToForegroundTransformer());
                    break;
                case 3:
                    mViewPager.setPageTransformer(true, new CubeInTransformer());
                    break;
                case 4:
                    mViewPager.setPageTransformer(true, new CubeOutTransformer());
                    break;
                case 5:
                    mViewPager.setPageTransformer(true, new DepthPageTransformer());
                    break;
                case 6:
                    mViewPager.setPageTransformer(true, new FlipHorizontalTransformer());
                    break;
                case 7:
                    mViewPager.setPageTransformer(true, new FlipVerticalTransformer());
                    break;
                case 8:
                    mViewPager.setPageTransformer(true, new ForegroundToBackgroundTransformer());
                    break;
                case 9:
                    mViewPager.setPageTransformer(true, new RotateDownTransformer());
                    break;
                case 10:
                    mViewPager.setPageTransformer(true, new RotateUpTransformer());
                    break;
                case 11:
                    mViewPager.setPageTransformer(true, new ScaleInOutTransformer());
                    break;
                case 12:
                    mViewPager.setPageTransformer(true, new StackTransformer());
                    break;
                case 13:
                    mViewPager.setPageTransformer(true, new TabletTransformer());
                    break;
                case 14:
                    mViewPager.setPageTransformer(true, new ZoomInTransformer());
                    break;
                case 15:
                    mViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
                    break;
                case 16:
                    mViewPager.setPageTransformer(true, new ZoomOutTranformer());
                    break;
                default:
                    break;
            }
        }
    }
}
