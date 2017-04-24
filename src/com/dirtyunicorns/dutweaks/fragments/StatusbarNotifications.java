/*
 * Copyright (C) 2015-2016 The Dirty Unicorns Project
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
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.settings.Utils;

public class StatusbarNotifications extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String STATUS_BAR_NOTIF_COUNT = "status_bar_notif_count";
    private static final String FORCE_EXPANDED_NOTIFICATIONS = "force_expanded_notifications";
    private static final String DISABLE_IMMERSIVE_MESSAGE = "disable_immersive_message";
    private static final String MISSED_CALL_BREATH = "missed_call_breath";
    private static final String VOICEMAIL_BREATH = "voicemail_breath";
    private static final String SMS_BREATH = "sms_breath";
    private static final String BREATHING_NOTIFICATIONS = "breathing_notifications";

    private SwitchPreference mEnableNC;
    private SwitchPreference mForceExpanded;
    private SwitchPreference mDisableIM;
    private SwitchPreference mMissedCallBreath;
    private SwitchPreference mVoicemailBreath;
    private SwitchPreference mSmsBreath;
    private PreferenceGroup mBreathingNotifications;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.statusbar_notifications);

        final PreferenceScreen prefSet = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();

        mEnableNC = (SwitchPreference) findPreference(STATUS_BAR_NOTIF_COUNT);
        mEnableNC.setOnPreferenceChangeListener(this);
        int EnableNC = Settings.System.getInt(getContentResolver(),
                STATUS_BAR_NOTIF_COUNT, 0);
        mEnableNC.setChecked(EnableNC != 0);

        mForceExpanded = (SwitchPreference) findPreference(FORCE_EXPANDED_NOTIFICATIONS);
        mForceExpanded.setOnPreferenceChangeListener(this);
        int ForceExpanded = Settings.System.getInt(getContentResolver(),
                FORCE_EXPANDED_NOTIFICATIONS, 0);
        mForceExpanded.setChecked(ForceExpanded != 0);

        mDisableIM = (SwitchPreference) findPreference(DISABLE_IMMERSIVE_MESSAGE);
        mDisableIM.setOnPreferenceChangeListener(this);
        int DisableIM = Settings.System.getInt(getContentResolver(),
                DISABLE_IMMERSIVE_MESSAGE, 0);
        mDisableIM.setChecked(DisableIM != 0);

        mMissedCallBreath = (SwitchPreference) findPreference(MISSED_CALL_BREATH);
        mVoicemailBreath = (SwitchPreference) findPreference(VOICEMAIL_BREATH);
        mSmsBreath = (SwitchPreference) findPreference(SMS_BREATH);

        mBreathingNotifications = (PreferenceGroup) findPreference(BREATHING_NOTIFICATIONS);

        Context context = getActivity();
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm.isNetworkSupported(ConnectivityManager.TYPE_MOBILE)) {

            mMissedCallBreath.setChecked(Settings.System.getInt(resolver,
                    Settings.System.KEY_MISSED_CALL_BREATH, 0) == 1);
            mMissedCallBreath.setOnPreferenceChangeListener(this);

            mVoicemailBreath.setChecked(Settings.System.getInt(resolver,
                    Settings.System.KEY_VOICEMAIL_BREATH, 0) == 1);
            mVoicemailBreath.setOnPreferenceChangeListener(this);

            mSmsBreath.setChecked(Settings.Global.getInt(resolver,
                    Settings.Global.KEY_SMS_BREATH, 0) == 1);
            mSmsBreath.setOnPreferenceChangeListener(this);
        } else {
            prefSet.removePreference(mMissedCallBreath);
            prefSet.removePreference(mVoicemailBreath);
            prefSet.removePreference(mSmsBreath);
            prefSet.removePreference(mBreathingNotifications);
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
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if  (preference == mEnableNC) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(), STATUS_BAR_NOTIF_COUNT,
                    value ? 1 : 0);
            return true;
        } else if (preference == mForceExpanded) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(), FORCE_EXPANDED_NOTIFICATIONS,
                    value ? 1 : 0);
            return true;
        } else if (preference == mDisableIM) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(), DISABLE_IMMERSIVE_MESSAGE,
                    value ? 1 : 0);
            return true;
        } else if (preference == mMissedCallBreath) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(), MISSED_CALL_BREATH,
                    value ? 1 : 0);
            return true;
        } else if (preference == mVoicemailBreath) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(), VOICEMAIL_BREATH,
                    value ? 1 : 0);
            return true;
        } else if (preference == mSmsBreath) {
            boolean value = (Boolean) newValue;
            Settings.Global.putInt(getContentResolver(), SMS_BREATH,
                    value ? 1 : 0);
            return true;
        }
        return false;
    }
}
