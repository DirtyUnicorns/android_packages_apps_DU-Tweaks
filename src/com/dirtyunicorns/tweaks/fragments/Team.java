/*
 * Copyright (C) 2017-2018 The Dirty Unicorns Project
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dirtyunicorns.tweaks.R;

public class Team extends DialogFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext()
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
            setTeamMember("+AlexCruz", alex);

            FrameLayout bret = view.findViewById(R.id.bret);
            setTeamMember("+BretZamzow", bret);

            FrameLayout daniel = view.findViewById(R.id.daniel);
            setTeamMember("+DanielJohansson2000", daniel);

            FrameLayout edwin = view.findViewById(R.id.edwin);
            setTeamMember("+EdwinSPACEMANRivera", edwin);

            FrameLayout james = view.findViewById(R.id.james);
            setTeamMember("+JamesTaylor3", james);

            FrameLayout joshchasky = view.findViewById(R.id.joshchasky);
            setTeamMember("+JoshChasky", joshchasky);

            FrameLayout joshcorrell = view.findViewById(R.id.joshcorrell);
            setTeamMember("+JoshCorrell", joshcorrell);

            FrameLayout josip = view.findViewById(R.id.josip);
            setTeamMember("+JosipLazic", josip);

            FrameLayout mark = view.findViewById(R.id.mark);
            setTeamMember("104191856656335658539", mark);

            FrameLayout mat = view.findViewById(R.id.mat);
            setTeamMember("+MatPls", mat);

            FrameLayout nathan = view.findViewById(R.id.nathan);
            setTeamMember("+NathanChancellor", nathan);

            FrameLayout nick = view.findViewById(R.id.nick);
            setTeamMember("+NickDoherty81", nick);

            FrameLayout nicolas = view.findViewById(R.id.nicolas);
            setTeamMember("+NicolasDhouailly", nicolas);

            FrameLayout tushar = view.findViewById(R.id.tushar);
            setTeamMember("+TusharSaini91", tushar);

            FrameLayout will = view.findViewById(R.id.will);
            setTeamMember("107452708412699546248", will);
        }

        dialog.show();

        return dialog;
    }

    private void setTeamMember(final String gplus, final FrameLayout name) {
        if (name != null) {
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://plus.google.com/u/0/" + gplus));
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
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
