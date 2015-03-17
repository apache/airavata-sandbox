/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
*/

/**
 * Mock Credential Store API
*/

namespace java org.apache.airavata.api.credentials
namespace php Airavata.API.Credentials
namespace cpp apache.airavata.api.credentials
namespace perl ApacheAiravataAPICredentials
namespace py apache.airavata.api.credentials
namespace js ApacheAiravataAPICredentials

service CredentialManagementService {

  /**
   * Generate and Register SSH Key Pair with Airavata Credential Store.
   *
   * @param gatewayId
   *    The identifier for the requested gateway.
   *
   * @param userName
   *    The User for which the credential should be registered. For community accounts, this user is the name of the
   *    community user name. For computational resources, this user name need not be the same user name on resoruces.
   *
   * @return airavataCredStoreToken
   *   An SSH Key pair is generated and stored in the credential store and associated with users or community account
   *   belonging to a gateway.
   *
   **/

   string generateAndRegisterSSHKeys (1: required string gatewayId, 2: required string userName)

   string getSSHPubKey (1: required string airavataCredStoreToken)

   map<string, string> getAllUserSSHPubKeys (1: required string userName)

}