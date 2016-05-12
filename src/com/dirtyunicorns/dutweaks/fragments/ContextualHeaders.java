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
import com.dirtyunicorns.dutweaks.widget.SeekBarPreferenceCham;

public class ContextualHeaders extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String PREF_CUSTOM_HEADER = "status_bar_custom_header";
    private static final String CUSTOM_HEADER_IMAGE_SHADOW = "status_bar_custom_header_shadow";

    private SwitchPreference mCustomHeader;
    private SeekBarPreferenceCham mHeaderShadow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.contextualheaders);
        PreferenceScreen prefSet = getPreferenceScreen();

        final ContentResolver resolver = getActivity().getContentResolver();

        mCustomHeader = (SwitchPreference) prefSet.findPreference(PREF_CUSTOM_HEADER);
        mCustomHeader.setChecked((Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.STATUS_BAR_CUSTOM_HEADER, 0) == 1));
        mCustomHeader.setOnPreferenceChangeListener(this);

        mHeaderShadow = (SeekBarPreferenceCham) findPreference(CUSTOM_HEADER_IMAGE_SHADOW);
        int headerShadow = Settings.System.getInt(resolver,
                Settings.System.STATUS_BAR_CUSTOM_HEADER_SHADOW, 0);
        mHeaderShadow.setValue(headerShadow);
        mHeaderShadow.setOnPreferenceChangeListener(this);
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
    public boolean onPreferenceChange(Preference preference, Object newValue) {
    ContentResolver resolver = getActivity().getContentResolver();
      if (preference == mCustomHeader) {
         Settings.System.putInt(getContentResolver(),
                 Settings.System.STATUS_BAR_CUSTOM_HEADER,
                 (Boolean) newValue ? 1 : 0);
      } else if (preference == mHeaderShadow) {
         int headerShadow = (Integer) newValue;
         Settings.System.putInt(getActivity().getContentResolver(),
                 Settings.System.STATUS_BAR_CUSTOM_HEADER_SHADOW, headerShadow);
       }
       return true;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
