java -Xmx1024M -XX:+CMSClassUnloadingEnabled -XX:+UseConcMarkSweepGC -XX:MaxPermSize=1024M -XX:PermSize=128M -jar sbt-launch.jar "$@"