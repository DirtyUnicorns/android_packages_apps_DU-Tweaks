/*
 * Copyright (C) 2020 The Dirty Unicorns Project
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

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.dirtyunicorns.tweaks.fragments.team.TeamActivity;

import github.com.st235.lib_expandablebottombar.ExpandableBottomBar;
import github.com.st235.lib_expandablebottombar.ExpandableBottomBarMenuItem;

public class DirtyTweaks extends SettingsPreferenceFragment {

    Context mContext;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mContext = getActivity();
        Resources res = getResources();
        Window win = getActivity().getWindow();

        win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        win.setNavigationBarColor(res.getColor(R.color.dirty_tweaks_navbar_color));
        win.setNavigationBarDividerColor(res.getColor(R.color.dirty_tweaks_navbar_color));

        view = inflater.inflate(R.layout.dirtytweaks, container, false);

        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.dirtytweaks_title);
        }

        ExpandableBottomBar bottomBar = view.findViewById(R.id.expandable_bottom_bar);

        Fragment system = new com.dirtyunicorns.tweaks.tabs.System();
        Fragment statusbar = new com.dirtyunicorns.tweaks.tabs.Statusbar();
        Fragment navigation = new com.dirtyunicorns.tweaks.tabs.Navigation();
        Fragment multitasking = new com.dirtyunicorns.tweaks.tabs.Multitasking();

        Fragment fragment = (Fragment) getFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, system);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        bottomBar.addItems(mContext.getResources().getBoolean(
                R.bool.has_active_edge) ? new ExpandableBottomBarMenuItem.Builder(mContext)
                .addItem(R.id.system,
                        R.drawable.bottomnav_system,
                        R.string.bottom_nav_system_title, getThemeAccentColor(mContext))
                .addItem(R.id.statusbar,
                        R.drawable.bottomnav_statusbar,
                        R.string.bottom_nav_statusbar_title, getThemeAccentColor(mContext))
                .addItem(R.id.navigation,
                        R.drawable.bottomnav_navigation,
                        R.string.bottom_nav_navigation_title, getThemeAccentColor(mContext))
                .addItem(R.id.multitasking,
                        R.drawable.bottomnav_multitasking,
                        R.string.bottom_nav_multitasking_title, getThemeAccentColor(mContext))
                .build() : new ExpandableBottomBarMenuItem.Builder(mContext)
                .addItem(R.id.system,
                        R.drawable.bottomnav_system,
                        R.string.bottom_nav_system_title, getThemeAccentColor(mContext))
                .addItem(R.id.statusbar,
                        R.drawable.bottomnav_statusbar,
                        R.string.bottom_nav_statusbar_title, getThemeAccentColor(mContext))
                .addItem(R.id.navigation,
                        R.drawable.bottomnav_navigation,
                        R.string.bottom_nav_navigation_title, getThemeAccentColor(mContext))
                .build()
        );

        bottomBar.setOnItemSelectedListener((view, menuItem) -> {
            int id = menuItem.getItemId();
            if (id == R.id.system) {
                launchFragment(system);
            } else if (id == R.id.statusbar) {
                launchFragment(statusbar);
            } else if (id == R.id.navigation) {
                launchFragment(navigation);
            } else if (id == R.id.multitasking) {
                if (mContext.getResources().getBoolean(R.bool.has_active_edge)) {
                    launchFragment(multitasking);
                }
            }
            return null;
        });

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    }

    private void launchFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
        if (item.getItemId() == 0) {
            Intent intent = new Intent(mContext, TeamActivity.class);
            mContext.startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        view = getView();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP &&
                    keyCode == KeyEvent.KEYCODE_BACK) {
                getActivity().finish();
                return true;
            }
            return false;
        });
    }

    public static int getThemeAccentColor (final Context context) {
        final TypedValue value = new TypedValue ();
        context.getTheme ().resolveAttribute (android.R.attr.colorAccent, value, true);
        return value.data;
    }
}
