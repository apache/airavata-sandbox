<?php

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

if ( !isset( $_SESSION[ $window ][ 'udpport' ] ) ||
     !isset( $_SESSION[ $window ][ 'udphost' ] ) || 
     !isset( $_SESSION[ $window ][ 'resources' ] ) ||
     !isset( $_SESSION[ $window ][ 'submitpolicy' ] ) )
{
   $appjson = json_decode( file_get_contents( "__appconfig__" ) );
   $_SESSION[ $window ][ 'udphost'         ] = $appjson->messaging->udphostip;
   $_SESSION[ $window ][ 'udpport'         ] = $appjson->messaging->udpport;
   $_SESSION[ $window ][ 'resources'       ] = $appjson->resources;
   $_SESSION[ $window ][ 'resourcedefault' ] = $appjson->resourcedefault;
   $_SESSION[ $window ][ 'submitpolicy'    ] = $appjson->submitpolicy;
}

$policy = $_SESSION[ $window ][ 'submitpolicy' ];

session_write_close();

if ( isset( $_REQUEST[ '_submitpolicy' ] ) )
{
   $policy = $_REQUEST[ '_submitpolicy' ];
}

if ( !isset( $_SESSION[ $window ][ 'logon' ] ) ||
     !strlen( $_SESSION[ $window ][ 'logon' ] ) )
{
  if ( $policy != "all" )
  {
     echo '2';
  } else {
     echo '0';
  }
  exit();
}

$GLOBALS[ 'logon' ] = $_SESSION[ $window ][ 'logon' ];

require_once "../joblog.php";

$GLOBALS[ 'project' ] = isset( $_SESSION[ $window ][ 'project' ] ) &&
                        strlen( $_SESSION[ $window ][ 'project' ] ) ? 
                        $_SESSION[ $window ][ 'project' ] : "no_project_specified";

$dir = "/opt/lampp/htdocs/psptest/results/users/" . $_SESSION[ $window ][ 'logon' ] . "/" . $GLOBALS[ 'project' ];

$locked = isprojectlocked( $dir );





if ( $locked )
{
   echo '1';
} else {
   echo '0';
}
exit();
?>
