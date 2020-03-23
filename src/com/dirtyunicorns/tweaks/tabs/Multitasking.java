/*
 * Copyright (C) 2017-2019 The Dirty Unicorns Project
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

package com.dirtyunicorns.tweaks.tabs;

import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class Multitasking extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String ACTIVE_EDGE_CATEGORY = "active_edge_category";
    private static final String AWARE_CATEGORY = "aware_settings";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.multitasking);

        Preference ActiveEdge = findPreference(ACTIVE_EDGE_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_active_edge)) {
            getPreferenceScreen().removePreference(ActiveEdge);
        } else {
            if (!getContext().getPackageManager().hasSystemFeature(
                    "android.hardware.sensor.assist")) {
                getPreferenceScreen().removePreference(ActiveEdge);
            }
        }

        Preference Aware = findPreference(AWARE_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_aware)) {
            getPreferenceScreen().removePreference(Aware);
        } else {
            if (!SystemProperties.getBoolean(
                    "ro.vendor.aware_available", false)) {
                getPreferenceScreen().removePreference(Aware);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DIRTYTWEAKS;
    }
}
