#!/bin/sh

# cleanup
rm -Rf api/dist/

# package
sbt "project app-api" "dist"

# unpack
cd api/dist/
unzip *.zip

# execute
cd app-api-0.2/
chmod +x start
./start -Dhttp.port=8080 -Dhttp.address=127.0.0.1 -Xms768M -Xmx768m -server