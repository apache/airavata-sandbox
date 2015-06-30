#include "Register.h"
#include <map>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <thrift/transport/TSocket.h>
#include <thrift/TApplicationException.h>

using std::string;

const string Register::THRIFT_SERVER_HOST = "127.0.0.1" ;
const string Register::DEFAULT_GATEWAY = "default.registry.gateway" ;


struct module {
    string id;
    string name;
    string moduleId;
    string interfaceId;
};
vector<module> genapp_modules;
map<string,module> genapp_mod;
Register* Register::register_ = NULL;
bool Register::instanceFlag = false;


Register::Register(){}
Register::~Register(){}

void Register::init()
{
    this->moduleDir="";
    this->moduleId="";
    this->localhostId="";
    
//    QDirIterator it(":/scripts",QDirIterator::Subdirectories);
//    while (it.hasNext()) {
//        qDebug() << it.next();
//        module new_module;
//        new_module.id = it.next().toStdString();
//        new_module.name = it.next().toStdString();
//        genapp_mod[new_module.name] = new_module;
//        genapp_modules.push_back(new_module);
//    }
    //string directivesHome = getenv("DIRECTIVES_HOME");
//    string directivesHome = "/home/priyanshu-sekhar/gsoc/new_airavata/new_genapp/psptest";
    //    directivesHome += "/../..";
    //    QString qDirectivesHome = QString::fromStdString(directivesHome);
    //    qDebug() << "directivesHome: " << qDirectivesHome;

//    this->moduleDir = directivesHome;
    //    if(directivesHome.empty())
    //    {
    //        moduleDir = "/bin";
    //    }
    //    else
    //    {
    this->directivesFile = ":/jsons/directives.json";
    this->modulesFile = ":/jsons/menu.json";
    this->appConfig = ":/jsons/appconfig.json";

    //    }
    //    cout << "Directives directory: "+this->directivesFile << endl;
    //    //directives
    QString val;
    QFile file;
    file.setFileName(":/jsons/directives.json");
    file.open(QIODevice::ReadOnly | QIODevice::Text);
    val = file.readAll();
    file.close();
    qDebug() << val;
    QJsonDocument d = QJsonDocument::fromJson(val.toUtf8());
    QJsonObject sett2 = d.object();
    QJsonValue value = sett2.value(QString("executable_path"));
    QJsonObject item = value.toObject();
    QJsonValue subobj = item["qt5"];
    qDebug() << subobj.toString();
    this->moduleDir = subobj.toString().toStdString();

    file.setFileName(QString(":/jsons/menu.json"));
    file.open(QIODevice::ReadOnly | QIODevice::Text);
    val = file.readAll();
    file.close();
    qDebug() << val;
    QJsonDocument d1 = QJsonDocument::fromJson(val.toUtf8());
    QJsonObject sett21 = d1.object();
    QJsonValue value1 = sett21.value(QString("menu"));
    QJsonArray menu = value1.toArray();
    qDebug() << menu;
    for(int i=0;i<menu.size();i++)
    {
        QJsonValue lected_menu = menu[i].toObject().value(QString("modules"));
        qDebug() << lected_menu;
        QJsonArray selected_menu = lected_menu.toArray();
        qDebug() << selected_menu;
        for(int j=0;j<selected_menu.size();j++)
        {
            QJsonValue id = selected_menu[j].toObject().value(QString("id"));
            QJsonValue name = selected_menu[j].toObject().value(QString("label"));
            module new_module;
            new_module.id = id.toString().toStdString();
            new_module.name = name.toString().toStdString();
            genapp_mod[new_module.name] = new_module;
            genapp_modules.push_back(new_module);
            qDebug() << "module" << QString::fromStdString(new_module.name);
        }
    }
//    qDebug() << subobj.toString();
//    this->moduleDir = subobj.toString().toStdString();


    //    qDebug() << "1";
    //    json directives_json = json::parse_file(file);
    //    qDebug() << "2";
    //    json executable_path = directives_json["executable_path"];
    //    this->moduleDir = executable_path["qt5"].as<std::string>();

    //    //modules
    
    //    json modules = json::parse_file(this->modulesFile);
    //    json module_menus = modules["menu"].as<json::array>();
    //    for(size_t i=0;i<module_menus.size();i++)
    //    {
    //        json menu_modules = module_menus[i]["modules"].as<json::array>();
    //        for(size_t j=0;j<menu_modules.size();j++)
    //        {
    //            module new_module;
    //            new_module.id = menu_modules[j]["id"].as<std::string>();
    //            new_module.name = menu_modules[j]["label"].as<std::string>();
    //            genapp_mod[new_module.name] = new_module;
    //            genapp_modules.push_back(new_module);
    //            qDebug() << "module" << QString::fromStdString(new_module.name);
    //        }
    //    }
    
}


