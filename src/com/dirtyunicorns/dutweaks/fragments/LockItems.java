/*
 * Copyright (C) 2014-2016 The Dirty Unicorns Project
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
import android.support.v7.preference.Preference;
import android.support.v7.preference.ListPreference;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;

import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.internal.widget.LockPatternUtils;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class LockItems extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String PREF_SHOW_EMERGENCY_BUTTON = "show_emergency_button";

    private SwitchPreference mEmergencyButton;

    private static final int MY_USER_ID = UserHandle.myUserId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.lock_items);

        final ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefSet = getPreferenceScreen();
        final LockPatternUtils lockPatternUtils = new LockPatternUtils(getActivity());

        mEmergencyButton = (SwitchPreference) findPreference(PREF_SHOW_EMERGENCY_BUTTON);
        if (lockPatternUtils.isSecure(MY_USER_ID)) {
            mEmergencyButton.setChecked((Settings.System.getInt(resolver,
                Settings.System.SHOW_EMERGENCY_BUTTON, 1) == 1));
            mEmergencyButton.setOnPreferenceChangeListener(this);
        } else {
            prefSet.removePreference(mEmergencyButton);
        }
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.DIRTYTWEAKS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {

        if  (preference == mEmergencyButton) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.SHOW_EMERGENCY_BUTTON, checked ? 1:0);
            return true;
        }
        return false;
    }
}
