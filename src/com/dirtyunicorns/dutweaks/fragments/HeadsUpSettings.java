/*
 * Copyright (C) 2014-2017 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dirtyunicorns.dutweaks.fragments;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.dirtyunicorns.dutweaks.preferences.BaseGlobalSettingSwitchBar;

public class HeadsUpSettings extends SettingsPreferenceFragment
        implements BaseGlobalSettingSwitchBar.SwitchBarChangeCallback {

    private BaseGlobalSettingSwitchBar mEnabledSwitch;

    private ViewGroup mPrefsContainer;
    private View mDisabledText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.headsup_settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.headsup_fragment, container, false);
        mPrefsContainer = (ViewGroup) v.findViewById(R.id.prefs_container);
        mDisabledText = v.findViewById(R.id.disabled_text);

        View prefs = super.onCreateView(inflater, mPrefsContainer, savedInstanceState);
        mPrefsContainer.addView(prefs);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        final SettingsActivity activity = (SettingsActivity) getActivity();
        mEnabledSwitch = new BaseGlobalSettingSwitchBar(activity, activity.getSwitchBar(),
                Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED, true, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        final SettingsActivity activity = (SettingsActivity) getActivity();
        if (mEnabledSwitch != null) {
            mEnabledSwitch.resume(activity);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mEnabledSwitch != null) {
            mEnabledSwitch.pause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mEnabledSwitch != null) {
            mEnabledSwitch.teardownSwitchBar();
        }
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.DIRTYTWEAKS;
    }

    private boolean getUserHeadsUpState() {
         return Settings.Global.getInt(getContentResolver(),
                Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED,
                Settings.Global.HEADS_UP_ON) != 0;
    }

    private void setUserHeadsUpState(int val) {
         Settings.Global.putInt(getContentResolver(),
                Settings.Global.HEADS_UP_NOTIFICATIONS_ENABLED, val);
    }

    private void updateEnabledState() {
        mPrefsContainer.setVisibility(getUserHeadsUpState() ? View.VISIBLE : View.GONE);
        mDisabledText.setVisibility(getUserHeadsUpState() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onEnablerChanged(boolean isEnabled) {
        setUserHeadsUpState(getUserHeadsUpState() ? 1 : 0);
        updateEnabledState();
    }
}
