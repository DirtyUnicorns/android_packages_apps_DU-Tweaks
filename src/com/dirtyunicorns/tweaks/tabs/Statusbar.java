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

package com.dirtyunicorns.tweaks.tabs;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v14.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceChangeListener;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto;

public class Statusbar extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String BATTERY_CATEGORY = "battery_options_category";
    private static final String CARRIER_LABEL_CATEGORY = "carrier_label_category";
    private static final String CLOCK_CATEGORY = "clock_options_category";
    private static final String ICON_MANAGER_CATEGORY = "icon_manager_title";
    private static final String NOTIFICATIONS_CATEGORY = "notifications_category";
    private static final String QUICK_SETTINGS_CATEGORY = "quick_settings_category";
    private static final String TRAFFIC_CATEGORY = "traffic_category";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.statusbar);

        Preference BatteryOptions = findPreference(BATTERY_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_battery_options)) {
            getPreferenceScreen().removePreference(BatteryOptions);
        }

        Preference CarrierLabel = findPreference(CARRIER_LABEL_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_carrier_label)) {
            getPreferenceScreen().removePreference(CarrierLabel);
        }

        Preference ClockOptions = findPreference(CLOCK_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_clock_options)) {
            getPreferenceScreen().removePreference(ClockOptions);
        }

        Preference IconManager = findPreference(ICON_MANAGER_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_icon_manager)) {
            getPreferenceScreen().removePreference(IconManager);
        }

        Preference Notifications = findPreference(NOTIFICATIONS_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_notifications)) {
            getPreferenceScreen().removePreference(Notifications);
        }

        Preference QuickSettings = findPreference(QUICK_SETTINGS_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_quick_settings)) {
            getPreferenceScreen().removePreference(QuickSettings);
        }

        Preference Traffic = findPreference(TRAFFIC_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_traffic)) {
            getPreferenceScreen().removePreference(Traffic);
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