void Register::readConfigFile(char* cfgfile, string& airavata_server, int& airavata_port, int& airavata_timeout) {

    QFile file;
    file.setFileName(":/jsons/appconfig.json");
    file.open(QIODevice::ReadOnly | QIODevice::Text);
    QString val;
    val = file.readAll();
    file.close();
    qDebug() << val;
    QJsonDocument d1 = QJsonDocument::fromJson(val.toUtf8());
    QJsonObject sett21 = d1.object();
    QJsonValue value1 = sett21.value(QString("hostip"));
    airavata_server = value1.toString().toStdString();
    QJsonValue value2 = sett21.value(QString("hostport"));
    airavata_port = value2.toInt();
    QJsonValue value3 = sett21.value(QString("hosttimeout"));
    airavata_timeout = value3.toInt();
    qDebug() << QString::fromStdString(airavata_server);
    qDebug() << airavata_port;
    qDebug() << airavata_timeout;
//    airavata_server = config_json["hostip"].as<std::string>();
//    airavata_port = config_json["hostport"].as<int>();
//    airavata_timeout = config_json["hosttimeout"].as<int>();
}

void Register::registerAll()
{  
    
    int airavata_port, airavata_timeout;
    string airavata_server="";
    char* cfgfile = "./airavata-client-properties.ini";;
    readConfigFile(cfgfile, airavata_server, airavata_port, airavata_timeout);
    airavata_server.erase(0,1);
    airavata_server.erase(airavata_server.length()-1,1);
    boost::shared_ptr<TSocket> socket(new TSocket(airavata_server, airavata_port));
    socket->setSendTimeout(airavata_timeout);
    boost::shared_ptr<TTransport> transport(new TBufferedTransport(socket));
    boost::shared_ptr<TProtocol> protocol(new TBinaryProtocol(transport));
    airavataClient = new AiravataClient(protocol);

    qDebug() << "registerAll";
    transport->open();
    registerGateway();
    registerLocalhost();
    registerGatewayProfile();
    registerApplicationModules();
    registerApplicationDeployments();
    registerApplicationInterfaces();
    transport->close();
}

void Register::registerGateway() 
{
    try
    {
        qDebug() << "#### Registering Gateway ####";
        Gateway gateway;
        gateway.__set_gatewayName("Sample");
        gateway.__set_gatewayId("sample");
        airavataClient->addGateway(this->gatewayId,gateway);
        qDebug() << "Gateway registered, id: " << QString::fromStdString(this->gatewayId);
    }
    catch(TException e)
    {
        qDebug() << QString::fromStdString(e.what());
    }
}

