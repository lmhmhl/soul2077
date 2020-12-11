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

package org.dromara.soul.plugin.hystrix.command;

import com.netflix.hystrix.exception.HystrixRuntimeException;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import java.net.URI;
import java.util.Objects;
import org.dromara.soul.plugin.api.result.SoulResultEnum;
import org.dromara.soul.plugin.base.utils.SoulResultWrap;
import org.dromara.soul.plugin.base.utils.SpringBeanUtils;
import org.dromara.soul.plugin.base.utils.WebFluxResultUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import rx.Observable;

/**
 * hystrix command for semaphore and thread.
 *
 * @author liangziqiang
 */
public interface Command {
    /**
     * wrap fetch Observable in {@link HystrixCommand} and {@link HystrixCommandOnThread}.
     *
     * @return {@code Observable<R>} that executes and calls back with the result of command execution
     *         or a fallback if the command fails for any reason.
     */
    Observable<Void> fetchObservable();

    /**
     * whether the 'circuit-breaker' is open.
     *
     * @return boolean
     */
    boolean isCircuitBreakerOpen();

    /**
     * generate a error when some error occurs.
     *
     * @param exchange  the exchange
     * @param exception exception instance
     * @return error which be wrapped by {@link SoulResultWrap}
     */
    default Object generateError(ServerWebExchange exchange, Throwable exception) {
        Object error;
        if (exception instanceof HystrixRuntimeException) {
            HystrixRuntimeException e = (HystrixRuntimeException) exception;
            if (e.getFailureType() == HystrixRuntimeException.FailureType.TIMEOUT) {
                exchange.getResponse().setStatusCode(HttpStatus.GATEWAY_TIMEOUT);
                error = SoulResultWrap.error(SoulResultEnum.SERVICE_TIMEOUT.getCode(), SoulResultEnum.SERVICE_TIMEOUT.getMsg(), null);
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                error = SoulResultWrap.error(SoulResultEnum.SERVICE_RESULT_ERROR.getCode(), SoulResultEnum.SERVICE_RESULT_ERROR.getMsg(), null);
            }
        } else if (exception instanceof HystrixTimeoutException) {
            exchange.getResponse().setStatusCode(HttpStatus.GATEWAY_TIMEOUT);
            error = SoulResultWrap.error(SoulResultEnum.SERVICE_TIMEOUT.getCode(), SoulResultEnum.SERVICE_TIMEOUT.getMsg(), null);
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            error = SoulResultWrap.error(SoulResultEnum.SERVICE_RESULT_ERROR.getCode(), SoulResultEnum.SERVICE_RESULT_ERROR.getMsg(), null);
        }
        return error;
    }

    /**
     * do fall back when some error occurs on hystrix execute.
     * @param exchange {@link ServerWebExchange}
     * @param exception {@link Throwable}
     * @return {@code Mono<Void>} to indicate when request processing is complete.
     */
    default Mono<Void> doFallback(ServerWebExchange exchange, Throwable exception) {
        if (Objects.isNull(getCallBackUri())) {
            Object error;
            error = generateError(exchange, exception);
            return WebFluxResultUtils.result(exchange, error);
        }
        DispatcherHandler dispatcherHandler =
            SpringBeanUtils.getInstance().getBean(DispatcherHandler.class);
        ServerHttpRequest request = exchange.getRequest().mutate().uri(getCallBackUri()).build();
        ServerWebExchange mutated = exchange.mutate().request(request).build();
        return dispatcherHandler.handle(mutated);
    }

    /**
     * get call back uri.
     * @return when some error occurs in hystrix invoke it will forward to this
     */
    URI getCallBackUri();

}
