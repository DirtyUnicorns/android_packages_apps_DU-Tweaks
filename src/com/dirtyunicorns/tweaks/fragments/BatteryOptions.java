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

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import java.util.ArrayList;
import java.util.List;

public class BatteryOptions extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener, Indexable {

    private ListPreference mBatteryIconStyle;
    private ListPreference mBatteryPercentage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.battery_options);

        final ContentResolver resolver = getActivity().getContentResolver();

        int batteryStyle = Settings.Secure.getIntForUser(resolver,
                Settings.Secure.STATUS_BAR_BATTERY_STYLE, 0/*portrait*/,
                UserHandle.USER_CURRENT);
        mBatteryIconStyle = (ListPreference) findPreference("status_bar_battery_style");
        mBatteryIconStyle.setValue(Integer.toString(batteryStyle));
        mBatteryIconStyle.setOnPreferenceChangeListener(this);

         int percentage = Settings.System.getIntForUser(resolver,
                Settings.System.SHOW_BATTERY_PERCENT, 0,
                UserHandle.USER_CURRENT);
        mBatteryPercentage = (ListPreference) findPreference("status_bar_show_battery_percent");
        mBatteryPercentage.setValue(Integer.toString(percentage));
        mBatteryPercentage.setOnPreferenceChangeListener(this);
        boolean hideForcePercentage =
                batteryStyle == 5 || batteryStyle == 6; /*text or hidden style*/
        mBatteryPercentage.setEnabled(!hideForcePercentage);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mBatteryIconStyle) {
            int value = Integer.valueOf((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.STATUS_BAR_BATTERY_STYLE, value,
                    UserHandle.USER_CURRENT);
            boolean hideForcePercentage = value == 5 || value == 6;/*text or hidden style*/
            mBatteryPercentage.setEnabled(!hideForcePercentage);
            return true;
        } else  if (preference == mBatteryPercentage) {
            int value = Integer.valueOf((String) newValue);
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.SHOW_BATTERY_PERCENT, value,
                    UserHandle.USER_CURRENT);
            boolean hideForcePercentage = value == 5 || value == 6;/*text or hidden style*/
            mBatteryPercentage.setEnabled(!hideForcePercentage);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DIRTYTWEAKS;
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    final ArrayList<SearchIndexableResource> result = new ArrayList<>();
                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.battery_options;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
    };
}
