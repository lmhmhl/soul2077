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

package org.dromara.soul.admin.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.dromara.soul.admin.dto.DashboardUserDTO;
import org.dromara.soul.common.utils.UUIDUtils;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * DashboardUserDO.
 *
 * @author jiangxiaofeng(Nicholas)
 */
@Data
public class DashboardUserDO extends BaseDO {

    /**
     * user name.
     */
    private String userName;

    /**
     * user password.
     */
    private String password;

    /**
     * dashboard role.
     */
    private Integer role;

    /**
     * whether enabled.
     */
    private Boolean enabled;

    /**
     * build dashboardUserDO.
     *
     * @param dashboardUserDTO {@linkplain DashboardUserDTO}
     * @return {@linkplain DashboardUserDO}
     */
    public static DashboardUserDO buildDashboardUserDO(final DashboardUserDTO dashboardUserDTO) {
        return Optional.ofNullable(dashboardUserDTO).map(item -> {
            DashboardUserDO dashboardUserDO = new DashboardUserDO();
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (StringUtils.isEmpty(item.getId())) {
                dashboardUserDO.setId(UUIDUtils.getInstance().generateShortUuid());
                dashboardUserDO.setEnabled(true);
                dashboardUserDO.setDateCreated(currentTime);
            } else {
                dashboardUserDO.setId(item.getId());
                dashboardUserDO.setEnabled(item.getEnabled());
            }
            dashboardUserDO.setUserName(item.getUserName());
            dashboardUserDO.setPassword(item.getPassword());
            dashboardUserDO.setRole(item.getRole());
            dashboardUserDO.setDateUpdated(currentTime);
            return dashboardUserDO;
        }).orElse(null);
    }
}
