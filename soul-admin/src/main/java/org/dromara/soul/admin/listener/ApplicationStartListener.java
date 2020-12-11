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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * ApplicationStartListener.
 *
 * @author xiaoyu
 */
@Component
public class ApplicationStartListener implements ApplicationListener<WebServerInitializedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartListener.class);

    @Override
    public void onApplicationEvent(final WebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        final String host = getHost();
        final String domain = System.getProperty("soul.httpPath");
        if (StringUtils.isBlank(domain)) {
            SoulDomain.getInstance()
                    .setHttpPath("http://" + String.join(":", host, String.valueOf(port)));
        } else {
            SoulDomain.getInstance()
                    .setHttpPath(domain);
        }
    }

    private String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOGGER.error("Get host error!", e);
            return "127.0.0.1";
        }
    }
}
