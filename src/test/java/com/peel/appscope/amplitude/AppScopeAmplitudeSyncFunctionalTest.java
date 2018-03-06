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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amplitude.api.Amplitude;
import com.amplitude.api.AmplitudeClient;
import com.google.gson.Gson;
import com.peel.appscope.AppScope;
import com.peel.appscope.TypedKey;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Functional tests to validate that {@code TypedKeyAmplitudeSynced} properties get synced correctly with amplitude user properties.
 *
 * @author Inderjeet Singh
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Context.class, SharedPreferences.class, Amplitude.class })
public class AppScopeAmplitudeSyncFunctionalTest {

    private AmplitudeClient amplitudeClient;
    private JSONObject userProperties;

    @Before
    public void setUp() throws Exception {
        Context context = AndroidFixtures.createMockContext();
        AppScope.TestAccess.init(context, new Gson());
        amplitudeClient = Mockito.mock(AmplitudeClient.class);
        Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                userProperties = (JSONObject) args[0]; // Just set userProperties
                return null;
            }
        }).when(amplitudeClient).setUserProperties(Mockito.any(JSONObject.class));
        AppScope.addListener(new AppScopeAmplitudeSyncListener(amplitudeClient));
    }

    @Test
    public void testAmplitudeSync() throws Exception {
        TypedKeyAmplitudeSynced<String> sync = new TypedKeyAmplitudeSynced<>("sync", String.class, false, false);
        AppScope.bind(sync, "test");
        assertEquals("test", userProperties.get("sync"));

        // assert that amplitude sync doesn't happen for non-synced properties
        TypedKey<String> nosync = new TypedKey<>("nosync", String.class, false, false);
        AppScope.bind(nosync, "test");
        assertFalse(userProperties.has("nosync"));
    }

    @Test
    public void testAmplitudeUnsetOnBooleanPropertyRemoval() throws Exception {
        TypedKeyAmplitudeSynced<Boolean> bool = new TypedKeyAmplitudeSynced<>("bool", Boolean.class, false, false);
        AppScope.bind(bool, true);
        assertTrue(userProperties.getBoolean("bool"));

        AppScope.remove(bool);
        assertFalse(userProperties.getBoolean("bool"));
    }
}
