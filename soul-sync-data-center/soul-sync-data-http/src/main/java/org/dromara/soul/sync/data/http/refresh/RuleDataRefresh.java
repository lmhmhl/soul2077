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

package org.dromara.soul.sync.data.http.refresh;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.dromara.soul.common.dto.ConfigData;
import org.dromara.soul.common.dto.RuleData;
import org.dromara.soul.common.enums.ConfigGroupEnum;
import org.dromara.soul.sync.data.api.PluginDataSubscriber;

/**
 * The type Rule data refresh.
 */
@Slf4j
@RequiredArgsConstructor
public class RuleDataRefresh extends AbstractDataRefresh<RuleData> {

    private final PluginDataSubscriber pluginDataSubscriber;

    @Override
    protected JsonObject convert(final JsonObject data) {
        return data.getAsJsonObject(ConfigGroupEnum.RULE.name());
    }

    @Override
    protected ConfigData<RuleData> fromJson(final JsonObject data) {
        return GSON.fromJson(data, new TypeToken<ConfigData<RuleData>>() {
        }.getType());
    }

    @Override
    protected boolean updateCacheIfNeed(final ConfigData<RuleData> result) {
        return updateCacheIfNeed(result, ConfigGroupEnum.RULE);
    }

    @Override
    public ConfigData<?> cacheConfigData() {
        return GROUP_CACHE.get(ConfigGroupEnum.RULE);
    }

    @Override
    protected void refresh(final List<RuleData> data) {
        if (CollectionUtils.isEmpty(data)) {
            log.info("clear all rule cache, old cache");
            data.forEach(pluginDataSubscriber::unRuleSubscribe);
            pluginDataSubscriber.refreshRuleDataAll();
        } else {
            // update cache for UpstreamCacheManager
            pluginDataSubscriber.refreshRuleDataAll();
            data.forEach(pluginDataSubscriber::onRuleSubscribe);
        }
    }
}
