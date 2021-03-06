#FROM amazoncorretto:11.0.15

#https://github.com/corretto/corretto-docker/blob/main/11/jdk/debian/Dockerfile:
FROM debian:buster-slim

ARG version=11.0.15.9-1
# In addition to installing the Amazon corretto, we also install
# fontconfig. The folks who manage the docker hub's
# official image library have found that font management
# is a common usecase, and painpoint, and have
# recommended that Java images include font support.
#
# See:
#  https://github.com/docker-library/official-images/blob/master/test/tests/java-uimanager-font/container.java

RUN set -eux \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
        curl ca-certificates gnupg software-properties-common fontconfig java-common \
    && curl -fL https://apt.corretto.aws/corretto.key | apt-key add - \
    && add-apt-repository 'deb https://apt.corretto.aws stable main' \
    && mkdir -p /usr/share/man/man1 || true \
    && apt-get update \
    && apt-get install -y java-11-amazon-corretto-jdk=1:$version \
    && apt-get purge -y --auto-remove -o APT::AutoRemove::RecommendsImportant=false \
        curl gnupg software-properties-common

ENV LANG C.UTF-8
ENV JAVA_HOME=/usr/lib/jvm/java-11-amazon-corretto

MAINTAINER berkanterdogan
COPY target/kafka-to-elastic-service-0.0.1-SNAPSHOT.jar kafka-to-elastic-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/kafka-to-elastic-service-0.0.1-SNAPSHOT.jar"]