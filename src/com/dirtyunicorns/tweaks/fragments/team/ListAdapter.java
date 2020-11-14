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

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.settings.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DevInfoAdapter> mList;

    public ListAdapter(List<DevInfoAdapter> list){
        super();
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.team_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        DevInfoAdapter itemAdapter = mList.get(position);

        ((ViewHolder) viewHolder).mDevImage.setImageResource(itemAdapter.getImage());
        ((ViewHolder) viewHolder).mDevName.setText(itemAdapter.getDevName());
        ((ViewHolder) viewHolder).mDevTitle.setText(itemAdapter.getDevTitle());
        ((ViewHolder) viewHolder).mGithubName.setText(itemAdapter.getGithubName());
        ((ViewHolder) viewHolder).mTwitterName.setText(itemAdapter.getTwitterName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView mDevImage;
        public ImageView mGithub;
        public ImageView mTwitter;
        public TextView mDevName;
        public TextView mDevTitle;
        public TextView mGithubName;
        public TextView mTwitterName;

        private Context mContext;

        public ViewHolder(View itemView) {
            super(itemView);

            mDevImage = itemView.findViewById(R.id.devImage);
            mDevName = itemView.findViewById(R.id.devName);
            mDevTitle = itemView.findViewById(R.id.devTitle);
            mGithub = itemView.findViewById(R.id.devGithub);
            mGithubName = itemView.findViewById(R.id.githubName);
            mTwitter = itemView.findViewById(R.id.devTwitter);
            mTwitterName = itemView.findViewById(R.id.twitterName);

            mContext = itemView.getContext();

            mGithub.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/" + mGithubName.getText()));
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            });

            mTwitter.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            mTwitterName.getText().equals("") ?
                                    Uri.parse("https://twitter.com/_DirtyUnicorns_") :
                                    Uri.parse("https://twitter.com/" + mTwitterName.getText()));
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
