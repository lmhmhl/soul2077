/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package org.dromara.soul.spring.boot.starter.plugin.sofa;

import org.dromara.soul.plugin.api.SoulPlugin;
import org.dromara.soul.plugin.api.sofa.SofaParamResolveService;
import org.dromara.soul.plugin.base.handler.PluginDataHandler;
import org.dromara.soul.plugin.sofa.SofaPlugin;
import org.dromara.soul.plugin.sofa.handler.SofaPluginDataHandler;
import org.dromara.soul.plugin.sofa.param.BodyParamPlugin;
import org.dromara.soul.plugin.sofa.proxy.SofaProxyService;
import org.dromara.soul.plugin.sofa.response.SofaResponsePlugin;
import org.dromara.soul.plugin.sofa.subscriber.SofaMetaDataSubscriber;
import org.dromara.soul.sync.data.api.MetaDataSubscriber;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type sofa plugin configuration.
 *
 * @author tydhot
 */
@Configuration
@ConditionalOnClass(SofaPlugin.class)
public class SofaPluginConfiguration {
    
    /**
     * Sofa plugin soul plugin.
     *
     * @param sofaParamResolveService the sofa param resolve service
     * @return the soul plugin
     */
    @Bean
    public SoulPlugin sofaPlugin(final ObjectProvider<SofaParamResolveService> sofaParamResolveService) {
        return new SofaPlugin(new SofaProxyService(sofaParamResolveService.getIfAvailable()));
    }
    
    /**
     * Body param plugin soul plugin.
     *
     * @return the soul plugin
     */
    @Bean
    public SoulPlugin bodyParamPlugin() {
        return new BodyParamPlugin();
    }
    
    /**
     * Dubbo response plugin soul plugin.
     *
     * @return the soul plugin
     */
    @Bean
    public SoulPlugin sofaResponsePlugin() {
        return new SofaResponsePlugin();
    }
    
    /**
     * Sofa plugin data handler plugin data handler.
     *
     * @return the plugin data handler
     */
    @Bean
    public PluginDataHandler sofaPluginDataHandler() {
        return new SofaPluginDataHandler();
    }
    
    /**
     * Sofa meta data subscriber meta data subscriber.
     *
     * @return the meta data subscriber
     */
    @Bean
    public MetaDataSubscriber sofaMetaDataSubscriber() {
        return new SofaMetaDataSubscriber();
    }
}
