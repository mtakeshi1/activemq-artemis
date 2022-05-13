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

public class ManagedMQTTConnectionManager extends MQTTConnectionManager {

    private final MQTTSession session;

    public ManagedMQTTConnectionManager(MQTTSession session) {
        super(session);
        this.session = session;
    }

    void disconnect(boolean failure) {
        if (session == null || session.getStopped()) {
            return;
        }

        MQTTSessionState state = session.getState();
        //stop before the sync
        synchronized (state) {
            ThreadCoordination.waitForStage(ThreadCoordination.ExecutionStage.STARTING, ThreadCoordination.ExecutionStage.FAILURE_DETECTOR_STATE_MONITOR_ACQUIRED);
            try {
                ThreadCoordination.waitForStage(ThreadCoordination.ExecutionStage.CONNECT_BEFORE_SESSION_START, ThreadCoordination.ExecutionStage.FAILURE_DETECTOR_BEFORE_STOP);
                session.stop(failure);
                session.getConnection().destroy();
            } catch (Exception e) {
                MQTTLogger.LOGGER.errorDisconnectingClient(e);
            } finally {
                if (session.getState() != null) {
                    String clientId = session.getState().getClientId();
                    /*
                     *  ensure that the connection for the client ID matches *this* connection otherwise we could remove the
                     *  entry for the client who "stole" this client ID via [MQTT-3.1.4-2]
                     */
                    if (clientId != null && session.getProtocolManager().isClientConnected(clientId, session.getConnection())) {
                        session.getProtocolManager().removeConnectedClient(clientId);
                    }
                }
            }
        }
    }

    @Override
    protected void beforeStart(MQTTSession session) {
        ThreadCoordination.waitForStage(ThreadCoordination.ExecutionStage.CONNECT_BEFORE_STATE_SWAP, ThreadCoordination.ExecutionStage.CONNECT_BEFORE_SESSION_START);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void beforeSessionSwap(MQTTSessionState sessionState) {
        ThreadCoordination.waitForStage(ThreadCoordination.ExecutionStage.FAILURE_DETECTOR_STATE_MONITOR_ACQUIRED, ThreadCoordination.ExecutionStage.CONNECT_BEFORE_STATE_SWAP);
    }
}
