/*
 * Copyright (C) 2017 The Dirty Unicorns Project
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

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.applications.LayoutPreference;

import com.android.internal.logging.nano.MetricsProto;

public class Multitasking extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static final String HEADS_UP_CATEGORY = "headsup_category";
    private static final String RECENTS_CATEGORY = "recents_category";
    private static final String TICKER_CATEGORY = "ticker_category";

    private LayoutPreference mHeadsUp;
    private LayoutPreference mRecents;
    private LayoutPreference mTicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.multitasking);

        mHeadsUp = (LayoutPreference) findPreference(HEADS_UP_CATEGORY);
        mHeadsUp.setTitle(R.string.headsup_title);

        mRecents = (LayoutPreference) findPreference(RECENTS_CATEGORY);
        mRecents.setTitle(R.string.recents_title);

        mTicker = (LayoutPreference) findPreference(TICKER_CATEGORY);
        mTicker.setTitle(R.string.ticker_title);
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

