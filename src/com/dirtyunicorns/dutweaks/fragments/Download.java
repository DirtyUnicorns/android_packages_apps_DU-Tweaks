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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SeekBarPreference;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class Download extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String GAPPS = "gapps";

    private ListPreference mGapps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.download);
        PreferenceScreen prefSet = getPreferenceScreen();

        mGapps = (ListPreference) prefSet.findPreference(GAPPS);
        mGapps.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (newValue != null) {
            if (preference != null) {
                return launchBrowser(newValue.toString());
            }
        }

        return false;
    }

    private boolean launchBrowser(String urlValue) {
        if (urlValue != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlValue));
            startActivity(browserIntent);
			return true;
        }

        return false;
    }
}