void Register::registerLocalhost() 
{
    try
    {
        qDebug() << "\n #### Registering Localhost Computational Resources ####";
        string hostname="localhost";
        string hostDesc="LocalHost";
//        vector<string> ipAddresses;
//        ipAddresses.push_back("192.168.1.106");
        //        vector<string> hostAliases;

        ComputeResourceDescription host;
        host.__set_hostName(hostname);
        host.__set_resourceDescription(hostDesc);
//        host.__set_ipAddresses(ipAddresses);
        //host.__set_hostAliases(hostAliases);
        // host.__set_computeResourceId("localhost_ad82d657-d0c2-4c91-87fc-7bee3cbd8284");
        
        // int airavata_port, airavata_timeout;
        // string airavata_server="";
        // char* cfgfile = "./airavata-client-properties.ini";;
        // readConfigFile(cfgfile, airavata_server, airavata_port, airavata_timeout);
        // airavata_server.erase(0,1);
        // airavata_server.erase(airavata_server.length()-1,1);
        // boost::shared_ptr<TSocket> socket(new TSocket(airavata_server, airavata_port));
        // socket->setSendTimeout(airavata_timeout);
        // boost::shared_ptr<TTransport> transport(new TBufferedTransport(socket));
        // boost::shared_ptr<TProtocol> protocol(new TBinaryProtocol(transport));
        // airavataClient = new AiravataClient(protocol);
        // transport->open();
        airavataClient->registerComputeResource(this->localhostId,host);
        // transport->close();

        qDebug() << "localhostId:" << QString::fromStdString(this->localhostId);
        ResourceJobManager resourceJobManager;
        map<JobManagerCommand::type, std::string> commandmap;
        // JobManagerCommand::type jobManagerCommandType = JobManagerCommandType::SUBMISSION;
        // commandmap[JobManagerCommand::SUBMISSION]="addLocalSubmissionDetails";
        resourceJobManager.__set_resourceJobManagerType(ResourceJobManagerType::FORK);
        resourceJobManager.__set_pushMonitoringEndpoint("");
        resourceJobManager.__set_jobManagerBinPath("");
        resourceJobManager.__set_jobManagerCommands(commandmap);


        LOCALSubmission localSubmission;
        localSubmission.__set_resourceJobManager(resourceJobManager);

        string submission = "";
        
        // transport->open();
        airavataClient->addLocalSubmissionDetails(submission,this->localhostId,1,localSubmission);
        // transport->close();

        qDebug() << "submission:" << QString::fromStdString(submission);
        qDebug() << "Localhost Resource Id is " << QString::fromStdString(this->localhostId);
    }
    catch(TException& e)
    {
        qDebug() << QString::fromStdString(e.what());
    }
}

void Register::registerGatewayProfile()
{
    try
    {
        DataMovementProtocol::type dataMovementProtocol;
        string scratchlocation = this->moduleDir + "/../tmp/qt5";
        
        char* cLocation = new char[scratchlocation.length()+ 1];
        strcpy(cLocation,scratchlocation.c_str());

        struct stat info;
        int err = stat(cLocation, &info);
        if(err!=-1 && S_ISDIR(info.st_mode))
            scratchlocation = this->moduleDir + "/../tmp/qt5";
        else
            scratchlocation = this->moduleDir + "/..";

        JobSubmissionProtocol::type jobSubmissionProtocol;
        string preferredBatchQueue;
        bool overridebyAiravata = false;
        string allocationProjectNumber = "Sample";
        string computeResourceId = this->localhostId;
        qDebug() << "ComputeResourceId:" << QString::fromStdString(computeResourceId);
        ComputeResourcePreference localhostResourcePreference;
        localhostResourcePreference.__set_computeResourceId(computeResourceId);
        localhostResourcePreference.__set_allocationProjectNumber(allocationProjectNumber);
        localhostResourcePreference.__set_overridebyAiravata(overridebyAiravata);
        localhostResourcePreference.__set_preferredBatchQueue(preferredBatchQueue);
        localhostResourcePreference.__set_preferredJobSubmissionProtocol(jobSubmissionProtocol);
        localhostResourcePreference.__set_preferredDataMovementProtocol(dataMovementProtocol);
        localhostResourcePreference.__set_scratchLocation(scratchlocation);

        GatewayResourceProfile gatewayResourceProfile;
        gatewayResourceProfile.__set_gatewayID(this->gatewayId);
        std::vector<ComputeResourcePreference> crpVector;
        crpVector.push_back(localhostResourcePreference);
        gatewayResourceProfile.__set_computeResourcePreferences(crpVector);
        string _registerGatewayResourceProfile="";
        airavataClient->registerGatewayResourceProfile(_registerGatewayResourceProfile,gatewayResourceProfile);
        qDebug() << "gateway profile registered:" << QString::fromStdString(_registerGatewayResourceProfile);
        
    }
    catch(TException e)
    {
        qDebug() << QString::fromStdString(e.what());
    }
}

