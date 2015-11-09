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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public final class AdBlocker_Helpers {
    private static final String TAG = "AdBlocker_Helpers";

    private AdBlocker_Helpers() {
        throw new AssertionError();
    }

    public static void checkStatus(Context context) {
        File defHosts = new File("/etc/hosts.og");
        File altHosts = new File("/etc/hosts.alt");
        File hosts = new File("/etc/hosts");
        try {
            if (Settings.System.getInt(context.getContentResolver(), Settings.System.ADBLOCKER_DISABLE_ADS, 0) == 1
                    && areFilesDifferent(hosts, altHosts)) {
                copyFiles(altHosts, hosts);
            } else if (Settings.System.getInt(context.getContentResolver(), Settings.System.ADBLOCKER_DISABLE_ADS, 0) == 0
                    && areFilesDifferent(hosts, defHosts)) {
                copyFiles(defHosts, hosts);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean areFilesDifferent(File file1, File file2) throws IOException {
        String cr1, cr2;
        BufferedReader br1 = getBufferedReader(file1);
        BufferedReader br2 = getBufferedReader(file2);
        while ((cr1 = br1.readLine()) != null) {
                if((cr2 = br2.readLine()) != null) {
                        if(cr1.equals(cr2)) {
                            continue;
                        }
                }
                return true;
        }

        return br2.readLine() != null;
    }

    private static BufferedReader getBufferedReader(File file) throws IOException {
            return new BufferedReader(new FileReader(file));
    }

    public static void RunAsRoot(String string) throws IOException {
        Process P = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(P.getOutputStream());
        os.writeBytes(string + "\n");
        os.writeBytes("exit\n");
        os.flush();
    }

    public static void copyFiles(File srcFile, File dstFile) throws IOException {
        if (srcFile.exists() && dstFile.exists()) {
            String cmd = "mount -o rw,remount /system"
                       + " && rm -f " + dstFile.getAbsolutePath()
                       + " && cp -f " + srcFile.getAbsolutePath() + " " + dstFile.getAbsolutePath()
                       + " && chmod 644 " + dstFile.getAbsolutePath()
                       + " ; mount -o ro,remount /system";
            RunAsRoot(cmd);
        }
    }
}
