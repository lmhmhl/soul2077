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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.soul.common.dto.ConfigData;
import org.dromara.soul.common.enums.ConfigGroupEnum;

/**
 * The type Abstract data refresh.
 *
 * @param <T> the type parameter
 */
@Slf4j
public abstract class AbstractDataRefresh<T> implements DataRefresh {

    /**
     * The Group cache.
     */
    protected static final ConcurrentMap<ConfigGroupEnum, ConfigData<?>> GROUP_CACHE = new ConcurrentHashMap<>();

    /**
     * The constant GSON.
     */
    protected static final Gson GSON = new Gson();

    /**
     * Convert json object.
     *
     * @param data the data
     * @return the json object
     */
    protected abstract JsonObject convert(JsonObject data);

    /**
     * From json config data.
     *
     * @param data the data
     * @return the config data
     */
    protected abstract ConfigData<T> fromJson(JsonObject data);

    /**
     * Refresh.
     *
     * @param data the data
     */
    protected abstract void refresh(List<T> data);

    @Override
    public Boolean refresh(final JsonObject data) {
        boolean updated = false;
        JsonObject jsonObject = convert(data);
        if (null != jsonObject) {
            ConfigData<T> result = fromJson(jsonObject);
            if (this.updateCacheIfNeed(result)) {
                updated = true;
                refresh(result.getData());
            }
        }
        return updated;
    }

    /**
     * Update cache if need boolean.
     *
     * @param result the result
     * @return the boolean
     */
    protected abstract boolean updateCacheIfNeed(ConfigData<T> result);

    /**
     * If the MD5 values are different and the last update time of the old data is less than
     * the last update time of the new data, the configuration cache is considered to have been changed.
     *
     * @param newVal    the lasted config
     * @param groupEnum the group enum
     * @return true : if need update
     */
    protected boolean updateCacheIfNeed(final ConfigData<T> newVal, final ConfigGroupEnum groupEnum) {
        // first init cache
        if (GROUP_CACHE.putIfAbsent(groupEnum, newVal) == null) {
            return true;
        }
        ResultHolder holder = new ResultHolder(false);
        GROUP_CACHE.merge(groupEnum, newVal, (oldVal, value) -> {
            // must compare the last update time
            if (!StringUtils.equals(oldVal.getMd5(), newVal.getMd5()) && oldVal.getLastModifyTime() < newVal.getLastModifyTime()) {
                log.info("update {} config: {}", groupEnum, newVal);
                holder.result = true;
                return newVal;
            }
            log.info("Get the same config, the [{}] config cache will not be updated, md5:{}", groupEnum, oldVal.getMd5());
            return oldVal;
        });
        return holder.result;
    }

    private static final class ResultHolder {

        private boolean result;

        /**
         * Instantiates a new Result holder.
         *
         * @param result the result
         */
        ResultHolder(final boolean result) {
            this.result = result;
        }
    }
}
