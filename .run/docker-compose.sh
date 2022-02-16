#!/bin/bash
#cd ../docker-compose && docker-compose -f common.yml -f kafka_cluster.yml up
cd ../docker-compose && docker-compose -f common.yml -f kafka_cluster.yml -f services.yml up