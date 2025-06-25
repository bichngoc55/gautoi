#!/bin/bash

PERSON_TOPIC="person-events"
(
  echo '{"eventType":"CREATE","person":{"taxNumber":"123456789","firstName":"EXCEPTION","lastName":"NONBLOCK","dateOfBirth":"2000-01-01"}}'
  echo '{"eventType":"CREATE","person":{"taxNumber":"12141600","firstName":"ABC","lastName":"ABC","dateOfBirth":"2014-05-05"}}'
  echo '{"eventType":"CREATE","person":{"taxNumber":"12354609","firstName":"DEF","lastName":"DEF","dateOfBirth":"2010-01-01"}}'
 ) | podman exec -it kafka /usr/bin/kafka-console-producer --bootstrap-server localhost:9092 --topic $PERSON_TOPIC

echo "PersonEvent messages sent."
