CC 	       = gcc
CXX		   = g++
DEPENDPATH += . thrift
INCLUDEPATH += . thrift
THRIFT_VER = thrift-0.9.2
USR_DIR    = ${HOME}/usr
CXXFLAGS      = -m64 -pipe -w -Wno-write-strings -DHAVE_INTTYPES_H -DHAVE_NETINET_IN_H `pkg-config --cflags glib-2.0` -O2 -Wall -W -D_REENTRANT $(DEFINES)
THRIFT_DIR = ${USR_DIR}/${THRIFT_VER}
INCS_DIRS  = -I../lib -I/usr/include/thrift -I/usr/local/include
LINK       = g++
LFLAGS 	   = -w -Wall -std=c++11 -Wno-write-strings -DHAVE_CONFIG_H -DHAVE_INTTYPES_H -DHAVE_NETINET_IN_H `pkg-config --cflags glib-2.0`
LIBS_DIRS  = -L/usr/local/lib 
CPP_DEFS   = -D=HAVE_CONFIG_H
CPP_OPTS   = -Wall -O2
LIBS       = `pkg-config --cflags glib-2.0` -L/usr/local/lib -lthrift 
DEL_FILE   = rm -f
GEN_SRC    = Register.cpp

####### Output directory

OBJECTS_DIR   = ./

####### Files

SOURCES 	= Register.cpp \
    airavata.cpp \
	../lib/thrift/transport/TSocket.cpp 

OBJECTS 	= Register.o \
    -o airavata.o \
    -o Thrift.o \
    -o TSocket.o \

	         
TARGET 		= register

$(TARGET):   
	$(LINK) ${INCS_DIRS} $(LFLAGS) $(OBJCOMP) $(GEN_SRC) -o $(OBJECTS) $(LIBS)

Thrift.o: thrift/Thrift.cpp thrift/Thrift.h
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o Thrift.o thrift/Thrift.cpp

TSocket.o: ../lib/thrift/transport/TSocket.cpp 
	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o TSocket.o ../lib/thrift/transport/TSocket.cpp

clean:
	-$(DEL_FILE) $(OBJECTS)
	-$(DEL_FILE) *~ core *.core


####### Compile

airavata.o: ../lib/thrift/transport/TTransport.h \
		../lib/thrift/transport/TSocket.h \
		../lib/thrift/protocol/TProtocol.h \
		../lib/thrift/protocol/TBinaryProtocol.h \
		../lib/thrift/protocol/TBinaryProtocol.tcc \
		../lib/thrift/protocol/TProtocolException.h \
		../lib/airavata/Airavata.h \
		../lib/airavata/airavataAPI_types.h \
		../lib/airavata/airavataErrors_types.h \
		../lib/airavata/experimentModel_types.h \
		../lib/airavata/computeResourceModel_types.h \
		../lib/airavata/airavataDataModel_types.h \
		../lib/airavata/Workflow.h \
		../lib/airavata/workflowAPI_types.h \
		../lib/airavata/workspaceModel_types.h \
		../lib/airavata/workflowAPI_constants.h \
		../lib/airavata/workflowDataModel_types.h \
		../lib/airavata/workspaceModel_constants.h \
		../lib/airavata/workflowDataModel_constants.h \
		../lib/airavata/applicationDeploymentModel_types.h \
		../lib/airavata/applicationInterfaceModel_types.h \
		../lib/airavata/gatewayResourceProfileModel_types.h \
		../lib/airavata/Airavata.cpp \
		../lib/airavata/airavataDataModel_types.cpp \
		../lib/airavata/airavataErrors_types.cpp \
		../lib/airavata/experimentModel_types.cpp \
		../lib/airavata/airavataAPI_types.cpp \
		../lib/airavata/applicationDeploymentModel_types.cpp \
		../lib/airavata/applicationInterfaceModel_types.cpp \
		../lib/airavata/gatewayResourceProfileModel_types.cpp \
		../lib/airavata/workflowAPI_types.cpp \
		../lib/airavata/Workflow.cpp \
		../lib/airavata/workspaceModel_types.cpp \
		../lib/airavata/workflowAPI_constants.cpp \
		../lib/airavata/workflowDataModel_types.cpp \
		../lib/airavata/workspaceModel_constants.cpp \
		../lib/airavata/workflowDataModel_constants.cpp \
		../lib/airavata/Workflow_server.skeleton.cpp \
		../lib/airavata/computeResourceModel_types.cpp
	$(CXX) -c $(CXXFLAGS) $(INCLUDEPATH) -o airavata.o airavata.cpp

