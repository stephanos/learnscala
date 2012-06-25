#!/bin/bash
find ./ -iname '*.conf' -type f | xargs sed -i 's/_APP_VERSION_/'$1'/g'