@echo off

if "%OW_HOME%" == "" set OW_HOME=%~dp0..
set BIN_DIR=%OW_HOME%\bin
set LIB_DIR=%OW_HOME%\lib
set TARGET_DIR=%OW_HOME%\target
set BUILD_DIR=%OW_HOME%\build

set CLASSPATH=%BUILD_DIR%;%TARGET_DIR%\overlayweaver.jar;%LIB_DIR%\je-4.1.10.jar;%LIB_DIR%\xmlrpc-common-3.1.3.jar;%LIB_DIR%\xmlrpc-server-3.1.3.jar;%LIB_DIR%\ws-commons-util-1.0.2.jar;%LIB_DIR%\commons-cli-1.2.jar;%LIB_DIR%\servlet-api-2.5.jar;%LIB_DIR%\jetty-server-7.3.1.v20110307.jar;%LIB_DIR%\jetty-servlet-7.3.1.v20110307.jar;%LIB_DIR%\jetty-util-7.3.1.v20110307.jar;%LIB_DIR%\jetty-http-7.3.1.v20110307.jar;%LIB_DIR%\jetty-security-7.3.1.v20110307.jar;%LIB_DIR%\jetty-io-7.3.1.v20110307.jar;%LIB_DIR%\jetty-continuation-7.3.1.v20110307.jar;%LIB_DIR%\clink200.jar
set LOGGING_CONFIG="%BIN_DIR%\logging.properties"

set JVM_OPTION=-Xss80k
rem set JVM_OPTION=-server -Xss80k -Xmx250m

java %JVM_OPTION% -Djava.util.logging.config.file=%LOGGING_CONFIG% ow.tool.dhtshell.Main %*
