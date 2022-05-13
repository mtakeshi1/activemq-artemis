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

import java.util.concurrent.atomic.AtomicReference;

public class ThreadCoordination {

    public enum ExecutionStage {
        STARTING,
        FAILURE_DETECTOR_STATE_MONITOR_ACQUIRED,
        CONNECT_BEFORE_STATE_SWAP,
        CONNECT_BEFORE_SESSION_START,
        FAILURE_DETECTOR_BEFORE_STOP,
        FINISHED
    }

    private static final AtomicReference<ExecutionStage> CURRENT_STAGE = new AtomicReference<>(ExecutionStage.STARTING);

    public static void waitForStage(ExecutionStage expectedStage, ExecutionStage nextStage) {
        System.out.printf("[%s] - Waiting for stage: %s, currently at: %s%n", Thread.currentThread().getName(), expectedStage, CURRENT_STAGE.get());
        try {
            synchronized (CURRENT_STAGE) {
                while (CURRENT_STAGE.get() != expectedStage) {
                    CURRENT_STAGE.wait(100);
                }
                CURRENT_STAGE.set(nextStage);
                CURRENT_STAGE.notifyAll();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted", e);
        }
        System.out.printf("[%s] - Resumed from %s to %s %n", Thread.currentThread().getName(), expectedStage, nextStage);
    }

}
