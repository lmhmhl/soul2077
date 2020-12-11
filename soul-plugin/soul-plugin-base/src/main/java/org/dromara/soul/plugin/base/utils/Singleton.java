/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.soul.plugin.base.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton.
 *
 * @author xiaoyu
 */
public enum Singleton {

    /**
     * Inst singleton.
     */
    INST;

    /**
     * The Singles.
     */
    private static final Map<String, Object> SINGLES = new ConcurrentHashMap<>();

    /**
     * Single.
     *
     * @param clazz the clazz
     * @param o     the o
     */
    public void single(final Class clazz, final Object o) {
        SINGLES.put(clazz.getName(), o);
    }

    /**
     * Get t.
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final Class<T> clazz) {
        return (T) SINGLES.get(clazz.getName());
    }
}
