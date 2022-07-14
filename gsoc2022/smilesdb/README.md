# Small Molecule Ionic Lattices (SMILES) Data Models

![GitHub last commit](https://img.shields.io/github/last-commit/bhavesh-asana/airavata-sandbox)
![GitHub commit activity (branch)](https://img.shields.io/github/commit-activity/w/bhavesh-asana/airavata-sandbox/master)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/bhavesh-asana/airavata-sandbox)


This project is an experimental workspace used for the Scientific and Chemical Engineering.
This is an end to end implementation of [SEAGrid Data Catalog](https://data.seagrid.org/),
embedded with the new features and advanced data visualization techniques.

**DEVELOPMENT GOALS**
1. Create a robust database to reduce the latency.
2. Redesigning the data models.
3. Synchronising the data with a user dashboard.

# Table of Contents

* [Pre-requisites](#pre-requisites)
  * [Technical Stack](#technical-stack)
  * [Set up the code directory](#set-up-the-code-directory)
* [How to run the project](#how-to-run-the-project)
  * [Server Initialization](#server-initialization)
  * [Middleware (Django Application)](#middleware-django-application)
  * [Client Initialization](#client-initialization)
  * [Database](#database)
    * [Mongo Compass GUI](#visualize-the-data-with-mongo-compass-gui)
    * [Mongo Shell](#visualize-with-mongo-shell)
  * [Test Data](#test-data)
* [References](#references)
* [The Team](#the-team)

# Pre-requisites

## Technical Stack

| **Function**                         | **Language/Framework/Technology used**                                |
|--------------------------------------|-----------------------------------------------------------------------|
| Backend Microservice Implementation  | 1. Spring Boot (Java) <br/> _(Suggested tool IntelliJ)_               |
| Frontend Microservice Implementation | 1. Django (Python)  <br/> 2. Vue JS  <br/> _(Suggested tool PyCharm)_ |
| Inter-service Communication          | 1. Google Remote Procedure Calls (gRPC) <br/> 2. REST framework       |
| Database Management                  | MongoDB (Mongo Compass)                                               |
| Performance Testing                  | BloomRPC (for gRPC routing)                                           |

## Set up the code directory

**Suggestion:** For the effective workspace management, use IntelliJ and PyCharm.<br/><br/>
**Working on Mac Environment:** <br/>

1. Open the terminal application and set the path to the home directory,
   use the command `cd ~/` to move to the home directory.
2. Clone the GitHub repository and use the following commands to change
   the working directory.
   ```commandline
    git init
    git clone https://github.com/bhavesh-asana/airavata-sandbox.git
    cd ~/airavata-sandbox/gsoc2022/smilesdb/
    ```

# How to run the project

## Server Initialization

On the server side, Spring Boot with the Maven configuration is being used to interact with the database
and define the proto schema. To start the communication services the
Google Remote Procedure Call (gRPC) stub is implemented in Java, which
acts as a server and helps to transfer the data effectively with the
connected clients across the distributed systems.

Open the **Server** directory in IntelliJ and follow the steps to run the
server application.

1. Open the terminal in IntelliJ and ensure you are in the **Server** path.
2. Run the following commands to build the maven project. <br />
   ```commandline
   mvn clean compile install
   ```
3. In the target folder, make the following directories as a source root.
    1. target/classes
    2. target/generated-sources/protobuf/grpc-java
    3. target/generated-sources/protobuf/java
4. Run the **ServerApplication** to initialise the server.
5. On successful build, you can find the following message in the terminal.<br/>
   **message:** Server running successfully<br/>
   This ensures that the server is listening at the local port 7594.

## Middleware (Django Application)
Open the **DjangoMiddleware** directory in Pycharm and follow the steps to run the middleware.
1. Create a virtual environment using the following command. <br/>
   Strictly recommended to use Python version 3.8.3 to build the **grpcio-wheel**.
      ```commandline
      $ python -m venv <EnvironmentName>
      $ source <EnvironmentName>/bin/activate
      ```
2. Install the required dependencies using the **requirements.txt** file.
      ```commandline
      $ pip install requirements.txt
      ```
3. Run the Django application.
   ```commandline
   $ python manage.py runserver
   ```
4. Open http://127.0.0.1:8000/api/calcinfo/ to check the data transmission from
   the server application. On successful transmission, the data can also be visualized 
   in the server terminal.


## Client Initialization
The front-end client application is developed in the JavaScript framework (vue.js).
The vue.js is communicated with the Django application (Middleware)
using REST api calls and the data is exchanged in between the server
and client application.

To run the client application, follow the below steps
1. Open the **smiles_dashboard** directory in PyCharm (another window).
2. Open the new terminal and run the following commands to build the project.
   ```commandline
   npm install
   npm run serve
   ```
3. Open
   - http://localhost:8081/ for Login page.
   - http://localhost:8081/SEAGrid for SEAGrid Homepage.
   - http://localhost:8081/calcinfo for the live data synchronization.

## Database

### Visualize the data with Mongo Compass GUI
The mongo instances are configured in the application.properties file (located 
under Server/src/main/resources/). Initialise the mongo compass and connect
to the respective port (27017). On execution of the **ServerApplication**, 
the **smiles** database is created and the test data of _calcinfo_ is sent 
to the database, which can be viewed under the **calcInfo** collection.

### Visualize with mongo shell
To view the data using Mongo shell, open the terminal and follow the commands
mentioned below.
```mongo
 mongo
 show dbs
 use smiles
 show collections
 db.calcInfo.find()
```
## Test Data
- The instant test data for each parameter of the **calcInfo** proto buffer is defined in <br/>
  Server/src/main/java/com/smiles/calcinfo/CalcInfoImpl.java from line 20 to 24.
- Update the data or add new data to visualize the live data handling.
# References

1. **Jira Issue:** <br/>
   https://issues.apache.org/jira/browse/AIRAVATA-3593
2. **Confluence Page:** <br/>
   https://cwiki.apache.org/confluence/display/AIRAVATA/SMILES+Data+Models
3. **GitHub - Airavata sand-box:** <br/>
   Master branch: https://github.com/apache/airavata-sandbox

# The team

## GSoC Mentors

- Suresh Marru<br/>
  [<img src="https://img.shields.io/badge/LinkedIn-0077B5?style=plastic&logo=linkedin&logoColor=white" />](https://www.linkedin.com/in/sureshmarru/)
- Sudhakar Pamidighatam <br/>
  [<img src="https://img.shields.io/badge/LinkedIn-0077B5?style=plastic&logo=linkedin&logoColor=white" />](https://www.linkedin.com/in/sudhakar-pamidighantam-0074a77/)

## Contributor

- Bhavesh Asanabada <br/>

  [<img src="https://img.shields.io/badge/LinkedIn-0077B5?style=plastic&logo=linkedin&logoColor=white" />](https://www.linkedin.com/in/bhavesh-asana/)