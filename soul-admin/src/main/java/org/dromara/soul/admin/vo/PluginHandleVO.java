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

package org.dromara.soul.admin.vo;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dromara.soul.admin.entity.PluginHandleDO;

/**
 * this is plugin handle view to web front.
 *
 * @author liangziqiang
 * @author dengliming
 */
@Data
@AllArgsConstructor
public class PluginHandleVO implements Serializable {

    /**
     * primary key.
     */
    private String id;


    /**
     * plugin id.
     */
    private String pluginId;

    /**
     * the attribute name.
     */
    private String field;

    /**
     * the attribute label.
     */
    private String label;

    /**
     * the data type.
     * 1 indicates number
     * 2 indicates string
     * 3 indicates select box
     */
    private String dataType;

    /**
     * created time.
     */
    private String dateCreated;

    /**
     * updated time.
     */
    private String dateUpdated;

    private List<SoulDictVO> dictOptions;

    /**
     * build {@linkplain PluginHandleVO}.
     *
     * @param pluginHandleDO {@linkplain PluginHandleDO}
     * @param dictOptions dictOptions
     * @return {@linkplain PluginHandleVO}
     */
    public static PluginHandleVO buildPluginHandleVO(final PluginHandleDO pluginHandleDO, final List<SoulDictVO> dictOptions) {
        if (Objects.isNull(pluginHandleDO)) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new PluginHandleVO(pluginHandleDO.getId(), pluginHandleDO.getPluginId(),
                pluginHandleDO.getField(), pluginHandleDO.getLabel(),
                String.valueOf(pluginHandleDO.getDataType()), dateTimeFormatter.format(pluginHandleDO.getDateCreated().toLocalDateTime()),
                dateTimeFormatter.format(pluginHandleDO.getDateUpdated().toLocalDateTime()), dictOptions);
    }
}
