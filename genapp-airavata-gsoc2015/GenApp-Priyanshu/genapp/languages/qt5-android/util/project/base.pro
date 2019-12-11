TEMPLATE = app
TARGET = psptest
CONFIG += qwt
CONFIG += console

INCLUDEPATH += `pkg-config --libs glib-2.0` ../lib
INCLUDEPATH += -I/usr/local/include/thrift
QMAKE_CXXFLAGS += -w -Wall -std=c++11 -Wno-write-strings -DHAVE_INTTYPES_H -DHAVE_NETINET_IN_H `pkg-config --cflags glib-2.0`
#LIBS +=  -L/usr/local/lib -lthrift

# Input
HEADERS += analyze.h \
           analyze_synctest.h \
           build.h \
           build_build_1.h \
           build_build_2.h \
           calculate.h \
           calculate_calculate_1.h \
           calculate_calculate_2.h \
           interact.h \
           interact_interact_1.h \
           interact_plottest.h \
           psptest.h \
           Register.h \
           simulate.h \
           simulate_simulate_1.h \
           simulate_simulate_2.h \
           tools.h \
           tools_align.h \
           tools_center.h \
           tools_data_interpolation.h \
           tools_filetest.h \
           utility_routines.h \
           Register.cpp \
           ../lib/thrift/transport/TTransport.h \
           ../lib/thrift/transport/TBufferTransports.h \
           ../lib/thrift/protocol/TProtocol.h \
           ../lib/thrift/protocol/TBinaryProtocol.h \
           ../lib/thrift/protocol/TBinaryProtocol.tcc \
           ../lib/thrift/TApplicationException.h \
           ../lib/thrift/transport/TTransportException.h \
           ../lib/thrift/protocol/TProtocolException.h \
           ../lib/thrift/transport/TSocket.h \
           ../lib/airavata/Airavata.h \
           ../lib/airavata/airavataAPI_types.h \
           ../lib/airavata/airavataErrors_types.h \
           ../lib/airavata/experimentModel_types.h \
           ../lib/airavata/computeResourceModel_types.h \
           ../lib/airavata/applicationInterfaceModel_types.h \
           ../lib/airavata/airavataDataModel_types.h \
           ../lib/airavata/workspaceModel_types.h \
           ../lib/airavata/messagingEvents_types.h \
           ../lib/airavata/applicationDeploymentModel_types.h \
           ../lib/airavata/gatewayResourceProfileModel_types.h \
           ../lib/airavata/workflowDataModel_types.h \
           ../lib/airavata/Airavata.cpp \
           ../lib/airavata/airavataDataModel_types.cpp \
           ../lib/airavata/airavataErrors_types.cpp \
           ../lib/airavata/experimentModel_types.cpp \
           ../lib/airavata/workspaceModel_types.cpp \
           ../lib/airavata/airavataAPI_types.cpp \
           ../lib/airavata/applicationDeploymentModel_types.cpp \
           ../lib/airavata/applicationInterfaceModel_types.cpp \
           ../lib/airavata/gatewayResourceProfileModel_types.cpp \
           ../lib/airavata/computeResourceModel_types.cpp \
           ../lib/jsoncons/json.hpp \
           ../lib/jsoncons/json1.hpp \
           ../lib/jsoncons/jsoncons.hpp \
           ../lib/jsoncons/jsoncons_config.hpp \
           ../lib/jsoncons/json_output_handler.hpp \
           ../lib/jsoncons/output_format.hpp \
           ../lib/jsoncons/json2.hpp \
           ../lib/jsoncons/json_type_traits.hpp \
           ../lib/jsoncons/json_structures.hpp \
           ../lib/jsoncons/json_reader.hpp \
           ../lib/jsoncons/json_input_handler.hpp \
           ../lib/jsoncons/parse_error_handler.hpp \
           ../lib/jsoncons/json_deserializer.hpp \
           ../lib/jsoncons/json_serializer.hpp
