<?php
header('Content-type: application/json');
// $cstrong = true;
$json = json_decode( file_get_contents( "__appconfig__" ) );
// $response[ '_sid' ] = bin2hex( openssl_random_pseudo_bytes ( 20, $cstrong ) );
$response[ '_ws'  ] = 'ws://' . $json->hostip . ':' . $json->messaging->wsport;
__~usewss{$response[ '_ws'  ] = 'wss://' . $json->hostname . ':' . $json->messaging->wssport . "/wss2";}
__~usews{$response[ '_ws'  ] = 'ws://' . $json->hostname . ':' . $json->messaging->wssport . "/ws2";}

echo (json_encode($response));
?>
