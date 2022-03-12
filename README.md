# microservices-lab

## twitter-to-kafka-service
- It is a microservice project which streams tweets or mock tweets and produces these tweets as messages to a Kafka Topic. 
If you don't have an twitter developer account, you can stream mock tweets. There is some configurable environment variables for the project: 
  - **LOGGING_LEVEL_ROOT** -> Logback logging level for root. It's default value is "debug".
  - **LOGGING_LEVEL_COM_BERKANTERDOGAN_MICROSERVICES_LAB** -> Logback logging for main package. It's default value is "**debug**".
  - **MOCK_ENABLED** -> We can stream mock tweets and then produces these as Kafka messages. It's default value is "**true**".
  - **TWITTER4J_ENABLED** -> We can stream real tweets and then produces these as Kafka messages. It's default value is "**false**".
  - **TWITTER4J_DEBUG** -> We can log details about Twitter Stream by twitter4j library. It's default value is "**false**". If we want to more detailed info about Twitter Stream, we set it to true.
  - **TWITTER4J_OAUTH_CONSUMER_KEY** -> The value of this is our OAuth consumer key for Twitter Developer Account. There is no a default value for this environment variable.
  - **TWITTER4J_OAUTH_CONSUMER_SECRET** -> The value of this is our OAuth consumer secret for Twitter Developer Account. There is no a default value for this environment variable.
  - **TWITTER4J_OAUTH_ACCESS_TOKEN** -> The value of this is our OAuth access token for Twitter Developer Account. There is no a default value for this environment variable.
  - **TWITTER4J_OAUTH_ACCESS_TOKEN_SECRET** -> The value of this is our OAuth access token secret for Twitter Developer Account. There is no a default value for this environment variable.
  - **KAFKA_CONFIG_BOOTSTRAP_SERVERS** -> The value of this your Kafka bootstrap servers. It's default value is "**localhost:19092, localhost:29092, localhost:39092**".
  - **KAFKA_CONFIG_TOPIC_NAME** -> The value of this is the name of topic which Tweets as messages will be produced. It's default value is "**twitter-topic**".
  
- There are docker-compose configurations about the project in the docker-compose folder. Check files in this folder.
Before you run the project, you run `mvn clean install` command to build the project. After this command is completed successfully, 
a docker image of this project is created. You can check the image with `docker image ls` command. 
Now, you run `docker-compose up` command on the docker-compose folder and then we will see all containers started.