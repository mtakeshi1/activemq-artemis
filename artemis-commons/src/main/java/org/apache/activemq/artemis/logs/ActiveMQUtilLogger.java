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
package org.apache.activemq.artemis.logs;

import org.apache.activemq.artemis.logprocessor.CodeFactory;
import org.apache.activemq.artemis.logprocessor.annotation.Cause;
import org.apache.activemq.artemis.logprocessor.annotation.LogBundle;
import org.apache.activemq.artemis.logprocessor.annotation.LogMessage;

/**
 * Logger Code 20
 */
@LogBundle(projectCode = "AMQ")
public interface ActiveMQUtilLogger {

   ActiveMQUtilLogger LOGGER = CodeFactory.getCodeClass(ActiveMQUtilLogger.class);

   @LogMessage(id = 201000, value = "Network is healthy, starting service {}", level = LogMessage.Level.INFO)
   void startingService(String component);

   @LogMessage(id = 201001, value = "Network is unhealthy, stopping service {}", level = LogMessage.Level.WARN)
   void stoppingService(String component);

   @LogMessage(id = 202000, value = "Missing privileges to set Thread Context Class Loader on Thread Factory. Using current Thread Context Class Loader", level = LogMessage.Level.WARN)
   void missingPrivsForClassloader();

   @LogMessage(id = 202001, value = "{} is a loopback address and will be discarded.", level = LogMessage.Level.WARN)
   void addressloopback(String address);

   @LogMessage(id = 202002, value = "Ping Address {} wasn't reacheable.", level = LogMessage.Level.WARN)
   void addressWasntReacheable(String address);

   @LogMessage(id = 202003, value = "Ping Url {} wasn't reacheable.", level = LogMessage.Level.WARN)
   void urlWasntReacheable(String url);

   @LogMessage(id = 202004, value = "Error starting component {} ", level = LogMessage.Level.WARN)
   void errorStartingComponent(@Cause Exception e, String component);

   @LogMessage(id = 202005, value = "Error stopping component {} ", level = LogMessage.Level.WARN)
   void errorStoppingComponent(@Cause Exception e, String component);

   @LogMessage(id = 202006, value = "Failed to check Url {}.", level = LogMessage.Level.WARN)
   void failedToCheckURL(@Cause Exception e, String url);

   @LogMessage(id = 202007, value = "Failed to check Address {}.", level = LogMessage.Level.WARN)
   void failedToCheckAddress(@Cause Exception e, String address);

   @LogMessage(id = 202008, value = "Failed to check Address list {}.", level = LogMessage.Level.WARN)
   void failedToParseAddressList(@Cause Exception e, String addressList);

   @LogMessage(id = 202009, value = "Failed to check Url list {}.", level = LogMessage.Level.WARN)
   void failedToParseUrlList(@Cause Exception e, String urlList);

   @LogMessage(id = 202010, value = "Failed to set NIC {}.", level = LogMessage.Level.WARN)
   void failedToSetNIC(@Cause Exception e, String nic);

   @LogMessage(id = 202011, value = "Failed to read from stream {}.", level = LogMessage.Level.WARN)
   void failedToReadFromStream(String stream);

   @LogMessage(id = 202012, value = "Object cannot be serialized.", level = LogMessage.Level.WARN)
   void failedToSerializeObject(@Cause Exception e);

   @LogMessage(id = 202013, value = "Unable to deserialize object.", level = LogMessage.Level.WARN)
   void failedToDeserializeObject(@Cause Exception e);

   @LogMessage(id = 202014, value = "Unable to encode byte array into Base64 notation.", level = LogMessage.Level.WARN)
   void failedToEncodeByteArrayToBase64Notation(@Cause Exception e);

   @LogMessage(id = 202015, value = "Failed to clean up file {}", level = LogMessage.Level.WARN)
   void failedToCleanupFile(String file);

   @LogMessage(id = 202016, value = "Could not list files to clean up in {}", level = LogMessage.Level.WARN)
   void failedListFilesToCleanup(String path);
}
