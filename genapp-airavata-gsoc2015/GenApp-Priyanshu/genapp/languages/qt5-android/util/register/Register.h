#include <glib.h>
#include <iostream>
#include <stdint.h>
#include <sys/time.h>
#include <fstream>
#include <stdlib.h>
#define _WIN32_WINNT 0x501

#include "../lib/thrift/transport/TTransport.h"
#include "../lib/thrift/transport/TBufferTransports.h"
#include "../lib/thrift/transport/TSocket.h"
#include "../lib/thrift/protocol/TProtocol.h"
#include "../lib/thrift/protocol/TBinaryProtocol.h"
#include "../lib/thrift/protocol/TBinaryProtocol.tcc"
#include "../lib/thrift/TApplicationException.h"
#include "../lib/thrift/transport/TTransportException.h"
#include "../lib/thrift/protocol/TProtocolException.h"
#include "../lib/airavata/Airavata.h"
#include "../lib/airavata/Airavata.cpp"
#include "../lib/airavata/airavataDataModel_types.h"
#include "../lib/airavata/airavataDataModel_types.cpp"
#include "../lib/airavata/airavataErrors_types.h"
#include "../lib/airavata/airavataErrors_types.cpp"
#include "../lib/airavata/experimentModel_types.h"
#include "../lib/airavata/experimentModel_types.cpp"
#include "../lib/airavata/workspaceModel_types.h"
#include "../lib/airavata/workspaceModel_types.cpp"
#include "../lib/airavata/airavataAPI_types.h"
#include "../lib/airavata/airavataAPI_types.cpp"
#include "../lib/airavata/applicationDeploymentModel_types.h"
#include "../lib/airavata/applicationDeploymentModel_types.cpp"
#include "../lib/airavata/applicationInterfaceModel_types.h"
#include "../lib/airavata/applicationInterfaceModel_types.cpp"
#include "../lib/airavata/gatewayResourceProfileModel_types.h"
#include "../lib/airavata/gatewayResourceProfileModel_types.cpp"
#include "../lib/airavata/computeResourceModel_types.h"
#include "../lib/airavata/computeResourceModel_types.cpp"

using namespace std;
using namespace apache::thrift;
using namespace apache::thrift::protocol;
using namespace apache::thrift::transport;
using namespace apache::airavata::api;
using namespace apache::airavata::model::workspace;
using namespace apache::airavata::model::workspace::experiment;
using namespace apache::airavata::model::appcatalog::computeresource;
using namespace apache::airavata::model::appcatalog::appinterface;
using namespace apache::airavata::model::appcatalog::appdeployment;
using namespace apache::airavata::model::appcatalog::gatewayprofile;
using namespace apache::airavata::api::error;

class Register
{
public:
    void init();
    void readConfigFile(char* cfgfile,string& airavata_server, int& airavata_port, int& airavata_timeout);
    void registerAll();
    void registerLocalhost();
    void registerGateway();
    void registerGatewayProfile();
    void registerApplicationInterfaces();
    void registerApplicationDeployments();
    void registerApplicationModules();
    void registerApplicationInterface(string moduleName,string moduleId);
    void registerMain(Register* registerGenapp);
    string getGatewayId();
    string getInterfaceId(string moduleName);
    string getComputeResourceId();
    static Register* getInstance();


private:
    Register();
    ~Register();
    static const string THRIFT_SERVER_HOST ;
    static const int THRIFT_SERVER_PORT = 8930;
    static const string DEFAULT_GATEWAY ;
    AiravataClient* airavataClient;
    string localhost_ip = "127.0.0.1";
    string localhostId ;
    string moduleId;
    string moduleDir; 
    string directivesFile;
    string modulesFile;
    string appConfig;
    string gatewayId;
    static bool instanceFlag;
    static Register* register_;
};
