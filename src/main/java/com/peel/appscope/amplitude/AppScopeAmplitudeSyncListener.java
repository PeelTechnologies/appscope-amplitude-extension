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

import org.json.JSONObject;

import com.amplitude.api.Amplitude;
import com.peel.appscope.AppScope;
import com.peel.appscope.TypedKey;

/**
 * Bind this listener to {@code AppScope#addListener(AppScope.EventListener)} to automatically sync all
 * properties of type {@code TypedKeyAmplitudeSynced} as Amplitude user properties
 *
 * @author Inderjeet Singh
 */
public final class AppScopeAmplitudeSyncListener implements AppScope.EventListener {
    @Override
    public <T> void onBind(TypedKey<T> key, T value) {
        if (key instanceof TypedKeyAmplitudeSynced) {
            try {
                JSONObject props = new JSONObject();
                props.put(key.getName(), value);
                Amplitude.getInstance().setUserProperties(props);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public <T> void onRemove(TypedKey<T> key) {
        try {
            if (key instanceof TypedKeyAmplitudeSynced && key.getTypeOfValue() == Boolean.class) {
                // Only for boolean keys, set them to false in Amplitude
                JSONObject props = new JSONObject();
                props.put(key.getName(), false);
                Amplitude.getInstance().setUserProperties(props);
            }
        } catch (Exception ignored) {
        }
    }
}
