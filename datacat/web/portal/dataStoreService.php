<?php

session_start();

$call = $_POST['call'];
if(isset($_POST['data'])){
    $data = $_POST['data'];
}

function getResults($data)
{
    if($_SESSION['loggedIn'] == "Y"){                                                   // append username ang groups of user to a query parameter array
        $data['username'] = $_SESSION['username'];
        $data['userGroups'] = $_SESSION['userGroups'];
    }

    $url = 'http://localhost:8887/datacat/getResults';
    $jsonData = json_encode($data);

    $options = array(
        'http' => array(
            'method'  => 'POST',
            'content' => $jsonData,
            'header'=>  "Content-Type: application/json\r\n" .
                "Accept: application/json\r\n"
        )
    );

    $context  = stream_context_create( $options );                                      // get the resulting data products for a given query
    $result = file_get_contents( $url, false, $context );

    return $result  ;
}

function getMetadataFieldList()
{
    $url = 'http://localhost:8887/datacat/getMetadataFieldList';

    $options = array(
        'http' => array(
            'method'  => 'GET',
            'header'=>  "Content-Type: application/json\r\n" .
                "Accept: application/json\r\n"
        )
    );

    $context  = stream_context_create( $options );
    $result = file_get_contents( $url, false, $context );                               // get the full list of searchable/indexed fields

    return $result;
}

function getAclList($data)
{
    $url = 'http://localhost:8887/datacat/getAclList?id=' . urlencode($data['id']);

    $options = array(
        'http' => array(
            'method'  => 'GET',
            'header'=>  "Content-Type: application/json\r\n" .
                "Accept: application/json\r\n"
        )
    );

    $context  = stream_context_create( $options );
    $result = file_get_contents( $url, false, $context );                               // get the ACL list of a data product given its id

    return $result  ;
}

function updateAclList($data)
{

    $url = 'http://localhost:8887/datacat/updateAclList';
    $jsonData = json_encode($data);

    $options = array(
        'http' => array(
            'method'  => 'POST',
            'content' => $jsonData,
            'header'=>  "Content-Type: application/json\r\n" .
                "Accept: application/json\r\n"
        )
    );

    $context  = stream_context_create( $options );
    $result = file_get_contents( $url, false, $context );                               // update ACL list of a data product

    return $result  ;
}

function getAllUserGroups()
{
    $url = 'http://localhost:8889/userstore/getGroupsList';

    $options = array(
        'http' => array(
            'method'  => 'GET',
            'header'=>  "Content-Type: application/json\r\n" .
                "Accept: application/json\r\n"
        )
    );

    $context  = stream_context_create( $options );
    $result = file_get_contents( $url, false, $context );                               // get the full list of user groups for sharing data products

    return $result  ;
}

function checkLogin()                                                                   // from frontend javascript, check user logged in and get username
{
    if ($_SESSION['loggedIn'] == "Y") {
        return $_SESSION['username'];
    } else {
        return "%%false%%";
    }
}

function login($data)
{
    $url = 'http://localhost:8889/userstore/authenticate?username=' . urlencode($data['username']) . '&password=' . urlencode($data['password']);

    $options = array(
        'http' => array(
            'method'  => 'GET',
            'header'=>  "Content-Type: application/json\r\n" .
                "Accept: application/json\r\n"
        )
    );

    $context  = stream_context_create( $options );
    $auth = file_get_contents( $url, false, $context );                                 // authenticate user

    if ($auth == 'true') {

        $url = 'http://localhost:8889/userstore/getGroupsOfUser?username=' . urlencode($data['username']);

        $options = array(
            'http' => array(
                'method'  => 'GET',
                'header'=>  "Content-Type: application/json\r\n" .
                    "Accept: application/json\r\n"
            )
        );

        $context  = stream_context_create( $options );
        $userGroups = file_get_contents( $url, false, $context );                       // if authenticated, get users list of access groups

        $_SESSION['loggedIn'] = "Y";                                                    // login user

        $_SESSION['username'] = $data['username'];
        $_SESSION['userGroups'] =  json_decode( $userGroups );
    }

    return $auth;
}

function logout()                                                                       // destroy session and logout
{
    session_destroy();
    return true;
}


if ($call == 'getResults') {                                                            // adaptor to run corresponding functions
    print_r(getResults($data));
} else if ($call == 'getMetadataFieldList') {
    print_r(getMetadataFieldList());
} else if ($call == 'getAclList') {
    print_r(getAclList($data));
} else if ($call == 'login') {
    print_r(login($data));
} else if ($call == 'logout') {
    print_r(logout());
} else if ($call == 'checkLogin') {
    print_r(checkLogin());
} else if ($call == 'getAllUserGroups') {
    print_r(getAllUserGroups());
} else if ($call == 'updateAclList') {
    print_r(updateAclList($data));
}