SOURCES += psptest.cpp \
           Register.cpp \
           utility_routines.cpp \
           ../lib/airavata/Airavata.cpp \
           ../lib/airavata/airavataAPI_types.cpp \
           ../lib/airavata/airavataErrors_types.cpp \
           ../lib/airavata/experimentModel_types.cpp \
           ../lib/airavata/computeResourceModel_types.cpp \
           ../lib/airavata/applicationInterfaceModel_types.cpp \
           ../lib/airavata/airavataDataModel_types.cpp \
           ../lib/airavata/workspaceModel_types.cpp \
           ../lib/airavata/messagingEvents_types.cpp \
           ../lib/airavata/applicationDeploymentModel_types.cpp \
           ../lib/airavata/gatewayResourceProfileModel_types.cpp \
           ../lib/airavata/workflowDataModel_types.cpp
RESOURCES += psptest.qrc

win32:CONFIG(release, debug|release): LIBS += -L$$PWD/../../../../../../../Downloads/qwt-6.1.2/lib/release/ -lqwt
else:win32:CONFIG(debug, debug|release): LIBS += -L$$PWD/../../../../../../../Downloads/qwt-6.1.2/lib/debug/ -lqwt
else:unix: LIBS += -L$$PWD/../../../../../../../Downloads/qwt-6.1.2/lib/ -lqwt


INCLUDEPATH += $$PWD/../../../../../../../Downloads/qwt-6.1.2
DEPENDPATH += $$PWD/../../../../../../../Downloads/qwt-6.1.2


contains(ANDROID_TARGET_ARCH,armeabi-v7a) {
    ANDROID_EXTRA_LIBS = \
        /home/priyanshu-sekhar/gsoc/new_airavata/new_genapp/psptest/output/qt5/psptest/../../../../../../../Downloads/qwt-6.1.2/lib/libqwt.so \
        /home/priyanshu-sekhar/gsoc/new_airavata/new_genapp/psptest/output/qt5/psptest/../../../../../../../Qt5/5.4/android_armv7/lib/libQt5PrintSupport.so \
        /home/priyanshu-sekhar/gsoc/new_airavata/new_genapp/psptest/output/qt5/psptest/../../../../../../../Qt5/5.4/android_armv7/lib/libQt5OpenGL.so \
        /home/priyanshu-sekhar/gsoc/new_airavata/new_genapp/psptest/output/qt5/psptest/../../../../../../../Qt5/5.4/android_armv7/lib/libQt5Concurrent.so \


DISTFILES += \
    android/AndroidManifest.xml \
    android/gradle/wrapper/gradle-wrapper.jar \
    android/gradlew \
    android/res/values/libs.xml \
    android/build.gradle \
    android/gradle/wrapper/gradle-wrapper.properties \
    android/gradlew.bat


ANDROID_PACKAGE_SOURCE_DIR = $$PWD/android


}


DISTFILES +=


win32:CONFIG(release, debug|release): LIBS += -L$$PWD/../../../../../../../../../opt/grandroid/lib/release/ -lthrift
else:win32:CONFIG(debug, debug|release): LIBS += -L$$PWD/../../../../../../../../../opt/grandroid/lib/debug/ -lthrift
else:unix: LIBS += -L$$PWD/../../../../../../../../../opt/grandroid/lib/ -lthrift

INCLUDEPATH += $$PWD/../../../../../../../../../opt/grandroid/include
DEPENDPATH += $$PWD/../../../../../../../../../opt/grandroid/include

win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../../../../../../../../../opt/grandroid/lib/release/libthrift.a
else:win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../../../../../../../../../opt/grandroid/lib/debug/libthrift.a
else:win32:!win32-g++:CONFIG(release, debug|release): PRE_TARGETDEPS += $$PWD/../../../../../../../../../opt/grandroid/lib/release/thrift.lib
else:win32:!win32-g++:CONFIG(debug, debug|release): PRE_TARGETDEPS += $$PWD/../../../../../../../../../opt/grandroid/lib/debug/thrift.lib
else:unix: PRE_TARGETDEPS += $$PWD/../../../../../../../../../opt/grandroid/lib/libthrift.a
