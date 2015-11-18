/*
 * Copyright (C) 2014 Dirty Unicorns
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.List;

public class AdBlocker_IntentReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";
    Context settingsContext = null;
    Boolean mSetupRunning = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for(int i = 0; i < procInfos.size(); i++)
        {
            if(procInfos.get(i).processName.equals("com.google.android.setupwizard")) {
                mSetupRunning = true;
            }
        }
        if(!mSetupRunning) {
             try {
                 settingsContext = context.createPackageContext("com.android.settings", 0);
             } catch (Exception e) {
                 Log.e(TAG, "Package not found", e);
             }
             Intent serv = new Intent(context, AdBlocker_CheckHosts.class);
             serv.setAction(intent.getAction());
             serv.putExtras(intent);
             context.startService(serv);
        }
    }
}
