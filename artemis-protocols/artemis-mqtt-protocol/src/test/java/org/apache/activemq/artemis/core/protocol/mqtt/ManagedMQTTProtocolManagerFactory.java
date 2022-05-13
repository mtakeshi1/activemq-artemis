/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.artemis.core.protocol.mqtt;

import org.apache.activemq.artemis.api.core.BaseInterceptor;
import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.apache.activemq.artemis.spi.core.protocol.ProtocolManager;
import org.apache.activemq.artemis.utils.uri.BeanSupport;

import java.util.List;
import java.util.Map;

public class ManagedMQTTProtocolManagerFactory extends MQTTProtocolManagerFactory {
    @Override
    public ProtocolManager createProtocolManager(ActiveMQServer server,
                                                 final Map<String, Object> parameters,
                                                 List<BaseInterceptor> incomingInterceptors,
                                                 List<BaseInterceptor> outgoingInterceptors) throws Exception {

        BeanSupport.stripPasswords(parameters);
        MQTTProtocolManager obj = new ManagedMQTTProtocolManager(server, incomingInterceptors, outgoingInterceptors);
        return BeanSupport.setData(obj, parameters);
    }

    @Override
    public String[] getProtocols() {
        return new String[] {"MQTT-DEBUG"};
    }


}
