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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Functional tests to validate that {@code TypedKeyAmplitudeSynced} properties get synced correctly with amplitude user properties.
 *
 * @author Inderjeet Singh
 */
public class BuilderTest {

    @Test
    public void testPersistable() {
        TypedKeyAmplitudeSynced<String> key = new TypedKeyAmplitudeSynced.Builder<>("a", String.class)
                .persist()
                .build();
        assertTrue(key.isPersistable());
        assertFalse(key.isCacheableInMemory());
        assertFalse(key.isConfigType());
    }

    @Test
    public void testCacheableInMemory() {
        TypedKeyAmplitudeSynced<String> key = new TypedKeyAmplitudeSynced.Builder<>("a", String.class)
                .persist(false)
                .build();
        assertTrue(key.isPersistable());
        assertFalse(key.isCacheableInMemory());
        assertFalse(key.isConfigType());
    }

    @Test
    public void testConfigType() {
        TypedKeyAmplitudeSynced<String> key = new TypedKeyAmplitudeSynced.Builder<>("a", String.class)
                .survivesReset()
                .build();
        assertFalse(key.isPersistable());
        assertTrue(key.isCacheableInMemory());
        assertTrue(key.isConfigType());
    }
}
