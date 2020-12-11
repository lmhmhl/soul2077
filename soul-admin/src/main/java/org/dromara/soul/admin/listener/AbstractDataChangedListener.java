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

package org.dromara.soul.admin.listener;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections4.CollectionUtils;
import org.dromara.soul.admin.service.AppAuthService;
import org.dromara.soul.admin.service.MetaDataService;
import org.dromara.soul.admin.service.PluginService;
import org.dromara.soul.admin.service.RuleService;
import org.dromara.soul.admin.service.SelectorService;
import org.dromara.soul.common.dto.AppAuthData;
import org.dromara.soul.common.dto.ConfigData;
import org.dromara.soul.common.dto.MetaData;
import org.dromara.soul.common.dto.PluginData;
import org.dromara.soul.common.dto.RuleData;
import org.dromara.soul.common.dto.SelectorData;
import org.dromara.soul.common.enums.ConfigGroupEnum;
import org.dromara.soul.common.enums.DataEventTypeEnum;
import org.dromara.soul.common.utils.GsonUtils;
import org.dromara.soul.common.utils.Md5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * Abstract class for ConfigEventListener.
 * As we think that the md5 value of the in-memory data is the same as the md5 value of the database,
 * although it may be a little different, but it doesn't matter, we will have thread to periodica
 * pull the data in the database.
 *
 * @author huangxiaofeng
 * @since 2.0.0
 */
@SuppressWarnings("all")
public abstract class AbstractDataChangedListener implements DataChangedListener, InitializingBean {

