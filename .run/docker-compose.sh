#!/bin/bash

cd ..
#mvn clean install

#cd docker-compose && docker-compose -p ml-lab-clusters -f common.yml -f kafka_cluster.yml -f elastic_cluster.yml up
cd docker-compose && docker-compose -p ml-lab -f common.yml -f kafka_cluster.yml -f elastic_cluster.yml -f services.yml up