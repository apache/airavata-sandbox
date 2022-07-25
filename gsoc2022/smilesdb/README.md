# Small Molecule Ionic Lattices (SMILES) Data Models
![Local Build](https://img.shields.io/badge/local%20build-successful%20-green)
![GitHub last commit](https://img.shields.io/github/last-commit/bhavesh-asana/airavata-sandbox)
![GitHub commit activity (branch)](https://img.shields.io/github/commit-activity/m/bhavesh-asana/airavata-sandbox/master)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/bhavesh-asana/airavata-sandbox)


This project is an experimental workspace used for the Scientific and Chemical Engineering.
This is an end to end implementation of [SEAGrid Data Catalog](https://data.seagrid.org/),
embedded with the new features and advanced data visualization techniques.

**DEVELOPMENT GOALS**
1. Create a robust database to reduce the latency.
2. Redesigning the data models.
3. Synchronising the data with a user dashboard on performing experiment successfully.

# Table of Contents

* [Pre-requisites](#pre-requisites)
  * [Technical Stack](#technical-stack)
  * [Set up the code directory](#set-up-the-code-directory)
* [How to run the project](#how-to-run-the-project)
  * [Server Initialization](#server-initialization)
  * [Middleware (Django Application)](#middleware-django-application)
  * [Client Initialization](#client-initialization)
  * [Database Management](#database-management)
    * [Mongo Compass GUI](#visualize-the-data-with-mongo-compass-gui)
    * [Mongo CLI](#visualize-with-mongo-cli)
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
    ```

# How to run the project

## Server Initialization

On the server side, Spring Boot with the Maven configuration is being used to interact with the database
and define the proto schema. To start the communication services the
Google Remote Procedure Call (gRPC) stub is implemented in Java, which
acts as a server and helps to transfer the data effectively with the
connected clients across the distributed systems.

1. Before initializing the server, make sure the MongoDB is installed and the instance 
   is running locally. 
   ```commandline
   mongo --port 27017
    ```
   This command ensure the Mongo instance is running locally and connected the instance to the port 27017.
2. Open a new terminal window (server_runner) and change the directory to the
   server codebase
   ```commandline
   cd ~/airavata-sandbox/gsoc2022/smilesdb/Server/
    ```
3. Build the Maven project.
   ```commandline
   mvn package
   mvn clean install
    ```
4. Run the Spring Boot application.
   ```commandline
   mvn spring-boot:run
    ```
   On successful running of the server application, it shows a message as
   _"Server running successfully"_ and open connection with mongodb driver.

## Middleware (Django Application)
Open a new terminal window and follow the steps to run the middleware application.
1. Change the working directory to SMILES middleware.
   ```commandline
   cd ~/airavata-sandbox/gsoc2022/smilesdb/DjangoMiddleware
   ```
2. Create a virtual environment using the following command. <br/>
   Strictly recommended to use Python version 3.8.3 to build the **grpcio-wheel**.
      ```commandline
      $ conda create -n <EnvironmentName> python=3.8.3
      $ conda activate <EnvironmentName>
      ```
3. Upgrade the PIP version and install the required dependencies using the **requirements.txt** file.
      ```commandline
      pip install -U pip
      pip install -r requirements
      ```
4. Run the Django application.
   ```commandline
   python manage.py runserver
   ```
5. Open http://127.0.0.1:8000/api/calcinfo/ to check the data transmission from
   the server application. On successful transmission, the data can also be visualized 
   in the server terminal.


## Client Initialization
The front-end client application is developed in the JavaScript framework (vue.js).
The vue.js is communicated with the Django application (Middleware)
using REST api calls and the data is exchanged in between the server
and client application.

To run the client application, open a new terminal window and follow the below steps
1. Change the working directory to SMILES Dashboard.
   ```commandline
   cd ~/airavata-sandbox/gsoc2022/smilesdb/smiles_dashboard
   ```
2. Open the new terminal and run the following commands to build the project.
   ```commandline
   npm install
   npm run serve
   ```
3. Open
   - http://localhost:8080/ for Login page.
   - http://localhost:8080/SEAGrid for SEAGrid Homepage.
   - http://localhost:8080/calcinfo for the live CalcInfo data synchronization.

## Database Management

### Visualize the data with Mongo Compass GUI
The mongo instances are configured in the application.properties file (located 
under Server/src/main/resources/). Initialise the mongo compass and connect
to the respective port (27017). On execution of the **ServerApplication**, 
the **smiles** database is created and the test data of _calcinfo_ is sent 
to the database, which can be viewed under the **calcInfo** collection.

### Visualize with Mongo CLI
To view the data using Mongo shell, open the terminal and follow the commands
mentioned below.
```mongo
 mongo
 show dbs
 use smiles
 show collections
 db.calcInfo.find()
```

# References

1. **Jira Issue:** <br/>
   https://issues.apache.org/jira/browse/AIRAVATA-3593
2. **Confluence Page:** <br/>
   https://cwiki.apache.org/confluence/display/AIRAVATA/SMILES+Data+Models
3. **GitHub - Airavata sand-box:** <br/>
   Master branch: https://github.com/apache/airavata-sandbox
4. [**DevDocs**](dev_docs.md)

# The team

## GSoC Mentors

- Suresh Marru<br/>
  [<img src="https://img.shields.io/badge/LinkedIn-0077B5?style=plastic&logo=linkedin&logoColor=white" />](https://www.linkedin.com/in/sureshmarru/)
- Sudhakar Pamidighatam <br/>
  [<img src="https://img.shields.io/badge/LinkedIn-0077B5?style=plastic&logo=linkedin&logoColor=white" />](https://www.linkedin.com/in/sudhakar-pamidighantam-0074a77/)

## Contributor

- Bhavesh Asanabada <br/>
  [<img src="https://img.shields.io/badge/LinkedIn-0077B5?style=plastic&logo=linkedin&logoColor=white" />](https://www.linkedin.com/in/bhavesh-asana/)