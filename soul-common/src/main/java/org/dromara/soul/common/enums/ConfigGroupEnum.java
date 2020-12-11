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

package org.dromara.soul.common.enums;

import org.dromara.soul.common.exception.SoulException;

import java.util.Arrays;
import java.util.Objects;

/**
 * configuration group.
 *
 * @author huangxiaofeng
 */
public enum ConfigGroupEnum {

    /**
     * App auth config group enum.
     */
    APP_AUTH,

    /**
     * Plugin config group enum.
     */
    PLUGIN,

    /**
     * Rule config group enum.
     */
    RULE,

    /**
     * Selector config group enum.
     */
    SELECTOR,

    /**
     * Meta data config group enum.
     */
    META_DATA;

    /**
     * Acquire by name config group enum.
     *
     * @param name the name
     * @return the config group enum
     */
    public static ConfigGroupEnum acquireByName(final String name) {
        return Arrays.stream(ConfigGroupEnum.values())
                .filter(e -> Objects.equals(e.name(), name))
                .findFirst().orElseThrow(() -> new SoulException(" this ConfigGroupEnum can not support!"));
    }
}
