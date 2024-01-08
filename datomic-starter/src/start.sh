#!/bin/bash

# Starts datomic transactor
./bin/transactor -Ddatomic.printConnectionInfo=true config/dev-transactor.properties &

# Starts datomic console
./bin/console -p 8080 dev datomic:dev://localhost:4334/ &

wait -n

exit $?