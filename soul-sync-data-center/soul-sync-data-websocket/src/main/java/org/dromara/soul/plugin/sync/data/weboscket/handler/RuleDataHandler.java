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

package org.dromara.soul.plugin.sync.data.weboscket.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.dromara.soul.common.dto.RuleData;
import org.dromara.soul.common.utils.GsonUtils;
import org.dromara.soul.sync.data.api.PluginDataSubscriber;

/**
 * The type rule data handler.
 */
@RequiredArgsConstructor
public class RuleDataHandler extends AbstractDataHandler<RuleData> {

    private final PluginDataSubscriber pluginDataSubscriber;

    @Override
    public List<RuleData> convert(final String json) {
        return GsonUtils.getInstance().fromList(json, RuleData.class);
    }

    @Override
    protected void doRefresh(final List<RuleData> dataList) {
        pluginDataSubscriber.refreshRuleDataSelf(dataList);
        dataList.forEach(pluginDataSubscriber::onRuleSubscribe);
    }

    @Override
    protected void doUpdate(final List<RuleData> dataList) {
        dataList.forEach(pluginDataSubscriber::onRuleSubscribe);
    }

    @Override
    protected void doDelete(final List<RuleData> dataList) {
        dataList.forEach(pluginDataSubscriber::unRuleSubscribe);
    }
}
