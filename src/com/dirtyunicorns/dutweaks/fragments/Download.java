/*
 * Copyright (C) 2014 The Dirty Unicorns Project
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

import android.app.ActivityManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SeekBarPreference;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class Download extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    Preference mBanksGapps;
    Preference mArm64Gapps;
    Preference mPaGapps;
    Preference mTboGapps;
    Preference mTboClearGapps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.download);

        final ContentResolver resolver = getActivity().getContentResolver();

        mBanksGapps = findPreference("banks_gapps");
        mArm64Gapps = findPreference("arm64_gapps");
        mPaGapps = findPreference("pa_gapps");
        mTboGapps = findPreference("tbo_gapps");
        mTboClearGapps = findPreference("tbo_clear_gapps");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mBanksGapps) {
            Uri uri = Uri.parse("http://download.dirtyunicorns.com/files/gapps/banks_gapps/gapps-L-4-21-15.zip");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        } else if (preference == mArm64Gapps) {
            Uri uri = Uri.parse("http://download.dirtyunicorns.com/files/gapps/arm64/du-gapps-20150707-arm64-signed.zip");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        } else if (preference == mPaGapps) {
            Uri uri = Uri.parse("http://download.dirtyunicorns.com/files/gapps/pa_gapps/pa_gapps-stock-5.1-20150404-signed.zip");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        } else if (preference == mTboGapps) {
            Uri uri = Uri.parse("http://downloads.hostingsharedbox.com/spaceman/DU_TBO_Gapps/DU_TBO_GAPPS.zip");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        } else if (preference == mTboClearGapps) {
            Uri uri = Uri.parse("http://downloads.hostingsharedbox.com/spaceman/DU_TBO_Gapps/DU_TBO_GAPPS_CLEAR.zip");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object value) {
         return true;
    }

    public static class DeviceAdminLockscreenReceiver extends DeviceAdminReceiver {}

}
