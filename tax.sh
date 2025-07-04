#!/bin/bash

TAX_TOPIC="tax-calculation-events"
(
  echo '{"amount":200.0,"taxNumber":"16896778"}'
    echo '{"amount":300.0,"taxNumber":"16896978"}'
    echo '{"amount":500.0,"taxNumber":"16496988"}'
      echo '{"amount":9999.0,"taxNumber":"16896778"}'
  echo '{"amount":400.0,"taxNumber":"16896978"}'


) | podman exec -i kafka /bin/kafka-console-producer --bootstrap-server localhost:9092 --topic $TAX_TOPIC
echo "TaxCalculationEvent messages sent."