void Register::registerApplicationModules() 
{
    try
    {
        for(std::vector<module>::iterator it = genapp_modules.begin(); it != genapp_modules.end(); it++)
        {
            string moduleName = it->name;
            //            moduleName = this->moduleDir + moduleName;
            string appModuleVersion = "1.0";
            string appModuleDescription = moduleName+" application description";
            ApplicationModule appModule;
            appModule.__set_appModuleName(moduleName);
            appModule.__set_appModuleVersion(appModuleVersion);
            appModule.__set_appModuleDescription(appModuleDescription);
            airavataClient->registerApplicationModule(it->moduleId,this->gatewayId,appModule);
            qDebug() << "module " << QString::fromStdString(moduleName) << "registered, id=" << QString::fromStdString(it->moduleId);
        }
    }
    catch(TException& e)
    {
        qDebug() << QString::fromStdString(e.what());
    }
}

void Register::registerApplicationDeployments()
{
    try
    {
        qDebug() << "#### Registering Genapp Modules on Localhost ####";

        for(std::vector<module>::iterator it = genapp_modules.begin(); it != genapp_modules.end(); it++)
        {
            string moduleName = it->name;
            string moduleId = it->moduleId;
            string moduleDeployId="";
            string computeResourceId = this->localhostId;
            string executablePath = this->moduleDir + "/" +it->id;
            qDebug() << "path->" << QString::fromStdString(executablePath);
            ApplicationDeploymentDescription applicationDeploymentDescription;
            applicationDeploymentDescription.__set_appModuleId(moduleId);
            applicationDeploymentDescription.__set_computeHostId(computeResourceId);
            applicationDeploymentDescription.__set_executablePath(executablePath);
            applicationDeploymentDescription.__set_parallelism(ApplicationParallelismType::SERIAL);
            applicationDeploymentDescription.__set_appDeploymentDescription(moduleName);
            airavataClient->registerApplicationDeployment(moduleDeployId,this->gatewayId,applicationDeploymentDescription);
            qDebug() << "Successfully registered " << QString::fromStdString(moduleName) << " application on localhost. Id= " << QString::fromStdString(moduleDeployId);
        }
    }
    catch(TException& e)
    {
        cout << e.what() << endl;
    }
}

void Register::registerApplicationInterfaces()
{   
    for(std::vector<module>::iterator it = genapp_modules.begin(); it != genapp_modules.end(); it++)
    {
        string moduleName = it->name;
        string moduleId = it->moduleId;
        registerApplicationInterface(moduleName,moduleId);
    }
}



