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

package org.dromara.soul.springboot.starter.plugin.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webflux.exception.SentinelBlockExceptionHandler;
import org.dromara.soul.plugin.base.handler.PluginDataHandler;
import org.dromara.soul.plugin.sentinel.SentinelPlugin;
import org.dromara.soul.plugin.sentinel.handler.SentinelRuleHandle;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

/**
 * Sentinel plugin configuration.
 *
 * @author tydhot
 */
@Configuration
public class SentinelPluginConfiguration {

    /**
     * Sentinel plugin viewResolvers.
     */
    private final List<ViewResolver> viewResolvers;

    /**
     * Sentinel plugin serverCodecConfigurer.
     */
    private final ServerCodecConfigurer serverCodecConfigurer;

    /**
     * sentinelPluginConfiguration constructor.
     */
    public SentinelPluginConfiguration(final ObjectProvider<List<ViewResolver>> listObjectProvider, final ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = listObjectProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    /**
     * Sentinel plugin.
     *
     * @return the soul plugin
     */
    @Bean
    public SentinelPlugin sentinelPlugin() {
        return new SentinelPlugin();
    }

    /**
     * Sentinel plugin data handler plugin data handler.
     *
     * @return the plugin data handler
     */
    @Bean
    public PluginDataHandler sentinelRuleHandle() {
        return new SentinelRuleHandle();
    }

    /**
     * Sentinel exception handler.
     *
     * @return the soul plugin
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelBlockExceptionHandler sentinelBlockExceptionHandler() {
        return new SentinelBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }
}
