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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import android.widget.Toast;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.android.internal.logging.nano.MetricsProto;

import com.dirtyunicorns.tweaks.DirtyTweaks;

public class Miscellaneous extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static final String HEADSET_CONNECT_PLAYER = "headset_connect_player";
    private static final String SYSTEMUI_THEME_STYLE = "systemui_theme_style";
    private static final String SCREEN_OFF_ANIMATION = "screen_off_animation";

    private ListPreference mLaunchPlayerHeadsetConnection;
    private ListPreference mSystemUIThemeStyle;
    private ListPreference mScreenOffAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.miscellaneous);

        ContentResolver resolver = getActivity().getContentResolver();

        mLaunchPlayerHeadsetConnection = (ListPreference) findPreference(HEADSET_CONNECT_PLAYER);
        int mLaunchPlayerHeadsetConnectionValue = Settings.System.getIntForUser(resolver,
                Settings.System.HEADSET_CONNECT_PLAYER, 0, UserHandle.USER_CURRENT);
        mLaunchPlayerHeadsetConnection.setValue(Integer.toString(mLaunchPlayerHeadsetConnectionValue));
        mLaunchPlayerHeadsetConnection.setSummary(mLaunchPlayerHeadsetConnection.getEntry());
        mLaunchPlayerHeadsetConnection.setOnPreferenceChangeListener(this);

        mSystemUIThemeStyle = (ListPreference) findPreference(SYSTEMUI_THEME_STYLE);
        int systemUIThemeStyle = Settings.System.getInt(resolver,
                Settings.System.SYSTEM_UI_THEME, 0);
        mSystemUIThemeStyle.setValue(String.valueOf(systemUIThemeStyle));
        mSystemUIThemeStyle.setSummary(mSystemUIThemeStyle.getEntry());
        mSystemUIThemeStyle.setOnPreferenceChangeListener(this);

        mScreenOffAnimation = (ListPreference) findPreference(SCREEN_OFF_ANIMATION);
        int screenOffStyle = Settings.System.getInt(resolver,
                Settings.System.SCREEN_OFF_ANIMATION, 0);
        mScreenOffAnimation.setValue(String.valueOf(screenOffStyle));
        mScreenOffAnimation.setSummary(mScreenOffAnimation.getEntry());
        mScreenOffAnimation.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mLaunchPlayerHeadsetConnection) {
            int mLaunchPlayerHeadsetConnectionValue = Integer.valueOf((String) newValue);
            int index = mLaunchPlayerHeadsetConnection.findIndexOfValue((String) newValue);
            mLaunchPlayerHeadsetConnection.setSummary(
                    mLaunchPlayerHeadsetConnection.getEntries()[index]);
            Settings.System.putIntForUser(resolver, Settings.System.HEADSET_CONNECT_PLAYER,
                    mLaunchPlayerHeadsetConnectionValue, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mSystemUIThemeStyle) {
            String value = (String) newValue;
            Settings.System.putInt(resolver,
                    Settings.System.SYSTEM_UI_THEME, Integer.valueOf(value));
            int valueIndex = mSystemUIThemeStyle.findIndexOfValue(value);
            mSystemUIThemeStyle.setSummary(mSystemUIThemeStyle.getEntries()[valueIndex]);
            if (valueIndex == 2) {
                Intent intent2 = new Intent(Intent.ACTION_MAIN);
                intent2.addCategory(Intent.CATEGORY_HOME);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                Toast.makeText(getContext(), R.string.system_dark_theme_toast_before,
                    Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          Intent intent = new Intent(Intent.ACTION_MAIN);
                          intent.setClassName("com.android.settings",
                                "com.android.settings.Settings$MiscellaneousSettingsActivity");
                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                          startActivity(intent);
                          Toast.makeText(getContext(), R.string.system_dark_theme_toast_after,
                              Toast.LENGTH_SHORT).show();
                      }
                }, 2000);
            }
            return true;
        } else if (preference == mScreenOffAnimation) {
            String value = (String) newValue;
            Settings.System.putInt(resolver,
                    Settings.System.SCREEN_OFF_ANIMATION, Integer.valueOf(value));
            int valueIndex = mScreenOffAnimation.findIndexOfValue(value);
            mScreenOffAnimation.setSummary(mScreenOffAnimation.getEntries()[valueIndex]);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DIRTYTWEAKS;
    }
}
