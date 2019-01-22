/*
 * Copyright (C) 2019 The Dirty Unicorns Project
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

package com.dirtyunicorns.tweaks.preferences;

import android.content.Context;
import android.provider.Settings;
import android.util.AttributeSet;
import com.dirtyunicorns.support.preferences.IconPackPreference;

public class RecentsIconPackPreference extends IconPackPreference {

    public RecentsIconPackPreference(Context context) {
        super(context, null);
    }

    public RecentsIconPackPreference(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public RecentsIconPackPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * This method overrides Preference method so to store the information
     * inside RECENTS_ICON_PACK.
     *
     * @param value the packageName to store.
     * @return true if the Preference got stored, false otherwise.
     */
    @Override
    public boolean persistString(String value) {
        return Settings.System.putString(getContext().getContentResolver(),
                Settings.System.RECENTS_ICON_PACK, /* packageName */ value);
    }

    /**
     * This method overrides Preference method so to get the information
     * from RECENTS_ICON_PACK.
     *
     * @param value the default value to return if RECENTS_ICON_PACK is not available.
     * @return the value from the storage or the default return value.
     */
    @Override
    public String getPersistedString(String value) {
        String currentIconPack = Settings.System.getString(getContext().getContentResolver(),
                Settings.System.RECENTS_ICON_PACK);
        return currentIconPack == null ? value : currentIconPack;
    }
}