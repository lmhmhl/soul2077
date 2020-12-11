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

package org.dromara.soul.springboot.starter.plugin.waf;

import org.dromara.soul.plugin.api.SoulPlugin;
import org.dromara.soul.plugin.base.handler.PluginDataHandler;
import org.dromara.soul.plugin.waf.WafPlugin;
import org.dromara.soul.plugin.waf.handler.WafPluginDataHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Waf plugin configuration.
 *
 * @author xiaoyu
 */
@Configuration
public class WafPluginConfiguration {
    
    /**
     * Waf plugin soul plugin.
     *
     * @return the soul plugin
     */
    @Bean
    public SoulPlugin wafPlugin() {
        return new WafPlugin();
    }
    
    /**
     * Waf plugin data handler plugin data handler.
     *
     * @return the plugin data handler
     */
    @Bean
    public PluginDataHandler wafPluginDataHandler() {
        return new WafPluginDataHandler();
    }
}