void Register::registerApplicationInterface(string moduleName,string moduleId)
{
    try
    {
        qDebug() << "#### Registering " << QString::fromStdString(moduleName) << " Interface ####";

        vector<string> appModules;
        appModules.push_back(moduleId);

        InputDataObjectType input;
        string inputName = "Input_JSON";
        string inputValue = "{}";
        apache::airavata::model::appcatalog::appinterface::DataType::type type = apache::airavata::model::appcatalog::appinterface::DataType::STRING;
        string applicationArgument="";
        bool stdIn = false;
        string description = "JSON String";
        string metaData="";

        if(!inputName.empty())
            input.__set_name(inputName);
        if(!inputValue.empty())
            input.__set_value(inputValue);
        input.__set_type(type);
        if(!applicationArgument.empty())
            input.__set_applicationArgument(applicationArgument);
        input.__set_standardInput(stdIn);
        if(!description.empty())
            input.__set_userFriendlyDescription(description);
        if(!metaData.empty())
            input.__set_metaData(metaData);

        vector<InputDataObjectType> applicationInputs;
        applicationInputs.push_back(input);
        
        string outputName = "Output_JSON";
        string outputValue = "{}";
        
        OutputDataObjectType output;
        if(!outputName.empty())
            output.__set_name(outputName);
        if(!outputValue.empty())
            output.__set_value(outputValue);
        output.__set_type(type);

        std::vector<OutputDataObjectType> applicationOutputs;
        applicationOutputs.push_back(output);

        string ModuleInterfaceId = "";
        ApplicationInterfaceDescription applicationInterfaceDescription;
        applicationInterfaceDescription.__set_applicationName(moduleName);
        applicationInterfaceDescription.__set_applicationDescription(moduleName + " the inputs");
        applicationInterfaceDescription.__set_applicationModules(appModules);
        applicationInterfaceDescription.__set_applicationInputs(applicationInputs);
        applicationInterfaceDescription.__set_applicationOutputs(applicationOutputs);

        airavataClient->registerApplicationInterface(ModuleInterfaceId,this->gatewayId,applicationInterfaceDescription);
        genapp_mod[moduleName].interfaceId = ModuleInterfaceId;
        qDebug() << QString::fromStdString(moduleName) << " Module Interface Id: " << QString::fromStdString(genapp_mod[moduleName].interfaceId);
    }
    catch(TException& e)
    {
        qDebug() << QString::fromStdString(e.what());
    }
}

void Register::registerMain(Register* registerGenapp)
{
    try
    {
        qDebug() << "register.cpp" ;
        registerGenapp->init();
        qDebug() << "registerInit";
        registerGenapp->registerAll();
        qDebug() << "registerALl";
    }
    catch(AiravataClientException& e1)
    {
        qDebug() << "Cannot connect to server-" << QString::fromStdString(e1.what());
    }
    catch(TException& e2)
    {
        qDebug() << QString::fromStdString(e2.what());
    }
}

string Register::getGatewayId()
{ 
    return this->gatewayId;
}

string Register::getInterfaceId(string moduleName)
{
    if(genapp_mod.find(moduleName)==genapp_mod.end())
        return "Error: Module not found";
    else
        return genapp_mod[moduleName].interfaceId;
}

string Register::getComputeResourceId()
{
    qDebug() << "ComputeResourceId: " << QString::fromStdString(this->localhostId);
    return this->localhostId;
}

Register* Register::getInstance()
{
    if(!instanceFlag)
    {
        register_ = new Register();
        instanceFlag = true;
        return register_ ;
    }
    else
    {
        return register_;
    }
}

typedef struct {
    gchar *airavata_server, *app_catalog_server;
    gint airavata_port, app_catalog_port, airavata_timeout;
} Settings;

string gatewayId;

void readConfigFile(char* cfgfile, string& airavata_server, int& airavata_port, int& airavata_timeout) {

    airavata_server="'192.168.1.106'";
    airavata_port = 8930;
    airavata_timeout = 5000;

}

AiravataClient createAiravataClient()
{
    int airavata_port, airavata_timeout;
    string airavata_server;
    char* cfgfile;
    cfgfile = "./airavata-client-properties.ini";
    readConfigFile(cfgfile, airavata_server, airavata_port, airavata_timeout);
    airavata_server.erase(0,1);
    airavata_server.erase(airavata_server.length()-1,1);
    boost::shared_ptr<TSocket> socket(new TSocket(airavata_server, airavata_port));
    socket->setSendTimeout(airavata_timeout);
    boost::shared_ptr<TTransport> transport(new TBufferedTransport(socket));
    boost::shared_ptr<TProtocol> protocol(new TBinaryProtocol(transport));
    AiravataClient airavataClient(protocol);
    return airavataClient;
}

