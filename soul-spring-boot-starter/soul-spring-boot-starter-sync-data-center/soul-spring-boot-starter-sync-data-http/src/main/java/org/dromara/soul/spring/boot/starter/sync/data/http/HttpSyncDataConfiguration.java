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

package org.dromara.soul.spring.boot.starter.sync.data.http;

import lombok.extern.slf4j.Slf4j;
import org.dromara.soul.sync.data.api.AuthDataSubscriber;
import org.dromara.soul.sync.data.api.MetaDataSubscriber;
import org.dromara.soul.sync.data.api.PluginDataSubscriber;
import org.dromara.soul.sync.data.api.SyncDataService;
import org.dromara.soul.sync.data.http.HttpSyncDataService;
import org.dromara.soul.sync.data.http.config.HttpConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Http sync data configuration for spring boot.
 *
 * @author xiaoyu(Myth)
 */
@Configuration
@ConditionalOnClass(HttpSyncDataService.class)
@ConditionalOnProperty(prefix = "soul.sync.http", name = "url")
@Slf4j
public class HttpSyncDataConfiguration {

    /**
     * Http sync data service.
     *
     * @param httpConfig        the http config
     * @param pluginSubscriber the plugin subscriber
     * @param metaSubscribers   the meta subscribers
     * @param authSubscribers   the auth subscribers
     * @return the sync data service
     */
    @Bean
    public SyncDataService httpSyncDataService(final ObjectProvider<HttpConfig> httpConfig, final ObjectProvider<PluginDataSubscriber> pluginSubscriber,
                                           final ObjectProvider<List<MetaDataSubscriber>> metaSubscribers, final ObjectProvider<List<AuthDataSubscriber>> authSubscribers) {
        log.info("you use http long pull sync soul data");
        return new HttpSyncDataService(Objects.requireNonNull(httpConfig.getIfAvailable()), Objects.requireNonNull(pluginSubscriber.getIfAvailable()),
                metaSubscribers.getIfAvailable(Collections::emptyList), authSubscribers.getIfAvailable(Collections::emptyList));
    }

    /**
     * Http config http config.
     *
     * @return the http config
     */
    @Bean
    @ConfigurationProperties(prefix = "soul.sync.http")
    public HttpConfig httpConfig() {
        return new HttpConfig();
    }
}
