/*
 * Copyright (C) 2014-2016 The Dirty Unicorns Project
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

package com.dirtyunicorns.dutweaks.tabs;

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;

import android.provider.Settings;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.internal.util.du.DuUtils;
import com.android.settings.Utils;

public class System extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "System";

    private static final String KEY_MOTOACTIONS = "motoactions";
    private static final String KEY_MOTO_ACTIONS_PACKAGE_NAME = "com.dirtyunicorns.settings.device";

    private static final String KEY_ONEPLUSDOZE = "oneplusdoze";
    private static final String KEY_ONEPLUS_DOZE_PACKAGE_NAME = "com.cyanogenmod.settings.doze";
    private PreferenceScreen mMotoActions;
    private PreferenceScreen mOneplusDoze;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.system);

        ContentResolver resolver = getActivity().getContentResolver();
        PreferenceScreen prefSet = getPreferenceScreen();

        mMotoActions = (PreferenceScreen) findPreference(KEY_MOTOACTIONS);
        if (!DuUtils.isPackageInstalled(getActivity(), KEY_MOTO_ACTIONS_PACKAGE_NAME)) {
            prefSet.removePreference(mMotoActions);
        }
        mOneplusDoze = (PreferenceScreen) findPreference(KEY_ONEPLUSDOZE);
        if (!DuUtils.isPackageInstalled(getActivity(), KEY_ONEPLUS_DOZE_PACKAGE_NAME)) {
            prefSet.removePreference(mOneplusDoze);
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
        final String key = preference.getKey();
        return true;
    }

}

