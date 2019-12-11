<?php

$json = json_decode( file_get_contents( "__appconfig__" ) );

$context = new ZMQContext();
$zmq_socket = $context->getSocket(ZMQ::SOCKET_PUSH, 'psptest udp pusher');
$zmq_socket->connect("tcp://" . $json->messaging->zmqhostip . ":" . $json->messaging->zmqport );

$udp_socket = socket_create(AF_INET, SOCK_DGRAM, SOL_UDP);
socket_bind($udp_socket, $json->messaging->udphostip, $json->messaging->udpport );

echo "msg_udpserver: listening UDP host:" . $json->messaging->udphostip . " UDP port:" . $json->messaging->udpport . " sending ZMQ host:" . $json->messaging->zmqhostip . " port:" . $json->messaging->zmqport . PHP_EOL;

do {
   $from = '';
   $port = 0;
   socket_recvfrom($udp_socket, $buf, 65535, 0, $from, $port);
   $buf = str_replace( "/opt/lampp/htdocs/psptest/", "", $buf );

   $zmq_socket->send( $buf );
} while( 1 );
