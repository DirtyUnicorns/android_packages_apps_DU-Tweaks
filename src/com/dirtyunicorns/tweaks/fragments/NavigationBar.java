/*
 * Copyright (C) 2017-2018 The Dirty Unicorns Project
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

package com.dirtyunicorns.tweaks.fragments;

import android.os.Bundle;
import android.os.UserHandle;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.android.internal.logging.nano.MetricsProto;

public class NavigationBar extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String KEY_PULSE_SETTINGS = "pulse_settings";
    private static final String KEY_STOCK_NAVBAR = "stock_navbar";
    private static final String KEY_NAVIGATION_BAR_ENABLED = "navigation_bar";

    private PreferenceScreen mPulseSettings;
    private Preference mStockNavbar;
    private SwitchPreference mNavigationBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.navigation_bar);

        mPulseSettings = (PreferenceScreen) findPreference(KEY_PULSE_SETTINGS);

        mStockNavbar = (Preference) findPreference(KEY_STOCK_NAVBAR);

        mNavigationBar = (SwitchPreference) findPreference(KEY_NAVIGATION_BAR_ENABLED);
        final boolean defaultToNavigationBar = getResources().getBoolean(
                com.android.internal.R.bool.config_defaultToNavigationBar);
        mNavigationBar.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.NAVIGATION_BAR_ENABLED,
                defaultToNavigationBar ? 1 : 0) == 1));
        mNavigationBar.setOnPreferenceChangeListener(this);

        updateNavbar();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mNavigationBar) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.NAVIGATION_BAR_ENABLED, value ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DIRTYTWEAKS;
    }

    private void updateNavbar() {
        boolean swipeUpGesture = Settings.Secure.getInt(getActivity().getContentResolver(),
                Settings.Secure.SWIPE_UP_TO_SWITCH_APPS_ENABLED, 0) != 0;
        if (!swipeUpGesture) {
            mStockNavbar.setEnabled(true);
            mNavigationBar.setEnabled(true);
            mStockNavbar.setSummary(R.string.systemui_tuner_navbar_enabled_summary);
        } else {
            mStockNavbar.setEnabled(false);
            mNavigationBar.setEnabled(true);
            mStockNavbar.setSummary(R.string.systemui_tuner_navbar_disabled_summary);
        }
    }
}
