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

import java.util.ArrayList;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

import com.android.internal.logging.MetricsLogger;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.SwitchPreference;
import android.preference.Preference;
import android.preference.ListPreference;
import android.provider.Settings;

public class PulseSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = PulseSettings.class.getSimpleName();
    private static final String CUSTOM_DIMEN = "pulse_custom_dimen";
    private static final String CUSTOM_DIV = "pulse_custom_div";
    private static final String PULSE_BLOCK = "pulse_filled_block_size";
    private static final String EMPTY_BLOCK = "pulse_empty_block_size";
    private static final String FUDGE_FACOR = "pulse_custom_fudge_factor";

    SwitchPreference mShowPulse;
    SwitchPreference mLavaLampEnabled;
    ColorPickerPreference mPulseColor;
    ListPreference mCustomDimen;
    ListPreference mCustomDiv;
    ListPreference mFilled;
    ListPreference mEmpty;
    ListPreference mFudge;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pulse_settings);

        ActionBar bar = getActivity().getActionBar();
        if (bar != null) {
            bar.setTitle(R.string.pulse_settings);
        }

        mShowPulse = (SwitchPreference) findPreference("eos_fling_show_pulse");
        mShowPulse.setChecked(Settings.Secure.getInt(getContentResolver(),
                Settings.Secure.FLING_PULSE_ENABLED, 1) == 1);
        mShowPulse.setOnPreferenceChangeListener(this);

        int pulseColor = Settings.Secure.getIntForUser(getContentResolver(),
                Settings.Secure.FLING_PULSE_COLOR, Color.WHITE, UserHandle.USER_CURRENT);
        mPulseColor = (ColorPickerPreference) findPreference("eos_fling_pulse_color");
        mPulseColor.setNewPreviewColor(pulseColor);
        mPulseColor.setOnPreferenceChangeListener(this);

        mLavaLampEnabled = (SwitchPreference) findPreference("eos_fling_lavalamp");
        mLavaLampEnabled.setChecked(Settings.Secure.getInt(getContentResolver(),
                Settings.Secure.FLING_PULSE_LAVALAMP_ENABLED, 1) == 1);
        mLavaLampEnabled.setOnPreferenceChangeListener(this);

        int customdimen = Settings.Secure.getIntForUser(getContentResolver(),
                Settings.Secure.PULSE_CUSTOM_DIMEN, 0, UserHandle.USER_CURRENT);
        mCustomDimen = (ListPreference) findPreference(CUSTOM_DIMEN);
        mCustomDimen.setValue(String.valueOf(customdimen));
        mCustomDimen.setOnPreferenceChangeListener(this);

        int customdiv = Settings.Secure.getIntForUser(getContentResolver(),
                Settings.Secure.PULSE_CUSTOM_DIV, 0, UserHandle.USER_CURRENT);
        mCustomDiv = (ListPreference) findPreference(CUSTOM_DIV);
        mCustomDiv.setValue(String.valueOf(customdiv));
        mCustomDiv.setOnPreferenceChangeListener(this);

        int filled = Settings.Secure.getIntForUser(getContentResolver(),
                Settings.Secure.PULSE_FILLED_BLOCK_SIZE, 0, UserHandle.USER_CURRENT);
        mFilled = (ListPreference) findPreference(PULSE_BLOCK);
        mFilled.setValue(String.valueOf(filled));
        mFilled.setOnPreferenceChangeListener(this);

        int empty = Settings.Secure.getIntForUser(getContentResolver(),
                Settings.Secure.PULSE_EMPTY_BLOCK_SIZE, 0, UserHandle.USER_CURRENT);
        mEmpty = (ListPreference) findPreference(EMPTY_BLOCK);
        mEmpty.setValue(String.valueOf(empty));
        mEmpty.setOnPreferenceChangeListener(this);

        int fudge = Settings.Secure.getIntForUser(getContentResolver(),
                Settings.Secure.PULSE_CUSTOM_FUDGE_FACTOR, 0, UserHandle.USER_CURRENT);
        mFudge = (ListPreference) findPreference(FUDGE_FACOR);
        mFudge.setValue(String.valueOf(fudge));
        mFudge.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.equals(mShowPulse)) {
            boolean enabled = ((Boolean) newValue).booleanValue();
            Settings.Secure.putInt(getContentResolver(),
                    Settings.Secure.FLING_PULSE_ENABLED, enabled ? 1 : 0);
            return true;
        } else if (preference.equals(mPulseColor)) {
            int color = ((Integer) newValue).intValue();
            Settings.Secure.putInt(getContentResolver(),
                    Settings.Secure.FLING_PULSE_COLOR, color);
            return true;
        } else if (preference.equals(mLavaLampEnabled)) {
            boolean enabled = ((Boolean) newValue).booleanValue();
            Settings.Secure.putInt(getContentResolver(),
                    Settings.Secure.FLING_PULSE_LAVALAMP_ENABLED, enabled ? 1 : 0);
            return true;
        } else if (preference == mCustomDimen) {
            int customdimen = Integer.valueOf((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.PULSE_CUSTOM_DIMEN, customdimen, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mCustomDiv) {
            int customdiv = Integer.valueOf((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.PULSE_CUSTOM_DIV, customdiv, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mFilled) {
            int filled = Integer.valueOf((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.PULSE_FILLED_BLOCK_SIZE, filled, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mEmpty) {
            int empty = Integer.valueOf((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.PULSE_EMPTY_BLOCK_SIZE, empty, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mFudge) {
            int fudge = Integer.valueOf((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.PULSE_CUSTOM_FUDGE_FACTOR, fudge, UserHandle.USER_CURRENT);
            return true;
        }
        return false;
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.DIRTYTWEAKS;
    }
}
