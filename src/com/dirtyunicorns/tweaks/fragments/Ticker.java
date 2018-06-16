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

package com.dirtyunicorns.tweaks.fragments;

import android.content.ContentResolver;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class Ticker extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

    private ListPreference mTickerMode;
    private ListPreference mTickerAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.ticker);

        mFooterPreferenceMixin.createFooterPreference().setTitle(R.string.ticker_warning_text);

        mTickerMode = (ListPreference) findPreference("ticker_mode");
        mTickerMode.setOnPreferenceChangeListener(this);
        int tickerMode = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.STATUS_BAR_SHOW_TICKER,
                0, UserHandle.USER_CURRENT);
        mTickerMode.setValue(String.valueOf(tickerMode));
        mTickerMode.setSummary(mTickerMode.getEntry());

        mTickerAnimation = (ListPreference) findPreference("status_bar_ticker_animation_mode");
        mTickerAnimation.setOnPreferenceChangeListener(this);
        int tickerAnimationMode = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.STATUS_BAR_TICKER_ANIMATION_MODE,
                1, UserHandle.USER_CURRENT);
        mTickerAnimation.setValue(String.valueOf(tickerAnimationMode));
        mTickerAnimation.setSummary(mTickerAnimation.getEntry());
        updatePrefs();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference.equals(mTickerMode)) {
            int tickerMode = Integer.parseInt(((String) newValue).toString());
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.STATUS_BAR_SHOW_TICKER, tickerMode, UserHandle.USER_CURRENT);
            updatePrefs();
            int index = mTickerMode.findIndexOfValue((String) newValue);
            mTickerMode.setSummary(
                    mTickerMode.getEntries()[index]);
            return true;
        } else if (preference.equals(mTickerAnimation)) {
            int tickerAnimationMode = Integer.parseInt(((String) newValue).toString());
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.STATUS_BAR_TICKER_ANIMATION_MODE, tickerAnimationMode, UserHandle.USER_CURRENT);
            int index = mTickerAnimation.findIndexOfValue((String) newValue);
            mTickerAnimation.setSummary(
                    mTickerAnimation.getEntries()[index]);
            return true;
         }
        return false;
    }

    private void updatePrefs() {
          ContentResolver resolver = getActivity().getContentResolver();
          boolean enabled = (Settings.Global.getInt(resolver,
                  Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED, 0) == 1);
        if (enabled) {
            Settings.System.putInt(resolver,
                Settings.System.STATUS_BAR_SHOW_TICKER, 0);
            Settings.System.putInt(resolver,
                Settings.System.STATUS_BAR_TICKER_ANIMATION_MODE, 1);
            mTickerMode.setEnabled(false);
            mTickerAnimation.setEnabled(false);
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DIRTYTWEAKS;
    }
}
