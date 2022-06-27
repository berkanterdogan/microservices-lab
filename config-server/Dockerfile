FROM amazoncorretto:11.0.14
MAINTAINER berkanterdogan
COPY target/config-server-${CONFIG_SERVER_SERVICE_VERSION}.jar config-server-${CONFIG_SERVER_SERVICE_VERSION}.jar
ENTRYPOINT ["java","-jar","/config-server-$CONFIG_SERVER_SERVICE_VERSION.jar"]