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

import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.apache.activemq.artemis.spi.core.protocol.ConnectionEntry;

import java.lang.reflect.Field;
import java.util.Collections;

public class ManagedMQTTProtocolHandler extends MQTTProtocolHandler {

    public ManagedMQTTProtocolHandler(ActiveMQServer server, MQTTProtocolManager protocolManager) {
        super(server, protocolManager);
    }

    void setConnection(MQTTConnection connection, ConnectionEntry entry) throws Exception {
        super.setConnection(connection, entry);
        Field sessionField = MQTTProtocolHandler.class.getDeclaredField("session");
        sessionField.setAccessible(true);
        MQTTSession session = (MQTTSession) sessionField.get(this);
        Field connectionManagerField = MQTTSession.class.getDeclaredField("mqttConnectionManager");
        connectionManagerField.setAccessible(true);
        MQTTConnectionManager existingConnectionManager = (MQTTConnectionManager) connectionManagerField.get(session);
        session.getConnection().setFailureListeners(Collections.emptyList());
        ManagedMQTTConnectionManager newConnectionManager = new ManagedMQTTConnectionManager(session);
        connectionManagerField.set(session, newConnectionManager);
//        this.session = new MQTTSession(this, connection, protocolManager, server.getConfiguration().getWildcardConfiguration());
    }

}
