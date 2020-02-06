/*
 * Copyright (C) 2017-2019 The Dirty Unicorns Project
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

package com.dirtyunicorns.tweaks.fragments;

import androidx.annotation.NonNull;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.fragment.app.DialogFragment;

import com.dirtyunicorns.tweaks.R;

import com.android.internal.util.du.Utils;

import java.util.Objects;

public class Team extends DialogFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity(), R.style.TeamDialogTheme);
        LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(getContext())
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup nullParent = null;
        View view = null;
        if (inflater != null) {
            view = inflater.inflate(R.layout.teamdialog, nullParent);
        }
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        if (view != null) {
            FrameLayout alex = view.findViewById(R.id.alex);
            setTeamMember("Mazda--", alex);

            FrameLayout bret = view.findViewById(R.id.bret);
            setTeamMember("MazWoz", bret);

            FrameLayout edwin = view.findViewById(R.id.edwin);
            setTeamMember("spaceman860", edwin);

            FrameLayout ezio = view.findViewById(R.id.ezio);
            setTeamMember("ezio84", ezio);

            FrameLayout giuseppe = view.findViewById(R.id.giuseppe);
            setTeamMember("Jertlok", giuseppe);

            FrameLayout james = view.findViewById(R.id.james);
            setTeamMember("JmzTaylor", james);

            FrameLayout joshchasky = view.findViewById(R.id.joshchasky);
            setTeamMember("nychitman1", joshchasky);

            FrameLayout joshcorrell = view.findViewById(R.id.joshcorrell);
            setTeamMember("jbats", joshcorrell);

            FrameLayout josip = view.findViewById(R.id.josip);
            setTeamMember("nasty007", josip);

            FrameLayout mark = view.findViewById(R.id.mark);
            setTeamMember("moepda", mark);

            FrameLayout nathan = view.findViewById(R.id.nathan);
            setTeamMember("nathanchance", nathan);

            FrameLayout nick = view.findViewById(R.id.nick);
            setTeamMember("nickdoherty81", nick);

            FrameLayout nicolas = view.findViewById(R.id.nicolas);
            setTeamMember("Nico60", nicolas);

            FrameLayout surge = view.findViewById(R.id.surge);
            setTeamMember("Surge1223", surge);

            FrameLayout tushar = view.findViewById(R.id.tushar);
            setTeamMember("DeviousFusion", tushar);

            FrameLayout will = view.findViewById(R.id.will);
            setTeamMember("flintman", will);
        }

        dialog.show();

        return dialog;
    }

    private void setTeamMember(final String github, final FrameLayout name) {
        if (Utils.isConnected(getContext())) {
            if (name != null) {
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://github.com/" + github));
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onDestroyView() {
        if(getDialog()!=null && getRetainInstance()) {
            getDialog().setDismissMessage(null);

        }
        super.onDestroyView();
    }
}
