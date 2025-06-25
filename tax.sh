#!/bin/bash

TAX_TOPIC="tax-calculation-events"
(
  echo '{"amount":9999.0,"taxNumber":"12045479"}'
  echo '{"amount":100.0,"taxNumber":"12045479"}'
  echo '{"amount":200.0,"taxNumber":"12045479"}'
) | podman exec -i kafka /bin/kafka-console-producer --bootstrap-server localhost:9092 --topic $TAX_TOPIC
echo "TaxCalculationEvent messages sent."