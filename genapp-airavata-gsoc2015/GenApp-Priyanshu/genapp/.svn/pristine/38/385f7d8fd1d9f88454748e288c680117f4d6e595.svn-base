<?php
header('Content-type: application/json');

session_start(); 

$results[ '_status' ] = "complete";

if ( !sizeof( $_REQUEST ) )
{
    $results[ "error" ] = "PHP code received no \$_REQUEST?";
    echo (json_encode($results));
    exit();
}

if ( !is_string( $_REQUEST[ 'userid' ] ) || 
   strlen( $_REQUEST[ 'userid' ] ) < 3 ||
   strlen( $_REQUEST[ 'userid' ] ) > 30 ||
   !filter_var( $_REQUEST[ 'userid' ], FILTER_VALIDATE_REGEXP, array("options"=>array("regexp"=>'/^[A-Za-z][A-Za-z0-9_]+$/') ) ) )
{
    $results[ "error" ] = "empty or invalid user name";
    echo (json_encode($results));
    exit();
}

$userid = $_REQUEST[ 'userid' ];

if ( !is_string( $_REQUEST[ 'password1' ] ) || strlen( $_REQUEST[ 'password1' ] ) < 10 || strlen( $_REQUEST[ 'password1' ] ) > 100 )
{
    $results[ "error" ] = "empty or invalid password";
    echo (json_encode($results));
    exit();
}

if( $_REQUEST[ 'password1' ] != $_REQUEST[ 'password2' ] )
{
    $results[ "error" ] = "Passwords do not match";
    echo (json_encode($results));
    exit();
}

$email = filter_var( $_REQUEST[ 'email' ], FILTER_SANITIZE_EMAIL );

if ( !is_string( $email ) || 
   !strlen( $email ) ||
   !filter_var( $email, FILTER_VALIDATE_EMAIL ) )
{
    $results[ "error" ] = "PHP code received empty or invalid email";
    echo (json_encode($results));
    exit();
}


// connect
try {
     $m = new MongoClient();
} catch ( Exception $e ) {
    $results[ "error" ] = "Could not connect to the db " . $e->getMessage();
    echo (json_encode($results));
    exit();
}
  
$coll = $m->__application__->users;

if ( $doc = $coll->findOne( array( "name" => $_REQUEST[ 'userid' ] ) ) )
{
   $results[ 'status' ] = "User id already registered, please try another";
   echo (json_encode($results));
   exit();
}

if ( PHP_VERSION_ID < 50500 )
{
  $pw = crypt( $_REQUEST[ 'password1' ] );
} else {
  $pw = password_hash( $_REQUEST[ 'password1' ], PASSWORD_DEFAULT );
}

date_default_timezone_set( 'UTC' );

try {
    $coll->insert( array( "name" => $_REQUEST[ 'userid' ], "password" => $pw, "email" => $email, "registered" => new MongoDate() )__~mongojournal{, array("j" => true )});
} catch(MongoCursorException $e) {
    $results[ 'status' ] = "User id already registered, please try another. " . $e->getMessage();
    echo (json_encode($results));
    exit();
}
require_once "../mail.php";
admin_mail( "[__application__][new user] $email", "User: " . $_REQUEST[ 'userid' ] . "\nEmail: $email\n" );
$results[ 'status' ] = "User successfully added, you can now login";

echo (json_encode($results));
exit();
?>
