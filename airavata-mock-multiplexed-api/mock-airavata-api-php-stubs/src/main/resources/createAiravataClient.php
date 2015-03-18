<?php
/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache V2
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

namespace Airavata\Client\Samples;


$GLOBALS['THRIFT_ROOT'] = 'lib/Thrift/';
require_once $GLOBALS['THRIFT_ROOT'] . 'Transport/TTransport.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Transport/TBufferedTransport.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Transport/TSocket.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Protocol/TProtocol.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Protocol/TBinaryProtocol.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Protocol/TProtocolDecorator.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Protocol/TMultiplexedProtocol.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Exception/TException.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Exception/TTransportException.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Exception/TApplicationException.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Exception/TProtocolException.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Base/TBase.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Type/TType.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Type/TMessageType.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'Factory/TStringFuncFactory.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'StringFunc/TStringFunc.php';
require_once $GLOBALS['THRIFT_ROOT'] . 'StringFunc/Core.php';

$GLOBALS['AIRAVATA_ROOT'] = 'lib/Airavata/';
require_once $GLOBALS['AIRAVATA_ROOT'] . 'API/Credentials/CredentialManagementService.php';
require_once $GLOBALS['AIRAVATA_ROOT'] . 'API/GatewayManagement/GatewayManagementService.php';

use Airavata\API\Credentials\CredentialManagementServiceClient;
use Airavata\API\GatewayManagement\GatewayManagementServiceClient;
use Thrift\Protocol\TBinaryProtocol;
use Thrift\Protocol\TMultiplexedProtocol;
use Thrift\Transport\TBufferedTransport;
use Thrift\Transport\TSocket;

$socket = new TSocket('localhost', 9190);
$transport = new TBufferedTransport($socket);
$transport->open();

$credentialprotocol = new TMultiplexedProtocol(new TBinaryProtocol($transport), "CredentialManagementService");
$credentialManagementClient = new CredentialManagementServiceClient($credentialprotocol);

$gatewayprotocol = new TMultiplexedProtocol(new TBinaryProtocol($transport), "GatewayManagementService");
$gatewayManagementClient = new GatewayManagementServiceClient($gatewayprotocol);


echo 'Test SSH Key is ' .  $credentialManagementClient->generateAndRegisterSSHKeys('testgateway','testuser');

echo 'Test Gateay Name is ' . $gatewayManagementClient->getGatewayName('test');

?>
