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

import java.lang.reflect.Type;

import com.peel.appscope.AppScope;
import com.peel.appscope.TypedKey;

/**
 * A key of this type is kept automatically in sync with an Amplitude user property of the same name
 *
 * @author Inderjeet Singh
 */
public final class TypedKeyAmplitudeSynced<T> extends TypedKey<T> {

    public static final class Builder<R> extends TypedKey.Builder<R> {

        public Builder(String name, Type type) {
            super(name, type);
        }

        public Builder(String name, Class<R> clazz) {
            super(name, (Type) clazz);
        }

        /**
         * Configure that {@link AppScope#reset()} will not wipe out this key
         * @return the builder
         */
        @Override
        public Builder<R> survivesReset() {
            return (Builder<R>) super.survivesReset();
        }

        /**
         * Call this method to indicate that this key is to be persisted on disk.
         *
         * @return the builder
         */
        @Override
        public Builder<R> persist() {
            return (Builder<R>) super.persist();
        }

        /**
         * Call this method to indicate that this key is to be persisted on disk.
         *
         * @param cacheableInMemory whether this key/value can be cached in memory.
         *   If false, the key is only stored in prefs and reloaded from it every time.
         *   Most keys should set this to true.mv
         * @return the builder
         */
        @Override
        public Builder<R> persist(boolean cacheableInMemory) {
            return (Builder<R>) super.persist(persist);
        }

        @Override
        public TypedKeyAmplitudeSynced<R> build() {
            return new TypedKeyAmplitudeSynced<R>(type, name, config, persist, cacheableInMemory);
        }
    }

    public TypedKeyAmplitudeSynced(String name, Class<T> clazz, boolean config, boolean persist) {
        super(name, clazz, config, persist);
    }

    private TypedKeyAmplitudeSynced(Type type, String name, boolean config, boolean persist, boolean cacheableInMemory) {
        super(type, name, config, persist, cacheableInMemory);
    }
}
