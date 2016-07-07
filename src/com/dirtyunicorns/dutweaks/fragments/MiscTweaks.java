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
import android.hardware.fingerprint.FingerprintManager;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.SwitchPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.util.Log;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import android.provider.Settings.SettingNotFoundException;
import com.android.internal.util.du.AbstractAsyncSuCMDProcessor;
import com.android.internal.util.du.CMDProcessor;
import com.android.internal.util.du.Helpers;
import com.android.internal.util.du.DuUtils;
import com.android.internal.logging.MetricsLogger;
import com.android.settings.Utils;

import java.io.File;
import java.io.IOException;
import java.io.DataOutputStream;

import com.dirtyunicorns.dutweaks.fragments.AdBlocker_Helpers;

public class MiscTweaks extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String TAG = "MiscTweaks";
    private static final String STATUS_BAR_BRIGHTNESS_CONTROL = "status_bar_brightness_control";
    private static final String PREF_MEDIA_SCANNER_ON_BOOT = "media_scanner_on_boot";
    private static final String SCROLLINGCACHE_PREF = "pref_scrollingcache";
    private static final String SCROLLINGCACHE_PERSIST_PROP = "persist.sys.scrollingcache";
    private static final String SCROLLINGCACHE_DEFAULT = "1";
    private static final String ENABLE_TASK_MANAGER = "enable_task_manager";
    private static final String SELINUX = "selinux";
    private static final String FLASHLIGHT_NOTIFICATION = "flashlight_notification";
    private static final String ADBLOCKER_DISABLE_ADS = "adblocker_disable_ads";

    private FingerprintManager mFingerprintManager;
    private ListPreference mMsob;
    private ListPreference mScrollingCachePref;
    private SwitchPreference mFingerprintVib;
    private SwitchPreference mStatusBarBrightnessControl;
    private SwitchPreference mEnableTaskManager;
    private SwitchPreference mSelinux;
    private SwitchPreference mFlashlightNotification;
    private SwitchPreference mAdBlockerDisableAds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.misctweaks);

        final ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();

        //SELinux
        mSelinux = (SwitchPreference) findPreference(SELINUX);
        mSelinux.setOnPreferenceChangeListener(this);

        if (CMDProcessor.runShellCommand("getenforce").getStdout().contains("Enforcing")) {
            Log.d(TAG, "cmdline: selinux:Enforcing");
            mSelinux.setChecked(true);
            mSelinux.setSummary(R.string.selinux_enforcing_title);
        } else {
            Log.d(TAG, "cmdline: selinux:Permissive");
            mSelinux.setChecked(false);
            mSelinux.setSummary(R.string.selinux_permissive_title);
        }

        mFlashlightNotification = (SwitchPreference) findPreference(FLASHLIGHT_NOTIFICATION);
        mFlashlightNotification.setOnPreferenceChangeListener(this);
        if (!DuUtils.deviceSupportsFlashLight(getActivity())) {
            prefScreen.removePreference(mFlashlightNotification);
        } else {
        mFlashlightNotification.setChecked((Settings.System.getInt(resolver,
                Settings.System.FLASHLIGHT_NOTIFICATION, 0) == 1));
        }

        mMsob = (ListPreference) findPreference(PREF_MEDIA_SCANNER_ON_BOOT);
        mMsob.setValue(String.valueOf(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.MEDIA_SCANNER_ON_BOOT, 0)));
        mMsob.setSummary(mMsob.getEntry());
        mMsob.setOnPreferenceChangeListener(this);

        mScrollingCachePref = (ListPreference) findPreference(SCROLLINGCACHE_PREF);
        mScrollingCachePref.setValue(SystemProperties.get(SCROLLINGCACHE_PERSIST_PROP,
                SystemProperties.get(SCROLLINGCACHE_PERSIST_PROP, SCROLLINGCACHE_DEFAULT)));
        mScrollingCachePref.setOnPreferenceChangeListener(this);

        mStatusBarBrightnessControl = (SwitchPreference) findPreference(STATUS_BAR_BRIGHTNESS_CONTROL);
        mStatusBarBrightnessControl.setOnPreferenceChangeListener(this);
        int statusBarBrightnessControl = Settings.System.getInt(getContentResolver(),
                STATUS_BAR_BRIGHTNESS_CONTROL, 0);
        mStatusBarBrightnessControl.setChecked(statusBarBrightnessControl != 0);
        try {
            if (Settings.System.getInt(getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                mStatusBarBrightnessControl.setEnabled(false);
                mStatusBarBrightnessControl.setSummary(R.string.status_bar_brightness_control_info);
            }
        } catch (SettingNotFoundException e) {
        }

        mEnableTaskManager = (SwitchPreference) findPreference(ENABLE_TASK_MANAGER);
        mEnableTaskManager.setChecked((Settings.System.getInt(resolver,
                Settings.System.ENABLE_TASK_MANAGER, 0) == 1));

        mFingerprintManager = (FingerprintManager) getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
        mFingerprintVib = (SwitchPreference) prefScreen.findPreference("fingerprint_success_vib");
        if (!mFingerprintManager.isHardwareDetected()){
            prefScreen.removePreference(mFingerprintVib);
        }

        mAdBlockerDisableAds = (SwitchPreference) findPreference(ADBLOCKER_DISABLE_ADS);
        mAdBlockerDisableAds.setChecked((Settings.System.getInt(resolver,
                Settings.System.ADBLOCKER_DISABLE_ADS, 0) == 1));
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
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if  (preference == mAdBlockerDisableAds) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.ADBLOCKER_DISABLE_ADS, checked ? 1:0);
            AdBlocker_Helpers.checkStatus(getActivity());
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mStatusBarBrightnessControl) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(), STATUS_BAR_BRIGHTNESS_CONTROL,
                    value ? 1 : 0);
            return true;
        } else if (preference == mMsob) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.MEDIA_SCANNER_ON_BOOT,
                    Integer.valueOf(String.valueOf(newValue)));
            mMsob.setValue(String.valueOf(newValue));
            mMsob.setSummary(mMsob.getEntry());
            return true;
        } else if (preference == mEnableTaskManager) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.ENABLE_TASK_MANAGER, checked ? 1:0);
            return true;
        } else if  (preference == mFlashlightNotification) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                   Settings.System.FLASHLIGHT_NOTIFICATION, checked ? 1:0);
            return true;
        } else if (preference == mSelinux) {
            if (newValue.toString().equals("true")) {
                Log.d(TAG, "setenforce 1");
                CMDProcessor.runShellCommand("echo 1 > /sys/fs/selinux/enforce");
                mSelinux.setSummary(R.string.selinux_enforcing_title);
            } else if (newValue.toString().equals("false")) {
                Log.d(TAG, "setenforce 0");
                CMDProcessor.runShellCommand("echo 0 > /sys/fs/selinux/enforce");
                mSelinux.setSummary(R.string.selinux_permissive_title);
            }
            return true;
        } else if (preference == mScrollingCachePref) {
            if (newValue != null) {
                SystemProperties.set(SCROLLINGCACHE_PERSIST_PROP, (String)newValue);
            return true;
            }
        }
        return false;
    }
}
