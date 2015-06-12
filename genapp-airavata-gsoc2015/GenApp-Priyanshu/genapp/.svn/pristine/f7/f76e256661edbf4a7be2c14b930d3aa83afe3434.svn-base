<?php
header('Content-type: application/json');

session_start(); 

$window = "";
if ( isset( $_REQUEST[ '_window' ] ) )
{
   $window = $_REQUEST[ '_window' ];
}
if ( !isset( $_SESSION[ $window ] ) )
{
   $_SESSION[ $window ] = array( "logon" => "", "project" => "" );
}

if ( !isset( $_SESSION[ $window ][ 'logon' ] ) ||
     !isset( $_REQUEST[ '_logon' ] ) )
{
    $results[ 'error' ] .= "Not logged in. ";
    echo (json_encode($results));
    exit();
}

try {
    $m = new MongoClient();
} catch ( Exception $e ) {
    $results[ 'error' ] .= "Could not connect to the db " . $e->getMessage();
    echo (json_encode($results));
    exit();
}

if ( $doc = $m->__application__->license->findOne( array( "name" => $_SESSION[ $window ][ 'logon' ] ) ) ) {
    $results[ 'license' ] = $doc;
} else {
    $results[ 'license' ] = (object)array();
}

if ( strlen( $_REQUEST[ "_logon" ] ) ) {
   $appconfig = json_decode( file_get_contents( "__appconfig__" ) );
   if ( isset( $appconfig->admin ) && in_array( $_REQUEST[ "_logon" ], $appconfig->admin ) ) {
       $results[ "hasadmin" ] = true;
   }
}

echo (json_encode($results));
?>
