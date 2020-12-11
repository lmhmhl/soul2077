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

package org.dromara.soul.test.springcloud.controller;

import org.dromara.soul.client.springcloud.annotation.SoulSpringCloudClient;
import org.dromara.soul.test.springcloud.dto.OrderDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * OrderController.
 *
 * @author xiaoyu
 */
@RestController
@RequestMapping("/order")
@SoulSpringCloudClient(path = "/order")
public class OrderController {

    /**
     * Save order dto.
     *
     * @param orderDTO the order dto
     * @return the order dto
     */
    @PostMapping("/save")
    @SoulSpringCloudClient(path = "/save")
    public OrderDTO save(@RequestBody final OrderDTO orderDTO) {
        orderDTO.setName("hello world spring cloud save order");
        return orderDTO;
    }

    /**
     * Find by id order dto.
     *
     * @param id the id
     * @return the order dto
     */
    @GetMapping("/findById")
    @SoulSpringCloudClient(path = "/findById")
    public OrderDTO findById(@RequestParam("id") final String id) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        orderDTO.setName("hello world spring cloud findById");
        return orderDTO;
    }

    /**
     * Gets path variable.
     *
     * @param id   the id
     * @param name the name
     * @return the path variable
     */
    @GetMapping("/path/{id}/{name}")
    @SoulSpringCloudClient(path = "/path/**")
    public OrderDTO getPathVariable(@PathVariable("id") final String id, @PathVariable("name") final String name) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        orderDTO.setName("hello world spring cloud restful: " + name);
        return orderDTO;
    }

    /**
     * Test rest ful order dto.
     *
     * @param id the id
     * @return the order dto
     */
    @GetMapping("/path/{id}/name")
    @SoulSpringCloudClient(path = "/path/**/name")
    public OrderDTO testRestFul(@PathVariable("id") final String id) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        orderDTO.setName("hello world spring cloud restful inline " + id);
        return orderDTO;
    }
}
