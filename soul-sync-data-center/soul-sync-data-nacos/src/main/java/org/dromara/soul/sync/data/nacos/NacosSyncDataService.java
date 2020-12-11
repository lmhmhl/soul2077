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

package org.dromara.soul.sync.data.nacos;

import com.alibaba.nacos.api.config.ConfigService;
import org.dromara.soul.sync.data.api.AuthDataSubscriber;
import org.dromara.soul.sync.data.api.MetaDataSubscriber;
import org.dromara.soul.sync.data.api.PluginDataSubscriber;
import org.dromara.soul.sync.data.api.SyncDataService;
import org.dromara.soul.sync.data.nacos.handler.NacosCacheHandler;

import java.util.List;

/**
 * The type Nacos sync data service.
 *
 * @author Chenxj
 * @author xiaoyu
 */
public class NacosSyncDataService extends NacosCacheHandler implements AutoCloseable, SyncDataService {

    /**
     * Instantiates a new Nacos sync data service.
     *
     * @param configService         the config service
     * @param pluginDataSubscriber the plugin data subscriber
     * @param metaDataSubscribers   the meta data subscribers
     * @param authDataSubscribers   the auth data subscribers
     */
    public NacosSyncDataService(final ConfigService configService, final PluginDataSubscriber pluginDataSubscriber,
                                final List<MetaDataSubscriber> metaDataSubscribers, final List<AuthDataSubscriber> authDataSubscribers) {

        super(configService, pluginDataSubscriber, metaDataSubscribers, authDataSubscribers);
        start();
    }

    /**
     * Start.
     */
    public void start() {
        watcherData(PLUGIN_DATA_ID, this::updatePluginMap);
        watcherData(SELECTOR_DATA_ID, this::updateSelectorMap);
        watcherData(RULE_DATA_ID, this::updateRuleMap);
        watcherData(META_DATA_ID, this::updateMetaDataMap);
        watcherData(AUTH_DATA_ID, this::updateAuthMap);
    }

    @Override
    public void close() {
        LISTENERS.forEach((dataId, lss) -> {
            lss.forEach(listener -> getConfigService().removeListener(dataId, GROUP, listener));
            lss.clear();
        });
        LISTENERS.clear();
    }
}
