/*
 * Copyright (C) 2014 TeamEos
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

package com.dirtyunicorns.dutweaks.fragments;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.SwitchPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;

import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.utils.du.DUActionUtils;
import com.android.settings.R;

public class NavbarSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String NAVBAR_VISIBILITY = "navbar_visibility";
    private static final String KEY_NAVBAR_MODE = "navbar_mode";
    private static final String KEY_FLING_NAVBAR_SETTINGS = "fling_settings";
    private static final String KEY_CATEGORY_NAVIGATION_INTERFACE = "category_navbar_interface";
    private static final String KEY_CATEGORY_NAVIGATION_GENERAL = "category_navbar_general";
    private static final String KEY_NAVIGATION_BAR_LEFT = "navigation_bar_left";
    private static final String KEY_NAVIGATION_BAR_DT2S = "double_tap_sleep_navbar";
    private static final String KEY_NAVIGATION_BAR_SIZE = "navigation_bar_size";

    private SwitchPreference mNavbarVisibility;
    private ListPreference mNavbarMode;
    private PreferenceScreen mFlingSettings;
    private PreferenceCategory mNavInterface;
    private PreferenceCategory mNavGeneral;
    private Preference mNavbarSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.navbar_settings);

        final PreferenceScreen prefScreen = getPreferenceScreen();

        mNavInterface = (PreferenceCategory) findPreference(KEY_CATEGORY_NAVIGATION_INTERFACE);
        mNavGeneral = (PreferenceCategory) findPreference(KEY_CATEGORY_NAVIGATION_GENERAL);
        mNavbarVisibility = (SwitchPreference) findPreference(NAVBAR_VISIBILITY);
        mNavbarMode = (ListPreference) findPreference(KEY_NAVBAR_MODE);
        mFlingSettings = (PreferenceScreen) findPreference(KEY_FLING_NAVBAR_SETTINGS);
        mNavbarSettings = (Preference) findPreference(KEY_NAVIGATION_BAR_DT2S);

        boolean showing = Settings.Secure.getInt(getContentResolver(),
                Settings.Secure.NAVIGATION_BAR_VISIBLE,
                DUActionUtils.hasNavbarByDefault(getActivity()) ? 1 : 0) != 0;
        updateBarVisibleAndUpdatePrefs(showing);
        mNavbarVisibility.setOnPreferenceChangeListener(this);

        int mode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.NAVIGATION_BAR_MODE,
                0);
        updateBarModeSettings(mode);
        mNavbarMode.setOnPreferenceChangeListener(this);

        // Navigation bar left-in-landscape
        // remove if not a phone
        if (!DUActionUtils.isNormalScreen()) {
            mNavGeneral.removePreference(findPreference(KEY_NAVIGATION_BAR_LEFT));
        }
    }

    private void updateBarModeSettings(int mode) {
        mNavbarMode.setValue(String.valueOf(mode));
        mNavbarSettings.setEnabled(mode == 0);
        mNavbarSettings.setSelectable(mode == 0);
        mFlingSettings.setEnabled(mode == 1);
        mFlingSettings.setSelectable(mode == 1);
    }

    private void updateBarVisibleAndUpdatePrefs(boolean showing) {
        mNavbarVisibility.setChecked(showing);
        mNavInterface.setEnabled(mNavbarVisibility.isChecked());
        mNavGeneral.setEnabled(mNavbarVisibility.isChecked());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.equals(mNavbarMode)) {
            int mode = Integer.parseInt(((String) newValue).toString());
            Settings.Secure.putInt(getContentResolver(),
                    Settings.Secure.NAVIGATION_BAR_MODE, mode);
            updateBarModeSettings(mode);
            return true;
        } else if (preference.equals(mNavbarVisibility)) {
            boolean showing = ((Boolean)newValue);
            Settings.Secure.putInt(getContentResolver(), Settings.Secure.NAVIGATION_BAR_VISIBLE,
                    showing ? 1 : 0);
            updateBarVisibleAndUpdatePrefs(showing);
            return true;
        }
        return false;
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.DIRTYTWEAKS;
    }
}
