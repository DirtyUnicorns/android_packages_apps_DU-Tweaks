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
import android.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class System extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String DEVICE_CATEGORY = "device_extras_category";
    private static final String EXPANDED_DESKTOP_CATEGORY = "expanded_desktop_category";
    private static final String LOCKSCREEN_ITEMS_CATEGORY = "lockscreen_items_category";
    private static final String MISC_CATEGORY = "miscellaneous_category";
    private static final String POWERMENU_CATEGORY = "powermenu_category";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.system);

        Preference DeviceExtras = findPreference(DEVICE_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_device_extras)) {
            getPreferenceScreen().removePreference(DeviceExtras);
        }

        Preference ExpandedDesktop = findPreference(EXPANDED_DESKTOP_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_expanded_desktop)) {
            getPreferenceScreen().removePreference(ExpandedDesktop);
        }

        Preference LockscreenItems = findPreference(LOCKSCREEN_ITEMS_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_lockscreen_items)) {
            getPreferenceScreen().removePreference(LockscreenItems);
        }

        Preference MiscOptions = findPreference("miscellaneous_category");
        if (!getResources().getBoolean(R.bool.has_misc_options)) {
            getPreferenceScreen().removePreference(MiscOptions);
        }

        Preference PowerMenu = findPreference(POWERMENU_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_powermenu)) {
            getPreferenceScreen().removePreference(PowerMenu);
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