string createProject(char* owner, char* projectName)
{ 
    int airavata_port, airavata_timeout;
    string airavata_server;
    char* cfgfile;
    cfgfile = "./airavata-client-properties.ini";
    readConfigFile(cfgfile, airavata_server, airavata_port, airavata_timeout);
    airavata_server.erase(0,1);
    airavata_server.erase(airavata_server.length()-1,1);
    boost::shared_ptr<TSocket> socket(new TSocket(airavata_server, airavata_port));
    socket->setSendTimeout(airavata_timeout);
    boost::shared_ptr<TTransport> transport(new TBufferedTransport(socket));
    boost::shared_ptr<TProtocol> protocol(new TBinaryProtocol(transport));
    AiravataClient airavataClient(protocol);
    transport->open();
    // AiravataClient airavataClient = createAiravataClient();
    apache::airavata::model::workspace::Project project;
    project.owner=owner;
    project.name=projectName;
    string _return;
    Register* register_= Register::getInstance();
    gatewayId = register_->getGatewayId();
    qDebug() << "Gateway Id:" << QString::fromStdString(gatewayId);
    airavataClient.createProject(_return,gatewayId,project);
    transport->close();
    return _return;
}

string createExperiment(char* usrName, char* expName, char* projId, char* execId, char* inp)
{
    int airavata_port, airavata_timeout;
    string airavata_server;
    char* cfgfile;
    cfgfile = "./airavata-client-properties.ini";
    readConfigFile(cfgfile, airavata_server, airavata_port, airavata_timeout);
    airavata_server.erase(0,1);
    airavata_server.erase(airavata_server.length()-1,1);
    boost::shared_ptr<TSocket> socket(new TSocket(airavata_server, airavata_port));
    socket->setSendTimeout(airavata_timeout);
    boost::shared_ptr<TTransport> transport(new TBufferedTransport(socket));
    boost::shared_ptr<TProtocol> protocol(new TBinaryProtocol(transport));
    AiravataClient airavataClient(protocol);
    transport->open();
    Register* register_= Register::getInstance();
    ComputationalResourceScheduling cmRST;
    cmRST.__set_resourceHostId(register_->getComputeResourceId());
    cmRST.__set_computationalProjectAccount(usrName);
    cmRST.__set_totalCPUCount(1);
    cmRST.__set_nodeCount(1);
    cmRST.__set_numberOfThreads(1);
    cmRST.__set_queueName("normal");
    cmRST.__set_wallTimeLimit(30);
    cmRST.__set_jobStartTime(0);
    cmRST.__set_totalPhysicalMemory(1);


    UserConfigurationData userConfigurationData;
    userConfigurationData.__set_airavataAutoSchedule(false);
    userConfigurationData.__set_overrideManualScheduledParams(false);
    userConfigurationData.__set_computationalResourceScheduling(cmRST);


    
    char* appId = execId;


    InputDataObjectType input;
    input.__set_name("input");
    input.__set_value(inp);
    input.__set_type(DataType::STRING);
    std::vector<InputDataObjectType> exInputs;
    exInputs.push_back(input);
    OutputDataObjectType output;
    output.__set_name("output");
    output.__set_value("");
    output.__set_type(DataType::STDOUT);
    std::vector<OutputDataObjectType> exOutputs;
    exOutputs.push_back(output);


    char* user = usrName;
    char* exp_name = expName;
    char* proj = projId;

    Experiment experiment;
    experiment.__set_projectID(proj);
    experiment.__set_userName(user);
    experiment.__set_name(exp_name);
    experiment.__set_applicationId(appId);
    experiment.__set_userConfigurationData(userConfigurationData);
    experiment.__set_experimentInputs(exInputs);
    experiment.__set_experimentOutputs(exOutputs);

    string _return = "";
    
    airavataClient.createExperiment(_return, gatewayId, experiment);
    transport->close();
    return _return;
}

