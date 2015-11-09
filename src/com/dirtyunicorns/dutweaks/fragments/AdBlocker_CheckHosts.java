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

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class AdBlocker_CheckHosts extends Service {

    private static final String TAG = "AdBlocker_CheckHosts";

    public static final String DISABLE_ADS = "disable_ads";

    private Context mContext;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mContext = this;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        AdBlocker_Helpers.checkStatus(mContext);
    }
}
