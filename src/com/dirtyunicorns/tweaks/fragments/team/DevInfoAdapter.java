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

public class DevInfoAdapter {

    private int image;
    private String devName;
    private String devTitle;
    private String githubName;
    private String twitterName;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevTitle(String devTitle) {
        this.devTitle = devTitle;
    }

    public String getDevTitle() {
        return devTitle;
    }

    public void setGithubName(String githubName) {
        this.githubName = githubName;
    }

    public String getGithubName() {
        return githubName;
    }

    public void setTwitterName(String twitterName) {
        this.twitterName = twitterName;
    }

    public String getTwitterName() {
        return twitterName;
    }
}
