
# Running
1) Create an account on https://testdrive.airavata.org/
2) Get dev admin access on this account
3) Run the following command with your credentials to get the access token:
    ```
    curl --data "username=myusername&password=mypassword" https://dev.testdrive.airavata.org/api-login
    ```
4) Copy airavata-client.ini.template to airavata-client.ini and fill your username and accesstoken in it
5) Install Python 3
6) create a virtual environment and activate it
   ```
   python3 -m venv ENV
   . ENV/bin/activate
   ```
7) Install dependencies
    ```
    pip install -r requirements.txt
    ```
8: Run the test program: `python test.py`
