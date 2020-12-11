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

package org.dromara.soul.plugin.alibaba.dubbo.subscriber;

import com.google.common.collect.Maps;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import org.dromara.soul.common.dto.MetaData;
import org.dromara.soul.common.enums.RpcTypeEnum;
import org.dromara.soul.plugin.alibaba.dubbo.cache.ApplicationConfigCache;
import org.dromara.soul.sync.data.api.MetaDataSubscriber;

/**
 * The type Alibaba dubbo meta data subscriber.
 *
 * @author xiaoyu
 */
public class AlibabaDubboMetaDataSubscriber implements MetaDataSubscriber {
    
    private static final ConcurrentMap<String, MetaData> META_DATA = Maps.newConcurrentMap();
    
    @Override
    public void onSubscribe(final MetaData metaData) {
        if (RpcTypeEnum.DUBBO.getName().equals(metaData.getRpcType())) {
            MetaData exist = META_DATA.get(metaData.getPath());
            if (Objects.isNull(META_DATA.get(metaData.getPath())) || Objects.isNull(ApplicationConfigCache.getInstance().get(metaData.getPath()))) {
                //第一次初始化
                ApplicationConfigCache.getInstance().initRef(metaData);
            } else {
                //有更新,只支持serviceName rpcExt parameterTypes methodName四种属性的更新，因为这四种属性会影响dubbo的调用；
                if (!metaData.getServiceName().equals(exist.getServiceName())
                        || !metaData.getRpcExt().equals(exist.getRpcExt())
                        || !metaData.getParameterTypes().equals(exist.getParameterTypes())
                        || !metaData.getMethodName().equals(exist.getMethodName())) {
                    ApplicationConfigCache.getInstance().build(metaData);
                }
            }
            META_DATA.put(metaData.getPath(), metaData);
        }
    }
    
    @Override
    public void unSubscribe(final MetaData metaData) {
        if (RpcTypeEnum.DUBBO.getName().equals(metaData.getRpcType())) {
            ApplicationConfigCache.getInstance().invalidate(metaData.getPath());
            META_DATA.remove(metaData.getPath());
        }
    }
}
