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

package org.dromara.soul.plugin.ratelimiter.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.soul.common.enums.RedisModeEnum;

import java.io.Serializable;
import java.time.Duration;

/**
 * The rateLimiter configuration for redis.
 *
 * @author xiaoyu(Myth)
 */
@Data
@EqualsAndHashCode
public class RateLimiterConfig implements Serializable {

    private Integer database = 0;

    private String master;

    private String mode = RedisModeEnum.STANDALONE.getName();

    /**
     * 如果是集群或者哨兵模式;分隔.
     */
    private String url;

    /**
     * the password.
     */
    private String password;

    /**
     * Maximum number of "idle" connections in the pool. Use a negative value to
     * indicate an unlimited number of idle connections.
     */
    private int maxIdle = 8;

    /**
     * Target for the minimum number of idle connections to maintain in the pool. This
     * setting only has an effect if it is positive.
     */
    private int minIdle;

    /**
     * Maximum number of connections that can be allocated by the pool at a given
     * time. Use a negative value for no limit.
     */
    private int maxActive = 8;

    /**
     * Maximum amount of time a connection allocation should block before throwing an
     * exception when the pool is exhausted. Use a negative value to block
     * indefinitely.
     */
    private Duration maxWait = Duration.ofMillis(-1);
}