void launchExperiment(char* expId)
{
    try {
        int airavata_port, airavata_timeout;
        string airavata_server;
        char* cfgfile;
        cfgfile = "./airavata-client-properties.ini";
        readConfigFile(cfgfile, airavata_server, airavata_port, airavata_timeout);
        airavata_server.erase(0,1);
        airavata_server.erase(airavata_server.length()-1,1);
        boost::shared_ptr<TSocket> socket(new TSocket(airavata_server, airavata_port));
        socket->setSendTimeout(airavata_timeout);
        boost::shared_ptr<TTransport> transport(new TBufferedTransport(socket));
        boost::shared_ptr<TProtocol> protocol(new TBinaryProtocol(transport));
        AiravataClient airavataClient(protocol);
        transport->open();
        string tokenId = "-0bbb-403b-a88a-42b6dbe198e9";
        airavataClient.launchExperiment(expId, "airavataToken");
        qDebug() << "launched client experiment";
        transport->close();
    } catch (ExperimentNotFoundException e) {
        qDebug() << "Error occured while launching the experiment..." << QString::fromStdString(e.what());
        throw new ExperimentNotFoundException(e);
    } catch (AiravataSystemException e) {
        qDebug() << "Error occured while launching the experiment..." << QString::fromStdString(e.what());
        throw new AiravataSystemException(e);
    } catch (InvalidRequestException e) {
        qDebug() << "Error occured while launching the experiment..." << QString::fromStdString(e.what());
        throw new InvalidRequestException(e);
    } catch (AiravataClientException e) {
        qDebug() << "Error occured while launching the experiment..." << QString::fromStdString(e.what());
        throw new AiravataClientException(e);
    } catch (TException e) {
        qDebug() << "Error occured while launching the experiment..." << QString::fromStdString(e.what());
        throw new TException(e);
    }

}

int getExperimentStatus(char* expId)
{
    int airavata_port, airavata_timeout;
    string airavata_server;
    char* cfgfile;
    cfgfile = "./airavata-client-properties.ini";
    readConfigFile(cfgfile, airavata_server, airavata_port, airavata_timeout);
    airavata_server.erase(0,1);
    airavata_server.erase(airavata_server.length()-1,1);
    boost::shared_ptr<TSocket> socket(new TSocket(airavata_server, airavata_port));
    socket->setSendTimeout(airavata_timeout);
    boost::shared_ptr<TTransport> transport(new TBufferedTransport(socket));
    boost::shared_ptr<TProtocol> protocol(new TBinaryProtocol(transport));
    AiravataClient airavataClient(protocol);
    transport->open();
    ExperimentStatus _return;
    airavataClient.getExperimentStatus(_return, expId);
    transport->close();
    return _return.experimentState;

}

string getExperimentOutput(char* expId)
{
    int airavata_port, airavata_timeout;
    string airavata_server;
    char* cfgfile;
    cfgfile = "./airavata-client-properties.ini";
    readConfigFile(cfgfile, airavata_server, airavata_port, airavata_timeout);
    airavata_server.erase(0,1);
    airavata_server.erase(airavata_server.length()-1,1);
    boost::shared_ptr<TSocket> socket(new TSocket(airavata_server, airavata_port));
    socket->setSendTimeout(airavata_timeout);
    boost::shared_ptr<TTransport> transport(new TBufferedTransport(socket));
    boost::shared_ptr<TProtocol> protocol(new TBinaryProtocol(transport));
    AiravataClient airavataClient(protocol);
    transport->open();
    // AiravataClient airavataClient = createAiravataClient();
    std::vector<OutputDataObjectType> _return;
    string texpId(expId);
    airavataClient.getExperimentOutputs(_return, texpId);
    transport->close();
    return _return[0].value;

}

