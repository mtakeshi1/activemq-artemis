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

import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.activemq.artemis.junit.EmbeddedActiveMQResource;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import static org.eclipse.paho.client.mqttv3.MqttConnectOptions.MQTT_VERSION_3_1_1;

public class MQTTConnectionDeadLockTest {

    public EmbeddedActiveMQResource jmsServer = new EmbeddedActiveMQResource("test-artemis-server-mqtt.xml");
    @Rule
    public RuleChain rulechain = RuleChain.outerRule(jmsServer);

    @Test
    public void testSimpleConnect() throws InterruptedException {
        try {
            final MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setServerURIs(new String[]{"tcp://localhost:1883"});
            options.setMqttVersion(MQTT_VERSION_3_1_1);
            options.setKeepAliveInterval(10);
            final ThreadFactory threadFactory = new DefaultThreadFactory("mqtt-client-exec");
            var executorService = new ScheduledThreadPoolExecutor(5, threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
            var mqttClient = new MqttClient("tcp://localhost:1883", "deadlock-tester", new MemoryPersistence(), executorService);
            mqttClient.setTimeToWait(-1);
            mqttClient.connect(options);
        } catch (MqttException e) {
            //this can fail if it's a bit faster than the server
        }
        ThreadCoordination.waitForStage(ThreadCoordination.ExecutionStage.FAILURE_DETECTOR_BEFORE_STOP, ThreadCoordination.ExecutionStage.FINISHED);
        Thread.sleep(1000);
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreads = threadMXBean.findMonitorDeadlockedThreads();
        if (deadlockedThreads != null) {
            ThreadInfo[] threadInfo = threadMXBean.getThreadInfo(deadlockedThreads, 20);
            System.err.println("Deadlock found. Stack trace follows: ");
            for (var ti : threadInfo) {
                System.err.println("-----------------------------------------------------");
                System.err.println("Thread: " + ti);
                Arrays.stream(ti.getStackTrace()).forEach(System.err::println);
            }
        }
        Assert.assertNull(deadlockedThreads);
    }

}
