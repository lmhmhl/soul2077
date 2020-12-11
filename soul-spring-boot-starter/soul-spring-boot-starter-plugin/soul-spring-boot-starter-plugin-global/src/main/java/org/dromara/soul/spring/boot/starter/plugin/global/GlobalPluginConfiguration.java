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

package org.dromara.soul.spring.boot.starter.plugin.global;

import org.dromara.soul.plugin.api.SoulPlugin;
import org.dromara.soul.plugin.api.context.SoulContextBuilder;
import org.dromara.soul.plugin.global.DefaultSoulContextBuilder;
import org.dromara.soul.plugin.global.GlobalPlugin;
import org.dromara.soul.plugin.global.subsciber.MetaDataAllSubscriber;
import org.dromara.soul.sync.data.api.MetaDataSubscriber;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Global plugin configuration.
 *
 * @author xiaoyu(Myth)
 */
@Configuration
@ConditionalOnClass(GlobalPlugin.class)
public class GlobalPluginConfiguration {

    /**
     * Global plugin soul plugin.
     *
     * @param soulContextBuilder the soul context builder
     * @return the soul plugin
     */
    @Bean
    public SoulPlugin globalPlugin(final SoulContextBuilder soulContextBuilder) {
        return new GlobalPlugin(soulContextBuilder);
    }

    /**
     * Soul context builder soul context builder.
     *
     * @return the soul context builder
     */
    @Bean
    @ConditionalOnMissingBean(value = SoulContextBuilder.class, search = SearchStrategy.ALL)
    public SoulContextBuilder soulContextBuilder() {
        return new DefaultSoulContextBuilder();
    }

    /**
     * Data subscriber meta data subscriber.
     *
     * @return the meta data subscriber
     */
    @Bean
    public MetaDataSubscriber metaDataAllSubscriber() {
        return new MetaDataAllSubscriber();
    }
}
