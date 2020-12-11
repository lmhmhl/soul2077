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

import com.netflix.hystrix.HystrixObservableCommand;
import java.net.URI;
import org.dromara.soul.plugin.api.SoulPluginChain;
import org.dromara.soul.plugin.base.utils.UriUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import rx.Observable;
import rx.RxReactiveStreams;

/**
 * the Hystrix command.
 *
 * @author xiaoyu(Myth)
 */
public class HystrixCommand extends HystrixObservableCommand<Void> implements Command {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HystrixCommand.class);

    private final ServerWebExchange exchange;

    private final SoulPluginChain chain;

    private final URI callBackUri;

    /**
     * Instantiates a new Http command.
     *
     * @param setter   the setter
     * @param exchange the exchange
     * @param chain    the chain
     */
    public HystrixCommand(final Setter setter,
                   final ServerWebExchange exchange,
                   final SoulPluginChain chain,
                   final String callBackUri) {

        super(setter);
        this.exchange = exchange;
        this.chain = chain;
        this.callBackUri = UriUtils.createUri(callBackUri);
    }

    @Override
    protected Observable<Void> construct() {
        return RxReactiveStreams.toObservable(chain.execute(exchange));
    }

    @Override
    protected Observable<Void> resumeWithFallback() {
        return RxReactiveStreams.toObservable(doFallback());
    }

    private Mono<Void> doFallback() {
        if (isFailedExecution()) {
            LOGGER.error("hystrix execute have error:", getExecutionException());
        }
        final Throwable exception = getExecutionException();
        return doFallback(exchange, exception);
    }

    @Override
    public Observable<Void> fetchObservable() {
        return this.toObservable();
    }

    @Override
    public URI getCallBackUri() {
        return callBackUri;
    }

}
