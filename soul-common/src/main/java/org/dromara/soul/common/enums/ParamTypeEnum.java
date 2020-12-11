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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dromara.soul.common.exception.SoulException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Param Type.
 *
 * @author xiaoyu(Myth)
 */
@RequiredArgsConstructor
@Getter
public enum ParamTypeEnum {

    /**
     * Post param type enum.
     */
    POST("post", true),

    /**
     * Uri param type enum.
     */
    URI("uri", true),

    /**
     * Query param type enum.
     */
    QUERY("query", true),

    /**
     * Host param type enum.
     */
    HOST("host", true),

    /**
     * Ip param type enum.
     */
    IP("ip", true),

    /**
     * Header param type enum.
     */
    HEADER("header", true);

    private final String name;

    private final Boolean support;

    /**
     * acquire param type supports.
     *
     * @return param type support.
     */
    public static List<ParamTypeEnum> acquireSupport() {
        return Arrays.stream(ParamTypeEnum.values())
                .filter(e -> e.support).collect(Collectors.toList());
    }

    /**
     * get param type enum by name.
     *
     * @param name param type name.
     * @return param type enum.
     */
    public static ParamTypeEnum getParamTypeEnumByName(final String name) {
        return Arrays.stream(ParamTypeEnum.values())
                .filter(e -> e.getName().equals(name) && e.support).findFirst()
                .orElseThrow(() -> new SoulException(" this  param type can not support!"));
    }
}
