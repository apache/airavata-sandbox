## Steps to Use the Validation API

- This is the validation API for the nextcloud to enable the backend programs to authenticate to the nextcloud instance via the OAuth token through WebDAV.

-This is the POST API and takes the input as the username and token, then returns 200 response if the authentication is successfull else returns 401 unauthorized response.

- Place the checkuser.php API in the server where nextcloud is installed with the configuration files.

- For the API to work the nextcloud should be configured with the Single sign on using the user_saml app along with the keycloak.
The more information of configuring the nextcloud with the user_saml app and keycloak can be found at the following link:
https://stackoverflow.com/a/48400813/3446129 https://docs.nextcloud.com/server/11/admin_manual/configuration_server/sso_configuration.html 

- The introspection endpoint should be updated in the config.php, along with the other configurations settings with respect to the keycloak and the nextcloud database.


