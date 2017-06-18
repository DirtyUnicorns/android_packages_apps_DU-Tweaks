/*
 * Copyright (C) 2014-2017 The Dirty Unicorns Project
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
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.internal.util.du.DuUtils;

import com.dirtyunicorns.dutweaks.preference.SystemSettingSwitchPreference;

public class FingerprintPrefs extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String FP_UNLOCK_KEYSTORE = "fp_unlock_keystore";
    private static final String FP_SWIPE_CALL_ACTIONS = "fp_swipe_call_actions";
    private static final String FINGERPRINT_VIB = "fingerprint_success_vib";
    private static final String PREF_QUICK_PULLDOWN_FP = "quick_pulldown_fp";

    private ListPreference mFpSwipeCallActions;
    private SystemSettingSwitchPreference mFpKeystore;
    private SystemSettingSwitchPreference mFingerprintVib;
    private SystemSettingSwitchPreference mQuickPulldownFp;

    private int mFpSwipeCallActionsValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.fingerprint_prefs);
        PreferenceScreen prefSet = getPreferenceScreen();

        ContentResolver resolver = getActivity().getContentResolver();

        mFpKeystore = (SystemSettingSwitchPreference) findPreference(FP_UNLOCK_KEYSTORE);
        if (Build.BOARD.contains("marlin") || Build.BOARD.contains("sailfish")) {
            prefSet.removePreference(mFpKeystore);
        } else {
            mFpKeystore.setChecked((Settings.System.getInt(getContentResolver(),
                   Settings.System.FP_UNLOCK_KEYSTORE, 0) == 1));
            mFpKeystore.setOnPreferenceChangeListener(this);
        }

        mFingerprintVib = (SystemSettingSwitchPreference) findPreference(FINGERPRINT_VIB);
        mFingerprintVib.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.FINGERPRINT_SUCCESS_VIB, 0) == 1));
        mFingerprintVib.setOnPreferenceChangeListener(this);

        mQuickPulldownFp = (SystemSettingSwitchPreference) findPreference(PREF_QUICK_PULLDOWN_FP);
        if (getResources().getBoolean(com.android.internal.R.bool.config_supportSystemNavigationKeys)) {
            mQuickPulldownFp.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_QUICK_QS_PULLDOWN_FP, 0) == 1));
            mQuickPulldownFp.setOnPreferenceChangeListener(this);

            mFpSwipeCallActions = (ListPreference) findPreference(FP_SWIPE_CALL_ACTIONS);
            mFpSwipeCallActionsValue = Settings.System.getIntForUser(getContentResolver(),
                    Settings.System.FP_SWIPE_CALL_ACTIONS, 0, UserHandle.USER_CURRENT);
            mFpSwipeCallActions.setValue(Integer.toString(mFpSwipeCallActionsValue));
            mFpSwipeCallActions.setSummary(mFpSwipeCallActions.getEntry());
            mFpSwipeCallActions.setOnPreferenceChangeListener(this);
        } else {
            prefSet.removePreference(mQuickPulldownFp);
            prefSet.removePreference(mFpSwipeCallActions);
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
    public void onPause() {
        super.onPause();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mFpKeystore) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.FP_UNLOCK_KEYSTORE, value ? 1 : 0);
            return true;
        } else if (preference == mFingerprintVib) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.FINGERPRINT_SUCCESS_VIB, value ? 1 : 0);
            return true;
        } else if (preference == mQuickPulldownFp) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_QUICK_QS_PULLDOWN_FP, value ? 1 : 0);
            return true;
        } else if (preference == mFpSwipeCallActions) {
            mFpSwipeCallActionsValue = Integer.valueOf((String) objValue);
            int index = mFpSwipeCallActions.findIndexOfValue((String) objValue);
            mFpSwipeCallActions.setSummary(
                    mFpSwipeCallActions.getEntries()[index]);
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.FP_SWIPE_CALL_ACTIONS, mFpSwipeCallActionsValue,
                    UserHandle.USER_CURRENT);
            return true;
        }
        return false;
    }
}
