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

package com.dirtyunicorns.tweaks.fragments.team;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.settings.R;

import java.util.ArrayList;
import java.util.List;

public class TeamActivity extends Activity {

    private List<DevInfoAdapter> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_recyclerview);

        initTeam();

        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
    }
    private void initTeam(){
        RecyclerView mRecycleview = findViewById(R.id.listView);

        setTeamMember("Alex", getString(R.string.developer_title), "Mazda--", "dualexcruz", R.drawable.alex);
        setTeamMember("Bret", getString(R.string.developer_title), "MazWoz", "MazWoz", R.drawable.bret);
        setTeamMember("Edwin", getString(R.string.developer_title)
                + " / " + getString(R.string.maintainer_title), "spaceman860", "spaceman860", R.drawable.edwin);
        setTeamMember("Ezio", getString(R.string.developer_title), "ezio84", "ezio_lacandia", R.drawable.ezio);
        setTeamMember("Francesco", getString(R.string.developer_title), "Dil3mm4", "Dil3mm4_ita", R.drawable.frank);
        setTeamMember("Giuseppe", getString(R.string.developer_title)
                + " / " + getString(R.string.maintainer_title), "Jertlok", "Jertlok", R.drawable.giuseppe);
        setTeamMember("James", getString(R.string.developer_title)
                + " / " + getString(R.string.server_admin_title), "JmzTaylor", "JmzAF", R.drawable.james);
        setTeamMember("Josh Chasky", getString(R.string.developer_title)
                + " / " + getString(R.string.maintainer_title), "nychitman1", "NYCHitman1", R.drawable.joshchasky);
        setTeamMember("Josh Correll", getString(R.string.developer_title)
                + " / " + getString(R.string.maintainer_title), "jbats", "jbatsDU", R.drawable.joshcorrell);
        setTeamMember("Josip", getString(R.string.developer_title), "nasty007", "josip1982", R.drawable.josip);
        setTeamMember("Mark", getString(R.string.developer_title), "moepda", "MoEpDa", R.drawable.mark);
        setTeamMember("Nathan", getString(R.string.developer_title), "nathanchance", "nathanchance", R.drawable.nathan);
        setTeamMember("Nick", getString(R.string.developer_title), "nickdoherty81", "nickdoh81", R.drawable.nick);
        setTeamMember("Nico", getString(R.string.developer_title), "Nico60", "NicolasNico60", R.drawable.nicolas);
        setTeamMember("Randall", getString(R.string.developer_title), "bigrushdog", "bigrushdog", R.drawable.randall);
        setTeamMember("Surge", getString(R.string.developer_title), "Surge1223", "Surge1223", R.drawable.surge);
        setTeamMember("Tushar", getString(R.string.developer_title)
                + " / " + getString(R.string.maintainer_title), "DeviousFusion", "DeviousFusion", R.drawable.tushar);
        setTeamMember("Will", getString(R.string.developer_title)
                + " / " + getString(R.string.maintainer_title), "flintman", "wbellavance", R.drawable.will);

        ListAdapter mAdapter = new ListAdapter(mList);
        mRecycleview.setAdapter(mAdapter);
        mRecycleview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();
    }

    private void setTeamMember(String devName, String devTitle,
                               String githubLink, String twitter, int devImage) {
        DevInfoAdapter adapter;

        adapter = new DevInfoAdapter();
        adapter.setImage(devImage);
        adapter.setDevName(devName);
        adapter.setDevTitle(devTitle);
        adapter.setGithubName(githubLink);
        adapter.setTwitterName(twitter);
        mList.add(adapter);
    }
}
