<?php
 include('config.php');

 	$servername = $dbhost;
        $username = $dbuser;
        $password = $dbpassword;
        $db = $database;
        // Create connection
        $conn = new mysqli($servername, $username, $password,$db);
        // Check connection
        if ($conn->connect_error) {
                die("Connection failed: " . $conn->connect_error);
        }             
?>

