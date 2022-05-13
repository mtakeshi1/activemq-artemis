/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.artemis.service.extensions.xa.recovery;

import org.apache.activemq.artemis.api.core.client.ClientSessionFactory;
import org.apache.activemq.artemis.logprocessor.CodeFactory;
import org.apache.activemq.artemis.logprocessor.annotation.Cause;
import org.apache.activemq.artemis.logprocessor.annotation.LogBundle;
import org.apache.activemq.artemis.logprocessor.annotation.LogMessage;
import org.w3c.dom.Node;

/**
 * Logger Code 17
 */
@LogBundle(projectCode = "AMQ")
public interface ActiveMQXARecoveryLogger {

   ActiveMQXARecoveryLogger LOGGER = CodeFactory.getCodeClass(ActiveMQXARecoveryLogger.class, ActiveMQXARecoveryLogger.class.getPackage().getName());

   @LogMessage(id = 171003, value = "JMS Server Manager Running cached command for {0}", level = LogMessage.Level.INFO)
   void serverRunningCachedCommand(Runnable run);

   @LogMessage(id = 171004, value = "JMS Server Manager Caching command for {0} since the JMS Server is not active.",
      level = LogMessage.Level.INFO)
   void serverCachingCommand(Object runnable);

   @LogMessage(id = 172005, value = "Invalid \"host\" value \"0.0.0.0\" detected for \"{0}\" connector. Switching to \"{1}\". If this new address is incorrect please manually configure the connector to use the proper one.",
      level = LogMessage.Level.WARN)
   void invalidHostForConnector(String name, String newHost);

   @LogMessage(id = 172007, value = "Queue {0} does not exist on the topic {1}. It was deleted manually probably.", level = LogMessage.Level.WARN)
   void noQueueOnTopic(String queueName, String name);

   @LogMessage(id = 172008, value = "XA Recovery can not connect to any broker on recovery {0}", level = LogMessage.Level.WARN)
   void recoveryConnectFailed(String s);

   @LogMessage(id = 172011, value = "error unbinding {0} from JNDI", level = LogMessage.Level.WARN)
   void jndiUnbindError(@Cause Exception e, String key);

   @LogMessage(id = 172012, value = "JMS Server Manager error", level = LogMessage.Level.WARN)
   void jmsServerError(@Cause Exception e);

   @LogMessage(id = 172013, value = "Error in XA Recovery recover", level = LogMessage.Level.WARN)
   void xaRecoverError(@Cause Exception e);

   @LogMessage(id = 172014, value = "Notified of connection failure in xa recovery connectionFactory for provider {0} will attempt reconnect on next pass",
      level = LogMessage.Level.WARN)
   void xaRecoverConnectionError(@Cause Exception e, ClientSessionFactory csf);

   @LogMessage(id = 172015, value = "Can not connect to {0} on auto-generated resource recovery",
      level = LogMessage.Level.WARN)
   void xaRecoverAutoConnectionError(@Cause Throwable e, XARecoveryConfig csf);

   @LogMessage(id = 172016, value = "Error in XA Recovery", level = LogMessage.Level.DEBUG)
   void xaRecoveryError(@Cause Exception e);

   @LogMessage(id = 172017, value = "Tried to correct invalid \"host\" value \"0.0.0.0\" for \"{0}\" connector, but received an exception.",
      level = LogMessage.Level.WARN)
   void failedToCorrectHost(@Cause Exception e, String name);

   @LogMessage(id = 172018, value = "Could not start recovery discovery on {0}, we will retry every recovery scan until the server is available",
      level = LogMessage.Level.WARN)
   void xaRecoveryStartError(XARecoveryConfig e);

   @LogMessage(id = 174000, value = "key attribute missing for JMS configuration {0}", level = LogMessage.Level.ERROR)
   void jmsConfigMissingKey(Node e);

   @LogMessage(id = 174002, value = "Failed to start JMS deployer", level = LogMessage.Level.ERROR)
   void jmsDeployerStartError(@Cause Exception e);
}
