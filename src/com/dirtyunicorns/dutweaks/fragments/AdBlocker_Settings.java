/*
 * Copyright (C) 2015 The Dirty Unicorns Project
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

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.SwitchPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.MetricsLogger;
import com.android.settings.Utils;
import com.dirtyunicorns.dutweaks.fragments.AdBlocker_Helpers;

public class AdBlocker_Settings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String ADBLOCKER_DISABLE_ADS = "adblocker_disable_ads";

    private SwitchPreference mAdBlockerDisableAds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.adblocker_settings);

        final ContentResolver resolver = getActivity().getContentResolver();

        mAdBlockerDisableAds = (SwitchPreference) findPreference(ADBLOCKER_DISABLE_ADS);
        mAdBlockerDisableAds.setChecked((Settings.System.getInt(resolver,
                Settings.System.ADBLOCKER_DISABLE_ADS, 0) == 1));
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.DIRTYTWEAKS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if  (preference == mAdBlockerDisableAds) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.ADBLOCKER_DISABLE_ADS, checked ? 1:0);
            AdBlocker_Helpers.checkStatus(getActivity());
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object value) {
         return true;
    }
}

