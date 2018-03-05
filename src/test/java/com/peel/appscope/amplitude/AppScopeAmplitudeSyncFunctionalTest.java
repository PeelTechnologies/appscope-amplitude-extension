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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.rule.PowerMockRule;

import com.amplitude.api.Amplitude;
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
@PrepareForTest({ Context.class, SharedPreferences.class })
public class AppScopeAmplitudeSyncFunctionalTest {

    @Rule // Needed to make PowerMockito work
    public PowerMockRule rule = new PowerMockRule();

    private AmplitudeClientMock amplitudeClient;

    @Before
    public void setUp() throws Exception {
        Context context = AndroidFixtures.createMockContext();
        AppScope.TestAccess.init(context, new Gson());
        PowerMockito.mockStatic(Amplitude.class);
        this.amplitudeClient = new AmplitudeClientMock();
        PowerMockito.when(Amplitude.getInstance()).thenReturn(amplitudeClient);
    }

    @Test
    public void testAmplitudeSync() throws Exception {
        TypedKeyAmplitudeSynced<String> sync = new TypedKeyAmplitudeSynced<>("sync", String.class, false, false);
        AppScope.bind(sync, "test");
        assertEquals("test", (String) amplitudeClient.getUserProperties().get("sync"));

        // assert that amplitude sync doesn't happen for non-synced properties
        TypedKey<String> nosync = new TypedKey<>("nosync", String.class, false, false);
        AppScope.bind(nosync, "test");
        assertFalse(amplitudeClient.getUserProperties().has("nosync"));
    }

    @Test
    public void testAmplitudeUnsetOnBooleanPropertyRemoval() throws Exception {
        TypedKeyAmplitudeSynced<Boolean> bool = new TypedKeyAmplitudeSynced<>("bool", Boolean.class, false, false);
        AppScope.bind(bool, true);
        assertTrue((Boolean) amplitudeClient.getUserProperties().get("bool"));

        AppScope.remove(bool);
        assertFalse((Boolean) amplitudeClient.getUserProperties().get("bool"));
    }
}
