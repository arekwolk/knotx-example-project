/*
 * Copyright (C) 2016 Cognifide Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.gateway;

import com.acme.gateway.impl.RequestProcessorKnotProxyImpl;
import io.knotx.gateway.GatewayKnotOptions;
import io.knotx.proxy.KnotProxy;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.serviceproxy.ServiceBinder;

public class RequestProcessorKnotVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestProcessorKnotVerticle.class);

  private GatewayKnotOptions configuration;

  private MessageConsumer<JsonObject> consumer;

  private ServiceBinder serviceBinder;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    this.configuration = new GatewayKnotOptions(config());
  }

  @Override
  public void start() throws Exception {
    LOGGER.info("Starting <{}>", this.getClass().getSimpleName());

    serviceBinder = new ServiceBinder(getVertx());
    consumer = serviceBinder
        .setAddress(configuration.getAddress())
        .register(KnotProxy.class, new RequestProcessorKnotProxyImpl());
  }

  @Override
  public void stop() throws Exception {
    serviceBinder.unregister(consumer);
  }
}