    /**
     * The constant CACHE.
     */
    protected static final ConcurrentMap<String, ConfigDataCache> CACHE = new ConcurrentHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataChangedListener.class);

    @Resource
    private AppAuthService appAuthService;

    /**
     * The Plugin service.
     */
    @Resource
    private PluginService pluginService;

    /**
     * The Rule service.
     */
    @Resource
    private RuleService ruleService;

    /**
     * The Selector service.
     */
    @Resource
    private SelectorService selectorService;

    @Resource
    private MetaDataService metaDataService;

    /**
     * fetch configuration from database.
     *
     * @param groupKey the group key
     * @return the configuration data
     */
    public ConfigData<?> fetchConfig(final ConfigGroupEnum groupKey) {
        ConfigDataCache config = CACHE.get(groupKey.name());
        switch (groupKey) {
            case APP_AUTH:
                List<AppAuthData> appAuthList = GsonUtils.getGson().fromJson(config.getJson(), new TypeToken<List<AppAuthData>>() {
                }.getType());
                return new ConfigData<>(config.getMd5(), config.getLastModifyTime(), appAuthList);
            case PLUGIN:
                List<PluginData> pluginList = GsonUtils.getGson().fromJson(config.getJson(), new TypeToken<List<PluginData>>() {
                }.getType());
                return new ConfigData<>(config.getMd5(), config.getLastModifyTime(), pluginList);
            case RULE:
                List<RuleData> ruleList = GsonUtils.getGson().fromJson(config.getJson(), new TypeToken<List<RuleData>>() {
                }.getType());
                return new ConfigData<>(config.getMd5(), config.getLastModifyTime(), ruleList);
            case SELECTOR:
                List<SelectorData> selectorList = GsonUtils.getGson().fromJson(config.getJson(), new TypeToken<List<SelectorData>>() {
                }.getType());
                return new ConfigData<>(config.getMd5(), config.getLastModifyTime(), selectorList);
            case META_DATA:
                List<MetaData> metaList = GsonUtils.getGson().fromJson(config.getJson(), new TypeToken<List<MetaData>>() {
                }.getType());
                return new ConfigData<>(config.getMd5(), config.getLastModifyTime(), metaList);
            default:
                throw new IllegalStateException("Unexpected groupKey: " + groupKey);
        }
    }

    @Override
    public void onAppAuthChanged(final List<AppAuthData> changed, final DataEventTypeEnum eventType) {
        if (CollectionUtils.isEmpty(changed)) {
            return;
        }
        this.updateAppAuthCache();
        this.afterAppAuthChanged(changed, eventType);
    }

    @Override
    public void onMetaDataChanged(final List<MetaData> changed, final DataEventTypeEnum eventType) {
        if (CollectionUtils.isEmpty(changed)) {
            return;
        }
        this.updateMetaDataCache();
        this.afterMetaDataChanged(changed, eventType);
    }

    /**
     * After meta data changed.
     *
     * @param changed   the changed
     * @param eventType the event type
     */
    protected void afterMetaDataChanged(final List<MetaData> changed, final DataEventTypeEnum eventType) {
    }

    /**
     * After app auth changed.
     *
     * @param changed   the changed
     * @param eventType the event type
     */
    protected void afterAppAuthChanged(final List<AppAuthData> changed, final DataEventTypeEnum eventType) {
    }

    @Override
    public void onPluginChanged(final List<PluginData> changed, final DataEventTypeEnum eventType) {
        if (CollectionUtils.isEmpty(changed)) {
            return;
        }
        this.updatePluginCache();
        this.afterPluginChanged(changed, eventType);
    }

    /**
     * After plugin changed.
     *
     * @param changed   the changed
     * @param eventType the event type
     */
    protected void afterPluginChanged(final List<PluginData> changed, final DataEventTypeEnum eventType) {
    }

    @Override
    public void onRuleChanged(final List<RuleData> changed, final DataEventTypeEnum eventType) {
        if (CollectionUtils.isEmpty(changed)) {
            return;
        }
        this.updateRuleCache();
        this.afterRuleChanged(changed, eventType);
    }

    /**
     * After rule changed.
     *
     * @param changed   the changed
     * @param eventType the event type
     */
    protected void afterRuleChanged(final List<RuleData> changed, final DataEventTypeEnum eventType) {
    }

    @Override
    public void onSelectorChanged(final List<SelectorData> changed, final DataEventTypeEnum eventType) {
        if (CollectionUtils.isEmpty(changed)) {
            return;
        }
        this.updateSelectorCache();
        this.afterSelectorChanged(changed, eventType);
    }

    /**
     * After selector changed.
     *
     * @param changed   the changed
     * @param eventType the event type
     */
    protected void afterSelectorChanged(final List<SelectorData> changed, final DataEventTypeEnum eventType) {
    }

    @Override
    public final void afterPropertiesSet() {
        updateAppAuthCache();
        updatePluginCache();
        updateRuleCache();
        updateSelectorCache();
        updateMetaDataCache();
        afterInitialize();
    }

    protected abstract void afterInitialize();

    /**
     * if md5 is not the same as the original, then update lcoal cache.
     * @param group ConfigGroupEnum
     * @param data the new config data
     */
    protected <T> void updateCache(final ConfigGroupEnum group, final List<T> data) {
        String json = GsonUtils.getInstance().toJson(data);
        ConfigDataCache newVal = new ConfigDataCache(group.name(), json, Md5Utils.md5(json), System.currentTimeMillis());
        ConfigDataCache oldVal = CACHE.put(newVal.getGroup(), newVal);
        LOGGER.info("update config cache[{}], old:{}, updated:{}", group, oldVal, newVal);
    }

    /**
     * Update selector cache.
     */
    protected void updateSelectorCache() {
        this.updateCache(ConfigGroupEnum.SELECTOR, selectorService.listAll());
    }

    /**
     * Update rule cache.
     */
    protected void updateRuleCache() {
        this.updateCache(ConfigGroupEnum.RULE, ruleService.listAll());
    }

    /**
     * Update plugin cache.
     */
    protected void updatePluginCache() {
        this.updateCache(ConfigGroupEnum.PLUGIN, pluginService.listAll());
    }

    /**
     * Update app auth cache.
     */
    protected void updateAppAuthCache() {
        this.updateCache(ConfigGroupEnum.APP_AUTH, appAuthService.listAll());
    }

    /**
     * Update meta data cache.
     */
    protected void updateMetaDataCache() {
        this.updateCache(ConfigGroupEnum.META_DATA, metaDataService.listAll());
    }

}
