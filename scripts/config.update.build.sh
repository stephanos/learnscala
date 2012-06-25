#!/bin/bash
sed -i 's/_BUILD_ENV_/'$1'/' project/*.scala
sed -i 's/_BUILD_APP_/'$2'/' project/*.scala