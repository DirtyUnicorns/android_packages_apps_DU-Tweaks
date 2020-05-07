/*
 * Copyright (C) 2020 The Dirty Unicorns Project
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
package com.dirtyunicorns.tweaks.fragments.navigation.customactions;

import android.content.pm.ActivityInfo;
import android.provider.Settings;

public class HomeDoubleTap extends BackLongPress {

    @Override
    protected void setPackage(String packageName, String friendlyAppString) {
        Settings.System.putString(
                getContentResolver(), Settings.System.KEY_HOME_DOUBLE_TAP_CUSTOM_APP, packageName);
        Settings.System.putString(
                getContentResolver(), Settings.System.KEY_HOME_DOUBLE_TAP_CUSTOM_APP_FR_NAME,
                friendlyAppString);
    }

    @Override
    protected void setPackageActivity(ActivityInfo ai) {
        Settings.System.putString(
                getContentResolver(), Settings.System.KEY_HOME_DOUBLE_TAP_CUSTOM_ACTIVITY,
                ai != null ? ai.name : "NONE");
    }
}

