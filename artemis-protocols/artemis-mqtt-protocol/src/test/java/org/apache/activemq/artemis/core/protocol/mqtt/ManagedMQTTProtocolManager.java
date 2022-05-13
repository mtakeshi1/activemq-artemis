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

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import org.apache.activemq.artemis.api.core.BaseInterceptor;
import org.apache.activemq.artemis.core.server.ActiveMQServer;

import java.util.List;

public class ManagedMQTTProtocolManager extends MQTTProtocolManager {

    private final ActiveMQServer server;

    ManagedMQTTProtocolManager(ActiveMQServer server, List<BaseInterceptor> incomingInterceptors, List<BaseInterceptor> outgoingInterceptors) {
        super(server, incomingInterceptors, outgoingInterceptors);
        this.server = server;
    }

    @Override
    public int getServerKeepAlive() {
        return 2;
    }

    @Override
    public void addChannelHandlers(ChannelPipeline pipeline) {
        pipeline.addLast(MqttEncoder.INSTANCE);
        /*
         * If we use the value from getMaximumPacketSize() here anytime a client sends a packet that's too large it
         * will receive a DISCONNECT with a reason code of 0x81 instead of 0x95 like it should according to the spec.
         * See https://docs.oasis-open.org/mqtt/mqtt/v5.0/os/mqtt-v5.0-os.html#_Toc3901086:
         *
         *   If a Server receives a packet whose size exceeds this limit, this is a Protocol Error, the Server uses
         *   DISCONNECT with Reason Code 0x95 (Packet too large)...
         *
         * Therefore we check manually in org.apache.activemq.artemis.core.protocol.mqtt.MQTTProtocolHandler.handlePublish
         */
        pipeline.addLast(new MqttDecoder(MQTTUtil.MAX_PACKET_SIZE));

        pipeline.addLast(new ManagedMQTTProtocolHandler(server, this));
    }


}
