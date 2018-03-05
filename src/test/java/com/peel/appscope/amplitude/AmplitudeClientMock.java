/*
 * Copyright (C) 2018 Peel Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peel.appscope.amplitude;

import android.app.Application;
import android.content.Context;

import com.amplitude.api.AmplitudeClient;

import org.json.JSONObject;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * A mock for AmplitudeClient that collects events in a list instead of sending
 * them to Amplitude. It also collects user properties in a JSONObject.
 *
 * @author Inderjeet Singh
 */
public final class AmplitudeClientMock extends AmplitudeClient {
    private List<Pair<String, JSONObject>> events = new ArrayList<>();
    private JSONObject userProperties;

    @Override
    public void setUserProperties(final JSONObject userProps) {
        userProperties = userProps;
    }

    @Override
    public void logEvent(String eventType, JSONObject eventProperties) {
        events.add(Pair.create(eventType, eventProperties));
    }

    @Override
    public AmplitudeClient initialize(Context context, String apiKey, String userId) {
        return this;
    }

    @Override
    public AmplitudeClient enableForegroundTracking(Application app) {
        return this;
    }

    public void clearUserProperties() {
        userProperties = null;
    }

    public JSONObject getUserProperties() {
        return userProperties;
    }

    public List<Pair<String, JSONObject>> getEvents() {
        return events;
    }
}
