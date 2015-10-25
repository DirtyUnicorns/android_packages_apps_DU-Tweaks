/*
 * Copyright (C) 2014 The Dirty Unicorns Project
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
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;

import com.android.settings.R;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class PowerMenu extends SettingsPreferenceFragment implements OnPreferenceChangeListener {
    private static final String KEY_ADVANCED_REBOOT = "advanced_reboot";

    private ListPreference mAdvancedReboot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.powermenu);

        final ContentResolver resolver = getActivity().getContentResolver();

        mAdvancedReboot = (ListPreference) findPreference(KEY_ADVANCED_REBOOT);
        mAdvancedReboot.setValue(String.valueOf(Settings.Secure.getInt(
                getContentResolver(), Settings.Secure.ADVANCED_REBOOT, 1)));
        mAdvancedReboot.setSummary(mAdvancedReboot.getEntry());
        mAdvancedReboot.setOnPreferenceChangeListener(this);
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.DIRTYTWEAKS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object value) {
        if (preference == mAdvancedReboot) {
            Settings.Secure.putInt(getContentResolver(), Settings.Secure.ADVANCED_REBOOT,
                    Integer.valueOf((String) value));
            mAdvancedReboot.setValue(String.valueOf(value));
            mAdvancedReboot.setSummary(mAdvancedReboot.getEntry());
        } else {
            return false;
        }

        return true;
    }
}
