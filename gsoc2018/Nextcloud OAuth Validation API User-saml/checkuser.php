<?php
include('connect.php');
include('config.php'); 
header("Content-Type:application/json"); 
//Process the client request
if(!empty($_POST['username']) && !empty($_POST['token'])){
	
	$user=$_POST['username'];
	$token=$_POST['token'];
		
	if(validate_token($token, $clientsecret, $introspurl, $clientid,$tokentype)){	
			
		if(checkuserexist($user,$conn)) {
			updateusertoken($token, $user,$conn);
			deliver_response(200, "Token Validated and Updated Successfully", NULL);
		}
		else {
			insertusertoken($token, $user, $conn);
			deliver_response(200, "Token Validated and Creater User Successfully", NULL);
		}
	}
	else {
		deliver_response(401,"Unauthorized: Invalid Token",$token);	
	}
	
	//validate_token($token, $clientsecret, $introspurl, $clientid,$tokentype);	
}

else 
{
	//throw the invalid request exception
	deliver_response(400, "Invalid Request", NULL);
        	
}
	
function deliver_response($status, $status_message, $data){
	
	header("HTTP/1.1 $status $status_message");	
	$response['status'] = $status;
	$response['status_message']=$status_message;
	$response['data']=$data;
	$json_response=json_encode($response);
	echo $json_response;
}

//This function validates the token using the introspection endpoint of the 
//keycloak configured in the server 
function validate_token($token, $clientsecret, $introspurl, $clientid,$tokentype)
{
        $post_url = $introspurl;
        $curl = curl_init($post_url);
        $fields = array(
                        'client_id' => $clientid,
                        'client_secret' => $clientsecret,
                        'token_type_hint' => $tokentype,
                        'token' => $token
                        );
	//Url-ify the data to prepare for the post request
        $fields_string = http_build_query($fields);
	//Open the connection	
	$ch = curl_init();
	
	//now set the url
	curl_setopt($ch, CURLOPT_URL, $post_url);
	curl_setopt($ch, CURLOPT_POST, 4);
	curl_setopt($ch, CURLOPT_POSTFIELDS,$fields_string);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch,CURLOPT_SSL_VERIFYPEER, false);
	//execute the post request
	$result = curl_exec($ch);
	//echo $result;
	$result_json = json_decode($result,true);
	//var_dump($result_json);
	$result_boolean = $result_json["active"];
	return $result_boolean;
}

//function to check if the user is already authenticated and has logged in once 
//in the keycloak so that only the token with respect to that particular userid can 
//be updated into the token table

function checkuserexist($uid, $conn)
{  

	$query = "SELECT * FROM oc_user_saml_auth_token WHERE uid = '".$uid."'";
        $result = mysqli_query($conn,$query);
        if(!mysqli_num_rows($result)>=1) {
        	return false;
        }
        return true;
}
//function to update the token in the oc_user_saml_token table if the token is 
//present in the table
function updateusertoken($token, $uid, $conn)
{
	$token_to_hash=substr($token, -72);
	//echo $token_to_hash;
	$token_hash = password_hash($token_to_hash, PASSWORD_DEFAULT);
		
	$query= "UPDATE oc_user_saml_auth_token SET token = '".$token_hash."' WHERE uid = '".$uid."'";
	
	if (!mysqli_query($conn, $query)) {
    		return false;
	} 
	return true;	
}

function insertusertoken($token, $uid, $conn)
{
        //Create User before inserting the token 
	if(!createuser($uid, $conn)) {
	 	return false;
	}
	//Hash the token before inserting it into the database
	$token_to_hash=substr($token, -72);
	//echo $token_to_hash;
        $token_hash = password_hash($token_to_hash, PASSWORD_DEFAULT);
        $query= "INSERT INTO oc_user_saml_auth_token (uid, name, token) VALUES ('".$uid."', '".$uid."', '".$token_hash."')";

        if (!mysqli_query($conn, $query)) {
		return false;
        }
    		return true;
}

function createuser($uid, $conn)
{
	$query = "INSERT INTO oc_user_saml_users (uid, displayname) VALUES ('".$uid."', '".$uid."')";
	 
	if (!mysqli_query($conn, $query)) {
                return false;
        }
        return true;
}

?>
